/*
 * Copyright (c) 2016- syu_chan_1005
 * Released under the MIT license
 * http://opensource.org/licenses/mit-license.php
 */

package co.honobono.honobonoserver.constructor;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import co.honobono.honobonoserver.constructor.RegistManager.AddCommand;

public class RegistManager {
	private Plugin pl;
	private Map<String, Map<Base, Method>> Commands = new HashMap<String, Map<Base, Method>>();

	/**
	 * Create new instance
	 * @param plugin OwnerPlugin
	 * @throws IOException
	 */
	public RegistManager(Plugin plugin) throws ReflectiveOperationException, IOException {
		this.pl = plugin;
		JarFile jar = null;
		try {
			jar = new JarFile(
					this.getPlugin().getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
			for (Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements();) {
				String className = e.nextElement().getName();
				if (!className.endsWith(".class")) continue;
				Class<?> clazz = Class.forName(className.replace('/', '.').substring(0, className.length() - 6));
				if (hasListener(clazz)) {
					this.getPlugin().getServer().getPluginManager().registerEvents((Listener) this.getInstance(clazz),
							this.getPlugin());
				}
				Arrays.stream(clazz.getMethods())
						.filter(m -> hasCommand(m) != null)
						.forEach(m -> this.putMap(new Base(hasCommand(m)), m));
			}
		} finally {
			jar.close();
		}
	}

	/**
	 * Processing Command.
	 * @param sender Command Sender
	 * @param command Command
	 * @param args Argument
	 * @return Result
	 */
	public boolean run(CommandSender sender, Command command, String[] args) throws ReflectiveOperationException {
		if (Commands.containsKey(command.getName().toLowerCase())) {
			if (args.length == 0) args = new String[] { "" };
			Map<Base, Method> map = Commands.get(command.getName().toLowerCase());
			for (Map.Entry<Base, Method> e : map.entrySet()) {
				String sub = args[0];
				if (e.getKey().getName().equalsIgnoreCase(command.getName()) && (e.getKey().getSubCommand().equals(sub)
						|| e.getKey().getAliases().stream().anyMatch(a -> a.equalsIgnoreCase(sub)))) {
					if (sender.hasPermission(e.getKey().getPermission())) {
						return (boolean) e.getValue().invoke(this.getInstance(e.getValue().getDeclaringClass()), sender,
								command, args);
					} else {
						sender.sendMessage(e.getKey().getPermissionMessage());
						return true;
					}
				}
			}
		}
		return false;
	}

	public void registerCommand(String command, String description, String usageMessage, String permission,
			String permissionMessage) throws ReflectiveOperationException {
		this.registerCommand(command, description, usageMessage, permission, permissionMessage, new String[0]);
	}

	/**
	 * Register Bukkit Command.
	 * @throws ReflectiveOperationException
	 */
	public void registerCommand(String command, String description, String usageMessage, String permission,
			String permissionMessage, String... aliases) throws ReflectiveOperationException {
		Base base = new Base(command, description, usageMessage, Arrays.asList(aliases), permission,
				permissionMessage);
		this.getCommandMap().register(this.getPlugin().getName(), base.toPluginCommand(this));
	}

	/**
	 * Send the help.
	 * @param sender Command Sender
	 * @param command Command
	 */
	public void sendHelpMessage(CommandSender sender, String command) {
		if (Commands.containsKey(command.toLowerCase())) {
			StringBuilder sb = new StringBuilder();
			sb.append("-----Help-----\n");
			for (Map.Entry<Base, Method> e : Commands.get(command.toLowerCase()).entrySet()) {
				Base base = e.getKey();
				String sub = base.getSubCommand();
				if (!(sub.isEmpty() || sub.equalsIgnoreCase("help"))) {
					sb.append(sub);
					if (base.getAliases().size() >= 1) {
						sb.append('(');
						for (String alias : base.getAliases()) {
							sb.append(alias);
							sb.append(',');
						}
						sb.setLength(sb.length() - 1);
						sb.append(')');
					}
					sb.append(" : ");
					sb.append(base.getUsage());
					sb.append("\n");
				}
			}
			sender.sendMessage(sb.toString());
		}
	}

	/**
	 * Return the owner plugin.
	 */
	public Plugin getPlugin() {
		return this.pl;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object getInstance(Class clazz) throws ReflectiveOperationException {
		if (clazz.isInstance(this.getPlugin())) return this.getPlugin();
		for (Constructor con : clazz.getDeclaredConstructors()) {
			con.setAccessible(true);
			Class[] clazzes = con.getParameterTypes();
			if (clazzes.length == 1 && (clazzes[0].isAssignableFrom(JavaPlugin.class) || clazzes[0].isAssignableFrom(this.getPlugin().getClass())))
				return con.newInstance(this.getPlugin());
		}
		return clazz.newInstance();
	}

	private void putMap(Base base, Method method) {
		if (Commands.containsKey(base.getName())) {
			Commands.get(base.getName()).put(base, method);
		} else {
			Map<Base, Method> map = new HashMap<>();
			map.put(base, method);
			Commands.put(base.getName(), map);
		}
	}

	@SuppressWarnings("rawtypes")
	private static AddCommand hasCommand(Method method) {
		Class clazz[] = method.getParameterTypes();
		if (clazz.length == 3 && clazz[0] == CommandSender.class && clazz[1] == Command.class
				&& clazz[2] == String[].class && method.getReturnType()
						.equals(boolean.class)) { return (AddCommand) method.getAnnotation(AddCommand.class); }
		return null;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface AddCommand {
		String Command();

		String subCommand() default "";

		String[] Aliases() default {};

		String Description() default "Not added";

		String Permission() default "Not added";

		String PermissionMessage() default "You don't have permission.";

		String Usage() default "Not added";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static boolean hasListener(Class clazz) {
		AddListener listener = (AddListener) clazz.getAnnotation(AddListener.class);
		if (listener == null || !listener.value()) return false;
		return Arrays.stream(clazz.getInterfaces()).anyMatch(c -> c.equals(Listener.class));
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface AddListener {
		boolean value() default true;
	}

	private SimpleCommandMap getCommandMap() throws ReflectiveOperationException {
		Server server = this.getPlugin().getServer();
		Field f = server.getClass().getDeclaredField("commandMap");
		f.setAccessible(true);
		return (SimpleCommandMap) f.get(this.getPlugin().getServer());
	}

}

class Base extends Command {
	private String subCommand;

	public Base(String command, String description, String usageMessage, List<String> aliases, String Permission,
			String PermissionMessage) {
		super(command, description, usageMessage, aliases);
		this.setPermission(Permission);
		this.setPermissionMessage(PermissionMessage);
		this.setSubCommand("");
	}

	public Base(AddCommand a) {
		super(a.Command(), a.Description(), a.Usage(), Arrays.asList(a.Aliases()));
		this.setPermission(a.Permission());
		this.setPermissionMessage(a.PermissionMessage());
		this.setSubCommand(a.subCommand());
	}

	public String getSubCommand() {
		return subCommand;
	}

	public void setSubCommand(String subCommand) {
		this.subCommand = subCommand;
	}

	public PluginCommand toPluginCommand(RegistManager manager) throws ReflectiveOperationException {
		Constructor<?> cs = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
		cs.setAccessible(true);
		PluginCommand cmd = (PluginCommand) cs.newInstance(this.getName(), manager.getPlugin());
		cmd.setDescription(this.getDescription());
		cmd.setUsage(this.getUsage());
		cmd.setAliases(this.getAliases());
		cmd.setPermission(this.getPermission());
		cmd.setPermissionMessage(this.getPermissionMessage());
		cmd.setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command cmd, String commandName, String[] args) {
				try {
					return manager.run(sender, cmd, args);
				} catch (ReflectiveOperationException e) {
					e.printStackTrace();
					return true;
				}
			}
		});
		return cmd;
	}

	@Override
	public String getName() {
		return super.getName().toLowerCase();
	}

	@Override
	public Base clone() {
		try {
			return (Base) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		return true;
	}

}