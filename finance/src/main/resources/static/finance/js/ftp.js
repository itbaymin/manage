/**---ftp文件上传---**/
function uploadFile() {
    var formData = new FormData();
    formData.append("file",$("#file")[0].files[0]);
    formData.append("folder",$("input[name='folder']").val());
    formData.append("fileName",$("input[name='fileName']").val());
    $.ajax({
        url : _ctx+"uploadFile",
        type : 'POST',
        data : formData,
        processData : false,
        contentType : false,
        beforeSend:function(){
            $("#submnitBtn").attr("disabled",true);
            $(".fa-spinner").show();
        },
        success:function (data) {
            if(data.code==200){
                fullFiles(_dir);
            }else{
                alert("上传失败")
            }
        },
        error:function () {
            alert("上传失败");
        },
        complete:function () {
            $("#submnitBtn").attr("disabled",false);
            $(".fa-spinner").hide();
        }
    });
}

var _dir;
/**---ftp文件列表---**/
$(function () {
    fullFiles("/big");
})
//填充文件显示区
function fullFiles(dir) {
    _dir = dir;
    var menu = "",url = "";
    var split = dir.split("/");
    for(var i=0;i<split.length;i++){
        if(split[i]) {
            url += "/"+split[i];
            menu += "<li class='breadcrumb-item active'><a href='javascript:;' onclick='fullFiles(\"" + url + "\")'>" + split[i] + "</a></li>";
        }
    }
    $(".breadcrumb.ftp-menu").html(menu);
    $("input[name='folder']").val(dir);
    $.post(_ctx+"finance/getFTPFiles",{dir:dir},function (data) {
        if(data.code!=200){
            alert("获取文件列表失败");
        }else{
            var files = data.data;
            var str = "";
            if(files.length>0){
                for (let file of files){
                    str += "<tr>";
                    str += "     <td style='text-align: center'>";
                    if(file.icon=='fa-folder')
                        str += "        <span class='fa "+file.icon+"' title='打开' onclick='fullFiles(\""+dir+"/"+file.name+"\")' style='cursor: pointer'></span>";
                    else if(file.icon=="fa-file-audio-o")
                        str += "        <span class='fa "+file.icon+"' title='播放' onclick='addMusic(\""+dir+"\",\""+file.name+"\")' style='cursor: pointer'></span>";
                    else if(file.icon=="fa-file-pdf-o")
                        str += "        <a title='预览' href='"+_ctx+"finance/showPDF?path="+dir+"&name="+file.name+"'><span class='fa "+file.icon+"'></span></a>";
                    else
                        str += "        <span class='fa "+file.icon+"'></span>";
                    str += "     </td>";
                    str += "     <td>"+file.type+"</td>";
                    if(file.type=='文件')
                        str += "     <td><a title='下载' href='"+_ctx+"finance/downloadFTPFile?path="+dir+"&name="+file.name+"'>"+file.name+"</a></td>";
                    else
                        str += "     <td title='打开' style='cursor: pointer' onclick='fullFiles(\""+dir+"/"+file.name+"\")'>"+file.name+"</td>";
                    str += "     <td>"+(file.size/1024/1024).toFixed(2)+"MB</td>";
                    str += "     <td><button class='btn hidden-sm-down btn-danger btn-sm fa fa-trash-o' onclick='delFile(\""+dir+"\",\""+file.name+"\",\""+file.type+"\")'>&nbsp;删除</button></td>";
                    str += "</tr>";
                }
            }else {
                str = "<tr><td colspan='6'></td>无数据</td></tr>";
            }
            $("tbody.fileList").html(str);
        }
    })
}


/**---ftp文件列表功能---**/
//删除
function delFile(path,name,type){
    $.post(_ctx+"finance/delFTPFile",{path:path,name:name,type:type},function (data) {
        if(data.code==200){
            fullFiles(path);
            alert("成功");
        }else{
            alert("失败");
        }
    })
}