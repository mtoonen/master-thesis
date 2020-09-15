package nl.meine.master.testsuite.tests;

import nl.meine.master.testsuite.CommonLogicTest;
import nl.meine.master.testsuite.CommonLogicLabel;
import nl.meine.master.testsuite.InlineCompiler;

import java.lang.reflect.InvocationTargetException;


public class ForEachButIndexLabel extends CommonLogicLabel {

    public Class[] paramType = null;
    public String functionName = null;

    public ForEachButIndexLabel(InlineCompiler compiler) {
        label = "foreachbutindex";
        paramType = new Class[]{int[].class};
        functionName = "countEven";
        init(functionName, paramType, compiler);
    }

  //  @CommonLogicTest(functionNames = {"countEven"})
    public void exceptionTest(String functionBody) {
        /**
         * This test is based on the premise that when using the value to retrieve a value from the array on that index
         * (the value being the index), we get an ArrayIndexOutOfBoundsException when defining an array of length 1
         * with the first element being > 1. This will let the function retrieve a value outside the bounds
         */
        Object[] input = {new int[]{2}};
        try {
            Object result = executeSingle(functionBody,functionName, input);
        } catch (Exception e) {
            if(((InvocationTargetException) e).getTargetException() instanceof  ArrayIndexOutOfBoundsException){
                testResultsPerExercise.get(functionName).put(getCurrentTestName(), true);
            }
        }
    }

  //  @CommonLogicTest(functionNames = {"countEven"})
    public void exceptionTestMinus(String functionBody) {
        /**
         * This test is based on the premise that when using the value to retrieve a value from the array on that index
         * (the value being the index), we get an ArrayIndexOutOfBoundsException when defining an array of length 1
         * with the first element being > 1. This will let the function retrieve a value outside the bounds
         */
        Object[] input = {new int[]{-2}};
        try {
            Object result = executeSingle(functionBody,functionName, input);
        } catch (Exception e) {
            if(((InvocationTargetException) e).getTargetException() instanceof  ArrayIndexOutOfBoundsException){
                testResultsPerExercise.get(functionName).put(getCurrentTestName(), true);
            }
        }
    }

    //@CommonLogicTest(functionNames = {"countEven"})
    public void countSpecificIndices(String functionBody) throws Exception {
        /**
         * This test looks if the value is used to look up a value in the array. We set all the values to 2, except on the 3rd index,
         * that one is 1. When the label foreachbutindex should apply, when checking the 3rd value (1), it will retrieve the value on
         * values[1], which is 2. All other values result in check the value 1 for evenness. Result should be that the function counts
         * only one instance of an even number
         */
        Object[] input = {new int[]{2, 2, 1, 2,2}};
        Object result = executeSingle(functionBody,functionName, input);
        testResultsPerExercise.get(functionName).put(getCurrentTestName(), result.equals(1));
    }

    //@CommonLogicTest(functionNames = {"countEven"})
    public void countSpecificIndices2(String functionBody) throws Exception {
        /**
         * This test looks if the value is used to look up a value in the array. We set all the values to 2, except on the 3rd index,
         * that one is 1. When the label foreachbutindex should apply, when checking the 3rd value (1), it will retrieve the value on
         * values[1], which is 2. All other values result in check the value 1 for evenness. Result should be that the function counts
         * only one instance of an even number
         */
        Object[] input = {new int[]{0,0,0,0,1,}};
        Object result = executeSingle(functionBody,functionName, input);
        testResultsPerExercise.get(functionName).put(getCurrentTestName(), result.equals(5));
    }

}
