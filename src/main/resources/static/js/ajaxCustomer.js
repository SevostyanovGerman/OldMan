
function showResult(str)
{
    if (str.length==0)
    {
        document.getElementById("livesearch").innerHTML="";
        document.getElementById("livesearch").style.border="0px";
        return;
    }
    if (window.XMLHttpRequest)
    {// IE7+, Firefox, Chrome, Opera, Safari 浏览器执行
        xmlhttp=new XMLHttpRequest();
    }
    else
    {// IE6, IE5 浏览器执行
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.status==200)
        {
            // result = new Array();
            result = JSON.parse(xmlhttp.response);


            var list="";
            arrayResult = result;

            var count = 10;
            if (result.length<10) {
                count = result.length;
            }
            for(var i = 0; i<count; i++){


                list =list + "<a onclick=selectCustomer("+i+") style=\"cursor: pointer\">"+ result[i].firstName +  " "+ result[i].secName + "</a>" +"<br>" ;
            }

            list=list+"<a href='#' >Показать всех</a>";
            document.getElementById("livesearch").innerHTML=list;
            document.getElementById("livesearch").style.border="1px solid #A5ACB2";
        }
    }
    xmlhttp.open("GET","/customersearch?q="+str,true);
    xmlhttp.send();
}

function choose (elm) {
    var box = document.getElementById("liveText")
    box.value=elm.text;

}