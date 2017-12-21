var files; // переменная. будет содержать данные файлов

// заполняем переменную данными, при изменении значения поля file
$('input[type=file]').on('change', function(){
    files = this.files;
    // $("#avatarBtn").show();

    $('#avatarBtn').css('display', 'inline');
    readURL(this);
});


$('#avatarBtn').on( 'click', function( event ){

    // event.stopPropagation(); // остановка всех текущих JS событий
    // event.preventDefault();  // остановка дефолтного события для текущего элемента - клик для <a> тега

    // ничего не делаем если files пустой
    if( typeof files == 'undefined' ) return;

    // создадим объект данных формы
    var data = new FormData();

    // заполняем объект данных файлами в подходящем для отправки формате
    $.each( files, function( key, value ){

       // $('#avatarImg').attr('src', files.target.result);
        data.append( key, value );
    });


    // добавим переменную для идентификации запроса
    data.append( 'file', 1 );


    // $("#avatarBtn").hidden;
    // $("#avatarBtn").hide();
    $('#avatarBtn').css('display', 'none');

    // AJAX запрос
    $.ajax({
        url         : '/profile/avatar',
        type        : 'POST', // важно!
        data        : data,
        cache       : false,
        dataType    : 'json',
        enctype     : "multipart/form-data",
        // отключаем обработку передаваемых данных, пусть передаются как есть
        processData : false,
        // отключаем установку заголовка типа запроса. Так jQuery скажет серверу что это строковой запрос
        contentType : false,
        // функция успешного ответа сервера
        success     : function(){
        }

    });

});



function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#avatarImg').attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
    }
}


$('#changePassword').on( 'click', function( event ){

    $('#currentPass').css('display', 'inline');
    $('#newPass').css('display', 'inline');
    $('#replyPass').css('display', 'inline');
    $('#forgotPass').css('display', 'inline');
    $('#changePassBtn').css('display', 'inline');
    $('#cancelPassBtn').css('display', 'inline');

});


$('#cancelPassBtn').on( 'click', function( event ){

    $('#currentPass').css('display', 'none');
    $('#newPass').css('display', 'none');
    $('#replyPass').css('display', 'none');
    $('#forgotPass').css('display', 'none');
    $('#changePassBtn').css('display', 'none');
    $('#cancelPassBtn').css('display', 'none');

});

$('#changePassBtn').on( 'click', function( event ) {

    var current = document.getElementById("currentPass");
    var newPass = document.getElementById("newPass");
    var reply = document.getElementById("replyPass");
    var error = document.getElementById("errorCurrentPass");


    if ( reply.value == newPass.value ) {

        var data = {
            currentPassword: current.value,
            newPassword: newPass.value
        };

        $("#errorCurrentPass").load("/profile/password", data, function() {
           hideChangeBtn();
        });

    } else {
        error.innerHTML = "Пароли не совпадают"
    }


});

function hideChangeBtn() {

    $('#currentPass').css('display', 'none');
    $('#newPass').css('display', 'none');
    $('#replyPass').css('display', 'none');
    $('#forgotPass').css('display', 'none');
    $('#changePassBtn').css('display', 'none');
    $('#cancelPassBtn').css('display', 'none');

}

