/* Lock button */
function checkStatus() {


    var status = document.getElementsByName("status");
    var check = document.getElementsByName("sendBtn");

    for (var i = 0; i < status.length; i++) {
        if (status[i].innerHTML == "готов" ) {
            check[0].disabled = false;
        } else {
            check[0].disabled = true;
        }
    }
}

/* Date range picker */
$(function() {

    $('input[name="datefilter"]').daterangepicker({
        autoUpdateInput: false,
        locale: {
            cancelLabel: 'Clear'
        }
    });

    $('input[name="datefilter"]').on('apply.daterangepicker', function(ev, picker) {
        $(this).val(picker.startDate.format('MM/DD/YYYY') + ' - ' + picker.endDate.format('MM/DD/YYYY'));

        var data = {
            startDate: picker.startDate.format('MM/DD/YYYY'),
            endDate: picker.endDate.format('MM/DD/YYYY')
        };

        var allOrdersTable = $('#ex_body');

        $.ajax({
            type: "POST",
            data:  data,
            dataType: "json",
            url: "/master/ordersByRange",
            success: function(data, textStatus, jqXHR) {
                allOrdersTable.find('tr').remove();
                 drawTable(data);
            }
        });
    });

    function drawTable(data) {

        for (var i=0; i<data.length; i++) {
            drawRow(data[i]);
        }
    }

    function drawRow(rowData) {
        var row = $('<tr role="row">')

        row.append($('<td style="text-align:center;">' + rowData.id + '</td>'));
        row.append($('<td style="text-align:center;">' + rowData.master + '</td>'));
        row.append($('<td style="text-align:center;">' + rowData.paymentString + '</td>'));
        row.append($('<td style="text-align:center;">' + rowData.deliveryType + '</td>'));
        row.append($('<td style="text-align:center;">' + rowData.status + '</td>'));
        row.append($('<td style="text-align:center;">' + rowData.dateRecieved + '</td>'));
        row.append($('<td style="text-align:center;">' + rowData.dateTransferred + '</td>'));
        row.append($(
            '<td style="text-align:center;">' +
            '<form action="/master/order/' + rowData.id +'" method="get">' +
            '<input class="btn btn-info"  type="submit" value="Открыть" />' +
            '</form>' +
            '</td>'));
        row.append($('</tr>'));
        $('#ex_body').append(row);
    }

    $('input[name="datefilter"]').on('cancel.daterangepicker', function(ev, picker) {
        $(this).val('');
    });

});

/* Tabs */
$('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
    localStorage.setItem('activeTab', $(e.target).attr('href'));
});

var activeTab = localStorage.getItem('activeTab');

console.log(activeTab);

if (activeTab) {
    $('a[href="' + activeTab + '"]').tab('show');
}

/* SmartPhoto */
window.addEventListener('DOMContentLoaded',function(){
    new smartPhoto(".js-smartPhoto");
});

/* Comment toggle */
function toggleComment(event) {
    var element =  event.currentTarget;
    var edition = $(element).parent().next(".comment-content");
    $(edition).toggle();
    $(edition).next(".comment-edition").toggle();

}

