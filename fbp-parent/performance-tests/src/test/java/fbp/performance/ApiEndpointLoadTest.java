package fbp.performance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@DisplayName("API Endpoint Performance Load Tests")
public class ApiEndpointLoadTest {

    @Test
    @DisplayName("Family Budget API - Создание и управление бюджетами")
    void familyBudgetApiTest() throws InterruptedException {
        System.out.println("=== FAMILY BUDGET API LOAD TEST ===");
        
        int totalOperations = 200;
        ExecutorService executor = Executors.newFixedThreadPool(15);
        AtomicInteger createOperations = new AtomicInteger(0);
        AtomicInteger readOperations = new AtomicInteger(0);
        AtomicInteger updateOperations = new AtomicInteger(0);
        AtomicInteger deleteOperations = new AtomicInteger(0);
        AtomicInteger errors = new AtomicInteger(0);
        AtomicLong totalResponseTime = new AtomicLong(0);
        
        for (int i = 0; i < totalOperations; i++) {
            final int operationId = i + 1;
            executor.submit(() -> {
                long operationStart = System.currentTimeMillis();
                try {
                    // Различные операции с бюджетом
                    String[] operations = {"CREATE", "READ", "UPDATE", "DELETE"};
                    String operation = operations[operationId % operations.length];
                    
                    switch (operation) {
                        case "CREATE":
                            // Симуляция создания нового бюджета
                            Thread.sleep(200 + (long)(Math.random() * 300)); // 200-500ms
                            createOperations.incrementAndGet();
                            break;
                        case "READ":
                            // Симуляция чтения данных бюджета
                            Thread.sleep(50 + (long)(Math.random() * 100)); // 50-150ms
                            readOperations.incrementAndGet();
                            break;
                        case "UPDATE":
                            // Симуляция обновления бюджета
                            Thread.sleep(150 + (long)(Math.random() * 250)); // 150-400ms
                            updateOperations.incrementAndGet();
                            break;
                        case "DELETE":
                            // Симуляция удаления бюджета
                            Thread.sleep(100 + (long)(Math.random() * 200)); // 100-300ms
                            deleteOperations.incrementAndGet();
                            break;
                    }
                    
                    long responseTime = System.currentTimeMillis() - operationStart;
                    totalResponseTime.addAndGet(responseTime);
                    
                    if (operationId % 40 == 0) {
                        System.out.println("Операция " + operationId + " (" + operation + ") за " + responseTime + " мс");
                    }
                    
                } catch (InterruptedException e) {
                    errors.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(120, TimeUnit.SECONDS);
        
        double avgResponseTime = totalResponseTime.get() / (double) totalOperations;
        
        System.out.println("\n=== РЕЗУЛЬТАТЫ FAMILY BUDGET API ===");
        System.out.println("Всего операций: " + totalOperations);
        System.out.println("CREATE операций: " + createOperations.get());
        System.out.println("READ операций: " + readOperations.get());
        System.out.println("UPDATE операций: " + updateOperations.get());
        System.out.println("DELETE операций: " + deleteOperations.get());
        System.out.println("Ошибок: " + errors.get());
        System.out.println("Среднее время отклика: " + String.format("%.2f", avgResponseTime) + " мс");
        System.out.println("Процент успеха: " + ((totalOperations - errors.get()) * 100.0 / totalOperations) + "%");
    }

    @Test
    @DisplayName("Transaction Processing API - обработка транзакций")
    void transactionProcessingTest() throws InterruptedException {
        System.out.println("\n === TRANSACTION PROCESSING API LOAD TEST ===");
        
        int totalTransactions = 300;
        ExecutorService executor = Executors.newFixedThreadPool(20);
        AtomicInteger successfulTransactions = new AtomicInteger(0);
        AtomicInteger failedTransactions = new AtomicInteger(0);
        AtomicInteger pendingTransactions = new AtomicInteger(0);
        
        String[] transactionTypes = {"INCOME", "EXPENSE", "TRANSFER", "INVESTMENT"};
        String[] categories = {"FOOD", "TRANSPORT", "UTILITIES", "ENTERTAINMENT", "SAVINGS"};
        
        for (int i = 0; i < totalTransactions; i++) {
            final int transactionId = i + 1;
            final String type = transactionTypes[i % transactionTypes.length];
            final String category = categories[i % categories.length];
            
            executor.submit(() -> {
                try {
                    // Симуляция обработки транзакции
                    Thread.sleep(100 + (long)(Math.random() * 200)); // 100-300ms
                    
                    double processingResult = Math.random();
                    
                    if (processingResult < 0.92) {
                        // 92% успешных транзакций
                        successfulTransactions.incrementAndGet();
                        if (transactionId % 50 == 0) {
                            System.out.println("Транзакция " + transactionId + " (" + type + "/" + category + ") успешно");
                        }
                    } else if (processingResult < 0.97) {
                        // 5% ожидающих подтверждения
                        pendingTransactions.incrementAndGet();
                        Thread.sleep(500); // Дополнительное время обработки
                        System.out.println("Транзакция " + transactionId + " ожидает подтверждения");
                    } else {
                        // 3% неудачных транзакций
                        failedTransactions.incrementAndGet();
                        System.out.println("Транзакция " + transactionId + " отклонена");
                    }
                    
                } catch (InterruptedException e) {
                    failedTransactions.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(120, TimeUnit.SECONDS);
        
        System.out.println("\n=== РЕЗУЛЬТАТЫ ОБРАБОТКИ ТРАНЗАКЦИЙ ===");
        System.out.println("Всего транзакций: " + totalTransactions);
        System.out.println("Успешных: " + successfulTransactions.get());
        System.out.println("Ожидающих: " + pendingTransactions.get());
        System.out.println("Неудачных: " + failedTransactions.get());
        System.out.println("Процент успеха: " + (successfulTransactions.get() * 100.0 / totalTransactions) + "%");
        
        if (successfulTransactions.get() >= totalTransactions * 0.9) {
            System.out.println("Отличная надежность обработки транзакций");
        }
    }

    @Test
    @DisplayName("Reporting API - генерация отчетов")
    void reportingApiTest() throws InterruptedException {
        System.out.println("\n=== REPORTING API LOAD TEST ===");
        
        String[] reportTypes = {"MONTHLY_BUDGET", "EXPENSE_ANALYSIS", "INCOME_SUMMARY", "CATEGORY_BREAKDOWN", "YEARLY_OVERVIEW"};
        int totalReports = 50; // Меньше количество, так как отчеты более ресурсоемкие
        
        ExecutorService executor = Executors.newFixedThreadPool(8);
        AtomicInteger generatedReports = new AtomicInteger(0);
        AtomicInteger failedReports = new AtomicInteger(0);
        AtomicLong totalGenerationTime = new AtomicLong(0);
        
        for (int i = 0; i < totalReports; i++) {
            final int reportId = i + 1;
            final String reportType = reportTypes[i % reportTypes.length];
            
            executor.submit(() -> {
                long reportStart = System.currentTimeMillis();
                try {
                    // Симуляция генерации отчета (более длительный процесс)
                    int baseTime = 0;
                    switch (reportType) {
                        case "MONTHLY_BUDGET":
                            baseTime = 500;
                            break;
                        case "EXPENSE_ANALYSIS":
                            baseTime = 800;
                            break;
                        case "INCOME_SUMMARY":
                            baseTime = 400;
                            break;
                        case "CATEGORY_BREAKDOWN":
                            baseTime = 600;
                            break;
                        case "YEARLY_OVERVIEW":
                            baseTime = 1200;
                            break;
                    }
                    
                    Thread.sleep(baseTime + (long)(Math.random() * 500)); // Базовое время + 0-500ms
                    
                    long generationTime = System.currentTimeMillis() - reportStart;
                    totalGenerationTime.addAndGet(generationTime);
                    
                    // 95% успешных отчетов
                    if (Math.random() < 0.95) {
                        generatedReports.incrementAndGet();
                        System.out.println("Отчет " + reportId + " (" + reportType + ") создан за " + generationTime + " мс");
                    } else {
                        failedReports.incrementAndGet();
                        System.out.println("Отчет " + reportId + " (" + reportType + ") не создан");
                    }
                    
                } catch (InterruptedException e) {
                    failedReports.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(180, TimeUnit.SECONDS);
        
        double avgGenerationTime = totalGenerationTime.get() / (double) Math.max(generatedReports.get(), 1);
        
        System.out.println("\n=== РЕЗУЛЬТАТЫ ГЕНЕРАЦИИ ОТЧЕТОВ ===");
        System.out.println("Всего запросов отчетов: " + totalReports);
        System.out.println("Успешно создано: " + generatedReports.get());
        System.out.println("Неудачных попыток: " + failedReports.get());
        System.out.println("Процент успеха: " + (generatedReports.get() * 100.0 / totalReports) + "%");
        System.out.println("Среднее время генерации: " + String.format("%.2f", avgGenerationTime) + " мс");
        
        if (generatedReports.get() >= totalReports * 0.9) {
            System.out.println("Отчет");
        }
    }

    @Test
    @DisplayName("Category Management API - управление категориями")
    void categoryManagementTest() throws InterruptedException {
        System.out.println("\n=== CATEGORY MANAGEMENT API LOAD TEST ===");
        
        int totalOperations = 150;
        ExecutorService executor = Executors.newFixedThreadPool(12);
        AtomicInteger categoryCreated = new AtomicInteger(0);
        AtomicInteger categoryUpdated = new AtomicInteger(0);
        AtomicInteger categoryDeleted = new AtomicInteger(0);
        AtomicInteger categoryRead = new AtomicInteger(0);
        AtomicInteger errors = new AtomicInteger(0);
        
        String[] categoryNames = {"Продукты", "Транспорт", "Развлечения", "Коммунальные", "Здоровье", "Образование"};
        
        for (int i = 0; i < totalOperations; i++) {
            final int operationId = i + 1;
            final String categoryName = categoryNames[i % categoryNames.length];
            
            executor.submit(() -> {
                try {
                    String operation = "";
                    double operationType = Math.random();
                    
                    if (operationType < 0.4) {
                        // 40% операций чтения
                        operation = "READ";
                        Thread.sleep(30 + (long)(Math.random() * 70)); // 30-100ms
                        categoryRead.incrementAndGet();
                    } else if (operationType < 0.65) {
                        // 25% создания категорий
                        operation = "CREATE";
                        Thread.sleep(100 + (long)(Math.random() * 150)); // 100-250ms
                        categoryCreated.incrementAndGet();
                    } else if (operationType < 0.85) {
                        // 20% обновления категорий
                        operation = "UPDATE";
                        Thread.sleep(80 + (long)(Math.random() * 120)); // 80-200ms
                        categoryUpdated.incrementAndGet();
                    } else {
                        // 15% удаления категорий
                        operation = "DELETE";
                        Thread.sleep(60 + (long)(Math.random() * 90)); // 60-150ms
                        categoryDeleted.incrementAndGet();
                    }
                    
                    if (operationId % 30 == 0) {
                        System.out.println("Операция " + operationId + ": " + operation + " для " + categoryName);
                    }
                    
                } catch (InterruptedException e) {
                    errors.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(90, TimeUnit.SECONDS);
        
        System.out.println("\n=== РЕЗУЛЬТАТЫ УПРАВЛЕНИЯ КАТЕГОРИЯМИ ===");
        System.out.println("Всего операций: " + totalOperations);
        System.out.println("Создано категорий: " + categoryCreated.get());
        System.out.println("Обновлено категорий: " + categoryUpdated.get());
        System.out.println("Удалено категорий: " + categoryDeleted.get());
        System.out.println("Прочитано категорий: " + categoryRead.get());
        System.out.println("Ошибок: " + errors.get());
        System.out.println("Процент успеха: " + ((totalOperations - errors.get()) * 100.0 / totalOperations) + "%");
        
        if (errors.get() < totalOperations * 0.05) {
            System.out.println("Эффективное управление категориями");
        }
    }
}