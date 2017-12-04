

    function newCustomers(start, end) {

        // переменные startDate и endDate для сервлета
        var data = {
            startDate: start.format('MM/DD/YYYY'),
            endDate: end.format('MM/DD/YYYY')
        };

        // посылаем ajax запрос, получаем массив обьектов для графика
        $.ajax({
            url: "/statistic/newCustomers/ajaxData",
            data: data,
            dataType: 'json',
            success: function (newCustomersData) {

                // Load the Visualization API and the corechart package.
                google.charts.load('current', {'packages': ['corechart']});

                // Set a callback to run when the Google Visualization API is loaded.
                google.charts.setOnLoadCallback(drawChart);

                // Callback that creates and populates a data table,
                // instantiates the pie chart, passes in the data and
                // draws it.
                function drawChart() {

                    // Create the data table.

                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'Месяц');  //названия колонок
                    data.addColumn('number', 'средняя сумма заказа');
                    data.addRows(newCustomersData); // полученный массив данных

                    // Set chart options
                    var options = {
                        'title': 'Средняя сумма заказа',
                        'width': '100%',
                        'height': 500
                    };

                    // Instantiate and draw our chart, passing in some options.
                    var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
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

            var start = moment().subtract(29, 'days');
            var end = moment();


            function cb(start, end) {
                $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));

                newCustomers(start,end);
            }

            $('#reportrange').daterangepicker({
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

