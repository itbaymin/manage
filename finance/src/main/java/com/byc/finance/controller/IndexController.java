package com.byc.finance.controller;

import com.alibaba.fastjson.JSONObject;
import com.byc.common.mvc.R;
import com.byc.dao.entity.movie.Movie;
import com.byc.dao.movie.MovieRepository;
import com.byc.dao.support.extend.SystemRequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by baiyc
 * 2019/5/17/017 10:27
 * Description：财务控制器
 */
@Controller
public class IndexController {

    @Autowired
    MovieRepository movieRepository;

    @RequestMapping("/finance/index")
    public String index(Model model,String keyword){
        List<Movie> news = movieRepository.findFirst3ByOrderByModifyTimeDesc();
        model.addAttribute("news",news);
        model.addAttribute("keyword",keyword);
        model.addAttribute("action","index");
        return "finance/index";
    }

    @RequestMapping("/finance/index/search")
    public String search(){
        return "forward:/finance/index";
    }

    @ResponseBody
    @RequestMapping("/finance/getMovieCount")
    public R movieCount(String time){
        List<Object> movieCount = movieRepository.findMovieCount(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        movieCount.forEach(mc->{
            Object[] mc1 = (Object[]) mc;
            try {
                Date parse = format.parse(mc1[0].toString());
                mc1[0] = parse.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        Object json = JSONObject.toJSON(movieCount);
        return R.succ(json);
    }


    @ResponseBody
    @RequestMapping("/finance/getMovie")
    public R movie(String name){
        Page<Movie> page = movieRepository.findByNameLike("%"+name+"%", SystemRequestHolder.getPageRequest());
        return R.succ(page);
    }

    @RequestMapping("/finance/movie/{id}")
    public String moviePage(@PathVariable("id") long id,Model model) throws UnsupportedEncodingException {
        Optional<Movie> byId = movieRepository.findById(id);
        String source = byId.get().getSource();
        model.addAttribute("fileName", URLEncoder.encode(source.substring(source.lastIndexOf("/")+1),"utf-8"));
        return "finance/movie";
    }
}
