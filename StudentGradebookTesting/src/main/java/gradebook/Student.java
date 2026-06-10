package gradebook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {
private final String studentId;
private final String firstName;
private final String lastName;
private final String courseCode;

private final Map<String, List<Integer>> grades;

public Student(String studentId, String firstName, String lastName, String courseCode) {
if (studentId == null || studentId.isBlank()) {
throw new IllegalArgumentException("Student ID is required.");
}
if (firstName == null || firstName.isBlank()) {
throw new IllegalArgumentException("First name is required.");
}
if (lastName == null || lastName.isBlank()) {
throw new IllegalArgumentException("Last name is required.");
}
if (courseCode == null || courseCode.isBlank()) {
throw new IllegalArgumentException("Course code is required.");
}
this.studentId = studentId;
this.firstName = firstName;
this.lastName = lastName;
this.courseCode = courseCode;
this.grades = new HashMap<>();
}
public String getStudentId() {
return studentId;
}
public String getFirstName() {
return firstName;
}
public String getLastName() {

return lastName;
}
public String getCourseCode() {
return courseCode;
}
public String getFullName() {
return firstName + " " + lastName;
}
public Map<String, List<Integer>> getGrades() {
return grades;
}
public void addGrade(String subject, int grade) {
if (subject == null || subject.isBlank()) {
throw new IllegalArgumentException("Subject is required.");
}
if (grade < 0 || grade > 100) {
throw new IllegalArgumentException("Grade must be between 0 and 100.");
}
grades.putIfAbsent(subject, new ArrayList<>());
grades.get(subject).add(grade);
}
}
