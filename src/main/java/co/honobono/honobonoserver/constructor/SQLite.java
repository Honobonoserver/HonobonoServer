package co.honobono.honobonoserver.constructor;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class SQLite {

	public static enum Casts {
		INTEGER("INTEGER"),
		TEXT("TEXT"),
		BLOB("BLOB"),
		REAL("REAL"),
		NUMERIC("NUMERIC"),
		TIMESTAMP("TIMESTAMP"),
		DATA("DATA"),
		NULL("NULL"),
		NOTNULL("NOT NULL"),
		UNIQUE("UNIQUE"),
		PRIMARYKEY("PRIMARY KEY"),
		AUTOINCREMENT("AUTOINCREMENT");

		final private String sql;

		private Casts(final String sql) {
			this.sql = sql;
		}

		@Override
		public String toString() {
			return this.sql;
		}

		public static Casts[] toArray(Casts... type) {
			return type;
		}
	}

	private Statement state;

	/**
	 * インスタンス生成メソッド
	 *
	 * @param file DBファイルへのパス
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public SQLite(File file) throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		Connection con = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
		this.state = con.createStatement();
	}

	/**
	 * テーブルを作成します。すでにある場合には作成せず終了します
	 *
	 * @param table テーブル名
	 * @param map   String: カラム名   Casts[]: 対応する変数の型
	 * @throws SQLException
	 */
	public void create(String table, LinkedHashMap<String, Casts[]> map) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS \"" + table + "\"(";
		for (Map.Entry<String, Casts[]> e : map.entrySet()) {
			sql += "\"" + e.getKey() + "\" ";
			for (Casts s : e.getValue()) {
				sql += s.toString() + " ";
			}
			sql += ",";
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ");";
		executeUpdate(sql);
	}

	/**
	 * テーブルに項目を挿入します。すでにある場合はそれを削除し新規で挿入します
	 *
	 * @param table       テーブル名
	 * @param firstcolumn 検索対象のカラム名
	 * @param firstvalue  検索対象の値
	 * @param values      検索対象を除く他の値
	 * @throws SQLException
	 */
	public void checkPut(String table, String firstcolumn, Object firstvalue, Object... values) throws SQLException {
		delete(table, firstcolumn, firstvalue);
		Object[] o = new Object[values.length + 1];
		o[0] = firstvalue;
		for (int i = 1; i < o.length; i++)
			o[i] = values[i - 1];
		put(table, o);
	}

	/**
	 * テーブルに値を挿入します
	 *
	 * @param table テーブル名
	 * @param obj   挿入する値
	 * @throws SQLException
	 */
	public void put(String table, Object... obj) throws SQLException {
		String sql = "INSERT INTO " + table + " VALUES(";
		for (Object o : obj) {
			sql += "\"" + o + "\",";
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ");";
		executeUpdate(sql);
	}

	/**
	 * テーブルの値を変更します
	 *
	 * @param table          テーブル名
	 * @param setcolumnname  変更するカラム名
	 * @param setcolumnvalue 変更する値
	 * @param wherecolumn    検索対象のカラム名
	 * @param wherevalue     検索対象の値
	 * @throws SQLException
	 */
	public void set(String table, String setcolumnname, String setcolumnvalue, String wherecolumn, String wherevalue)
			throws SQLException {
		String sql = "UPDATE " + table + " SET " + setcolumnname + " = " + "\"" + setcolumnvalue + "\" where "
				+ wherecolumn + " = " + wherevalue;
		executeUpdate(sql);
	}

	/**
	 * テーブルから値を取得します
	 *
	 * @param table  テーブル名
	 * @param column カラム名
	 * @return カラムの値一覧
	 * @throws SQLException
	 */
	public ResultSet get(String table, String[] column) throws SQLException {
		String sql = "SELECT ";
		for (String c : column)
			sql += "\"" + c + "\",";
		sql = sql.substring(0, sql.length() - 1) + " from " + table;
		return executeQuery(sql);
	}

	/**
	 * テーブルから値を取得します
	 *
	 * @param table       テーブル名
	 * @param wherecolumn 検索対象のカラム名
	 * @param wherevalue  検索対象の値
	 * @param getcolumn   取得対象のカラム名
	 * @return カラムの値一覧
	 * @throws SQLException
	 */
	public ResultSet get(String table, String wherecolumn, Object wherevalue, String... getcolumn) throws SQLException {
		String sql = "SELECT ";
		for (String s : getcolumn)
			sql += "\"" + s + "\",";
		sql = sql.substring(0, sql.length() - 1);
		sql += " from " + table + " where \"" + wherecolumn + "\" = \"" + wherevalue + "\";";
		return executeQuery(sql);
	}

	/**
	 * テーブルの値を削除します
	 *
	 * @param table       テーブル名
	 * @param column      削除対象のカラム名
	 * @param columnvalue 削除対象の値
	 * @throws SQLException
	 */
	public void delete(String table, String column, Object columnvalue) throws SQLException {
		String sql = "DELETE FROM " + table + " WHERE \"" + column + "\" == \"" + columnvalue + "\"";
		executeUpdate(sql);
	}

	/**
	 * テーブルに対してSQL文を送信します。返り値がない場合はこちらを使用してください。
	 *
	 * @param sql 実行するSQL文
	 * @throws SQLException
	 */
	public void executeUpdate(String sql) throws SQLException {
		state.executeUpdate(sql);
	}

	/**
	 * テーブルに対してSQL文を送信します。返り値がある場合はこちらを使用してください。
	 *
	 * @param sql 実行するSQL文
	 * @return 実行結果のResultSet
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		return state.executeQuery(sql);
	}

	/**
	 * テーブルのその値があるかどうか判定します。
	 *
	 * @param table       テーブル名
	 * @param countcolumn 数えるカラム名
	 * @param value       数えるカラムの値
	 * @return 値が0: true それ以外: false
	 * @throws SQLException
	 */
	@Deprecated
	public boolean noValue(String table, String countcolumn, Object value) throws SQLException {
		String sql = "select count(\"" + countcolumn + "\") where \"" + countcolumn + "\" = \"" + value + "\""
				+ " from " + table;
		ResultSet rs = executeQuery(sql);
		rs.next();
		int i = rs.getInt(1);
		return (i == 0);
	}

	/**
	 * Casts.DATA, TimeStamp, Timeに相当するTimeStampを取得します
	 *
	 * @return FormatされたStringの値
	 */
	public static String getTimeStamp() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
		return simpleDateFormat.format(date);
	}
}
