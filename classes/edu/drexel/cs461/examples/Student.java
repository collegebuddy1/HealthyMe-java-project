package edu.drexel.cs461.examples;

/**
 * 
 * An implementation of the Student class.
 *  
 * @author Julia Stoyanovich (stoyanovich@drexel.edu) 
 *
 */
public class Student {
  
  private String _name;
  private int _id;
  private double _gpa;

  /**
   * Constructor.
   * @param name
   */
  public Student(String name) {
    _name = name;
  }
  
  /**
   * Constructor
   * @param id
   * @param name
   * @param gpa
   */
  public Student(int id, String name, double gpa) {
    _name = name;
    _id = id;
    _gpa = gpa;
  }
  
  /**
   * Get the name of the student.
   * @return name
   */
  public String getName() {
    return _name;
  }
  
  /**
   * Get the student's id.
   * @return id
   */
  public int getId() {
    return _id;
  }
  
  /**
   * Get the student's GPA.
   * @return GPA
   */
  public double getGPA() {
    return _gpa;
  }
  
  /**
   * Set the student's id.
   * @param id
   */
  public void setId(int id) {
    _id = id;
  }
  /**
   * Set the student's GPA.
   * @param gpa
   */
  public void setGPA(double gpa) {
    _gpa = gpa;
  }
  /**
   * Generate a string representation of the student.
   * @return string representation
   */
  public String toString() {
    return _id + " : " + _name + ", GPA = " + _gpa;
  }
  
  public String toHTML() {
	  return "<tr><td>" + _id + "</td><td>" + _name + "</td><td>" + _gpa + "</td><td></tr>";
  }
}