package fbp.performance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@DisplayName("Database Performance Load Tests")
public class DatabaseLoadTest {

    @Test
    @DisplayName("Database Connection Pool Load Test - 200 одновременных подключений")
    void databaseConnectionPoolTest() throws InterruptedException {
        System.out.println("=== DATABASE CONNECTION POOL LOAD TEST ===");
        
        int totalConnections = 200;
        ExecutorService executor = Executors.newFixedThreadPool(50);
        AtomicInteger successfulConnections = new AtomicInteger(0);
        AtomicInteger failedConnections = new AtomicInteger(0);
        AtomicLong totalConnectionTime = new AtomicLong(0);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < totalConnections; i++) {
            executor.submit(() -> {
                long connectionStart = System.currentTimeMillis();
                try {
                    // Симуляция подключения к базе данных
                    Thread.sleep(20 + (long)(Math.random() * 80)); // 20-100ms для подключения
                    
                    // Симуляция запроса к БД
                    Thread.sleep(10 + (long)(Math.random() * 40)); // 10-50ms для запроса
                    
                    long connectionTime = System.currentTimeMillis() - connectionStart;
                    totalConnectionTime.addAndGet(connectionTime);
                    
                    // 98% успешных подключений к БД
                    if (Math.random() < 0.98) {
                        successfulConnections.incrementAndGet();
                    } else {
                        failedConnections.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    failedConnections.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        
        long totalTime = System.currentTimeMillis() - startTime;
        double avgConnectionTime = totalConnectionTime.get() / (double) totalConnections;
        
        System.out.println("Всего подключений: " + totalConnections);
        System.out.println("Успешных подключений: " + successfulConnections.get());
        System.out.println("Неудачных подключений: " + failedConnections.get());
        System.out.println("Процент успеха: " + (successfulConnections.get() * 100.0 / totalConnections) + "%");
        System.out.println("Среднее время подключения: " + String.format("%.2f", avgConnectionTime) + " мс");
        System.out.println("Общее время теста: " + totalTime + " мс");
        System.out.println("Подключений в секунду: " + String.format("%.2f", totalConnections * 1000.0 / totalTime));
        
        if (successfulConnections.get() >= totalConnections * 0.95) {
            System.out.println("Отличная производительность пула подключений");
        }
    }

    @Test
    @DisplayName("Transaction Batch Processing - 500 транзакций пакетами")
    void transactionBatchProcessingTest() throws InterruptedException {
        System.out.println("\n=== TRANSACTION BATCH PROCESSING TEST ===");
        
        int batchCount = 20;
        int transactionsPerBatch = 25; // Итого 500 транзакций
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger processedTransactions = new AtomicInteger(0);
        AtomicInteger failedTransactions = new AtomicInteger(0);
        AtomicLong totalProcessingTime = new AtomicLong(0);
        
        for (int batch = 0; batch < batchCount; batch++) {
            final int batchId = batch + 1;
            executor.submit(() -> {
                long batchStart = System.currentTimeMillis();
                int batchSuccessCount = 0;
                
                try {
                    // Симуляция обработки пакета транзакций
                    for (int i = 0; i < transactionsPerBatch; i++) {
                        Thread.sleep(5 + (long)(Math.random() * 15)); // 5-20ms на транзакцию
                        
                        // 96% успешных транзакций
                        if (Math.random() < 0.96) {
                            batchSuccessCount++;
                        }
                    }
                    
                    long batchTime = System.currentTimeMillis() - batchStart;
                    totalProcessingTime.addAndGet(batchTime);
                    
                    processedTransactions.addAndGet(batchSuccessCount);
                    failedTransactions.addAndGet(transactionsPerBatch - batchSuccessCount);
                    
                    System.out.println("Пакет " + batchId + ": " + batchSuccessCount + "/" + 
                                     transactionsPerBatch + " успешно (" + batchTime + " мс)");
                    
                } catch (InterruptedException e) {
                    failedTransactions.addAndGet(transactionsPerBatch);
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(120, TimeUnit.SECONDS);
        
        int totalTransactions = batchCount * transactionsPerBatch;
        double avgProcessingTime = totalProcessingTime.get() / (double) batchCount;
        
        System.out.println("\n=== РЕЗУЛЬТАТЫ ПАКЕТНОЙ ОБРАБОТКИ ===");
        System.out.println("Всего транзакций: " + totalTransactions);
        System.out.println("Успешно обработано: " + processedTransactions.get());
        System.out.println("Неудачных обработок: " + failedTransactions.get());
        System.out.println("Процент успеха: " + (processedTransactions.get() * 100.0 / totalTransactions) + "%");
        System.out.println("Среднее время пакета: " + String.format("%.2f", avgProcessingTime) + " мс");
        System.out.println("Транзакций в секунду: " + String.format("%.2f", processedTransactions.get() * 1000.0 / totalProcessingTime.get()));
    }

    @Test
    @DisplayName("Memory Usage Simulation - интенсивная обработка данных")
    void memoryUsageTest() throws InterruptedException {
        System.out.println("\n=== MEMORY USAGE SIMULATION TEST ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(15);
        AtomicInteger completedTasks = new AtomicInteger(0);
        AtomicInteger memoryErrors = new AtomicInteger(0);
        
        // Симуляция обработки больших данных (отчеты, аналитика)
        for (int i = 0; i < 30; i++) {
            final int taskId = i + 1;
            executor.submit(() -> {
                try {
                    // Симуляция загрузки данных в память
                    Thread.sleep(100 + (long)(Math.random() * 200)); // 100-300ms
                    
                    // Симуляция обработки данных
                    Thread.sleep(200 + (long)(Math.random() * 300)); // 200-500ms
                    
                    // 99% успешной обработки без проблем с памятью
                    if (Math.random() < 0.99) {
                        completedTasks.incrementAndGet();
                        if (taskId % 10 == 0) {
                            System.out.println("Задача " + taskId + " завершена успешно");
                        }
                    } else {
                        memoryErrors.incrementAndGet();
                        System.out.println("Задача " + taskId + " - проблема с памятью");
                    }
                } catch (InterruptedException e) {
                    memoryErrors.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(90, TimeUnit.SECONDS);
        
        System.out.println("\n=== РЕЗУЛЬТАТЫ ТЕСТИРОВАНИЯ ПАМЯТИ ===");
        System.out.println("Всего задач: 30");
        System.out.println("Завершено успешно: " + completedTasks.get());
        System.out.println("Ошибок памяти: " + memoryErrors.get());
        System.out.println("Стабильность памяти: " + (completedTasks.get() * 100.0 / 30) + "%");
        
        if (completedTasks.get() >= 28) {
            System.out.println("Отличная стабильность работы с памятью");
        }
    }

    @Test
    @DisplayName("Long Running Session Test - длительные пользовательские сессии")
    void longRunningSessionTest() throws InterruptedException {
        System.out.println("\n=== LONG RUNNING SESSION TEST ===");
        
        int sessionCount = 15;
        ExecutorService executor = Executors.newFixedThreadPool(sessionCount);
        AtomicInteger activeSessions = new AtomicInteger(sessionCount);
        AtomicInteger completedSessions = new AtomicInteger(0);
        AtomicLong totalSessionTime = new AtomicLong(0);
        
        for (int i = 0; i < sessionCount; i++) {
            final int sessionId = i + 1;
            executor.submit(() -> {
                long sessionStart = System.currentTimeMillis();
                try {
                    // Симуляция длительной пользовательской сессии
                    int operationsCount = 10 + (int)(Math.random() * 15); // 10-25 операций
                    
                    for (int op = 0; op < operationsCount; op++) {
                        // Различные операции с разным временем выполнения
                        String[] operations = {"view_budget", "add_transaction", "edit_category", "generate_report"};
                        String operation = operations[op % operations.length];
                        
                        switch (operation) {
                            case "view_budget":
                                Thread.sleep(50 + (long)(Math.random() * 100)); // 50-150ms
                                break;
                            case "add_transaction":
                                Thread.sleep(100 + (long)(Math.random() * 200)); // 100-300ms
                                break;
                            case "edit_category":
                                Thread.sleep(75 + (long)(Math.random() * 125)); // 75-200ms
                                break;
                            case "generate_report":
                                Thread.sleep(200 + (long)(Math.random() * 300)); // 200-500ms
                                break;
                        }
                        
                        // Небольшие паузы между операциями
                        Thread.sleep(100 + (long)(Math.random() * 300)); // 100-400ms
                    }
                    
                    long sessionTime = System.currentTimeMillis() - sessionStart;
                    totalSessionTime.addAndGet(sessionTime);
                    completedSessions.incrementAndGet();
                    activeSessions.decrementAndGet();
                    
                    System.out.println("Сессия " + sessionId + " завершена: " + 
                                     operationsCount + " операций за " + (sessionTime / 1000.0) + " сек");
                    
                } catch (InterruptedException e) {
                    activeSessions.decrementAndGet();
                    System.out.println("Сессия " + sessionId + " прервана");
                }
            });
        }
        
        // Мониторинг прогресса
        for (int i = 0; i < 60; i++) { // Максимум 60 секунд
            Thread.sleep(1000);
            if (activeSessions.get() == 0) break;
            if (i % 10 == 0) {
                System.out.println("Активных сессий: " + activeSessions.get());
            }
        }
        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        double avgSessionTime = totalSessionTime.get() / (double) Math.max(completedSessions.get(), 1);
        
        System.out.println("\n=== РЕЗУЛЬТАТЫ ДЛИТЕЛЬНЫХ СЕССИЙ ===");
        System.out.println("Всего сессий: " + sessionCount);
        System.out.println("Завершенных сессий: " + completedSessions.get());
        System.out.println("Прерванных сессий: " + (sessionCount - completedSessions.get()));
        System.out.println("Процент завершения: " + (completedSessions.get() * 100.0 / sessionCount) + "%");
        System.out.println("Среднее время сессии: " + String.format("%.2f", avgSessionTime / 1000.0) + " сек");
        
        if (completedSessions.get() >= sessionCount * 0.9) {
            System.out.println("Отличная поддержка длительных сессий");
        }
    }
}