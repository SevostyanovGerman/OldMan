function settingUpTable() {

    $('#sortingTable').dataTable({

        "order": [[ 9, "asc" ]],

        "columnDefs": [
            {
                "targets": [ 1,7 ],
                "searchable": false


            },

            {
                "targets": [ 9 ],
                "visible": false,
                "searchable": false
            }
        ],

        "oLanguage": {
            "sLengthMenu": "Показать _MENU_ записей на странице",
            "sZeroRecords": "Извините - ничего не найдено",
            "sInfo": "Показано _START_ до _END_ из _TOTAL_ записей",
            "sInfoEmpty": "Нет записей",
            "sInfoFiltered": "(из _MAX_ записей)",
            "sSearch": "Поиск:",
            "oPaginate": {
                "sNext": "След. стр.",
                "sPrevious": "Пред. стр."
            }
        },

        "createdRow": function( row, data, dataIndex ) {
            if ( data["9"] == "1" ) {
                $( row ).addClass( "info" );
            }
            if ( data["9"] == "3" ) {
                 $( row ).addClass( "warning" );
            }
        }

    });
}
$(document).ready(function () {
    settingUpTable();
});