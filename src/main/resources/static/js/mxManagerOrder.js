$(function() {
    //при нажатии на кнопку с id="save"
    $('#newCustomerBtn').click(function() {
        //переменная formValid
        var formValid = true;
        //перебрать все элементы управления input
        $('input').each(function() {
            //найти предков, которые имеют класс .form-group, для установления success/error
            var formGroup = $(this).parents('.form-group');
            //найти glyphicon, который предназначен для показа иконки успеха или ошибки
            var glyphicon = formGroup.find('.form-control-feedback');
            //для валидации данных используем HTML5 функцию checkValidity
            if (this.checkValidity()) {
                //добавить к formGroup класс .has-success, удалить has-error
                formGroup.addClass('has-success').removeClass('has-error');
                //добавить к glyphicon класс glyphicon-ok, удалить glyphicon-remove
                glyphicon.addClass('glyphicon-ok').removeClass('glyphicon-remove');
            } else {
                //добавить к formGroup класс .has-error, удалить .has-success
                formGroup.addClass('has-error').removeClass('has-success');
                //добавить к glyphicon класс glyphicon-remove, удалить glyphicon-ok
                glyphicon.addClass('glyphicon-remove').removeClass('glyphicon-ok');
                //отметить форму как невалидную
                formValid = false;
            }
        });

    });
});


//выбор клиента в ajax поиске//

function selectCustomer(ajaxCustomerId) {
    var name = document.getElementById("firstNameField");
    var second = document.getElementById("secNameField");
    var email = document.getElementById("emailField");
    var phone = document.getElementById("phoneField");
    var delivery = document.getElementById("deliveryField");
    var customer = result[ajaxCustomerId];
    name.value = customer.firstName;
    second.value = customer.secName;
    email.value = customer.email;
    phone.value = customer.phone;
    delivery.textContent = customer.defaultDelivery.country +' , ' + customer.defaultDelivery.city +' , ' +  customer.defaultDelivery.address +' , ' +  customer.defaultDelivery.zip;
    var subEditBtn = document.getElementById("editBtnSub");
    subEditBtn.disabled = false;

    };


function editCustomer() {
    var firstName = document.getElementById("firstNameField");
    var secName = document.getElementById("secNameField");
    var email = document.getElementById("emailField");
    var phone = document.getElementById("phoneField");
    var editBtn = document.getElementById("editBtn");
    var subEditBtn = document.getElementById("editBtnSub");

    if (  $("#firstNameField").prop('readonly')) {
        $("#firstNameField").prop('readonly', false);
        $("#secNameField").prop('readonly', false);
        $("#emailField").prop('readonly', false);
        $("#phoneField").prop('readonly', false);
        editBtn.textContent = "Отмена";
        subEditBtn.disabled = false;
    } else {
        $("#firstNameField").prop('readonly', true);
        $("#secNameField").prop('readonly', true);
        $("#emailField").prop('readonly', true);
        $("#phoneField").prop('readonly', true);
        editBtn.textContent = "Изменить";
        subEditBtn.disabled = true;
    }
}





//Рисуем DataTable при открытии модального окна
$('#designerModal').on('shown.bs.modal', function() {
    //Get the datatable which has previously been initialized
    var dataTable= $('#designerTableMD').DataTable({
        "dom": '<"top">f<"bottom"><"clear">',
        "ordering": false,
        language: {
            "processing": "Подождите...",
            "search": "Поиск:",
            "lengthMenu": "Показать _MENU_ записей",
            "info": "Записи с _START_ до _END_ из _TOTAL_ записей",
            "infoEmpty": "Записи с 0 до 0 из 0 записей",
            "infoFiltered": "(отфильтровано из _MAX_ записей)",
            "infoPostFix": "",
            "loadingRecords": "Загрузка записей...",
            "zeroRecords": "Записи отсутствуют.",
            "emptyTable": "В таблице отсутствуют данные",
            "paginate": {
                "first": "Первая",
                "previous": "Предыдущая",
                "next": "Следующая",
                "last": "Последняя"
            },
            "aria": {
                "sortAscending": ": активировать для сортировки столбца по возрастанию",
                "sortDescending": ": активировать для сортировки столбца по убыванию"
            }
        }
    });
    //recalculate the dimensions
    dataTable.columns.adjust().responsive.recalc();

});

//Рисуем DataTable при открытии модального окна
$('#masterModal').on('shown.bs.modal', function() {
    //Get the datatable which has previously been initialized
    var  dataTable = $('#masterTableMD').DataTable({
        "dom": '<"top">f<"bottom"><"clear">',
        "ordering": false,
        language: {
            "processing": "Подождите...",
            "search": "Поиск:",
            "lengthMenu": "Показать _MENU_ записей",
            "info": "Записи с _START_ до _END_ из _TOTAL_ записей",
            "infoEmpty": "Записи с 0 до 0 из 0 записей",
            "infoFiltered": "(отфильтровано из _MAX_ записей)",
            "infoPostFix": "",
            "loadingRecords": "Загрузка записей...",
            "zeroRecords": "Записи отсутствуют.",
            "emptyTable": "В таблице отсутствуют данные",
            "paginate": {
                "first": "Первая",
                "previous": "Предыдущая",
                "next": "Следующая",
                "last": "Последняя"
            },
            "aria": {
                "sortAscending": ": активировать для сортировки столбца по возрастанию",
                "sortDescending": ": активировать для сортировки столбца по убыванию"
            }
        }
    });

    //recalculate the dimensions
    dataTable.columns.adjust().responsive.recalc();



});

//удаляем DataTable при закрытии модального окна
$('#masterModal').on(' hide.bs.modal', function() {
    //Get the datatable which has previously been initialized

    var  dataTable = $('#masterTableMD').DataTable();
    dataTable.destroy();


});

//удаляем DataTable при закрытии модального окна
$('#designerModal').on(' hide.bs.modal', function() {
    //Get the datatable which has previously been initialized

    var  dataTable = $('#designerTableMD').DataTable();
    dataTable.destroy();


});