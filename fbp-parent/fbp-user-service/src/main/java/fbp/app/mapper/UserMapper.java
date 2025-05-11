package fbp.app.mapper;

import fbp.app.dto.user.*;
import fbp.app.model.User;

public class UserMapper {
    public static KeycloakUserDto toKeycloakUserDto(RegisterUserRequestDto request) {
        return KeycloakUserDto.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .build();
    }

    public static KeycloakUserDto toKeycloakUserDto(RegisterParentDtoRequest request) {
        return KeycloakUserDto.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .build();
    }

    public static KeycloakUserDto toKeycloakUserDto(AddParentToFamilyDtoRequest request) {
        return KeycloakUserDto.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .keycloakId(user.getKeycloakId())
                .familyId(user.getFamily().getId())
                .build();
    }
}
