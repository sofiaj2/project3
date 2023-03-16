package com.example.studentinterfacegui;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.time.format.DateTimeFormatter;

public class TuitionManagerController {

    private Roster rutgersRoster = new Roster();
    private Enrollment enrollRoster = new Enrollment();

    @FXML
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
            textArea.appendText("Missing data\n");
            return null;
        }
        return major;
    }

    @FXML
    private void addRoster(ActionEvent event) {
        Major major;
        major = checkMajor(event);
        if (isValidInputAdd(event) && major != null) {
            String firstName = rosterFirstName.getText();
            String lastName = rosterLastName.getText();
            int credits = Integer.parseInt(RosterCreditsCompleted.getText());
            String date = RosterDatePicker.getValue().toString();
            Date dob = new Date(date);
            Profile profile = new Profile(firstName, lastName, dob);
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
                        textArea.appendText("Missing data\n");
                    } //error message
                } else if (RosterInternationalButton.isSelected()) {
                    if (RosterStudyAbroadButton.isSelected())
                        student = new International(profile, major, credits, true);
                    else
                        student = new International(profile, major, credits, false);
                } else {
                    textArea.appendText("Missing data\n");
                }
            } else {
                textArea.appendText("Missing data\n");
            }
            if (student != null)
                addStudentToRoster(student, rutgersRoster, firstName, lastName, date, dob);
        }

    }

    private boolean isValidInputAdd(ActionEvent event) {
        String firstName = rosterFirstName.getText();
        String lastName = rosterLastName.getText();
        String creditsCompleted = RosterCreditsCompleted.getText();
        if (firstName.equals("") || lastName.equals("")) {
            textArea.appendText("Missing data\n");
            return false;
        }
        if (!isValidCredits(creditsCompleted)) {
            textArea.appendText("Missing data\n");
            return false;
        }
        //String date = RosterDatePicker.getValue().toString();

        try {
            String date = RosterDatePicker.getValue().toString();
        }//("yyyy-mm-dd"));
        catch (NullPointerException e) {
            textArea.appendText("Missing data\n");
            return false;
        }

        return true;
    }

    private boolean isValidInputNamesandDate(ActionEvent event) {
        String firstName = rosterFirstName.getText();
        String lastName = rosterLastName.getText();
        if (firstName.equals("") || lastName.equals("")) {
            textArea.appendText("Missing data\n");
            return false;
        }

        try {
            String date = RosterDatePicker.getValue().toString();
        }//("yyyy-mm-dd"));
        catch (NullPointerException e) {
            textArea.appendText("Missing data\n");
            return false;
        }
        return true;
    }

    private void addStudentToRoster(Student student, Roster rutgersRoster,
                                    String firstName, String lastName,
                                    String dateOfBirth, Date studentDate) {
        if (student != null) {
            if (rutgersRoster.contains(student)) {
                textArea.appendText(firstName + " " + lastName + " " + dateOfBirth
                        + " is already in the roster.\n");
            } else {
                if (rutgersRoster.add(student)) {
                    textArea.appendText(firstName + " " + lastName + " " +
                            dateOfBirth + " added to the roster.\n");
                } else {
                    textArea.appendText("DOB invalid: " + studentDate +
                            " younger than 16 years old.\n");
                }
            }
        }
    }

    @FXML
    private void removeRoster(ActionEvent event) {
        String firstName = rosterFirstName.getText();
        String lastName = rosterLastName.getText();
        String date = RosterDatePicker.getValue().toString();
        Date dateOfBirth = new Date(date);
        if (isValidInputNamesandDate(event)) {
            Profile studentProfile = new Profile(firstName, lastName, dateOfBirth);
            if (!rutgersRoster.contains(studentProfile)) {
                textArea.appendText(firstName + " " + lastName + " " + dateOfBirth
                        + " is not in the roster.\n");
            } else {
                rutgersRoster.remove(studentProfile);
                textArea.appendText(firstName + " " + lastName + " " + dateOfBirth
                        + " removed from the roster.\n");
            }
        }
    }

    @FXML
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
        else {return null;}
    }


    @FXML
    private void changeMajor(ActionEvent event){
        String firstName = rosterFirstName.getText();
        String lastName = rosterLastName.getText();
        String date = RosterDatePicker.getValue().toString();
        Date dateOfBirth = new Date(date);
        Major major = checkMajor(event);
        if (isValidInputNamesandDate(event) && major != null) {
            Profile studentProfile = new Profile(firstName, lastName, dateOfBirth);
            if (!rutgersRoster.contains(studentProfile)) {
                textArea.appendText(firstName + " " + lastName + " " + dateOfBirth
                        + " is not in the roster.\n");
            } else {
                rutgersRoster.findStudent(studentProfile).setMajor(major);
                textArea.appendText(firstName + " " + lastName + " " + dateOfBirth +
                        " major changed to " +
                        printMajor(rutgersRoster.findStudent(studentProfile).getMajor()) + "\n");
            }
        }
    }

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

    @FXML
    private void triStateSelected(ActionEvent event){
        if (RosterTriStateButton.isSelected()){
            RosterStudyAbroadButton.setDisable(true);
            RosterNYButton.setDisable(false);
            RosterCTButton.setDisable(false);
            RosterStudyAbroadButton.setSelected(false);
        }
    }

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

    @FXML
    private boolean isValidInputEnroll(ActionEvent event) {
        String firstName = EnrollFirstNameTextField.getText();
        String lastName = EnrollLastNameTextField.getText();
        String creditsCompleted = EnrollCredits.getText();
        if (firstName.equals("") || lastName.equals("")) {
            textArea.appendText("Missing data\n");
            return false;
        }
        if (!isValidCredits(creditsCompleted)) {
            textArea.appendText("Missing data\n");
            return false;
        }

        try {String date = EnrollDatePicker.getValue().toString();}//("yyyy-mm-dd"));
        catch (NullPointerException e) {
            textArea.appendText("Missing data\n");
            return false;
        }
        return true;
    }

    @FXML
    private boolean isValidInputEnrollNoCredit (ActionEvent event) {
        String firstName = EnrollFirstNameTextField.getText();
        String lastName = EnrollLastNameTextField.getText();
        if (firstName.equals("") || lastName.equals("")) {
            textArea.appendText("Missing data\n");
            return false;
        }
        try {String date = EnrollDatePicker.getValue().toString();}//("yyyy-mm-dd"));
        catch (NullPointerException e) {
            textArea.appendText("Missing data\n");
            return false;
        }

        return true;
    }

    @FXML
    private void EnrollStudent(ActionEvent event){
        String firstName = EnrollFirstNameTextField.getText();
        String lastName = EnrollLastNameTextField.getText();
        String date = EnrollDatePicker.getValue().toString();
        Date dateOfBirth = new Date(date);
        if (isValidInputEnroll(event)){
            int credits = Integer.parseInt(RosterCreditsCompleted.getText());
            Profile studentProfile = new Profile(firstName, lastName, dateOfBirth);
            if (rutgersRoster.contains(studentProfile)){
                EnrollStudent newStudent = new EnrollStudent(studentProfile, credits);
                enrollRoster.add(newStudent);
                textArea.appendText(studentProfile.toString() + " enrolled " + credits
                        + "credits\n");
            }
            else{textArea.appendText("Cannot Enroll: " + studentProfile.toString() +
                    " is not in the roster.\n");}
        }
    }

    @FXML
    private void dropStudent(ActionEvent event){
        String firstName = EnrollFirstNameTextField.getText();
        String lastName = EnrollLastNameTextField.getText();
        String date = EnrollDatePicker.getValue().toString();
        Date dateOfBirth = new Date(date);
        if (isValidInputEnrollNoCredit(event)){
            Profile studentProfile = new Profile(firstName, lastName, dateOfBirth);
            EnrollStudent newEnrolledStudent = new EnrollStudent(studentProfile);
            if (!enrollRoster.contains(newEnrolledStudent)){
                textArea.appendText(firstName + " " + lastName + " " + dateOfBirth +
                        " is not enrolled.\n");
            }
            else{enrollRoster.remove(newEnrolledStudent);
                textArea.appendText(firstName + " " + lastName + " " + dateOfBirth +
                        " dropped.\n");}
        }
    }

    @FXML
    private boolean isValidCredits(String creditsCompleted) {
        if (creditsCompleted.equals(""))
            return false;
        try {
            if (Integer.parseInt(creditsCompleted) < 0) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @FXML
    private boolean isValidInputScholarship (ActionEvent event) {
        String firstName = ScholarshipFirstNameTextField.getText();
        String lastName = ScholarshipLastNameTextField.getText();
        int amount = Integer.parseInt(ScholarshipAmount.getText());
        if (firstName.equals("") || lastName.equals("")) {
            textArea.appendText("Missing data\n");
            return false;
        }
        try {String date = ScholarshipDatePicker.getValue().toString();}//("yyyy-mm-dd"));
        catch (NullPointerException e) {
            textArea.appendText("Missing data\n");
            return false;
        }

        return true;
    }

    @FXML
    private void scholarShipUpdateAmount(ActionEvent event){
        if (isValidInputEnroll(event)){
            String firstName = ScholarshipFirstNameTextField.getText();
            String lastName = ScholarshipLastNameTextField.getText();
            String date = RosterDatePicker.getValue().toString();
            Date dateOfBirth = new Date(date);
            int scholarshipAmount = Integer.parseInt(EnrollCredits.getText());
            Profile studentProfile = new Profile(firstName, lastName, dateOfBirth);
            Student studentToAward = rutgersRoster.findStudent(studentProfile);
            EnrollStudent studentEnrollFind =
                    enrollRoster.findStudent(studentProfile);
            if (rutgersRoster.contains(studentProfile)){
                if (studentToAward.isResident()) { //check if resident
                    Resident residentToAward = (Resident) studentToAward;
                    if (residentToAward.isFullTime(studentEnrollFind.getCreditsEnrolled())) {
                        residentToAward.setScholarship(scholarshipAmount);
                        textArea.appendText(studentProfile +
                                ": scholarship amount updated.\n");
                    } else { //not full time
                        textArea.appendText(" part time student is not " +
                                "eligible for the scholarship.\n");
                    }
                } else { //not resident
                    textArea.appendText(studentProfile + " " + studentToAward.invalidStudent() +
                            "is not eligible for the scholarship.\n");
                }
            } else {textArea.appendText(studentProfile + " is not in the roster.\n");}
        }
    }

    @FXML
    private void printByProfile(ActionEvent event){
        rutgersRoster.print(textArea);
    }

    @FXML
    private void printbySchoolandMajor(ActionEvent event){
        rutgersRoster.printBySchoolMajor(textArea);
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
    private Button RosterLoadFromFileButton;

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
    private Button EnrollSemesterEnd;

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
}