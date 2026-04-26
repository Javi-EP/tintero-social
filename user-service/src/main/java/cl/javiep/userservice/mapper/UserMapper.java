package cl.javiep.userservice.mapper;

import cl.javiep.userservice.dto.UserRequestDTO;
import cl.javiep.userservice.dto.UserResponseDTO;
import cl.javiep.userservice.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    // RequestDTO → Entidad
    // Nota: el passwordHash NO se asigna acá — lo hace el service
    public User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setBio(dto.getBio());
        user.setAvatarUrl(dto.getAvatarUrl());
        return user;
    }

    // Entidad → ResponseDTO
    public UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
