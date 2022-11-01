function clearSearch() {
    window.location = "/admin/teacher";
}

$(".table .btn-primary").on("click", function (e) {
    $("#formEditTeacher input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    e.preventDefault();
    const url = $(this).attr("href");
    $.get(url, function (response) {
        if (response.status === "OK") {
            $("#teacherIdEdit").val(response.data.id);
            $("#teacherFirstNameEdit").val(response.data.teacherFirstName);
            $("#teacherLastNameEdit").val(response.data.teacherLastName);
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

function checkGetOne(id, name) {
    const url = "/admin/teacher/" + id;
    $.get(url, function (response) {
        if (response.status === "OK") {
            $("#modalConfirmTitle").text("Cảnh báo");
            if (name === 'lock') {
                $("#modalConfirmMessage").text("Bạn có thực sự muốn khóa giáo viên có mã số " + id);
                $("#yes").attr("href", "/admin/teacher/" + id + "/lock");
                $("#modalConfirm").modal("show");
            } else {
                $("#modalConfirmMessage").text("Bạn có thực sự muốn mở khóa giáo viên có mã số " + id);
                $("#yes").attr("href", "/admin/teacher/" + id + "/unlock");
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
        $("#modalNotificationMessage").text("Lỗi kết nối đến server");
        $("#modalNotificationTitle").text("Lỗi server");
        $("#modalNotification").modal("show");
    });
}

function checkInfo(form) {
    $("#formEditTeacher input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    const errors = new Map();
    const teacherEdit = new Map([
        ["teacherLastNameEdit", [$("#teacherLastNameEdit").val(), 100]],
        ["teacherFirstNameEdit", [$("#teacherFirstNameEdit").val(), 100]],
    ]);
    teacherEdit.forEach(function (value, key) {
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

function checkUniqueTeacher(form) {
    $("#formAddTeacher input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    })
    const errors = new Map();
    const teacher = new Map([
        ["teacherId", [$("#teacherId").val(), 10]],
        ["teacherLastName", [$("#teacherLastName").val(), 100]],
        ["teacherFirstName", [$("#teacherFirstName").val(), 100]],
    ]);
    teacher.forEach(function (value, key) {
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
    const url = "/admin/teacher/" + teacher.get("teacherId")[0];
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