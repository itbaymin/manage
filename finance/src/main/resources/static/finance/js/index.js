$(function () {
    fullMovies(10);
    $("#movieSize").change(function () {
        fullMovies($(this).val());
    });
    //复制链接
    var clipboard = new ClipboardJS('.clipboard', {
        text: function(trigger) {
            return trigger.getAttribute('target');
        }
    });
    clipboard.on('success', function(e) {
        alert("复制成功，打开迅雷即可下载！");
    });
})
//获取电影列表
function getMovie(page,size) {
    $.post(_ctx+"finance/getMovie",{pageIndex:page,pageSize:size,name:$("input[name='keyword']").val()},function (data) {
        if(data.code!=200){
            alert("获取电影数据失败");
        }else {
            var movies = data.data.content;
            var str = "";
            if(movies.length>0) {
                for (let movie of movies) {
                    str += "<tr>";
                    str += "   <td style='width:50px;'><img src='" + movie.cover + "' onerror='javascript:this.src=\"" + _ctx + "favicon.ico\"' class='round'/></td>";
                    str += "   <td>";
                    str += "   <h6><a href='"+_ctx+"finance/movie/"+movie.id+"'>" + movie.name + "</a></h6><small class='text-muted'>" + movie.show_time + "</small>";
                    str += "</td>";
                    str += "<td>" + movie.publish_time + "</td>";
                    str += "<td>" + movie.language + "</td>";
                    str += "<td>" + movie.screen + "</td>";
                    str += "<td><button class='btn hidden-sm-down btn-success clipboard' target='" + movie.source + "'>复制下载</button></td>";
                    str += "</tr>";
                }
            }else{
                str = "<tr><td colspan='6'></td>无数据</td></tr>";
            }
            $("#movies tbody").html(str);
        }
    })
}
//初始化电影页码下拉框
function initPageSelect() {
    $.post(_ctx+"finance/getMovie",{pageIndex:1,pageSize:$("#movieSize").val(),name:$("input[name='keyword']").val()},function (data) {
        var str = "";
        if(data.code!=200){
            str += "<option value='1'>第1页</option>";
        }else {
            for (var i = 1; i <= data.data.totalPages; i++) {
                str += "<option value='" + i + "'>第" + i + "页</option>";
            }
        }
        $("#moviePage").html(str);
        $("#moviePage").change(function () {
            getMovie($(this).val(), $("#movieSize").val());
        });
    });
}
//填充电影列表及下拉框
function fullMovies(size){
    getMovie(1,size);
    initPageSelect();
}
