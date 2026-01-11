package com.mold.digitalization.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mold.digitalization.entity.Product;
import com.mold.digitalization.service.ProductService;
import com.mold.digitalization.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 成品管理控制器
 * 实现前端所需的成品管理API接口
 */
@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 获取成品列表
     * @param page 页码
     * @param size 每页数量
     * @param productCategory 成品类别（可选）
     * @param productSpec 成品规格（可选）
     * @return 成品列表
     */
    @GetMapping
    public ResponseDTO<IPage<Product>> getProducts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String productCategory,
            @RequestParam(required = false) String productSpec) {
        // 构建分页查询
        Page<Product> pageParam = new Page<>(page, size);
        
        // 构建查询条件
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        
        // 添加成品类别查询条件
        if (productCategory != null && !productCategory.isEmpty()) {
            queryWrapper.eq("product_category", productCategory);
        }
        
        // 添加成品规格查询条件
        if (productSpec != null && !productSpec.isEmpty()) {
            queryWrapper.eq("product_spec", productSpec);
        }
        
        // 执行带条件的分页查询
        IPage<Product> productPage = productService.page(pageParam, queryWrapper);
        return ResponseDTO.success(productPage);
    }

    /**
     * 获取单个成品详情
     * @param id 成品ID
     * @return 成品详情
     */
    @GetMapping("/{id}")
    public ResponseDTO<Product> getProductDetail(@PathVariable Long id) {
        Product product = productService.getById(id);
        return ResponseDTO.success(product);
    }

    /**
     * 创建成品
     * @param product 成品数据
     * @return 创建结果
     */
    @PostMapping
    public ResponseDTO<Product> createProduct(@RequestBody @Valid Product product) {
        boolean saved = productService.save(product);
        if (saved) {
            return ResponseDTO.success(product);
        } else {
            return ResponseDTO.error("创建成品失败");
        }
    }

    /**
     * 更新成品
     * @param id 成品ID
     * @param product 成品数据
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseDTO<Product> updateProduct(@PathVariable Long id, @RequestBody @Valid Product product) {
        product.setId(id);
        boolean updated = productService.updateById(product);
        if (updated) {
            return ResponseDTO.success(product);
        } else {
            return ResponseDTO.error("更新成品失败");
        }
    }

    /**
     * 删除成品
     * @param id 成品ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseDTO<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.removeById(id);
        if (deleted) {
            return ResponseDTO.success(null);
        } else {
            return ResponseDTO.error("删除成品失败");
            }
    }
    
    /**
     * 批量删除成品
     * @param ids 成品ID列表
     * @return 删除结果
     */
    @DeleteMapping("/batch-delete")
    public ResponseDTO<Void> batchDeleteProducts(@RequestBody List<Long> ids) {
        boolean deleted = productService.removeByIds(ids);
        if (deleted) {
            return ResponseDTO.success(null);
        } else {
            return ResponseDTO.error("批量删除成品失败");
        }
    }
    
    /**
     * 导出成品列表
     * @return 导出文件
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportProducts() {
        // 这里需要实现导出逻辑，返回CSV或Excel文件
        // 暂时返回空响应
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
    }
    
    /**
     * 导入成品列表
     * @param file 导入文件
     * @return 导入结果
     */
    @PostMapping("/import")
    public ResponseDTO<Void> importProducts(@RequestParam("file") MultipartFile file) {
        // 这里需要实现导入逻辑
        // 暂时返回成功响应
        return ResponseDTO.success(null);
    }
}