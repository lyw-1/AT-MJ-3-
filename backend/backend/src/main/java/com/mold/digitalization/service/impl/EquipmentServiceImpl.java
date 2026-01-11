package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.EquipmentMapper;
import com.mold.digitalization.entity.Equipment;
import com.mold.digitalization.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 璁惧服务实现绫?
 * 实现璁惧鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉?
 */
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {
    
    @Autowired
    private EquipmentMapper equipmentMapper;
    
    @Override
    public Equipment getEquipmentByCode(String equipmentCode) {
        return equipmentMapper.selectByEquipmentCode(equipmentCode);
    }
    
    @Override
    public List<Equipment> getEquipmentByTypeId(Long typeId) {
        return equipmentMapper.selectByTypeId(typeId);
    }
    
    @Override
    public boolean createEquipment(Equipment equipment) {
        return save(equipment);
    }
    
    @Override
    public boolean updateEquipment(Equipment equipment) {
        return updateById(equipment);
    }
    
    @Override
    public boolean deleteEquipment(Long equipmentId) {
        Equipment existingEquipment = getById(equipmentId);
        if (existingEquipment == null) {
            return false;
        }
        return equipmentMapper.deleteById(equipmentId) > 0;
    }
    
    @Override
    public List<Equipment> getAllEquipment() {
        return list();
    }
    
    @Override
    public boolean updateEquipmentStatus(Long equipmentId, Integer status) {
        UpdateWrapper<Equipment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", equipmentId)
                     .set("status", status);
        return update(updateWrapper);
    }
}
