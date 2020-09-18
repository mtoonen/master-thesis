package nl.meine.master.testsuite.tests;

import nl.meine.master.testsuite.CommonLogicLabel;
import nl.meine.master.testsuite.CommonLogicTest;
import nl.meine.master.testsuite.InlineCompiler;

public class AlwaysAddLabel extends CommonLogicLabel {
    public AlwaysAddLabel(InlineCompiler compiler) {
        label = "alwaysadd";
        this.compiler = compiler;
        init();
    }


    @CommonLogicTest(functionNames = {"sumValues"})
    public void noIF(String functionBody) throws Exception {
        Object[] input = {new int[]{1,2,3,4}, true};
        int expected = 10;
        Object result = null;
        result = executeSingle( input);


        Object[] input2 = {new int[]{1,2,3,4}, false};
        Object result2 = null;
        result2 = executeSingle( input2);

        addTestScore( (result.equals(expected) && (result2.equals(expected))));
    }

    @CommonLogicTest(functionNames = {"sumValues"})
    public void incorrectContraction(String functionBody) throws Exception {
        Object[] input = {new int[]{-5}, true};
        int expected = -5;
        Object result = null;
        result = executeSingle( input);

        addTestScore( (result.equals(expected)));
    }


    @CommonLogicTest(functionNames = {"sumValues"})
    public void extraCheckForPositive(String functionBody) throws Exception {
        Object[] input = {new int[]{-5,-5}, false};
        int expected = 0;
        Object result = null;
        result = executeSingle( input);

        addTestScore( (result.equals(expected)));
    }

    @CommonLogicTest(functionNames = {"sumValues"})
    public void extraCheckForPositive2(String functionBody) throws Exception {
        Object[] input = {new int[]{-5,-5}, true};
        int expected = 0;
        Object result = null;
        result = executeSingle( input);

        addTestScore( (result.equals(expected)));
    }
}
