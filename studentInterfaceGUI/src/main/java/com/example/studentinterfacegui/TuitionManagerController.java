package com.example.studentinterfacegui;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.text.DecimalFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

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
            //textArea.appendText("Missing data\n");
        }
        return major;
    }

    @FXML
    private void addRoster(ActionEvent event) {
        Major major = checkMajor(event);
        boolean allInputExists = isValidInputAdd(event);
        if (allInputExists && major != null) {
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
                        allInputExists = false;
                    }
                } else if (RosterInternationalButton.isSelected()) {
                    if (RosterStudyAbroadButton.isSelected())
                        student = new International(profile, major, credits, true);
                    else
                        student = new International(profile, major, credits, false);
                } else {
                    allInputExists = false;
                }
            } else {
                allInputExists = false;
            }
            if (student != null) {
                addStudentToRoster(student, dob);
            }
        }
        if (!allInputExists || major == null)
            textArea.appendText("Missing data\n");
    }

    private boolean isValidInputAdd(ActionEvent event) {
        String firstName = rosterFirstName.getText();
        String lastName = rosterLastName.getText();
        String creditsCompleted = RosterCreditsCompleted.getText();
        if (firstName.equals("") || lastName.equals("")) {
            return false;
        }
        if (!isValidCredits(creditsCompleted)) {
            return false;
        }

        try {
            String date = RosterDatePicker.getValue().toString();
        }
        catch (NullPointerException e) {
            return false;
        }
        catch (DateTimeParseException exception) {
            return false;
        }
        catch (Exception exception) {
            return false;
        }

        return true;
    }

    private boolean isValidInputNamesandDate(ActionEvent event) {
        String firstName = rosterFirstName.getText();
        String lastName = rosterLastName.getText();
        if (firstName.equals("") || lastName.equals("")) {
            return false;
        }

        try {
            String date = RosterDatePicker.getValue().toString();
        }//("yyyy-mm-dd"));
        catch (NullPointerException e) {
            return false;
        }
        return true;
    }

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

    @FXML
    private void removeRoster(ActionEvent event) {
        String firstName = rosterFirstName.getText();
        String lastName = rosterLastName.getText();
        if (isValidInputNamesandDate(event)) {
            String date = RosterDatePicker.getValue().toString();
            Date dateOfBirth = new Date(date);
            Profile studentProfile = new Profile(lastName, firstName, dateOfBirth);
            if (!rutgersRoster.contains(studentProfile)) {
                textArea.appendText(studentProfile.toString()
                        + " is not in the roster.\n");
            } else {
                rutgersRoster.remove(studentProfile);
                textArea.appendText(studentProfile.toString()
                        + " removed from the roster.\n");
            }
        }
        else {textArea.appendText("Missing data\n");}
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
        Major major = checkMajor(event);
        if (isValidInputNamesandDate(event) && major != null) {
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
        else {textArea.appendText("Missing data\n");}
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
        if (isValidInputEnroll(event)){
            String date = EnrollDatePicker.getValue().toString();
            Date dateOfBirth = new Date(date);
            int credits = Integer.parseInt(EnrollCredits.getText());
            Profile studentProfile = new Profile(lastName, firstName, dateOfBirth);
            if (rutgersRoster.contains(studentProfile)){
                EnrollStudent newStudent = new EnrollStudent(studentProfile, credits);
                enrollRoster.add(newStudent);
                textArea.appendText(studentProfile.toString() + " enrolled " + credits
                        + " credits\n");
            }
            else{textArea.appendText("Cannot Enroll: " + studentProfile.toString() +
                    " is not in the roster.\n");}
        }
    }

    @FXML
    private void dropStudent(ActionEvent event){
        String firstName = EnrollFirstNameTextField.getText();
        String lastName = EnrollLastNameTextField.getText();
        if (isValidInputEnrollNoCredit(event)){
            String date = EnrollDatePicker.getValue().toString();
            Date dateOfBirth = new Date(date);
            Profile studentProfile = new Profile(lastName, firstName, dateOfBirth);
            EnrollStudent newEnrolledStudent = new EnrollStudent(studentProfile);
            if (!enrollRoster.contains(newEnrolledStudent)){
                textArea.appendText(studentProfile.toString() +
                        " is not enrolled.\n");
            }
            else {
                enrollRoster.remove(newEnrolledStudent);
                textArea.appendText(studentProfile.toString() + " dropped.\n");
            }
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
    private boolean isValidInputScholarship(ActionEvent event) {
        String firstName = ScholarshipFirstNameTextField.getText();
        String lastName = ScholarshipLastNameTextField.getText();
        if (firstName.equals("") || lastName.equals("")) {
            textArea.appendText("Missing data\n");
            return false;
        }
        if (!isValidCredits(ScholarshipAmount.getText())) {
            textArea.appendText("Missing data\n");
            return false;
        }
        try {String date = ScholarshipDatePicker.getValue().toString();}
        catch (NullPointerException e) {
            textArea.appendText("Missing data\n");
            return false;
        }

        return true;
    }

    @FXML
    private void scholarShipUpdateAmount(ActionEvent event){
        if (isValidInputScholarship(event)){
            String firstName = ScholarshipFirstNameTextField.getText();
            String lastName = ScholarshipLastNameTextField.getText();
            String date = ScholarshipDatePicker.getValue().toString();
            Date dateOfBirth = new Date(date);
            int scholarshipMoney = Integer.parseInt(ScholarshipAmount.getText());
            Profile studentProfile = new Profile(lastName, firstName, dateOfBirth);
            Student studentToAward = rutgersRoster.findStudent(studentProfile);
            EnrollStudent studentEnrollFind =
                    enrollRoster.findStudent(studentProfile);
            if (rutgersRoster.contains(studentProfile)){
                if (studentToAward.isResident()) { //check if resident
                    Resident residentToAward = (Resident) studentToAward;
                    if (residentToAward.isFullTime(studentEnrollFind.getCreditsEnrolled())) {
                        residentToAward.setScholarship(scholarshipMoney);
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
        textArea.appendText("can you see this\n");
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
    private void printbyStanding(ActionEvent event){
        rutgersRoster.printByStanding(textArea);
    }

    @FXML
    private void rbsPrint(ActionEvent event){
        rutgersRoster.printBySchool(textArea, Major.BAIT.getSchool().toString());
    }

    @FXML
    private void sasPrint(ActionEvent event){
        rutgersRoster.printBySchool(textArea, Major.CS.getSchool().toString());
    }

    @FXML
    private void soePrint(ActionEvent event){
        rutgersRoster.printBySchool(textArea, Major.EE.getSchool().toString());
    }

    @FXML
    private void sciPrint(ActionEvent event){
        rutgersRoster.printBySchool(textArea, Major.ITI.getSchool().toString());
    }

    @FXML
    private void eePrint(ActionEvent event){
        rutgersRoster.printBySchool(textArea, Major.EE.getSchool().toString());
    }

    @FXML
    private void printEnrolled(ActionEvent event){
        enrollRoster.print(textArea);
    }

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

    @FXML
    private void loadFromFile(ActionEvent event) {
        if (fileToLoad.equals(""))
            textArea.appendText("Missing data.\n");
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
                    System.out.println(student.getProfile().toString() + " " +
                            student.getMajor().toString() + student.toString()
                            + rutgersRoster.getStanding(student).toString() +
                            student.getClassification());
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