package com.byc.service.spider;

import com.byc.dao.entity.movie.Movie;
import com.byc.dao.movie.MovieRepository;
import com.byc.spider.MovieSpider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

/**
 * Created by baiyc
 * 2019/5/31/031 20:38
 * Description：处理爬虫数据
 */
@Slf4j
@AllArgsConstructor
public class MoviePageModelPipeline implements PageModelPipeline {

    private MovieRepository movieRepository;

    @Override
    public void process(Object o, Task task) {
        try {
            Assert.isInstanceOf(MovieSpider.class,o);
            MovieSpider movieSpider = (MovieSpider)o;
            Movie movie = new Movie();
            movie.setName(movieSpider.getName());
            movie.setAlias(movieSpider.getAlias());
            movie.setCover(movieSpider.getCover());
            movie.setDescription(movieSpider.getDescription());
            movie.setDouban(movieSpider.getDouban());
            movie.setDirector(movieSpider.getDirector());
            movie.setLanguage(movieSpider.getLanguage());
            movie.setPublish_time(movieSpider.getPublish_time());
            movie.setScreen(movieSpider.getScreen());
            movie.setShow_time(movieSpider.getShow_time());
            movie.setStars(movieSpider.getStars());
            movie.setSource(movieSpider.getSource());
            movie.setTag(movieSpider.getTag());
            movie.setIcon(movieSpider.getIcons());
            movieRepository.save(movie);
        }catch (Exception e){
            log.error("持久化数据异常");
        }

    }
}
