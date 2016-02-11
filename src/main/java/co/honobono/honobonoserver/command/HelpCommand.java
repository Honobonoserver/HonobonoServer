package co.honobono.honobonoserver.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import co.honobono.honobonoserver.HonobonoServer;
import co.honobono.honobonoserver.constructor.RegistManager.AddCommand;

public class HelpCommand {

	@AddCommand(Command = "hn")
	public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
		sender.sendMessage("You enter the /hn help Command.");
		return true;
	}

	@AddCommand(Command = "hn", subCommand = "help")
	public boolean onCommand2(CommandSender sender, Command cmd, String[] args) {
		HonobonoServer.getManager().sendHelpMessage(sender, "hn");
		return true;
	}

}
