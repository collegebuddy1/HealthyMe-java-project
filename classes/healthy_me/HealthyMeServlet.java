package healthy_me;

import java.io.*;
import java.util.ArrayList;
import java.sql.SQLException;
import javax.servlet.http.*;
import javax.servlet.*;

public class HealthyMeServlet extends HttpServlet {

    private HealthyMe _healthyme;
    private String _message;

    public void init() throws ServletException {
        _healthyme = new HealthyMe();
        _message = _healthyme.openDBConnection("PgBundle");
    }

    public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        out.println("<!DOCTYPE html><html>");
        out.println("<head>" +
                "<meta charset=\"utf-8\"/>" +
                "<title>HealthyMe</title>" +
                "<style>\n" +
                "table, th, td {border: 1px solid black;}" +
                "table {align: 'center';}" +
                "th, td {padding: 15px; text-align: left;}" +
                "</style>" +
                "</head>");
        out.println("<body>");
        out.println("<h1>Healthy Me</h1>");
        if (!_message.startsWith("Servus")) {
            out.println("<h1>Database connection failed to open " + _message + "</h1>");
        } else {
            if (req.getParameterMap().containsKey("form")) {
                _healthyme.executeForm(req, out);
            }
        }

        //Personal Information
        out.println("<form>"
                + "<h2>1. Add User</h2>"
                + "<input type='hidden' name='form' value='user'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Age: <input type='number' name='age'><br>"
                + "<input type='submit' name='action' value='Add'>"
                + "</form><br>");

        //Body Stats
        out.println("<form>"
                + "<h2>2. Add Body Statistics</h2>"
                + "<input type='hidden' name='form' value='body_stats'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Height: <input type='number' step='any' name='height'>meters<br>"
                + "Weight: <input type='number' step='any' name='weight'>kilograms<br>"
                + "Date: <input type='date' name='date_x'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "<input type='submit' name='action' value='Add'>"
                + "</form><br>");

        //Activities
        out.println("<form>"
                + "<h2>3. Add Activity</h2>"
                + "<input type='hidden' name='form' value='activities'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Exercise Name: <input type='text' name='name'><br>"
                + "Calories Burned: <input type='number' name='calories_burned'>kcal<br>"
                + "Date: <input type='date' name='date_x'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "Start Time: <input type='time' name='start_time'> e.g. 14:25 (02:25 PM on Google Chrome)<br>"
                + "End Time: <input type='time' name='end_time'> e.g. 16:47 (04:47 PM on Google Chrome)<br>"
                + "<input type='submit' name='action' value='Add'>"
                + "</form><br>");

        //Nutrition
        out.println("<form>"
                + "<h2>4. Add Nutrition</h2>"
                + "<input type='hidden' name='form' value='nutrition'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Food Name: <input type='text' name='food_name'><br>"
                + "Meal Type: <select name='meal_type'>"
                + "<option value=breakfast>Breakfast</option>"
                + "<option value=lunch>Lunch</option>"
                + "<option value=dinner>Dinner</option>"
                + "<option value=snack>Snack</option>"
                + "</select><br>"
                + "Calories: <input type='number' name='calories'>kcal<br>"
                + "Date: <input type='date' name='date_x'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "<input type='submit' name='action' value='Add'>"
                + "</form><br>");

        //BMI
        out.println("<form>"
                + "<h2>5. Calculate BMI</h2>"
                + "<input type='hidden' name='form' value='bmi'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Date: <input type='date' name='date_x'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "<input type='submit' name='action' value='Retrieve'>"
                + "</form><br>");

        //Difference in Weight
        out.println("<form>"
                + "<h2>6. Determine Change in Weight</h2>"
                + "<input type='hidden' name='form' value='weight_difference'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Date From: <input type='date' name='date_x'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "Date To: <input type='date' name='date_y'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "<input type='submit' name='action' value='Retrieve'>"
                + "</form><br>");

        //Calories Breakdown
        out.println("<form>"
                + "<h2>7. Calorie Breakdown by Date</h2>"
                + "<input type='hidden' name='form' value='calories_breakdown'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Date: <input type='date' name='date_x'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "<input type='submit' name='action' value='Retrieve'>"
                + "</form><br>");

        //Calories Consumed
        out.println("<form>"
                + "<h2>8. Total Calories Consumed</h2>"
                + "<input type='hidden' name='form' value='calories_consumed'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Date: <input type='date' name='date_x'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "<input type='submit' name='action' value='Retrieve'>"
                + "</form><br>");

        //Calories Burned
        out.println("<form>"
                + "<h2>9. Total Calories Burned</h2>"
                + "<input type='hidden' name='form' value='calories_burned'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Date: <input type='date' name='date_x'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "<input type='submit' name='action' value='Retrieve'>"
                + "</form><br>");

        //Average Steps
        out.println("<form>"
                + "<h2>10. Average Steps Walked Per Day</h2>"
                + "<input type='hidden' name='form' value='average_steps'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Date From: <input type='date' name='date_x'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "Date To: <input type='date' name='date_y'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "<input type='submit' name='action' value='Retrieve'>"
                + "</form><br>");

        //Average Calories Burned
        out.println("<form>"
                + "<h2>11. Average Calories Burned Per Day</h2>"
                + "<input type='hidden' name='form' value='average_calories_burned'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Date From: <input type='date' name='date_x'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "Date To: <input type='date' name='date_y'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "<input type='submit' name='action' value='Retrieve'>"
                + "</form><br>");

        //Average Calories Consumed
        out.println("<form>"
                + "<h2>12. Average Calories Consumed Per Meal</h2>"
                + "<input type='hidden' name='form' value='average_calories_consumed'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Date From: <input type='date' name='date_x'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "Date To: <input type='date' name='date_y'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "<input type='submit' name='action' value='Retrieve'>"
                + "</form><br>");

        //Max Heart Rate
        out.println("<form>"
                + "<h2>13. Maximum Heart Rate During Activity</h2>"
                + "<input type='hidden' name='form' value='max_heart_rate'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Date From: <input type='date' name='date_x'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "Date To: <input type='date' name='date_y'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "<input type='submit' name='action' value='Retrieve'>"
                + "</form><br>");

        //Average Sleep Heart Rate
        out.println("<form>"
                + "<h2>14. Average Heart Rate During Periods of Sleep</h2>"
                + "<input type='hidden' name='form' value='average_sleep_heart_rate'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "Date From: <input type='date' name='date_x'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "Date To: <input type='date' name='date_y'> e.g. 2016-12-31 (12/31/2016 on Google Chrome)<br>"
                + "<input type='submit' name='action' value='Retrieve'>"
                + "</form><br>");

        //Average Resting Heart Rate
        out.println("<form>"
                + "<h2>15. Resting Heart Rate</h2>"
                + "<input type='hidden' name='form' value='average_resting_heart_rate'>"
                + "First Name: <input type='text' name='first_name'> e.g. Ford<br>"
                + "Last Name: <input type='text' name='last_name'> e.g. Prefect<br>"
                + "<input type='submit' name='action' value='Retrieve'>"
                + "</form><br>");

        out.println("</body>");
        log(out, req.getQueryString());
        out.println("</html>");
        out.close();
    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {
        doGet(inRequest, outResponse);
    }

    public void log(PrintWriter out, String message) {
        out.println("<script>console.log('"+message+"');</script>");
    }

    public void destroy() {
        _healthyme.closeDBConnection();
    }
}
