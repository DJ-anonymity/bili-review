window.onload = function (){
    getBiliUser();

    document.getElementById("btn-yzm").addEventListener("click", function (event){
        getCheckNum();
    });

    document.getElementById("btn-reg").addEventListener("click", function (){
        register();
    });

    document.getElementById("register-username").addEventListener('change', function (event){
        checkName(event.target.value);
    })
}

function getCheckNum(){
    let email = $("#register-email").val();

    $.ajax({
        url: "/bili/portal/user/checkNum/send?email="+email,
        type: "post",
        //发送的数据
        dataType: "json",
        success: function (data) {
            if (data.status == Status.SUCCESS){
                $("#yzm-check-tip").css("display", "none");
                btnInvalidate(document.getElementById("btn-yzm"), 60*1000);
            } else {
                $("#yzm-check-tip").css("display", "block");
            }
        }
    });
}

function register(){
    let user = new User();
    user.username = $("#register-username").val().trim();
    user.password = $("#register-passwd").val().trim();
    user.email = $("#register-email").val().trim();
    user.checkNum = $("#register-yzm").val().trim();

    $.ajax({
        url: "/bili/portal/user/register",
        type: "post",
        //发送的数据
        data: JSON.stringify(user),
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            if (data.status == Status.SUCCESS){
                alert("注册成功");
                window.location.href = "/bili/login.html";
            } else {
                alert(data.msg);
            }
        }
    });
}

function checkName(username){
    if (username != null && username != ''){
        $.ajax({
            url: "/bili/portal/user/name/check?username="+username,
            type: "get",
            //发送的数据
            dataType: "json",
            success: function (data) {
                if (data.status == Status.SUCCESS){
                    $("#name-check-tip").css("display", "none");
                } else {
                    $("#name-check-tip").css("display", "block");
                }
            }
        });
    }
}

function getBiliUser(){
    $.ajax({
        url: "/bili/portal/user/bili/info",
        type: "get",
        //发送的数据
        dataType: "json",
        success: function (data) {
            if (data.status == Status.SUCCESS){
                if (data.data != null){
                    alert("插件已经成功获取权限");
                } else {
                    alert("请重新打开插件")
                }
            } else {
               alert(data.msg);
            }
        }
    });
}