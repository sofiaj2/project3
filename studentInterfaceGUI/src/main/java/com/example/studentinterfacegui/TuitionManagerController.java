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
    private void addRoster(ActionEvent event){
        Major major;
        if (RosterBAITButton.isSelected()){major = Major.BAIT;}
        else if (RosterITIButton.isSelected()) {major = Major.ITI;}
        else if (RosterCSButton.isSelected()) {major = Major.CS;}
        else if (RosterECEButton.isSelected()) {major = Major.EE;}
        else if (RosterMATHButton.isSelected()){major = Major.MATH;}
        else {
            major = null;
            textArea.appendText("Missing data\n");
        }
        if (isValidInputAdd(event) && major != null) {
            String firstName = rosterFirstName.getText();
            String lastName = rosterLastName.getText();
            int credits = Integer.parseInt(RosterCreditsCompleted.getText());
            String date = RosterDatePicker.getValue().toString();
            Date dob = new Date(date);
            Profile profile = new Profile(firstName, lastName, dob);
            Student student = null;
            if (RosterResidentButton.isSelected()){ student = new Resident(profile, major, credits); }
            else if (RosterNonResidentButton.isSelected()){
                if (RosterTriStateButton.isSelected()) {
                    if (RosterCTButton.isSelected())
                        student = new TriState(profile, major, credits, "CT");
                    else if (RosterNYButton.isSelected())
                        student = new TriState(profile, major, credits, "NY");
                    else {textArea.appendText("Missing data\n");} //error message
                }
                else if (RosterInternationalButton.isSelected()) {
                    if (RosterStudyAbroadButton.isSelected())
                        student = new International(profile, major, credits, true);
                    else
                        student = new International(profile, major, credits, false);
                }
                else {textArea.appendText("Missing data\n");}
            }
            else {textArea.appendText("Missing data\n");}
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

        try {String date = RosterDatePicker.getValue().toString();}//("yyyy-mm-dd"));
        catch (Exception e) {
            textArea.appendText("Missing data\n");
            return false;
        }

        return true;

    }

    private void addStudentToRoster(Student student, Roster rutgersRoster,
                           String firstName, String lastName,
                           String dateOfBirth, Date studentDate){
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

    private boolean isValidCredits(String creditsCompleted) {
        if (creditsCompleted.equals(""))
            return false;
        try {
            if (Integer.parseInt(creditsCompleted) < 0) {
                //System.out.println("Credits completed invalid: cannot be negative!");
                return false;
            }
        } catch (NumberFormatException nfe) {
            //System.out.println("Credits completed invalid: not an integer!");
            return false;
        }
        return true;
    }
    @FXML
    private void removeRoster(ActionEvent event){
        String firstName = rosterFirstName.getText();
        String lastName = rosterLastName.getText();
        String date = RosterDatePicker.getValue().toString();
        Date dateOfBirth = new Date(date);
        Profile studentProfile = new Profile(firstName, lastName, dateOfBirth);
        for (int i = 0; i < rutgersRoster.getSize(); i++){
            //if (rutgersRoster[i].)
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