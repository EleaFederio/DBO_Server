package sample.Systems;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Systems.recordsList.EventRecordList;
import sample.Systems.recordsList.FeesRecordList;
import sample.Utilities.Database;
import sample.Utilities.Generate;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Records implements Initializable{
    static int studentIdNumber = 0;
    Database database = new Database();
    Generate generate = new Generate();
    @FXML private TableView<EventRecordList> eventTable = new TableView<>();
    @FXML private TableColumn<EventRecordList, String> eventNameColumn = new TableColumn<>();
    @FXML private TableColumn<EventRecordList, String> eventAttendanceColumn = new TableColumn<>();
    @FXML private TableView<FeesRecordList> feesRecordTable = new TableView<>();
    @FXML private TableColumn<FeesRecordList, String> feesNameColumn = new TableColumn<>();
    @FXML private TableColumn<FeesRecordList, String> feesAttendanceColumn = new TableColumn<>();

    @FXML private JFXTextField studentIdField = new JFXTextField();
    @FXML private Label firstNameLabel = new Label();
    @FXML private Label lastNameLabel = new Label();
    @FXML private Label yearAndBlockLabel = new Label();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void fillEventTable(){
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventAttendanceColumn.setCellValueFactory(new PropertyValueFactory<>("presentOrAbsent"));
        eventTable.getColumns().clear();
        eventTable.setItems(getEventsAttendance());
        eventTable.getColumns().addAll(eventNameColumn, eventAttendanceColumn);
    }

    public void fillFeesColumn(){
        feesNameColumn.setCellValueFactory(new PropertyValueFactory<>("feesName"));
        feesAttendanceColumn.setCellValueFactory(new PropertyValueFactory<>("presentOrAbsent"));
        feesRecordTable.getColumns().clear();
        feesRecordTable.setItems(getFeesPayments());
        feesRecordTable.getColumns().addAll(feesNameColumn, feesAttendanceColumn);
    }

    public void searchStudent(){
        if (studentIdField.getText().equals("")){
            System.out.println("please insert id number");
        }else {
            String selectThisStudent = "SELECT * FROM `students` WHERE `studentId` =  '"+studentIdField.getText()+"' ";
            System.out.println(selectThisStudent);
            try {
                database.connect();
                Statement statement = database.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectThisStudent);
                while (resultSet.next()){
                    studentIdNumber = resultSet.getInt("id");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    int tempCourse = resultSet.getInt("course");
                    String stringCourse = "";
                    if (tempCourse == 1){
                        stringCourse = "BSCS";
                    }else if (tempCourse == 2){
                        stringCourse = "BAT";
                    }else if (tempCourse == 3){
                        stringCourse = "BEED";
                    }else if (tempCourse == 4){
                        stringCourse = "BSED";
                    }
                    String course = stringCourse;
                    int year = resultSet.getInt("year");
                    String block = resultSet.getString("block");
                    firstNameLabel.setText(firstName);
                    lastNameLabel.setText(lastName);
                    yearAndBlockLabel.setText(year+" "+block);
                    studentIdField.clear();
                    //System.out.println(studentIdNumber+" "+firstName+" "+lastName+" "+course+" "+year+" "+block);
                }
                database.connection.close();
                fillEventTable();
                fillFeesColumn();
            }catch (SQLException sql){
                System.out.println(sql.getErrorCode());
            }catch (Exception ex){
                System.out.println(ex);
            }
        }
        studentIdField.clear();
    }

    public ObservableList<EventRecordList> getEventsAttendance(){
        ObservableList<EventRecordList> eventRecordLists = FXCollections.observableArrayList();
        String presentOrAbsent;
        String eventName;
        String selectEvents = "SELECT * FROM `events`";
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectEvents);
            while (resultSet.next()){
                eventName = resultSet.getString("eventName");
                try {
                    String checkAttendance = "SELECT * FROM `"+generate.eventsTableName(eventName)+"` WHERE `student` = "+studentIdNumber+" ";
                    Statement statementOne = database.connection.createStatement();
                    ResultSet resultSetOne = statementOne.executeQuery(checkAttendance);
                    if (resultSetOne.next()){
                        presentOrAbsent = "√";
                        eventRecordLists.add(new EventRecordList(eventName, presentOrAbsent));
                    }else {
                        presentOrAbsent = "x";
                        eventRecordLists.add(new EventRecordList(eventName, presentOrAbsent));
                    }
                }catch (SQLException sql){
                    sql.printStackTrace();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            database.connection.close();
        }catch (SQLException sql){
            System.out.println(sql.getErrorCode());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return eventRecordLists;
    }

    public ObservableList<FeesRecordList> getFeesPayments(){
        ObservableList<FeesRecordList> feesRecordLists = FXCollections.observableArrayList();
        String feesName;
        String presentOrAbsent;
        String selectFees = "SELECT * FROM fees";
        try{
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectFees);
            while (resultSet.next()){
                feesName = resultSet.getString("contributionName");
                try {
                    String checkPayment = "SELECT * FROM `"+generate.feesTableName(feesName)+"` WHERE `student` = "+studentIdNumber+" ";
                    Statement statementOne = database.connection.createStatement();
                    ResultSet resultSetOne = statementOne.executeQuery(checkPayment);
                    if (resultSetOne.next()){
                        presentOrAbsent = "√";
                        feesRecordLists.add(new FeesRecordList(feesName, presentOrAbsent));
                    }else {
                        presentOrAbsent = "x";
                        feesRecordLists.add(new FeesRecordList(feesName, presentOrAbsent));
                    }
                }catch (SQLException sql){
                    sql.printStackTrace();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                database.connection.close();
            }
        }catch (SQLException sql){
            sql.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return feesRecordLists;
    }



}
