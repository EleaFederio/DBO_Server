package sample.Systems.attendanceWindow;

public class TempAttendance {
    private String fisrtName;
    private String lastName;
    private String course;
    private String year;
    private String block;

    public TempAttendance(String fisrtName, String lastName, String course, String year, String block) {
        this.fisrtName = fisrtName;
        this.lastName = lastName;
        this.course = course;
        this.year = year;
        this.block = block;
    }

    public String getFisrtName() {
        return fisrtName;
    }

    public void setFisrtName(String fisrtName) {
        this.fisrtName = fisrtName;
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

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
}
