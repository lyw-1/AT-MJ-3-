package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.entity.NonMoldApplication;
import com.mold.digitalization.mapper.NonMoldApplicationMapper;
import com.mold.digitalization.service.NonMoldApplicationService;
import org.springframework.stereotype.Service;

@Service
public class NonMoldApplicationServiceImpl extends ServiceImpl<NonMoldApplicationMapper, NonMoldApplication> implements NonMoldApplicationService {
}
