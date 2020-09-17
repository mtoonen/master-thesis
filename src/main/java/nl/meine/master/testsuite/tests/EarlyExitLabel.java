package nl.meine.master.testsuite.tests;


import nl.meine.master.testsuite.CommonLogicLabel;
import nl.meine.master.testsuite.CommonLogicTest;
import nl.meine.master.testsuite.InlineCompiler;

public class EarlyExitLabel extends CommonLogicLabel {

    public Class[] paramType = null;

    public EarlyExitLabel(InlineCompiler compiler) {
        label = "earlyexit";
        this.compiler = compiler;
        addFunction("countEven", new Class[]{int[].class});
        addFunction("hasDoubled", new Class[]{double.class, int.class});
    }

  //  @CommonLogicTest(functionNames = {"countEven"})
    public void exitsAfterFirstIncorrect(String functionBody) throws Exception {
        Object[] input = {new int[]{2,2,2,1,2}};
        int expected = 3;
        Object result = null;
        result = executeSingle( input);
        addTestScore(result.equals(expected));
    }

    //@CommonLogicTest(functionNames = {"countEven"})
    public void exitsAfterFirstCorrect(String functionBody) throws Exception {
        Object[] input = {new int[]{2,2}};
        int expected = 1;
        Object result = null;
        result = executeSingle( input);
        addTestScore(result.equals(expected));
    }

  //  @CommonLogicTest(functionNames = {"countEven"})
    public void exitsAfterFirstIterationIncorrectCheck(String functionBody) throws Exception {
        Object[] evenInput = {new int[]{2,2}};
        int evenExpected = 0;
        Object evenresult = executeSingle(evenInput);

        Object[] unevenInput = {new int[]{1,1}};
        int unevenExpected = 1;
        Object unevenresult = executeSingle(unevenInput);

        addTestScore((evenresult.equals(evenExpected) && unevenresult.equals(unevenExpected)));
    }


    @CommonLogicTest(functionNames = {"hasDoubled"})
    public void hasDoubled(String functionBody) throws Exception {
        Object[] evenInput = {2.0, 2};
        int evenExpected = 0;
        Object evenresult = executeSingle( evenInput);

        addTestScore((evenresult.equals(evenExpected)));
    }

}