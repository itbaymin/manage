package com.byc.dao.movie;

import com.byc.dao.entity.movie.Movie;
import com.byc.dao.support.extend.CustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by baiyc
 * 2019/5/31/031 20:36
 * Description：电影数据库操作接口
 */
public interface MovieRepository extends CustomRepository<Movie,Long> {
    Movie findByName(String name);
    List<Movie> findFirst3ByOrderByModifyTimeDesc();

    @Query(value = "select publish_time time,count(1) counts from movie where publish_time>?1 group by publish_time",nativeQuery = true)
    List findMovieCount(String time);

    Page<Movie> findByNameLike(String name, Pageable pageable);
}
