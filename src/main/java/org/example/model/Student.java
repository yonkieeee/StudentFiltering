package org.example.model;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import lombok.*;

import java.util.List;

@Getter @Setter
@ToString @NoArgsConstructor @Data
public class Student {

    @CsvBindByName
    private String name;

    @CsvBindByName
    private String group;

    @CsvBindAndSplitByName(elementType = Integer.class, splitOn = ",")
    private List<Integer> grades;
}
