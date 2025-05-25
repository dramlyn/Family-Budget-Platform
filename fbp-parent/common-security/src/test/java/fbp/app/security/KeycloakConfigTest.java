package fbp.app.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = KeycloakConfig.class)
@TestPropertySource(properties = {
        "keycloak.server-url=http://localhost:8080",
        "keycloak.realm=test-realm",
        "spring.security.oauth2.client.registration.keycloak.client-id=test-client",
        "spring.security.oauth2.client.registration.keycloak.client-secret=test-secret",
        "keycloak.username=test-user",
        "keycloak.password=test-password"
})
@DisplayName("Keycloak Configuration Tests")
class KeycloakConfigTest {

    @Autowired
    private KeycloakConfig keycloakConfig;

    @Test
    @DisplayName("Should create Keycloak bean successfully")
    void shouldCreateKeycloakBeanSuccessfully() {
        // When
        Keycloak keycloak = keycloakConfig.keycloak();

        // Then
        assertThat(keycloak).isNotNull();
        assertThat(keycloak.serverInfo()).isNotNull();
    }

    @Test
    @DisplayName("Should have correct configuration annotation")
    void shouldHaveCorrectConfigurationAnnotation() {
        // Given
        Class<?> configClass = KeycloakConfig.class;

        // Then
        assertThat(configClass.isAnnotationPresent(org.springframework.context.annotation.Configuration.class))
                .isTrue();
    }
}