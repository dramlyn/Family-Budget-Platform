package fbp.app.rest;

import fbp.app.dto.CreateTransactionDtoRequest;
import fbp.app.dto.TransactionDto;
import fbp.app.model.type.TransactionType;
import fbp.app.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Transaction Controller Tests")
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private TransactionDto testTransactionDto;
    private CreateTransactionDtoRequest createRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        objectMapper = new ObjectMapper();

        testTransactionDto = TransactionDto.builder()
                .id(1L)
                .amount(100)
                .type(TransactionType.INCOME)
                .userId(1L)
                .categoryId(1L)
                .periodId(1L)
                .createdAt(Instant.now())
                .build();

        createRequest = new CreateTransactionDtoRequest();
        createRequest.setUserId(1L);
        createRequest.setCategoryId(1L);
        createRequest.setAmount(100);
        createRequest.setType(TransactionType.INCOME);
    }

    @Test
    @DisplayName("Should create transaction successfully")
    void shouldCreateTransactionSuccessfully() throws Exception {
        // Given
        when(transactionService.createTransaction(any(CreateTransactionDtoRequest.class)))
                .thenReturn(testTransactionDto);

        // When & Then
        mockMvc.perform(post("/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.amount").value(100))
                .andExpect(jsonPath("$.type").value("INCOME"));

        verify(transactionService).createTransaction(any(CreateTransactionDtoRequest.class));
    }

    @Test
    @DisplayName("Should get transaction by ID successfully")
    void shouldGetTransactionByIdSuccessfully() throws Exception {
        // Given
        when(transactionService.getTransactionById(1L)).thenReturn(testTransactionDto);

        // When & Then
        mockMvc.perform(get("/v1/transaction/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.amount").value(100));

        verify(transactionService).getTransactionById(1L);
    }

    @Test
    @DisplayName("Should get transactions by parameters")
    void shouldGetTransactionsByParameters() throws Exception {
        // Given
        List<TransactionDto> transactions = Arrays.asList(testTransactionDto);
        when(transactionService.getAllByParam(1L, 1L)).thenReturn(transactions);

        // When & Then
        mockMvc.perform(get("/v1/transaction/by-param")
                        .param("periodId", "1")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(transactionService).getAllByParam(1L, 1L);
    }

    @Test
    @DisplayName("Should delete transaction successfully")
    void shouldDeleteTransactionSuccessfully() throws Exception {
        // Given
        doNothing().when(transactionService).deleteTransactionById(1L);

        // When & Then
        mockMvc.perform(delete("/v1/transaction/1"))
                .andExpect(status().isNoContent());

        verify(transactionService).deleteTransactionById(1L);
    }
}