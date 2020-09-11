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

    protected void init(String exercise, Class[] parameterTypes, InlineCompiler compiler) {
        testResultsPerExercise.put(exercise, new HashMap<>());
        parameterTypesPerExercise.put(exercise, parameterTypes);

        Class<? extends Annotation> annotation = CommonLogicTest.class;
        for (final Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                CommonLogicTest a = (CommonLogicTest) method.getAnnotation(annotation);
                for (String ex : a.exercises()) {

                    if (!submissionsPerExercise.containsKey(exercise)) {
                        submissionsPerExercise.put(exercise, new ArrayList<>());
                    }
                    submissionsPerExercise.get(exercise).add(method);
                }
            }
        }
        this.compiler = compiler;
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
        List<Method> methods = submissionsPerExercise.get(functionName);
        if(methods !=null) {
            methods.forEach(test -> {
                try {
                    test.invoke(this, body);
                } catch (Exception e) {
                 //   e.printStackTrace();
                }
            });
        }

    }

    protected String getCurrentTestName(){
        StackWalker walker = StackWalker.getInstance();
        Optional<String> methodName = walker.walk(frames -> frames
                .skip(1)
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        return methodName.get();
    }

    protected Object executeSingle(String functionbody,String functionName,  Object[] input)throws Exception{
        return compiler.execute( functionName, input, parameterTypesPerExercise.get(functionName));
    }

    public String getLabel(){
        return label;
    }

    public int calcScore(String exercise) {
        AtomicInteger score = new AtomicInteger(100);
        Map<String, Boolean> tests = testResultsPerExercise.get(exercise);
        if(tests.isEmpty()){
            return 0;
        }
        int scorePerTest = score.get() / tests.size();
        tests.forEach((s, passed) -> {
            if (!passed) {
                score.addAndGet(-scorePerTest);
            }
        });
        return score.intValue();

    }
}
