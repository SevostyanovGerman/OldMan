function checkStatus() {


    var status = document.getElementsByName("status");
    var check = document.getElementsByName("sendBtn");

    for (var i = 0; i < status.length; i++) {
        if (status[i].innerHTML == "ready" ) {
            check[0].disabled = false;
        } else {
            check[0].disabled = true;
        }
    }
}