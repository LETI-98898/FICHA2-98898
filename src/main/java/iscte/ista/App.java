package iscte.ista;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

public class App {

    private static final String FIRSTNAMES_F_FILE = "data/firstnames_f.json";
    private static final String FIRSTNAMES_M_FILE = "data/firstnames_m.json";
    private static final String SURNAMES_FILE = "data/surnames.json";

    private static final String STUDENTS_CSV_FILE = "data/students.csv";

    private static final int INITIAL_NUMBER = 21000;

    public static void main(String[] args) {

        try {

            ObjectMapper mapper = new ObjectMapper();

            List<String> femaleNames = mapper.readValue(
                    getFile(FIRSTNAMES_F_FILE),
                    new TypeReference<List<String>>() {}
            );

            List<String> maleNames = mapper.readValue(
                    getFile(FIRSTNAMES_M_FILE),
                    new TypeReference<List<String>>() {}
            );

            List<String> surnames = mapper.readValue(
                    getFile(SURNAMES_FILE),
                    new TypeReference<List<String>>() {}
            );

            Scanner scanner = new Scanner(System.in);

            System.out.print("Quantos estudantes pretende gerar? ");
            int quantity = scanner.nextInt();

            Random random = new Random();

            Set<String> usedNames = new HashSet<>();
            List<Student> students = new ArrayList<>();

            int number = INITIAL_NUMBER;

            while (students.size() < quantity) {

                String firstName;

                if (random.nextBoolean()) {
                    firstName = femaleNames.get(
                            random.nextInt(femaleNames.size())
                    );
                } else {
                    firstName = maleNames.get(
                            random.nextInt(maleNames.size())
                    );
                }

                String surname = surnames.get(
                        random.nextInt(surnames.size())
                );

                String fullName = firstName + " " + surname;

                if (!usedNames.contains(fullName)) {

                    usedNames.add(fullName);

                    students.add(
                            new Student(fullName, number++)
                    );
                }
            }

            File outputFile = new File(STUDENTS_CSV_FILE);
            outputFile.getParentFile().mkdirs();

            PrintWriter writer = new PrintWriter(outputFile);
            writer.println("number,name");

            for (Student student : students) {
                writer.println(student.toCSV());
            }

            writer.close();

            System.out.println(
                    quantity + " estudantes gerados com sucesso."
            );

            System.out.println(
                    "Ficheiro criado: " + STUDENTS_CSV_FILE
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static InputStream getFile(String fileName) {
        return App.class.getClassLoader()
                .getResourceAsStream(fileName);
    }
}