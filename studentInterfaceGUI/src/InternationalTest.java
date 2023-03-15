package com.example.studentinterfacegui;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This JUnit Test class tests the tuitionDue method of International
 * study abroad students
 * @authors Sofia Juliani, Arnold Nguyen
 */
public class InternationalTest {
    /**
     * This student is non study abroad and thus should pay the tuition as
     * well as the fees. This method should return true.
     */
    @Test
    public void test_tuitionDue_nonStudyAbroad(){
        Date StudentDate = new Date("8/17/2003");
        Profile studentProfile = new Profile ("Doe", "John", StudentDate);
        Major studentMajor = Major.CS;
        boolean studiesAbroad = false;
        int credits = 87;
        double tuition;
        int creditsEnrolled = 15;
        Student student = new International(studentProfile,
                studentMajor, credits, studiesAbroad);
        tuition = student.tuitionDue(creditsEnrolled);
        assertTrue(tuition == (2650 + 3268 + 29737));
    }

    /**
     * This student should pay only the fees because he is a study abroad
     * student. Thus only the University Fee and Health Insurance fee applies.
     */
    @Test
    public void test_tuitionDue_studyAbroad(){
        Date StudentDate = new Date("8/17/2003");
        Profile studentProfile = new Profile ("Doe", "John", StudentDate);
        Major studentMajor = Major.CS;
        boolean studiesAbroad = true;
        int credits = 87;
        double tuition;
        int creditsEnrolled = 15;
        Student student = new International(studentProfile,
                studentMajor, credits, studiesAbroad);
        EnrollStudent enrolledStudent = new EnrollStudent(studentProfile, creditsEnrolled);
        tuition = student.tuitionDue(creditsEnrolled);
        assertTrue(tuition == (2650 + 3268));
    }
}