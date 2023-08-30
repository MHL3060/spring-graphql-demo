package local.demo;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Author {

    private Long id;


    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private List<String> bookIds;

    @Email
    private String emailAddress;
}
