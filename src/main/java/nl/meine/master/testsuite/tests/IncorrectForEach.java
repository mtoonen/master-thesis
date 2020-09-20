package nl.meine.master.testsuite.tests;

import nl.meine.master.testsuite.CommonLogicLabel;
import nl.meine.master.testsuite.CommonLogicTest;
import nl.meine.master.testsuite.InlineCompiler;

public class IncorrectForEach extends CommonLogicLabel {

    public IncorrectForEach(InlineCompiler compiler) {
        label = "incorrectforeach";
        this.compiler = compiler;
        init();
    }

    @CommonLogicTest(functionNames = {"oddSum"})
    public void checkValueInsteadOfIndex(String functionBody) throws Exception {
        Object[] input = {new int[]{1, -1, 1, 1, 1,}};
        Object result = executeSingle(input);
        addTestScore(result.equals(-1));
    }

}
