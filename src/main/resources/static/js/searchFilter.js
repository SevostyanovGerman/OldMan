var startMx;
var endMx;
var startDateSession;
var endDateSession;
// Календарь

$(function() {
    $('input[name="daterange"]').daterangepicker();
});


$(function() {
    var start;
    var end;



    if ( startDateSession != null) {
        start = moment(startDateSession);
    } else {
        start = moment().startOf('month');

    }

    if ( endDateSession != null) {
        end = moment(endDateSession);
    } else {
        end = moment();
    }



    function cb(start, end) {
        $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
        startMx =start;
        endMx = end;
    }

    $('#reportrange').daterangepicker({

        "locale": {
            "applyLabel": "Применить",
            "cancelLabel": "Отмена",
            "customRangeLabel": "Выбрать период"
            },
        language: 'ru',
        startDate: start,
        endDate: end,
        ranges: {
            'Текущий месяц': [moment().startOf('month'), moment().endOf('month')],
            'Прошедший месяц': [moment().subtract(1, 'month').startOf('month'),
                moment().subtract(1, 'month').endOf('month')],
            '2 месяца': [moment().subtract(3, 'month').startOf('month'),
                moment().endOf('month')],
            '6 месяцев': [moment().subtract(6, 'month').startOf('month'),
                moment().endOf('month')],
            'Год': [moment().subtract(12, 'month').startOf('month'),
                moment().endOf('month')]
        }
    }, cb);

    cb(start, end);
});


$('#reportrange').on('apply.daterangepicker', function(ev, picker) {
    filterSearch();
});

function filterSearch(pageNumber) {
    var url ="/orders/search";
    var searchWord = document.getElementById("search");
    var sortBy = document.getElementById("sortingBy");
    var pageSize = document.getElementById("pageSize");
    var minPrice = document.getElementById("minPrice");
    var maxPrice = document.getElementById("maxPrice");
    var orderBy = document.getElementById("orderBy");

    if (pageNumber == null) {
        pageNumber = 1;
    }
    if (pageSize == null) {
        pageSize = 25;
    }
    if (orderBy == null) {
        orderBy = "DESC";
    }
    var page = pageNumber;
    var data = {
        search: searchWord.value,
        startDate: startMx.format('MM/DD/YYYY'),
        endDate: endMx.format('MM/DD/YYYY'),
        sort: sortBy.value,
        minPrice: minPrice.value,
        maxPrice: maxPrice.value,
        pageNumber: page,
        pageSize: pageSize.value,
        orderBy: orderBy.value
    };
    $("#sortingTable").load(url, data);
}

function notification(){
    $("#sortingTable").load('/order/notification/get');
}



