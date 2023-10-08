package com.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class AppController {
    //学校计划设置
    public Button schoolPlan;
    public Button showExcel;
    public Button exit;

    // 跳转学校计划设置
    public void schoolPlan(ActionEvent actionEvent) throws IOException {
//        PlanSettingController p = new PlanSettingController();
//        p.setData();

        Parent root = FXMLLoader.load(getClass().getResource("plan_setting.fxml"));
        schoolPlan.getScene().setRoot(root);
    }

    //跳转显示录取名单
    public void showExcel(ActionEvent actionEvent)throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("showExcel.fxml"));
        showExcel.getScene().setRoot(root);
    }

    // 退出
    public void exit(ActionEvent actionEvent) {
        Stage stage = (Stage)exit.getScene().getWindow();
        stage.close();

    }
}
