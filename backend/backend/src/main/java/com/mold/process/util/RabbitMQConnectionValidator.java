package com.mold.process.util;

import com.mold.process.config.RabbitMQConfig;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * RabbitMQ连接验证鍣? * 鏁村悎鎵€鏈夐獙璇佺粍浠讹紝鎻愪緵瀹屾暣鐨勮繛鎺ユ祴璇曟祦绋? */
public class RabbitMQConnectionValidator {
    private static final String DEFAULT_REPORT_DIR = "d:/Trae/AT-MJ-1/docs/rabbitmq_validation/reports";
    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    public static void main(String[] args) {
    System.out.println("==========================================");
    System.out.println("RABBITMQ connection validator");
    System.out.println("==========================================");
    System.out.println("Starting lightweight RabbitMQ connectivity checks...");

        // 创建鐘舵€佹姤鍛婂櫒
        ConnectionStatusReporter reporter = new ConnectionStatusReporterImpl();
    reporter.reportPhase("Phase 1: Basic connectivity checks");
        
        try {
            // 1. 妫€鏌ユ湇鍔″櫒鏄惁可达
            reporter.reportPhase("Step 1: Check if server is reachable");
            boolean reachable = RabbitMQConnectionUtil.isServerReachable(RabbitMQConfig.getStaticHost(), RabbitMQConfig.getStaticPort());

            if (!reachable) {
                ConnectionStatus status = new ConnectionStatusImpl("localhost", 5672, "guest");
                status.setLastError(new Exception("RabbitMQ server unreachable on default host/port"));
                reporter.reportFailure(status);
                generateAndSaveReport(reporter);
                System.out.println("\n验证失败: RabbitMQ服务鍣ㄤ笉可达");
                System.exit(1);
            }
            reporter.reportDiagnostic("Server appears reachable and ping responded");

            // 2. 测试鍩烘湰连接
            reporter.reportPhase("Step 2: Test basic connection");
            ConnectionStatus status = RabbitMQConnectionUtil.testConnection();

            if (status.isConnected()) {
                reporter.reportSuccess(status);

                // 3. send/receive test
                reporter.reportPhase("Step 3: Send/receive test");
                boolean messageSent = RabbitMQConnectionUtil.sendTestMessage(
                    status,
                    "RabbitMQ connection validator test message - " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                );

                if (messageSent) {
                    reporter.reportDiagnostic("Test message sent successfully");
                } else {
                    reporter.reportDiagnostic("Test message send failed, connectivity or permissions may be limited");
                }

            } else {
                reporter.reportFailure(status);
                generateAndSaveReport(reporter);
                System.out.println("\n验证失败: 鏃犳硶寤虹珛RabbitMQ连接");
                System.exit(1);
            }

            // 4. 鐢熸垚骞朵繚瀛樻姤鍛?            reporter.reportPhase("闃舵4: 鐢熸垚验证鎶ュ憡");
            String reportPath = generateAndSaveReport(reporter);

            System.out.println("\n==========================================");
            System.out.println("验证瀹屾垚");
            System.out.println("验证结果: 成功");
            System.out.println("鎶ュ憡宸蹭繚瀛樿嚦: " + reportPath);
            System.out.println("==========================================");

            System.exit(0);

        } catch (Exception e) {
            reporter.reportDiagnostic("Validator encountered an unexpected error: " + e.getMessage());
            e.printStackTrace();
            generateAndSaveReport(reporter);

            System.out.println("\nValidation failed: unexpected error occurred");
            System.exit(1);
        }
    }
    
    /**
     * 鐢熸垚骞朵繚瀛橀獙璇佹姤鍛?     *
     * @param reporter 验证鎶ュ憡鍣?     * @return 鎶ュ憡鏂囦欢璺緞
     */
    private static String generateAndSaveReport(ConnectionStatusReporter reporter) {
        try {
            // 纭繚鎶ュ憡鐩綍瀛樺湪
            Path reportDir = Paths.get(DEFAULT_REPORT_DIR);
            if (!Files.exists(reportDir)) {
                Files.createDirectories(reportDir);
            }
            
            String reportContent = reporter.generateReport();

            // create report file name and path
            String fileName = "rabbitmq_validation_" + LocalDateTime.now().format(FILE_FORMATTER) + ".txt";
            String filePath = Paths.get(DEFAULT_REPORT_DIR, fileName).toString();

            // Save to file
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(reportContent);
            }

            System.out.println("Report generated: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            System.err.println("淇濆瓨鎶ュ憡失败: " + e.getMessage());
            // 濡傛灉淇濆瓨失败锛岃嚦灏戞墦鍗版姤鍛婂唴瀹?            System.out.println("\n鎶ュ憡内容:");
            System.out.println(reporter.generateReport());
            return "(鎶ュ憡鐢熸垚失败锛屽凡鎵撳嵃鍒版帶鍒跺彴)";
        }
    }
    
    /**
     * 执行绠€鍖栫増鐨勮繛鎺ラ獙璇?
     * @return 验证鏄惁成功
     */
    public static boolean validateConnection() {
        ConnectionStatusReporter reporter = new ConnectionStatusReporterImpl();
        
        // 妫€鏌ユ湇鍔″櫒可达鎬?
        boolean reachable = RabbitMQConnectionUtil.isServerReachable(RabbitMQConfig.getStaticHost(), RabbitMQConfig.getStaticPort());
        if (!reachable) {
            return false;
        }
        
        // 测试连接
        ConnectionStatus status = RabbitMQConnectionUtil.testConnection();
        return status.isConnected();
    }
}
