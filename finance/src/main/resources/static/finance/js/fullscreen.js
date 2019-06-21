document.getElementById("full-screen").onclick=function(){
    var elem = document.getElementById("content");
    requestFullScreen(elem);
};
// document.getElementById("exit-full").onclick=function(){
//     exitFullscreen();
// };
/*
 全屏显示
 */
function requestFullScreen(element) {
    var requestMethod = element.requestFullScreen || element.webkitRequestFullScreen || element.mozRequestFullScreen || element.msRequestFullScreen;
    requestMethod.call(element);
};
/*
 全屏退出
 */
// function exitFullscreen() {
//     var elem = document;
//     var elemd = document.getElementById("content");
//     if (elem.webkitCancelFullScreen) {
//         elem.webkitCancelFullScreen();
//     } else if (elem.mozCancelFullScreen) {
//         elemd.mozCancelFullScreen();
//     } else if (elem.cancelFullScreen) {
//         elem.cancelFullScreen();
//     } else if (elem.exitFullscreen) {
//         elem.exitFullscreen();
//     } else {
//         //浏览器不支持全屏API或已被禁用
//     };
//
//     elemd.style.height = '500px';
//     elemd.style.width = '700px'
// }