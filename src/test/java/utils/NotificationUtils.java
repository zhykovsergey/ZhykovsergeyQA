package utils;

import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Утилиты для отправки уведомлений
 */
public class NotificationUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationUtils.class);
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    // ==================== SLACK УВЕДОМЛЕНИЯ ====================
    
    /**
     * Отправляет уведомление в Slack
     */
    public static void sendSlackNotification(String webhookUrl, String message, String channel, String username) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("text", message);
            payload.put("channel", channel);
            payload.put("username", username);
            payload.put("icon_emoji", ":robot_face:");
            
            String jsonPayload = convertToJson(payload);
            
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();
            
            CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            
            response.thenAccept(r -> {
                if (r.statusCode() == 200) {
                    logger.info("Slack notification sent successfully");
                    Allure.addAttachment("Slack Notification", "text/plain", 
                        "Message: " + message + "\nStatus: Success");
                } else {
                    logger.error("Failed to send Slack notification: " + r.statusCode());
                    Allure.addAttachment("Slack Notification Error", "text/plain", 
                        "Message: " + message + "\nStatus: Failed\nResponse: " + r.body());
                }
            });
            
        } catch (Exception e) {
            logger.error("Error sending Slack notification", e);
            Allure.addAttachment("Slack Notification Exception", "text/plain", 
                "Message: " + message + "\nError: " + e.getMessage());
        }
    }
    
    /**
     * Отправляет уведомление о результате теста в Slack
     */
    public static void sendTestResultNotification(String webhookUrl, String testName, boolean success, String details) {
        String status = success ? "✅ PASSED" : "❌ FAILED";
        String message = String.format(
            "Test Result: %s\nTest: %s\nStatus: %s\nDetails: %s\nTime: %s",
            status, testName, success ? "SUCCESS" : "FAILURE", details, getCurrentTimestamp()
        );
        
        sendSlackNotification(webhookUrl, message, "#test-automation", "TestBot");
    }
    
    // ==================== EMAIL УВЕДОМЛЕНИЯ ====================
    
    /**
     * Отправляет email уведомление
     */
    public static void sendEmailNotification(String smtpHost, int smtpPort, String username, String password,
                                           String from, String to, String subject, String body) {
        try {
            // Здесь можно интегрировать с JavaMail API
            logger.info("Email notification would be sent to: " + to);
            logger.info("Subject: " + subject);
            logger.info("Body: " + body);
            
            Allure.addAttachment("Email Notification", "text/plain", 
                String.format("To: %s\nSubject: %s\nBody: %s", to, subject, body));
            
        } catch (Exception e) {
            logger.error("Error sending email notification", e);
            Allure.addAttachment("Email Notification Error", "text/plain", 
                "Error: " + e.getMessage());
        }
    }
    
    /**
     * Отправляет уведомление о результате теста по email
     */
    public static void sendTestResultEmail(String smtpHost, int smtpPort, String username, String password,
                                         String from, String to, String testName, boolean success, String details) {
        String subject = String.format("Test Result: %s - %s", testName, success ? "PASSED" : "FAILED");
        String body = String.format(
            "Test Name: %s\nStatus: %s\nDetails: %s\nTime: %s\n\nThis is an automated notification from the test automation system.",
            testName, success ? "SUCCESS" : "FAILURE", details, getCurrentTimestamp()
        );
        
        sendEmailNotification(smtpHost, smtpPort, username, password, from, to, subject, body);
    }
    
    // ==================== TELEGRAM УВЕДОМЛЕНИЯ ====================
    
    /**
     * Отправляет уведомление в Telegram
     */
    public static void sendTelegramNotification(String botToken, String chatId, String message) {
        try {
            String url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("chat_id", chatId);
            payload.put("text", message);
            payload.put("parse_mode", "HTML");
            
            String jsonPayload = convertToJson(payload);
            
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();
            
            CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            
            response.thenAccept(r -> {
                if (r.statusCode() == 200) {
                    logger.info("Telegram notification sent successfully");
                    Allure.addAttachment("Telegram Notification", "text/plain", 
                        "Message: " + message + "\nStatus: Success");
                } else {
                    logger.error("Failed to send Telegram notification: " + r.statusCode());
                    Allure.addAttachment("Telegram Notification Error", "text/plain", 
                        "Message: " + message + "\nStatus: Failed\nResponse: " + r.body());
                }
            });
            
        } catch (Exception e) {
            logger.error("Error sending Telegram notification", e);
            Allure.addAttachment("Telegram Notification Exception", "text/plain", 
                "Message: " + message + "\nError: " + e.getMessage());
        }
    }
    
    /**
     * Отправляет уведомление о результате теста в Telegram
     */
    public static void sendTestResultTelegram(String botToken, String chatId, String testName, boolean success, String details) {
        String status = success ? "✅ PASSED" : "❌ FAILED";
        String message = String.format(
            "<b>Test Result</b>\n" +
            "Test: <code>%s</code>\n" +
            "Status: %s\n" +
            "Details: %s\n" +
            "Time: %s",
            testName, status, details, getCurrentTimestamp()
        );
        
        sendTelegramNotification(botToken, chatId, message);
    }
    
    // ==================== WEBHOOK УВЕДОМЛЕНИЯ ====================
    
    /**
     * Отправляет webhook уведомление
     */
    public static void sendWebhookNotification(String webhookUrl, Map<String, Object> payload) {
        try {
            String jsonPayload = convertToJson(payload);
            
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();
            
            CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            
            response.thenAccept(r -> {
                if (r.statusCode() >= 200 && r.statusCode() < 300) {
                    logger.info("Webhook notification sent successfully");
                    Allure.addAttachment("Webhook Notification", "text/plain", 
                        "Payload: " + jsonPayload + "\nStatus: Success");
                } else {
                    logger.error("Failed to send webhook notification: " + r.statusCode());
                    Allure.addAttachment("Webhook Notification Error", "text/plain", 
                        "Payload: " + jsonPayload + "\nStatus: Failed\nResponse: " + r.body());
                }
            });
            
        } catch (Exception e) {
            logger.error("Error sending webhook notification", e);
            Allure.addAttachment("Webhook Notification Exception", "text/plain", 
                "Payload: " + payload + "\nError: " + e.getMessage());
        }
    }
    
    // ==================== УВЕДОМЛЕНИЯ О ПРОИЗВОДИТЕЛЬНОСТИ ====================
    
    /**
     * Отправляет уведомление о производительности
     */
    public static void sendPerformanceNotification(String webhookUrl, String testName, long duration, 
                                                 Map<String, Object> metrics) {
        String message = String.format(
            "Performance Alert\nTest: %s\nDuration: %d ms\nMetrics: %s\nTime: %s",
            testName, duration, metrics.toString(), getCurrentTimestamp()
        );
        
        sendSlackNotification(webhookUrl, message, "#performance", "PerformanceBot");
    }
    
    // ==================== УВЕДОМЛЕНИЯ О ОШИБКАХ ====================
    
    /**
     * Отправляет уведомление об ошибке
     */
    public static void sendErrorNotification(String webhookUrl, String testName, String errorMessage, 
                                           String stackTrace) {
        String message = String.format(
            "Error Alert\nTest: %s\nError: %s\nStack Trace: %s\nTime: %s",
            testName, errorMessage, stackTrace, getCurrentTimestamp()
        );
        
        sendSlackNotification(webhookUrl, message, "#errors", "ErrorBot");
    }
    
    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================
    
    /**
     * Конвертирует объект в JSON строку
     */
    private static String convertToJson(Map<String, Object> payload) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            if (!first) {
                json.append(",");
            }
            json.append("\"").append(entry.getKey()).append("\":");
            
            Object value = entry.getValue();
            if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else {
                json.append(value);
            }
            
            first = false;
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Получает текущий timestamp
     */
    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }
    
    // ==================== КОНФИГУРАЦИЯ ====================
    
    /**
     * Получает webhook URL из системных свойств
     */
    public static String getSlackWebhookUrl() {
        return System.getProperty("slack.webhook.url", "");
    }
    
    /**
     * Получает Telegram bot token из системных свойств
     */
    public static String getTelegramBotToken() {
        return System.getProperty("telegram.bot.token", "");
    }
    
    /**
     * Получает Telegram chat ID из системных свойств
     */
    public static String getTelegramChatId() {
        return System.getProperty("telegram.chat.id", "");
    }
    
    /**
     * Проверяет, включены ли уведомления
     */
    public static boolean isNotificationsEnabled() {
        return Boolean.parseBoolean(System.getProperty("notifications.enabled", "false"));
    }
}
