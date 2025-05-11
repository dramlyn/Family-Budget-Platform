package fbp.app.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomJwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final List<String> rolesBlackList = List.of("default-roles-dev", "offline_access", "uma_authorization");

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Извлекаем поле realm_access из JWT
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
        if (realmAccess == null || realmAccess.isEmpty()) {
            return List.of(); // Если realm_access отсутствует, возвращаем пустой список ролей
        }

        // Извлекаем список ролей из realm_access.roles
        List<String> roles = (List<String>) realmAccess.get("roles");

        // Преобразуем роли в GrantedAuthority с префиксом ROLE_
        return roles.stream()
                .filter(role -> !rolesBlackList.contains(role))
                .map(role -> "ROLE_" + role.toUpperCase()) // Добавляем префикс ROLE_
                .map(SimpleGrantedAuthority::new)          // Преобразуем в GrantedAuthority
                .collect(Collectors.toList());
    }
}
