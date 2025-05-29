package fbp.performance;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@DisplayName("Basic Load Testing - без внешних зависимостей")
public class BasicLoadTest {

    private static final String BASE_URL = "http://localhost:8081";

    @BeforeAll
    static void setup() {
        System.out.println("Проверяем доступность приложения на " + BASE_URL);
        
        try {
            URL url = new URL(BASE_URL + "/actuator/health");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Приложение доступно Статус: " + responseCode);
            } else if (responseCode == 401) {
                System.out.println("Приложение работает (требует авторизацию). Статус: " + responseCode);
            } else {
                System.out.println("Приложение отвечает со статусом: " + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Приложение недоступно на " + BASE_URL +
                                     ". Запустите fbp-user-service перед тестами. Ошибка: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Health Check Load Test - 100 запросов")
    void healthCheckLoadTest() throws InterruptedException {
        System.out.println("\n=== HEALTH CHECK LOAD TEST ===");
        
        int totalRequests = 100;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        List<Long> responseTimes = new ArrayList<>();
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < totalRequests; i++) {
            executor.submit(() -> {
                long requestStart = System.currentTimeMillis();
                try {
                    URL url = new URL(BASE_URL + "/actuator/health");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    
                    int responseCode = connection.getResponseCode();
                    long responseTime = System.currentTimeMillis() - requestStart;
                    
                    synchronized (responseTimes) {
                        responseTimes.add(responseTime);
                    }
                    
                    if (responseCode == 200 || responseCode == 401) {
                        successCount.incrementAndGet();
                    } else {
                        errorCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        
        long totalTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Всего запросов: " + totalRequests);
        System.out.println("Успешных запросов: " + successCount.get());
        System.out.println("Неудачных запросов: " + errorCount.get());
        System.out.println("Процент успеха: " + (successCount.get() * 100.0 / totalRequests) + "%");
        System.out.println("Общее время: " + totalTime + " мс");
        
        if (!responseTimes.isEmpty()) {
            double avgTime = responseTimes.stream().mapToLong(Long::longValue).average().orElse(0);
            long maxTime = responseTimes.stream().mapToLong(Long::longValue).max().orElse(0);
            long minTime = responseTimes.stream().mapToLong(Long::longValue).min().orElse(0);
            
            System.out.println("Среднее время отклика: " + String.format("%.2f", avgTime) + " мс");
            System.out.println("Максимальное время: " + maxTime + " мс");
            System.out.println("Минимальное время: " + minTime + " мс");
            System.out.println("Запросов в секунду: " + String.format("%.2f", totalRequests * 1000.0 / totalTime));
        }
        
        if (successCount.get() >= totalRequests * 0.95) {
            System.out.println("Отлично! Более 95% запросов успешны");
        }
    }

    @Test
    @DisplayName("API Users Load Test - 50 запросов")
    void userApiLoadTest() throws InterruptedException {
        System.out.println("\n=== USER API LOAD TEST ===");
        
        int totalRequests = 50;
        ExecutorService executor = Executors.newFixedThreadPool(5);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        
        for (int i = 0; i < totalRequests; i++) {
            executor.submit(() -> {
                try {
                    URL url = new URL(BASE_URL + "/api/users");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    
                    int responseCode = connection.getResponseCode();
                    
                    // Принимаем различные статусы как валидные ответы API
                    if (responseCode >= 200 && responseCode < 500) {
                        successCount.incrementAndGet();
                    } else {
                        errorCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        
        System.out.println("Всего запросов: " + totalRequests);
        System.out.println("Успешных ответов: " + successCount.get());
        System.out.println("Ошибок: " + errorCount.get());
        System.out.println("Процент успеха: " + (successCount.get() * 100.0 / totalRequests) + "%");
    }

    @Test
    @DisplayName("Stress Test - 200 смешанных запросов")
    void stressTest() throws InterruptedException {
        System.out.println("\n=== STRESS TEST ===");
        
        String[] endpoints = {
            "/actuator/health",
            "/api/users",
            "/api/families",
            "/api/transactions"
        };
        
        int totalRequests = 200;
        ExecutorService executor = Executors.newFixedThreadPool(20);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < totalRequests; i++) {
            final String endpoint = endpoints[i % endpoints.length];
            executor.submit(() -> {
                try {
                    URL url = new URL(BASE_URL + endpoint);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    
                    int responseCode = connection.getResponseCode();
                    
                    // Принимаем любые HTTP статусы < 500 как успешные
                    if (responseCode < 500) {
                        successCount.incrementAndGet();
                    } else {
                        errorCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        
        long totalTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Всего запросов: " + totalRequests);
        System.out.println("Успешных запросов: " + successCount.get());
        System.out.println("Неудачных запросов (5xx): " + errorCount.get());
        System.out.println("Процент успеха: " + (successCount.get() * 100.0 / totalRequests) + "%");
        System.out.println("Общее время: " + totalTime + " мс");
        System.out.println("Запросов в секунду: " + String.format("%.2f", totalRequests * 1000.0 / totalTime));
        
        if (successCount.get() >= totalRequests * 0.8) {
            System.out.println("Отличная стрессоустойчивость. Более 80% запросов успешны");
        }
    }

    @Test
    @DisplayName("Concurrent Users Simulation")
    void concurrentUsersTest() throws InterruptedException {
        System.out.println("\n👥 === CONCURRENT USERS SIMULATION ===");
        
        int userCount = 25;
        ExecutorService executor = Executors.newFixedThreadPool(userCount);
        AtomicInteger completedSessions = new AtomicInteger(0);
        AtomicInteger totalOperations = new AtomicInteger(0);
        AtomicInteger successfulOperations = new AtomicInteger(0);
        
        for (int i = 0; i < userCount; i++) {
            final int userId = i + 1;
            executor.submit(() -> {
                try {
                    // Имитируем пользовательскую сессию из 3 операций
                    String[] userOperations = {
                        "/actuator/health",
                        "/api/users",
                        "/api/families"
                    };
                    
                    for (String operation : userOperations) {
                        totalOperations.incrementAndGet();
                        
                        try {
                            URL url = new URL(BASE_URL + operation);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(3000);
                            connection.setReadTimeout(3000);
                            
                            int responseCode = connection.getResponseCode();
                            if (responseCode >= 200 && responseCode < 500) {
                                successfulOperations.incrementAndGet();
                            }
                        } catch (Exception e) {
                            // Операция не удалась
                        }
                        
                        Thread.sleep(200); // Пауза между операциями
                    }
                    
                    completedSessions.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("Ошибка сессии пользователя " + userId);
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        
        System.out.println("Пользователей в тесте: " + userCount);
        System.out.println("Завершенных сессий: " + completedSessions.get());
        System.out.println("Всего операций: " + totalOperations.get());
        System.out.println("Успешных операций: " + successfulOperations.get());
        System.out.println("Процент завершенных сессий: " + (completedSessions.get() * 100.0 / userCount) + "%");
        System.out.println("Процент успешных операций: " + (successfulOperations.get() * 100.0 / totalOperations.get()) + "%");
        
        if (completedSessions.get() >= userCount * 0.9) {
            System.out.println("Отличная поддержка одновременных пользователей");
        }
    }
}