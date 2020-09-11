package nl.meine.master.reader;

import nl.meine.master.testsuite.Label;
import nl.meine.master.testsuite.TestRunner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args){
        read(args);
    }

    public static void read(String[] args){
        String sql = "select *" +
                "from trainingset "
                //+ "where label ='foreachbutindex' order by time"
                ;

        TestRunner tr =new TestRunner();

        try (Connection conn = DB.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                String ts = rs.getString("TIME");
                String label = rs.getString("label");
                String exercise = rs.getString("exerciseid");
                String submittedfunction = rs.getString("submittedfunction");
                String userid = rs.getString("userid");
                Label ownLabel = tr.calculateLabel(exercise, submittedfunction);
                if(ownLabel != null){
                    System.out.println( String.format("Time %s, dblabel %s, ownlabel %s - confidence %d", ts, label, ownLabel.getLabel(), ownLabel.getConfidence()));
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
