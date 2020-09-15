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


    @CommonLogicTest(functionNames = {"countEven"})
    public void exitsAfterFirstIncorrect(String functionBody) throws Exception {
        Object[] input = {new int[]{2,2,2,1,2}};
        int expected = 3;
        Object result = null;
        result = executeSingle(functionBody, functionName, input);
        testResultsPerExercise.get(functionName).put(getCurrentTestName(), result.equals(expected));
    }

    @CommonLogicTest(functionNames = {"countEven"})
    public void exitsAfterFirstCorrect(String functionBody) throws Exception {
        Object[] input = {new int[]{2,2}};
        int expected = 1;
        Object result = null;
        result = executeSingle(functionBody, functionName, input);
        testResultsPerExercise.get(functionName).put(getCurrentTestName(), result.equals(expected));
    }


    @CommonLogicTest(functionNames = {"countEven"})
    public void exitsAfterFirstIterationIncorrectCheck(String functionBody) throws Exception {
        Object[] evenInput = {new int[]{2,2}};
        int evenExpected = 0;
        Object evenresult = executeSingle(functionBody, functionName, evenInput);

        Object[] unevenInput = {new int[]{1,1}};
        int unevenExpected = 1;
        Object unevenresult = executeSingle(functionBody, functionName, unevenInput);

        testResultsPerExercise.get(functionName).put(getCurrentTestName(), (evenresult.equals(evenExpected) && unevenresult.equals(unevenExpected)));
    }
}