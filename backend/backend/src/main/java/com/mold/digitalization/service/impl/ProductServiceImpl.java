package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.Product;
import com.mold.digitalization.mapper.ProductMapper;
import com.mold.digitalization.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    /**
     * 重写save方法，添加数据唯一性验证
     * @param product 成品数据
     * @return 是否保存成功
     */
    @Override
    public boolean save(Product product) {
        // 检查数据是否重复（基于五字段组合）
        if (isProductDuplicate(product)) {
            throw new RuntimeException(buildDuplicateErrorMessage(product, "添加"));
        }
        return super.save(product);
    }

    /**
     * 重写updateById方法，添加数据唯一性验证
     * @param product 成品数据
     * @return 是否更新成功
     */
    @Override
    public boolean updateById(Product product) {
        // 检查数据是否重复（基于五字段组合，排除自身）
        if (isProductDuplicate(product, product.getId())) {
            throw new RuntimeException(buildDuplicateErrorMessage(product, "更新"));
        }
        return super.updateById(product);
    }

    /**
     * 构建重复数据错误信息
     * @param product 成品数据
     * @param operation 操作类型（添加/更新）
     * @return 格式化的错误信息
     */
    private String buildDuplicateErrorMessage(Product product, String operation) {
        return String.format("【%s】失败：该成品数据已存在。\n" +
                             "重复条件：成品类别='%s'、成品规格='%s'、容重要求='%s-%s'、槽宽要求='%s-%s'\n" +
                             "请检查并修改以上字段后重试。",
                operation,
                product.getProductCategory(),
                product.getProductSpec(),
                product.getDensityRequirementMin(),
                product.getDensityRequirementMax(),
                product.getSlotWidthRequirementMin(),
                product.getSlotWidthRequirementMax());
    }

    /**
     * 检查成品数据是否重复
     * @param product 成品数据
     * @return 是否重复
     */
    private boolean isProductDuplicate(Product product) {
        return isProductDuplicate(product, null);
    }

    /**
     * 检查成品数据是否重复（可排除指定ID）
     * 基于四字段组合：成品类别、成品规格、容重要求、槽宽要求
     * @param product 成品数据
     * @param excludeId 排除的ID（用于更新操作）
     * @return 是否重复
     */
    private boolean isProductDuplicate(Product product, Long excludeId) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        
        // 基于四字段组合的唯一性校验，排除客户字段
        queryWrapper.eq("product_category", product.getProductCategory());
        queryWrapper.eq("product_spec", product.getProductSpec());
        queryWrapper.eq("density_requirement_min", product.getDensityRequirementMin());
        queryWrapper.eq("density_requirement_max", product.getDensityRequirementMax());
        queryWrapper.eq("slot_width_requirement_min", product.getSlotWidthRequirementMin());
        queryWrapper.eq("slot_width_requirement_max", product.getSlotWidthRequirementMax());
        
        // 如果是更新操作，排除自身
        if (excludeId != null) {
            queryWrapper.ne("id", excludeId);
        }
        
        // 执行查询，检查是否存在重复数据
        return this.count(queryWrapper) > 0;
    }
}
