package nl.meine.master.testsuite.tests;

import nl.meine.master.testsuite.CommonLogicLabel;
import nl.meine.master.testsuite.CommonLogicTest;
import nl.meine.master.testsuite.InlineCompiler;

import java.lang.reflect.InvocationTargetException;

public class NoElseClause extends CommonLogicLabel {

    public NoElseClause(InlineCompiler compiler) {
        label = "noelseclause";
        this.compiler = compiler;
        init();
    }

    @CommonLogicTest(functionNames = {"sumValues"})
    public void onlyPositives(String functionBody) throws Exception {
        Object[] input = {new int[]{1, -1, -1, 2,3}, true};
        Object result = executeSingle(input);
        addTestScore(result.equals(6));
    }

}
