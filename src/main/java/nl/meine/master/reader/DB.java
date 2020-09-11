package nl.meine.master.reader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    /**
     * Connect to a sample database
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            // db parametersx-special/nautilus-clipboard
            //copy
            //file:///home/meine/Dropbox/Studie/Open%20Universiteit/Afstuderen/Data/RQ1/ideas-2019-v2-rpt.db
            String url = "jdbc:sqlite:/home/meine/Dropbox/Studie/Open Universiteit/Afstuderen/Data/RQ1/ideas-2019-v2-rpt.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

}
