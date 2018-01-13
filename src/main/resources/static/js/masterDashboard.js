/* Date range picker */
$(document).ready(function () {
    var dateRangeInput = $('input[name="datefilter"]');

    dateRangeInput.daterangepicker({
        autoUpdateInput: false,
        locale: {
            cancelLabel: 'Clear'
        }
    });

    dateRangeInput.on('apply.daterangepicker', function (ev, picker) {
        $(this).val(picker.startDate.format('MM/DD/YYYY') + ' - ' + picker.endDate.format('MM/DD/YYYY'));

        var data = {
            startDate: picker.startDate.format('MM/DD/YYYY'),
            endDate: picker.endDate.format('MM/DD/YYYY')
        };

        var allOrdersTable = $('#ex_body');

        $.ajax({
            type: "POST",
            data: data,
            dataType: "json",
            url: "/master/ordersByRange",
            success: function (data, textStatus, jqXHR) {
                allOrdersTable.find('tr').remove();
                drawTable(data);
            }
        });
    });

    function drawTable(data) {
        for (var i = 0; i < data.length; i++) {
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
            '<form action="/master/order/' + rowData.id + '" method="get">' +
            '<input class="btn btn-info"  type="submit" value="Открыть" />' +
            '</form>' +
            '</td>'));
        row.append($('</tr>'));
        $('#ex_body').append(row);
    }

    dateRangeInput.on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });
});

