$(document).ready(function () {

    //delete idea
    $('.actions .delBtn').on('click', function (event){
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function(idea, status){
            $('.myFormDelete #myModal #id').val(idea.id);
        });
        // $('#myModal #delRef').attr('href', href);
        $('.myFormDelete #myModal').modal();
    });

    //delete comment
    $('.commentList .delBtn').on('click', function (event){
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function(com, status){
            $('.myCommDelete #comModal #id').val(com.id);
        });
        // $('#myModal #delRef').attr('href', href);
        $('.myCommDelete #comModal').modal();
    });


});