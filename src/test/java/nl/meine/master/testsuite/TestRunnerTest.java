package nl.meine.master.testsuite;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestRunnerTest {

    private TestRunner instance;
    @BeforeEach
    void setUp() {
        instance = new TestRunner();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void calculateLabel() throws UncompilableException {
        Label label = instance.calculateLabel("1.even", TestUtils.FUNCTION_FOREACHBUTINDEX);
        assertEquals("foreachbutindex", label.getLabel());
    }
}