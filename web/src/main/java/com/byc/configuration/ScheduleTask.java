package com.byc.configuration;

import com.byc.dao.movie.MovieRepository;
import com.byc.service.spider.MoviePagePipeline;
import com.byc.spider.MoviePageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.Spider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by baiyc
 * 2019/6/5/005 20:42
 * Description：定时任务
 */
@Configuration
@EnableScheduling
public class ScheduleTask {
    @Autowired
    MovieRepository movieRepository;

    //3.添加定时任务
    @Scheduled(cron = "0 0 8,20 * * *")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String format = dateFormat.format(new Date());
        MoviePageProcessor moviePageProcessor = new MoviePageProcessor();
        moviePageProcessor.setHelperUrl("https://www.dytt8.net/html/gndy/dyzz/list_23_\\d+.html");
        moviePageProcessor.setTargetUrl("https://www.dytt8.net/html/gndy/dyzz/"+format+"/\\d+.html");
        Spider.create(moviePageProcessor)
                .addPipeline(new MoviePagePipeline(movieRepository))
                //从"https://github.com/code4craft"开始抓
                //.addUrl("https://www.dytt8.net/html/gndy/dyzz/list_23_1.html")
                .addUrl("https://www.dytt8.net/index.htm")
                //开启5个线程抓取
                .thread(5)
                //启动爬虫
                .run();
    }
}
