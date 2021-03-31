
window.onload = function (){
    document.getElementById("btn-login").addEventListener('click', function (){
        login();
    });
}


function login(){
    let user = new User();
    user.email = $("#login-email").val().trim();
    user.password = $("#login-passwd").val().trim();

    $.ajax({
        url: "/bili/portal/user/login",
        type: "post",
        //发送的数据
        data: JSON.stringify(user),
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            if (data.status == Status.SUCCESS){
                window.location.href = "sub.html";
            } else {
                alert("账号密码有误");
            }
        }
    });
}