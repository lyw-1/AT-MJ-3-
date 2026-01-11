package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.MoldProcess;
import com.mold.digitalization.entity.ProcessStatusHistory;
import com.mold.digitalization.entity.ProcessException;
import com.mold.digitalization.entity.InspectionResult;
import java.util.List;

/**
 * 妯″叿鍔犲伐鐘舵€佹満核心服务接口
 * 瀹氫箟鐘舵€佽浆鎹€佹祦绋嬫帶鍒躲€佸紓甯稿鐞嗙瓑核心涓氬姟逻辑
 */
public interface ProcessStateMachineService extends IService<MoldProcess> {
    
    /**
     * 瑙﹀彂鐘舵€佽浆鎹?
     * @param processId 娴佺▼ID
     * @param event 瑙﹀彂浜嬩欢
     * @param operatorId 操作浜哄憳ID
     * @return 鏄惁杞崲成功
     */
    boolean triggerStateTransition(Long processId, String event, Long operatorId);
    
    /**
     * 瑙﹀彂鐘舵€佽浆鎹紙甯﹀弬鏁帮級
     * @param processId 娴佺▼ID
     * @param event 瑙﹀彂浜嬩欢
     * @param operatorId 操作浜哄憳ID
     * @param parameters 杞崲鍙傛暟
     * @return 鏄惁杞崲成功
     */
    boolean triggerStateTransition(Long processId, String event, Long operatorId, Object parameters);
    
    /**
     * 验证鐘舵€佽浆鎹㈡潯浠?
     * @param processId 娴佺▼ID
     * @param event 瑙﹀彂浜嬩欢
     * @return 验证结果
     */
    boolean validateTransitionConditions(Long processId, String event);
    
    /**
     * 获取褰撳墠鐘舵€佸厑璁哥殑浜嬩欢鍒楄〃
     * @param processId 娴佺▼ID
     * @return 鍏佽鐨勪簨浠跺垪琛?
     */
    List<String> getAllowedEvents(Long processId);
    
    /**
     * 获取鐘舵€佽浆鎹㈠巻鍙?
     * @param processId 娴佺▼ID
     * @return 鐘舵€佸巻鍙茶褰曞垪琛?
     */
    List<ProcessStatusHistory> getStateTransitionHistory(Long processId);
    
    /**
     * 记录鐘舵€佽浆鎹㈠巻鍙?
     * @param processId 娴佺▼ID
     * @param fromState 鍘熺姸鎬?
     * @param toState 鐩爣状态
     * @param event 瑙﹀彂浜嬩欢
     * @param operatorId 操作浜哄憳ID
     * @param description 鎻忚堪淇℃伅
     * @return 鏄惁记录成功
     */
    boolean recordStateTransition(Long processId, Integer fromState, Integer toState, 
                                 String event, Long operatorId, String description);
    
    /**
     * 澶勭悊娴佺▼开傚父
     * @param processId 娴佺▼ID
     * @param exceptionCode 开傚父浠ｇ爜
     * @param exceptionMessage 开傚父淇℃伅
     * @param operatorId 操作浜哄憳ID
     * @return 鏄惁澶勭悊成功
     */
    boolean handleProcessException(Long processId, String exceptionCode, 
                                  String exceptionMessage, Long operatorId);
    
    /**
     * 获取娴佺▼开傚父记录
     * @param processId 娴佺▼ID
     * @return 开傚父记录鍒楄〃
     */
    List<ProcessException> getProcessExceptions(Long processId);
    
    /**
     * 记录璐ㄩ噺妫€楠岀粨鏋?
     * @param processId 娴佺▼ID
     * @param inspectionResult 妫€楠岀粨鏋?
     * @param inspectorId 妫€楠屼汉鍛業D
     * @return 鏄惁记录成功
     */
    boolean recordInspectionResult(Long processId, InspectionResult inspectionResult, Long inspectorId);
    
    /**
     * 获取璐ㄩ噺妫€楠岀粨鏋?
     * @param processId 娴佺▼ID
     * @return 妫€楠岀粨鏋滃垪琛?
     */
    List<InspectionResult> getInspectionResults(Long processId);
    
    /**
     * 妫€鏌ユ祦绋嬫槸鍚﹀彲开€濮?
     * @param processId 娴佺▼ID
     * @return 鏄惁鍙紑濮?
     */
    boolean canStartProcess(Long processId);
    
    /**
     * 妫€鏌ユ祦绋嬫槸鍚﹀彲鏆傚仠
     * @param processId 娴佺▼ID
     * @return 鏄惁鍙殏鍋?
     */
    boolean canPauseProcess(Long processId);
    
    /**
     * 妫€鏌ユ祦绋嬫槸鍚﹀彲鎭㈠
     * @param processId 娴佺▼ID
     * @return 鏄惁鍙仮澶?
     */
    boolean canResumeProcess(Long processId);
    
    /**
     * 妫€鏌ユ祦绋嬫槸鍚﹀彲瀹屾垚
     * @param processId 娴佺▼ID
     * @return 鏄惁鍙畬鎴?
     */
    boolean canCompleteProcess(Long processId);
    
    /**
     * 妫€鏌ユ祦绋嬫槸鍚﹀彲鍙栨秷
     * @param processId 娴佺▼ID
     * @return 鏄惁鍙彇娑?
     */
    boolean canCancelProcess(Long processId);
    
    /**
     * 获取娴佺▼褰撳墠鐘舵€佷俊鎭?
     * @param processId 娴佺▼ID
     * @return 鐘舵€佷俊鎭?
     */
    ProcessStatusInfo getCurrentProcessStatus(Long processId);
    
    /**
     * 鐘舵€佷俊鎭被
     */
    class ProcessStatusInfo {
        private Integer currentState;
        private String stateName;
        private String stateDescription;
        private List<String> allowedEvents;
        private Long durationInCurrentState; // 鍦ㄥ綋鍓嶇姸鎬佺殑鎸佺画鏃堕棿锛堟绉掞級
        
        public ProcessStatusInfo() {}
        
        public ProcessStatusInfo(Integer currentState, String stateName, String stateDescription, 
                               List<String> allowedEvents, Long durationInCurrentState) {
            this.currentState = currentState;
            this.stateName = stateName;
            this.stateDescription = stateDescription;
            this.allowedEvents = allowedEvents;
            this.durationInCurrentState = durationInCurrentState;
        }
        
        // getter鍜宻etter方法
        public Integer getCurrentState() { return currentState; }
        public void setCurrentState(Integer currentState) { this.currentState = currentState; }
        
        public String getStateName() { return stateName; }
        public void setStateName(String stateName) { this.stateName = stateName; }
        
        public String getStateDescription() { return stateDescription; }
        public void setStateDescription(String stateDescription) { this.stateDescription = stateDescription; }
        
        public List<String> getAllowedEvents() { return allowedEvents; }
        public void setAllowedEvents(List<String> allowedEvents) { this.allowedEvents = allowedEvents; }
        
        public Long getDurationInCurrentState() { return durationInCurrentState; }
        public void setDurationInCurrentState(Long durationInCurrentState) { this.durationInCurrentState = durationInCurrentState; }
    }
}
