package sample.Systems.feesWindow;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Systems.LoginController;
import sample.Utilities.Database;
import sample.Utilities.FeesData;
import sample.Utilities.Generate;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CreateFeesSubScene implements Initializable{
    Database database = new Database();
    Generate generate = new Generate();
    @FXML private TableView<FeesData> feesTable = new TableView<>();
    @FXML private TableColumn<FeesData, String> feesNameColumn = new TableColumn<>();
    @FXML private TableColumn<FeesData, Boolean> feesAmountColumn = new TableColumn<>();
    @FXML private JFXTextField contributionNameField = new JFXTextField();
    @FXML private JFXTextField contributionAmountField = new JFXTextField();
    static int feesId;
    String feesName;
    double feesAmount;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        feesNameColumn.setCellValueFactory(new PropertyValueFactory<>("feesName"));
        feesAmountColumn.setCellValueFactory(new PropertyValueFactory<>("feesAmount"));
        feesTable.getColumns().clear();
        feesTable.setItems(getFeesData());
        System.out.println("ok");
        feesTable.getColumns().addAll(feesNameColumn, feesAmountColumn);
    }

    public void clearFields(){
        contributionNameField.clear();
        contributionAmountField.clear();
    }

    public void add(){
        //************************ Check if fees already exist ***********************************//
        String check = "SELECT * FROM fees WHERE `contributionName` = '"+contributionNameField.getText()+"' ";
        try{
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet0 = statement.executeQuery(check);
            if (resultSet0.next()){
                System.out.println("Already Exist");
                clearFields();
            }else {
                //******************************************//
                //******************************************//
                String addQuery = "INSERT INTO fees (`contributionName`, `amount`, `organization`) VALUES " +
                        "('"+contributionNameField.getText()+"', "+contributionAmountField.getText()+", '"+ LoginController.userCourse+"')";
                try{
                    Statement statement2 = database.connection.createStatement();
                    int added = statement2.executeUpdate(addQuery);
                    if (added == 0){
                        System.out.println("Data added");
                    }
                }catch (SQLException sql){
                    System.out.println(sql);
                }catch (Exception ex){
                    System.out.println(ex);
                }

                //********** CREATE TABLE **********//
                String createThisTable = "CREATE TABLE `"+generate.feesTableName(contributionNameField.getText())+"` (feeId INT(11) NOT NULL AUTO_INCREMENT, date DATE NOT NULL, time TIME(2) NOT NULL, " +
                        "payFor INT(11) NOT NULL, student INT(11) NOT NULL, organizationId INT(11) NOT NULL, PRIMARY KEY(feeId))";
                try {
                    database.connect();
                    Statement statement3 = database.connection.createStatement();
                    int create = statement3.executeUpdate(createThisTable);
                    System.out.println(create+" created");
                    database.connection.close();
                }catch (SQLException sql){
                    System.out.println(sql);
                }catch (Exception ex){
                    System.out.println(ex);
                }
                //******************************************//
                //******************************************//
                System.out.println("Hello Wor");
            }
        }catch (SQLException sql){
            System.out.println(sql);
        }catch (Exception ex){
            System.out.println(ex);
        }
        feesTable.setItems(getFeesData());
        clearFields();
    }

    public void tableClick(){
        FeesData feesData = feesTable.getSelectionModel().getSelectedItem();
        feesId = feesData.getFeesId();
        feesName = feesData.getFeesName();
        feesAmount = feesData.getFeesAmount();
        System.out.println(feesId);
        contributionNameField.setText(feesName);
        contributionAmountField.setText(Double.toString(feesAmount));
    }

    public void edit(){
        String updateFee = "UPDATE fees SET `contributionName` = '"+contributionNameField.getText()+"', `amount` = "+contributionAmountField.getText()+" WHERE  feesId = "+feesId+" ";
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            int updated = statement.executeUpdate(updateFee);
            System.out.println(updated+" data updated");
            //***************************************************************//
            String renameThisTable = "RENAME TABLE `"+generate.feesTableName(feesName)+"` TO `"+generate.feesTableName(contributionNameField.getText())+"` ";
            try{
                Statement statement1 = database.connection.createStatement();
                int renamedTable = statement1.executeUpdate(renameThisTable);
                if (renamedTable == 0){
                    System.out.println("table renamed successfully");
                }
            }catch (SQLException sql3){
                System.out.println(sql3);
            }catch (Exception ex){
                System.out.println(ex);
            }
            //***************************************************************//
            database.connection.close();
            feesTable.setItems(getFeesData());
            clearFields();
        }catch (SQLException sql){
            System.out.println(sql.getErrorCode());
        }catch (Exception ex){
            System.out.println(ex);
        }
        System.out.println(updateFee);
    }

    public void delete(){
        String deleteFee = "DELETE FROM `fees` WHERE feesId = "+feesId+" ";
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            int deleted = statement.executeUpdate(deleteFee);
            System.out.println(deleted+" fees deleted");

            String dropThisTable = "DROP TABLE `"+generate.feesTableName(feesName)+"` ";
            try {
                Statement statement1 = database.connection.createStatement();
                int droppedTable = statement1.executeUpdate(dropThisTable);
                if (droppedTable == 0){
                    System.out.println("table successfully drop");
                }
            }catch (SQLException sql){
                System.out.println(sql);
            }catch (Exception ex){
                System.out.println(ex);
            }

            database.connection.close();
            feesTable.setItems(getFeesData());
            clearFields();
        }catch (SQLException sql){
            System.out.println(sql.getErrorCode());
        }
    }

    public ObservableList<FeesData> getFeesData(){
        ObservableList<FeesData> feesData = FXCollections.observableArrayList();
        try {
            database.connect();
            String selectFees = "SELECT * FROM `fees`";
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectFees);
            while (resultSet.next()){
                feesId = resultSet.getInt("feesId");
                feesName = resultSet.getString("contributionName");
                feesAmount = resultSet.getDouble("amount");
                feesData.add(new FeesData(feesId, feesName, feesAmount));
            }
            database.connection.close();
        }catch (SQLException sql){
            System.out.println("getFeesData() :"+sql.getErrorCode());
        }

        return feesData;
    }

}
