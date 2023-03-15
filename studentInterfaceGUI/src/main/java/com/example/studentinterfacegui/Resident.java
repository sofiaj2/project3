package com.example.studentinterfacegui;

/**
 * Class that handles Resident objects with methods such as tuitionDue to
 * handle the tuition owed by Resident students.
 * @author Sofia Juliani, Arnold Nguyen
 */
public class Resident extends Student {
    private double scholarship;
    private static final double residentTuition = 12536, creditHour = 404;

    /**
     * Constructor for the Resident object
     * @param givenProfile attribute for Resident objects
     * @param givenMajor attribute that handles the major for Resident objects
     * @param givenCredits represents credits taken by a Resident
     */
    public Resident(Profile givenProfile, Major givenMajor,
                    int givenCredits) {
        super(givenProfile, givenMajor, givenCredits);
        this.scholarship = 0;
    }

    /**
     * Gives the tuition due for a resident of the abstract class Student
     * @param creditsEnrolled for credits the resident is taking
     * @return tuition the Resident owes
     */
    public double tuitionDue(int creditsEnrolled) {
        double tuition;
        if (isFullTime(creditsEnrolled)) { //full time student
            tuition = universityFee + residentTuition - scholarship;
            if (creditsEnrolled > creditHourLimit) {
                tuition =
                        (creditHour * (creditsEnrolled - creditHourLimit)) + tuition;
            }
        }
        else { //part time student
            tuition = (creditHour * creditsEnrolled)
                    + (percentFullTimeRate * universityFee);
        }
        return tuition;
    }

    /**
     * Returns true if part of Resident object
     * @return true
     */
    public boolean isResident() {
        return true;
    }

    /**
     * Resident true if resident has above 12 credits
     * @return true if student is full time
     */
    public boolean isFullTime(int creditsEnrolled) {
        if (creditsEnrolled >= 12)
            return true;
        return false;
    }

    /**
     * Sets the scholarship of the Resident student
     * @param givenScholarship scholarship to be given to student
     */
    public void setScholarship(int givenScholarship) {
        this.scholarship += givenScholarship;
    }

    /**
     * Gets classification of Resident
     * @return resident String
     */
    public String getClassification() {
        return "(resident)";
    }

    /**
     * Another classification method of Resident
     * @return Resident String
     */
    public String invalidStudent() {
        return "(Resident) ";
    }
}
