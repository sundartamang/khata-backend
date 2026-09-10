package khata_backend.com.settings.department.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class DepartmentDTO {

    private UUID id;

    @NotBlank(message = "Department code cannot be blank.")
    @Size(max = 50, message = "Department code must be less than 50 characters.")
    private String departmentCode;

    @NotBlank(message = "Department name cannot be blank.")
    @Size(min=3, max = 100, message = "Department name must be between 3-100 characters.")
    private String departmentName;
}
