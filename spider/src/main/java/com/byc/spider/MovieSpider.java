package com.byc.spider;

import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by baiyc
 * 2019/5/31/031 17:30
 * Description：
 */
@TargetUrl("https://www.dytt8.net/html/gndy/dyzz/\\d+/\\d+.html")
@HelpUrl("https://www.dytt8.net/html/gndy/dyzz/list_23_\\d+.html")
public class MovieSpider {

    @ExtractBy("//div[@class=bd3r]//div[@class=title_all]//font/text()")
    private String name;

    @ExtractBy(value = "(?<=发布时间：)\\S+",type = ExtractBy.Type.Regex)
    private String publish_time;

    @ExtractBy("//div[@id=Zoom]//p//img[1]/@src")
    private String cover;

    @ExtractBy(value = "(?<=◎译　　名　)[^ <]*",type = ExtractBy.Type.Regex)
    private String alias;

    @ExtractBy(value = "(?<=◎语　　言　)[^ <]*",type = ExtractBy.Type.Regex)
    private String language;

    @ExtractBy(value = "(?<=◎豆瓣评分　)[^ <]*",type = ExtractBy.Type.Regex)
    private String douban;

    @ExtractBy(value = "(?<=◎上映日期　)[^ <]*",type = ExtractBy.Type.Regex)
    private String show_time;

    @ExtractBy(value = "(?<=◎视频尺寸　)(.*?)(?=( )*(<br>)+◎)",type = ExtractBy.Type.Regex)
    private String screen;

    @ExtractBy(value = "(?<=◎导　　演　)(.*?)(?=(<br>)+◎)",type = ExtractBy.Type.Regex)
    private String director;

    @ExtractBy(value = "(?<=◎主　　演　)(.*?)(?=(<br>)+◎)",type = ExtractBy.Type.Regex)
    private String stars;

    @ExtractBy(value = "(?<=◎标　　签　)(.*?)(?=(<br>)+◎)",type = ExtractBy.Type.Regex)
    private String tag;

    @ExtractBy(value = "(?<=◎简　　介 ).*?(?=<img)",type = ExtractBy.Type.Regex)
    private String description;

    @ExtractBy(value = "//div[@id=Zoom]//p//img[2]/@src")
    private String icon1;

    @ExtractBy(value = "//div[@id=Zoom]//p//img[3]/@src")
    private String icon2;

    @ExtractBy(value = "//div[@id=Zoom]//p//img[4]/@src")
    private String icon3;

    @ExtractBy(value = "//div[@id=Zoom]//p//img[5]/@src")
    private String icon4;

    @ExtractBy("//div[@id=Zoom]//tbody//a/text()")
    private String source;

    public String getIcons(){
        return getIcon1()+getIcon2()+getIcon3()+getIcon4();
    }

    @Override
    public String toString() {
        return "MovieSpider{" +
                "name='" + name + '\'' +
                ", publish_time='" + publish_time + '\'' +
                ", cover='" + cover + '\'' +
                ", alias='" + alias + '\'' +
                ", language='" + language + '\'' +
                ", douban='" + douban + '\'' +
                ", show_time='" + show_time + '\'' +
                ", screen='" + screen + '\'' +
                ", director='" + director + '\'' +
                ", stars='" + stars + '\'' +
                ", tag='" + tag + '\'' +
                ", description='" + description + '\'' +
                ", icon1='" + icon1 + '\'' +
                ", icon2='" + icon2 + '\'' +
                ", icon3='" + icon3 + '\'' +
                ", icon4='" + icon4 + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    public String getName() {
        return StringUtils.isEmpty(name)?"":name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublish_time() {
        return StringUtils.isEmpty(publish_time)?"":publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getCover() {
        return StringUtils.isEmpty(cover)?"":cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAlias() {
        return StringUtils.isEmpty(alias)?"":alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getLanguage() {
        return StringUtils.isEmpty(language)?"":language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDouban() {
        return StringUtils.isEmpty(douban)?"":douban;
    }

    public void setDouban(String douban) {
        this.douban = douban;
    }

    public String getShow_time() {
        return StringUtils.isEmpty(show_time)?"":show_time;
    }

    public void setShow_time(String show_time) {
        this.show_time = show_time;
    }

    public String getScreen() {
        return StringUtils.isEmpty(screen)?"":screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getDirector() {

        if(StringUtils.isEmpty(director))
            return "";
        else
            return director.replaceAll("<[^>]+?>[　]*","|");
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStars() {
        if(StringUtils.isEmpty(stars))
            return "";
        else
            return stars.replaceAll("<[^>]+?>[　]*","|");
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getTag() {
        return StringUtils.isEmpty(tag)?"":tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return StringUtils.isEmpty(description)?"":description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon1() {
        return StringUtils.isEmpty(icon1)?"":icon1;
    }

    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    public String getIcon2() {
        return StringUtils.isEmpty(icon2)?"":"|"+icon2;
    }

    public void setIcon2(String icon2) {
        this.icon2 = icon2;
    }

    public String getIcon3() {
        return StringUtils.isEmpty(icon3)?"":"|"+icon3;
    }

    public void setIcon3(String icon3) {
        this.icon3 = icon3;
    }

    public String getIcon4() {
        return StringUtils.isEmpty(icon4)?"":"|"+icon4;
    }

    public void setIcon4(String icon4) {
        this.icon4 = icon4;
    }

    public String getSource() {
        return StringUtils.isEmpty(source)?"":source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
