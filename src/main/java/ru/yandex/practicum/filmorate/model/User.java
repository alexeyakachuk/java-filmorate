package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private Long id;
    @Email
    @NotNull
    private String email;
    @NotBlank(message = "Не может быть пустым")
    @NotNull
    private String login;
    private String name;
    @PastOrPresent(message = "не может быть в будущем")
    private LocalDate birthday;


}
