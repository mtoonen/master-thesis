package nl.meine.master.testsuite.tests;

import nl.meine.master.testsuite.CommonLogicLabel;
import nl.meine.master.testsuite.CommonLogicTest;
import nl.meine.master.testsuite.InlineCompiler;

import java.lang.reflect.InvocationTargetException;

public class IncorrectForEach extends CommonLogicLabel {

    public IncorrectForEach(InlineCompiler compiler) {
        label = "incorrectforeach";
        this.compiler = compiler;
        init();
    }

    @CommonLogicTest(functionNames = {"oddSum"})
    public void dontCheckOnOddIndices(String functionBody) throws Exception {
        Object[] input = {new int[]{1, 1, -1, 1, 1,}};
        Object result = executeSingle(input);
        addTestScore(result.equals(2));
    }

    @CommonLogicTest(functionNames = {"oddSum"})
    public void dontCheckBounds(String functionBody) throws Exception {
        Object[] input = {new int[]{1}};
        try {
            Object result = executeSingle(input);
        } catch (Exception e) {
            if (((InvocationTargetException) e).getTargetException() instanceof ArrayIndexOutOfBoundsException) {
                addTestScore(true);
            }
        }
    }

    @CommonLogicTest(functionNames = {"oddSum"})
    public void tooEarlyUpdatingIndices(String functionBody) throws Exception {
        Object[] input = {new int[]{1,2}};
        try {
            Object result = executeSingle(input);
        } catch (Exception e) {
            if (((InvocationTargetException) e).getTargetException() instanceof ArrayIndexOutOfBoundsException) {
                addTestScore(true);
            }
        }
    }
}
