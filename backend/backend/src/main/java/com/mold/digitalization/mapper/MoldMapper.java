package com.mold.digitalization.mapper;

import com.mold.digitalization.entity.Mold;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 妯″叿Mapper接口
 * 鎻愪緵瀵筸old琛ㄧ殑数据搴撴搷浣滄柟娉? */
@Mapper
public interface MoldMapper extends BaseMapper<Mold> {

    /**
     * 鏍规嵁妯″叿缂栧彿查询妯″叿淇℃伅
     * @param moldCode 妯″叿缂栧彿
     * @return 妯″叿实体
     */
    Mold selectByMoldCode(String moldCode);

    /**
     * 鏍规嵁妯″叿鐘舵€佹煡璇㈡ā鍏峰垪琛?     * @param moldStatusId 妯″叿鐘舵€両D
     * @return 妯″叿鍒楄〃
     */
    List<Mold> selectByStatus(Long moldStatusId);
    
    /**
     * 鏍规嵁妯″叿绫诲瀷ID查询妯″叿鍒楄〃
     * @param typeId 妯″叿绫诲瀷ID
     * @return 妯″叿鍒楄〃
     */
    List<Mold> selectByTypeId(@Param("typeId") Long typeId);
    
    /**
     * 鏍规嵁妯″叿鐘舵€両D查询妯″叿鍒楄〃锛堜笌ServiceImpl鍖归厤鐨勬柟娉曞悕锛?     * @param statusId 妯″叿鐘舵€両D
     * @return 妯″叿鍒楄〃
     */
    List<Mold> selectByStatusId(@Param("statusId") Long statusId);

    /**
     * 鏍规嵁鏉愯川查询妯″叿鍒楄〃
     * @param material 鏉愯川
     * @return 妯″叿鍒楄〃
     */
    List<Mold> selectByMaterial(String material);

    /**
     * 查询鎺ヨ繎浣跨敤瀵垮懡鐨勬ā鍏?     * @param threshold 瀵垮懡闃堝€肩櫨鍒嗘瘮
     * @return 妯″叿鍒楄〃
     */
    List<Mold> selectNearEndOfLife(Integer threshold);

    /**
     * 缁熻鍚勭被鍨嬫ā鍏锋暟閲?     * @return 绫诲瀷缁熻结果
     */
    List<Map<String, Object>> countByMoldType();

    /**
     * 缁熻鍚勭姸鎬佹ā鍏锋暟閲?     * @return 鐘舵€佺粺璁＄粨鏋?     */
    List<Map<String, Object>> countByMoldStatus();
}