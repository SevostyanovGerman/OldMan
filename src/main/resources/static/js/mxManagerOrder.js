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
    delivery.textContent = customer.defaultDelivery;
    };

