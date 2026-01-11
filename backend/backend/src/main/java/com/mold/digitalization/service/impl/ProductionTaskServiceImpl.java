package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mold.digitalization.mapper.ProductionTaskMapper;
import com.mold.digitalization.entity.ProductionTask;
import com.mold.digitalization.service.ProductionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * 鐢熶骇浠诲姟服务实现绫? * 实现鐢熶骇浠诲姟鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉?
 */
@Service
public class ProductionTaskServiceImpl extends ServiceImpl<ProductionTaskMapper, ProductionTask>
        implements ProductionTaskService {
    private static final Logger log = LoggerFactory.getLogger(ProductionTaskServiceImpl.class);

    @Autowired
    private ProductionTaskMapper productionTaskMapper;

    @Override
    public ProductionTask getProductionTaskByCode(String taskCode) {
        return productionTaskMapper.selectByTaskCode(taskCode);
    }

    @Override
    public List<ProductionTask> getProductionTasksByEquipmentId(Long equipmentId) {
        return productionTaskMapper.selectByEquipmentId(equipmentId);
    }

    @Override
    public List<ProductionTask> getProductionTasksByStatus(Integer status) {
        return productionTaskMapper.selectByStatus(status);
    }

    @Override
    public List<ProductionTask> getProductionTasksByMoldId(Long moldId) {
        QueryWrapper<ProductionTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mold_id", moldId);
        return list(queryWrapper);
    }

    @Override
    public List<ProductionTask> getProductionTasksByTimeRange(Date startTime, Date endTime) {
        QueryWrapper<ProductionTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("create_time", startTime, endTime);
        return list(queryWrapper);
    }

    @Override
    public boolean createProductionTask(ProductionTask productionTask) {
        return save(productionTask);
    }

    @Override
    public boolean updateProductionTask(ProductionTask productionTask) {
        if (productionTask.getId() == null) {
            return false;
        }
        // 调试输出，便于在 surefire 报告中确认传入参数是否为 null
        System.out.println("[DEBUG] updateProductionTask input => id=" + productionTask.getId()
                + ", taskName=" + productionTask.getTaskName()
                + ", plannedQuantity=" + productionTask.getPlannedQuantity()
                + ", status=" + productionTask.getStatus()
                + ", description=" + productionTask.getDescription()
                + ", updateTime=" + productionTask.getUpdateTime());
        // 按主键更新，仅更新非空字段；实体上已设置 description 的 updateStrategy=NOT_NULL，确保非空时更新
        return updateById(productionTask);
    }

    @Override
    public boolean deleteProductionTask(Long taskId) {
        return removeById(taskId);
    }

    @Override
    public List<ProductionTask> getAllProductionTasks() {
        return list();
    }

    @Override
    public boolean updateProductionTaskStatus(Long taskId, Integer status) {
        ProductionTask productionTask = new ProductionTask();
        productionTask.setId(taskId);
        productionTask.setStatus(status);
        return updateById(productionTask);
    }
}
