package fbp.app.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Simplified Security Tests")
class SimplifiedSecurityTests {

    @Test
    @DisplayName("Should create security configuration instance")
    void shouldCreateSecurityConfigurationInstance() {
        // When
        SecurityConfiguration config = new SecurityConfiguration();

        // Then
        assertThat(config).isNotNull();
    }

    @Test
    @DisplayName("Should create JWT authentication converter")
    void shouldCreateJwtAuthenticationConverter() {
        // Given
        SecurityConfiguration config = new SecurityConfiguration();

        // When
        var converter = config.jwtAuthenticationConverter();

        // Then
        assertThat(converter).isNotNull();
        assertThat(converter).isInstanceOf(JwtAuthenticationConverter.class);
    }

    @Test
    @DisplayName("Should create custom JWT role converter instance")
    void shouldCreateCustomJwtRoleConverterInstance() {
        // When
        CustomJwtRoleConverter converter = new CustomJwtRoleConverter();

        // Then
        assertThat(converter).isNotNull();
    }
}