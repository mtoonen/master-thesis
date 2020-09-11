package nl.meine.master.testsuite.tests;


import nl.meine.master.testsuite.CommonLogicTest;
import nl.meine.master.testsuite.CommonLogicLabel;
import nl.meine.master.testsuite.InlineCompiler;

public class EarlyExitLabel extends CommonLogicLabel {

    public Class[] paramType = null;
    public String functionName = null;

    public EarlyExitLabel(InlineCompiler compiler) {
        paramType = new Class[]{int[].class};
        functionName = "countEven";
        label = "earlyexit";
        init(functionName, paramType,compiler);
    }

    @CommonLogicTest(exercises = {"countEven"})
    public void earlyExit(String functionBody) throws Exception {
        Object[] input = {new int[]{2,2}};
        int expected = 0;
        Object result = null;
        result = executeSingle(functionBody, functionName, input);
        testResultsPerExercise.get(functionName).put(getCurrentTestName(), result.equals(expected));
    }
}