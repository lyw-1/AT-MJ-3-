package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.ProductionTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 鐢熶骇浠诲姟Mapper接口
 * 鎻愪緵鐢熶骇浠诲姟琛ㄧ殑数据搴撴搷浣滄柟娉?
 */
@Mapper
public interface ProductionTaskMapper extends BaseMapper<ProductionTask> {
    
    /**
     * 鏍规嵁浠诲姟浠ｇ爜查询鐢熶骇浠诲姟
     * @param taskCode 浠诲姟浠ｇ爜
     * @return 鐢熶骇浠诲姟淇℃伅
     */
    @Select("SELECT * FROM production_task WHERE task_code = #{taskCode}")
    ProductionTask selectByTaskCode(String taskCode);
    
    /**
     * 鏍规嵁璁惧ID查询鐢熶骇浠诲姟鍒楄〃
     * @param equipmentId 璁惧ID
     * @return 鐢熶骇浠诲姟鍒楄〃
     */
    @Select("SELECT * FROM production_task WHERE equipment_id = #{equipmentId}")
    List<ProductionTask> selectByEquipmentId(Long equipmentId);
    
    /**
     * 鏍规嵁浠诲姟鐘舵€佹煡璇㈢敓浜т换鍔″垪琛?
     * @param status 浠诲姟状态
     * @return 鐢熶骇浠诲姟鍒楄〃
     */
    @Select("SELECT * FROM production_task WHERE status = #{status}")
    List<ProductionTask> selectByStatus(Integer status);
}
