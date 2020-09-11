package nl.meine.master.reader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private Main instance;

    @BeforeEach
    void setUp() {
        instance = new Main();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void process() {
    }

    @Test
    void calculatePrecision() {
        assertEquals(5/(double)8,instance.calculatePrecision(5, 8));
    }

    @Test
    void calculateRecall() {
        assertEquals(5/(double)12,instance.calculateRecall(5, 12));
    }
}