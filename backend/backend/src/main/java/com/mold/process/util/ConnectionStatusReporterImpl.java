package com.mold.process.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 连接鐘舵€佹姤鍛婂櫒实现绫? */
public class ConnectionStatusReporterImpl implements ConnectionStatusReporter {
    private List<String> reportEntries;
    private LocalDateTime startTime;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    public ConnectionStatusReporterImpl() {
        this.reportEntries = new ArrayList<>();
        this.startTime = LocalDateTime.now();
        addEntry("RabbitMQ连接验证鎶ュ憡鍚姩");
    }
    
    @Override
    public void reportSuccess(ConnectionStatus status) {
        addEntry("鉁?连接成功: " + status.getConnectionDetails());
    }
    
    @Override
    public void reportFailure(ConnectionStatus status) {
        addEntry("鉁?连接失败: " + status.getConnectionDetails());
        if (status.getLastError() != null) {
            addEntry("   错误璇︽儏: " + status.getLastError().getMessage());
        }
    }
    
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("==========================================\n");
        report.append("RABBITMQ连接验证鎶ュ憡\n");
        report.append("==========================================\n");
        report.append("开€濮嬫椂闂? " + startTime.format(FORMATTER) + "\n");
        report.append("缁撴潫鏃堕棿: " + LocalDateTime.now().format(FORMATTER) + "\n");
        report.append("==========================================\n");
        
        for (String entry : reportEntries) {
            report.append(entry + "\n");
        }
        
        report.append("==========================================\n");
        
        return report.toString();
    }
    
    /**
     * 添加鎶ュ憡鏉＄洰
     * @param entry 鎶ュ憡内容
     */
    public void addEntry(String entry) {
        String timestamp = getTimestamp();
        reportEntries.add("[" + timestamp + "] " + entry);
    }
    
    @Override
    /**
     * 记录连接测试鐨勫悇涓樁娈?
     * @param phaseName 测试闃舵鍚嶇О
     */
    public void reportPhase(String phaseName) {
        addEntry("馃攧 " + phaseName);
        System.out.println("[Phase] " + phaseName);
    }
    
    @Override
    /**
     * 记录璇婃柇淇℃伅
     * @param diagnostic 璇婃柇淇℃伅
     */
    public void reportDiagnostic(String diagnostic) {
        addEntry("鈩癸笍 " + diagnostic);
        System.out.println("[Diagnostic] " + diagnostic);
    }
    
    /**
     * 获取褰撳墠鏃堕棿鎴?
     * @return 鏍煎紡鍖栫殑鏃堕棿鎴冲瓧绗︿覆
     */
    private String getTimestamp() {
        return LocalDateTime.now().format(FORMATTER);
    }
}
