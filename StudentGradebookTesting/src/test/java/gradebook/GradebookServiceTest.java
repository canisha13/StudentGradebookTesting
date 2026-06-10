package gradebook;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.testng.Assert.*;

public class GradebookServiceTest {

    private GradebookService service;

    @BeforeMethod
    public void setUp() {
        service = new GradebookService();
        service.seedData();
    }
    @Test
    public void seedDataShouldCreateFiveStudents() {
    assertEquals(service.getAllStudents().size(), 5);
    }

    @Test
    public void seedDataShouldCreateUniqueCourseCodes() {
    Set<String> courseCodes = service.getUniqueCourseCodes();
    assertEquals(courseCodes.size(), 4);
    assertTrue(courseCodes.contains("CSD101"));
    assertTrue(courseCodes.contains("JAV101"));
    assertTrue(courseCodes.contains("SDET202"));
    assertTrue(courseCodes.contains("DEVOPS301"));
    }
    @Test
    public void addStudentShouldAddStudentAndCourseCode() {
    Student student = new Student("ST006", "John", "Doe", "CYB101");
    boolean added = service.addStudent(student);
    assertTrue(added);
    assertEquals(service.getAllStudents().size(), 6);
    assertTrue(service.courseCodeExists("CYB101"));
    }
    @Test
    public void addStudentShouldRejectDuplicateStudentId() {
    Student student = new Student("ST001", "John", "Doe", "CYB101");
        boolean added = service.addStudent(student);
        assertFalse(added);
        assertEquals(service.getAllStudents().size(), 5);
    }
    
    
    @Test
    public void removeStudentShouldReturnFalseWhenStudentDoesNotExist() {
    	boolean removed = service.removeStudentById("ST999");
    assertFalse(removed);
    assertEquals(service.getAllStudents().size(),5);
    }
    
    @Test
    public void findStudentByIdShouldBeCaseInsensitive() {
    Optional<Student> student = service.findStudentById("st001");
        assertTrue(student.isPresent());
        assertEquals(student.get().getStudentId(), "ST001");
    }
    
    
    @Test
    public void studentsShouldBeSortedByLastName() {
    	List<Student> students = service.getStudentsSortedByLastName();
        assertEquals(students.get(0).getLastName(), "Ahmed");
        assertEquals(students.get(1).getLastName(), "Brown");
        assertEquals(students.get(2).getLastName(), "Khan");
        assertEquals(students.get(3).getLastName(), "Patel");
        assertEquals(students.get(4).getLastName(), "Smith");
    }
    	

    	
    @Test
    public void addGradeShouldAddGradeToSubject() {
    	 service.addGrade("ST001", "Java", 90);
    	    Map<String, List<Integer>> grades =
    	            service.getGradesForStudent("ST001");
    	    assertEquals(grades.get("Java").get(0).intValue(), 90);
    
    }
    
    
    @Test
    public void addGradeShouldAllowMulipleGradesForSameSubject() {
        service.addGrade("ST001", "Java", 90);
        service.addGrade("ST001", "Java", 80);
        Map<String, List<Integer>> grades =
                service.getGradesForStudent("ST001");
        assertEquals(grades.get("Java").size(), 2);
        assertEquals(grades.get("Java").get(0).intValue(), 90);
        assertEquals(grades.get("Java").get(1).intValue(), 80);
    }
   
    
    @Test
    public void addGradeShouldRejectInvalidLowGrade() {
    IllegalArgumentException exception = expectThrows(
    IllegalArgumentException.class,
    () -> service.addGrade("ST001", "Programming", -1)
    );
    assertEquals(exception.getMessage(),
            "Grade must be between 0 and 100.");

    }
  
    
    
    @Test
    public void addGradeShouldRejectInvalidHighGrade() {
    	IllegalArgumentException exception = expectThrows(
    	        IllegalArgumentException.class,
    	        () -> service.addGrade("ST001", "Programming", 101)
    	    );
    	    assertEquals(
    	        exception.getMessage(),
    	        "Grade must be between 0 and 100."
    	    );
    }
    
    
    
    @Test
    public void addGradeShouldRejectUnknownStudent() {
    	IllegalArgumentException exception = expectThrows(
    	        IllegalArgumentException.class,
    	        () -> service.addGrade("ST999", "Programming", 90)
    	    );
    	    assertEquals(exception.getMessage(), "Student not found.");
    }
   
    @Test
    public void calculateSubjectAverageShouldReturnCorrectAverage() {
    	IllegalArgumentException exception = expectThrows(
    	        IllegalArgumentException.class,
    	        () -> service.addGrade("ST999", "Programming", 90)
    	    );
    	    assertEquals(exception.getMessage(), "Student not found.");
    }

    @Test
    public void addCourseCodeShouldReturnFalseForDuplicateCourseCode() {
    	boolean added = service.addCourseCode("CSD101");
        assertFalse(added);
        assertEquals(service.getUniqueCourseCodes().size(), 4);
    }
    
    
    @Test
    public void addCourseCodeShouldAddNewCourseCode() {
    	boolean added = service.addCourseCode("CYB101");
        assertTrue(added);
        assertTrue(service.courseCodeExists("CYB101"));
    }
    
    @Test
    public void addToAssessmentQueueShouldQueueStudent() {
    service.addToAssessmentQueue("ST001");
    assertEquals(service.getAssessmentQueue().size(), 1);
    }
    
    
    @Test
   public void
   peekNextAssessmentStudentShouldReturnFirstStudentWithoutRemoving() {
    	service.addToAssessmentQueue("ST001");
        Optional<Student> student =
                service.peekNextAssessmentStudent();
        assertTrue(student.isPresent());
        assertEquals(student.get().getStudentId(), "ST001");
        assertEquals(service.getAssessmentQueue().size(), 1);
   
}
    
    @Test
    public void processNextAssessmentStudentShouldUseFifoOrder() {
    	 service.addToAssessmentQueue("ST001");
    	    service.addToAssessmentQueue("ST002");
    	    Optional<Student> student =
    	            service.processNextAssessmentStudent();
    	    assertTrue(student.isPresent());
    	    assertEquals(student.get().getStudentId(), "ST001");
    	    assertEquals(service.getAssessmentQueue().size(), 1);
    }
   
    
    
    @Test
    public void
   processNextAssessmentStudentShouldReturnEmptyWhenQueueIsEmpty() {
    	   Optional<Student> student =
    	            service.processNextAssessmentStudent();
    	    assertFalse(student.isPresent());
    }
  
    
    @Test
    public void recentActionsShouldTrackLatestActionFirst() {
        service.addCourseCode("CYB101");
        Optional<String> action =
                service.getMostRecentAction();
        assertTrue(action.isPresent());
        assertEquals(
                action.get(),
                "Added course code CYB101"
    );
    }
    @Test
    public void undoMostRecentActionShouldRemoveLatestAction() {
    	service.addCourseCode("CYB101");
        Optional<String> action =
                service.undoMostRecentAction();
        assertTrue(action.isPresent());
        assertEquals(action.get(), "Added course code CYB101");
    }
    
    
    @Test
    public void getTopPerformingStudentShouldReturnHighestAverageStudent() {
    	 service.addGrade("ST001", "Java", 80);
    	    service.addGrade("ST002", "Java", 95);
    	    Optional<Student> student =
    	            service.getTopPerformingStudent();
    	    assertTrue(student.isPresent());
    	    assertEquals(student.get().getStudentId(), "ST002");
    }
    
    
    @Test
    public void getStudentsByCourseCodeShouldReturnMatchingStudents() {
    	List<Student> students =
                service.getStudentsByCourseCode("CSD101");
        assertEquals(students.size(), 2);
    }
}
    
    
    
    
    