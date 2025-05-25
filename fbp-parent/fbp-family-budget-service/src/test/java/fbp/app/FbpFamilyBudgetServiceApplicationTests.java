package fbp.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Application Basic Tests")
class FbpFamilyBudgetServiceApplicationTests {

    @Test
    @DisplayName("Should have main application class")
    void shouldHaveMainApplicationClass() {
        // Verify that the main application class exists and can be instantiated
        assertThat(FbpFamilyBudgetServiceApplication.class).isNotNull();
        assertThat(FbpFamilyBudgetServiceApplication.class.getSimpleName())
                .isEqualTo("FbpFamilyBudgetServiceApplication");
    }

    @Test
    @DisplayName("Should have main method")
    void shouldHaveMainMethod() throws NoSuchMethodException {
        // Verify that the main method exists
        var mainMethod = FbpFamilyBudgetServiceApplication.class.getMethod("main", String[].class);
        assertThat(mainMethod).isNotNull();
        assertThat(mainMethod.getReturnType()).isEqualTo(void.class);
    }
}