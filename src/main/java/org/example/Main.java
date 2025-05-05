package org.example;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.example.model.Student;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("students.csv");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            CsvToBean<Student> csvToBean = new CsvToBeanBuilder<Student>(reader)
                    .withType(Student.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Student> students = csvToBean.parse();

            System.out.println("###Marks above 80###");

            students.stream()
                    .filter(student -> student.getGrades()
                                                .stream().allMatch(grade -> grade > 80))
                    .forEach(System.out::println);

            System.out.println("\n###Avarage mark above 85###");

            students.stream()
                    .filter(student -> getAvarage(student) >= 85)
                    .forEach(student -> {
                        double avarage = getAvarage(student);
                        System.out.println(student + "| Avarage: " + avarage);
                    });


            Optional<Map.Entry<String, Double>> bestGroup = students.stream()
                    .collect(Collectors.groupingBy(
                            Student::getGroup,
                            Collectors.averagingDouble(Main::getAvarage)
                    ))
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue());

            bestGroup.ifPresent(group ->
                    System.out.println("\nGroup with the highest average grade: " + group.getKey() +
                            " with average grade: " + group.getValue()));

            System.out.println("\n###Count in each group###");

            students.stream()
                    .collect(Collectors.groupingBy(
                            Student::getGroup,
                            Collectors.counting()
                    ))
                    .forEach((group, count) -> System.out.println(group + ": " + count + " students"));

            Student bestStudent = students.stream()
                    .max(Comparator.comparing(Main::getAvarage)).orElse(null);

            System.out.println("\nBest student: " + bestStudent.getName() + " Avarage: " + getAvarage(bestStudent));

            System.out.println("\n###Is there exist students with mark less than 50???###");
            students.stream()
                    .filter(student -> getAvarage(student) <= 50)
                    .findAny()
                    .ifPresentOrElse(student -> System.out.println("YES")
                    , () -> System.out.println("NO"));

            System.out.println("\n###Avarage in each group###");

            students.stream()
                    .collect(Collectors.groupingBy(
                            Student::getGroup,
                            Collectors.averagingDouble(Main::getAvarage)
                    ))
                    .forEach((group, avarage) -> System.out.println(group + ": " + avarage));

            System.out.println("\n###Top 3 students!!!!###");

            students.stream()
                    .sorted(Comparator.comparing(Main::getAvarage).reversed())
                    .limit(3)
                    .forEach(student -> {
                        double avarage = getAvarage(student);
                        int i = 0;

                        System.out.println(student + "| Avarage: " + avarage);
                    });


        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double getAvarage(Student student){
        return student.getGrades().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }
}