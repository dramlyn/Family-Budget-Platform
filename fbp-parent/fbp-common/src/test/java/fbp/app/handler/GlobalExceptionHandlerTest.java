package fbp.app.handler;

import fbp.app.exception.UserServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Global Exception Handler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @DisplayName("Should handle UserServiceException")
    void shouldHandleUserServiceException() {
        // Given
        UserServiceException exception = new UserServiceException("User not found", HttpStatus.NOT_FOUND);

        // When
        ProblemDetail response = globalExceptionHandler.handleResourceNotFoundException(exception);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getProperties().get("error")).isEqualTo("User not found");
    }
}