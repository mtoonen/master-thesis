package nl.meine.master.testsuite;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

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
        Set<Label> label = instance.calculateLabel("1.even", TestUtils.FUNCTION_FOREACHBUTINDEX);
        assertEquals(1, label.size());
        assertEquals("foreachbutindex", label.toArray(new Label[]{})[0].getLabel());
    }
}