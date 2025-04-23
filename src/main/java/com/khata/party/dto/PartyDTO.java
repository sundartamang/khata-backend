package com.khata.party.dto;

import com.khata.party.entity.enums.PartyType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PartyDTO {
    private Integer id;

    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 4, max = 100, message = "Name must be between 4 and 100 characters.")
    private String name;

    @Email(message = "Invalid email address format.")
    @NotBlank(message = "Email cannot be blank.")
    @Size(max = 100, message = "Email must be less than 100 characters.")
    private String email;

    @Size(max = 15, message = "Phone number must be less than or equal to 15 digits.")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be blank.")
    @Size(min = 7, max = 100, message = "Address must be between 7 and 100 characters.")
    private String address;

    @NotBlank(message = "Business name cannot be blank.")
    @Size(min = 7, max = 100, message = "Business name must be between 7 and 100 characters.")
    private String partyBusinessName;

    @NotNull(message = "Party type cannot be null")
    private PartyType partyType;
}
