package healthy_me;

/**
 * Created by jugal on 7/30/2016.
 */
public class BodyStat {
    public int get_stat_id() {
        return _stat_id;
    }

    public void set_stat_id(int _stat_id) {
        this._stat_id = _stat_id;
    }

    public int get_user_id() {
        return _user_id;
    }

    public void set_user_id(int _user_id) {
        this._user_id = _user_id;
    }

    public float get_height() {
        return _height;
    }

    public void set_height(float _height) {
        this._height = _height;
    }

    public float get_weight() {
        return _weight;
    }

    public void set_weight(float _weight) {
        this._weight = _weight;
    }

    public String get_date_x() {
        return _date_x;
    }

    public void set_date_x(String _date_x) {
        this._date_x = _date_x;
    }

    private String first_name;
    private String last_name;
    private int _stat_id;
    private int _user_id;
    private float _height;
    private float _weight;
    private String _date_x;

    public BodyStat(int _user_id, float _height, float _weight, String _date_x, String first_name, String last_name) {
        this._user_id = _user_id;
        this._height = _height;
        this._weight = _weight;
        this._date_x = _date_x;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public String toString() {
        return String.format("body statistic: height=%s and weight=%s on %s for user %s %s", _height, _weight, _date_x, first_name, last_name);
    }
}
