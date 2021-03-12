//获取url参数的方法
function getQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);//search,查询？后面的参数，并匹配正则
    if(r!=null)
        return  unescape(r[2]);
    return null;
}
//检测是不是数字的正则表达式
function checkIsNum(content) {
    var reg = '^\\d+$';
    var regex = new RegExp(reg);
    return regex.test(content);
}
//取#后面内容的正则表达式
function getDataFromUrl() {
    var reg = '#.*';
    var regex = new RegExp(reg);
    var r = window.location.href.match(regex);
    if(r!=null)
        return  r[0].substr(1);
    return null;
}
//时间戳转变成普通日期
function timestampToTime(timestamp) {
    var date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    /*var Y = date.getFullYear() + '-';*/
    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var D = date.getDate() + ' ';
    /*var h = date.getHours() + ':';
    var m = date.getMinutes() + ':';
    var s = date.getSeconds();*/
    return M + D;
}

/**
 * 按钮定时不能使用 并且附带倒计时
 * @param btn
 * @param time
 */
function btnInvalidate(btn, time){
    let content = btn.innerHTML;
    let timeout = time/1000;

    btn.disabled = true;
    let interval = window.setInterval(function (){
        timeout--;
        btn.innerHTML = "<span>"+timeout+"</span>";
    }, 1000);

    window.setTimeout(function (){
        btn.disabled = false;
        btn.innerHTML = content;
        window.clearInterval(interval);
    }, time);
}

function setReviewContent(list, reviewType){
    var content = "";
    var detailContent = "";
    var dataContent = "";
    for (var i = 0;i < list.length; i++){
        //点赞数
        var starContent = "";
        for (var j = 0;j < list[i].score/2;j++){
            starContent+= '<i class="icon-start fa fa-star"></i>';
        }
        var jumpReviewDetail = "window.location.href = '"+list[i].url+"'";
        if (reviewType == LONG_REVIEW){
            detailContent = '<div class="review-detail" onclick="'+jumpReviewDetail+'">' +
                '<div class="review-detail-title">' +
                '<span>' +
                list[i].title+
                '</span>' +
                '</div>' +
                '<div class="review-detail-content">' +
                list[i].content+
                '</div>' +
                '</div>' ;
            dataContent =   '<i class="fa fa-thumbs-up"></i>' +
                list[i].stat.likes+'&nbsp;'+
                '<i class="fa fa-comment" ></i>\n' +
                list[i].stat.reply;
        } else {
            detailContent = '<div class="review-detail">' +
                '<div class="review-detail-content">' +
                list[i].content+
                '</div>' +
                '</div>' ;
            dataContent =   '<i class="fa fa-thumbs-up"></i>' +
                list[i].stat.likes;
        }
        //当动漫标题不为空时显示标题（通过mid查询的时候才显示，其他时候不显示）
        var animationTitle = '';
        if (list[i].animation!= null && list[i].animation.title != null){
            animationTitle = list[i].animation.title+": "
        }
        //作者信息
        content+= '<li>' +
            '<div class="review-author-info">' +
            '<div class="review-author-face" onclick="userDetail('+list[i].mid+')">' +
            '<img src="'+list[i].author.avatar+'" lazy="loaded">' +
            '</div>' +
            '<div class="review-author-name is-vip" onclick="userDetail('+list[i].mid+')">' +
            list[i].author.uname+
            '</div>' +
            '<div class="review-author-star">' +
            '<span class="review-stars">' +
            starContent+
            '</span>' +
            '</div>' +
            '<div class="review-author-time">' +
                timestampToTime(list[i].mtime)+
            '</div>' +
            '<div class="review-author-time" style="margin-right: 100px">\n' +
                animationTitle+list[i].progress+
            '</div>'+
            '</div>' +
            detailContent+
            '<div class="review-data">' +
            '<div class="review-data-like">' +
            dataContent+
            '</div>' +
            '</div>' +
            '<div class="review-control">' +
            '</div>' +
            '</li>';
    }
    $("#review-content").html(content);
}