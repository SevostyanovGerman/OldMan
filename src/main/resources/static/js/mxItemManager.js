<!--Считаем сумму позиции-->

function amountItem() {
    var amount = document.getElementById("amountItemField");
    var price = document.getElementById("priceItemField");
    var number = document.getElementById("numberItemField");
    var btn = document.getElementById("saveItemBtn");

    try{
        if (price.value != 0 & number.value != 0) {
            amount.value = price.value * number.value ;
            if (isNaN(amount.value) | amount.value<= 0) {
                btn.disabled = true;
                amount.value = "";
            } else {
                btn.disabled = false;
            }
        } else {
            amount.value = "";
            btn.disabled = true;
        }

    } catch (e){

    }
}
