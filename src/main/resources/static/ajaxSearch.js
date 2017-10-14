
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
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            var result = new Array();
             result = JSON.parse(xmlhttp.response);

            var list="";
            for(var i = 0; i<result.length; i++){
                list =list + "<a href=\"/designer/order/" + result[i].numer+ " \" >"+ result[i].number+"</a>" +"<br>" ;

            }
            document.getElementById("livesearch").innerHTML=list;
            document.getElementById("livesearch").style.border="1px solid #A5ACB2";
        }
    }
    xmlhttp.open("GET","/ajax?q="+str,true);
    xmlhttp.send();
}
