package local.demo;


import lombok.Data;

import java.util.List;

@Data
public class Author {

    private Long id;

    private String firstName;

    private String lastName;

    private List<String> bookIds;

    private String emailAddress;
}
