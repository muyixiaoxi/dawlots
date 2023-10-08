package com.modle;

import javafx.beans.property.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;

public class Plan {
    private final StringProperty schoolName;
    private final StringProperty number;
    private final StringProperty stuSum;
    private final StringProperty list;
    private final ObjectProperty<Button> actionButton=new SimpleObjectProperty<>();


    @Override
    public String toString() {
        return "Plan{" +
                "schoolName=" + schoolName +
                ", number=" + number +
                ", strSum=" + stuSum +
                '}';
    }

    public Plan() {
        schoolName = null;
        number = null;
        stuSum=null;
        list=null;
    }

    public Plan(String schoolName, String number,String stuSum,String list) {
        this.schoolName = new SimpleStringProperty(schoolName);
        this.number = new SimpleStringProperty(number);
        this.stuSum=new SimpleStringProperty(stuSum);
        this.list=new SimpleStringProperty(list);
    }

    public String getSchoolName() {
        return schoolName.get();
    }

    public StringProperty schoolNameProperty() {
        return schoolName;
    }

    public StringProperty stuSumProperty(){
        return stuSum;
    }

    public void setStuSum(String stuSum){
        this.stuSum.set(stuSum);
    }

    public void setSchoolName(String schoolName) {
        this.schoolName.set(schoolName);
    }

    public String getNumber() {
        return number.get();
    }

    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public ObjectProperty<Button> actionButtonProperty() {
        return actionButton;
    }

    public Button getActionButton() {
        return actionButton.get();
    }

    public void setActionButton(Button button) {
        actionButton.set(button);
    }

    public String getList() {
        return list.get();
    }

    public StringProperty listProperty() {
        return list;
    }

    public void setList(String list) {
        this.list.set(list);
    }

    public String getStuSum() {
        return stuSum.get();
    }
}
