package com.mold.digitalization.controller;

import com.mold.digitalization.entity.ConsumableItem;
import com.mold.digitalization.service.ConsumableItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumables")
public class ConsumableItemController extends BaseController {

  @Autowired
  private ConsumableItemService service;
  @Autowired
  private com.mold.digitalization.service.ConsumableStockRecordService recordService;

    @GetMapping
    public ResponseEntity<List<ConsumableItem>> list() {
        try {
            return success(service.list());
        } catch (Exception e) {
            return success(List.of());
        }
    }

    @GetMapping("/check-material-code-unique")
    public ResponseEntity<?> checkMaterialCodeUnique(@RequestParam String materialCode, @RequestParam(required = false) Long id) {
        try {
            // 调用服务层检查物料编码唯一性
            boolean isUnique = service.isMaterialCodeUnique(materialCode, id);
            return success(isUnique);
        } catch (Exception e) {
            System.err.println("检查物料编码唯一性失败: " + e.getMessage());
            return internalServerError("检查物料编码唯一性失败");
        }
    }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody ConsumableItem item) {
    boolean ok = service.create(item);
    if (!ok) return internalServerError("Create failed");
    return created(java.util.Map.of("id", item.getId()));
  }

  @PostMapping("/{id}/inbound")
  public ResponseEntity<?> inbound(@PathVariable Long id, @RequestBody java.util.Map<String, Integer> body) {
    Integer qty = body.getOrDefault("qty", 0);
    if (qty == null || qty <= 0) return badRequest("数量必须大于0");
    ConsumableItem item = service.getById(id);
    if (item == null) return notFoundGeneric("Not Found");
    int current = item.getCurrentStock() == null ? 0 : item.getCurrentStock();
    item.setCurrentStock(current + qty);
    item.setUpdateTime(java.time.LocalDateTime.now());
    service.updateById(item);
    // 记录入库
    com.mold.digitalization.entity.ConsumableStockRecord r = new com.mold.digitalization.entity.ConsumableStockRecord();
    r.setItemId(item.getId());
    r.setItemName(item.getItemName());
    r.setItemCategory(item.getItemCategory());
    r.setItemSpec(item.getSpecification());
    r.setRecordType("IN");
    r.setQty(qty);
    r.setOperator("dev");
    r.setRemark("入库调整");
    recordService.createRecord(r);
    return success(item);
  }

  @PostMapping("/{id}/outbound")
  public ResponseEntity<?> outbound(@PathVariable Long id, @RequestBody java.util.Map<String, Integer> body) {
    Integer qty = body.getOrDefault("qty", 0);
    if (qty == null || qty <= 0) return badRequest("数量必须大于0");
    ConsumableItem item = service.getById(id);
    if (item == null) return notFoundGeneric("Not Found");
    int current = item.getCurrentStock() == null ? 0 : item.getCurrentStock();
    if (current < qty) return badRequest("库存不足");
    item.setCurrentStock(current - qty);
    item.setUpdateTime(java.time.LocalDateTime.now());
    service.updateById(item);
    // 记录出库
    com.mold.digitalization.entity.ConsumableStockRecord r = new com.mold.digitalization.entity.ConsumableStockRecord();
    r.setItemId(item.getId());
    r.setItemName(item.getItemName());
    r.setItemCategory(item.getItemCategory());
    r.setItemSpec(item.getSpecification());
    r.setRecordType("OUT");
    r.setQty(qty);
    r.setOperator("dev");
    r.setRemark("出库调整");
    recordService.createRecord(r);
    return success(item);
  }

  // 入库记录接口（仅允许针对已存在的三要素进行入库）
  @PostMapping("/inbound-records")
  public ResponseEntity<?> createInboundRecord(@RequestBody java.util.Map<String, Object> payload) {
    Long itemId = payload.get("itemId") != null ? Long.valueOf(String.valueOf(payload.get("itemId"))) : null;
    Integer qty = payload.get("qty") != null ? Integer.valueOf(String.valueOf(payload.get("qty"))) : 0;
    String remark = String.valueOf(payload.getOrDefault("remark", ""));
    if (qty == null || qty <= 0) return badRequest("数量必须大于0");
    ConsumableItem item = null;
    if (itemId != null) {
      item = service.getById(itemId);
    }
    if (item == null) {
      // 尝试按三要素匹配
      String name = String.valueOf(payload.getOrDefault("itemName", ""));
      String category = String.valueOf(payload.getOrDefault("itemCategory", ""));
      String spec = String.valueOf(payload.getOrDefault("itemSpec", ""));
      com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ConsumableItem> qw = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
      qw.eq("item_name", name).eq("item_category", category).eq("item_spec", spec);
      item = service.getOne(qw, false);
    }
    if (item == null) return badRequest("耗材不存在，不能入库");
    int current = item.getCurrentStock() == null ? 0 : item.getCurrentStock();
    item.setCurrentStock(current + qty);
    item.setUpdateTime(java.time.LocalDateTime.now());
    service.updateById(item);
    com.mold.digitalization.entity.ConsumableStockRecord r = new com.mold.digitalization.entity.ConsumableStockRecord();
    r.setItemId(item.getId()); r.setItemName(item.getItemName()); r.setItemCategory(item.getItemCategory()); r.setItemSpec(item.getSpecification());
    r.setRecordType("IN"); r.setQty(qty); r.setOperator("dev"); r.setRemark(remark);
    recordService.createRecord(r);
    return success(item);
  }

  @GetMapping("/stock-records")
  public ResponseEntity<java.util.List<com.mold.digitalization.entity.ConsumableStockRecord>> listStockRecords(
          @RequestParam(value = "type", required = false) String type,
          @RequestParam(value = "itemId", required = false) Long itemId
  ) {
    com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.mold.digitalization.entity.ConsumableStockRecord> qw = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
    if (type != null && !type.isEmpty()) qw.eq("record_type", type);
    if (itemId != null) qw.eq("item_id", itemId);
    java.util.List<com.mold.digitalization.entity.ConsumableStockRecord> list = recordService.list(qw);
    return success(list);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ConsumableItem payload) {
    ConsumableItem item = service.getById(id);
    if (item == null) {
      // 兜底：按名称+规格尝试匹配并更新；若仍不存在则执行创建
      com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ConsumableItem> qw = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
      if (payload.getItemName() != null) qw.eq("item_name", payload.getItemName());
      if (payload.getSpecification() != null) qw.eq("specification", payload.getSpecification());
      ConsumableItem byFields = service.getOne(qw, false);
      if (byFields != null) {
        id = byFields.getId();
        item = byFields;
      } else {
        // 创建新记录
        payload.setCreateTime(java.time.LocalDateTime.now());
        payload.setUpdateTime(java.time.LocalDateTime.now());
        boolean created = service.save(payload);
        if (!created) return internalServerError("Upsert failed");
        return success(payload);
      }
    }
    item.setMaterialCode(payload.getMaterialCode());
    item.setItemName(payload.getItemName());
    item.setItemCategory(payload.getItemCategory());
    item.setSpecification(payload.getSpecification());
    item.setCurrentStock(payload.getCurrentStock());
    item.setUnit(payload.getUnit());
    item.setMinStock(payload.getMinStock());
    item.setUpdateTime(java.time.LocalDateTime.now());
    service.updateById(item);
    return success(item);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    boolean ok = service.removeById(id);
    if (!ok) return internalServerError("Delete failed");
    return success(java.util.Map.of("id", id));
  }
}
