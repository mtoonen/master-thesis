package nl.meine.master.testsuite.tests;


import nl.meine.master.testsuite.CommonLogicLabel;
import nl.meine.master.testsuite.CommonLogicTest;
import nl.meine.master.testsuite.InlineCompiler;

public class EarlyExitLabel extends CommonLogicLabel {

    public EarlyExitLabel(InlineCompiler compiler) {
        label = "earlyexit";
        this.compiler = compiler;
        init();
    }

    @CommonLogicTest(functionNames = {"countEven"})
    public void exitsAfterFirstIncorrect(String functionBody) throws Exception {
        Object[] input = {new int[]{2,2,2,1,2}};
        int expected = 3;
        Object result = null;
        result = executeSingle( input);
        addTestScore(result.equals(expected));
    }

    @CommonLogicTest(functionNames = {"countEven"})
    public void exitsAfterFirstCorrect(String functionBody) throws Exception {
        Object[] input = {new int[]{2,2}};
        int expected = 1;
        Object result = null;
        result = executeSingle( input);
        addTestScore(result.equals(expected));
    }

    @CommonLogicTest(functionNames = {"countEven"})
    public void exitsAfterFirstIterationIncorrectCheck(String functionBody) throws Exception {
        Object[] evenInput = {new int[]{2,2}};
        int evenExpected = 0;
        Object evenresult = executeSingle(evenInput);

        Object[] unevenInput = {new int[]{1,1}};
        int unevenExpected = 1;
        Object unevenresult = executeSingle(unevenInput);

        addTestScore((evenresult.equals(evenExpected) && unevenresult.equals(unevenExpected)));
    }

    @CommonLogicTest(functionNames = {"countEven"})
    public void exitsAfterFirstUnevenNumber(String functionBody) throws Exception {
        Object[] evenInput = {new int[]{2,1}};
        int evenExpected = 1;
        Object evenresult = executeSingle(evenInput);

        Object[] unevenInput = {new int[]{1,1}};
        int unevenExpected = 0;
        Object unevenresult = executeSingle(unevenInput);

        addTestScore((evenresult.equals(evenExpected) && unevenresult.equals(unevenExpected)));
    }

    @CommonLogicTest(functionNames = {"hasDoubled"})
    public void hasDoubledFirstIteration(String functionBody) throws Exception {
        Object[] evenInput = {1000.0, 2};
        int evenExpected = 1;
        Object result = executeSingle( evenInput);

        addTestScore((result.equals(evenExpected)));
    }

    @CommonLogicTest(functionNames = {"oddSum"})
    public void oddSumFirstIteration(String functionBody) throws Exception {
        Object[] evenInput = {new int[] {1,16,2,3}};
        int evenExpected = 16;
        Object result = executeSingle( evenInput);

        addTestScore((result.equals(evenExpected)));
    }

}