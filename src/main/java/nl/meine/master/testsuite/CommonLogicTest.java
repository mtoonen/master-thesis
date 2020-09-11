package nl.meine.master.testsuite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate a method testing one variation of a common logic error.
 * Used for bookkeeping: CommonErrorTestRunner keeps a list these methods for automatic running of these methods
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommonLogicTest {
    public String[] exercises();
}
