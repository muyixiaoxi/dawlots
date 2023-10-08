package com.modle;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Student {

    @ExcelProperty(value = "序号",index = 0)
    private String id;
    @ExcelProperty(value = "学生姓名",index = 1)
    private String name;
    @ExcelProperty(value = "性别",index = 2)
    private String sex;
    @ExcelProperty(value = "身份证号",index = 3)
    private String identityCard;
    @ExcelProperty(value = "联系电话",index = 4)
    private String phone;


    public Student(){}

    public Student(String id, String name, String sex, String identityCard, String phone, boolean flag) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.identityCard = identityCard;
        this.phone = phone;

    }
}
