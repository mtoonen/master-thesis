package nl.meine.master.testsuite;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestRunnerTest {

    private TestRunner instance;
    private String inputMethod;
    @BeforeEach
    void setUp() {
        instance = new TestRunner();
        inputMethod = "public static int countEven(int [] values)\n" +
                "    {\n" +
                "        int count = 0;\n" +
                "        for (int i = 0; i < values.length; i++)\n" +
                "        {\n" +
                "            if (values[i] % 2 == 0)\n" +
                "            {\n" +
                "                count++;\n" +
                "            }\n" +
                "            else\n" +
                "            {\n" +
                "                return count;\n" +
                "            }\n" +
                "        }\n" +
                "        return count;\n" +
                "    }";
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void calculateLabel() {
        String label = instance.calculateLabel("1.even", inputMethod);
        assertEquals("foreachbutindex", label);
    }
}