package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.ConsumableItem;

public interface ConsumableItemService extends IService<ConsumableItem> {
    boolean create(ConsumableItem item);
    boolean isMaterialCodeUnique(String materialCode, Long id);
}
