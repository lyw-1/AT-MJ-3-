package com.mold.process.util;

/**
 * 连接鐘舵€佹姤鍛婂櫒接口
 * 璐熻矗澶勭悊连接鐘舵€佺殑鎶ュ憡鐢熸垚
 */
public interface ConnectionStatusReporter {
    /**
     * 鎶ュ憡连接成功
     * @param status 连接鐘舵€佸璞?     */
    void reportSuccess(ConnectionStatus status);
    
    /**
     * 鎶ュ憡连接失败
     * @param status 连接鐘舵€佸璞?     */
    void reportFailure(ConnectionStatus status);
    
    /**
     * 鐢熸垚鏈€缁堟姤鍛?     * @return 鎶ュ憡内容瀛楃涓?     */
    String generateReport();
    
    /**
     * 鎶ュ憡验证闃舵
     * @param phaseName 闃舵鍚嶇О
     */
    void reportPhase(String phaseName);
    
    /**
     * 鎶ュ憡璇婃柇淇℃伅
     * @param diagnostic 璇婃柇淇℃伅
     */
    void reportDiagnostic(String diagnostic);
}
