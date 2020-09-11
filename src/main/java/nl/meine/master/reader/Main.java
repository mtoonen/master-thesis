package nl.meine.master.reader;

import nl.meine.master.testsuite.Label;
import nl.meine.master.testsuite.TestRunner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Main m = new Main();
        m.process();
    }

    public void process() {
        String sql = "select *" +
                "from trainingset "
                + "where " +
                //"label ='foreachbutindex' " +
                " exerciseid = '1.even' order by time";

        TestRunner tr = new TestRunner();

        try (Connection conn = DB.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int total = 0;
            int totalCorrect = 0;
            int totalFound = 0;


            Map<String, Integer> totalMap = new HashMap<>();
            Map<String, Integer> totalCorrectMap = new HashMap<>();
            Map<String, Integer> totalFoundMap = new HashMap<>();

            // loop through the result set
            while (rs.next()) {
                total++;
                String ts = rs.getString("TIME");
                String dbLabel = rs.getString("label");
                String exercise = rs.getString("exerciseid");
                String submittedfunction = rs.getString("submittedfunction");
                String userid = rs.getString("userid");

                totalMap.put(dbLabel,totalMap.getOrDefault(dbLabel, 0)+1);

                Label ownLabel = tr.calculateLabel(exercise, submittedfunction);
                if (ownLabel != null) {

                    totalFoundMap.put(dbLabel,totalFoundMap.getOrDefault(dbLabel, 0)+1);

                    totalFound++;
                    if (ownLabel.getConfidence() == 0) {
                        tr.calculateLabel(exercise, submittedfunction);
                    }
                    if(ownLabel.getLabel().equals(dbLabel)){
                        totalCorrectMap.put(dbLabel,totalCorrectMap.getOrDefault(dbLabel, 0)+1);

                        totalCorrect++;
                    }
                    //System.out.println(String.format("Time %s, dblabel %s, ownlabel %s - confidence %d", ts, label, ownLabel.getLabel(), ownLabel.getConfidence()));
                }
            }
            System.out.println("Precision: " + calculatePrecision(totalCorrect, totalFound));
            System.out.println("Recall: " + calculateRecall(totalCorrect, total));

            calculateRelevance(totalCorrectMap, totalFoundMap, totalMap);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void calculateRelevance(Map<String,Integer> totalCorrectMap,Map<String,Integer> totalFoundMap,Map<String,Integer> totalMap){
        System.out.println("******************************");
        System.out.println("Relevance");
        totalMap.forEach((label, total) -> {
            int totalCorrect = totalCorrectMap.getOrDefault(label,0);
            int totalFound = totalFoundMap.getOrDefault(label, 0);
            double precision =  calculatePrecision( totalCorrect, totalFound);
            double recall = calculateRecall(totalCorrect, total);
            System.out.format("Label %s: precision: %f, recall: %f \n", label, precision, recall);
        });
        System.out.println("******************************");

    }

    public Double calculatePrecision(int totalCorrect, int totalFound) {
        return totalCorrect / (double)totalFound;
    }

    public Double calculateRecall( int totalCorrect,int total) {
        return totalCorrect / (double)total;
    }
}
