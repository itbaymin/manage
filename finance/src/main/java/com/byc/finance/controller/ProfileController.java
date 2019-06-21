package com.byc.finance.controller;

import com.byc.dao.entity.system.User;
import com.byc.dao.system.UserRepository;
import com.byc.util.UpdateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by baiyc
 * 2019/6/5/005 09:24
 * Description：个人资料控制器
 */
@Slf4j
@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Value("${upload.path}")
    String uploadPath;

    @Value("${resource.prefix}")
    String prefix;

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/doEdit")
    public String doEdit(User user){
        Optional<User> byId = userRepository.findById(user.getId());
        UpdateUtil.copyNullProperties(byId.get(),user);
        userRepository.save(user);
        return "redirect:/finance/profile";
    }

    @RequestMapping("/repeat")
    public String repeatIcon(HttpServletRequest req){
        String username = (String) req.getAttribute("username");
        File dir = new File(uploadPath);
        int length = dir.listFiles().length;
        int imgNum = (int)(Math.random() * length) + 1;
        User user = userRepository.findByName(username);
        user.setIcon(prefix+imgNum+".jpg");
        userRepository.save(user);
        return "redirect:/finance/profile";
    }

    @RequestMapping("/upload")
    public String uploadIcon(@RequestParam("file") MultipartFile file,HttpServletRequest req){
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        File dir = new File(uploadPath);
        int length = dir.listFiles().length;
        String fileName = length+1+".jpg";
        File dest = new File(dir.getAbsolutePath() + File.separator + fileName);
        try {
            file.transferTo(dest);
            String username = (String) req.getAttribute("username");
            User user = userRepository.findByName(username);
            user.setIcon(prefix+fileName);
            userRepository.save(user);
            log.info("上传成功");
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return "redirect:/finance/profile";
    }
}
