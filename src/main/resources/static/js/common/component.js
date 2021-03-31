/*
* 使用本模态框注意事项
* 1.触发按钮的class为 z-button-modal
* 2.触发按钮的data.target要为 弹出层的id
* 3.弹出层的class要设置为z-modal-box
* 4.弹出层的具体样式请自己设置，默认样式仅供参考
* @author bootzhong
* example
* <input type="button" class="z-button-modal" data-target="add-subscribe" value="查看">
* <div class="z-modal-box" id="add-subscribe">666</div>
* */
$(document).ready(function () {
    //遮罩层的内容
    var maskBox = "<div class='z-mask-box' id='z-mask-box'>"+ "</div>";
    //获取要使用模态框的按钮数
    var modalButtons = document.getElementsByClassName("z-button-modal");
    //如果没有使用，则直接退出本函数
    if (modalButtons.length ==0){
        return;
    }
    //添加遮罩层
    $("body").append(maskBox);
    for (var i = 0; i < modalButtons.length; i++){
        //获取弹出层的id
        var modalBoxId = modalButtons[i].getAttribute("data-target");
        //给每一个模态框的按钮添加一个点击事件
        $(modalButtons[i]).click(function (event) {
            event.stopPropagation();
            //遮罩层出现
            $("#z-mask-box").css("display", "block");
            //获取弹出层的id,因为触发点击事件时循环变量i已经消失掉，无法获取搭配modalBoxId，所以要用this获取
            var modalBoxId = this.getAttribute("data-target");
            //弹出层出现
            console.log(modalBoxId);
            $("#"+modalBoxId).css("display", "block");
        });
        //点击模态框内 模态框不消失
        $("#"+modalBoxId).click(function (event) {
            event.stopPropagation();
        });
    }
    //点击外面模态框关闭
    $(document).click(function () {
        console.log("1");
        //遮罩层隐藏
        $("#z-mask-box").css("display", "none");
        //全部弹出层隐藏
        for (var i = 0; i < modalButtons.length; i++){
            var modalBoxId = modalButtons[i].getAttribute("data-target");
            console.log(modalBoxId+"mask");
            $("#"+modalBoxId).css("display", "none");
        }
    });
});

