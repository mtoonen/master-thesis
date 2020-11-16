package nl.meine.master.reader;

import nl.meine.master.testsuite.Label;
import nl.meine.master.testsuite.TestRunner;
import nl.meine.master.testsuite.UncompilableException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Main m = new Main();
        m.process();
        //m.extractSubmitted();
    }

    public void extractSubmitted() {
        String sql = "select TIME,exerciseid, serviceinfo,input, output,label " +
                "from studyrequests " +
                "where serviceinfo like 'Buggy%'";


        try (Connection conn = DB.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                String ts = rs.getString("TIME");
                String inputString = rs.getString("input");
                String label = rs.getString("label");
                JSONObject input = new JSONObject(inputString);
                JSONArray params = input.getJSONArray("params");
                if (!params.isEmpty()) {
                    String bodyOutside = params.getString(1);
                    // System.out.println(ts);
                    //System.out.println(bodyOutside);

                    String update = "update studyrequests set submittedfunction = '" + bodyOutside + "' where time = '" + ts + "';";
                    Statement updateStmt = conn.createStatement();
                    int res = updateStmt.executeUpdate(update);
                    if (res != 1) {
                        int a = 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    int total = 0;
    int totalCorrect = 0;
    int totalFound = 0;

    Map<String, Integer> totalMap = new HashMap<>();
    Map<String, Integer> totalCorrectMap = new HashMap<>();
    Map<String, Integer> totalFoundMap = new HashMap<>();

    public void process() throws IOException {
        String sql = "select *" +
                "from trainingset "
                + "where "
                // + "label like '%incorrectf%'"
                + " (label like '%earlyexit%'"
                + " OR label like '%foreachbutindex%'"
                + " OR label like '%alwaysused%'"
                + " OR label like '%missingcase%'"
                + " OR label like '%orinsteadofand%'"
                + " OR label like '%incorrectforeach%') and not label like '%onlypositive%'"
             /*   + "label in (" +
                "'earlyexit'," +
                " 'foreachbutindex'," +
                "'alwaysadd'," +
                "'noelseclause'," +
                "'orinsteadofand'," +
                    "'incorrectforeach'" +
                ") "*/
                // + "and TIME = '2019-10-14 08:03:55.081647'"
                //+" and exerciseid = '4.score' order by time"
                ;
        String[] labelspresent = {
                "earlyexit",
                "foreachbutindex",
                "alwaysused",
                "missingcase",
                "orinsteadofand",
                "incorrectforeach"
        };
        TestRunner tr = new TestRunner();

        FileWriter out = new FileWriter("/home/meine/Dropbox/Studie/Open Universiteit/Afstuderen/Orange/features.csv");
        List<String> sortedKeys = tr.getUnittestNames();

        Collections.sort(sortedKeys);


        sortedKeys.add(0, "time");
        sortedKeys.add(1, "exercise1");
        sortedKeys.add(2, "exercise2");
        sortedKeys.add(3, "exercise3");
        sortedKeys.add(4, "exercise4");
        sortedKeys.add(5, "exercise5");
        sortedKeys.add(6, "exerciseid");
        sortedKeys.add(7, "targetlabel");
        for (int i = 0; i < labelspresent.length; i++) {
            String l = labelspresent[i];
            sortedKeys.add(8 + i, l);
        }
        try (Connection conn = DB.connect();
             Statement stmt = conn.createStatement();
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(sortedKeys.toArray(new String[]{})));
             ResultSet rs = stmt.executeQuery(sql)) {

            Set<String> exercisesNotChecked = new HashSet<>();
            // loop through the result set
            while (rs.next()) {
                String ts = rs.getString("TIME");
                String[] dbLabels = rs.getString("label").split(",");
                String exercise = rs.getString("exerciseid");
                String submittedfunction = rs.getString("submittedfunction");
                String userid = rs.getString("userid");

                Set<Label> ownLabels = null;
                try {
                    addSubmission(dbLabels);
                    //ownLabel = tr.calculateLabel(exercise, submittedfunction);

                    if (!tr.hasTestForExercise(exercise)) {
                        exercisesNotChecked.add(exercise);
                        continue;
                    }

                    Map<String, Boolean> results = tr.executeTests(exercise, submittedfunction);
                    ownLabels = tr.calculateLabelAdhoc(exercise, submittedfunction);
                    printer.print(ts);
                    writeExercise(exercise, printer);
                    printer.print(exercise);
                    printer.print(String.join(",", dbLabels));
                    writeLabels(dbLabels, printer, labelspresent);

                    sortedKeys.forEach(unitTest -> {
                        try {
                            if (results.containsKey(unitTest)) {
                                printer.print(results.get(unitTest));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    printer.println();
                    int a = 0;

                    if (ownLabels != null && ownLabels.size() != 0) {
                        addFound(ownLabels, dbLabels);
                        addCorrect(dbLabels, ownLabels);
                    } else {
                        int b = 0;
                    }

                } catch (UncompilableException e) {
                    subtractFound(dbLabels);
                }

            }
            System.out.println("******************************");
            System.out.println("Overall relevance: ");
            System.out.println("Precision: " + calculatePrecision(totalCorrect, totalFound));
            System.out.println("Recall: " + calculateRecall(totalCorrect, total));

            calculateRelevance(totalCorrectMap, totalFoundMap, totalMap);

            System.out.println("Exercises not checked: " + exercisesNotChecked);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void writeLabels(String[] dbLabels, CSVPrinter printer, String[] labelspresent) throws IOException {
        String[] l = new String[]{"", "", "" };
        for (String possibleLabel : labelspresent) {
            boolean found = false;
            for (String dbLabel : dbLabels) {
                if(dbLabel.equals(possibleLabel)){
                    found = true;
                    break;
                }
            }
            printer.print(found);
        }
    }

    private void writeExercise(String exerciseId, CSVPrinter printer) throws IOException {
        boolean[] exercises = new boolean[5];
        for (int i = 0; i < 5; i++) {
            if (exerciseId.contains("" + (i + 1))) {
                exercises[i] = true;
            }
            printer.print(exercises[i]);
        }
    }


    private void addCorrect(String[] dbLabels, Set<Label> own) {
        for (String dbLabel : dbLabels) {
            own.forEach(ownLabel -> {
                if (ownLabel.getLabel().equals(dbLabel)) {
                    totalCorrectMap.put(dbLabel, totalCorrectMap.getOrDefault(dbLabel, 0) + 1);
                    totalCorrect++;
                } else {
                    int a = 0;
                }
            });
        }
    }

    private void addSubmission(String[] dbLabels) {
        for (String dbLabel : dbLabels) {
            totalMap.put(dbLabel, totalMap.getOrDefault(dbLabel, 0) + 1);
            total++;
        }
    }

    private void subtractFound(String[] dbLabels) {
        for (String dbLabel : dbLabels) {
            total--;
            totalMap.put(dbLabel, totalMap.getOrDefault(dbLabel, 0) - 1);
        }
    }

    private void addFound(Set<Label> own, String[] dbLabels) {
        own.forEach(label -> {
            totalFoundMap.put(label.getLabel(), totalFoundMap.getOrDefault(label.getLabel(), 0) + 1);
            totalFound++;
        });
    }

    public void calculateRelevance(Map<String, Integer> totalCorrectMap, Map<String, Integer> totalFoundMap, Map<String, Integer> totalMap) {
        System.out.println("******************************");
        System.out.println("Relevance per label");
        totalMap.forEach((label, total) -> {
            int totalCorrect = totalCorrectMap.getOrDefault(label, 0);
            int totalFound = totalFoundMap.getOrDefault(label, 0);
            double precision = calculatePrecision(totalCorrect, totalFound);
            double recall = calculateRecall(totalCorrect, total);
            System.out.format("Label %s: precision: %f, recall: %f \n", label, precision, recall);
        });
        System.out.println("******************************");

    }

    public Double calculatePrecision(int totalCorrect, int totalFound) {
        if (totalFound == 0) {
            return 0.0;
        }
        return totalCorrect / (double) totalFound;
    }

    public Double calculateRecall(int totalCorrect, int total) {
        return totalCorrect / (double) total;
    }

}
