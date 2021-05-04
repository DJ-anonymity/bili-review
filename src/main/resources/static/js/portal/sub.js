/*
* 订阅模块js
* */
const USER = "user";
const MEDIA = "media";
const TYPE_USER = 1;
const TYPE_MEDIA = 0;
const SUB_FOLLOW = 1;
const SUB_CANCEL = 0;
let user = getLoginUser();
checkQQ();

$(document).ready(function(){
    //给导航栏绑定点击事件
    $(".nav-item").click(function () {
        console.log(this);
        window.location.href = "#"+this.getAttribute("data-target");
        getSubList();
        //设置当前按钮为选中状态
        $(".nav-item").removeClass("router-link-exact-active");
        $(".nav-item").removeClass("router-link-active");
        this.classList.add("router-link-exact-active");
        this.classList.add("router-link-active");
    });
    $("#search-btn").click(function () {
        search();
    });
    //获取内容列表
    getSubList();
});

/**
 * 检查该账号是否已经绑定qq
 */
function checkQQ() {
    console.log(user);
    if (user.qq == null || user.qq == ''){
        alert("不绑定QQ无法使用该功能");
        window.location.href = 'userUpdate.html';
    }
}

/**
 * 获取订阅内容
 */
function getSubList() {
    let currentType = getDataFromUrl();
    let url = '/bili/sub/list';
    if (currentType == MEDIA){
        url = url + "?type=0"
    } else {
        url = url + "?type=1"
    }

    $.ajax ({
        url: url,
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            if (data.status != 0){
                alert(data.msg);
            }

            let content ='';
            //数据集合
            let list = data.data.list;
            if (currentType == MEDIA){
                setMediaContent(list);
            } else {
                setUpContent(list);
            }

        }
    })
}

//把up订阅内容拼接成html
function setUpContent(list) {
    let content = '';
    for (var i = 0;i < list.length;i++){
        content+=
            '<div data-v-4c5d0db0="" data-v-da17fbc8="" class="favourite-card p-relative f-clear border-box dp-i-block v-top" tags="">\n' +
            '    <div data-v-4c5d0db0="" class="anchor-info clear-float">\n' +
            '        <a data-v-4c5d0db0="" href="//space.bilibili.com/'+list[i].fid+'" target="_blank"\n' +
            '           class="f-left">\n' +
            '            <div data-v-4c5d0db0="" title="'+list[i].name+'"\n' +
            '                 class="anchor-avatar over-hidden b-circle bg-cover"\n' +
            '                 style="background-image: url('+list[i].cover+');">\n' +
            '            </div>\n' +
            '        </a>\n' +
            '        <div data-v-4c5d0db0="" class="anchor-follow f-left">\n' +
            '            <div data-v-4c5d0db0="">\n' +
            '                <a data-v-4c5d0db0="" href="//space.bilibili.com/'+list[i].fid+'" target="_blank" class="anchor-name">\n' +
            '                 ' +list[i].name+
            '                </a>\n' +
            '            </div>\n' +
            '            <div data-v-4c5d0db0="">\n' +
            '                <button data-v-25137da1="" data-v-4c5d0db0=""\n' +
            '                        class="bl-button attend-btn pointer ts-dot-2 bl-button--primary bl-button--size btn-default"\n' +
            '                        onclick="modifySub('+list[i].fid+','+TYPE_USER+')">\n' +
            '                    取消关注\n' +
            '                </button>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '    </div>\n' +
            '</div>';
    }

    $("#sub-content").html(content);
}
//把media订阅内容拼接成html
function setMediaContent(list) {
    let content = '';
    for (var i = 0;i < list.length;i++){
        content+=
            '<div data-v-4c5d0db0="" data-v-da17fbc8="" class="favourite-card p-relative f-clear border-box dp-i-block v-top" tags="">\n' +
            '    <div data-v-4c5d0db0="" class="anchor-info clear-float" style="height: 160px">\n' +
            '        <a data-v-4c5d0db0="" href="https://www.bilibili.com/bangumi/media/md'+list[i].fid+'" target="_blank" class="f-left">\n' +
            '            <div data-v-4c5d0db0="" title="'+list[i].name+'"\n' +
            '                 class="media-avatar over-hidden bg-cover"\n' +
            '                 style="background-image: url('+list[i].cover+');"></div>\n' +
            '        </a>\n' +
            '        <div data-v-4c5d0db0="" class="anchor-follow f-left">\n' +
            '            <div data-v-4c5d0db0="">\n' +
            '                <a data-v-4c5d0db0="" href="https://www.bilibili.com/bangumi/media/md'+list[i].fid+'" target="_blank" class="media-name">\n' +
            '                    <div style="font-weight: 400;color: #222;line-height: 24px;font-size: 18px;">\n' +
            '                       ' + list[i].name +
            '                    </div>\n' +
            '                    <div class="description">\n' +
            '                        变形兄弟系列第三季来了， 这⼀次，B站将自掏腰包，“大方”解决第三季的全部制作费用。但世界上没有免费的午餐，第三季的制作费将由兄弟们自行偿还。兄弟们需要通过八轮破圈任务，从游戏中获得单人2000w的B...\n' +
            '                    </div>\n' +
            '                </a>\n' +
            '            </div>\n' +
            '            <div data-v-4c5d0db0="">\n' +
            '                <button data-v-25137da1="" data-v-4c5d0db0=""\n' +
            '                        class="bl-button attend-btn pointer ts-dot-2 bl-button--primary bl-button--size btn-default" style="margin-top: 2px"\n' +
            '                        onclick="modifySub('+list[i].fid+','+TYPE_MEDIA+')">\n' +
            '                    取消关注\n' +
            '                </button>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '    </div>\n' +
            '</div>'
    }
    $("#sub-content").html(content);
}

//搜索up/media
function search() {
    let type;
    let id;
    let url;
    let kw = $("#search-keyword").val().trim();

    //分析到底是
    if (kw.indexOf("space.bilibili.com") != -1){
        type = TYPE_USER;
        url = "/bili/portal/user/bili/";
        id = getUrlLast(kw);
    } else if (kw.indexOf("www.bilibili.com/bangumi/media/") != -1){
        type = TYPE_MEDIA;
        url = "/bili/portal/animation/";
        id = getUrlLast(kw).substr(2);
    } else {
        alert("请输入有效地址");
        return;
    }

    if (id == null){
        alert("请输入有效地址");
        return;
    }

    //发起ajax请求
    $.ajax ({
        url: url + id,
        type: "get",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            if (data.status == 0) {
                let id;
                let img;
                let name;
                let href;
                let followBtn;
                let is_follow = data.data.is_follow;

                if (type == TYPE_MEDIA){
                    id = data.data.media_id;
                    img = data.data.cover;
                    name = data.data.title;
                    href = "https://www.bilibili.com/bangumi/media/md"+id;
                } else {
                    id = data.data.mid;
                    img = data.data.avatar;
                    name = data.data.uname;
                    href = "https://space.bilibili.com/space.bilibili.com/"+id;
                }

                //已关注则显示取关按钮
                if (is_follow){
                    followBtn = '<button class="btn-follow pointer btn-default" onclick="modifySub('+id+','+type+')">取消关注</button>\n';
                } else {
                    console.log(id, type)
                    followBtn = '<button class="btn-follow pointer" onclick="modifySub('+id+','+type+')">关注</button>\n' ;
                }

                var content =
                   '   <div class="search-result-card">\n' +
                   '       <div class="info">\n' +
                   '           <a class="f-left" href="'+href+'">\n' +
                   '               <img src="'+img+'">\n' +
                   '           </a>\n' +
                   '           <div class="follow f-left">\n' +
                   '               <div>\n' +
                   '                   <a class="ctx-name v-top" href="'+href+'">\n' +
                   '                       '+name+'\n' +
                   '                   </a>\n' +
                   '               </div>\n' +
                   '               <div>\n' +
                   '                      '+followBtn+
                   '               </div>\n' +
                   '           </div>\n' +
                   '       </div>\n' +
                   '   </div>';

                $("#search-result").html(content);
            } else {
                alert(data.msg);
            }
        }
    });
}

//修改订阅状态
function modifySub(id, type) {
    console.log(id+"--"+type)
    if (isBlank(id) || isBlank(type)){
        return;
    }

    let event = window.event;
    let status;
    //通过按钮是默认状态 则本次操作是取关0 否则是关注1
    if (event.target.classList.contains("btn-default")){
        status = SUB_CANCEL;
    } else {
        status = SUB_FOLLOW;
    }

    //发起ajax
    $.ajax ({
        url: "/bili/sub/modify",
        type: "post",
        //发送的数据
        data: JSON.stringify({"fid":id, "type":type, "status":status}),
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            if (data.status == 0){
                if (status == SUB_FOLLOW){
                    alert("关注成功");
                    //默认为取关按钮
                    event.target.classList.add("btn-default");
                    event.target.innerHTML = "取消关注";
                } else {
                    alert("取消关注成功");
                    //默认为取关按钮
                    event.target.classList.remove("btn-default");
                    event.target.innerHTML = "关注";
                }
            } else {
                alert(data.msg);
            }
        }
    });
}
