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
        //clean();
        read(args);
    }

    public static void clean(){
        String sql = "select TIME,exerciseid, serviceinfo,input, output,label " +
                "from studyrequests "+
            "where serviceinfo like 'NotEquivalent%'";


        try (Connection conn = DB.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                String ts = rs.getString("TIME");
                String inputString = rs.getString("input");
                String label = rs.getString("label");
                JSONObject input = new JSONObject(inputString);
                JSONArray params = input.getJSONArray("params");
                if(!params.isEmpty()){
                    String bodyOutside = params.getString(1);
                    System.out.println(ts);
                    //System.out.println(bodyOutside);

                 /*   String update = "update studyrequests set submittedfunction = '"+bodyOutside+"' where time = '" + ts +"';";

                    Statement updateStmt  = conn.createStatement();
                    int res    = updateStmt.executeUpdate(update);
                    if(res != 1){
                        int a = 0;
                    }*/
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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
               // System.out.println(String.format("Calculating userid %s with time %s",userid, ts));
                Label ownLabel = tr.calculateLabel(exercise, submittedfunction);
                if(ownLabel != null){
                    System.out.println( String.format("Time %s, dblabel %s, ownlabel %s - confidence %d", ts, label, ownLabel.getLabel(), ownLabel.getConfidence()));
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int countEven(int [] values)
    {
        int count = 0;
        for (int i : values)
        {
            if (values[i] % 2 == 0)
            {
                count++;
            }
        }
        return count;
    }
}
