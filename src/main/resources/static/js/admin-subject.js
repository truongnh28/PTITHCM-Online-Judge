$(".table .btn-primary").on("click", function (e) {
    $("#subjectNameEdit").removeClass("border-danger");
    $("#subjectNameEditValidate").text("");
    e.preventDefault();
    const href = $(this).attr("href");
    $.get(href, function (response) {
        if (response.status === "OK") {
            $("#subjectIdEdit").val(response.data.id);
            $("#subjectNameEdit").val(response.data.subjectName);
            $("#modalEdit").modal("show");
        } else if (response.status === "FOUND") {
            $("#modalNotificationTitle").text("Cảnh báo");
            $("#modalNotificationMessage").text(response.message);
            $("#modalNotification").modal("show");
        }
    })
});

function clearSearch() {
    window.location = "/admin/subject";
}

function checkGetOne(id, name) {
    const url = "/admin/subject/" + id;
    $.get(url, function (response){
        if (response.status === "OK") {
            $("#modalConfirmTitle").text("Cảnh báo");
            if (name === 'lock') {
                $("#modalConfirmMessage").text("Bạn có thực sự muốn khóa môn học này?");
                $("#yes").attr("href", "/admin/subject/" + id + "/lock");
                $("#modalConfirm").modal("show");
            } else {
                $("#modalConfirmMessage").text("Bạn có thực sự muốn mở khóa môn học này?");
                $("#yes").attr("href", "/admin/subject/" + id + "/unlock");
                $("#modalConfirm").modal("show");
            }
        } else if (response.status === "FOUND") {
            $("#modalNotificationTitle").text("Lỗi server");
            $("#modalNotificationMessage").text(response.message);
            $("#modalNotification").modal("show");
        } else {
            $("#modalNotificationTitle").text("Lỗi server");
            $("#modalNotificationMessage").text("Lỗi không xác định");
            $("#modalNotification").modal("show");
        }
    }).fail(function (){
        $("#modalNotificationMessage").text("Lỗi kết nối đến server");
        $("#modalNotificationTitle").text("Lỗi server");
        $("#modalNotification").modal("show");
    })

}

function checkInfo(form) {
    $("#subjectNameEdit").removeClass("border-danger");
    $("#subjectNameEditValidate").text("");
    const errors = new Map();
    const subjectNameEdit = $("#subjectNameEdit").val();
    if (subjectNameEdit.trim().length === 0)
        errors.set("subjectNameEdit", "Không được để trống");
    if (subjectNameEdit.trim().length > 100)
        errors.set("subjectNameEdit", "Không được quá 100 ký tự");
    if (errors.size > 0) {
        errors.forEach(function (value, key) {
            $("#" + key).addClass("border-danger");
            $("#" + key + "Validate").text(value);
        })
        return false;
    }
    form.submit();
    return false;
}

function checkUniqueSubject(form) {
    $("#subjectId").removeClass("border-danger");
    $("#subjectIdValidate").text("");
    $("#subjectName").removeClass("border-danger");
    $("#subjectNameValidate").text("");
    const errors = new Map();
    const subjectId = $("#subjectId").val();
    const subjectName = $("#subjectName").val();
    if (subjectId.trim().length === 0)
        errors.set("subjectId", "Không được để trống");
    if (subjectId.trim().length > 10)
        errors.set("subjectId", "Không được quá 10 ký tự");
    if (subjectName.trim().length === 0)
        errors.set("subjectName", "Không được để trống");
    if (subjectName.trim().length > 100)
        errors.set("subjectName", "Không được quá 100 ký tự");
    if (errors.size > 0) {
        errors.forEach(function (value, key) {
            $("#" + key).addClass("border-danger");
            $("#" + key + "Validate").text(value);
        });
        return false;
    }
    let url = "/admin/subject/" + subjectId;
    $.post(url, null, function (response) {
        if (response.status === "OK")
            form.submit();
        else if (response.status === "FOUND") {
            $("#modalNotificationTitle").text("Cảnh báo");
            $("#modalNotificationMessage").text(response.message);
            $("#modalNotification").modal("show");
        }
    }).fail(function () {
        $("#modalNotificationTitle").text("Lỗi server");
        $("#modalNotificationMessage").text("Lỗi kết nối đến server");
        $("#modalNotification").modal("show");
    })
    return false;
}