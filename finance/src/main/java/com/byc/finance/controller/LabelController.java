package com.byc.finance.controller;

import com.byc.finance.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by baiyc
 * 2019/6/3/003 17:56
 * Description：书签控制器
 */
@Controller
public class LabelController {

    @Autowired
    LabelService labelService;

    @RequestMapping("/label/saveLabel")
    public String saveLable(String url,Integer typeId) {
        labelService.doSaveLable(url,typeId);
        return "redirect:/finance/label";
    }
}
