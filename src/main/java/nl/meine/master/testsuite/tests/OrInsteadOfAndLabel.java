package nl.meine.master.testsuite.tests;

import nl.meine.master.testsuite.CommonLogicLabel;
import nl.meine.master.testsuite.CommonLogicTest;
import nl.meine.master.testsuite.InlineCompiler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class OrInsteadOfAndLabel  extends CommonLogicLabel {

    public OrInsteadOfAndLabel(InlineCompiler compiler) {
        label = "orinsteadofand";
        this.compiler = compiler;
        init();
    }

    @CommonLogicTest(functionNames = {"calculateScore"})
    public void wrongWeekendCheck(String functionBody) throws Exception {
        /**
         * Checks if function always adds a penalty for traveling on a weekday
         */
        Object[] inputs = {1,2,3,4,5};
        List<Object> results = new ArrayList<>();
        for (Object input: inputs) {
            results.add(executeSingle(new Object[]{0,input}));
        }
        AtomicBoolean allWrong = new AtomicBoolean(true);
        results.forEach(res -> {
            if(!res.equals(7)){
                allWrong.set(false);
            }
        });
        addTestScore(allWrong.get());
    }

    @CommonLogicTest(functionNames = {"calculateScore"})
    public void wrongWeekendCheckInverse(String functionBody) throws Exception {
        /**
         * Checks if function never adds a penalty for traveling on a weekday
         */
        Object[] inputs = {1,2,3,4,5};
        List<Object> results = new ArrayList<>();
        for (Object input: inputs) {
            results.add(executeSingle(new Object[]{0,input}));
        }
        AtomicBoolean allWrong = new AtomicBoolean(true);
        results.forEach(res -> {
            if(!res.equals(10)){
                allWrong.set(false);
            }
        });
        addTestScore(allWrong.get());
    }

    @CommonLogicTest(functionNames = {"oddSum"})
    public void oddSum(String functionBody) throws Exception {
        /**
         * Checks if function doesn't respect boundary because of using the or instead of and
         */
        Object[] input = {new int[]{1, 1, 1, 1}};
        try{
            Object result = executeSingle(input);
        }catch(Exception e){
            if (((InvocationTargetException) e).getTargetException() instanceof ArrayIndexOutOfBoundsException) {
                addTestScore(true);
            }
        }
    }
}
