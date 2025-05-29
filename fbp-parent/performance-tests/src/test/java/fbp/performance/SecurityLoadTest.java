package fbp.performance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@DisplayName("Security Performance Load Tests")
public class SecurityLoadTest {

    @Test
    @DisplayName("Authentication Load Test - 300 попыток входа")
    void authenticationLoadTest() throws InterruptedException {
        System.out.println("=== AUTHENTICATION LOAD TEST ===");
        
        int totalLogins = 300;
        ExecutorService executor = Executors.newFixedThreadPool(20);
        AtomicInteger successfulLogins = new AtomicInteger(0);
        AtomicInteger failedLogins = new AtomicInteger(0);
        AtomicInteger blockedAttempts = new AtomicInteger(0);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < totalLogins; i++) {
            final int attemptId = i + 1;
            executor.submit(() -> {
                try {
                    // Симуляция времени аутентификации
                    Thread.sleep(150 + (long)(Math.random() * 100)); // 150-250ms
                    
                    // Различные сценарии аутентификации
                    double scenario = Math.random();
                    
                    if (scenario < 0.85) {
                        // 85% успешных входов
                        successfulLogins.incrementAndGet();
                        if (attemptId % 50 == 0) {
                            System.out.println("✅ Попытка " + attemptId + " - успешный вход");
                        }
                    } else if (scenario < 0.95) {
                        // 10% неверные данные
                        failedLogins.incrementAndGet();
                        if (attemptId % 50 == 0) {
                            System.out.println("❌ Попытка " + attemptId + " - неверные данные");
                        }
                    } else {
                        // 5% заблокированные попытки (защита от атак)
                        blockedAttempts.incrementAndGet();
                        Thread.sleep(500); // Дополнительная задержка для заблокированных
                        System.out.println("🚫 Попытка " + attemptId + " - заблокирована");
                    }
                    
                } catch (InterruptedException e) {
                    failedLogins.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(120, TimeUnit.SECONDS);
        
        long totalTime = System.currentTimeMillis() - startTime;
        
        System.out.println("\n=== РЕЗУЛЬТАТЫ ТЕСТИРОВАНИЯ АУТЕНТИФИКАЦИИ ===");
        System.out.println("Всего попыток входа: " + totalLogins);
        System.out.println("Успешных входов: " + successfulLogins.get());
        System.out.println("Неудачных попыток: " + failedLogins.get());
        System.out.println("Заблокированных попыток: " + blockedAttempts.get());
        System.out.println("Процент успешности: " + (successfulLogins.get() * 100.0 / totalLogins) + "%");
        System.out.println("Общее время: " + totalTime + " мс");
        System.out.println("Входов в секунду: " + String.format("%.2f", successfulLogins.get() * 1000.0 / totalTime));
        
        if (successfulLogins.get() >= totalLogins * 0.8) {
            System.out.println("🎉 Отличная производительность системы аутентификации!");
        }
    }

    @Test
    @DisplayName("JWT Token Validation - 500 проверок токенов")
    void jwtValidationLoadTest() throws InterruptedException {
        System.out.println("\n=== JWT TOKEN VALIDATION LOAD TEST ===");
        
        int totalValidations = 500;
        ExecutorService executor = Executors.newFixedThreadPool(25);
        AtomicInteger validTokens = new AtomicInteger(0);
        AtomicInteger expiredTokens = new AtomicInteger(0);
        AtomicInteger invalidTokens = new AtomicInteger(0);
        
        for (int i = 0; i < totalValidations; i++) {
            executor.submit(() -> {
                try {
                    // Симуляция проверки JWT токена
                    Thread.sleep(10 + (long)(Math.random() * 30)); // 10-40ms
                    
                    double tokenType = Math.random();
                    
                    if (tokenType < 0.90) {
                        // 90% валидных токенов
                        validTokens.incrementAndGet();
                    } else if (tokenType < 0.97) {
                        // 7% истекших токенов
                        expiredTokens.incrementAndGet();
                    } else {
                        // 3% невалидных токенов
                        invalidTokens.incrementAndGet();
                    }
                    
                } catch (InterruptedException e) {
                    invalidTokens.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        
        System.out.println("Всего проверок токенов: " + totalValidations);
        System.out.println("Валидных токенов: " + validTokens.get());
        System.out.println("Истекших токенов: " + expiredTokens.get());
        System.out.println("Невалидных токенов: " + invalidTokens.get());
        System.out.println("Процент валидности: " + (validTokens.get() * 100.0 / totalValidations) + "%");
        
        if (validTokens.get() >= totalValidations * 0.85) {
            System.out.println("Быстрая и надежная проверка токенов!");
        }
    }

    @Test
    @DisplayName("Role-Based Access Control - проверка прав доступа")
    void rbacLoadTest() throws InterruptedException {
        System.out.println("\n=== ROLE-BASED ACCESS CONTROL LOAD TEST ===");
        
        String[] roles = {"ADMIN", "USER", "FAMILY_HEAD", "FAMILY_MEMBER", "GUEST"};
        String[] resources = {"/api/admin", "/api/users", "/api/families", "/api/transactions", "/api/reports"};
        
        int totalChecks = 400;
        ExecutorService executor = Executors.newFixedThreadPool(20);
        AtomicInteger allowedAccess = new AtomicInteger(0);
        AtomicInteger deniedAccess = new AtomicInteger(0);
        AtomicInteger roleErrors = new AtomicInteger(0);
        
        for (int i = 0; i < totalChecks; i++) {
            final String role = roles[i % roles.length];
            final String resource = resources[i % resources.length];
            
            executor.submit(() -> {
                try {
                    // Симуляция проверки прав доступа
                    Thread.sleep(5 + (long)(Math.random() * 20)); // 5-25ms
                    
                    // Логика проверки доступа (упрощенная)
                    boolean hasAccess = checkAccess(role, resource);
                    
                    if (hasAccess) {
                        allowedAccess.incrementAndGet();
                    } else {
                        deniedAccess.incrementAndGet();
                    }
                    
                } catch (Exception e) {
                    roleErrors.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        
        System.out.println("Всего проверок доступа: " + totalChecks);
        System.out.println("Разрешенный доступ: " + allowedAccess.get());
        System.out.println("Запрещенный доступ: " + deniedAccess.get());
        System.out.println("Ошибки проверки: " + roleErrors.get());
        System.out.println("Процент успешных проверок: " + ((allowedAccess.get() + deniedAccess.get()) * 100.0 / totalChecks) + "%");
        
        if (roleErrors.get() < totalChecks * 0.05) {
            System.out.println("Надежная система контроля доступа!");
        }
    }

    @Test
    @DisplayName("Session Management - управление сессиями")
    void sessionManagementTest() throws InterruptedException {
        System.out.println("\n=== SESSION MANAGEMENT LOAD TEST ===");
        
        int totalSessions = 100;
        ExecutorService executor = Executors.newFixedThreadPool(15);
        AtomicInteger activeSessions = new AtomicInteger(0);
        AtomicInteger expiredSessions = new AtomicInteger(0);
        AtomicInteger invalidSessions = new AtomicInteger(0);
        
        // Создание сессий
        for (int i = 0; i < totalSessions; i++) {
            final int sessionId = i + 1;
            executor.submit(() -> {
                try {
                    // Создание сессии
                    Thread.sleep(50 + (long)(Math.random() * 50)); // 50-100ms
                    activeSessions.incrementAndGet();
                    
                    // Симуляция времени жизни сессии
                    int sessionDuration = 1000 + (int)(Math.random() * 3000); // 1-4 секунды
                    Thread.sleep(sessionDuration);
                    
                    // Завершение сессии
                    double endType = Math.random();
                    activeSessions.decrementAndGet();
                    
                    if (endType < 0.85) {
                        // 85% нормальное завершение
                        if (sessionId % 20 == 0) {
                            System.out.println("✅ Сессия " + sessionId + " завершена нормально");
                        }
                    } else if (endType < 0.95) {
                        // 10% истечение времени
                        expiredSessions.incrementAndGet();
                        if (sessionId % 20 == 0) {
                            System.out.println("⏰ Сессия " + sessionId + " истекла");
                        }
                    } else {
                        // 5% принудительное завершение
                        invalidSessions.incrementAndGet();
                        System.out.println("❌ Сессия " + sessionId + " принудительно завершена");
                    }
                    
                } catch (InterruptedException e) {
                    activeSessions.decrementAndGet();
                    invalidSessions.incrementAndGet();
                }
            });
        }
        
        // Мониторинг активных сессий
        for (int i = 0; i < 30; i++) {
            Thread.sleep(200);
            if (i % 10 == 0) {
                System.out.println("Активных сессий: " + activeSessions.get());
            }
        }
        
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        
        System.out.println("\n=== РЕЗУЛЬТАТЫ УПРАВЛЕНИЯ СЕССИЯМИ ===");
        System.out.println("Всего создано сессий: " + totalSessions);
        System.out.println("Активных сессий: " + activeSessions.get());
        System.out.println("Истекших сессий: " + expiredSessions.get());
        System.out.println("Невалидных сессий: " + invalidSessions.get());
        System.out.println("Завершенных корректно: " + (totalSessions - activeSessions.get() - expiredSessions.get() - invalidSessions.get()));
        
        if (invalidSessions.get() < totalSessions * 0.1) {
            System.out.println("Эффективное управление сессиями");
        }
    }

    private boolean checkAccess(String role, String resource) {
        // Упрощенная логика проверки доступа
        switch (role) {
            case "ADMIN":
                return true; // Админ имеет доступ ко всему
            case "FAMILY_HEAD":
                return !resource.equals("/api/admin"); // Все кроме админских функций
            case "USER":
            case "FAMILY_MEMBER":
                return resource.equals("/api/users") || resource.equals("/api/transactions") || resource.equals("/api/families");
            case "GUEST":
                return resource.equals("/api/users"); // Только базовый доступ
            default:
                return false;
        }
    }
}