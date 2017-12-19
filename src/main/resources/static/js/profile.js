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