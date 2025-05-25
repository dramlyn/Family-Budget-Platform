package fbp.app.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

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
@DisplayName("Security Integration Tests")
class SecurityIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Should load all security components successfully")
    void shouldLoadAllSecurityComponentsSuccessfully() {
        // Then
        assertThat(applicationContext.getBean(TestSecurityConfig.class)).isNotNull();
        assertThat(applicationContext.getBean(CustomJwtRoleConverter.class)).isNotNull();
        assertThat(applicationContext.getBean(SecurityFilterChain.class)).isNotNull();
    }

    @Test
    @DisplayName("Should create custom JWT role converter successfully")
    void shouldCreateCustomJwtRoleConverterSuccessfully() {
        // When
        CustomJwtRoleConverter converter = applicationContext.getBean(CustomJwtRoleConverter.class);

        // Then
        assertThat(converter).isNotNull();
        assertThat(converter).isInstanceOf(CustomJwtRoleConverter.class);
    }
}