package com.byc.finance.service;

import com.byc.common.mvc.R;
import com.byc.common.util.HTMLUtil;
import com.byc.dao.entity.label.Label;
import com.byc.dao.label.LabelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by baiyc
 * 2019/6/3/003 19:35
 * Description：书签业务类
 */
@Slf4j
@Service
public class LabelService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LabelRepository labelRepository;

    public R doSaveLable(String url,Integer typeId){
        String resp = restTemplate.getForObject(url, String.class);
        String title = HTMLUtil.getTitle(resp);
        Label label = new Label();
        label.setTitle(title);
        label.setUrl(url);
        label.setCollectTypeId(typeId);
        String icon = HTMLUtil.getIcon(url);
        try{
            restTemplate.getForEntity(icon, String.class);
        }catch (Exception e){
            icon = "/favicon.ico";
        }
        label.setIcon(icon);
        labelRepository.save(label);
        return R.succ();
    }
}
