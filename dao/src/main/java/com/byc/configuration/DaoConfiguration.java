package com.byc.configuration;

import com.byc.dao.support.extend.CustomRepositoryFactoryBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by baiyc
 * 2019/5/13/013 16:35
 * Description：自动配置创建时间配置类
 */
@EnableJpaAuditing
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class, basePackages = "com.byc.dao")
@EntityScan(basePackages = "com.byc.dao.entity")
@Configuration
public class DaoConfiguration {
    //@Bean
    //public AuditorAware auditorAware() {
    //    return () -> Optional.of("");
    //}
}
