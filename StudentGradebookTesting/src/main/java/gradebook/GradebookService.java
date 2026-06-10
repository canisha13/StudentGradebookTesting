package gradebook;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
public class GradebookService {

	private final List<Student> students = new ArrayList<>();
	private final Set<String> courseCodes = new HashSet<>();
	private final Queue<Student> assessmentQueue = new LinkedList<>();
	private final Deque<String> recentActions = new ArrayDeque<>();
	public void seedData() {
	students.clear();
	courseCodes.clear();
	assessmentQueue.clear();
	recentActions.clear();
	
	addStudent(new Student("ST001", "Sarah", "Ahmed", "CSD101"));
	addStudent(new Student("ST002", "James", "Brown", "JAV101"));
	addStudent(new Student("ST003", "Priya", "Patel", "SDET202"));
	addStudent(new Student("ST004", "Michael", "Smith", "DEVOPS301"));
	addStudent(new Student("ST005", "Aisha", "Khan", "CSD101"));
	
	recentActions.clear();
	recentActions.push("Seeded iniƟal student data");
	}
	public boolean addStudent(Student student) {
		if (student == null) {
		throw new IllegalArgumentException("Student cannot be null.");
		}
		if (findStudentById(student.getStudentId()).isPresent()) {
		return false;
		}
		students.add(student);
		courseCodes.add(student.getCourseCode());
		recentActions.push("Added student " + student.getStudentId());
		return true;
		}

		public boolean removeStudentById(String studentId) {
		Optional<Student> student = findStudentById(studentId);
		if (student.isEmpty()) {
			
		return false;
		}
			students.remove(student.get());
			assessmentQueue.remove(student.get());
			rebuildCourseCodes();
			recentActions.push("Removed student " + studentId);
			return true;
			}
		public Optional<Student> findStudentById(String studentId) {
			if (studentId == null) {
			return Optional.empty();
			}
			return students.stream()
			.filter(s -> s.getStudentId().equalsIgnoreCase(studentId))
			.findFirst();
			}
			public List<Student> getAllStudents() {
			return new ArrayList<>(students);
			}
			public List<Student> getStudentsSortedByLastName() {
			return students.stream()
			.sorted(Comparator.comparing(Student::getLastName))
			.toList();
			}
			
			public void addGrade(String studentId, String subject, int grade) {
				Student student = findStudentById(studentId)
				.orElseThrow(() -> new IllegalArgumentException("Student not found."));

				student.addGrade(subject, grade);
				recentActions.push(
				"Added grade " + grade + " for " + student.getStudentId() + " in " + subject
				);
				}
			public Map<String, List<Integer>> getGradesForStudent(String studentId) {
				Student student = findStudentById(studentId)
				.orElseThrow(() -> new IllegalArgumentException("Student not found."));
				return student.getGrades();
				}
			
				public double calculateSubjectAverage(String studentId, String subject) {
				Student student = findStudentById(studentId)
				.orElseThrow(() -> new IllegalArgumentException("Student not found."));
				List<Integer> grades = student.getGrades().get(subject);
				if (grades == null || grades.isEmpty()) {
				throw new IllegalArgumentException("No grades found for subject.");
				}
				return grades.stream()
				.mapToInt(Integer::intValue)
				.average()
				.orElse(0);
				}
				public double calculateOverallAverage(String studentId) {
					Student student = findStudentById(studentId)
					.orElseThrow(() -> new IllegalArgumentException("Student not found."));
					return student.getGrades()
					.values()
					.stream()
					.flatMap(List::stream)
					.mapToInt(Integer::intValue)
					.average()
					.orElse(0);

					}
					public Set<String> getUniqueCourseCodes() {
					return new HashSet<>(courseCodes);
					}
					public boolean courseCodeExists(String courseCode) {
					return courseCodes.contains(courseCode);
					}
					public boolean addCourseCode(String courseCode) {
					if (courseCode == null || courseCode.isBlank()) {
					throw new IllegalArgumentException("Course code is required.");
					}
					boolean added = courseCodes.add(courseCode);
					if (added) {
					recentActions.push("Added course code " + courseCode);
					}
					return added;
					}
					
					public void addToAssessmentQueue(String studentId) {
						Student student = findStudentById(studentId)
						.orElseThrow(() -> new IllegalArgumentException("Student not found."));
						assessmentQueue.add(student);
						recentActions.push("Added " + student.getStudentId() + " to assessment queue");
						}
						public Optional<Student> peekNextAssessmentStudent() {
						return Optional.ofNullable(assessmentQueue.peek());
						}
						public Optional<Student> processNextAssessmentStudent() {
						Student student = assessmentQueue.poll();
						if (student == null) {
						return Optional.empty();

						}
						recentActions.push("Processed assessment for " + student.getStudentId());
						return Optional.of(student);
						}
						public List<Student> getAssessmentQueue() {
						return new ArrayList<>(assessmentQueue);
						}
						public List<String> getRecentActions() {
						return new ArrayList<>(recentActions);
						}
							public Optional<String> getMostRecentAction() {
							return Optional.ofNullable(recentActions.peek());
							
							}
							public Optional<String> undoMostRecentAction() {
							    return Optional.ofNullable(recentActions.poll());
							}

							public Optional<Student> getTopPerformingStudent() {
							    return students.stream()
							            .max(Comparator.comparingDouble(
							                    s -> calculateOverallAverage(s.getStudentId())
							            ));
							}

							public List<Student> getStudentsByCourseCode(String courseCode) {
							    return students.stream()
							            .filter(s -> s.getCourseCode().equalsIgnoreCase(courseCode))
							            .toList();
							}
							
							private void rebuildCourseCodes() {
							    courseCodes.clear();

							    for (Student student : students) {
							        courseCodes.add(student.getCourseCode());
							}
							}
}		
		
		
		
		