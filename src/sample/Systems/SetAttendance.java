package sample.Systems;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Utilities.Database;
import sample.Utilities.EventsData;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SetAttendance implements Initializable {
    Database database = new Database();
    @FXML private ComboBox<EventsData> attendanceList = new ComboBox<>();
    final ObservableList eventsListItem = FXCollections.observableArrayList();
    public static Stage attendanceStage = new Stage();
    MainSystem mainSystem = new MainSystem();
    public static String tempEventNameX;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getEventsList();
        attendanceList.setItems(eventsListItem);
    }

    public void getEventsList(){
        String selectEvents = "SELECT * FROM `events` ";
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectEvents);
            while (resultSet.next()){
                eventsListItem.add(resultSet.getString("eventName"));
            }
        }catch (SQLException sql){
            System.out.println(sql);
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void launch(){
        tempEventNameX = String.valueOf(attendanceList.getSelectionModel().getSelectedItem());
        System.out.println(tempEventNameX);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("attendanceWindow/attendance.fxml"));
            //mainSystem.mainPane.setCenter(new SubScene(root, 800, 800));
            Scene scene = new Scene(root);
            LoginController.homeWindow.hide();
            attendanceStage.setScene(scene);
            //attendanceStage.initStyle(StageStyle.TRANSPARENT);
            attendanceStage.show();
        }catch (Exception ex){
            System.out.println(ex);
        }
    }
}
