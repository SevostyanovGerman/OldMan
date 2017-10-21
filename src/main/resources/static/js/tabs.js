$(document).ready(function(){

 var hash = window.location.hash;

 /*var tabel = document.getElementById(hash);
tabel.class="tab-pane fade active";
$('.nav-tabs a[href="' + hash + '"]').tab('show');

*/
var tabss = document.getElementById('tabIndex').textContent
    var i = tabss.name;
if (tabss == "") {
    $("#myTab2 li:eq(0) a").tab('show');
}
else {
    $('#myTab2 li:eq('+tabss +') a').tab('show');
    /*
    $('#myTab a[href="' + hash + '"]').tab('show');
     */
}

});