

    function geoStat() {

        // посылаем ajax запрос, получаем массив обьектов для графика
        $.ajax({
            url: "/statistic/geo/getGeoObjects",
            dataType: 'json',
            success: function (geoElement) {

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
                    data.addColumn('string', 'Страна');  //названия колонок
                    data.addColumn('number', 'Количество оплаченных заказов');
                    data.addRows(geoElement); // полученный массив данных

                    // Set chart options
                    var options = {
                        'title': 'Кол-во оплаченных заказовпо странам',
                        'width': '100%',
                        'height': 500,
                    };

                    // Instantiate and draw our chart, passing in some options.
                    var chart = new google.visualization.GeoChart(document.getElementById('chart_div'));
                    chart.draw(data, options);
                }


            }
        });

    }





