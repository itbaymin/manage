Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}
//判段日期
function getTime(n){
    var now=new Date();
    var year=now.getFullYear();
//因为月份是从0开始的,所以获取这个月的月份数要加1才行
    var month=now.getMonth()+1;
    var date=now.getDate();
    var day=now.getDay();
//判断是否为周日,如果不是的话,就让今天的day-1(例如星期二就是2-1)
    if(day!==0){
        n=n+(day-1);
    }
    else{
        n=n+day;
    }
    if(day){
//这个判断是为了解决跨年的问题
        if(month>1){
            month=month;
        }
//这个判断是为了解决跨年的问题,月份是从0开始的
        else{
            year=year-1;
            month=12;
        }
    }
    now.setDate(now.getDate()-n);
    year=now.getFullYear();
    month=now.getMonth()+1;
    date=now.getDate();
    s=year+""+(month<10?('0'+month):month)+""+(date<10?('0'+date):date)+"";
    return s;
}
var _movieCount = getMovieCount();
$(document).ready(function() {
    dealCount(_movieCount);
    plot();
});
function getMovieCount() {
    var start_time = new Date(new Date().getTime()-30*24*60*60*1000).format("yyyy-MM-dd");
    var res = [];
    $.ajaxSettings.async = false;
    $.post(_ctx+"finance/getMovieCount",{time:start_time},function (data) {
        if(data.code==200) {
            res = data.data;
        }
    })
    $.ajaxSettings.async = true;
    return res;
}
function GetPercent(num, total) {
    num = parseFloat(num);
    total = parseFloat(total);
    if (isNaN(num) || isNaN(total)) {
        return "-";
    }
    return total <= 0 ? "0%" : (Math.round(num / total * 10000) / 100.00)+"%";
}
//处理统计
function dealCount(data) {
    var now = new Date();
    var yesterday = getTime(1);
    var last_begin = getTime(now.getDay()+7);
    var last_end = getTime(now.getDay());
    var _now = 0,_yesterday = 0,_last = 0,_this = 0;
    var daily,weekly;
    for(let point of data){
        var record = new Date(point[0]).format("yyyyMMdd");
        if(record>last_begin && record<=last_end){
            _last += point[1];
        }
        if(record>last_end){
            _this += point[1];
        }
        if(record==yesterday){
            _yesterday = point[1];
        }
        if(record==now.format("yyyyMMdd")){
            _now = point[1];
        }
    }
    daily = GetPercent(_now,_yesterday);
    weekly = GetPercent(_this,_last);
    $("#day-count").text(_now);
    $("#day-percent").text(daily);
    $("#day-progress").css("width",daily);
    $("#week-count").text(_this);
    $("#week-percent").text(weekly);
    $("#week-progress").css("width",weekly);
}
function plot() {
    var sin = _movieCount;
    var options = {
        series: {
            lines: {
                show: true
            },
            points: {
                show: true
            }
        },
        grid: {
            hoverable: true //IMPORTANT! this is needed for tooltip to work
        },
        yaxis: {
            min: 0,
            max: 10
        },
        xaxis:{
            tickFormatter: function (axis) {
                return new Date(axis).format("yyyy/MM/dd");
            }
        },
        colors: ["#009efb"],
        grid: {
            color: "#AFAFAF",
            hoverable: true,
            borderWidth: 0
        },
        tooltip: true,
        tooltipOpts: {
            content: "%x%s%y",
            defaultTheme: false,
            shifts: {
                x: -60,
                y: 25
            }
        }
    };
    var plotObj = $.plot($("#flot-line-chart"), [{
        data: sin,
        label: "发布电影数：",
    }], options);
}