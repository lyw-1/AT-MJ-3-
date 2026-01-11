package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.EquipmentTypeMapper;
import com.mold.digitalization.entity.EquipmentType;
import com.mold.digitalization.service.EquipmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 璁惧绫诲瀷服务实现绫? * 实现璁惧绫诲瀷鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉? */
@Service
public class EquipmentTypeServiceImpl extends ServiceImpl<EquipmentTypeMapper, EquipmentType> implements EquipmentTypeService {
    
    @Autowired
    private EquipmentTypeMapper equipmentTypeMapper;
    
    @Override
    public EquipmentType getEquipmentTypeByCode(String typeCode) {
        return equipmentTypeMapper.selectByTypeCode(typeCode);
    }
    
    @Override
    public boolean createEquipmentType(EquipmentType equipmentType) {
        return save(equipmentType);
    }
    
    @Override
    public boolean updateEquipmentType(EquipmentType equipmentType) {
        return updateById(equipmentType);
    }
    
    @Override
    public boolean deleteEquipmentType(Long typeId) {
        EquipmentType existingType = getById(typeId);
        if (existingType == null) {
            return false;
        }
        return equipmentTypeMapper.deleteById(typeId) > 0;
    }
    
    @Override
    public List<EquipmentType> getAllEquipmentTypes() {
        return list();
    }
}
