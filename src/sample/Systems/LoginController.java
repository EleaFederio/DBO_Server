package sample.Systems;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Main;
import sample.Utilities.Database;
import sample.Utilities.Security;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    Main main = new Main();
    Database database = new Database();
    public static Stage homeWindow = new Stage();
    Security security = new Security();
    Screen screen = Screen.getPrimary();
    javafx.geometry.Rectangle2D bound = screen.getVisualBounds();
    @FXML private JFXTextField usernameField = new JFXTextField();
    @FXML private JFXPasswordField passwordField = new JFXPasswordField();
    public static int userPosition;
    public static String softwareUser, userCourse;
    public static int courseNumber;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void login(){
        try {
            database.connect();
            String loginQuery = "SELECT * FROM `students` JOIN `courses` ON `students`.`course` = `courses`.`courseId` WHERE `appUserName` = '"+usernameField.getText()+"' AND `appPassword` = '"+security.encrypt(passwordField.getText())+"' ";
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(loginQuery);
            if (resultSet.next()){
                courseNumber = resultSet.getInt("course");
                userCourse = resultSet.getString("organization");
                softwareUser = resultSet.getString("firstName")+" "+resultSet.getString("lastName");
                userPosition = resultSet.getInt("position");

                System.out.println("User: "+softwareUser);
                System.out.println("Position: "+userPosition);
                System.out.println("user course: "+userCourse);

                if(userPosition >= 2 && userPosition <= 7){
                    System.out.println("Authorized");
                }else{
                    System.out.println("Unauthorized");
                }

                Parent root = FXMLLoader.load(getClass().getResource("MainSystem.fxml"));
                Scene scene = new Scene(root);
                homeWindow.setScene(scene);

                //homeWindow.initStyle(StageStyle.UNDECORATED);

                /*homeWindow.setX(bound.getMinX());
                homeWindow.setY(bound.getMinY());
                homeWindow.setWidth(bound.getWidth());
                homeWindow.setHeight(bound.getHeight());*/
                main.window.close();
                homeWindow.show();
            }else {
                System.out.println("Login Error!");
                System.out.println(loginQuery);
            }
            database.connection.close();
        }catch (SQLException sql){
            System.out.println("login() Error: "+sql);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception ex){
            System.out.println("ERROR! "+ex);
        }
        usernameField.clear();
        passwordField.clear();
    }

    public void close(){
        main.window.close();
    }

}
