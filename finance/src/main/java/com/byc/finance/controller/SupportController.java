package com.byc.finance.controller;

import com.byc.dao.entity.label.CollectType;
import com.byc.dao.entity.label.Label;
import com.byc.dao.entity.system.User;
import com.byc.dao.label.CollectTypeRepository;
import com.byc.dao.label.LabelRepository;
import com.byc.dao.system.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by baiyc
 * 2019/5/17/017 14:42
 * Description：地图控制器
 */
@Slf4j
@Controller
public class SupportController {

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    CollectTypeRepository collectTypeRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping("finance/map")
    public String map(Model model){
        model.addAttribute("action","map");
        return "finance/map";
    }

    @RequestMapping("finance/map/search")
    public String mapSearch(Model model,String keyword) throws Exception {
        if(1==1)
            throw new Exception("");
        return "finance/weather";
    }

    @RequestMapping("finance/label")
    public String label(Model model){
        model.addAttribute("action","label");
        Label label = null;
        try {
            label = labelRepository.findFirstByOrderByModifyTimeDesc();
        }catch (Exception e){
            log.error("find label form datebase failed!",e);
        }
        List<CollectType> all = collectTypeRepository.findAll();
        model.addAttribute("url",label==null?"":label.getUrl());
        model.addAttribute("types",all);
        return "finance/label";
    }

    @RequestMapping("finance/label/search")
    public String labelSearch(Model model,String keyword){
        model.addAttribute("action","label");
        model.addAttribute("url",keyword);
        List<CollectType> all = collectTypeRepository.findAll();
        model.addAttribute("types",all);
        return "finance/label";
    }


    @RequestMapping("finance/weather")
    public String weather(Model model){
        model.addAttribute("action","weather");
        return "finance/weather";
    }

    @RequestMapping("finance/weather/search")
    public String weatherSearch(Model model,String keyword){
        model.addAttribute("action","weather");
        model.addAttribute("keyword",keyword);
        return "finance/weather";
    }

    @RequestMapping("finance/icon")
    public String icon(Model model){
        model.addAttribute("action","icon");
        return "finance/icon";
    }

    @RequestMapping("finance/profile")
    public String profile(Model model, HttpServletRequest req){
        String username = (String) req.getAttribute("username");
        User user = userRepository.findByName(username);
        model.addAttribute("action","profile");
        model.addAttribute("user",user);
        return "finance/profile";
    }

    @RequestMapping("finance/ftp")
    public String index(Model model){
        model.addAttribute("action","ftp");
        return "finance/ftp";
    }

    @RequestMapping("finance/404")
    public String notFound(){
        return "finance/404";
    }
}
