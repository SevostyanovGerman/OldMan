function settingUpTable() {

    $('#sortingTable').dataTable({

        "columnDefs": [
            {
                "targets": [ 1,6 ],
                "searchable": false
            }],

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
        }
    });
}
$(document).ready(function () {
    settingUpTable();
});