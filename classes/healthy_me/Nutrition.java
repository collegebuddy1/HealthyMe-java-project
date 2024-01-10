package healthy_me;

/**
 * Created by jugal on 7/30/2016.
 */
public class Nutrition {
    private String first_name;
    private String last_name;
    private int _meal_id;
    private int _user_id;
    private String _food_name;
    private String _meal_type;
    private int _calories;
    private String _date_x;

    public String get_date_x() {
        return _date_x;
    }

    public void set_date_x(String _date_x) {
        this._date_x = _date_x;
    }

    public int get_meal_id() {
        return _meal_id;
    }

    public void set_meal_id(int _meal_id) {
        this._meal_id = _meal_id;
    }

    public int get_user_id() {
        return _user_id;
    }

    public void set_user_id(int _user_id) {
        this._user_id = _user_id;
    }

    public String get_food_name() {
        return _food_name;
    }

    public void set_food_name(String _food_name) {
        this._food_name = _food_name;
    }

    public String get_meal_type() {
        return _meal_type;
    }

    public void set_meal_type(String _meal_type) {
        this._meal_type = _meal_type;
    }

    public int get_calories() {
        return _calories;
    }

    public void set_calories(int _calories) {
        this._calories = _calories;
    }

    public Nutrition(int _user_id, String _food_name, String _meal_type, int _calories, String _date_x, String first_name, String last_name) {
        this._user_id = _user_id;
        this._food_name = _food_name;
        this._meal_type = _meal_type;
        this._calories = _calories;
        this._date_x = _date_x;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public String toString() {
        return String.format("%s for %s with %s calories on %s for user: %s %s", _food_name, _meal_type, _calories, _date_x, first_name, last_name);
    }

}
