// lombok library for the project
import lombok.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
@Getter
@Setter
@NoArgsConstructor
class Course {
    private int courseId;
    private String courseName;
    private String courseStartedDate;
    private String courseEndedDate;
    private boolean isAvailable;

    public Course(int courseId, String courseName, String courseStartedDate, String courseEndedDate, boolean isAvailable) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseStartedDate = courseStartedDate;
        this.courseEndedDate = courseEndedDate;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseName;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseName = courseTitle;
    }

    public String getCourseStartedDate() {
        return courseStartedDate;
    }

    public void setCourseStartedDate(String courseStartedDate) {
        this.courseStartedDate = courseStartedDate;
    }

    public String getCourseEndedDate() {
        return courseEndedDate;
    }

    public void setCourseEndedDate(String courseEndedDate) {
        this.courseEndedDate = courseEndedDate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

interface CourseService {
    void addNewCourse();
    void getAllCourses();
    void getCourseById();
}

class CourseServiceImp implements CourseService {
    private List<Course> courseList;
    private final String FILE_PATH = "course.csv";

    public CourseServiceImp() {
        courseList = new ArrayList<>();
    }

    @Override
    public void addNewCourse() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter course title: ");
        String courseTitle = scanner.nextLine();

        // Generate course ID
        int courseId = courseList.size() + 1;

        // Generate course dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();

        String courseStartedDate = dateFormat.format(currentDate);
        String courseEndedDate = "2025-01-31";

        boolean isAvailable = true;

        Course course = new Course(courseId, courseTitle, courseStartedDate, courseEndedDate, isAvailable);

        courseList.add(course);
        saveCourseListToFile();

        System.out.println("New course added successfully.");
    }
    @Override
    public void getAllCourses() {
        loadCourseListFromFile();

        if (courseList.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            // Print table header
            System.out.println("Course_id\tcourse_name\t\tcourse_start_date\tcourse_ended_date\tcourse_available");
            System.out.println("----------------------------------------------------------------------------------");

            // Print course data
            for (Course course : courseList) {
                System.out.printf("%-15d\t%-15s\t%-12s\t%-20" + "s\t%-20s\n",
                        course.getCourseId(), course.getCourseTitle(), course.getCourseStartedDate(),
                        course.getCourseEndedDate(), course.isAvailable());
            }
        }
    }

    @Override
    public void getCourseById() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter course ID: ");
        int courseId = scanner.nextInt();

        loadCourseListFromFile();

        boolean found = false;

        for (Course course : courseList) {
            if (course.getCourseId() == courseId) {
                found = true;


                System.out.println("Course_id\tcourse_name\t\tcourse_start_date\tcourse_ended_date\tcourse_available");
                System.out.println("---------------------------------------------------------------------------------");
                System.out.printf("%-15d\t%-15s\t%-12s\t%-20" + "s\t%-20s\n",
                        course.getCourseId(), course.getCourseTitle(), course.getCourseStartedDate(),
                        course.getCourseEndedDate(), course.isAvailable());

                break;
            }
        }

        if (!found) {
            System.out.println("Course not found.");
        }
    }

    private void loadCourseListFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            courseList.clear();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int courseId = Integer.parseInt(data[0]);
                String courseTitle = data[1];
                String courseStartedDate = data[2];
                String courseEndedDate = data[3];

                boolean isAvailable = Boolean.parseBoolean(data[4]);
                Course course = new Course(courseId, courseTitle, courseStartedDate, courseEndedDate, isAvailable);
                courseList.add(course);
            }
        } catch (IOException e) {
            System.out.println("Error reading course data from file.");
        }
    }

    private void saveCourseListToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Course course : courseList) {
                writer.write(course.getCourseId() + "," + course.getCourseTitle() + "," +
                        course.getCourseStartedDate() + "," + course.getCourseEndedDate() + "," +
                        course.isAvailable() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing course data to file.");
        }
    }
}

class View {
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        CourseService courseService = new CourseServiceImp();

        while (true) {
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("1. Add new course");
            System.out.println("2. Get all courses");
            System.out.println("3. Search course by ID");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    courseService.addNewCourse();
                    break;
                case 2:
                    courseService.getAllCourses();
                    break;
                case 3:
                    courseService.getCourseById();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {

        View.menu();
    }
}

