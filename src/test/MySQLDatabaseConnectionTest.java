package test;

import java.sql.SQLException;
import java.util.ArrayList;

import de.mb.database.SQLAnswerTable;
import de.mb.database.mysql.MysqlConnectionFactory;
import de.mb.database.mysql.MysqlSQLExecution;

/**
 * Testclass
 *
 * @author Marco Behnke
 */
public class MySQLDatabaseConnectionTest {

	public static void main(String[] args) {
		try {
			String url = "localhost";
			String dbname = "usr_web4_2";
			MysqlConnectionFactory f = new MysqlConnectionFactory(url, dbname);
			MysqlSQLExecution exec = new MysqlSQLExecution(f.getConnection("web4",
					"sw666#GHf"));

			// one way: Give Query
			SQLAnswerTable t = exec.executeQuery("SELECT * FROM insel");
			System.out.println(t.toString(true));

			// second way
//			ArrayList fields = new ArrayList();
//			fields.add("id");
//			fields.add("name");
//			ArrayList from = new ArrayList();
//			from.add("insel");
//			t = exec.executeQuery(fields, from);
//			System.out.println(t.toString(true));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}
}
