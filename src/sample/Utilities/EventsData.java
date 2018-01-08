package sample.Utilities;

public class EventsData {
    int id;
    String name, venue, time, date, willingness;

    public EventsData(int id, String name, String venue, String time, String date, String willingness) {
        this.id = id;
        this.name = name;
        this.venue = venue;
        this.time = time;
        this.date = date;
        this.willingness = willingness;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWillingness() {
        return willingness;
    }

    public void setWillingness(String willingness) {
        this.willingness = willingness;
    }
}
