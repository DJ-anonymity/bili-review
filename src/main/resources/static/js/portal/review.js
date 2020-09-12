const LONG_REVIEW = "long";
const SHORT_REVIEW = "short";
const DEFAULT_CONDITION = null;//默认筛选条件
const Sort = {
    DEFAULT : 0,
    MTIME_ASC : 1,
    MTIME_DESC : 2,
    LIKES_ASC : 3
}
var currentReviewType = getDataFromUrl();//从url#判断现在是短评还是长评

function sortTypeTransform(sortType) {
    if (sortType == Sort.MTIME_ASC){
        return "最旧";
    } else if (sortType == Sort.MTIME_DESC){
        return "最新";
    } else if (sortType == Sort.LIKES_ASC){
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
    getReviewQuantity();//获取评论数量
    //观看长评
    $("#btn-to-long").click(function () {
        clean();
        currentReviewType = LONG_REVIEW;
        window.location.href = "#"+LONG_REVIEW;
        getReview();
        $("#btn-to-short").removeAttr("class");
        this.className = "on";
    })
    //观看短评
    $("#btn-to-short").click(function () {
        clean();
        currentReviewType = SHORT_REVIEW;
        window.location.href = "#"+SHORT_REVIEW;
        getReview();
        $("#btn-to-long").removeAttr("class");
        this.className = "on";

    })
    //给搜索绑定点击事件
    $("#btn-search").click(function () {
        var keyword = $("#search-keyword").val();
        search(keyword);
    });
    //获取评论内容
    if (currentReviewType == null || currentReviewType == '' || currentReviewType == LONG_REVIEW){
        $("#btn-to-long").click();
    } else {
        $("#btn-to-short").click();
    }
    //详细信息页面
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
    //给筛选条件绑定点击事件
    $(".condition-list li").click(function () {
        var conditionType = this.getAttribute("data-condition-type-id");
        $("#condition-type").attr("data-condition-type-id", conditionType);
        $("#condition-type").html(conditionTypeTransform(conditionType));
        getReview(currentReviewType);
    })
    //热词排行榜
    getSearchTrendList();
})

var media_id = getQueryString("media_id");
//获取长评和短评的数量
function getReviewQuantity() {
    $.ajax({
        url: '/bili/portal/animation/'+media_id+'/review/quantity',
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            $("#animation-long-review-quantity").html(data.data.long_review_finished);
            $("#animation-short-review-quantity").html(data.data.short_review_finished)
        }
    })
}
/*获取评论*/
function getReview() {
    var url;
    var sortType = $("#sort-type").attr("data-sort-type-id");
    var condition = $("#condition-type").attr("data-condition-type-id");
    if (currentReviewType == LONG_REVIEW){
        url = "/bili/portal/review/long/list";
        document.getElementById("search-trend-list").style.display = 'none';
    } else {
        url = "/bili/portal/review/short/list";
        document.getElementById("search-trend-list").style.display = 'list-item';
    }
    url+= "?media_id="+media_id+"&sort="+sortType;
    if (condition != null && condition != ''){
        url+="&score="+condition;
    }
    $.ajax({
        url: url,
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            setReviewContent(data.data.list, currentReviewType);
        }
    })
}
//搜索内容
function search(keyword) {
    var url;
    var sortType = $("#sort-type").attr("data-sort-type-id");
    var condition = $("#condition-type").attr("data-condition-type-id");
    if (currentReviewType == LONG_REVIEW){
        url = "/bili/portal/review/long/animation/"+media_id+"/search";
    } else {
        url = "/bili/portal/review/short/animation/"+media_id+"/search";
    }
    if (keyword != null){
        url+= "?keyword="+keyword;
        if (condition != null && condition != ''){
            url+= "&score="+condition+"&sort="+sortType
        } else {
            url+= "&sort="+sortType;
        }
    } else {
        url+= "?sort="+sortType;
    }
    $.ajax({
        url: url,
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            setReviewContent(data.data.list, currentReviewType);
        }
    })
}

function getSearchTrendList() {
    $.ajax({
        url: "/bili/portal/trending/search/keyword/list",
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            var trendListContent = '';
            for (var i = 0;i < data.data.length;i++){
                var keyword = "'"+data.data[i].keyword+"'";
                trendListContent+= '<li onclick="search('+keyword+')">'+data.data[i].keyword+'</li>';
            }
            $("#search-trend-list").html(trendListContent);
        }
    })
}
function userDetail(mid) {
    window.location.href = "https://space.bilibili.com/"+mid;
}

//清空筛选条件和排序方式
function clean() {
    $("#sort-type").attr("data-sort-type-id", Sort.DEFAULT);
    $("#sort-type").html(sortTypeTransform(Sort.DEFAULT));
    $("#condition-type").attr("data-condition-type-id", DEFAULT_CONDITION);
    $("#condition-type").html(conditionTypeTransform(DEFAULT_CONDITION));
}