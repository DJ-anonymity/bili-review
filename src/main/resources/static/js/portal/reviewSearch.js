const LONG_REVIEW = 0;
const SHORT_REVIEW = 1;
var currentReviewType = 0;
var mid = getQueryString("mid");

function sortTypeTransform(sortType) {
    if (sortType == 1){
        return "最旧";
    } else if (sortType == 2){
        return "最新";
    } else if (sortType == 3){
        return "点赞";
    } else {
        return "默认";
    }
};
function conditionTypeTransform(conditionType){
    if (conditionType != null & conditionType != ''){
        return conditionType/2+"星";
    } else {
        return '全部';
    }
}

$(document).ready(function () {
    getReview(SHORT_REVIEW);
    getReviewQty();
    $("#btn-to-long").click(function () {
        getReview(LONG_REVIEW);
        $("#btn-to-short").removeAttr("class");
        this.className = "on";
    })
    $("#btn-to-short").click(function () {
        getReview(SHORT_REVIEW);
        $("#btn-to-long").removeAttr("class");
        this.className = "on";
    })
    $("#btn-to-detail").click(function () {
        var media_id = getQueryString("media_id");
        window.location.href = "https://www.bilibili.com/bangumi/media/md"+media_id;
    })
    //给排序绑定点击事件
    $(".sort-list li").click(function () {
        var sortType = this.getAttribute("data-sort-type-id");
        $("#sort-type").attr("data-sort-type-id", sortType);
        $("#sort-type").html(sortTypeTransform(sortType));
        getReview(currentReviewType);
    })
    /*//给筛选条件绑定点击事件
    $(".condition-list li").click(function () {
        var conditionType = this.getAttribute("data-condition-type-id");
        $("#condition-type").attr("data-condition-type-id", conditionType);
        $("#condition-type").html(conditionTypeTransform(conditionType));
        getReview(currentReviewType);
    })*/
    //给搜索绑定点击事件
    $("#btn-search").click(function () {
       var keyword = $("#search-keyword").val();
       if (checkIsNum(keyword)){
           window.location.href = 'reviewSearch.html?mid='+keyword;
       } else {
           alert("请输入数字");
       }
    });
})
//获取评价数量
function getReviewQty() {
    $.ajax({
        url: '/bili/portal/user/'+mid+'/review/quantity',
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            $("#user-short-review-quantity").html(data.data.shortReviewQuantity);
            $("#user-long-review-quantity").html(data.data.longReviewQuantity);
        }
    })
}
//获取评论
function getReview(reviewType) {
    currentReviewType = reviewType;
    var url;
    var sortType = $("#sort-type").attr("data-sort-type-id");
   /* var condition = $("#condition-type").attr("data-condition-type-id");*/
    if (reviewType == LONG_REVIEW){
        url = "/bili/portal/review/long/search?mid="+mid+"&sort="+sortType;
    } else {
        url = "/bili/portal/review/short/search?mid="+mid+"&sort="+sortType;
    }
    //获取评论
    $.ajax({
        url: url,
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            setReviewContent(data.data.list, reviewType);
        }
    })
}


function userDetail(mid) {
    window.location.href = "https://space.bilibili.com/"+mid;
}
