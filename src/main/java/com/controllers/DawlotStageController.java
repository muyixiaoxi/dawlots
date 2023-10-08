package com.controllers;

import com.modle.Plan;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public class DawlotStageController {

    public Button startBtn;
    public Button stopBtn;
    public Button exitBtn;

    private Set<String> commonStudents = new HashSet<>();
    private List<TextArea> textAreas=new ArrayList<>();

    private Plan plan;
    @FXML
    private Label schoolName;
    @FXML
    private Label stuSum;
    @FXML
    private Label number;
    @FXML
    private Label lCount;

    @FXML
    private GridPane gridPane;

    private boolean isRunning;
    private Timer timer;
    private Random random2;
    private Random random = new Random();

    private boolean isStart=true;

    private int aCount=0;

    private List<String> selectedStudents = new ArrayList<>();
    public void initialize() {
        isStart=true;
        plan = PlanSettingController.data.get(PlanSettingController.schoolCount);
        schoolName.setText(plan.getSchoolName());
        stuSum.setText(plan.getStuSum());
        number.setText(plan.getNumber());
        start();
        exitBtnEvent();
    }

    public void start() {

        startBtn.setOnAction(event -> {

            // 执行操作
            String[] rows = plan.getList().split("\n");

            //填写表格

            if(isStart){
                int count = -1;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 25; j++) {
                        count++;
                        if (count >= rows.length) {
                            break;
                        }
                        TextArea textArea = new TextArea(rows[count]);
                        textAreas.add(textArea);
                        textArea.setEditable(false);
                        textArea.setStyle("-fx-font-size: 10px; -fx-alignment: center;-fx-pref-height: 15px");
                        gridPane.add(textArea, i, j);
                    }
                }
            }
            String filePath = System.getProperty("user.dir") + File.separator+"recommend.xls";
            List<String> data = readExcel(filePath);

            List<String> selectedStudents = randomSelectStudents(rows, Integer.parseInt(plan.getNumber()) - data.size(), data);

            exportExcel(selectedStudents);

            startRandomizing();
            isStart=false;
        });

    }

    private void startRandomizing() {
        // 检查是否正在运行
        if (isRunning) {
            return;
        }

        // 设置状态为正在运行
        isRunning = true;

        // 创建时间间隔
        timer = new Timer();

//        // 禁用开始按钮
//        startBtn.setDisable(true);

        // 开始随机排列
        animateRandomizing();
    }

    @FXML
    private void stopRandomizing() {
        // 检查是否运行中
        if (!isRunning) {
            return;
        }
        // 设置状态为停止运行
        isRunning = false;
        //取消定时器任务
        timer.cancel();
    }

    private void animateRandomizing() {
        // 检查是否在运行
        if (!isRunning) {
            return;
        }

        // 增加计数器

        int target = Integer.parseInt(plan.getNumber());

        if (aCount<target) {
            aCount += (int) Math.ceil(target * 0.2);
        }else{
            aCount=target;
        }

        Platform.runLater(() -> {
            lCount.setText(String.valueOf(aCount));
        });

        // 打乱文本区列表的顺序
        Collections.shuffle(textAreas);

        // 清空网格面板
        Platform.runLater(() -> gridPane.getChildren().clear());

        // 循环添加已打乱顺序的文本区到网格面板
        for (int i = 0; i < textAreas.size(); i++) {
            TextArea textArea = textAreas.get(i);
            int row = i / 2;
            int column = i % 2;
            final int finalRow = row;
            final int finalColumn = column;
            Platform.runLater(() -> gridPane.add(textArea, finalColumn, finalRow));
        }

        // 检查计数器是否达到目标值
        if (aCount < Integer.parseInt(plan.getNumber())) {
            // 设置延迟，然后进行下一次随机排列
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    animateRandomizing();
                }
            }, 1000);
        } else {
            // 排序达到目标值，停止动画
            isRunning = false;
            startBtn.setDisable(false);

            Platform.runLater(() -> {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("提示");
                successAlert.setHeaderText(null);
                successAlert.setContentText("抽取完毕！");
                successAlert.showAndWait();
                timer.cancel();
                timer.purge();

            });
            startBtn.setDisable(true);
            addSelectedStudentsToGridPane();
        }

    }

    public void addSelectedStudentsToGridPane(){
        // 清空网格面板
        Platform.runLater(() -> gridPane.getChildren().clear());

        textAreas.clear();

        // 将selectedStudents中的每个元素填入网格面板
        int numColumns = 2; // 每行的列数
        int rowIndex = 0;
        int columnIndex = 0;
        for (String student : selectedStudents) {
            TextArea textArea = new TextArea(student); // 每个元素作为文本区域的内容
            textArea.setStyle("-fx-font-size: 12px; -fx-alignment:bottom-center; -fx-pref-height: 15px");
            textAreas.add(textArea);


            VBox vBox = new VBox(textArea); // 将文本区域放置在垂直布局中
            vBox.setAlignment(Pos.BOTTOM_CENTER); // 垂直居下对齐


            final int finalRowIndex = rowIndex;
            final int finalColumnIndex = columnIndex;
            Platform.runLater(() -> {
//                gridPane.add(textArea, finalColumnIndex, finalRowIndex);
                gridPane.add(vBox, finalColumnIndex, finalRowIndex); // 将垂直布局直接添加到GridPane中
            });
            columnIndex++;
            if (columnIndex == numColumns) {
                columnIndex = 0;
                rowIndex++;
            }
        }


    }

    public void exitBtnEvent() {
        exitBtn.setOnAction(event -> {
            //执行操作
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("index.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            exitBtn.getScene().setRoot(root);
        });
    }

    //比对保送名单
    public void foxMessage(List<String> students1, List<String> students2) {
        Iterator<String> iterator1 = students1.iterator();
        while (iterator1.hasNext()) {
            String student = iterator1.next();
            String id1 = getStudentID(student);

            Iterator<String> iterator2 = students2.iterator();
            while (iterator2.hasNext()) {
                String otherStudent = iterator2.next();
                String id2 = getStudentID(otherStudent);

                if (id1.equals(id2)) {
                    commonStudents.add(student);
                    iterator2.remove(); // 移除students2中与commonStudents相同身份证号的学生
                    break;
                }
            }
        }

        students1.removeAll(commonStudents);

    }

    public String getStudentID(String student) {
        String[] parts = student.split("\t");
        return parts[2].trim();
    }


    //进行抽取
    public List<String> randomSelectStudents(String[] rows, int n,List<String> data) {
        //把rows数组内容添加到list集合中
        List<String> oriStudents=new ArrayList<>();
        for(String row:rows){
            oriStudents.add(row);
        }
        //比对保送名单
        foxMessage(data,oriStudents);



        // 分别保存男生和女生
        List<String> maleStudents = new ArrayList<>();
        List<String> femaleStudents = new ArrayList<>();

        // 将学生信息按性别分类
        for (String row : oriStudents) {
            if (row.contains("男")) {
                maleStudents.add(row);
            } else if (row.contains("女")) {
                femaleStudents.add(row);
            }
        }

        if(n<=40-data.size()){

        }else{

        }


        // 计算男女生要抽取数量1:1录取，如果为奇数，男生比女生多一个
        int totalCount = n;  // 总人数
        int maleCount = totalCount / 2;  // 男生数量
        int femaleCount = totalCount - maleCount;  // 女生数量

        // 打乱男生和女生的顺序
        Collections.shuffle(maleStudents, random);
        Collections.shuffle(femaleStudents, random);

        // 从男生中抽取指定数量的学生
        for (int i = 0; i < maleCount && i < maleStudents.size(); i++) {
            selectedStudents.add(maleStudents.get(i));
        }

        // 从女生中抽取指定数量的学生
        for (int i = 0; i < femaleCount && i < femaleStudents.size(); i++) {
            selectedStudents.add(femaleStudents.get(i));
        }

        //将报送学生添加到集合
        selectedStudents.addAll(commonStudents);
        Collections.shuffle(selectedStudents, random);


        return selectedStudents;
    }

    //导出抽取学生的名单表

    public void exportExcel( List<String> selectedStudents){
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("学生信息");

        //创建表头
        Row headerRow = sheet.createRow(0);
        String[] header = {"序号", "学生姓名", "性别", "身份证号"};
        for (int i = 0; i < header.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(header[i]);
        }


        // 填充学生数据
        for (int i = 0; i < selectedStudents.size(); i++) {
            Row dataRow = sheet.createRow(i + 1);
            String studentData = selectedStudents.get(i);
            String[] dataCell=studentData.split("\t");
            for(int j=0;j<dataCell.length;j++){
                Cell cell=dataRow.createCell(j);
                cell.setCellValue(dataCell[j]);
            }

        }

        try (FileOutputStream fileOut = new FileOutputStream(plan.getSchoolName()+"_selected_students.xls")) {
            workbook.write(fileOut);
            System.out.println("Excel 文件保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //读取保送名单的学生excel表
    public List<String> readExcel(String filePath){
        //路径：D:\JAVA\id\workspace\dawlots\recommend.xls
        List<String> data = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(filePath);
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表

            // 遍历每一行数据
            Iterator<Row> rowIterator = sheet.iterator();
            int rowCount = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (rowCount < 2) {
                    rowCount++;
                    continue; // 跳过前两行
                }

                StringBuilder rowData = new StringBuilder();

                // 遍历每个单元格
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell.getCellType() == CellType.BLANK) {
                        continue; // 跳过空单元格
                    }
                    String cellValue;
                    if (cell.getCellType() == CellType.NUMERIC) {
                        DataFormatter formatter = new DataFormatter();
                        cellValue = formatter.formatCellValue(cell);
                    } else {
                        cellValue = getCellValue(cell);
                    }
                    rowData.append(cellValue).append("\t");
                }

                // 将非空行数据添加到集合中
                if (rowData.length() > 0) {
                    data.add(rowData.toString().trim());
                }
            }

            // 关闭 Workbook 和输入流
            workbook.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getCellValue(Cell cell){
        String cellValue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    cellValue = "";
                    break;
                default:
                    cellValue = "";
            }
        }
        return cellValue;
    }


}
