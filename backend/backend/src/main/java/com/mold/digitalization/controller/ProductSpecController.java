package com.mold.digitalization.controller;

import com.mold.digitalization.entity.ProductSpec;
import com.mold.digitalization.service.ProductSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product-specs")
public class ProductSpecController extends BaseController {
    @Autowired
    private ProductSpecService productSpecService;

    @GetMapping
    public ResponseEntity<List<ProductSpec>> list() {
        return success(productSpecService.list());
    }

    @PostMapping
    public ResponseEntity<ProductSpec> create(@RequestBody ProductSpec s) {
        productSpecService.save(s);
        return success(s);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody ProductSpec s) {
        s.setId(id);
        return productSpecService.updateById(s) ? success() : notFoundGeneric("not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return productSpecService.removeById(id) ? success() : notFoundGeneric("not found");
    }
}
