package fbp.app.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Custom JWT Role Converter Tests")
class CustomJwtRoleConverterTest {

    private CustomJwtRoleConverter converter;
    private Jwt.Builder jwtBuilder;

    @BeforeEach
    void setUp() {
        converter = new CustomJwtRoleConverter();
        jwtBuilder = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .issuer("test-issuer")
                .subject("test-subject")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600));
    }

    @Test
    @DisplayName("Should convert JWT roles to granted authorities successfully")
    void shouldConvertJwtRolesToGrantedAuthoritiesSuccessfully() {
        // Given
        Map<String, Object> realmAccess = Map.of("roles", List.of("PARENT", "USER"));
        Jwt jwt = jwtBuilder.claim("realm_access", realmAccess).build();

        // When
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Then
        assertThat(authorities).hasSize(2);
        assertThat(authorities).containsExactlyInAnyOrder(
                new SimpleGrantedAuthority("ROLE_PARENT"),
                new SimpleGrantedAuthority("ROLE_USER")
        );
    }

    @Test
    @DisplayName("Should filter out blacklisted roles")
    void shouldFilterOutBlacklistedRoles() {
        // Given
        Map<String, Object> realmAccess = Map.of("roles", 
                List.of("PARENT", "default-roles-dev", "offline_access", "uma_authorization", "USER"));
        Jwt jwt = jwtBuilder.claim("realm_access", realmAccess).build();

        // When
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Then
        assertThat(authorities).hasSize(2);
        assertThat(authorities).containsExactlyInAnyOrder(
                new SimpleGrantedAuthority("ROLE_PARENT"),
                new SimpleGrantedAuthority("ROLE_USER")
        );
    }

    @Test
    @DisplayName("Should return empty collection when realm_access is null")
    void shouldReturnEmptyCollectionWhenRealmAccessIsNull() {
        // Given
        Jwt jwt = jwtBuilder.build();

        // When
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Then
        assertThat(authorities).isEmpty();
    }

    @Test
    @DisplayName("Should return empty collection when realm_access is empty")
    void shouldReturnEmptyCollectionWhenRealmAccessIsEmpty() {
        // Given
        Jwt jwt = jwtBuilder.claim("realm_access", Map.of()).build();

        // When
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Then
        assertThat(authorities).isEmpty();
    }

    @Test
    @DisplayName("Should handle mixed case roles correctly")
    void shouldHandleMixedCaseRolesCorrectly() {
        // Given
        Map<String, Object> realmAccess = Map.of("roles", List.of("parent", "Admin", "USER"));
        Jwt jwt = jwtBuilder.claim("realm_access", realmAccess).build();

        // When
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Then
        assertThat(authorities).hasSize(3);
        assertThat(authorities).containsExactlyInAnyOrder(
                new SimpleGrantedAuthority("ROLE_PARENT"),
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER")
        );
    }

    @Test
    @DisplayName("Should handle empty roles list")
    void shouldHandleEmptyRolesList() {
        // Given
        Map<String, Object> realmAccess = Map.of("roles", List.of());
        Jwt jwt = jwtBuilder.claim("realm_access", realmAccess).build();

        // When
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Then
        assertThat(authorities).isEmpty();
    }

    @Test
    @DisplayName("Should handle single role correctly")
    void shouldHandleSingleRoleCorrectly() {
        // Given
        Map<String, Object> realmAccess = Map.of("roles", List.of("ADMIN"));
        Jwt jwt = jwtBuilder.claim("realm_access", realmAccess).build();

        // When
        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Then
        assertThat(authorities).hasSize(1);
        assertThat(authorities).containsExactly(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}