
function newPassword() {

    var newPass = document.getElementById("newPass");
    var reply = document.getElementById("replyPass");
    var token = document.getElementById("token");
    var message = document.getElementById("errorCurrentPass");

    if ( reply.value == newPass.value ) {
        var data = {
            token: token.textContent,
            newPassword: newPass.value
        };

        $("#errorCurrentPass").load("/profile/newPassword", data,function() {

         if (message.textContent == 'Пароль изменен') {
             window.setTimeout(function () {
                 window.location.replace("/login");
             },3000);
         }

        });

    }  else {
        message.innerHTML = "Пароли не совпадают"
}

}