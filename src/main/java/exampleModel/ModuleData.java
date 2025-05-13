package exampleModel;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ModuleData {

    private MultipartFile schema;

    @NotBlank
    private String moduleName;

    @NotBlank
    private String author;

    private String description;

    @NotBlank
    private Language language;

    @NotBlank
    private String build;

    private List<String> dependencies;
}
