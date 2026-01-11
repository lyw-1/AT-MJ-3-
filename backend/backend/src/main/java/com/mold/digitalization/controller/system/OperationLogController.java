package com.mold.digitalization.controller.system;

import com.mold.digitalization.dto.ResponseDTO;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.service.system.OperationLogService;
import com.mold.digitalization.common.exception.BusinessException;
import com.mold.digitalization.enums.ErrorCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 操作鏃ュ織控制鍣?
 */
@RestController
@RequestMapping("/v1/api/log/operation")
@Api(tags = "操作日志管理")
@RequiredArgsConstructor
public class OperationLogController {

    private final OperationLogService operationLogService;

    @ApiOperation(value = "分页查询操作日志", notes = "支持筛选：用户名、操作类型、开始时间、结束时间")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "query", dataType = "int", defaultValue = "1"),
        @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, paramType = "query", dataType = "int", defaultValue = "10"),
        @ApiImplicitParam(name = "username", value = "用户名", required = false, paramType = "query", dataType = "string"),
        @ApiImplicitParam(name = "operationType", value = "操作类型", required = false, paramType = "query", dataType = "string"),
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, paramType = "query", dataType = "string", example = "2023-01-01 00:00:00"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, paramType = "query", dataType = "string", example = "2023-01-31 23:59:59")
    })
    @GetMapping("/page")
    public ResponseDTO<?> queryPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam Map<String, Object> params) {
        return ResponseDTO.success(operationLogService.queryPage(pageNum, pageSize, params));
    }

    @ApiOperation(value = "根据ID获取操作日志", notes = "返回单条操作日志详情")
    @ApiImplicitParam(name = "id", value = "日志ID", required = true, paramType = "path", dataType = "long")
    @GetMapping("/{id}")
    public ResponseDTO<?> getById(@PathVariable Long id) {
        OperationLog log = operationLogService.getById(id);
        if (log == null) {
            // 统一异常风格：未找到资源抛出业务异常
            throw new BusinessException(ErrorCodeEnum.DATA_NOT_FOUND);
        }
        return ResponseDTO.success(log);
    }

    @ApiOperation(value = "批量删除操作日志", notes = "通过ID数组删除操作日志")
    @ApiImplicitParam(name = "ids", value = "日志ID数组", required = true, paramType = "body", dataType = "Long[]", example = "[1,2,3]")
    @DeleteMapping("/batch")
    public ResponseDTO<?> deleteBatch(@RequestBody Long[] ids) {
        boolean result = operationLogService.deleteBatch(ids);
        if (result) {
            return ResponseDTO.success(null, "delete success");
        }
        throw new BusinessException(ErrorCodeEnum.OPERATION_FAILED);
    }
    @ApiOperation(value = "清理操作日志", notes = "按照配置策略清理操作日志")
    @DeleteMapping("/clean")
    public ResponseDTO<?> clean() {
        boolean result = operationLogService.cleanOperationLogs();
        if (result) {
            return ResponseDTO.success(null, "clean success");
        }
        throw new BusinessException(ErrorCodeEnum.OPERATION_FAILED);
    }
}
