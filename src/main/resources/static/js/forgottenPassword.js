function forgotPassword() {

    var email = document.getElementById("email");
    var data = {
        email: email.value
    };
    $("#forgottenMessage").load("/forgotten/mail/",data);
}