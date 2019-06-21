package com.byc.service.spider;

import com.byc.dao.entity.movie.Movie;
import com.byc.dao.movie.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * Created by baiyc
 * 2019/6/5/005 19:11
 * Description：
 */
@Slf4j
public class MoviePagePipeline implements Pipeline {

    private MovieRepository movieRepository;

    public MoviePagePipeline(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        try {
            if(StringUtils.isEmpty(resultItems.get("name")))
                return;
            Movie movie = movieRepository.findByName(resultItems.get("name"));
            if(movie==null)
                movie = new Movie();
            movie.setName(resultItems.get("name"));
            movie.setAlias(resultItems.get("alias"));
            movie.setCover(resultItems.get("cover"));
            movie.setDescription(resultItems.get("description"));
            movie.setDouban(resultItems.get("douban"));
            movie.setDirector(resultItems.get("director"));
            movie.setLanguage(resultItems.get("language"));
            movie.setPublish_time(resultItems.get("publish_time"));
            movie.setScreen(resultItems.get("screen"));
            movie.setShow_time(resultItems.get("show_time"));
            movie.setStars(resultItems.get("stars"));
            movie.setSource(resultItems.get("source"));
            movie.setTag(resultItems.get("tag"));
            movie.setIcon(resultItems.get("icons"));
            movieRepository.save(movie);
            System.out.println(movie.toString());
        }catch (Exception e){
            log.error("持久化数据异常");
        }
    }
}
