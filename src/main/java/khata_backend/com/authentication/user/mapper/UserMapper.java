package khata_backend.com.authentication.user.mapper;

import khata_backend.com.authentication.user.model.dto.UsersDTO;
import khata_backend.com.authentication.user.model.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public Users toEntity(UsersDTO usersDTO) {
        if (usersDTO == null) {
            return null;
        }
        Users users = new Users();
        users.setId(usersDTO.getId());
        users.setName(usersDTO.getName());
        users.setEmail(usersDTO.getEmail());
        return users;
    }

    public UsersDTO toDto(Users users) {
        if (users == null) {
            return null;
        }
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setId(users.getId());
        usersDTO.setName(users.getName());
        usersDTO.setEmail(users.getEmail());
        return usersDTO;
    }
}