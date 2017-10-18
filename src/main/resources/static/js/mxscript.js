

function checkStatus() {

    var status = document.getElementsByName("status");
    var check = document.getElementsByName("sendBtn");

    for (var i = 0; i < status.length; i++) {
        if (status[i].innerHTML == "YES" ) {
            check[0].disabled = false;
        } else {
            check[0].disabled = true;
        }
    }
}


function showHide(element_id) {
    //Если элемент с id-шником element_id существует
    if (document.getElementById(element_id)) {
        //Записываем ссылку на элемент в переменную obj
        var obj = document.getElementById(element_id);
        var btn = document.getElementById(element_id+'Btn');
        //Если css-свойство display не block, то:
        if (obj.style.display != "block") {
            obj.style.display = "block"; //Показываем элемент
            btn.textContent = 'Скрыть';
        }
        else {
            obj.style.display = "none";
            btn.textContent = 'Ответить';
        } //Скрываем элемент
    }

    //Если элемент с id-шником element_id не найден, то выводим сообщение
    else alert("Элемент с id: " + element_id + " не найден!");
}




