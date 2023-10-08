package com.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.EasyExcel;
import com.modle.Student;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
    public static void WriteExcel(String url){
        EasyExcel.write(url, Student.class).sheet("数据列表");
    }
//    public static List<Student> getData(){
//        List<Student> list = new ArrayList<>();
//        for (int i =0; i < 20; i++){
//            Student student = new Student();
//
//        }
//    }


}


