
function checkUser(str)
{
    if (str.length==0)
    {
        document.getElementById("checkEmail").innerHTML="";
        document.getElementById("checkEmail").style.border="0px";
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

           var result = xmlhttp.responseText;
           var btn = document.getElementById("newCustomerBtn");
           var message =  document.getElementById("erroremail");

           if (result == "false") {
               document.getElementById("erroremail").innerHTML="Клиент существует!";
               $('#newCustomerBtn').prop('disabled',true);
           } else {

               document.getElementById("erroremail").innerHTML="";
               $('#newCustomerBtn').prop('disabled',false)
           }
        }
    }
    xmlhttp.open("GET","/checkuser?q="+str,true);
    xmlhttp.send();
}

