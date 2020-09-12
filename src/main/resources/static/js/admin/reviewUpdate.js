$(document).ready(function () {
    getAnimationDetail();
    $("#pull-long-review").click(function () {
        pullLongReview();
    });
    $("#pull-short-review").click(function () {
        pullShortReview();
    });
})

//获取url中的media_id
var media_id = getQueryString("media_id");
//获取该剧的详细情况
function getAnimationDetail() {
    $.ajax({
        url: "/bili/portal/animation/"+media_id,
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            $("#animation-img").attr("src", data.data.cover);
            $("#animation-href").attr("href", data.data.share_url);
            $("#animation-title").html(data.data.title);
        }
    })
    //获取评论的数量
    $.ajax({
        url: "/bili/portal/animation/"+media_id+"/review/quantity/",
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            $("#all-long-review-num").html(data.data.long_review_total);
            $("#all-short-review-num").html(data.data.short_review_total);
            $("#finished-long-review-num").html(data.data.long_review_finished);
            $("#finished-short-review-num").html(data.data.short_review_finished);
        }
    })
}
/*更新长评*/
function pullLongReview() {
    $.ajax({
        url: "/bili/admin/review/long/pullNew?media_id="+media_id,
        type: "post",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        beforeSend: function(){
            //页面加载效果
            $(".loading").css("display","block");
        },
        success: function (data) {
            $(".loading").css("display","none");
            if (data.status == 0){
                alert("拉取完成");
                window.location.reload();
            } else {
                alert(data.msg)
            }
        },
        error: function () {
            $(".loading").css("display","none");
            alert("服务器出现问题，请及时修复");
        }
    })
}
/*拉取短评，短评较多，需要分布拉取*/
function pullShortReview() {
    /*获取持久化情况*/
    var finishedShortReviewNum = $("#finished-short-review-num").html();
    var totalShortReviewNum = $("#all-short-review-num").html();

    var remainShortReview = totalShortReviewNum - finishedShortReviewNum;
    var times = Math.ceil(remainShortReview/3000);
    if (!confirm("该视频需要更新的短评数量为"+remainShortReview+"预计需要"+times+"分钟")){
        return;
    }
    $.ajax({
        url: "/bili/admin/review/short/pullNew?media_id="+media_id,
        type: "post",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        beforeSend: function(){
            //页面加载效果
            $(".loading").css("display","block");
        },
        success: function (data) {
            $(".loading").css("display","none");
            if (data.status == 0){
                alert("拉取完成");
                window.location.reload();
            } else {
                alert(data.msg)
            }
        },
        error: function () {
            $(".loading").css("display","none");
            alert("服务器出现问题，请及时修复");
        }
    })
}