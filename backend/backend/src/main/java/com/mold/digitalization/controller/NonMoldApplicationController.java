package com.mold.digitalization.controller;

import com.mold.digitalization.entity.NonMoldApplication;
import com.mold.digitalization.service.NonMoldApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/non-mold-applications")
public class NonMoldApplicationController extends BaseController {
    @Autowired
    private NonMoldApplicationService service;

    @GetMapping
    public ResponseEntity<List<NonMoldApplication>> list() {
        return success(service.list());
    }

    @PostMapping
    public ResponseEntity<NonMoldApplication> create(@RequestBody NonMoldApplication a) {
        service.save(a);
        return success(a);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody NonMoldApplication a) {
        a.setId(id);
        return service.updateById(a) ? success() : notFoundGeneric("not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.removeById(id) ? success() : notFoundGeneric("not found");
    }
}
