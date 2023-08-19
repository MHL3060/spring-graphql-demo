package local.demo;

import lombok.Data;

import java.util.List;

@Data
public class Book {

    private String isdn;

    private String title;

    private List<Long> authorIds;
}
