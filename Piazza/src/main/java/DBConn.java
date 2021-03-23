import java.sql.*;
import java.util.Properties;

/**
 * Abstract method used by all controllers class to communicate with the database.
 *
 * (Denne klassen er hentet fra EKT-eksemplet vist under forelesning 5.03.21)
 * @author Svein Erik
 */
public abstract class DBConn {
  protected Connection conn;
  protected void connect() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      // Properties for user and password.
      Properties p = new Properties();
      p.put("user", "root");
      p.put("password", "Piazza123");
      conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/DB2?allowPublicKeyRetrieval=true&autoReconnect=true&useSSL=false",p);
    } catch (Exception e)
    {
      throw new RuntimeException("Unable to connect", e);
    }
  }
}


