package com.controllers;

import com.modle.Plan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;


public class PlanSettingController {

    public Button reUpdate;
    public static  Plan selectedPlan; // 选中行
    //计数第几个学校
    public static int schoolCount=-1;
    public Button addPlan;
    public Button upPlan;
    public Button saPlan;
    public Button clPlan;
    public Button dePlan;
    public Button exPlan;

    public Button imPlan;

    public AnchorPane p1;
    //及时刷新
    public static ObservableList<Plan> data = FXCollections.observableArrayList();
    //计算报名学生总数
    private Integer rowCount;
    //存储表格数据
    private StringBuilder sb;



    @FXML
    private TextField tSchoolName;//学校名称输入框
    @FXML
    private TextField tPlan; //文件类型输入框
    @FXML
    private TextField tStuSum;//导入学生总数量
    @FXML
    private TableView<Plan> pTable ;//学校名称和招生计划整个大容器
    @FXML
    private TableColumn<Plan, String> tcSchoolName ;
    @FXML
    private TableColumn<Plan, String> tcPlan ;
    @FXML
    private TableColumn<Plan, String> tcStuSum;
    @FXML
    private TableColumn<Plan, Void> actionColumn;


    //表格数据绑定
    public void initialize() {

        pTable.setItems(data);
        //必须调用一次
        getRow();
        selectExcel();

        //如果文本框未填写，则不允许导入名单
        imPlan.disableProperty().bind(tSchoolName.textProperty().isEmpty());
        imPlan.disableProperty().bind(tPlan.textProperty().isEmpty());
        addPlan.disableProperty().bind(tSchoolName.textProperty().isEmpty());
        addPlan.disableProperty().bind(tPlan.textProperty().isEmpty());

        //监听点击的行

        tcSchoolName.setCellValueFactory(cellData -> cellData.getValue().schoolNameProperty());
        tcPlan.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
        tcStuSum.setCellValueFactory(cellData -> cellData.getValue().stuSumProperty());


        //需要给 tPlan 加个监听器，过滤非数字内容
        textFormatter();


        actionColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Plan, Void> call(TableColumn<Plan, Void> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
//                          Plan plan = getTableRow().getItem();
                            Plan plan = getTableView().getItems().get(getIndex()); // 获取当前行的 Plan 对象
                            if (plan != null) {
                                setGraphic(plan.getActionButton());
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });
        // 设置列的数据提供
        actionColumn.setCellValueFactory(cellData -> null);

//        pTable.getColumns().add(actionColumn);


    }

    //点击表格选中行
    public void getRow(){
        pTable.setRowFactory(a -> {
            TableRow<Plan> row = new TableRow<>();

            row.setOnMouseClicked(event ->{
                if (event.getClickCount() == 1 && (!row.isEmpty())){
                    selectedPlan = row.getItem();
                    System.out.println(selectedPlan);
                }
            });

            return row;
        });
    }

    // 招生计划文本框只能输入数组
    public void textFormatter(){
        // 招生计划 文本框只能输入数字
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        });
        tPlan.setTextFormatter(textFormatter);
    }


    //退出
    public void exPlanEvent(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("index.fxml"));

        exPlan.getScene().setRoot(root);
    }

    // 添加
    public void addPlanEvent(ActionEvent actionEvent) {
        if (rowCount==0){
            return;
        }
        Plan plan = new Plan(tSchoolName.getText(),tPlan.getText(),rowCount.toString(),sb.toString());
        addButtonEvent(plan);

        data.add(plan);
        PlanSettingController.schoolCount++;

        //添加完后置空
        tSchoolName.setText("");
        tPlan.setText("");
    }

    //每添加一行数据,一个“摇号”按钮
    public void addButtonEvent(Plan plan){
        // 创建操作按钮
        Button actionButton = new Button("摇号");
        actionButton.setMinWidth(100);
        actionButton.setOnAction(event -> {
            // 执行操作
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("dowlot.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            actionButton.getScene().setRoot(root);
            plan.getActionButton().setDisable(false);
        });

        // 设置操作按钮到对应的 Plan 对象中
        plan.setActionButton(actionButton);

    }



    // 修改
    public void upPlanEvent(ActionEvent actionEvent) throws IOException {
        if(selectedPlan == null){
            return;
        }

        Parent root = FXMLLoader.load(getClass().getResource("updatePlan.fxml"));

        // 创建新的stage
        Stage secondStage = new Stage();
        Scene secondScene = new Scene(root, 600, 400);
        secondStage.setScene(secondScene);
        secondStage.show();

    }

    //选择Excel文件
    public void selectExcel(){

        imPlan.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel 文件", "*.xls") // 只允许选择xls文件
            );
            //获取当前stage
            Stage currentStage = (Stage) imPlan.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(currentStage);

            if (selectedFile != null) {
                loadExcelData(selectedFile);
            }
        });

    }

    //加载excel文件数据
    private void loadExcelData(File file) {
        try (Workbook workbook = new HSSFWorkbook(new FileInputStream(file))) {
            Sheet sheet = workbook.getSheetAt(0);

            sb = new StringBuilder();
            rowCount=0;
            DecimalFormat decimalFormat = new DecimalFormat("#0"); // 设置格式为整数形式

            for (Row row : sheet) {
                if (isRowEmpty(row)) {
                    continue;
                }
                Cell firstCell = row.getCell(0);

                if (firstCell != null && firstCell.getCellType() == CellType.NUMERIC) {
                    rowCount++;
                    for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        if (cell != null) {
                            if (cell.getCellType() == CellType.NUMERIC) {
                                sb.append(decimalFormat.format(cell.getNumericCellValue())).append("\t");
                            } else {
                                sb.append(cell.toString()).append("\t");
                            }
                        }
                    }
                    sb.append("\n");
                }
            }


        } catch (IOException ex) {
            ex.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("读取文件时出错，添加失败。");
            errorAlert.showAndWait();
            return;
        }

        if (rowCount>0||file.getName().endsWith(".xls")){
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("提示");
            successAlert.setHeaderText(null);
            successAlert.setContentText("导入成功，请确认无误后点击‘添加’！");
            successAlert.showAndWait();

        }else{
            Alert successAlert = new Alert(Alert.AlertType.ERROR);
            successAlert.setTitle("提示");
            successAlert.setHeaderText(null);
            successAlert.setContentText("导入失败！");
            successAlert.showAndWait();
        }

    }
    //判断是否为空单元行
    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }

        return true;
    }




    // 删除
    public void dePlanEvent(ActionEvent actionEvent) {
        data.remove(selectedPlan);
    }

    // 清空
    public void clPlanEvent(ActionEvent actionEvent) {
        data.clear();
    }




    public void reUpdateEvent(ActionEvent actionEvent) {
    }
}
