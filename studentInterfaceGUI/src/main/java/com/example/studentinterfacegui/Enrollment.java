package com.example.studentinterfacegui;
import javafx.scene.control.TextArea;
/**
 * Enrollment class that handles EnrollStudent objects
 * Has various methods to handle EnrollStudent objects such as add, grow,
 * remove.
 * @authors Sofia Juliani, Arnold Nguyen
 */
public class Enrollment {
    private EnrollStudent[] enrollStudents;
    private int size;

    /**
     * Initialization of Enrollment array and it's size
     */
    public Enrollment() {
        this.size = 4;
        this.enrollStudents = new EnrollStudent[this.size];
    }

    /**
     * adds enrollStudent to the Enrollment array
     * @param enrollStudent object
     */
    public void add(EnrollStudent enrollStudent) { //add to the end of array
        if (isEnrollmentFull(this.enrollStudents)) {
            this.grow();
        }

        if (!contains(enrollStudent)) { //add
            // enrollStudent for the first time
            if (!isEnrollmentFull(enrollStudents)) {
                for (int i = 0; i < this.size; i++) {
                    if (enrollStudents[i] == null) {
                        enrollStudents[i] = enrollStudent;
                        return;
                    }
                }
            }
        }

    }

    /**
     * Determines if the EnrollStudents array is full
     * @param enrollStudentsInstance array
     * @return true if it is full, false if it is not
     */
    //move the last one in the array to replace the deleting index position
    private boolean isEnrollmentFull(EnrollStudent[] enrollStudentsInstance) {
        for (int i = 0; i < this.size; i++) {
            if (enrollStudentsInstance[i] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Grows the enrollment array by 4 and then adds in the students into
     * the new array accordingly
     */
    private void grow() {
        int increaseByValue = 4;
        EnrollStudent[] tempEnrollment =
                new EnrollStudent[this.size + increaseByValue];
        for (int i = 0; i < this.size; i++) {
            tempEnrollment[i] = new EnrollStudent(this.enrollStudents[i]);
        }
        this.enrollStudents = tempEnrollment;
        this.size += increaseByValue;
    }

    /**
     * Finds the student in the Enrollment array and takes them out
     * @param enrollStudent to find in the EnrollStudent array
     */
    public void remove(EnrollStudent enrollStudent) {
        boolean studentExists = false;
        // instantiate the variable that will contain the index of the
        // student to remove:
        int nullIndex = -1;
        for (int i = 0; i < this.size; i++) {
            if (this.enrollStudents[i] != null) {
                if (this.enrollStudents[i].equals(enrollStudent)) {
                    studentExists = true;
                    nullIndex = i;
                    this.enrollStudents[i] = null;
                    break;
                }
            }
        }
        if (studentExists) {
            for (int i = this.size - 1; i > 0; i--) {
                if (this.enrollStudents[i] != null) {
                    this.enrollStudents[nullIndex] = this.enrollStudents[i];
                    this.enrollStudents[i] = null;
                    break;
                }
            }
        }

    }

    /**
     * Determines if the array contains the student in questiion
     * @param enrollStudent object to be found
     * @return true if EnrollStudent array contains enrollStudent
     */
    public boolean contains(EnrollStudent enrollStudent) {
        for (int i = 0; i < this.size; i++) {
            if (this.enrollStudents[i] != null) {
                if (this.enrollStudents[i].equals(enrollStudent)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Prints out the EnrollStudents in the order of the Enrollment array
     */
    public void print(TextArea TextArea)
    {
        for (int i = 0; i < this.size; i++){
            if (this.enrollStudents[i] != null)
                TextArea.appendText(this.enrollStudents[i].getProfile().toString()
                        + ": " + "credits enrolled: "
                        + this.enrollStudents[i].getCreditsEnrolled());
        }
    } //print the array as is without sorting

    /**
     * Getter method that returns the size of the array
     * @return size of the array
     */
    public int getSize(){ return this.size; }

    /**
     * Returns this.enrollStudents
     * @return the EnrollStudents from the array
     */
    public EnrollStudent[] getEnrollStudents() {
        return this.enrollStudents;
    }

    /**
     * Updates the array with the given credits of a specific student
     * @param studentProfile for student to be found in array
     * @param credits to be updated to
     */
    public void updateCredits(Profile studentProfile, int credits) {
        for (int i = 0; i < this.size; i++) {
            if (this.enrollStudents[i] != null) {
                if (this.enrollStudents[i].getProfile().equals(studentProfile)){
                    this.enrollStudents[i].setCreditsEnrolled(credits);
                }
            }
        }
    }

    /**
     * Determines if the enrollment is empty
     * @return true if empty, false if not
     */
    public boolean isEnrollmentEmpty() {
        for (int i = 0; i < this.size; i++) {
            if (this.enrollStudents[i] != null) {
                return false;
            }
        }
        return true;
    }

    public EnrollStudent findStudent(Profile profile) {
        for (int i = 0; i < this.size; i++)
            if (this.enrollStudents[i] != null) {
                if (this.enrollStudents[i].getProfile().equals(profile)) {
                    return this.enrollStudents[i];
                }
            }
        return null;
    }
}