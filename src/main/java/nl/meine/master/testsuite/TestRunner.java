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
        testRunners.add(new NoElseClause(compiler));

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

    public List<String> getUnittestNames(){
        List<String> tests = new ArrayList<>();
        for (CommonLogicLabel runner: testRunners) {
            runner.submissionsPerExercise.forEach((ex, methods) -> {
                methods.forEach(method -> {
                    tests.add(method.getName());
                });
            });
        }
        Collections.sort(tests);
        return tests;
    }

    public Map<String,Boolean> executeTests(String exercise, String functionBody) throws UncompilableException{
        Map<String,Boolean> unitTestsResults = new HashMap<>();
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
                runner.submissionsPerExercise.forEach((ex, methods) -> {
                    runner.testResultsPerExercise.get(ex).forEach((unitTestName, fired) -> {
                        unitTestsResults.put(unitTestName, fired);
                    });
                });
            }

            int finalScore = score;
            labelWithScore.forEach((l, s) -> {
                if(s == finalScore){
                    labels.add(new Label(l, s));
                }
            });
        } catch (UncompilableException e) {
          throw e;
        }catch (Exception e){
            System.err.println("Error during running" + e.getLocalizedMessage());
        }
        return unitTestsResults;
    }


    public Set<Label>  calculateLabelAdhoc(String exercise, String functionBody) throws UncompilableException{
        Map<String,Boolean> unitTestsResults = new HashMap<>();
        String functionName = mappingExerciseToFunction.get(exercise);
        if(functionName == null){
            return null;
        }
        Set<Label> labels = new HashSet<>();
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
            for (CommonLogicLabel runner: testRunners) {
               /* runner.submissionsPerExercise.forEach((ex, methods) -> {
                    runner.testResultsPerExercise.get(ex).forEach((unitTestName, fired) -> {
                        unitTestsResults.put(unitTestName, fired);
                    });
                });*/

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
        } catch (UncompilableException e) {
            throw e;
        }catch (Exception e){
            System.err.println("Error during running" + e.getLocalizedMessage());
        }
        return labels;
    }

}
