package com.example.studentinterfacegui;

/**
 * Student class that initializes Student objects
 * with Profile object, Major object and creditCompleted.
 * Has various student methods like compareTo
 * @author Sofia Juliani, Arnold Nguyen
 */
public abstract class  Student implements Comparable<Student> {
    private Profile profile;
    private Major major;
    private int creditCompleted;
    protected static final double universityFee = 3268;
    protected static final int minCredit = 3, maxCredit = 24;
    protected static final int creditHourLimit = 16;
    protected static final double percentFullTimeRate = 0.8;

    /**
     * Constructor class for Student object
     * @param givenProfile A profile object for the student
     * @param givenMajor A major object to create student
     * @param givenCredits Credit Completed object
     */
    public Student(Profile givenProfile, Major givenMajor, int givenCredits) {
        this.profile = givenProfile;
        this.major = givenMajor;
        this.creditCompleted = givenCredits;
    }

    /**
     * Extracts information attributes from Student Object
     * @param student object
     */
    public Student(Student student) {
        this.profile = student.profile;
        this.major = student.major;
        this.creditCompleted = student.creditCompleted;
    }

    /**
     * Profile only constructor for Student
     * @param givenProfile is Profile attribute
     */
    public Student(Profile givenProfile) {
        this.profile = givenProfile;
    }

    /**
     * Override equals method
     * @param obj for Student obj
     * @return true if the 2 Students have the same major, profile,
     * and credits completed, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student) {
            Student studentObj = (Student) obj;
            if (studentObj.major.compareTo(this.major) == 0){
                if (studentObj.profile.compareTo(this.profile) == 0){
                    if (studentObj.creditCompleted == this.creditCompleted){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Getter method to getMajor from Student object
     * @return attribute
     */
    public Major getMajor(){
        return this.major;
    }

    /**
     * Getter method to get school from Student object
     * @return school attribute
     */
    public String getSchool(){
        return this.major.getSchool();
    }

    /**
     * Getter method to get profile from Student object
     * @return profile attribute
     */
    public Profile getProfile() {
        return this.profile;
    }

    /**
     * Getter method for creditCompleted
     * @return creditCompleted attribute
     */
    public int getCreditCompleted(){
        return this.creditCompleted;
    }

    /**
     * Setter method for Major
     * @param newMajor to change major attribute
     */
    public void setMajor(Major newMajor) {
        this.major = newMajor;
    }

    /**
     * Compares students
     * @param student object to compare student
     * @return 0 if same profile, 1 if this.profile greater, -1 if less
     */
    @Override
    public int compareTo(Student student) {
        if (this.profile.compareTo(student.profile) > 0)
            return 1;
        else if (this.profile.compareTo(student.profile) < 0)
            return -1;
        return 0;
    }

    /**
     * Override toString method
     * @return String representation of Student object
     */
    @Override
    public String toString() {
        return " credits completed: " + this.creditCompleted + " ";
    }

    /**
     * isValid method for Student that determines if the creditsEnrolled
     * are valid
     * @param creditEnrolled credits Student is currently taking
     * @return true if student has valid enrollment credits
     */
    public boolean isValid(int creditEnrolled){
        if (creditEnrolled >= minCredit && creditEnrolled <= maxCredit)
            return true;
        else
            return false;
    }

    /**
     * Update the credits of a student's completed credits
     * @param creditsToAdd credits to update
     */
    public void updateCredits(int creditsToAdd) {
        this.creditCompleted += creditsToAdd;
    }

    /**
     * Abstract tuitionDue method used to be extended by other subclasses
     * @param creditsEnrolled credits currently being taken
     * @return double value that represents the tuition
     */
    public abstract double tuitionDue(int creditsEnrolled);

    /**
     * Abstract boolean method for isResident that is extended by subclasses
     * @return true if student is resident, false if not
     */
    public abstract boolean isResident();

    /**
     * Abstract invalidStudent checker that is extended by subclasses
     * @return String for each individual subclass
     */
    public abstract String invalidStudent();

    /**
     * Abstract getClassification for the subclasses of Student
     * @return string classification of role
     */
    public abstract String getClassification();

}
