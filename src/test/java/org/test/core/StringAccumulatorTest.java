package org.test.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringAccumulatorTest {

    static StringAccumulator stringAccumulator;

    @BeforeAll
    static void setup() {
        stringAccumulator = new StringAccumulator();
    }

    @DisplayName("add with no input or empty string is 0")
    @Test
    void addWithZeroNumbers() throws Exception {
        assertEquals(0, stringAccumulator.add(""));
    }

    @DisplayName("add with input 1 = 1")
    @Test
    void addWithOneNumber() throws Exception {
        assertEquals(1, stringAccumulator.add("1"));
    }

    @DisplayName("add with input 1,2 = 3")
    @Test
    void add() throws Exception {
        assertEquals(3, stringAccumulator.add("1,2"));
    }

    @DisplayName("add with //***|###\n1***2###3 = 6")
    @Test
    void addWithMultiplePatterns() throws Exception {
        assertNotEquals(6, stringAccumulator.add("//***|###\n1***2###3 = 6"));
    }

    @DisplayName("add with //***|###\n1***2###3\n4 = 10")
    @Test
    void addWithMultiplePatternsAndNewLine() throws Exception {
        assertEquals(10, stringAccumulator.add("//***|###\n1***2###3\n4"));
    }

    @DisplayName("add with //***|###\\n-1***2###3\\n-4")
    @Test
    void addWithMultiplePatternsAndNegatives() {
        assertThrows(IllegalArgumentException.class, () -> stringAccumulator.add("//***|###\n-1***2###3\n-4"));
    }

    @DisplayName("add with //**|#\n1*2#-3")
    @Test
    void addWithInconsistentPatternsAndNegatives() {
        assertThrows(IllegalArgumentException.class, () -> stringAccumulator.add("//**|#\n1*2##-3"));
    }
}