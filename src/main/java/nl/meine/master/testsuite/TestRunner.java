package nl.meine.master.testsuite;

import nl.meine.master.testsuite.tests.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestRunner {
    private List<CommonLogicLabel> testRunners = new ArrayList<>();
    private Map<String, String> mappingExerciseToFunction = new HashMap<>();
    protected InlineCompiler compiler = new InlineCompiler();
    protected Map<String, Class[]> parameterTypesPerExercise = new HashMap<>();


    public TestRunner(){
        mappingExerciseToFunction.put("1.even", "countEven");
        mappingExerciseToFunction.put("2.sumvalues", "sumValues");
        mappingExerciseToFunction.put("3.oddsum", "oddSum");
        mappingExerciseToFunction.put("4.score", "calculateScore");
        mappingExerciseToFunction.put("5.double", "hasDoubled");

        parameterTypesPerExercise.put("countEven", new Class[]{int[].class});
        parameterTypesPerExercise.put("oddSum", new Class[]{int[].class});
        parameterTypesPerExercise.put("sumValues", new Class[]{int[].class, boolean.class});
        parameterTypesPerExercise.put("hasDoubled", new Class[]{double.class, int.class});
        parameterTypesPerExercise.put("calculateScore", new Class[]{int.class, int.class});

        testRunners.add(new ForEachButIndexLabel(compiler));
        testRunners.add(new EarlyExitLabel(compiler));
        testRunners.add(new AlwaysAddLabel(compiler));
        testRunners.add(new OrInsteadOfAndLabel(compiler));
        testRunners.add(new IncorrectForEach(compiler));

        testRunners.forEach(runner -> {
            runner.setParameterTypesPerExercise(parameterTypesPerExercise);
        });
    }

    public boolean hasTestForExercise(String exercise){
        if(!mappingExerciseToFunction.containsKey(exercise)){
            return false;
        }
        AtomicBoolean hasTest = new AtomicBoolean(false);
        testRunners.forEach(runner -> {
            if(runner.hasTestForExercise(mappingExerciseToFunction.get(exercise))){
                hasTest.set(true);
            }
        });
        return hasTest.get();
    }

    public Set<Label> calculateLabel(String exercise, String functionBody) throws UncompilableException{
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
            Map<String, Integer> labelWithScore = new HashMap<>();
            Set<Label> labels = new HashSet<>();
            for (CommonLogicLabel runner: testRunners) {
                int runnerScore =runner.calcScore(functionName);
                String runnerLabel = runner.getLabel();
                if (runnerScore > score) {
                    score = runnerScore;
                    label = runnerLabel;
                }
                if(runnerScore != 0){
                    labelWithScore.put(runnerLabel, runnerScore);
                }
            }

            int finalScore = score;
            labelWithScore.forEach((l, s) -> {
                if(s == finalScore){
                    labels.add(new Label(l, s));
                }
            });
            return labels;
        } catch (UncompilableException e) {
          throw e;
        }catch (Exception e){
            System.err.println("Error during running" + e.getLocalizedMessage());
        }
        return null;
    }

}
