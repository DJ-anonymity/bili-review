const all = 64;
const ONLY_FANS = 65;
const IS_ELEC = 66;
const SORT_TIME_DESC = 0;
const SORT_LIKE_DESC = 1;
const SORT_REPLY_DESC = 2;
let user = getLoginUser();
checkBindBili();
/**
 * 检查该账号是否已经绑定B站
 */
function checkBindBili() {
    console.log(user);
    if (user.mid == null || user.mid == ''){
        alert("不绑定B站账号无法使用该功能");
        window.location.href = 'userUpdate.html';
    }
}

$(document).ready(function () {
    //加载评论内容
    showComment();
    //给关系选择绑定按钮事件
    $(".bcc-select-input-wrap").click(function (event) {
        $(this).next(".bcc-select-list-wrap").css("display","block");
        event.stopPropagation();
    });
    $("body").click(function () {
        $(".bcc-select-list-wrap").css("display","none");
    });
    //关系选择栏效果实现
    $(".bcc-select-list-wrap li").click(function () {
        $(this).siblings().removeClass("selected");
        $(this).addClass("selected");
        let inSelect = $(this).children("span").html();
        $("#relation").val(inSelect);

        showComment();
    });
    //给排序方式增加点击事件
    $(".operate-txt").click(function () {
        $(this).siblings().removeClass("active");
        $(this).addClass("active");
        showComment();
    });
    //搜索功能
    $("#search").click(function () {
        showComment();
    })
});


function showComment() {
    let url =  "/bili/portal/comment/list";
    //排序条件
    let sortType = $(".operate-txt.active").attr("data-sort-type-id");
    url+="?sort="+sortType;

    //增加筛选条件
    let condition = $(".bcc-option.selected").attr("data-reporter-id");
    if (condition == IS_ELEC){
        url+= "&is_elec=1";
    } else if (condition == ONLY_FANS){
        url+= "&relation=2";
    }

    //搜索功能
    let keyword = $("#search-keyword").val();
    if (keyword != null && keyword != ''){
        url+= "&keyword="+keyword;
    }

    $.ajax({
        url: url,
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            let content = "";
            let cList = data.data;
            for (var i = 0;i < cList.length;i++){
                content+=
                    '<div data-v-76b62926="" class="comment-list-item">\n' +
                    '    <a href="//space.bilibili.com/'+cList[i].mid+'" mid="'+cList[i].mid+'" target="_blank" card="键神不请自来" class="user-avatar" data-reporter-id="171">\n' +
                    '        <img src="'+cList[i].uface+'">\n' +
                    '    </a>\n' +
                    '    <div class="article-wrap">\n' +
                    '        <a href="//www.bilibili.com/video/'+cList[i].bvid+'" target="_blank" class="pic" data-reporter-id="172">\n' +
                    '            <img src="'+cList[i].cover+'">\n' +
                    '        </a>\n' +
                    '        <a href="" class="title ellipsis">\n' +
                    '            <span class="name">'+cList[i].title+'</span>\n' +
                    '            <span class="show-all">该视频全部评论</span></a>\n' +
                    '    </div>\n' +
                    '    <div class="ci-title">\n';
                if (cList[i].relation == 2){
                    content+= '<span class="relation-label"">粉丝</span>\n';
                }
                if (cList[i].is_elec == 1){
                    content+= '<span class="relation-label"">已充电</span>\n';
                }
                content+=
                    '        <a href="//space.bilibili.com/'+cList[i].mid+'" card="'+cList[i].replier+'" target="_blank">'+cList[i].replier+'</a>\n' +
                    '    </div>\n' +
                    '    <a href="'+cList[i].url+'" target="_blank">\n' +
                    '        <div class="ci-content">'+ cList[i].message +'</div>\n' +
                    '    </a>\n' +
                    '    <div class="ci-action">\n' +
                    '        <span class="date">'+cList[i].ctime+'</span>\n' +
                    '        <span class="like action">\n' +
                    '            <a data-reporter-id="173" style="color: rgb(0, 161, 214);">\n' +
                    '               <i class="fa fa-thumbs-up"></i>\n' +
                    '               <span class="num">'+cList[i].like+'</span>\n' +
                    '            </a>\n' +
                    '        </span>\n' +
                    '    </div>\n' +
                    '</div>';
            }

            $("#comment-list").html(content);
        }
    });
}
