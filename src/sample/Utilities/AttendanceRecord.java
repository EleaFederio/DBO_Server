package sample.Utilities;

public class AttendanceRecord {
    private String firstName;
    private String lastName;
    private String course;
    private int year;
    private String block;
    private String date;
    private String time;

    public AttendanceRecord(String firstName, String lastName, String course, int year, String block, String date, String time) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.course = course;
        this.year = year;
        this.block = block;
        this.date = date;
        this.time = time;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String  course) {
        this.course = course;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String timeIn) {
        this.date = timeIn;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String timeOut) {
        this.time
                = timeOut;
    }
}
