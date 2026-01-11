package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.MoldTypeMapper;
import com.mold.digitalization.entity.MoldType;
import com.mold.digitalization.service.MoldTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 妯″叿绫诲瀷服务实现绫?
 * 实现妯″叿绫诲瀷鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉?
 */
@Service
public class MoldTypeServiceImpl extends ServiceImpl<MoldTypeMapper, MoldType> implements MoldTypeService {
    
    @Autowired
    private MoldTypeMapper moldTypeMapper;
    
    @Override
    public MoldType getMoldTypeByCode(String typeCode) {
        return moldTypeMapper.selectByTypeCode(typeCode);
    }
    
    @Override
    public boolean createMoldType(MoldType moldType) {
        return save(moldType);
    }
    
    @Override
    public boolean updateMoldType(MoldType moldType) {
        return updateById(moldType);
    }
    
    @Override
    public boolean deleteMoldType(Long typeId) {
        // 鍏堟鏌ユā鍏风被鍨嬫槸鍚﹀瓨鍦?
        MoldType existingType = getById(typeId);
        if (existingType == null) {
            return false;
        }
        
        // 浣跨敤Mapper鐨刣eleteById方法删除
        return moldTypeMapper.deleteById(typeId) > 0;
    }
    
    @Override
    public List<MoldType> getAllMoldTypes() {
        return list();
    }
}