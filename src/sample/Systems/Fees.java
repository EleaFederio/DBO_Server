package sample.Systems;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.layout.BorderPane;

import java.lang.reflect.Executable;
import java.net.URL;
import java.util.ResourceBundle;

public class Fees implements Initializable{
    //@FXML private TableView<FeesData> feesTable = new TableView();
    @FXML private JFXButton createFeesButton = new JFXButton();
    @FXML private JFXButton collectFeesButton = new JFXButton();
    @FXML private BorderPane feesWindow = new BorderPane();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createFeesSelected();
    }

    public void createFeesSelected(){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("feesWindow/createFeesSubScene.fxml"));
            feesWindow.setCenter(new SubScene(root, 800, 680));
        }catch (Exception ex){
            System.out.println(ex);
        }
        createFeesButton.setStyle("-fx-background-color:  #d8dfea");
        collectFeesButton.setStyle("-fx-background-color: #ffffff");
    }

    public void collectFeesSelected(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("feesWindow/collectFeesScene.fxml"));
            feesWindow.setCenter(new SubScene(root, 800, 680));
        }catch (Exception ex){
            System.out.println(ex);
        }
        collectFeesButton.setStyle("-fx-background-color:  #d8dfea");
        createFeesButton.setStyle("-fx-background-color: #ffffff");
    }
}
