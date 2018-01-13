/* Lock button */
$(document).ready(function () {
    /* SmartPhoto */
    window.addEventListener('DOMContentLoaded', function () {
        new smartPhoto(".js-smartPhoto");
    });
})

function checkStatus() {
    var status = document.getElementsByName("status");
    var check = document.getElementsByName("sendBtn");

    for (var i = 0; i < status.length; i++) {
        if (status[i].innerHTML == "готов") {
            check[0].disabled = false;
        } else {
            check[0].disabled = true;
        }
    }
}

/* Comment toggle */
function toggleComment(event) {
    var element = event.currentTarget;
    var edition = $(element).parent().next(".comment-content");
    $(edition).toggle();
    $(edition).next(".comment-edition").toggle();
}

var activeTab = localStorage.getItem('activeTab');
console.log(activeTab);
if (activeTab) {
    $('a[href="' + activeTab + '"]').tab('show');
}

/* Tabs */
$('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
    localStorage.setItem('activeTab', $(e.target).attr('href'));
});




