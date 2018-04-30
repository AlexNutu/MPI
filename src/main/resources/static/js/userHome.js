$(document).ready(function () {

    // $("[data-toggle=tooltip]").tooltip();

    $('.sBtn').on('click', function (event){
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function(user, status){
                 $('.myForm #send_date').val(user.full_name);
                 $('.myForm #id_receiver').val(user.id);
            });
        $('.myForm #exampleModal').modal();
    });

    $('.details .delBtn').on('click', function (event){
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function(idea, status){
            $('.myFormDelete #myModal #id').val(idea.id);
        });
        // $('#myModal #delRef').attr('href', href);
        $('.myFormDelete #myModal').modal();
    });

});