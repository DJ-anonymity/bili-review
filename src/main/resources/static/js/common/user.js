
function getLoginUser(){
    let user;

    $.ajax({
        url: "/bili/portal/user/info",
        type: "get",
        //发送的数据
        dataType: "json",
        async: false,
        success: function (data) {
            if (data.status == Status.SUCCESS){
                user = data.data;
            } else {
                window.location.href = "/bili/login.html";
            }
        }
    });

    return user;
}