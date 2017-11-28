var spanGlob = [];
var files;
function handleFileSelect(evt) {
    files = evt.target.files; // FileList object

    // Loop through the FileList and render image files as thumbnails.
    for (var i = 0, f; f = files[i]; i++) {
        var j = 0;

        var reader = new FileReader();

        // Closure to capture the file information.
        reader.onload = (function(theFile) {

            return function(e) {

                // Render thumbnail.
                var span = document.createElement('span');
                span.innerHTML = ['<img style="cursor: pointer" width="50"+ height="50" class="thumb" onclick=\"cancelUplodFunc(',j,');\"' +
                '  src="', e.target.result,
                    '" title="', escape(theFile.name), '" /> '].join('');
                document.getElementById('list').insertBefore(span, null);


                spanGlob.push(span);
                j++;
            };
        })(f);

        // Read in the image file as a data URL.

        showUpload();
        reader.readAsDataURL(f);
    }
}

document.getElementById('files').addEventListener('change', handleFileSelect, false);

//удаляем иконки картинок и картинки из массива//
function cancelUplodFunc(i) {
    spanGlob[i].innerHTML="";
    var fileArray = Array.prototype.slice.call(files);
    fileArray.splice(i,1);
    files=fileArray;
}


//Отправляем сервлету массив картинок//
function sendImage(item) {


    var formData = new FormData();

    for(var i = 0; i<files.length; i++) {
        formData.append("files", files[i]);
    }

    $.ajax({
        url: "/statistic/middle/middleOrderPrice",
        success: function(data){
            alert( "Прибыли данные: " + data );
        }
    });

    hideUpload();

    setTimeout(function(){
        window.location.reload();
    }, 300);
}

function miniPicDel() {
    for(var i = 0; i<spanGlob.length; i++) {
        spanGlob[i].innerHTML="";
    }
    hideUpload();
}


function showUpload() {

        var cancel = document.getElementById("cancelBtn");
        var upload = document.getElementById("uploadBtn");

            upload.style.display = "inline"; //Показываем элемент
            cancel.style.display = "inline";

}


function hideUpload() {

    var cancel = document.getElementById("cancelBtn");
    var upload = document.getElementById("uploadBtn");

    cancel.style.display = "none";
    upload.style.display = "none";


}