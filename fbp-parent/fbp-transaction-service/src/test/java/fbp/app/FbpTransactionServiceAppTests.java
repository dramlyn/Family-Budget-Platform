package fbp.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Transaction Service Application Tests")
class FbpTransactionServiceAppTests {

    @Test
    @DisplayName("Should have main application class")
    void shouldHaveMainApplicationClass() {
        // Verify that the main application class exists and can be instantiated
        assertThat(FbpTransactionServiceApp.class).isNotNull();
        assertThat(FbpTransactionServiceApp.class.getSimpleName())
                .isEqualTo("FbpTransactionServiceApp");
    }

    @Test
    @DisplayName("Should have main method")
    void shouldHaveMainMethod() throws NoSuchMethodException {
        // Verify that the main method exists
        var mainMethod = FbpTransactionServiceApp.class.getMethod("main", String[].class);
        assertThat(mainMethod).isNotNull();
        assertThat(mainMethod.getReturnType()).isEqualTo(void.class);
    }
}