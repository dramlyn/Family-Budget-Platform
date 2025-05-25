package fbp.app.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

@DisplayName("User Service Exception Tests")
class UserServiceExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        // Given
        String message = "User not found";

        // When
        UserServiceException exception = new UserServiceException(message, HttpStatus.OK);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should create exception with message and cause")
    void shouldCreateExceptionWithMessageAndCause() {
        // Given
        String message = "Database error";

        // When
        UserServiceException exception = new UserServiceException(message, HttpStatus.OK);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}