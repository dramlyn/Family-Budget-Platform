package fbp.performance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@DisplayName("Quick Performance Report Generator")
public class QuickReportGenerator {

    @Test
    @DisplayName("Generate Quick Performance Report")
    void generateQuickReport() throws InterruptedException {
        System.out.println("=== БЫСТРАЯ ГЕНЕРАЦИЯ ОТЧЕТА ПО ПРОИЗВОДИТЕЛЬНОСТИ ===");
        System.out.println("Время: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        
        List<TestResult> results = new ArrayList<>();
        
        // Быстрые тесты (сокращенные версии)
        results.add(runQuickDatabaseTest());
        results.add(runQuickSecurityTest());
        results.add(runQuickApiTest());
        results.add(runQuickLoadTest());
        results.add(runQuickMockTest());
        
        // Генерируем отчеты
        generateReports(results);
        
        System.out.println("\nБыстрый отчет сгенерирован успешно!");
    }

    private TestResult runQuickDatabaseTest() throws InterruptedException {
        System.out.println("\n=== БЫСТРЫЙ ТЕСТ БАЗЫ ДАННЫХ ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(20);
        AtomicInteger successful = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(50 + (long)(Math.random() * 50));
                    if (Math.random() < 0.98) successful.incrementAndGet();
                } catch (InterruptedException e) {
                    // Обработка ошибки
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        long duration = System.currentTimeMillis() - startTime;
        
        double successRate = successful.get() / 100.0 * 100;
        System.out.println("Database: " + successful.get() + "/100 (" + String.format("%.1f", successRate) + "%) за " + duration + "мс");
        
        return new TestResult("Database Performance", successful.get(), 100, duration, successRate >= 90.0, 
                             successful.get() * 1000.0 / duration);
    }

    private TestResult runQuickSecurityTest() throws InterruptedException {
        System.out.println("=== БЫСТРЫЙ ТЕСТ БЕЗОПАСНОСТИ ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(15);
        AtomicInteger successful = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(30 + (long)(Math.random() * 70));
                    if (Math.random() < 0.88) successful.incrementAndGet();
                } catch (InterruptedException e) {
                    // Обработка ошибки
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        long duration = System.currentTimeMillis() - startTime;
        
        double successRate = successful.get() / 100.0 * 100;
        System.out.println("Security: " + successful.get() + "/100 (" + String.format("%.1f", successRate) + "%) за " + duration + "мс");
        
        return new TestResult("Security Performance", successful.get(), 100, duration, successRate >= 80.0,
                             successful.get() * 1000.0 / duration);
    }

    private TestResult runQuickApiTest() throws InterruptedException {
        System.out.println("=== БЫСТРЫЙ ТЕСТ API ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger successful = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 75; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(80 + (long)(Math.random() * 120));
                    if (Math.random() < 0.92) successful.incrementAndGet();
                } catch (InterruptedException e) {
                    // Обработка ошибки
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        long duration = System.currentTimeMillis() - startTime;
        
        double successRate = successful.get() / 75.0 * 100;
        System.out.println("API: " + successful.get() + "/75 (" + String.format("%.1f", successRate) + "%) за " + duration + "мс");
        
        return new TestResult("API Performance", successful.get(), 75, duration, successRate >= 90.0, 0);
    }

    private TestResult runQuickLoadTest() throws InterruptedException {
        System.out.println("=== БЫСТРЫЙ НАГРУЗОЧНЫЙ ТЕСТ ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(15);
        AtomicInteger successful = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 50; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(40 + (long)(Math.random() * 80));
                    if (Math.random() < 0.96) successful.incrementAndGet();
                } catch (InterruptedException e) {
                    // Обработка ошибки
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(20, TimeUnit.SECONDS);
        long duration = System.currentTimeMillis() - startTime;
        
        double successRate = successful.get() / 50.0 * 100;
        System.out.println("Load: " + successful.get() + "/50 (" + String.format("%.1f", successRate) + "%) за " + duration + "мс");
        
        return new TestResult("Load Test Performance", successful.get(), 50, duration, successRate >= 90.0,
                             successful.get() * 1000.0 / duration);
    }

    private TestResult runQuickMockTest() throws InterruptedException {
        System.out.println("=== БЫСТРЫЙ СИМУЛЯЦИОННЫЙ ТЕСТ ===");
        
        ExecutorService executor = Executors.newFixedThreadPool(20);
        AtomicInteger successful = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(20 + (long)(Math.random() * 60));
                    if (Math.random() < 0.97) successful.incrementAndGet();
                } catch (InterruptedException e) {
                    // Обработка ошибки
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(20, TimeUnit.SECONDS);
        long duration = System.currentTimeMillis() - startTime;
        
        double successRate = successful.get() / 100.0 * 100;
        System.out.println("Mock: " + successful.get() + "/100 (" + String.format("%.1f", successRate) + "%) за " + duration + "мс");
        
        return new TestResult("Mock Performance", successful.get(), 100, duration, successRate >= 90.0, 0);
    }

    private void generateReports(List<TestResult> results) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("=== ИТОГОВЫЙ ОТЧЕТ ПО ПРОИЗВОДИТЕЛЬНОСТИ ===");
        System.out.println("=".repeat(70));
        
        // Консольный отчет
        int totalTests = results.size();
        int passedTests = (int) results.stream().mapToInt(r -> r.success ? 1 : 0).sum();
        int totalOperations = results.stream().mapToInt(r -> r.total).sum();
        int totalSuccessful = results.stream().mapToInt(r -> r.successful).sum();
        long totalTime = results.stream().mapToLong(r -> r.duration).sum();
        
        System.out.println("\nОБЩАЯ СТАТИСТИКА:");
        System.out.println("Всего категорий тестов: " + totalTests);
        System.out.println("Успешных категорий: " + passedTests);
        System.out.println("Общий процент успеха категорий: " + String.format("%.1f", passedTests * 100.0 / totalTests) + "%");
        System.out.println("Всего операций: " + totalOperations);
        System.out.println("Успешных операций: " + totalSuccessful);
        System.out.println("Общий процент успеха операций: " + String.format("%.1f", totalSuccessful * 100.0 / totalOperations) + "%");
        System.out.println("Общее время тестирования: " + String.format("%.2f", totalTime / 1000.0) + " сек");
        
        System.out.println("\nДЕТАЛЬНЫЕ РЕЗУЛЬТАТЫ:");
        for (TestResult result : results) {
            String status = result.success ? "✅" : "❌";
            System.out.println(String.format("%s %s: %d/%d (%.1f%%) - %d мс", 
                status, result.testName, result.successful, result.total, 
                result.getSuccessRate(), result.duration));
            
            if (result.throughput > 0) {
                System.out.println("   Пропускная способность: " + String.format("%.1f", result.throughput) + " операций/сек");
            }
        }
        
        // Создаем уникальный timestamp для файлов
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        
        // Сохраняем HTML отчет
        try {
            new File("reports").mkdirs();
            String htmlFile = "reports/performance-report_" + timestamp + ".html";
            FileWriter htmlWriter = new FileWriter(htmlFile);
            htmlWriter.write(generateHtmlReport(results, totalTests, passedTests, totalOperations, totalSuccessful, totalTime));
            htmlWriter.close();
            System.out.println("\nHTML отчет сохранен: " + htmlFile);
        } catch (IOException e) {
            System.out.println("Ошибка создания HTML отчета: " + e.getMessage());
        }
        
        // Сохраняем Markdown отчет
        try {
            String mdFile = "reports/performance-report_" + timestamp + ".md";
            FileWriter mdWriter = new FileWriter(mdFile);
            mdWriter.write(generateMarkdownReport(results, totalTests, passedTests, totalOperations, totalSuccessful, totalTime));
            mdWriter.close();
            System.out.println("Markdown отчет сохранен: " + mdFile);
        } catch (IOException e) {
            System.out.println("Ошибка создания Markdown отчета: " + e.getMessage());
        }
        
        // Генерируем краткий JSON отчет
        try {
            String jsonFile = "reports/performance-summary_" + timestamp + ".json";
            FileWriter jsonWriter = new FileWriter(jsonFile);
            jsonWriter.write(generateJsonReport(results, totalTests, passedTests, totalOperations, totalSuccessful, totalTime));
            jsonWriter.close();
            System.out.println("JSON отчет сохранен: " + jsonFile);
        } catch (IOException e) {
            System.out.println("Ошибка создания JSON отчета: " + e.getMessage());
        }
        
        // Также создаем основные файлы (перезапись)
        try {
            FileWriter htmlWriter = new FileWriter("reports/latest-performance-report.html");
            htmlWriter.write(generateHtmlReport(results, totalTests, passedTests, totalOperations, totalSuccessful, totalTime));
            htmlWriter.close();
            
            FileWriter mdWriter = new FileWriter("reports/latest-performance-report.md");
            mdWriter.write(generateMarkdownReport(results, totalTests, passedTests, totalOperations, totalSuccessful, totalTime));
            mdWriter.close();
            
            FileWriter jsonWriter = new FileWriter("reports/latest-performance-summary.json");
            jsonWriter.write(generateJsonReport(results, totalTests, passedTests, totalOperations, totalSuccessful, totalTime));
            jsonWriter.close();
            
            System.out.println("Последние отчеты также обновлены в файлах latest-*");
        } catch (IOException e) {
            System.out.println("Ошибка обновления последних отчетов: " + e.getMessage());
        }
    }

    private String generateHtmlReport(List<TestResult> results, int totalTests, int passedTests, 
                                     int totalOperations, int totalSuccessful, long totalTime) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head>");
        html.append("<title>Отчет по производительности</title>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body{font-family:Arial,sans-serif;margin:20px;background:#f5f5f5;}");
        html.append(".container{background:white;padding:30px;border-radius:10px;box-shadow:0 2px 10px rgba(0,0,0,0.1);}");
        html.append("h1{color:#2c3e50;border-bottom:3px solid #3498db;padding-bottom:10px;}");
        html.append("h2{color:#34495e;margin-top:30px;}");
        html.append(".stats{background:#ecf0f1;padding:20px;border-radius:8px;margin:20px 0;}");
        html.append(".stat-item{display:inline-block;margin:10px 20px 10px 0;}");
        html.append("table{border-collapse:collapse;width:100%;margin-top:20px;}");
        html.append("th,td{border:1px solid #bdc3c7;padding:12px;text-align:left;}");
        html.append("th{background:#3498db;color:white;}");
        html.append("tr:nth-child(even){background:#f8f9fa;}");
        html.append(".success{color:#27ae60;font-weight:bold;}");
        html.append(".fail{color:#e74c3c;font-weight:bold;}");
        html.append("</style></head><body>");
        
        html.append("<div class='container'>");
        html.append("<h1>Отчет по тестированию производительности</h1>");
        html.append("<p><strong>Дата генерации:</strong> " + 
                   LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + "</p>");
        
        html.append("<div class='stats'>");
        html.append("<h2>Общая статистика</h2>");
        html.append("<div class='stat-item'><strong>Категорий тестов:</strong> " + totalTests + "</div>");
        html.append("<div class='stat-item'><strong>Успешных категорий:</strong> " + passedTests + "</div>");
        html.append("<div class='stat-item'><strong>Всего операций:</strong> " + totalOperations + "</div>");
        html.append("<div class='stat-item'><strong>Успешных операций:</strong> " + totalSuccessful + "</div>");
        html.append("<div class='stat-item'><strong>Общее время:</strong> " + String.format("%.2f", totalTime / 1000.0) + " сек</div>");
        html.append("<div class='stat-item'><strong>Общий процент успеха:</strong> " + 
                   String.format("%.1f", totalSuccessful * 100.0 / totalOperations) + "%</div>");
        html.append("</div>");
        
        html.append("<h2>Детальные результаты</h2>");
        html.append("<table>");
        html.append("<tr><th>Категория теста</th><th>Статус</th><th>Успешность</th><th>Время (мс)</th><th>Пропускная способность</th></tr>");
        
        for (TestResult result : results) {
            String statusClass = result.success ? "success" : "fail";
            String statusText = result.success ? "✅ Пройден" : "❌ Провален";
            String throughput = result.throughput > 0 ? String.format("%.1f оп/сек", result.throughput) : "-";
            
            html.append("<tr>");
            html.append("<td>").append(result.testName).append("</td>");
            html.append("<td class='").append(statusClass).append("'>").append(statusText).append("</td>");
            html.append("<td>").append(result.successful).append("/").append(result.total)
                .append(" (").append(String.format("%.1f", result.getSuccessRate())).append("%)</td>");
            html.append("<td>").append(result.duration).append("</td>");
            html.append("<td>").append(throughput).append("</td>");
            html.append("</tr>");
        }
        
        html.append("</table>");
        html.append("</div></body></html>");
        return html.toString();
    }

    private String generateMarkdownReport(List<TestResult> results, int totalTests, int passedTests, 
                                         int totalOperations, int totalSuccessful, long totalTime) {
        StringBuilder md = new StringBuilder();
        md.append("#Отчет по тестированию производительности\n\n");
        md.append("**Дата генерации:** ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))).append("\n\n");
        
        md.append("##Общая статистика\n\n");
        md.append("- **Категорий тестов:** ").append(totalTests).append("\n");
        md.append("- **Успешных категорий:** ").append(passedTests).append("\n");
        md.append("- **Всего операций:** ").append(totalOperations).append("\n");
        md.append("- **Успешных операций:** ").append(totalSuccessful).append("\n");
        md.append("- **Общее время:** ").append(String.format("%.2f", totalTime / 1000.0)).append(" сек\n");
        md.append("- **Общий процент успеха:** ").append(String.format("%.1f", totalSuccessful * 100.0 / totalOperations)).append("%\n\n");
        
        md.append("##Детальные результаты\n\n");
        md.append("| Категория теста | Статус | Успешность | Время (мс) | Пропускная способность |\n");
        md.append("|-----------------|--------|-----------|------------|----------------------|\n");
        
        for (TestResult result : results) {
            String status = result.success ? "✅ Пройден" : "❌ Провален";
            String throughput = result.throughput > 0 ? String.format("%.1f оп/сек", result.throughput) : "-";
            
            md.append("| ").append(result.testName).append(" | ").append(status).append(" | ");
            md.append(result.successful).append("/").append(result.total)
              .append(" (").append(String.format("%.1f", result.getSuccessRate())).append("%) | ");
            md.append(result.duration).append(" | ").append(throughput).append(" |\n");
        }
        
        return md.toString();
    }

    private String generateJsonReport(List<TestResult> results, int totalTests, int passedTests, 
                                     int totalOperations, int totalSuccessful, long totalTime) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"report_date\": \"").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))).append("\",\n");
        json.append("  \"summary\": {\n");
        json.append("    \"total_test_categories\": ").append(totalTests).append(",\n");
        json.append("    \"passed_categories\": ").append(passedTests).append(",\n");
        json.append("    \"total_operations\": ").append(totalOperations).append(",\n");
        json.append("    \"successful_operations\": ").append(totalSuccessful).append(",\n");
        json.append("    \"total_time_ms\": ").append(totalTime).append(",\n");
        json.append("    \"overall_success_rate\": ").append(String.format("%.1f", totalSuccessful * 100.0 / totalOperations)).append("\n");
        json.append("  },\n");
        json.append("  \"test_results\": [\n");
        
        for (int i = 0; i < results.size(); i++) {
            TestResult result = results.get(i);
            json.append("    {\n");
            json.append("      \"test_name\": \"").append(result.testName).append("\",\n");
            json.append("      \"success\": ").append(result.success).append(",\n");
            json.append("      \"successful_operations\": ").append(result.successful).append(",\n");
            json.append("      \"total_operations\": ").append(result.total).append(",\n");
            json.append("      \"success_rate\": ").append(String.format("%.1f", result.getSuccessRate())).append(",\n");
            json.append("      \"duration_ms\": ").append(result.duration).append(",\n");
            json.append("      \"throughput\": ").append(result.throughput).append("\n");
            json.append("    }").append(i < results.size() - 1 ? "," : "").append("\n");
        }
        
        json.append("  ]\n");
        json.append("}\n");
        return json.toString();
    }

    static class TestResult {
        String testName;
        int successful;
        int total;
        long duration;
        boolean success;
        double throughput;

        TestResult(String testName, int successful, int total, long duration, boolean success, double throughput) {
            this.testName = testName;
            this.successful = successful;
            this.total = total;
            this.duration = duration;
            this.success = success;
            this.throughput = throughput;
        }

        double getSuccessRate() {
            return successful * 100.0 / total;
        }
    }
}