package nl.meine.master.testsuite.tests;

import nl.meine.master.testsuite.CommonLogicLabel;
import nl.meine.master.testsuite.CommonLogicTest;
import nl.meine.master.testsuite.InlineCompiler;

import java.lang.reflect.InvocationTargetException;


public class ForEachButIndexLabel extends CommonLogicLabel {

    public Class[] paramType = null;

    public ForEachButIndexLabel(InlineCompiler compiler) {
        label = "foreachbutindex";
        this.compiler = compiler;
        addFunction("countEven", new Class[]{int[].class});
        addFunction("oddSum", new Class[]{int[].class});
        addFunction("sumValues", new Class[]{int[].class, boolean.class});
    }

    @CommonLogicTest(functionNames = {"countEven"})
    public void exceptionTest(String functionBody) {
        /**
         * This test is based on the premise that when using the value to retrieve a value from the array on that index
         * (the value being the index), we get an ArrayIndexOutOfBoundsException when defining an array of length 1
         * with the first element being > 1. This will let the function retrieve a value outside the bounds
         */
        Object[] input = {new int[]{2}};
        try {
            Object result = executeSingle(input);
        } catch (Exception e) {
            if (((InvocationTargetException) e).getTargetException() instanceof ArrayIndexOutOfBoundsException) {
                addTestScore(true);
            }
        }
    }

    @CommonLogicTest(functionNames = {"countEven"})
    public void exceptionTestMinus(String functionBody) {
        /**
         * This test is based on the premise that when using the value to retrieve a value from the array on that index
         * (the value being the index), we get an ArrayIndexOutOfBoundsException when defining an array of length 1
         * with the first element being > 1. This will let the function retrieve a value outside the bounds
         */
        Object[] input = {new int[]{-2}};
        try {
            Object result = executeSingle(input);
        } catch (Exception e) {
            if (((InvocationTargetException) e).getTargetException() instanceof ArrayIndexOutOfBoundsException) {
                addTestScore(true);
            }
        }
    }

    @CommonLogicTest(functionNames = {"countEven"})
    public void countSpecificIndices(String functionBody) throws Exception {
        /**
         * This test looks if the value is used to look up a value in the array. We set all the values to 2, except on the 3rd index,
         * that one is 1. When the label foreachbutindex should apply, when checking the 3rd value (1), it will retrieve the value on
         * values[1], which is 2. All other values result in check the value 1 for evenness. Result should be that the function counts
         * only one instance of an even number
         */
        Object[] input = {new int[]{2, 2, 1, 2, 2}};
        Object result = executeSingle(input);
        addTestScore(result.equals(1));
    }

    @CommonLogicTest(functionNames = {"countEven"})
    public void countSpecificIndices2(String functionBody) throws Exception {
        /**
         * This test looks if the value is used to look up a value in the array. We set all the values to 2, except on the 3rd index,
         * that one is 1. When the label foreachbutindex should apply, when checking the 3rd value (1), it will retrieve the value on
         * values[1], which is 2. All other values result in check the value 1 for evenness. Result should be that the function counts
         * only one instance of an even number
         */
        Object[] input = {new int[]{0, 0, 0, 0, 1,}};
        Object result = executeSingle(input);
        addTestScore(result.equals(5));
    }

    @CommonLogicTest(functionNames = {"countEven"})
    public void countOnlyUneven(String functionBody) throws Exception {
        /**
         * This test looks if the value is used to look up a value in the array. We set all the values to 1. It will always check
         * values[1], which has value 1, which is uneven.
         */
        Object[] input = {new int[]{1, 1, 1, 1}};
        Object result = executeSingle(input);
        addTestScore(result.equals(4));
    }

    @CommonLogicTest(functionNames = {"sumValues"})
    public void sumValuesAll(String functionBody) throws Exception {
        /**
         * Checks if function sums all values on index 1
         */
        Object[] input = {new int[]{1, 1, 1, 1}, false};
        Object result = executeSingle(input);

        try {
            executeSingle(new Object[] {new int []{3},false}); // should crash
        } catch (Exception e) {
            if (((InvocationTargetException) e).getTargetException() instanceof ArrayIndexOutOfBoundsException) {
                addTestScore(result.equals(4));
            }
        }
    }

    @CommonLogicTest(functionNames = {"sumValues"})
    public void sumValuesPositive(String functionBody) throws Exception {
        /**
         * Checks if function sums positive values on index 1
         */
        Object[] input = {new int[]{1, 1, 1, 1}, true};
        Object result = executeSingle(input);
        try {
            executeSingle(new Object[] {new int []{3},false}); // should crash
        } catch (Exception e) {
            if (((InvocationTargetException) e).getTargetException() instanceof ArrayIndexOutOfBoundsException) {
                addTestScore(result.equals(4));
            }
        }
    }

    @CommonLogicTest(functionNames = {"oddSum"})
    public void oddSum(String functionBody) throws Exception {
        /**
         * Checks if function sums all values on index 1
         */
        Object[] input = {new int[]{1, 1, 1, 1},};
        Object result = executeSingle(input);
        addTestScore(result.equals(4));
    }

}
