
package comp3415.telehealth.db;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import com.mysql.jdbc.Driver;
import java.sql.DriverManager;



public class MySQLConnections {

    public static Connection getConnection() throws Exception
    {
        String url = AppInfo.DB_HOST; //credentials for external host

        String hostUsername = "6Skh2ICVv6"; // Host credentials
        String hostPassword = "u3mslv6gsd";

        Class.forName("com.mysql.cj.jdbc.Driver"); // registering driver
        Connection sqlConnection = (Connection)DriverManager.getConnection(url, hostUsername, hostPassword); //SQL Connection

        return sqlConnection;
    }

    /**
     * Creates a password hash out of the input
     * @param inputPassword the password to be hashed.
     * @return the hashed password as a String
     */
    public static String getPasswordHash(String inputPassword)
    {
        String hash = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(inputPassword.getBytes());
            byte[] digest = md.digest();
            //Converting the byte array in to HexString format
            StringBuffer hexString = new StringBuffer();
            for (int i = 0;i<digest.length;i++) {
                hexString.append(Integer.toHexString(0xFF & digest[i]));
            }
            hash = String.valueOf(hexString);
        } catch (NoSuchAlgorithmException e) { // if the getInstance argument is invalid
            e.printStackTrace();
        }

        return hash;
    }


}
