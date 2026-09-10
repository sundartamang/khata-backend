package khata_backend.com.authentication.user.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UsersDTO {
    private Integer id;
    @NotBlank(message = "Name can not be blank")
    @Size(min = 4, message = "Username must be min of 4 characters !")
    private String name;
    @Email(message = "Email address is not valid")
    @NotBlank(message = "Email can not be blank")
    private String email;
    @NotBlank(message = "Password can not be blank")
    @Size(min = 4, max = 13, message = "Password must be min of 3 characters and max of 13 characters !")
    private String password;

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password=password;
    }
}
