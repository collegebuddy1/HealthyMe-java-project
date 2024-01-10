package edu.drexel.cs461.examples;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Julia Stoyanovich
 *
 */
public class RegistrarServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Registrar _reg;
    private String _message;

    public void init() throws ServletException {
	_reg = new Registrar();
	_message = _reg.openDBConnection("PgBundle");
    }

    public void printStudentRoster(PrintWriter out) {

	out.println("<h1>Student roster</h1>");
	out.println("<table>");
	
	try {
	    ArrayList<Student> roster = _reg.getRoster();
	    for (int i=0; i<roster.size(); i++) {
		Student student = (Student) roster.get(i);
		out.println(student.toHTML());
	    }
	} catch (SQLException sqle) {
	    sqle.printStackTrace(out);
	}
	
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
  
	response.setContentType("text/html");

	PrintWriter out = response.getWriter();
	out.println("<html><head></head><body>");
      
	if (!_message.startsWith("Servus")) {
	    out.println("<h1>Databaase connection failed to open " + _message + "</h1>");
	} else {
	    printStudentRoster(out);
	}
	  
	out.println("</table>");
	out.println("</html>");
    }
  
    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) 
	throws ServletException, IOException {  
	
	doGet(inRequest, outResponse);  
    }

    public void destroy() {
	_reg.closeDBConnection();
    }
}

