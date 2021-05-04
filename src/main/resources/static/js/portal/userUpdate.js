getLoginUser();

window.onload = function (){
    //获取原来的信息
    getOriginalInfo();

    document.getElementById("btn-update").addEventListener("click", function (){
        update();
    });

    document.getElementById("btn-bind").addEventListener("click", function (){
        bind();
    });
}

function getOriginalInfo() {
    let loginUser = getLoginUser();
    $("#update-username").val(loginUser.username);
    $("#update-qq").val(loginUser.qq);
}

function update(){
    let user = new User();
    user.username = $("#update-username").val().trim();
    user.qq = $("#update-qq").val().trim();

    $.ajax({
        url: "/bili/portal/user/update",
        type: "post",
        //发送的数据
        data: JSON.stringify(user),
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            if (data.status == Status.SUCCESS){
                alert("修改成功");
                window.location.href = "/bili/sub.html";
            } else {
                alert(data.msg);
            }
        }
    });
}

function bind(){
    $.ajax({
        url: "/bili/portal/user/bind",
        type: "post",
        //发送的数据
        dataType: "json",
        success: function (data) {
            if (data.status == Status.SUCCESS){
                alert("绑定成功");
            } else {
               alert(data.msg);
            }
        }
    });
}
