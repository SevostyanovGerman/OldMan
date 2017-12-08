

    function geoStat() {

        // посылаем ajax запрос, получаем массив обьектов для графика
        $.ajax({
            url: "/statistic/geo/getGeoObjects",
            dataType: 'json',
            success: function (geoElement) {

                // Load the Visualization API and the corechart package.
                google.charts.load('current', {mapsApiKey: 'AIzaSyCk3eD1o6TNWtpD5D1UMipqsBqLOYqA51M','packages': ['corechart']});

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

                    drawTable(geoElement); //рисуем таблицу

                    // Set chart options
                    var options = {
                        region: 'RU',
                        'title': 'Кол-во оплаченных заказовпо городам',
                        'width': '100%',
                        'height': 500,
                        displayMode: 'markers',
                        colorAxis: {colors: ['#e7711c', '#4374e0']} // orange to blue
                    };

                    // Instantiate and draw our chart, passing in some options.
                    var chart = new google.visualization.GeoChart(document.getElementById('chart_div'));
                    chart.draw(data, options);


                    var barchart_options = {title:'Кол-во оплаченных заказовпо городам',
                        width:'100%',
                        height:'100%',
                        legend: 'none'};

                    var chart2 = new google.visualization.BarChart(document.getElementById('chart_div2'));
                    chart2.draw(data, barchart_options);








                }



            }
        });

    }


    function drawTable(data) {
        data.sort();
        var table = document.createElement("table");
        table.setAttribute("class", "table table-bordered data");
        var tbody = document.createElement("tbody");
        table.appendChild(tbody);
        for(var i = 0; i < data.length; i++) {
            var tr = document.createElement('tr');
            var mass = data[i]
            for (var j = 0; j < mass.length; j++){
                var td = document.createElement('td');
                td.innerHTML = mass[j];
                tr.appendChild(td);
            }
            tbody.appendChild(tr);
        }
        document.getElementById("myTable").appendChild(table);
    }