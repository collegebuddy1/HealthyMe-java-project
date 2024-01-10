package healthy_me;

/**
 *
 * An implementation of the Student class.
 *
 * @author Julia Stoyanovich (stoyanovich@drexel.edu)
 *
 */
public class User {

    public int get_user_id() {
        return _user_id;
    }

    public void set_user_id(int _user_id) {
        this._user_id = _user_id;
    }

    private int _user_id;
    private String _first_name;
    private String _last_name;
    private int _age;

    public User(String _first_name, String _last_name, int _age) {
        this._first_name = _first_name;
        this._last_name = _last_name;
        this._age = _age;
    }

    public String get_first_name() {
        return _first_name;
    }

    public void set_first_name(String _first_name) {
        this._first_name = _first_name;
    }

    public String get_last_name() {
        return _last_name;
    }

    public void set_last_name(String _last_name) {
        this._last_name = _last_name;
    }

    public int get_age() {
        return _age;
    }

    public void set_age(int _age) {
        this._age = _age;
    }

    public String toString() {
        return String.format("%s %s age %s with user_id %s", _first_name, _last_name, _age, _user_id);
    }

    public String toHTML() {
        return "<tr><td>" + _user_id + "</td><td>" + _first_name + "</td><td>" + _last_name + "</td><td>" + _age + "</td></tr>";
    }
}