package local.demo;


import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Author {

    private Long id;
    private String firstName;

    private String lastName;

    private List<String> bookIds;

}
