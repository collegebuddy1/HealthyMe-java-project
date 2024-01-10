package healthy_me;

/**
 * Created by jugal on 7/30/2016.
 */
public class Activity {
    private String first_name;
    private String last_name;
    private int _activity_id;
    private int _user_id;
    private String _name;
    private int _calories_burned;
    private String _date_x;
    private String _start_time;
    private String _end_time;

    public Activity(int _user_id, String _name, int _calories_burned, String _date, String _start_time, String _end_time, String first_name, String last_name) {
        this._user_id = _user_id;
        this._name = _name;
        this._calories_burned = _calories_burned;
        this._date_x = _date;
        this._start_time = _start_time;
        this._end_time = _end_time;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public int get_activity_id() {
        return _activity_id;
    }

    public void set_activity_id(int _activity_id) {
        this._activity_id = _activity_id;
    }

    public int get_user_id() {
        return _user_id;
    }

    public void set_user_id(int _user_id) {
        this._user_id = _user_id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_calories_burned() {
        return _calories_burned;
    }

    public void set_calories_burned(int _calories_burned) {
        this._calories_burned = _calories_burned;
    }

    public String get_date_x() {
        return _date_x;
    }

    public void set_date(String _date_x) {
        this._date_x = _date_x;
    }

    public String get_start_time() {
        return _start_time;
    }

    public void set_start_time(String _start_time) {
        this._start_time = _start_time;
    }

    public String get_end_time() {
        return _end_time;
    }

    public void set_end_time(String _end_time) {
        this._end_time = _end_time;
    }

    public String toString() {
        return String.format("%s burned %s calories on %s from %s to %s for user: %s %s", _name, _calories_burned, _date_x, _start_time, _end_time, first_name, last_name);
    }

    public String toHTML() {
        return "<tr><td>" + _activity_id + "</td><td>" + _user_id + "</td><td>" + _calories_burned + "</td><td>" + _date_x + "</td><td>" + _start_time + "</td><td>" + _end_time + "</td></tr>";
    }
}
