package fbp.app.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = {TestSecurityConfig.class, CustomJwtRoleConverter.class})
@TestPropertySource(properties = {
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/test"
})
@DisplayName("Security Configuration Tests")
class SecurityConfigurationTest {

    @Autowired
    private TestSecurityConfig testSecurityConfig;

    @Test
    @DisplayName("Should have test security configuration bean")
    void shouldHaveTestSecurityConfigurationBean() {
        // Then
        assertThat(testSecurityConfig).isNotNull();
    }

    @Test
    @DisplayName("Should have correct test configuration annotations")
    void shouldHaveCorrectTestConfigurationAnnotations() {
        // Given
        Class<?> configClass = TestSecurityConfig.class;

        // Then
        assertThat(configClass.isAnnotationPresent(org.springframework.boot.test.context.TestConfiguration.class))
                .isTrue();
        assertThat(configClass.isAnnotationPresent(org.springframework.security.config.annotation.web.configuration.EnableWebSecurity.class))
                .isTrue();
    }
}