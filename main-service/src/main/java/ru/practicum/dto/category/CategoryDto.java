package ru.practicum.dto.category;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Integer id;

    @Size(min = 1, max = 50)
    private String name;
}

