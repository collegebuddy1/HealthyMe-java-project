package healthy_me;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.servlet.http.*;
/**
 *
 * An implementation of the Registrar.
 *
 * @author Julia Stoyanovich (stoyanovich@drexel.edu)
 *
 */
public class HealthyMe {

    private static Connection _conn = null;
    private static ResourceBundle _bundle;

    /**
     *
     * @param bundle - resource bundle that contains database connection information
     * @return
     */
    public String openDBConnection(String bundle) {
        _bundle = ResourceBundle.getBundle(bundle);
        return openDBConnection(
                _bundle.getString("dbUser"),
                _bundle.getString("dbPass"),
                _bundle.getString("dbSID"),
                _bundle.getString("dbHost"),
                Integer.parseInt(_bundle.getString("dbPort"))
        );
    }

    /**
     * Open the database connection.
     * @param dbUser
     * @param dbPass
     * @param dbSID
     * @param dbHost
     * @return
     */
    public String openDBConnection(String dbUser, String dbPass, String dbSID, String dbHost, int port) {

        String res="";
        if (_conn != null) {
            closeDBConnection();
        }

        try {
            _conn = DBUtils.openDBConnection(dbUser, dbPass, dbSID, dbHost, port);
            res = DBUtils.testConnection(_conn);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace(System.err);
        }
        return res;
    }

    /**
     * Close the database connection.
     */
    public void closeDBConnection() {
        try {
            DBUtils.closeDBConnection(_conn);
            System.out.println("Closed a connection");
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
    }

    /**
     * Register a new student in the database.
     * @param req
     * @return
     */
    public void executeForm(HttpServletRequest req, PrintWriter out) {
        try {
            String form = req.getParameter("form").trim();
            String action = req.getParameter("action").trim();

            ConditionParameters cp = new ConditionParameters();
            cp.set_first_name(req.getParameter("first_name").trim());
            cp.set_last_name(req.getParameter("last_name").trim());
            if (req.getParameter("date_x")!=null && isValidDate(req.getParameter("date_x").trim())) {
                cp.set_date_x(req.getParameter("date_x").trim());
            }
            if (req.getParameter("date_y")!=null && isValidDate(req.getParameter("date_y").trim())) {
                cp.set_date_y(req.getParameter("date_y").trim());
            }
            int user_id = DBUtils.getIntFromDB(_conn,
                    String.format(
                            "select user_id " +
                                    "from users u " +
                                    "where u.first_name = '%s' " +
                                    "and u.last_name = '%s';",
                            cp.get_first_name(),
                            cp.get_last_name()
                    )
            );
            if (user_id<0 && !form.equals("user")) {
                out.println("that user does not exist.");
            }
            else {
                if (action.equals("Add")) {
                    if (form.equals("user")) {
                        User user = new User(
                                cp.get_first_name(),
                                cp.get_last_name(),
                                Integer.parseInt(req.getParameter("age"))
                        );
                        registerUser(out, user);
                    }
                    else if (form.equals("nutrition")) {
                        Nutrition nutrition = new Nutrition(
                                user_id,
                                req.getParameter("food_name").trim(),
                                req.getParameter("meal_type").trim(),
                                Integer.parseInt(req.getParameter("calories")),
                                cp.get_date_x(),
                                cp.get_first_name(),
                                cp.get_last_name()
                        );
                        registerNutrition(out, nutrition);
                    }
                    else if (form.equals("body_stats")) {
                        BodyStat bodyStat = new BodyStat(
                                user_id,
                                Float.parseFloat(req.getParameter("height").trim()),
                                Float.parseFloat(req.getParameter("weight").trim()),
                                cp.get_date_x(),
                                cp.get_first_name(),
                                cp.get_last_name()
                        );
                        registerBodyStat(out, bodyStat);
                    }
                    else if (form.equals("activities")) {
                        if (isValidTime(req.getParameter("start_time").trim()) && isValidTime(req.getParameter("end_time").trim())) {
                            Activity activity = new Activity(
                                    user_id,
                                    req.getParameter("name").trim(),
                                    Integer.parseInt(req.getParameter("calories_burned").trim()),
                                    req.getParameter("date_x").trim(),
                                    req.getParameter("start_time").trim(),
                                    req.getParameter("end_time").trim(),
                                    cp.get_first_name(),
                                    cp.get_last_name()
                            );
                            registerActivity(out, activity);
                        }
                    }
                }
                else if (action.equals("Retrieve")) {
                    switch (form) {
                        case "bmi":
                            printBMI(out, cp);
                            break;
                        case "weight_difference":
                            printWeightChange(out, cp);
                            break;
                        case "calories_breakdown":
                            printCaloriesBreakdown(out, cp);
                            break;
                        case "calories_consumed":
                            printCaloriesConsumed(out, cp);
                            break;
                        case "calories_burned":
                            printCaloriesBurned(out, cp);
                            break;
                        case "average_steps":
                            printAvgSteps(out, cp);
                            break;
                        case "average_calories_burned":
                            printAvgCaloriesBurned(out, cp);
                            break;
                        case "average_calories_consumed":
                            printAvgCaloriesConsumed(out, cp);
                            break;
                        case "max_heart_rate":
                            printMaxHeartRate(out, cp);
                            break;
                        case "average_sleep_heart_rate":
                            printAvgSleepHeartRate(out, cp);
                            break;
                        case "average_resting_heart_rate":
                            printAvgRestingHeartRate(out, cp);
                            break;
                        default:
                            break;
                    }
                }
            }

        } catch (ParseException pe) {
            pe.printStackTrace();
            out.println("Your date(s) are in the wrong format.  It should be in yyyy-MM-dd");
            if (pe.getMessage().equals("TimeParseException")) {
                out.println("One of your time inputs is incorrect.  it should follow the format HH:mm such as 14:52");
            }
            else if (pe.getMessage().equals("DateParseException")) {
                out.println("One of your date inputs is incorrect.  it should follow the format yyyy-MM-dd such as 2016-12-31");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("You may have entered something incorrectly. Be sure to follow the examples provided.");
        }
    }

    public User registerUser(PrintWriter out, User newUser) {
        try {
            int user_id = 1 + DBUtils.getIntFromDB(_conn, "select max(user_id) from Users");
            newUser.set_user_id(user_id);
            String query = String.format("insert into Users values (%s, '%s', '%s', %s);",
                    newUser.get_user_id(),
                    newUser.get_first_name(),
                    newUser.get_last_name(),
                    newUser.get_age()
            );
            DBUtils.executeUpdate(_conn, query);
            out.println(String.format("added %s", newUser.toString()));
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            out.println("User already exists or invalid input.");
        }
        return newUser;
    }

    public Nutrition registerNutrition(PrintWriter out, Nutrition newNutrition) {
        try {
            int meal_id = 1 + DBUtils.getIntFromDB(_conn, "select max(meal_id) from need_Nutrition");
            newNutrition.set_meal_id(meal_id);
            String query = String.format("insert into need_Nutrition values (%s, %s, '%s', '%s', %s, '%s');",
                    newNutrition.get_meal_id(),
                    newNutrition.get_user_id(),
                    newNutrition.get_food_name(),
                    newNutrition.get_meal_type(),
                    newNutrition.get_calories(),
                    newNutrition.get_date_x()
            );
            DBUtils.executeUpdate(_conn, query);
            out.println(String.format("added %s", newNutrition.toString()));
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
        return newNutrition;
    }

    public BodyStat registerBodyStat(PrintWriter out, BodyStat newBodystat) {
        try {
            int stat_id = 1 + DBUtils.getIntFromDB(_conn, "select max(stat_id) from have_BodyStats");
            newBodystat.set_stat_id(stat_id);
            String query = String.format("insert into have_BodyStats values (%s, %s, %s, %s, '%s');",
                    newBodystat.get_stat_id(),
                    newBodystat.get_user_id(),
                    newBodystat.get_height(),
                    newBodystat.get_weight(),
                    newBodystat.get_date_x()
            );
            DBUtils.executeUpdate(_conn, query);
            out.println(String.format("added %s", newBodystat.toString()));
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
        return newBodystat;
    }

    public Activity registerActivity(PrintWriter out, Activity newActivity) {
        try {
            int activity_id = 1 + DBUtils.getIntFromDB(_conn, "select max(activity_id) from perform_Activities");
            newActivity.set_activity_id(activity_id);
            String query = String.format("insert into perform_Activities values (%s, %s, '%s', %s, '%s', '%s', '%s');",
                    newActivity.get_activity_id(),
                    newActivity.get_user_id(),
                    newActivity.get_name(),
                    newActivity.get_calories_burned(),
                    newActivity.get_date_x(),
                    newActivity.get_start_time(),
                    newActivity.get_end_time()
            );
            DBUtils.executeUpdate(_conn, query);
            out.println(String.format("added %s", newActivity.toString()));
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
        return newActivity;
    }

    public void printUsers(PrintWriter out) throws SQLException {
        out.println("<h2>User roster</h2>");
        out.println("<table>");
        out.println(toHTML("USER_ID", "FIRST_NAME", "LAST_NAME", "AGE (yrs)"));

        String query = "select * from Users";

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {

            int user_id = rs.getInt("user_id");
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            int age = rs.getInt("age");
            User user = new User(first_name, last_name, age);
            user.set_user_id(user_id);

            out.println(user.toHTML());
        }

        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printBMI(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>BMI</h2>");
        out.println("<table>");
        out.println(toHTML("FIRST_NAME", "LAST_NAME", "DATE", "BMI"));

        String optional_condition = "";
        if (cp.get_date_x()!=null && !cp.get_date_x().isEmpty()) {
            optional_condition = String.format("and b.date_x = '%s'", cp.get_date_x());
        }

        String query = String.format(
                "select u.first_name, u.last_name, b.date_x, round(b.weight/(b.height*b.height), 3) as BMI \n" +
                        "from users u, have_bodystats b \n" +
                        "where u.user_id = b.user_id \n" +
                        "and u.first_name='%s' \n" +
                        "and u.last_name='%s'\n" +
                        "%s;",
                cp.get_first_name(),
                cp.get_last_name(),
                optional_condition
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            String date_x = rs.getString("date_x");
            float bmi = rs.getFloat("bmi");
            out.println(toHTML(first_name, last_name, date_x, bmi));
        }

        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printWeightChange(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Weight Change</h2>");
        out.println("<table>");
        out.println(toHTML("DIFFERENCE_IN_WEIGHT (kg)"));
        String query = String.format(
                "select b.weight - (select s.weight \n" +
                        "                  from have_bodystats s, users u \n" +
                        "                  where u.user_id = s.user_id \n" +
                        "                  and s.date_x = '%s' \n" +
                        "                  and u.first_name = '%s' \n" +
                        "                  and u.last_name = '%s') as difference_in_weight \n" +
                        "from have_bodystats b, users u \n" +
                        "where u.user_id = b.user_id \n" +
                        "and b.date_x = '%s' \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s';",
                cp.get_date_y(),
                cp.get_first_name(),
                cp.get_last_name(),
                cp.get_date_x(),
                cp.get_first_name(),
                cp.get_last_name()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String difference_in_weight = rs.getString("difference_in_weight");
            out.println(toHTML(difference_in_weight));
        }

        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printCaloriesBreakdown(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Calorie Breakdown (kcal)</h2>");
        out.println("<table>");
        out.println(toHTML("FOOD_NAME", "MEAL_TYPE", "CALORIES"));
        String query = String.format(
                "select n.food_name, n.meal_type, n.calories \n" +
                        "from users u, need_nutrition n \n" +
                        "where u.user_id = n.user_id \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "and date_x = '%s';",
                cp.get_first_name(),
                cp.get_last_name(),
                cp.get_date_x()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String food_name = rs.getString("food_name");
            String meal_type = rs.getString("meal_type");
            int calories = rs.getInt("calories");
            out.println(toHTML(food_name, meal_type, calories));
        }

        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printCaloriesConsumed(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Calories Consumed</h2>");
        out.println("<table>");
        String optional_condition = "";
        if (cp.get_date_x()!=null && !cp.get_date_x().isEmpty()) {
            optional_condition = String.format("and n.date_x = '%s'", cp.get_date_x());
        }

        out.println(toHTML("FIRST_NAME", "LAST_NAME", "DATE_X", "TOTAL_CALORIES_CONSUMED (kcal)"));
        String query = String.format(
                "select u.first_name, u.last_name, n.date_x, sum(n.calories) as total_calories_consumed \n" +
                        "from users u, need_nutrition n \n" +
                        "where u.user_id = n.user_id \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "%s \n" +
                        "group by u.first_name, u.last_name, n.date_x;",
                cp.get_first_name(),
                cp.get_last_name(),
                optional_condition
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String first_name = rs.getString("first_name");
            String last_name= rs.getString("last_name");
            String date_x= rs.getString("date_x");
            int total_calories_consumed = rs.getInt("total_calories_consumed");
            out.println(toHTML(first_name, last_name, date_x, total_calories_consumed));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printCaloriesBurned(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Calories Burned</h2>");
        out.println("<table>");

        String optional_condition = "";
        if (cp.get_date_x()!=null && !cp.get_date_x().isEmpty()) {
            optional_condition = String.format("and a.date_x = '%s'", cp.get_date_x());
        }

        out.println(toHTML("FIRST_NAME", "LAST_NAME", "DATE_X", "TOTAL_CALORIES_BURNED (kcal)"));
        String query = String.format(
                "select u.first_name, u.last_name, s.date_x, sum(a.calories_burned)+sum(s.calories_burned) as total_calories_burned \n" +
                        "from users u, perform_activities a, walk_steps s \n" +
                        "where u.user_id = a.activity_id\n" +
                        "and u.user_id = s.step_id\n" +
                        "and a.date_x = s.date_x \n" +
                        "%s \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "group by u.first_name, u.last_name, s.date_x;",
                optional_condition,
                cp.get_first_name(),
                cp.get_last_name()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String first_name = rs.getString("first_name");
            String last_name= rs.getString("last_name");
            String date_x= rs.getString("date_x");
            int total_calories_consumed = rs.getInt("total_calories_burned");
            out.println(toHTML(first_name, last_name, date_x, total_calories_consumed));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printAvgSteps(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Average Steps</h2>");
        out.println("<table>");

        out.println(toHTML("AVG_STEPS"));
        String query = String.format(
                "select round(avg(s.num_steps), 2) as avg_steps \n" +
                        "from users u, walk_steps s \n" +
                        "where u.user_id = s.user_id \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "and s.date_x >= '%s' \n" +
                        "and s.date_x <= '%s'; ",
                cp.get_first_name(),
                cp.get_last_name(),
                cp.get_date_x(),
                cp.get_date_y()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int avg_steps = rs.getInt("avg_steps");
            out.println(toHTML(avg_steps));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printAvgCaloriesBurned(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Average Calories Burned</h2>");
        out.println("<table>");

        out.println(toHTML("DATE_X", "AVG_CALORIES_BURNED (kcal)"));
        String query = String.format(
                "select a.date_x, round(avg(a.calories_burned), 2) as avg_calories_burned \n" +
                        "from users u, perform_activities a \n" +
                        "where u.user_id = a.user_id \n" +
                        "and a.date_x >= '%s' \n" +
                        "and a.date_x <= '%s' \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "group by a.date_x\n" +
                        "order by a.date_x;",
                cp.get_date_x(),
                cp.get_date_y(),
                cp.get_first_name(),
                cp.get_last_name()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String date_x = rs.getString("date_x");
            int avg_calories_burned = rs.getInt("avg_calories_burned");
            out.println(toHTML(date_x, avg_calories_burned));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printAvgCaloriesConsumed(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Average Calories Per Meal</h2>");
        out.println("<table>");

        out.println(toHTML("DATE_X", "AVG_CALORIES_PER_MEAL (kcal)", "NUM_MEALS"));
        String query = String.format(
                "select n.date_x, round(avg(n.calories), 2) as avg_calories_per_meal, count(n.user_id) as num_meals \n" +
                        "from users u, need_nutrition n \n" +
                        "where u.user_id = n.user_id \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "and n.date_x >= '%s' \n" +
                        "and n.date_x <= '%s' \n" +
                        "group by n.date_x\n" +
                        "order by n.date_x; ",
                cp.get_first_name(),
                cp.get_last_name(),
                cp.get_date_x(),
                cp.get_date_y()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String date_x = rs.getString("date_x");
            int avg_calories_per_meal = rs.getInt("avg_calories_per_meal");
            int num_meals = rs.getInt("num_meals");
            out.println(toHTML(date_x, avg_calories_per_meal, num_meals));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printMaxHeartRate(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Maximum Active Heart Rate Per Day</h2>");
        out.println("<table>");

        out.println(toHTML("FIRST_NAME", "LAST_NAME", "DATE_X", "START_TIME", "END_TIME", "MAX_HEART_RATE (bpm)"));
        String query = String.format(
                "select u.first_name, u.last_name, h.date_x, a.start_time, a.end_time, max(h.heart_rate) as max_heart_rate \n" +
                        "from users u, have_heartrate h, perform_activities a \n" +
                        "where u.user_id = h.user_id \n" +
                        "and u.user_id = a.user_id \n" +
                        "and a.date_x = h.date_x \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "and h.date_x >= '%s' \n" +
                        "and h.date_x <= '%s' \n" +
                        "and h.start_time >= a.start_time \n" +
                        "and h.end_time <= a.end_time \n" +
                        "group by u.first_name, u.last_name, h.date_x, a.start_time, a.end_time\n" +
                        "order by h.date_x;",
                cp.get_first_name(),
                cp.get_last_name(),
                cp.get_date_x(),
                cp.get_date_y()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            String date_x = rs.getString("date_x");
            String start_time = rs.getString("start_time");
            String end_time = rs.getString("end_time");
            int max_heart_rate = rs.getInt("max_heart_rate");
            out.println(toHTML(first_name, last_name, date_x, start_time, end_time, max_heart_rate));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printAvgSleepHeartRate(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Average Heart Rate During Sleep</h2>");
        out.println("<table>");

        out.println(toHTML("DATE_X", "START_TIME", "END_TIME", "AVG_HEART_RATE (bpm)"));
        String query = String.format(
                "select h.date_x, s.start_time, s.end_time, round(avg(h.heart_rate), 2) as avg_heart_rate \n" +
                        "from users u, have_heartrate h, need_sleep s\n" +
                        "where u.user_id = h.user_id \n" +
                        "and u.user_id = s.user_id \n" +
                        "and s.date_x = h.date_x \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "and h.date_x >= '%s' \n" +
                        "and h.date_x <= '%s' \n" +
                        "and h.start_time >= s.start_time \n" +
                        "and h.end_time <= s.end_time \n" +
                        "group by u.first_name, u.last_name, h.date_x, s.start_time, s.end_time\n" +
                        "order by h.date_x;",
                cp.get_first_name(),
                cp.get_last_name(),
                cp.get_date_x(),
                cp.get_date_y()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String date_x = rs.getString("date_x");
            String start_time = rs.getString("start_time");
            String end_time = rs.getString("end_time");
            int avg_heart_rate = rs.getInt("avg_heart_rate");
            out.println(toHTML(date_x, start_time, end_time, avg_heart_rate));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printAvgRestingHeartRate(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Average Resting Heart Rate Per Day</h2>");
        out.println("<table>");

        out.println(toHTML("DATE_X", "AVG_RESTING_HEART_RATE (bpm)"));
        String query = String.format(
                "select h.date_x, round(avg(h.heart_rate), 2) as resting_heart_rate \n" +
                        "from users u, have_heartrate h \n" +
                        "where u.user_id = h.user_id \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "group by h.date_x \n" +
                        "order by h.date_x;",
                cp.get_first_name(),
                cp.get_last_name()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String date_x = rs.getString("date_x");
            int resting_heart_rate = rs.getInt("resting_heart_rate");
            out.println(toHTML(date_x, resting_heart_rate));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public static boolean isValidTime(String inTime) throws Exception {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeFormat.setLenient(false);
        try {
            timeFormat.parse(inTime.trim());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new Exception("TimeParseException", e);
        }
        return true;
    }

    public static boolean isValidDate(String inDate) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new Exception("DateParseException", e);
        }
        return true;
    }

    public String toHTML(Object... objects) {
        String html = "<tr>";
        for (int i=0; i< objects.length; i++) {
            html += String.format("<td>%s</td>", objects[i].toString());
        }
        html += "</tr>";
        return html;
    }
}
