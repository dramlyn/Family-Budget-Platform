package fbp.app.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = {
        TestSecurityConfig.class,
        CustomJwtRoleConverter.class
})
@TestPropertySource(properties = {
        "keycloak.server-url=http://localhost:8080",
        "keycloak.realm=test-realm",
        "spring.security.oauth2.client.registration.keycloak.client-id=test-client",
        "spring.security.oauth2.client.registration.keycloak.client-secret=test-secret",
        "keycloak.username=test-user",
        "keycloak.password=test-password",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/test"
})
@DisplayName("Common Security Application Tests")
class CommonSecurityApplicationTests {

    @Test
    @DisplayName("Should load application context successfully")
    void shouldLoadApplicationContextSuccessfully() {
        // Test passes if Spring context loads without exceptions
    }
}