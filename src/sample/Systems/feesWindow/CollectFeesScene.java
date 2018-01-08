package sample.Systems.feesWindow;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import sample.Systems.LoginController;
import sample.Utilities.Database;
import sample.Utilities.Generate;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CollectFeesScene implements Initializable{
    Generate generate = new Generate();
    Database database = new Database();
    @FXML private JFXTextField studentSearchField = new JFXTextField();
    @FXML private ComboBox<String> feesList = new ComboBox<>();
    @FXML private Label firstNameLabel, lastNameLabel, courseLabel, yearLabel, blockLabel = new Label();
    final ObservableList feesListItems = FXCollections.observableArrayList();
    String firstName, lastName, course, block, choosenFee, tableName;
    int year, contributionId;
    static int studentId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getFeesList();
        feesList.setItems(feesListItems);
        reset();
    }

    public void reset(){
        firstNameLabel.setText("");
        lastNameLabel.setText("");
        courseLabel.setText("");
        yearLabel.setText("");
        blockLabel.setText("");
    }

    public void getFeesList(){
        String selectFees = "SELECT * FROM `fees` ";
        try{
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectFees);
            while (resultSet.next()){
                feesListItems.add(resultSet.getString("contributionName"));
            }
            database.connection.close();
        }catch (SQLException sql){
            System.out.println(sql.getErrorCode());
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void selectFees(){
        choosenFee = feesList.getValue();
        try {
            String selectFee = "SELECT * FROM fees WHERE `contributionName` = '"+choosenFee+"' ";
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectFee);
            while (resultSet.next()){
                contributionId = resultSet.getInt("feesId");
                System.out.println(contributionId);
            }
        }catch (SQLException sql){
            System.out.println(sql.getErrorCode());
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void pay(){
        selectFees();
        //**************************************************************May Error pa dini****************************************************************//

        String orgList = "INSERT INTO `"+generate.feesTableName(feesList.getValue())+"`(`date`, `time`, `payFor`, `student`, `organizationId`) VALUES " +
                "(NOW(), NOW(), "+contributionId+", "+studentId+", "+LoginController.courseNumber+") ";
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            int dataAdded = statement.executeUpdate(orgList);
            System.out.println("data Added"+dataAdded);
            database.connection.close();
            reset();
            feesList.setValue("");
        }catch (SQLException sql){
            System.out.println(sql.getErrorCode()+"\n"+sql);
            if (sql.getErrorCode() == 1146){
                createFeesListTable();
            }
            System.out.println(orgList);
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void createFeesListTable(){
        selectFees();
        String createThisTable = "CREATE TABLE `"+generate.feesTableName(feesList.getValue())+"` (feeId INT(11) NOT NULL AUTO_INCREMENT, date DATE NOT NULL, time TIME(2) NOT NULL, " +
                "payFor INT(11) NOT NULL, student INT(11) NOT NULL, organizationId INT(11) NOT NULL, PRIMARY KEY(feeId))";
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            int create = statement.executeUpdate(createThisTable);
            System.out.println(create+" created");
            pay();
        }catch (SQLException sql){
            System.out.println(sql);
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void searchStudent(){
        String find = "SELECT * FROM `students` JOIN `courses` ON `students`.`course` = `courses`.`courseId` WHERE `studentId` = '"+studentSearchField.getText()+"' AND `organization` = '"+ LoginController.userCourse+"' ";
        try{
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(find);
            if (resultSet.next()){
                studentId = resultSet.getInt("id");
                firstName = resultSet.getString("firstName");
                lastName = resultSet.getString("lastName");
                course = resultSet.getString("courseName");
                year = resultSet.getInt("year");
                block = resultSet.getString("block");

                //**********+++++++++**********//
                firstNameLabel.setText(firstName);
                lastNameLabel.setText(lastName);
                courseLabel.setText(course);
                yearLabel.setText(Integer.toString(year));
                blockLabel.setText(block);
                studentSearchField.clear();
            }else {
                reset();
                studentSearchField.clear();
            }
        }catch (SQLException sql){
            System.out.println(sql);
        }catch (Exception ex){

        }
    }
}
