package htw.berlin.prog2.ha1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Retro calculator")
class CalculatorTest {

    @Test
    @DisplayName("should display result after adding two positive multi-digit numbers")
    void testPositiveAddition() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(2);
        calc.pressDigitKey(0);
        calc.pressBinaryOperationKey("+");
        calc.pressDigitKey(2);
        calc.pressDigitKey(0);
        calc.pressEqualsKey();

        String expected = "40";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should display result after getting the square root of two")
    void testSquareRoot() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(2);
        calc.pressUnaryOperationKey("√");

        String expected = "1.41421356";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should display error when dividing by zero")
    void testDivisionByZero() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(7);
        calc.pressBinaryOperationKey("/");
        calc.pressDigitKey(0);
        calc.pressEqualsKey();

        String expected = "Error";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should display error when drawing the square root of a negative number")
    void testSquareRootOfNegative() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(7);
        calc.pressNegativeKey();
        calc.pressUnaryOperationKey("√");

        String expected = "Error";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should not allow multiple decimal dots")
    void testMultipleDecimalDots() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(1);
        calc.pressDotKey();
        calc.pressDigitKey(7);
        calc.pressDotKey();
        calc.pressDigitKey(8);

        String expected = "1.78";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }


    //TODO hier weitere Tests erstellen

    @Test
    @DisplayName("should display result after subtracting two negative numbers")
    void testNegativeSubstraction() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(3);
        calc.pressNegativeKey();
        calc.pressBinaryOperationKey("-");
        calc.pressDigitKey(8);
        calc.pressNegativeKey();
        calc.pressEqualsKey();

        String expected = "5";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }
    @Test
    @DisplayName("should clear the previous number and display the new number")
    void testClearKey() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(3);
        calc.pressClearKey();
        calc.pressDigitKey(8);
        calc.pressBinaryOperationKey("+");
        calc.pressDigitKey(5);
        calc.pressEqualsKey();
        calc.pressClearKey();
        calc.pressClearKey();
        calc.pressBinaryOperationKey("+");
        calc.pressDigitKey(2);
        calc.pressEqualsKey();

        String expected = "2";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should display result after inverting a number")
    void testInverseKey() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(5);
        calc.pressUnaryOperationKey("1/x");


        String expected = "0.2";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should display result after adding two big numbers")
    void testBigNumbers() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(9);
        calc.pressDigitKey(9);
        calc.pressDigitKey(9);
        calc.pressDigitKey(9);
        calc.pressDigitKey(9);
        calc.pressDigitKey(9);
        calc.pressBinaryOperationKey("+");
        calc.pressDigitKey(9);
        calc.pressDigitKey(9);
        calc.pressDigitKey(9);
        calc.pressDigitKey(9);
        calc.pressDigitKey(9);
        calc.pressDigitKey(9);
        calc.pressEqualsKey();

        String expected = "1999998";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should display error when dividing by zero after a prior operation")
    void testDivisionByZeroAfterOperation() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(9);
        calc.pressBinaryOperationKey("-");
        calc.pressDigitKey(4);
        calc.pressEqualsKey(); // Ergebnis: 5

        calc.pressBinaryOperationKey("/");
        calc.pressDigitKey(0);
        calc.pressEqualsKey();

        String expected = "Error";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should repeat the last operation when equals is pressed multiple times")
    void testRepeatedEquals() {
        Calculator calc = new Calculator();

        calc.pressDigitKey(5);
        calc.pressBinaryOperationKey("+");
        calc.pressDigitKey(5);
        calc.pressEqualsKey();

        calc.pressEqualsKey();

        String expected = "15";
        String actual = calc.readScreen();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should display error when dividing zero by zero")
    void testZeroDividedByZero() {
    Calculator calc = new Calculator();

    calc.pressDigitKey(0);
    calc.pressBinaryOperationKey("/");
    calc.pressDigitKey(0);
    calc.pressEqualsKey();

    String expected = "Error";
    String actual = calc.readScreen();

    assertEquals(expected, actual);
}
}

