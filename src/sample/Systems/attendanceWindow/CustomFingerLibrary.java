package sample.Systems.attendanceWindow;

import com.zkteco.biometric.FingerprintSensor;
import com.zkteco.biometric.FingerprintSensorEx;
import sample.Systems.SetAttendance;
import sample.Systems.attendanceWindow.Attendance;
import sample.Utilities.Database;
import sample.Utilities.Generate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomFingerLibrary {
private static int studentId;



    public int fingerMatch(byte[] template, int retLen){
        //System.out.println("Finger template: " + template);
        int result;
        byte[] fingerFromDb = new byte[2084];
        Database database = new Database();
        //System.out.println("fingerMatch");
        String match = "SELECT * FROM `students` ";
        int deviceCount = FingerprintSensorEx.Init();
        int score = 0;
        try {
            Attendance attendance = new Attendance();
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(match);
            while (resultSet.next()){
                String tempTemplate = resultSet.getString("rightThumb");
                //System.out.println("zzz : " + fingerFromDb);
                FingerprintSensor.Base64ToBlob(tempTemplate, fingerFromDb, retLen);
                score =  FingerprintSensorEx.DBMatch(deviceCount, fingerFromDb, template);
                //System.out.println("zzz : " + fingerFromDb);
                //fingerFromDb = null
                System.out.println("Score : "+score);
                if (score > 0){
                    studentId = resultSet.getInt("id");
                    System.out.println("Student ID II : " + studentId);
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    int tempCourse = resultSet.getInt("course");
                    String course = "";
                    if (tempCourse == 1){
                        course = "BSCS";
                    }else if (tempCourse == 2){
                        course = "BAT";
                    }else if (tempCourse == 3){
                        course = "BEED";
                    }else if (tempCourse == 4){
                        course = "BSED";
                    }
                    int year = resultSet.getInt("year");
                    String block = resultSet.getString("block");

                    Generate generate = new Generate();

                    System.out.println(firstName+" "+lastName+" "+course+" "+year+" "+block);
                    new TempAttendance(firstName, lastName, course, Integer.toString(year), block);

                    try {
                        System.out.println("Student ID III : " + studentId);
                        Statement statement1b = database.connection.createStatement();
                        ResultSet resultSet1 = statement1b.executeQuery("SELECT * FROM "+generate.eventsTableName(SetAttendance.tempEventNameX)+" WHERE `student` = "+studentId+" ");
                        if (resultSet1.next()){
                            System.out.println("Attendance allready Recorded!");
                            studentId = 0;
                        }else {
                            String present = "INSERT INTO "+generate.eventsTableName(SetAttendance.tempEventNameX)+" (student, date, time) " +
                                    "VALUES ('"+studentId+"', NOW(), NOW()) ";
                            try {
                                Statement statement1 = database.connection.createStatement();
                                result = statement1.executeUpdate(present);
                                studentId = 0;
                                if (result == 0){
                                    System.out.println("Attendance Inserted");
                                    studentId = 0;
                                }

                            }catch (SQLException sql){
                                System.out.println(sql);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                            studentId = 0;
                        }
                    }catch (SQLException sql){
                        sql.getMessage();
                    }catch (Exception sx){
                        sx.printStackTrace();
                    }

                    result = 0;
                    studentId = 0;
                    //attendance.lastNameLabel.setText(lastName);
                    //attendance.assignLabels();
                }
                byte [] templateX = new byte[2048];
                score = FingerprintSensorEx.DBMatch(deviceCount, template, templateX);
            }
            database.connection.close();
        }catch (SQLException sql){
            System.out.println(sql);
        }catch (Exception ex){
            System.out.println("xxxxxx = "+ex);
            ex.printStackTrace();
        }
        return score;
    }
}

