package homework;

import homework.annotation.After;
import homework.annotation.Before;
import homework.annotation.Test;

public class CalculatorTest {
    @Before
    void before() {
        System.out.println("\n<Before method");
    }

    @After
    void after() {
        System.out.println("\n<After method>");
    }

    @Test
    void Test1() {
        Calculator.getDivision(4, 2);
    }
    @Test
    void Test2() {
        Calculator.getDivision(10, 2);
    }
    @Test
    void Test3() {
        Calculator.getDivision(5, 0);
    }


}