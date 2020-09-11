package nl.meine.master.testsuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunner {
    private List<CommonErrorTestRunner> testRunners = new ArrayList<>();
    private Map<String, String> mappingExerciseToFunction = new HashMap<>();
    protected InlineCompiler compiler = new InlineCompiler();


    public TestRunner(){
        mappingExerciseToFunction.put("1.even", "countEven");
        testRunners.add(new ForEachButIndexTest(compiler));
        testRunners.add(new EarlyExitTest(compiler));
    }

    public String calculateLabel(String exercise, String functionBody){
        String functionName = mappingExerciseToFunction.get(exercise);
        if(functionName == null){
            return null;
        }
        try {
            testRunners.forEach(runner -> {
                runner.reset();
                runner.runall(functionBody,functionName);
            });

            int score = 0;
            String label = "";
            for (CommonErrorTestRunner runner: testRunners) {
                int runnerScore =runner.calcScore(functionName);
                if (runnerScore > score) {
                    label = runner.getLabel();
                    score = runnerScore;
                }
            }
            return label;
     //   } catch (UncompilableException e) {
          // cannot compile
        //    int a = 0;
        }catch (Exception e){
            System.err.println("Error during running" + e.getLocalizedMessage());
        }
        return null;
    }
}