package com.byc.spider;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.regex.Pattern;

/**
 * Created by baiyc
 * 2019/6/5/005 17:44
 * Description：
 */
@Data
public class MoviePageProcessor implements PageProcessor {

    private String targetUrl;
    private String helperUrl;

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        Pattern compile = Pattern.compile(targetUrl);
        if(compile.matcher(page.getRequest().getUrl()).find()){
            String name = page.getHtml().xpath("//div[@class=bd3r]//div[@class=title_all]//font/text()").toString();
            String publish_time = page.getHtml().regex("(?<=发布时间：)\\S+").toString();
            String cover = page.getHtml().xpath("//div[@id=Zoom]//p//img[1]/@src").toString();
            String icon1 = page.getHtml().xpath("//div[@id=Zoom]//p//img[2]/@src").toString();
            String icon2 = page.getHtml().xpath("//div[@id=Zoom]//p//img[3]/@src").toString();
            String icon3 = page.getHtml().xpath("//div[@id=Zoom]//p//img[4]/@src").toString();
            String icon4 = page.getHtml().xpath("//div[@id=Zoom]//p//img[5]/@src").toString();
            String alias = page.getHtml().regex("(?<=◎译　　名　)[^ <]*").toString();
            String language = page.getHtml().regex("(?<=◎语　　言　)[^ <]*").toString();
            String douban = page.getHtml().regex("(?<=◎豆瓣评分　)[^ <]*").toString();
            String show_time = page.getHtml().regex("(?<=◎上映日期　)[^ <]*").toString();
            String screen = page.getHtml().regex("(?<=◎视频尺寸　)(.*?)(?=( )*(<br>)+◎)").toString();
            String director = page.getHtml().regex("(?<=◎导　　演　)(.*?)(?=(<br>)+◎)").toString();
            String stars = page.getHtml().regex("(?<=◎主　　演　)(.*?)(?=(<br>)+◎)").toString();
            String tag = page.getHtml().regex("(?<=◎标　　签　)(.*?)(?=(<br>)+◎)").toString();
            String description = page.getHtml().regex("(?<=◎简　　介 ).*?(?=<img|<strong)").toString();
            String source = page.getHtml().xpath("//div[@id=Zoom]//tbody//a/text()").toString();
            page.putField("name",name);
            page.putField("publish_time",publish_time);
            page.putField("cover",cover);
            String icons = icon1+ (StringUtils.isEmpty(icon2)?"":"|"+icon2)+(StringUtils.isEmpty(icon3)?"":"|"+icon3)+(StringUtils.isEmpty(icon4)?"":"|"+icon4);
            page.putField("icons",icons);
            page.putField("alias",alias);
            page.putField("language",language);
            page.putField("douban",douban);
            page.putField("show_time",show_time);
            page.putField("screen",screen);
            page.putField("director",director);
            page.putField("stars",stars);
            page.putField("tag",tag);
            page.putField("description",description);
            page.putField("source",source);
        }

        page.addTargetRequests(page.getHtml().links().regex(targetUrl).all());
        page.addTargetRequests(page.getHtml().links().regex(helperUrl).all());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
