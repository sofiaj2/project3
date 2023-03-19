package com.example.studentinterfacegui;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.text.DecimalFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import java.time.format.DateTimeFormatter;
/**
 * TuitionManagerController handles the GUI interface and attaches listeners to
 * buttons, textfields, etc. for user interaction.
 * @authors Arnold Nguyen, Sofia Juliani
 */
public class TuitionManagerController {

    private Roster rutgersRoster = new Roster();
    private Enrollment enrollRoster = new Enrollment();

    /**
     * checkMajor checks if one of the Major buttons is selected and
     * returns a major object for the major button that is selected
     * @param event the ActionEvent for when user interacts with specified object
     * @return Major object
     */
    private Major checkMajor(ActionEvent event) {
        Major major;
        if (RosterBAITButton.isSelected()) {
            major = Major.BAIT;
        } else if (RosterITIButton.isSelected()) {
            major = Major.ITI;
        } else if (RosterCSButton.isSelected()) {
            major = Major.CS;
        } else if (RosterECEButton.isSelected()) {
            major = Major.EE;
        } else if (RosterMATHButton.isSelected()) {
            major = Major.MATH;
        } else {
            major = null;
        }
        return major;
    }

    /**
     * Adds a Student to the Roster with Profile attribute and major
     * @param event ActionEvent for when user interacts with specified object
     */
    @FXML
    private void addRoster(ActionEvent event) {
        if (isValidInputAdd(event)) {
            Major major = checkMajor(event);
            String firstName = rosterFirstName.getText();
            String lastName = rosterLastName.getText();
            int credits = Integer.parseInt(RosterCreditsCompleted.getText());
            String date = RosterDatePicker.getValue().toString();
            Date dob = new Date(date);
            Profile profile = new Profile(lastName, firstName, dob);
            Student student = null;
            if (RosterResidentButton.isSelected()) {
                student = new Resident(profile, major, credits);
            } else if (RosterNonResidentButton.isSelected()) {
                if (RosterTriStateButton.isSelected()) {
                    if (RosterCTButton.isSelected())
                        student = new TriState(profile, major, credits, "CT");
                    else if (RosterNYButton.isSelected())
                        student = new TriState(profile, major, credits, "NY");
                    else {
                        textArea.appendText("Missing data in line command.\n");
                    }
                } else if (RosterInternationalButton.isSelected()) {
                    if (RosterStudyAbroadButton.isSelected())
                        student = new International(profile, major, credits, true);
                    else
                        student = new International(profile, major, credits, false);
                } else {
                    textArea.appendText("Missing data in line command.\n");
                }
            } else {
                textArea.appendText("Missing data in line command.\n");
            }
            if (student != null) {
                addStudentToRoster(student, dob);
            }
        }
    }

    /**
     * Determines if a user has entered in valid input for the add method for Roster
     * @param event ActionEvent for when the object interacts with specified object
     * @return true if the input is valid, false if not.
     */
    private boolean isValidInputAdd(ActionEvent event) {
        String firstName = rosterFirstName.getText();
        String lastName = rosterLastName.getText();
        String creditsCompleted = RosterCreditsCompleted.getText();
        Major major = checkMajor(event);
        if (firstName.equals("") || lastName.equals("") || major == null ||
            creditsCompleted.equals("")) {
            textArea.appendText("Missing data in line command.\n");
            return false;
        }
        if (!isValidCredits(creditsCompleted)) {
            return false;
        }

        try {
            String stringDate = RosterDatePicker.getValue().toString();
        }
        catch (NullPointerException e) {
            textArea.appendText("Missing data in line command.\n");
            return false;
        }

        return true;
    }

    /**
     * Determines if a user has entered in valid input without including credits
     * @param event ActionEvent for when the object interacts with specified object
     * @return true if the input is valid, false if not.
     */
    private boolean isValidInputNamesandDate(ActionEvent event) {
        String firstName = rosterFirstName.getText();
        String lastName = rosterLastName.getText();
        if (firstName.equals("") || lastName.equals("")) {
            textArea.appendText("Missing data in line command.\n");
            return false;
        }

        try {
            String date = RosterDatePicker.getValue().toString();
        }
        catch (NullPointerException e) {
            textArea.appendText("Missing data in line command.\n");
            return false;
        }
        return true;
    }

    /**
     * Add student to the Roster
     * @param student Student object to be added
     * @param studentDate attribute of Student
     */
    private void addStudentToRoster(Student student, Date studentDate) {
        if (student != null) {
            if (rutgersRoster.contains(student)) {
                textArea.appendText(student.getProfile().toString()
                        + " is already in the roster.\n");
            } else {
                if (rutgersRoster.add(student)) {
                    textArea.appendText(student.getProfile().toString() +
                            " added to the roster.\n");
                } else {
                    textArea.appendText("DOB invalid: " + studentDate +
                            " younger than 16 years old.\n");
                }
            }
        }
    }

    /**
     * Remove a student from the Roster.
     * @param event ActionEvent that handles user interaction of specified object
     */
    @FXML
    private void removeRoster(ActionEvent event) {
        String firstName = rosterFirstName.getText();
        String lastName = rosterLastName.getText();
        if (isValidInputNamesandDate(event)) {
            String date = RosterDatePicker.getValue().toString();
            Date dateOfBirth = new Date(date);
            Profile studentProfile = new Profile(lastName, firstName, dateOfBirth);

            if (rutgersRoster.remove(studentProfile))
                textArea.appendText(studentProfile + " removed from the roster.\n");
            else {
                textArea.appendText(studentProfile + " is not in the roster.\n");
            }
        }

    }

    /**
     * Takes in a Major object and prints the String of the Major
     * @param major Object
     * @return String of Major Object
     */
    private String printMajor(Major major) {
        if (major.equals(major.BAIT)) {
            return "BAIT";
        } else if (major.equals(major.ITI)) {
            return "ITI";
        } else if (major.equals(major.MATH)) {
            return "MATH";
        } else if (major.equals(major.CS)) {
            return "CS";
        } else if (major.equals(major.EE)) {
            return "EE";
        }
        else {
            return null;
        }
    }

    /**
     * Checks if the inputted textfields and radiobutton are valid input
     * @param event ActionEvent for handling objects user interacts with
     * @return true if valid input, false if not
     */
    private boolean isValidInputChangeMajor(ActionEvent event) {
        String firstName = rosterFirstName.getText();
        String lastName = rosterLastName.getText();
        Major major = checkMajor(event);
        if (firstName.equals("") || lastName.equals("") || major == null) {
            textArea.appendText("Missing data in line command.\n");
            return false;
        }

        try {
            String stringDate = RosterDatePicker.getValue().toString();
        }
        catch (NullPointerException e) {
            textArea.appendText("Missing data in line command.\n");
            return false;
        }

        return true;
    }

    /**
     * Changes the major of a specified Student in Roster
     * @param event ActionEvent that handles specified user interaction of object
     */
    @FXML
    private void changeMajor(ActionEvent event){
        String firstName = rosterFirstName.getText();
        String lastName = rosterLastName.getText();
        Major major = checkMajor(event);
        if (isValidInputChangeMajor(event)) {
            String date = RosterDatePicker.getValue().toString();
            Date dateOfBirth = new Date(date);
            Profile studentProfile = new Profile(lastName, firstName, dateOfBirth);
            if (!rutgersRoster.contains(studentProfile)) {
                textArea.appendText(studentProfile.toString()
                        + " is not in the roster.\n");
            } else {
                rutgersRoster.findStudent(studentProfile).setMajor(major);
                textArea.appendText(studentProfile.toString() +
                        " major changed to " +
                        printMajor(rutgersRoster.findStudent(studentProfile).getMajor()) + "\n");
            }
        }

    }

    /**
     * If residentRadioButton selected, will disable other Non-Resident buttons
     * @param event ActionEvent handler that reflects user specified object interaction
     */
    @FXML
    private void residentSelected(ActionEvent event){
        if (RosterResidentButton.isSelected()) {
            RosterTriStateButton.setDisable(true);
            RosterCTButton.setDisable(true);
            RosterNYButton.setDisable(true);
            RosterInternationalButton.setDisable(true);
            RosterStudyAbroadButton.setDisable(true);
            RosterTriStateButton.setSelected(false);
            RosterCTButton.setSelected(false);
            RosterNYButton.setSelected(false);
            RosterInternationalButton.setSelected(false);
            RosterStudyAbroadButton.setSelected(false);
        }
    }

    /**
     * If non-resident is selected, enables Non-Resident options like TriState
     * @param event ActionEvent handler for User Interaction objects
     */
    @FXML
    private void nonResidentSelected(ActionEvent event){
        if (RosterNonResidentButton.isSelected()){
            RosterTriStateButton.setDisable(false);
            RosterCTButton.setDisable(false);
            RosterNYButton.setDisable(false);
            RosterInternationalButton.setDisable(false);
            RosterStudyAbroadButton.setDisable(false);
        }
    }

    /**
     * When TriState interacted with, disables Study Abroad and enables NY and CT
     * @param event ActionEvent Handler for user interaction
     */
    @FXML
    private void triStateSelected(ActionEvent event){
        if (RosterTriStateButton.isSelected()){
            RosterStudyAbroadButton.setDisable(true);
            RosterNYButton.setDisable(false);
            RosterCTButton.setDisable(false);
            RosterStudyAbroadButton.setSelected(false);
        }
    }

    /**
     * When international selected, disables TriState options and enables Study Abroad
     * @param event ActionEvent Handler for object user interaction
     */
    @FXML
    private void internationalSelected(ActionEvent event){
        if (RosterInternationalButton.isSelected()){
            RosterNYButton.setDisable(true);
            RosterCTButton.setDisable(true);
            RosterStudyAbroadButton.setDisable(false);
            RosterNYButton.setSelected(false);
            RosterCTButton.setDisable(false);
        }
    }

    /**
     * Determines if the credits enrolled of a student are valid
     * @param creditsEnrolled attribute of student
     * @return true if credits enrolled are valid, false if not
     */
    private boolean isValidCreditsEnrolled(String creditsEnrolled) {
        try {
            if (Integer.parseInt(creditsEnrolled) < 0) {
                textArea.appendText("Credits enrolled invalid: cannot be negative!\n");
                return false;
            }
        } catch (NumberFormatException nfe) {
            textArea.appendText("Credits enrolled is not an integer.\n");
            return false;
        }
        return true;
    }

    /**
     * Determines if the student is eligible to enroll
     * @param event ActionEvent Handler for user interaction for specified object
     * @return true if input is valid, false if not
     */
    private boolean isValidInputEnroll(ActionEvent event) {
        String firstName = EnrollFirstNameTextField.getText();
        String lastName = EnrollLastNameTextField.getText();
        String creditsEnrolled = EnrollCredits.getText();
        if (firstName.equals("") || lastName.equals("") || creditsEnrolled.equals("")) {
            textArea.appendText("Missing data in line command.\n");
            return false;
        }
        if (!isValidCreditsEnrolled(creditsEnrolled)) {
            return false;
        }

        try {String date = EnrollDatePicker.getValue().toString();}//("yyyy-mm-dd"));
        catch (NullPointerException e) {
            textArea.appendText("Missing data in line command.\n");
            return false;
        }
        return true;
    }

    /**
     * Determines if the input is valid for dropping a student
     * @param event ActionEvent handler for obj
     * @return true if the input is valid to drop
     */
    private boolean isValidInputDrop(ActionEvent event) {
        String firstName = EnrollFirstNameTextField.getText();
        String lastName = EnrollLastNameTextField.getText();
        if (firstName.equals("") || lastName.equals("")) {
            textArea.appendText("Missing data in line command.\n");
            return false;
        }
        try {String date = EnrollDatePicker.getValue().toString();}//("yyyy-mm-dd"));
        catch (NullPointerException e) {
            textArea.appendText("Missing data in line command.\n");
            return false;
        }

        return true;
    }

    /**
     * Enrolls a student onto the Enrollment List
     * @param event ActionEvent handler that handles user specified object
     */
    @FXML
    private void EnrollStudent(ActionEvent event){
        String firstName = EnrollFirstNameTextField.getText();
        String lastName = EnrollLastNameTextField.getText();
        if (isValidInputEnroll(event)){
            String date = EnrollDatePicker.getValue().toString();
            Date dateOfBirth = new Date(date);
            int credits = Integer.parseInt(EnrollCredits.getText());
            Profile studentProfile = new Profile(lastName, firstName, dateOfBirth);

            EnrollStudent studentToEnroll = new EnrollStudent(studentProfile, credits);
            if (!rutgersRoster.contains(studentProfile)) { //not in roster
                textArea.appendText("Cannot enroll: " + studentProfile +
                        " is not in the roster.\n");
            } else {
                Student rosterStudent = rutgersRoster.findStudent(studentProfile);
                if (!rosterStudent.isValid(credits)) { //not valid credits
                    textArea.appendText(rosterStudent.invalidStudent() + credits +
                            ": invalid credit hours.\n"); }
                else {
                    if (!enrollRoster.contains(studentToEnroll))
                        enrollRoster.add(studentToEnroll);
                    else {enrollRoster.updateCredits(studentProfile, credits);}
                    textArea.appendText(studentToEnroll.getProfile().toString()
                            + " enrolled " + studentToEnroll.getCreditsEnrolled() + " credits\n"); }
            }
        }
    }

    /**
     * drops a Student from the enrollment List
     * @param event ActionEvent handler for user interaction specified object
     */
    @FXML
    private void dropStudent(ActionEvent event){
        String firstName = EnrollFirstNameTextField.getText();
        String lastName = EnrollLastNameTextField.getText();
        if (isValidInputDrop(event)){
            String date = EnrollDatePicker.getValue().toString();
            Date dateOfBirth = new Date(date);
            Profile studentProfile = new Profile(lastName, firstName, dateOfBirth);
            EnrollStudent studentToDrop = new EnrollStudent(studentProfile);
            if (!enrollRoster.contains(studentToDrop)){
                textArea.appendText(studentProfile.toString() +
                        " is not enrolled.\n");
            }
            else {
                enrollRoster.remove(studentToDrop);
                textArea.appendText(studentProfile.toString() + " dropped.\n");
            }
        }
    }

    /**
     * Determines if the credits completed are valid
     * @param creditsCompleted attribute of Student
     * @return true if credits are valid, false if not
     */
    private boolean isValidCredits(String creditsCompleted) {
        try {
            if (Integer.parseInt(creditsCompleted) < 0) {
                textArea.appendText("Credits completed invalid: cannot be negative!\n");
                return false;
            }
        } catch (NumberFormatException nfe) {
            textArea.appendText("Credits completed invalid: not an integer!\n");
            return false;
        }
        return true;
    }

    /**
     * Determines if a scholarship is able to be given to a Student
     * @param event ActionEvent handler for user specified interaction object
     * @return true if scholarship has valid input, false if not
     */
    private boolean isValidInputScholarship(ActionEvent event) {
        String firstName = ScholarshipFirstNameTextField.getText();
        String lastName = ScholarshipLastNameTextField.getText();
        if (firstName.equals("") || lastName.equals("")) {
            textArea.appendText("Missing data in line command.\n");
            return false;
        }
        if (!isValidScholarshipAmount(ScholarshipAmount.getText())) {
            return false;
        }
        try {String date = ScholarshipDatePicker.getValue().toString();}
        catch (NullPointerException e) {
            textArea.appendText("Missing data in line command.\n");
            return false;
        }
        return true;
    }

    /**
     * Determines if the scholarship amount is valid (in range 0 to 10000)
     * @param scholarshipString string of scholarship from textField
     * @return true if scholarshipAmount is valid, false if not
     */
    private boolean isValidScholarshipAmount(String scholarshipString) {
        try {
            int maxScholarshipAmount = 10000;
            if (Integer.parseInt(scholarshipString) <= 0 ||
                    Integer.parseInt(scholarshipString) > maxScholarshipAmount) {
                textArea.appendText(scholarshipString + ": invalid amount.\n");
                return false;
            }
        } catch (NumberFormatException nfe) {
            textArea.appendText("Amount is not an integer.\n");
            return false;
        }
        return true;
    }

    /**
     * Updates the scholarship amount of a Student
     * @param event ActionEvent handler of user specified object
     */
    @FXML
    private void scholarShipUpdateAmount(ActionEvent event){
        if (isValidInputScholarship(event)) {
            String firstName = ScholarshipFirstNameTextField.getText();
            String lastName = ScholarshipLastNameTextField.getText();
            String date = ScholarshipDatePicker.getValue().toString();
            Date dateOfBirth = new Date(date);
            Profile studentProfile = new Profile(lastName, firstName, dateOfBirth);
            if (rutgersRoster.contains(studentProfile)){
                Student studentToAward = rutgersRoster.findStudent(studentProfile);
                EnrollStudent studentEnrollFind =
                        enrollRoster.findStudent(studentProfile);
                if (studentToAward.isResident() && studentEnrollFind != null) { //check if resident
                    Resident residentToAward = (Resident) studentToAward;
                    if (residentToAward.isFullTime(studentEnrollFind.getCreditsEnrolled())) {
                        int scholarship = Integer.parseInt(ScholarshipAmount.getText());
                        residentToAward.setScholarship(scholarship);
                        textArea.appendText(studentProfile +
                                ": scholarship amount updated.\n");
                    } else { textArea.appendText(studentProfile + 
                            " part time student is not " +
                            "eligible for the scholarship.\n");}
                } else { //not resident or they're not enrolled
                    textArea.appendText(studentProfile + " "
                            + studentToAward.invalidStudent() +
                            "is not eligible for the scholarship.\n");
                }
            } else { 
                textArea.appendText(studentProfile + " is not in the roster.\n");
            }
        }
    }

    /**
     * Prints students sorted by their Profile
     * @param event ActionEvent handler of user interaction object
     */
    @FXML
    private void printByProfile(ActionEvent event){
        if (rutgersRoster.isRosterEmpty()) {
            textArea.appendText("Student roster is empty!\n");
        }
        else {
            rutgersRoster.print(textArea);
        }

    }

    /**
     * Prints students sorted by School and Major
     * @param event ActionEvent handler of user interaction object
     */
    @FXML
    private void printbySchoolandMajor(ActionEvent event){
        if (rutgersRoster.isRosterEmpty()) {
            textArea.appendText("Student roster is empty!\n");
        }
        else {
            rutgersRoster.printBySchoolMajor(textArea);
        }
    }

    /**
     * Prints students sorted by their lexicographical class standing
     * @param event ActionEvent handler for user interaction specified object
     */
    @FXML
    private void printbyStanding(ActionEvent event){
        if (rutgersRoster.isRosterEmpty()) {
            textArea.appendText("Student roster is empty!\n");
        }
        else {
            rutgersRoster.printByStanding(textArea);
        }
    }

    /**
     * Prints all students in RBS
     * @param event ActionEvent handler for user interaction specified object
     */
    @FXML
    private void rbsPrint(ActionEvent event){
        if (rutgersRoster.isRosterEmpty()) {
            textArea.appendText("Student roster is empty!\n");
        }
        else {
            rutgersRoster.printBySchool(textArea, Major.BAIT.getSchool().toString());
        }
    }

    /**
     * Prints all students in SAS
     * @param event ActionEvent handler for user interaction specified object
     */
    @FXML
    private void sasPrint(ActionEvent event){
        if (rutgersRoster.isRosterEmpty()) {
            textArea.appendText("Student roster is empty!\n");
        }
        else {
            rutgersRoster.printBySchool(textArea, Major.CS.getSchool().toString());
        }

    }

    /**
     * Prints all students in SOE
     * @param event ActionEvent handler for user interaction specified object
     */
    @FXML
    private void soePrint(ActionEvent event){
        if (rutgersRoster.isRosterEmpty()) {
            textArea.appendText("Student roster is empty!\n");
        }
        else {
            rutgersRoster.printBySchool(textArea, Major.EE.getSchool().toString());
        }

    }

    /**
     * Prints all students in SC&I school
     * @param event ActionEvent handler for user interaction specified object
     */
    @FXML
    private void sciPrint(ActionEvent event){
        if (rutgersRoster.isRosterEmpty()) {
            textArea.appendText("Student roster is empty!\n");
        }
        else {
            rutgersRoster.printBySchool(textArea, Major.ITI.getSchool().toString());
        }

    }

    /**
     * Prints all students with EE Major
     * @param event ActionEvent handler for user interaction specified objects
     */
    @FXML
    private void eePrint(ActionEvent event){
        if (rutgersRoster.isRosterEmpty()) {
            textArea.appendText("Student roster is empty!\n");
        }
        else {
            rutgersRoster.printBySchool(textArea, Major.EE.getSchool().toString());
        }
    }

    /**
     * Prints all students in the Enrollment list in the order of the array
     * @param event ActionEvent handler for user interaction specified object
     */
    @FXML
    private void printEnrolled(ActionEvent event){
        if (enrollRoster.isEnrollmentEmpty()) {
            textArea.appendText("Enrollment is empty!\n");
        }
        else {
            textArea.appendText("** Enrollment **\n");
            enrollRoster.print(textArea);
            textArea.appendText("* end of enrollment *\n");
        }
    }

    /**
     * Display the tuition dues for students in the Enrollment List
     * @param event ActionEvent handler for user interaction specified object
     */
    @FXML
    private void displayTuition(ActionEvent event){
        if (rutgersRoster.isRosterEmpty()) {
            textArea.appendText("Student roster is empty!\n");
        }
        else {
            textArea.appendText("** Tuition due **\n");
            EnrollStudent[] enrollList = enrollRoster.getEnrollStudents();
            Student[] rosterList = rutgersRoster.getRoster();
            for (int i = 0; i < enrollRoster.getSize(); i++){
                EnrollStudent tempEnrollStudent = enrollList[i];
                if (tempEnrollStudent != null) {
                    DecimalFormat dfLarge = new DecimalFormat( "##,###.00" );
                    DecimalFormat dfSmall = new DecimalFormat( "#,###.00");
                    Student tempStudent = rutgersRoster.findStudent(tempEnrollStudent.getProfile());
                    double tuition =
                            tempStudent.tuitionDue(tempEnrollStudent.getCreditsEnrolled());
                    String formattedTuition;
                    if (tuition >= 10000) {
                        formattedTuition = dfLarge.format(tuition);
                    }
                    else {
                        formattedTuition = dfSmall.format(tuition);
                    }
                    textArea.appendText(tempStudent.getProfile().toString() +
                            " " + tempStudent.invalidStudent() + "enrolled" +
                            " " +
                            tempEnrollStudent.getCreditsEnrolled() + " credits: " +
                            "tuition " +
                            "due: $" + formattedTuition + "\n");
                }
            }
            textArea.appendText("* end of tuition due *\n");
        }
    }

    /**
     * Loads students to the roster from an external file
     * @param event ActionEvent handler for user interaction specified object
     */
    @FXML
    private void loadFromFile(ActionEvent event) {
        if (fileToLoad.equals(""))
            textArea.appendText("Missing data in line command.\n");
        else {
            Scanner studentList;
            try {
                studentList = new Scanner(new File(fileToLoad.getText()));
            } catch (FileNotFoundException e) {
                //throw new RuntimeException(e);
                textArea.appendText("File not found.\n");
                return;
            }
            while (studentList.hasNextLine()) {
                String dataToken = studentList.nextLine();
                String[] inputsFromFile = dataToken.split(",");
                String typeOfStudent = inputsFromFile[0];
                String firstName = inputsFromFile[1];
                String lastName = inputsFromFile[2];
                String dateOfBirth = inputsFromFile[3];
                Date studentDate = new Date(dateOfBirth, true);
                String majorSubject = inputsFromFile[4];
                String creditsEnrolled = inputsFromFile[5];
                Student studentToAdd;
                Major studentMajor = returnMajor(majorSubject);
                if (studentMajor != null && studentDate.isValid() && isValidCredits(creditsEnrolled)) {
                    Profile studentProfile = new Profile(lastName, firstName, studentDate);
                    int credits = Integer.parseInt(creditsEnrolled);
                    studentToAdd = createStudentType(typeOfStudent,
                            studentProfile, studentMajor, credits, inputsFromFile);
                    if (studentToAdd != null) {
                        if (!rutgersRoster.contains(studentToAdd)) {
                            rutgersRoster.add(studentToAdd);
                        }
                    }
                }
            }
            textArea.appendText("Students loaded to the roster.\n");
        }
    }

    /**
     * Create a student type for Student dependent on Status RadioButton
     * @param dataToken dataToken read in from input file
     * @param studentProfile Profile object read in from input file
     * @param studentMajor Major attribute read in from input file
     * @param credits attribute for Student
     * @param inputs array for Student type creation
     * @return Student object of specified type
     */
    private Student createStudentType(String dataToken, Profile studentProfile,
                                      Major studentMajor, int credits, String[] inputs) {
        if (dataToken.equals("AR") || dataToken.equals("R")) {
            return new Resident(studentProfile, studentMajor, credits);
        } else if (dataToken.equals("AT") || dataToken.equals("T")) {
            String state;
            try {
                state = inputs[6];
            }
            catch (ArrayIndexOutOfBoundsException exception) {
                return null;
            }
            if (state.equalsIgnoreCase("CT") || state.equalsIgnoreCase("NY")) {
                return new TriState(studentProfile, studentMajor, credits,
                        state);
            }
            else {
                return null;
            }
        } else if (dataToken.equals("AI") || dataToken.equals("I")) {
            boolean studiesAbroad;
            try {
                studiesAbroad = Boolean.parseBoolean(inputs[6]);
            }
            catch (ArrayIndexOutOfBoundsException exception) {
                studiesAbroad = false;
            }
            return new International(studentProfile,
                    studentMajor, credits, studiesAbroad);
        } else if (dataToken.equals("AN") || dataToken.equals("N")) {
            return new NonResident(studentProfile, studentMajor, credits);
        } else
            return null;
    }

    /**
     * Returns a Major object if given a major in the input String, null otherwise
     * @param majorSubject a String input containing a major name
     * @return a Major object
     */
    private Major returnMajor(String majorSubject) {
        Major studentMajor;
        if (majorSubject.equalsIgnoreCase("CS")) {
            studentMajor = Major.CS;
        } else if (majorSubject.equalsIgnoreCase("ITI")) {
            studentMajor = Major.ITI;
        } else if (majorSubject.equalsIgnoreCase("BAIT")) {
            studentMajor = Major.BAIT;
        } else if (majorSubject.equalsIgnoreCase("EE")) {
            studentMajor = Major.EE;
        } else if (majorSubject.equalsIgnoreCase("MATH")) {
            studentMajor = Major.MATH;
        } else {
            return null;
        }
        return studentMajor;
    }

    /**
     * Ends the semester for students and adds CreditsEnrolled to CreditsCompleted
     * and prints out Students available for graduation
     * @param event ActionEvent Handler for specified user interaction object
     */
    @FXML
    private void semesterEnd(ActionEvent event) {
        Student[] rosterList = rutgersRoster.getRoster();
        EnrollStudent[] enrollList = enrollRoster.getEnrollStudents();
        for (int i = 0; i < enrollRoster.getSize(); i++) {
            EnrollStudent tempEnrollStudent = enrollList[i];
            if (tempEnrollStudent != null) {
                Student tempStudent =
                        rutgersRoster.findStudent(tempEnrollStudent.getProfile());
                tempStudent.updateCredits(enrollList[i].getCreditsEnrolled());
            }
        }
        textArea.appendText("Credit completed has been updated.\n");
        textArea.appendText("** list of students eligible for graduation **\n");

        for (int i = 0; i < rutgersRoster.getSize(); i++) {
            int creditsForGraduation = 120;
            Student student = rosterList[i];
            if (student != null) {
                if (student.getCreditCompleted() >= creditsForGraduation) {
                    textArea.appendText(student.getProfile().toString() + " " +
                            student.getMajor().toString() + student.toString()
                            + rutgersRoster.getStanding(student).toString() +
                            student.getClassification() + "\n");
                }
            }
        }
    }

    @FXML
    private TextField rosterFirstName;

    @FXML
    private TextField rosterLastName;

    @FXML
    private Button RosterAddButton;

    @FXML
    private RadioButton RosterBAITButton;

    @FXML
    private RadioButton RosterCSButton;

    @FXML
    private RadioButton RosterCTButton;

    @FXML
    private Button RosterChangeMajorButton;

    @FXML
    private TextField RosterCreditsCompleted;

    @FXML
    private DatePicker RosterDatePicker;

    @FXML
    private RadioButton RosterECEButton;

    @FXML
    private RadioButton RosterITIButton;

    @FXML
    private RadioButton RosterInternationalButton;

    @FXML
    private Button LoadFromFileButton;

    @FXML
    private RadioButton RosterMATHButton;

    @FXML
    private RadioButton RosterNYButton;

    @FXML
    private RadioButton RosterNonResidentButton;

    @FXML
    private Button RosterRemoveButton;

    @FXML
    private RadioButton RosterResidentButton;

    @FXML
    private CheckBox RosterStudyAbroadButton;

    @FXML
    private RadioButton RosterTriStateButton;

    @FXML
    private Button EnrollButton;

    @FXML
    private TextField EnrollCredits;

    @FXML
    private DatePicker EnrollDatePicker;

    @FXML
    private Button EnrollDrop;

    @FXML
    private TextField EnrollFirstNameTextField;

    @FXML
    private TextField EnrollLastNameTextField;

    @FXML
    private Menu MenuEnroll;

    @FXML
    private Menu MenuRoster;

    @FXML
    private Menu MenuSchool;

    @FXML
    private MenuItem PrintEnroll;

    @FXML
    private MenuItem PrintProfile;

    @FXML
    private MenuItem PrintSchoolandMajor;

    @FXML
    private MenuItem PrintSemesterEnd;

    @FXML
    private MenuItem PrintStanding;

    @FXML
    private MenuItem PrintTuition;

    @FXML
    private Button ScholarShipButton;

    @FXML
    private TextField ScholarshipAmount;

    @FXML
    private DatePicker ScholarshipDatePicker;

    @FXML
    private TextField ScholarshipFirstNameTextField;

    @FXML
    private TextField ScholarshipLastNameTextField;

    @FXML
    private MenuItem SchoolRBS;

    @FXML
    private MenuItem SchoolSAS;

    @FXML
    private MenuItem SchoolSCI;

    @FXML
    private MenuItem SchoolSOE;

    @FXML
    private TextArea textArea;

    @FXML
    private TextField fileToLoad;
}