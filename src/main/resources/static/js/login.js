$(document).ready(function(){
    $("button").click(function(e){
        e.preventDefault();

        var name = $("#username").val();
        var password = $("#email").val();
        if(name == "" || name == null){
            $(".nameInput").removeClass("has-success");
            $(".nameInput .glyphicon-ok").remove();
            $(".nameInput").addClass("has-error").append(" <span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
            var flag1 = ("false");
        }
        else{
            $(".nameInput").removeClass("has-error");
            $(".nameInput .glyphicon-remove").remove();
            $(".nameInput").addClass("has-success").append(" <span class='glyphicon glyphicon-ok form-control-feedback' aria-hidden='true'></span>");
            var flag1 = ("true");
        };
        if(password == "" || password == null){
            $(".emailInput").removeClass("has-success");
            $(".emailInput .glyphicon-ok").remove();
            $(".emailInput").addClass("has-error").append(" <span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
            var flag2 = ("false");
        }
        else{
            $(".emailInput").removeClass("has-error");
            $(".emailInput .glyphicon-remove").remove();
            $(".emailInput").addClass("has-success").append(" <span class='glyphicon glyphicon-ok form-control-feedback' aria-hidden='true'></span>");
            var flag2 = ("true");
        };
        if(flag1 == "true"){
            if(flag2 == "true"){
                location.href = 'http://www.yandex.ru/';
            }
            else{

            };
        }
        else{

        };
    });
});