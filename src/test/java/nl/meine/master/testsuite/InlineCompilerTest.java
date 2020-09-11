package nl.meine.master.testsuite;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InlineCompilerTest {

    private InlineCompiler instance;

    @BeforeEach
    void setUp() {
        instance = new InlineCompiler();

    }

    @AfterEach
    void tearDown() {
        instance.tearDown();
    }


    @Test
    void executeCorrect() throws Exception {
        Object[] input = {new int[]{2, 2, 1, 2,2}};
        instance.init(TestUtils.FUNCTION_FOREACHBUTINDEX);
        Object result = instance.execute( "countEven", input, int[].class);
        assertNotNull(result);
    }

    @Test
    void executeIncompilable() throws Exception {
        Object[] input = {new int[]{2, 2, 1, 2,2}};
        Assertions.assertThrows(UncompilableException.class,() ->{
            instance.init(TestUtils.FUNCTION_UNCOMPILABLE);
            instance.execute("countEven", input, int[].class);
        });
    }

    @Test
    void init() throws Exception {
        instance.init(TestUtils.FUNCTION_FOREACHBUTINDEX);
        assertTrue(instance.classFile.exists());
        assertTrue(instance.sourceFile.exists());
    }

    @Test
    void testTearDown() throws Exception {
        instance.init(TestUtils.FUNCTION_FOREACHBUTINDEX);
        instance.tearDown();
        assertFalse(instance.classFile.exists());
        assertFalse(instance.sourceFile.exists());
    }
}