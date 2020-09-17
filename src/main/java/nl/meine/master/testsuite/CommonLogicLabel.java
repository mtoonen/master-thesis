package nl.meine.master.testsuite;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Interface to be implemented by all classes that identify a common logic error
 */
public abstract class CommonLogicLabel {
    protected String label;
    protected Map<String, List<Method>> submissionsPerExercise = new HashMap<>();
    protected Map<String, Map<String, Boolean>> testResultsPerExercise = new HashMap<>();
    protected Map<String, Class[]> parameterTypesPerExercise = new HashMap<>();

    protected InlineCompiler compiler;

    protected String currentFunctionName;

    protected boolean hasTestForExercise(String exercise){
        return submissionsPerExercise.containsKey(exercise);
    }

    protected void addFunction(String functionName, Class[] parameterTypes) {
        testResultsPerExercise.put(functionName, new HashMap<>());
        parameterTypesPerExercise.put(functionName, parameterTypes);

        // look which methods have the CommonLogicTest annotation. These are unittests for Common Logic Errors
        Class<? extends Annotation> annotation = CommonLogicTest.class;
        for (final Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                // Check for which exercises the tests are available
                CommonLogicTest a = (CommonLogicTest) method.getAnnotation(annotation);
                for (String forFunctionName : a.functionNames()) {
                    if(!forFunctionName.equals(functionName)){
                        continue;
                    }
                    if (!submissionsPerExercise.containsKey(functionName)) {
                        submissionsPerExercise.put(functionName, new ArrayList<>());
                    }
                    submissionsPerExercise.get(functionName).add(method);
                }
            }
        }
    }

    public void reset(){
        submissionsPerExercise.forEach((exercise, methods) -> {
            Map<String, Boolean> results = new HashMap<>();
            methods.forEach(method -> {
                results.put(method.getName(), false);
            });
            testResultsPerExercise.put(exercise, results);
        });

    }

    public void runall(String body, String functionName){
        this.currentFunctionName = functionName;
        List<Method> methods = submissionsPerExercise.get(functionName);
        if(methods !=null) {
            methods.forEach(test -> {
                try {
                    test.invoke(this, body);
                } catch (Exception e) {
                 //   e.printStackTrace();
                    int a =0;
                }
            });
        }

    }

    protected String getCurrentTestName(){
        StackWalker walker = StackWalker.getInstance();
        Optional<String> methodName = walker.walk(frames -> frames
                .skip(2)
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        return methodName.get();
    }

    protected Object executeSingle( Object[] input)throws Exception{
        return compiler.execute( this.currentFunctionName, input, parameterTypesPerExercise.get(this.currentFunctionName));
    }

    public String getLabel(){
        return label;
    }

    public int calcScore(String exercise) {
        AtomicInteger score = new AtomicInteger(100);
        if(!testResultsPerExercise.containsKey(exercise) || testResultsPerExercise.get(exercise).isEmpty()){
            return 0;
        }
        Map<String, Boolean> tests = testResultsPerExercise.get(exercise);
        int scorePerTest = score.get() / tests.size();
        tests.forEach((s, passed) -> {
            if (!passed) {
                score.addAndGet(-scorePerTest);
            }
        });
        return score.intValue();

    }

    public void addTestScore(boolean success){
        testResultsPerExercise.get(this.currentFunctionName).put(getCurrentTestName(), success);
    }
}
