package ru.test.bonus.dto;

//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//public class User {
//    Integer id;
//    String email;
//    String password;
//}

public record User(Integer id,
        String email,
        String password) {
}