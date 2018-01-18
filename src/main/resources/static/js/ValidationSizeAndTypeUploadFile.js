function ValidationFileSize() {
    var MAX_FILE_SIZE_MB = 50;
    var allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;
    var uploadFiles = document.getElementById('fileMulti');

    if (uploadFiles.files.length > 0) {
        for (var i = 0; i <= uploadFiles.files.length - 1; i++) {

            var fileSize = Math.round(uploadFiles.files.item(i).size/1024/1024);
            var fileName = uploadFiles.files.item(i).name;

            if (fileSize > MAX_FILE_SIZE_MB) {
                alert("Размер одного из выбранных файлов больше 50.0 МБ");
                uploadFiles.value = "";
            }

            // if(!allowedExtensions.exec(fileName)){
            //     alert("Один из выбранных файлов не является изображением!");
            //     uploadFiles.value = "";
            // }
        }
    }
}