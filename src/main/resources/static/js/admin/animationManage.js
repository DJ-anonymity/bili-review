const NO_FINISHED = 0;
const IS_FINISHED = 1;

$(document).ready(function () {
    getAnimationList();
    $("#btn-animation-add").click(function () {
        addAnimation();
    })
})

function getAnimationList() {
    var url = "/bili/portal/animation/list?persistenceMark=";
    /*获取已完成的*/
    $.ajax({
        url: url+IS_FINISHED,
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            var animationUnfinished = '';
            var reviewUrl = "";
            var list = data.data.list;
            for (var i = 0;i < list.length;i++){
                reviewUrl = "'reviewUpdate.html?media_id="+list[i].media_id+"'";
                animationUnfinished+= '<div class="animation-card" onclick="location.href = '+reviewUrl+'">\n' +
                    '                        <img src="'+list[i].cover+'">\n' +
                    '                        <div>'+list[i].title+'</div>\n' +
                    '                </div>';
            }
            $("#animation-list").html(animationUnfinished);
        }
    })
    /*获取未完成的*/
    $.ajax({
        url: url+NO_FINISHED,
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            var animationFinished = '';
            var reviewUrl = "";
            var list = data.data.list;
            for (var i = 0;i < list.length;i++){
                reviewUrl = "'reviewPull.html?media_id="+list[i].media_id+"'";
                animationFinished+= ' <li onclick="location.href = '+reviewUrl+'">' +
                    '                   <img src="'+list[i].cover+'">\n' +
                    '                   <div>'+list[i].title+'</div>\n' +
                    '                </li>'
            }
            $("#animation-unfinished-list").html(animationFinished);
        }
    })
}

function addAnimation() {
    var media_id = $("#animation-add-id").val();
    if (!checkIsNum(media_id)){
        alert("请输入纯数字");
        return;
    }
    $.ajax({
        url: "/bili/admin/animation/pull?media_id=" + media_id,
        type: "post",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            if (data.status == 0) {
                alert("拉取成功");
                location.reload();
            } else {
                alert(data.msg);
            }
        },
        error: function () {
            alert("服务器出现问题，请及时修复")
        }
    })
}