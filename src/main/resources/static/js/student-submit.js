function check(form) {
    let fileUpload = document.getElementById("file").files;
    if (fileUpload.length === 0) {
        $("#file").addClass("border-danger");
        $("#fileValidate").text("File chưa được upload! Vui lòng upload file source code");
        return false;
    }
    form.submit();
    return false;
}