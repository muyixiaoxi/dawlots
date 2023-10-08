package com.controllers;

import com.modle.Plan;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.xml.crypto.Data;

public class UpdatePlanController {
    public Button reUpdate;
    public TextField tfName;
    public TextField tfPlan;

    public void initialize(){
        if(PlanSettingController.selectedPlan != null){
            tfName.setText(PlanSettingController.selectedPlan.getSchoolName());
            tfPlan.setText(PlanSettingController.selectedPlan.getNumber());
        }

    }

    // 确认修改
    public void reUpdateEvent(ActionEvent actionEvent) {
        // 修改选中行
        PlanSettingController.selectedPlan.setNumber(tfPlan.getText());
        PlanSettingController.selectedPlan.setSchoolName(tfName.getText());
        Stage stage =(Stage) reUpdate.getScene().getWindow();
        stage.close();
    }
}
