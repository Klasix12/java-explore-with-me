package ru.practicum.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Getter
@Setter
@ToString
public class UpdateCompilationRequest {
    @UniqueElements
    private List<Integer> events;

    private Boolean pinned;

    @Size(min = 1, max = 50)
    private String title;
}
