$(document).ready(function () {

    // $("[data-toggle=tooltip]").tooltip();

    $('.commentList .delBtn').on('click', function (event){
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function(com, status){
            $('.myFormDelete #myModal #id').val(com.id);
        });
        // $('#myModal #delRef').attr('href', href);
        $('.myFormDelete #myModal').modal();
    });

    // $('.myFormDelete #deleteMyIdeaForm').on('submit', function(e) {
    // trebuie adaugata atributul  dismiss de la butonul de No, pus pe un element din form
    // id-ul ideii, dupa care obtinute toate ideile din div-ul ce contine lista ideilor si stearsa
    // cea careia ii corespunde id-ul, apoi trigger-uita notificarea ca s-a sters ideea :)
    //     e.preventDefault(); // prevent native submit
    //
    //     $(this).ajaxSubmit({
    //         target: 'myResultsDiv'
    //     });
    // });

});