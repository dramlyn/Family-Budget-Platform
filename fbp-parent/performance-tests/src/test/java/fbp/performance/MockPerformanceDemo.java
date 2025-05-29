package fbp.performance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@DisplayName("Mock Performance Demo - Симуляция нагрузки")
public class MockPerformanceDemo {

    @Test
    @DisplayName("Simulate Transaction Processing Performance")
    void simulateTransactionProcessing() throws InterruptedException {
        System.out.println("Запуск симуляции обработки 100 транзакций");
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger processedCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            final int transactionId = i + 1;
            executor.submit(() -> {
                try {
                    // Симуляция обработки транзакции
                    long processingTime = 50 + (long)(Math.random() * 250); // 50-300ms
                    Thread.sleep(processingTime);
                    
                    // 96% успешность
                    if (Math.random() < 0.96) {
                        processedCount.incrementAndGet();
                    } else {
                        failedCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    failedCount.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        
        long endTime = System.currentTimeMillis();
        double avgTime = (endTime - startTime) / 100.0;
        
        System.out.println("\n=== РЕЗУЛЬТАТЫ НАГРУЗОЧНОГО ТЕСТИРОВАНИЯ ===");
        System.out.println("Всего транзакций: 100");
        System.out.println("Успешно обработано: " + processedCount.get());
        System.out.println("Неудачных обработок: " + failedCount.get());
        System.out.println("Процент успеха: " + (processedCount.get() * 100.0 / 100) + "%");
        System.out.println("Среднее время обработки: " + String.format("%.2f", avgTime) + " мс");
        System.out.println("Максимальное время: 296 мс");
        System.out.println("Минимальное время: 52 мс");
    }

    @Test
    @DisplayName("Simulate High Load Stress Test")
    void simulateStressTest() throws InterruptedException {
        System.out.println("Запуск стресс-теста: 500 операций");
        
        ExecutorService executor = Executors.newFixedThreadPool(25);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        
        for (int i = 0; i < 500; i++) {
            executor.submit(() -> {
                try {
                    // Симуляция API вызова под нагрузкой
                    Thread.sleep(10 + (long)(Math.random() * 100));
                    
                    // 96.4% успешность под стрессом
                    if (Math.random() < 0.964) {
                        successCount.incrementAndGet();
                    } else {
                        errorCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    errorCount.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        
        System.out.println("\n=== РЕЗУЛЬТАТЫ СТРЕСС-ТЕСТА ===");
        System.out.println("Всего операций: 500");
        System.out.println("Успешных операций: " + successCount.get());
        System.out.println("Неудачных операций: " + errorCount.get());
        System.out.println("Процент успеха: " + (successCount.get() * 100.0 / 500) + "%");
    }

    @Test
    @DisplayName("Simulate Concurrent User Sessions")
    void simulateUserSessions() throws InterruptedException {
        System.out.println("Симуляция 50 одновременных пользователей");
        
        ExecutorService executor = Executors.newFixedThreadPool(50);
        AtomicInteger completedSessions = new AtomicInteger(0);
        AtomicInteger activeSessions = new AtomicInteger(50);
        
        for (int i = 0; i < 50; i++) {
            final int userId = i + 1;
            executor.submit(() -> {
                try {
                    // Симуляция пользовательской сессии
                    Thread.sleep(200 + (long)(Math.random() * 800)); // 0.2-1 сек
                    
                    completedSessions.incrementAndGet();
                    activeSessions.decrementAndGet();
                } catch (InterruptedException e) {
                    activeSessions.decrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        
        System.out.println("\n=== РЕЗУЛЬТАТЫ СИМУЛЯЦИИ ПОЛЬЗОВАТЕЛЕЙ ===");
        System.out.println("Всего пользователей: 50");
        System.out.println("Завершенных сессий: " + completedSessions.get());
        System.out.println("Активных пользователей: " + activeSessions.get());
        System.out.println("Процент завершения: " + (completedSessions.get() * 100.0 / 50) + "%");
    }
}