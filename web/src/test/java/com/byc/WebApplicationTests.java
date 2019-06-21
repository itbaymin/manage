package com.byc;

import com.alibaba.fastjson.JSONObject;
import com.byc.dao.entity.label.CollectType;
import com.byc.dao.entity.label.Label;
import com.byc.dao.entity.system.Permission;
import com.byc.dao.entity.system.Role;
import com.byc.dao.entity.system.User;
import com.byc.dao.label.CollectTypeRepository;
import com.byc.dao.movie.MovieRepository;
import com.byc.dao.support.UserQuery;
import com.byc.dao.system.PermissionRepository;
import com.byc.dao.system.RoleRepository;
import com.byc.dao.system.UserRepository;
import com.byc.service.spider.MoviePageModelPipeline;
import com.byc.service.spider.MoviePagePipeline;
import com.byc.spider.MoviePageProcessor;
import com.byc.spider.MovieSpider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PermissionRepository permissionRepository;

	@Autowired
	MovieRepository movieRepository;

	@Autowired
	CollectTypeRepository collectTypeRepository;

	@Autowired
	RedisTemplate redisTemplate;

	@Test
	public void redis(){
		redisTemplate.convertAndSend("chat","Hemllasd");
	}

	@Test
	public void testCa(){
		UserQuery query = new UserQuery();
		query.setName("bai");
		query.setId(1);
		Pageable pageable = query.toPageable();
		Page<User> all = userRepository.findAll(query.toSpec(),pageable);
		System.out.println(all);
	}

	@Test
	public void contextLoads() {
		Permission per = new Permission();
		per.setId(1L);
		per.setName("主页");
		per.setRemark("测试");
		per.setIcon("sss");
		per.setType(0);
		per.setOrder_num(1);
		per.setParent_id(0L);
		per.setUrl("unl");
		per.setPermisssions("index");

		Role role = new Role();
		role.setId(1L);
		role.setName("amdin");
		role.setRemark("d");

		User user = new User();
		user.setName("baiyongcheng");
		user.setId(1L);
		user.setPassword("123");
		user.setSalt("123");
		user.setStatus(false);

		per.setRoles(new ArrayList<Role>(){{add(role);}});
		//role.setPermissions(new ArrayList<Permission>(){{add(per);}});
		role.setUsers(new ArrayList<User>(){{add(user);}});
		//user.setRoles(new ArrayList<Role>(){{add(role);}});
		permissionRepository.save(per);
		//userRepository.save(user);
	}

	@Test
	public void del(){
		roleRepository.deleteById(1L);
	}


	@Test
	public void spider(){
		OOSpider.create(Site.me(), new MoviePageModelPipeline(movieRepository), MovieSpider.class).addUrl("https://www.dytt8.net/html/gndy/dyzz/list_23_1.html").thread(8).run();
	}

	@Test
	public void spider1() {
		MoviePageProcessor moviePageProcessor = new MoviePageProcessor();
		moviePageProcessor.setHelperUrl("https://www.dytt8.net/html/gndy/dyzz/list_23_\\d+.html");
		moviePageProcessor.setTargetUrl("https://www.dytt8.net/html/gndy/dyzz/20190610/\\d+.html");
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

	@Test
	public void saveType(){
		CollectType collectType = new CollectType();
		collectType.setName("常用");
		collectType.setDescription("常用书签");
		Label label = new Label();
		label.setIcon("a");
		label.setTitle("测试");
		label.setCollectTypeId(1);//无效的设置，会被覆盖
		collectType.setLabels(new ArrayList<Label>(){{
			add(label);
		}});
		collectTypeRepository.save(collectType);
	}

	@Test
	public void testMovieCount(){
		List<Object> movieCount = movieRepository.findMovieCount("2019-05-05");
		Object json = JSONObject.toJSON(movieCount);
		System.out.println(json);
	}
}
