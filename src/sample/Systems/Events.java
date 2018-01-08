package sample.Systems;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Utilities.Database;
import sample.Utilities.EventsData;
import sample.Utilities.Generate;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class Events implements Initializable{
    final ToggleGroup group = new ToggleGroup();
    Database database = new Database();
    Generate generate = new Generate();

    @FXML private TableView<EventsData> eventsTable = new TableView<>();
    @FXML private TableColumn<EventsData, String> nameColumn = new TableColumn<>();
    @FXML private TableColumn<EventsData, String> venueColumn = new TableColumn<>();
    @FXML private TableColumn<EventsData, String> timeColumn = new TableColumn<>();
    @FXML private TableColumn<EventsData, String> dateColumn = new TableColumn<>();
    @FXML private TableColumn<EventsData, String> willingness = new TableColumn<>();

    @FXML private JFXTextField eventNameField = new JFXTextField();
    @FXML private JFXTextField eventVenueField = new JFXTextField();
    @FXML private JFXTimePicker timeField = new JFXTimePicker();
    @FXML private JFXDatePicker dateField = new JFXDatePicker();
    @FXML private RadioButton mandatory = new RadioButton();
    @FXML private RadioButton voluntary = new RadioButton();
    int dbEventId;
    String dbEventName, dbEventVenue, dbEventTime, dbEventDate, dbEventWillingness;
    static String isMandatory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        venueColumn.setCellValueFactory(new PropertyValueFactory<>("venue"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        willingness.setCellValueFactory(new PropertyValueFactory<>("willingness"));
        eventsTable.getColumns().clear();
        eventsTable.setItems(getEventsList());
        eventsTable.getColumns().addAll(nameColumn, venueColumn, timeColumn, dateColumn, willingness);
    }

    public void clearFields(){
        eventNameField.clear();
        eventVenueField.clear();
        timeField.setValue(null);
        dateField.setValue(null);
        voluntary.setSelected(false);
        mandatory.setSelected(false);
        isMandatory = null;
    }

    public void select(){
        EventsData eventsData = eventsTable.getSelectionModel().getSelectedItem();
        dbEventId = eventsData.getId();
        dbEventName = eventsData.getName();
        dbEventVenue = eventsData.getVenue();
        dbEventTime = eventsData.getTime();
        dbEventDate = eventsData.getDate();
        dbEventWillingness = eventsData.getWillingness();
        //****************************************************//
        eventNameField.setText(dbEventName);
        eventVenueField.setText(dbEventVenue);
        timeField.setValue(LocalTime.parse(dbEventTime));
        dateField.setValue(LocalDate.parse(dbEventDate));
        if (dbEventWillingness.equals("mandatory")){
            mandatory.setSelected(true);
            voluntary.setSelected(false);
        }else if(dbEventWillingness.equals("voluntary")){
            mandatory.setSelected(false);
            voluntary.setSelected(true);
        }
    }

    public void setEvent(){
        String eventName = eventNameField.getText();
        String eventVenue = eventVenueField.getText();
        String time = timeField.getValue().toString();
        String date = dateField.getValue().toString();
        if (eventName.isEmpty() || eventVenue.isEmpty() || time.isEmpty() || date.isEmpty() || isMandatory.isEmpty() ){
            System.out.println("please complete all fields.");
        }else {
            String set = "INSERT INTO events (`eventName`, `eventVenue`, `eventTime`, `eventDate`, `eventWillingness`) VALUES" +
                    "('"+eventName+"', '"+eventVenue+"', '"+time+"', '"+date+"', '"+isMandatory+"')";
            try{
                database.connect();
                Statement statement = database.connection.createStatement();
                int inserted = statement.executeUpdate(set);
                if (inserted > 0){
                    String createTable = "CREATE TABLE `"+generate.eventsTableName(eventName)+"` (" +
                            "eventId INT(11) NOT NULL AUTO_INCREMENT, student INT(11) NOT NULL, date DATE NOT NULL, time TIME NOT NULL, PRIMARY KEY (eventId))  ";
                    try{
                        Statement statement1 = database.connection.createStatement();
                        int result = statement1.executeUpdate(createTable);
                        if (result == 0){
                            System.out.println("Table created Successfully");
                        }
                    }catch (SQLException sql){
                        System.out.println(sql);
                    }
                    System.out.println("Events Added");
                }
                System.out.println(inserted+" event Inserted");
                eventsTable.setItems(getEventsList());
                database.connection.close();
                clearFields();
            }catch (SQLException sql){
                System.out.println(sql);
            }catch (Exception ex){
            }
        }
    }

    public void editEvents(){
        String editThisEvent = "UPDATE `events` SET `eventName` = '"+eventNameField.getText()+"', `eventVenue` = '"+eventVenueField.getText()+"', `eventTime` = '"+timeField.getValue()+"', " +
                "`eventDate` = '"+dateField.getValue()+"',  `eventWillingness` = '"+isMandatory+"' WHERE  `eventId` = "+dbEventId+" ";
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            int updated = statement.executeUpdate(editThisEvent);

            String updateEventTable = "RENAME TABLE `"+generate.eventsTableName(dbEventName)+"` TO `"+generate.eventsTableName(eventNameField.getText())+"` ";
            try {
                Statement statement1 = database.connection.createStatement();
                int result = statement1.executeUpdate(updateEventTable);
                if (result == 0){
                    System.out.println("table name updated");
                }
            }catch (SQLException sql){
                System.out.println(sql);
            }
            System.out.println(updated+" Updated Event/s");
            eventsTable.setItems(getEventsList());
            clearFields();
            database.connection.close();
        }catch (SQLException sql){
            System.out.println(sql);
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void deleteEvent(){
        String deleteThisEvent = "DELETE FROM events WHERE `eventId` = "+dbEventId+" ";
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            int deletedEvent = statement.executeUpdate(deleteThisEvent);
            if (deletedEvent == 0){
                System.out.println("Event Deleted");
            }
            try {
                Statement statement1 = database.connection.createStatement();
                int result = statement1.executeUpdate("DROP TABLE `"+generate.eventsTableName(dbEventName)+"` ");
                if (result == 0){
                    System.out.println("Event table has been deleted!");
                }
            }catch (SQLException sql){
                System.out.println(sql);
            }
            System.out.println(deletedEvent+" deleted Event");
            eventsTable.setItems(getEventsList());
            database.connection.close();
            clearFields();
        }catch (SQLException sql){
            System.out.println(sql);
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void mandatorySelected(){
        voluntary.setSelected(false);
        isMandatory = "mandatory";
    }

    public void voluntarySelected(){
        mandatory.setSelected(false);
        isMandatory = "voluntary";
    }

    public ObservableList<EventsData> getEventsList(){
        ObservableList<EventsData> eventsData = FXCollections.observableArrayList();
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM events");
            while (resultSet.next()){
                dbEventId = resultSet.getInt("eventId");
                dbEventName = resultSet.getString("eventName");
                dbEventVenue = resultSet.getString("eventVenue");
                dbEventTime = resultSet.getString("eventTime");
                dbEventDate = resultSet.getString("eventDate");
                dbEventWillingness = resultSet.getString("eventWillingness");
                eventsData.add(new EventsData(dbEventId, dbEventName, dbEventVenue, dbEventTime, dbEventDate, dbEventWillingness));
            }
            database.connection.close();
        }catch (SQLException sql){
            System.out.println(sql);
        }catch (Exception ex){
            System.out.println(ex);
        }
        return  eventsData;
    }
}
