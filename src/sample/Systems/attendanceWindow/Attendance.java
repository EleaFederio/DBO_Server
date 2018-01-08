package sample.Systems.attendanceWindow;

import com.sun.tracing.dtrace.FunctionName;
import com.zkteco.biometric.FingerprintSensor;
import com.zkteco.biometric.FingerprintSensorErrorCode;
import com.zkteco.biometric.FingerprintSensorEx;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import sample.Systems.LoginController;
import sample.Systems.SetAttendance;
import sample.Utilities.AttendanceRecord;
import sample.Utilities.Database;
import sample.Utilities.Generate;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Attendance implements Initializable{
    SetAttendance setAttendance = new SetAttendance();
    Generate generate = new Generate();
    Database database = new Database();
    @FXML private Label eventNameLabel = new Label();
    @FXML protected TableView<AttendanceRecord> attendanceRecordTable = new TableView<>();
    @FXML private TableColumn<AttendanceRecord, String> firstNameColumn = new TableColumn<>();
    @FXML private TableColumn<AttendanceRecord, String> lastNameColumn = new TableColumn<>();
    @FXML private TableColumn<AttendanceRecord, String> courseColumn = new TableColumn<>();
    @FXML private TableColumn<AttendanceRecord, Integer> yearColumn = new TableColumn<>();
    @FXML private TableColumn<AttendanceRecord, String> blockColumn = new TableColumn<>();
    @FXML private TableColumn<AttendanceRecord, String> dateColumn = new TableColumn<>();
    @FXML private TableColumn<AttendanceRecord, String> timeColumn = new TableColumn<>();

    @FXML private Label firstNameLabel = new Label("");
    @FXML private Label lastNameLabel = new Label("");
    @FXML private Label courseLabel = new Label("");
    @FXML private Label yearLabel = new Label("");
    @FXML private Label blockLabel = new Label("");


    //********************* Biometric Scanner *********************//
    @FXML private Label infoLabel = new Label();
    int fpWidth = 0;
    int fpHeight = 0;
    protected byte[] lastRegTemp = new byte[2048];
    protected int cbRegTemp = 0;
    protected byte[][] regtemparray = new byte[3][2048];
    protected boolean bRegister = false;
    protected boolean bIdentify = true;
    protected int iFid = 1;
    protected int nFakeFunOn = 1;
    protected static final int enroll_cnt = 3;
    protected int enroll_idx = 0;
    protected byte[] imgbuf = null;
    private static int triger;

    private byte [] template = new byte[2048];
    protected int[] templateLen = new int[1];
    protected boolean mbStop = true;
    protected long mhDevice = 0;
    protected long mhDB = 0;
    Runnable aaa = new Runnable() {
        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    CustomFingerLibrary customFingerLibrary = new CustomFingerLibrary();
                    try{
                        FingerprintSensorEx zk4500 = new FingerprintSensorEx();
                        int ret = 0;
                        System.out.println(ret);
                        while (!mbStop) {
                            templateLen[0] = 2048;
                            System.out.println(template);
                            ret = zk4500.AcquireFingerprint(mhDevice, imgbuf, template, templateLen);
                            System.out.println(template);
                            customFingerLibrary.fingerMatch(template, 2048);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }catch (Exception ex){
                        System.out.println(ex);
                    }
                }
            });


        }
    };
    Thread biometricScanner  = new Thread(aaa);

    Runnable sss = new Runnable() {
        @Override
        public void run() {
            while (triger != 0){
                attendanceRecordTable.setItems(getAttendanceRecord());
            }
            try{
                Thread.sleep(1000);
            }catch (InterruptedException i){
                i.printStackTrace();
            }
        }
    };
    Thread sssx = new Thread(sss);




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        triger = 1;
        //firstNameLabel.setText("Hello World!");
        eventNameLabel.setText(SetAttendance.tempEventNameX);
        firstNameColumn.setCellValueFactory( new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        blockColumn.setCellValueFactory(new PropertyValueFactory<>("block"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        attendanceRecordTable.getColumns().clear();
        sssx.start();
        //attendanceRecordTable.setItems(getAttendanceRecord());
        attendanceRecordTable.getColumns().addAll(firstNameColumn, lastNameColumn, courseColumn, yearColumn, blockColumn, dateColumn, timeColumn);

    }

    public void viewTableEvent(){
        attendanceRecordTable.setItems(getAttendanceRecord());
    }

    public void goBack(){
        triger = 0;
        try {
            Thread.sleep(500);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        FreeSensor();
        System.out.println("Scanner Thread: " + sssx.isAlive());
        System.out.println("Table Thread: " + biometricScanner.isAlive());
        setAttendance.attendanceStage.hide();
        LoginController.homeWindow.show();
    }

    public ObservableList<AttendanceRecord> getAttendanceRecord(){
        ObservableList<AttendanceRecord> attendanceRecords = FXCollections.observableArrayList();
        String attendanceList = "SELECT `firstName`, `lastName`, `course`, `year`, `block`, `date`, `time` FROM `students` JOIN `"+generate.eventsTableName(SetAttendance.tempEventNameX)+"` " +
                " ON `students`.`id` = `"+generate.eventsTableName(SetAttendance.tempEventNameX)+"`.`student`";
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(attendanceList);
            while (resultSet.next()){
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String courses = "";
                int tempCourse = resultSet.getInt("course");
                if (tempCourse == 1){
                    courses = "BSCS";
                }else if (tempCourse == 2){
                    courses = "BAT";
                }else if (tempCourse == 3){
                    courses = "BEED";
                }else if (tempCourse == 4){
                    courses = "BSED";
                }
                String course = courses;
                int year = resultSet.getInt("year");
                String block = resultSet.getString("block");
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                attendanceRecords.add(new AttendanceRecord(firstName, lastName, course, year, block, date, time));
                //System.out.println("getAttendanceRecord() : "+firstName+" "+lastName+" "+course+" "+year+" "+block+" "+time+" "+date);
            }
            database.connection.close();
        }catch (SQLException sql){
            System.out.println(sql);
        }catch (Exception ex){
            System.out.println("SASAS"+ex);
        }
        return attendanceRecords;
    }


    //****************************************************************************************************//
    public void openDevice(){
        infoLabel.setText("Loading...");

        if (0 != mhDevice)
        {
            infoLabel.setText("Please close device first!");
            return;
        }
        int ret = FingerprintSensorErrorCode.ZKFP_ERR_OK;
        cbRegTemp = 0;
        bRegister = false;
        bIdentify = false;
        iFid = 1;
        enroll_idx = 0;
        if (FingerprintSensorErrorCode.ZKFP_ERR_OK != FingerprintSensorEx.Init())
        {
            infoLabel.setText("Init failed!");
            return;
        }
        ret = FingerprintSensorEx.GetDeviceCount();
        if (ret < 0)
        {
            infoLabel.setText("No devices connected!");
            FreeSensor();
            return;
        }
        if (0 == (mhDevice = FingerprintSensorEx.OpenDevice(0)))
        {
            infoLabel.setText("Open device fail, ret = " + ret + "!");
            FreeSensor();
            return;
        }
        if (0 == (mhDB = FingerprintSensorEx.DBInit()))
        {
            infoLabel.setText("Init DB fail, ret = " + ret + "!");
            FreeSensor();
            return;
        }
        int nFmt = 0;	//Ansi
        FingerprintSensorEx.DBSetParameter(mhDB,  5010, nFmt);
        FingerprintSensorEx.SetParameters(mhDevice, 2002, changeByte(nFakeFunOn), 4);
        byte[] paramValue = new byte[4];
        int[] size = new int[1];
        FingerprintSensorEx.SetParameters(mhDevice, 106, paramValue, 4);
        size[0] = 4;
        FingerprintSensorEx.GetParameters(mhDevice, 1, paramValue, size);
        fpWidth = byteArrayToInt(paramValue);
        size[0] = 4;
        FingerprintSensorEx.GetParameters(mhDevice, 2, paramValue, size);
        fpHeight = byteArrayToInt(paramValue);
        imgbuf = new byte[fpWidth*fpHeight];
        mbStop = false;

        biometricScanner.start();
        infoLabel.setText("Finger Print Scanner Ready!");
        System.out.println("Device On");
    }

    public void closeDevice(){
        FreeSensor();
        infoLabel.setText("Scanner Closed!");
        System.out.println("Device Off");
    }

    private void FreeSensor(){
        FingerprintSensorEx zk4500 = new FingerprintSensorEx();
        mbStop = true;
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        if (0 != mhDB){
            zk4500.DBFree(mhDB);
            mhDB = 0;
        }
        if (0 != mhDevice){
            zk4500.CloseDevice(mhDevice);
            mhDevice = 0;
        }
        zk4500.Terminate();
    }

    public static byte[] changeByte(int data) {
        return intToByteArray(data);
    }

    public static byte[] intToByteArray (final int number) {
        byte[] abyte = new byte[4];
        abyte[0] = (byte) (0xff & number);
        abyte[1] = (byte) ((0xff00 & number) >> 8);
        abyte[2] = (byte) ((0xff0000 & number) >> 16);
        abyte[3] = (byte) ((0xff000000 & number) >> 24);
        return abyte;
    }

    public static int byteArrayToInt(byte[] bytes) {
        int number = bytes[0] & 0xFF;
        number |= ((bytes[1] << 8) & 0xFF00);
        number |= ((bytes[2] << 16) & 0xFF0000);
        number |= ((bytes[3] << 24) & 0xFF000000);
        return number;
    }


}
