import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import lombok.*;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

@Data
class Student {
        private int studentId;
        private String studentName;
        private String studentDOB;
        private String studentSubject;
}

interface StudentService {
        void addNewStudent(Student student);
        void displayAllStudents();
        void updateStudent(int studentId, Student updatedStudent);
        void removeStudentById(int studentId);
        Student getStudentById(int studentId);
        List<Student> searchStudents(String keyword);
}

@NoArgsConstructor
class StudentServiceImp implements StudentService {
        private final List<Student> studentList = new ArrayList<>();
        private final String FILE_PATH = "student.dat";


        @Override
        public void addNewStudent(Student student) {
                Scanner scanner = new Scanner(System.in);
                // Prompt a name
                System.out.print("Enter student's name : ");
                String studentName = scanner.nextLine();

                System.out.print("Enter student date of birth: (YYYY-MM-DD) ");
                String studentDOB = scanner.nextLine();

                System.out.print("Enter student subject: ");
                String studentSubject = scanner.nextLine();

                // Generate ID
                int studentId = studentList.size() + 1;



                student.setStudentId(studentId);
                student.setStudentName(studentName);
                student.setStudentDOB(studentDOB);
                student.setStudentSubject(studentSubject);

                //Updated/Created Date

                studentList.add(student); // Add student to the list

                System.out.println("Student added successfully.");
        }

        @Override
        public void displayAllStudents() {
                if (studentList.isEmpty()) {
                        System.out.println("No students available.");
                        return;
                }

                Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
                table.addCell("ID");
                table.addCell("STUDENT'S NAME");
                table.addCell("DATE OF BIRTH");
                table.addCell("CLASS");
                table.addCell("UPDATED");

                for (Student student : studentList) {
                        table.addCell(String.valueOf(student.getStudentId()), 1);
                        table.addCell(student.getStudentName(), 1);
                        table.addCell(student.getStudentDOB(), 1);
                        table.addCell(student.getStudentSubject(), 1);
                        // Assuming the 'updated' field is always 'Not Updated' for simplicity
                        table.addCell("Not Updated", 1);
                }

                System.out.println(table.render());
        }

        @Override
        public void updateStudent(int studentId, Student updatedStudent) {
                for (int i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).getStudentId() == studentId) {
                                studentList.set(i, updatedStudent);
                                System.out.println("Student updated successfully.");
                                return;
                        }
                }
                System.out.println("Student with ID " + studentId + " not found.");
        }

        @Override
        public void removeStudentById(int studentId) {
                boolean removed = studentList.removeIf(student -> student.getStudentId() == studentId);
                if (removed) {
                        System.out.println("Student with ID " + studentId + " removed successfully.");
                } else {
                        System.out.println("Student with ID " + studentId + " not found.");
                }
        }

        @Override
        public Student getStudentById(int studentId) {
                for (Student student : studentList) {
                        if (student.getStudentId() == studentId) {
                                return student;
                        }
                }
                return null;
        }

        @Override
        public List<Student> searchStudents(String keyword) {
                List<Student> searchResults = new ArrayList<>();
                for (Student student : studentList) {
                        if (student.getStudentName().contains(keyword) || String.valueOf(student.getStudentId()).contains(keyword)) {
                                searchResults.add(student);
                        }
                }
                return searchResults;
        }
}

@RequiredArgsConstructor
class View {
        private final StudentService studentService;
        private final int PAGE_SIZE = 5;

        public void menu() {
                Scanner scanner = new Scanner(System.in);
                while (true) {

                        System.out.println("""
                                
                                ░█████╗░░██████╗████████╗░█████╗░██████╗░
                                ██╔══██╗██╔════╝╚══██╔══╝██╔══██╗██╔══██╗
                                ██║░░╚═╝╚█████╗░░░░██║░░░███████║██║░░██║
                                ██║░░██╗░╚═══██╗░░░██║░░░██╔══██║██║░░██║
                                ╚█████╔╝██████╔╝░░░██║░░░██║░░██║██████╔╝
                                ░╚════╝░╚═════╝░░░░╚═╝░░░╚═╝░░╚═╝╚═════╝░
                                
                                """);
                        System.out.println("=".repeat(150));
                        System.out.println("1. ADD NEW STUDENT\t2. LIST ALL STUDENTS\t3. UPDATE STUDENT");
                        System.out.println("4. REMOVE STUDENT\t5. SEARCH FOR STUDENT\t6. COMMIT DATA TO FILE");
                        System.out.println("7. PAGINATION\t\t8. EXIT");
                        System.out.println("=".repeat(150));
                        System.out.print("Enter your choice: ");
                        int choice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character

                        switch (choice) {
                                case 1:
                                        addNewStudent(scanner);
                                        break;
                                case 2:
                                        studentService.displayAllStudents();
                                        break;
                                case 3:
                                        updateStudent(scanner);
                                        break;
                                case 4:
                                        removeStudent(scanner);
                                        break;
                                case 5:
                                        searchStudent(scanner);
                                        break;
                                case 6:
                                        // Not implemented yet
                                        break;
                                case 7:
                                        // Not implemented yet
                                        break;
                                case 8:
                                        System.out.println("Exiting...");
                                        return;
                                default:
                                        System.out.println("Invalid choice! Please enter a number between 1 and 8.");
                        }
                }
        }

        private void addNewStudent(Scanner scanner) {
                Student student = new Student();
                studentService.addNewStudent(student);
        }

        private void updateStudent(Scanner scanner) {
                System.out.print("Enter student ID to update: ");
                int studentId = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                Student existingStudent = studentService.getStudentById(studentId);
                if (existingStudent != null) {
                        Student updatedStudent = new Student();
                        System.out.print("Enter new name: ");
                        updatedStudent.setStudentName(scanner.nextLine());
                        System.out.print("Enter new date of birth: ");
                        updatedStudent.setStudentDOB(scanner.nextLine());
                        System.out.print("Enter new subject: ");
                        updatedStudent.setStudentSubject(scanner.nextLine());

                        studentService.updateStudent(studentId, updatedStudent);
                } else {
                        System.out.println("Student with ID " + studentId + " not found.");
                }
        }

        private void removeStudent(Scanner scanner) {
                System.out.print("Enter student ID to remove: ");
                int studentId = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                studentService.removeStudentById(studentId);
        }

        private void searchStudent(Scanner scanner) {
                System.out.print("Enter search keyword: ");
                String keyword = scanner.nextLine();

                List<Student> searchResults = studentService.searchStudents(keyword);
                if (searchResults.isEmpty()) {
                        System.out.println("No students found matching the keyword.");
                } else {
                        System.out.println("Search Results:");
                        for (Student student : searchResults) {
                                System.out.println("ID: " + student.getStudentId() + ", Name: " + student.getStudentName());
                        }
                }
        }
}

public class Main {
        public static void main(String[] args) {
                StudentService studentService = new StudentServiceImp();
                View view = new View(studentService);
                view.menu();
        }
}
