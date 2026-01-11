package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.ProductSpec;
import com.mold.digitalization.mapper.ProductSpecMapper;
import com.mold.digitalization.service.ProductSpecService;
import org.springframework.stereotype.Service;

@Service
public class ProductSpecServiceImpl extends ServiceImpl<ProductSpecMapper, ProductSpec> implements ProductSpecService {
}
