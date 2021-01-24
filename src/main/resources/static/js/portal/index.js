$(document).ready(function () {
    getAnimationList();
    getSearchTrend();
    $("#btn-get-followers").click(function () {
        getFollowers();
    })
})

var pageNum = getQueryString("pageNum");
if (pageNum == null){
    pageNum = 1;
}

function getAnimationList() {
    $.ajax({
        url: "/bili/portal/animation/list?pageSize=8&pageNum="+pageNum,
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            setAnimationContent(data.data.list);
            //设置分页
            var pageInfo = '';
            if (data.data.pageNum != 1){
                pageInfo+='<li onclick="back()">上一页</li>';
            } 
            for (var i = 1;i <= data.data.pages; i++){
                if (pageNum == i){
                    pageInfo+='<li class="on" onclick="change(this)">'+i+'</li>';
                } else {
                    pageInfo+='<li onclick="change(this)">'+i+'</li>';
                }
            }
            if (data.data.pageNum != data.data.pages){
                pageInfo+='<li onclick="next()">下一页</li>'
            }
            $("#page-info-list").html(pageInfo);
        }
    })
}
//分页信息
function next() {
    pageNum = parseInt(pageNum)+1;
    window.location.href = "index.html?pageNum="+pageNum;
}
function change(e) {
    pageNum = e.innerHTML;
    window.location.href = "index.html?pageNum="+pageNum;
}
function back() {
    pageNum = parseInt(pageNum)-1;
    window.location.href = "index.html?pageNum="+pageNum;
}
function setAnimationContent(list) {
    var animationContent = '';
    for (var i = 0;i < list.length;i++){
        var animationUrl = "'review.html?media_id="+list[i].media_id+"'";
        animationContent+= '<div class="animation-card" onclick="window.open('+animationUrl+')">\n' +
            '                        <img src="'+list[i].cover+'">\n' +
            '                        <div>'+list[i].title+'</div>\n' +
            '                </div>';
    }
    $("#animation-list").html(animationContent);
}
/*通过用户搜索评论*/
$("#btn-review-search").click(function () {
    var keyword = $("#review-search-keyword").val();
    //var searchType = $("input[type=radio]:checked").val();
    if (!checkIsNum(keyword)){
        alert("只能输入数字");
        return;
    }
    window.open('reviewSearch.html?mid='+keyword);
})
//获取搜索热词排行
function getSearchTrend() {
    $.ajax({
        url: "/bili/portal/trending/search/mid/list",
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            var rankListContent = '';
            for (var i = 0;i < data.data.length;i++) {
                var mid = data.data[i].mid;
                var rank = i+1;
                rankListContent += '<div class="rank-wrap">\n' +
                    '                    <span class="number on">'+rank+'</span>\n' +
                    '                    <a href="reviewSearch.html?mid=' + mid + '"" target="_blank" class="link">\n' +
                    '                        <p title="' + mid + '" class="title">\n' +
                                                mid +
                    '                        </p>\n' +
                    '                    </a>\n' +
                    '                </div>';
            }
            console.log(rankListContent)
            $("#rank-wrap").html(rankListContent);
        }
    })
}
//跳过mid获取粉丝
function getFollowers(){
    var mid = $("#mid").val();
    $.ajax({
        url:'/bili/portal/user/followers?mid='+mid,
        type:'get',
        data:'',
        dataType: "jsonp",
        jsonp: "callback",//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
        jsonpCallback:"__jp5",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以不写这个参数，jQuery会自动为你处理数据
        success: function(data){
            console.log(data);
        },
        error:function(XMLHttpRequest, textStatus, errorThrown){
            console.log("服务器异常");
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
            alert(XMLHttpRequest.responseText);
            alert(textStatus);
            alert(errorThrown);
            console.log(textStatus);
        }
    });
}
function __jp5(result) {
    var data = JSON.stringify(result); //json对象转成字符串
    console.log(data);
}