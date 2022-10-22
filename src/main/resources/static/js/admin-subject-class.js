function clearSearch(subject) {
    window.location = `/admin/subject/${subject}/class`;
}

$(".table .btn-primary").on("click", function (e) {
    $("#formEditClass input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    e.preventDefault();
    const url = $(this).attr("href");
    $.get(url, function (response) {
        if (response.status === "OK") {
            $("#classIdEdit").val(response.data.id);
            $("#classNameEdit").val(response.data.subjectClassName);
            $("#modalEdit").modal("show");
        } else if (response.status === "FOUND") {
            $("#modalNotificationTitle").text("Lỗi server");
            $("#modalNotificationMessage").text(response.message);
            $("#modalNotification").modal("show");
        } else {
            $("#modalNotificationTitle").text("Lỗi server");
            $("#modalNotificationMessage").text("Lỗi không xác định");
            $("#modalNotification").modal("show");
        }
    }).fail(function () {
        $("#modalNotificationTitle").text("Lỗi server");
        $("#modalNotificationMessage").text("Không kết nối được với server");
        $("#modalNotification").modal("show");
    })
})

function checkGetOne(subject, subjectClass, action) {
    const url = `/admin/subject/${subject}/class/${subjectClass}`;
    $.get(url, function (response) {
        if (response.status === "OK") {
            $("#modalConfirmTitle").text("Cảnh báo");
            if (action === 'lock') {
                $("#modalConfirmMessage").text("Bạn có thực sự muốn khóa lớp có mã số " + subjectClass);
                $("#yes").attr("href", `/admin/subject/${subject}/class/${subjectClass}/lock`);
                $("#modalConfirm").modal("show");
            } else {
                $("#modalConfirmMessage").text("Bạn có thực sự muốn mở khóa lớp có mã số " + subjectClass);
                $("#yes").attr("href", `/admin/subject/${subject}/class/${subjectClass}/unlock`);
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
    }).fail(function () {
        $("#modalNotificationMessage").text("Lỗi kết nối đến server! Vui lòng kiểm tra lại");
        $("#modalNotificationTitle").text("Lỗi server");
        $("#modalNotification").modal("show");
    });
}

function checkInfo(form) {
    $("#formEditClass input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    const errors = new Map();
    const classEdit = new Map([
        ["classNameEdit", [$("#classNameEdit").val(), 100]],
    ]);
    classEdit.forEach(function (value, key) {
        if (value[0].trim().length === 0)
            errors.set(key, "Không được bỏ trống");
        if (value[0].trim().length > value[1])
            errors.set(key, "Không được quá " + value[1] + " ký tự");
    });
    if (errors.size > 0) {
        errors.forEach(function (value, key) {
            $("#" + key).addClass("border-danger");
            $("#" + key + "Validate").text(value);
        });
        return false;
    }
    form.submit();
    return false;
}

function checkUniqueClass(form) {
    $("#formAddClass input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    })
    const errors = new Map();
    const subjectClass = new Map([
        ["classId", [$("#classId").val(), 50]],
        ["className", [$("#className").val(), 100]]
    ]);
    subjectClass.forEach(function (value, key) {
        if (value[0].trim().length === 0)
            errors.set(key, "Không được bỏ trống");
        if (value[0].trim().length > value[1])
            errors.set(key, "Không được quá " + value[1] + " ký tự");
    });
    if (errors.size > 0) {
        errors.forEach(function (value, key) {
            $("#" + key).addClass("border-danger");
            $("#" + key + "Validate").text(value);
        })
        return false;
    }
    const subjectId = new URL(window.location.href).pathname.split("/")[3];
    const url = "/admin/subject/" + subjectId + "/class/" + subjectClass.get("classId")[0];
    $.post(url, null, function (response) {
        if (response.status === "OK")
            form.submit();
        else if (response.status === "FOUND") {
            $("#modalNotificationTitle").text("Lỗi server");
            $("#modalNotificationMessage").text(response.message);
            $("#modalNotification").modal("show");
        } else {
            $("#modalNotificationTitle").text("Lỗi server");
            $("#modalNotificationMessage").text("Lỗi không xác định");
            $("#modalNotification").modal("show");
        }
    }).fail(function () {
        $("#modalNotificationTitle").text("Lỗi server");
        $("#modalNotificationMessage").text("Lỗi kết nối đến server");
        $("#modalNotification").modal("show");
    });
    return false;
}