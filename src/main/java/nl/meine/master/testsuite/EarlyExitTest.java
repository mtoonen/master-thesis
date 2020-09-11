package nl.meine.master.testsuite;


public class EarlyExitTest extends CommonErrorTestRunner {

    public Class[] paramType = null;
    public String functionName = null;

    public EarlyExitTest(InlineCompiler compiler) {
        paramType = new Class[]{int[].class};
        functionName = "countEven";
        label = "earlyexit";
        init(functionName, paramType,compiler);
    }

    @CommonErrorTest(exercises = {"countEven"})
    public void earlyExit(String functionBody) throws Exception {
        Object[] input = {new int[]{2,2}};
        int expected = 0;
        Object result = null;
        result = executeSingle(functionBody, functionName, input);
        testResultsPerExercise.get(functionName).put(getCurrentTestName(), result.equals(expected));
    }
}