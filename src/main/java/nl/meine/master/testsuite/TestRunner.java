package nl.meine.master.testsuite;

import nl.meine.master.testsuite.tests.EarlyExitLabel;
import nl.meine.master.testsuite.tests.ForEachButIndexLabel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunner {
    private List<CommonLogicLabel> testRunners = new ArrayList<>();
    private Map<String, String> mappingExerciseToFunction = new HashMap<>();
    protected InlineCompiler compiler = new InlineCompiler();


    public TestRunner(){
        mappingExerciseToFunction.put("1.even", "countEven");
        testRunners.add(new ForEachButIndexLabel(compiler));
        testRunners.add(new EarlyExitLabel(compiler));
    }

    public String calculateLabel(String exercise, String functionBody){
        String functionName = mappingExerciseToFunction.get(exercise);
        if(functionName == null){
            return null;
        }
        try {
            compiler.init(functionBody);
            testRunners.forEach(runner -> {
                runner.reset();
                runner.runall(functionBody,functionName);
            });
            compiler.tearDown();

            int score = 0;
            String label = "";
            for (CommonLogicLabel runner: testRunners) {
                int runnerScore =runner.calcScore(functionName);
                if (runnerScore > score) {
                    label = runner.getLabel();
                    score = runnerScore;
                }
            }
            return label;
        } catch (UncompilableException e) {
          // cannot compile
            int a = 0;
        }catch (Exception e){
            System.err.println("Error during running" + e.getLocalizedMessage());
        }
        return null;
    }
}
