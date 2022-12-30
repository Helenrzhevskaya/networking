package finalProject;

import java.util.*;
import java.util.stream.Collectors;

public class StudentStream {


    public static void main(String[] args) {

        List<Student> students = new ArrayList<>();
        students.add(new Student("Petr", Arrays.asList(
                new Course(1, "testing"),
                new Course(2, "analytics"),
                new Course(3, "German"),
                new Course(4, "programming")
        )));

        students.add(new Student("Anna", Arrays.asList(
                new Course(1, "testing"),
                new Course(2, "analytics"),
                new Course(5, "chemistry"),
                new Course(4, "programming"),
                new Course(6, "english")
        )));

        students.add(new Student("Andrey", Arrays.asList(
                new Course(6, "english"),
                new Course(2, "analytics"),
                new Course(5, "chemistry")
        )));

        students.add(new Student("Alisa", Arrays.asList(
                new Course(6, "english"),
                new Course(8, "machine learning"),
                new Course(7, "italian"),
                new Course(7, "German"),
                new Course(5, "chemistry")
        )));

        students.add(new Student("Anton", Arrays.asList(
                new Course(4, "programming"),
                new Course(8, "machine learning")
        )));
        getCourses(students);
        getNameStudent(students);
        getCoursesAndStudent(students, new Course(8, "machine learning"));

    }

    public static List<Student> getCoursesAndStudent(List<Student> students, Course course) {;

        System.out.println("Список студентов, которые посещают данный курс" + students.stream()
                .filter(s -> s.getCourses()
                        .stream().anyMatch(c -> c.getId() == course.getId()))
                .map(s -> s.getName())
                .collect(Collectors.toSet())
        );

        return students;
    }

    public static List<Student> getCourses(List<Student> students) {

                    System.out.println("Список уникальных курсов, на которые подписаны все студенты" + students.stream()
                            .map(s -> s.getCourses())
                            .flatMap(c -> c.stream())
                            .map(c -> c.getName())
                            .collect(Collectors.toSet())
                    );

            return students;
        }

        public static List<Student> getNameStudent(List<Student> students) {
        System.out.println("список из трех самых любознательных" + students.stream()
                .filter(s -> s.getCourses().size() > 3)
                .map(s -> s.getName())
                .collect(Collectors.toSet())
        );
            return students;
    }



}





