package com.example.studentinterfacegui;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test class for Date that runs test cases
 * @authors Sofia Juliani, Arnold Nguyen
 */
public class DateTest {
    /**
     * This test case tests a non-leap year for isValid with 29 days in Feb
     */
    @Test
    public void test_isValid_daysInFeb_nonLeap(){
        Date date = new Date("2/29/2003");
        assertFalse(date.isValid());
    }

    /**
     * This test case tests more than 30 days in April
     */
    @Test
    public void test_isValid_invalidDay_nonValid() {
        Date date = new Date("4/31/2003");
        assertFalse(date.isValid());
    }

    /**
     * This test case tests an invalid month
     */
    @Test
    public void test_isValid_monthInvalid(){
        Date date = new Date("13/31/2003");
        assertFalse(date.isValid());
    }

    /**
     * This test case tests an invalid day in March
     */
    @Test
    public void test_isValid_dayInvalid(){
        Date date = new Date("3/32/2003");
        assertFalse(date.isValid());
    }

    /**
     * This test case tests a negative month which should return false
     */
    @Test
    public void test_isValid_negativeMonth() {
        Date date = new Date("-1/31/2003");
        assertFalse(date.isValid());
    }

    /**
     * This test case is a valid day in February given that it is a leap year
     */
    @Test
    public void test_isValid_daysInFeb_validDate() {
        Date date = new Date("2/28/2004");
        assertTrue(date.isValid());
    }

    /**
     * This test case is valid in March as it has valid days and year.
     */
    @Test
    public void test_isValid_daysInMarch_validDates() {
        Date date = new Date("3/20/2003");
        assertTrue(date.isValid());
    }
}