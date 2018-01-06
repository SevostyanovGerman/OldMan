
var startMx;
var endMx;
var dwm = '%Y-%m';

    function avgPrice(start, end, dwm) {

        // переменные startDate и endDate для сервлета
        var data = {
            startDate: start.format('MM/DD/YYYY'),
            endDate: end.format('MM/DD/YYYY'),
            dwm: dwm
        };

        // посылаем ajax запрос, получаем массив обьектов для графика
        $.ajax({
            url: "/statistic/sumOrderPrice",
            data: data,
            dataType: 'json',
            success: function (averagePriceElement) {

                // Load the Visualization API and the corechart package.
                google.charts.load('current', {'packages': ['corechart', 'line']});

                // Set a callback to run when the Google Visualization API is loaded.
                google.charts.setOnLoadCallback(drawChart);

                // Callback that creates and populates a data table,
                // instantiates the pie chart, passes in the data and
                // draws it.
                function drawChart() {

                    // Create the data table.

                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'Дата');  //названия колонок
                    data.addColumn('number', 'сумма оплаченных заказов');
                    data.addColumn('number', 'сумма не оплаченных заказов');
                    data.addColumn({type: 'number', role: 'annotation'});

                    data.addRows(averagePriceElement); // полученный массив данных

                    drawTable(averagePriceElement); //рисуем таблицу

                    // Set chart options
                    var options = {
                        'title': 'Сумма заказа',
                        'width': '100%',
                        'height': 500,
                        hAxis: { minValue: 0, maxValue: 100 },
                        curveType: 'function',
                        pointSize: 10

                    };

                    // Instantiate and draw our chart, passing in some options.
                    var chart = new google.visualization.AreaChart(document.getElementById('chart_div'));
                    chart.draw(data, options);
                }


            }
        });

    }



// Календарь

    $(function() {
        $('input[name="daterange"]').daterangepicker();
    });


        $(function() {

            var start = moment().subtract(6, 'month');
            var end = moment();


            function cb(start, end) {
                $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));

                startMx =start;
                endMx = end;
                avgPrice(start,end, dwm);
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

function period(step) {
    dwm = step;
    avgPrice(startMx,endMx, dwm);

}

function drawTable(data) {
    data.sort();
    var destroyTable =document.getElementById("tableSum");
    if (destroyTable != null) {
        destroyTable.remove();
    }
    var table = document.createElement("table");
    table.setAttribute("class", "table table-bordered data ");
    table.setAttribute("id", "tableSum");
    table.setAttribute("style", "text-align: center;");
    var tbody = document.createElement("tbody");
    tbody.setAttribute("class", "table-body");
    table.appendChild(tbody);

    var thead = document.createElement("thead");
    table.appendChild(thead);
    var th1 = document.createElement('th');
    th1.setAttribute("class", "table-head");
    th1.setAttribute("style", "text-align: center;");
    th1.innerHTML = 'Период';
    thead.appendChild(th1);
    var th2 = document.createElement('th');
    th2.setAttribute("class", "table-head");
    th2.setAttribute("style", "text-align: center;");
    th2.innerHTML = 'Оплаченно';
    thead.appendChild(th2);
    var th3 = document.createElement('th');
    th3.setAttribute("class", "table-head");
    th3.setAttribute("style", "text-align: center;");
    th3.innerHTML = 'Не оплаченно';
    thead.appendChild(th3);


    for(var i = 0; i < data.length; i++) {
        var tr = document.createElement('tr');
        var mass = data[i]
        for (var j = 0; j < mass.length; j++){
            var td = document.createElement('td');
            td.setAttribute("class", "table-body");
            td.innerHTML = mass[j];
            tr.appendChild(td);
        }
        tbody.appendChild(tr);
    }
    document.getElementById("myTable").appendChild(table);
}
