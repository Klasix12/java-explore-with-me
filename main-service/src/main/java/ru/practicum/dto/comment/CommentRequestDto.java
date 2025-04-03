package ru.practicum.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.Data;
import ru.practicum.validator.Update;

@Data
public class CommentRequestDto {
    @NotBlank(groups = Update.class)
    @Size(min = 20, max = 2000, groups = {Update.class, Default.class})
    private String text;
}
