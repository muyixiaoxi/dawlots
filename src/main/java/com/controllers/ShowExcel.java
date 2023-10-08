package com.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ShowExcel {
    @FXML
    private ComboBox comboBox;
    @FXML
    private Button button;
    @FXML
    private Button exit;


    public void initialize(){
        //让下拉框获取所有学校名称
        for (int i = 0; i <PlanSettingController.data.size() ; i++) {
            comboBox.getItems().add(PlanSettingController.data.get(i).getSchoolName());
        }
    }
    @FXML
    private void handleOpenFile(ActionEvent event){
        String selectedSchool = comboBox.getValue().toString();
        if (selectedSchool==null){
            return;
        }
        //获取当前工作目录
        String filename=System.getProperty("user.dir") + File.separator+selectedSchool+"_selected_students.xls";

        try {
            // 打开文件
            File excelFile = new File(filename);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(excelFile);


        } catch (IOException | UnsupportedOperationException e) {
            e.printStackTrace();
        }

    }
    @FXML
    //跳转初始界面
    public void exit(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("index.fxml"));
        exit.getScene().setRoot(root);
    }



}
