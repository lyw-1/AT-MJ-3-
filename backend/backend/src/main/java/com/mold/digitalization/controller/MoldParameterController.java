package com.mold.digitalization.controller;

import com.mold.digitalization.entity.MoldParameter;
import com.mold.digitalization.service.MoldParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 妯″叿鍙傛暟Controller
 * 鎻愪緵妯″叿鍙傛暟鐩稿叧鐨凥TTP接口
 */
@RestController
@RequestMapping("/api/mold/parameter")
public class MoldParameterController {
    
    @Autowired
    private MoldParameterService moldParameterService;
    
    /**
     * 鏍规嵁妯″叿ID获取鍙傛暟淇℃伅
     */
    @GetMapping("/by-mold/{moldId}")
    public MoldParameter getByMoldId(@PathVariable Long moldId) {
        return moldParameterService.getByMoldId(moldId);
    }
    
    /**
     * 淇濆瓨妯″叿鍙傛暟
     */
    @PostMapping("/save")
    public boolean save(@RequestBody MoldParameter parameter) {
        return moldParameterService.saveParameter(parameter);
    }
    
    /**
     * 更新妯″叿鍙傛暟
     */
    @PutMapping("/update")
    public boolean update(@RequestBody MoldParameter parameter) {
        return moldParameterService.updateParameter(parameter);
    }
}
