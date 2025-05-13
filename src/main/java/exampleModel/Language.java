package exampleModel;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Language {

    @NotBlank
    private String type;

    @NotBlank
    private Integer version;
}
