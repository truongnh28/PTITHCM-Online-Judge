function clearSearch() {
    window.location = "/admin/subject/";
}

$(".table .btn-primary").on("click", function (e) {
    $("#formEditStudent input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    e.preventDefault();
    const url = $(this).attr("href");
    $.get(url, function (response) {
        if (response.status === "OK") {
            $("#studentIdEdit").val(response.data.id);
            $("#studentClassEdit").val(response.data.studentClass);
            $("#studentFirstNameEdit").val(response.data.studentFirstName);
            $("#studentLastNameEdit").val(response.data.studentLastName);
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
    const url = "/admin/student/" + id;
    $.get(url, function (response) {
        if (response.status === "OK") {
            $("#modalConfirmTitle").text("Cảnh báo");
            if (name === 'lock') {
                $("#modalConfirmMessage").text("Bạn có thực sự muốn khóa sinh viên có mã số " + id);
                $("#yes").attr("href", "/admin/student/" + id + "/lock");
                $("#modalConfirm").modal("show");
            } else {
                $("#modalConfirmMessage").text("Bạn có thực sự muốn mở khóa sinh viên có mã số " + id);
                $("#yes").attr("href", "/admin/student/" + id + "/unlock");
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
    $("#formEditStudent input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    const errors = new Map();
    const studentEdit = new Map([
        ["studentLastNameEdit", [$("#studentLastNameEdit").val(), 100]],
        ["studentFirstNameEdit", [$("#studentFirstNameEdit").val(), 100]],
        ["studentClass", [$("studentClassEdit").val(), 20]]
    ]);
    studentEdit.forEach(function (value, key) {
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

function checkUniqueStudent(form) {
    $("#formAddStudent input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    })
    const errors = new Map();
    const student = new Map([
        ["studentId", [$("#studentId").val(), 10]],
        ["studentLastName", [$("#studentLastName").val(), 100]],
        ["studentFirstName", [$("#studentFirstName").val(), 100]],
        ["studentClass", [$("#studentClass").val(), 20]]
    ]);
    student.forEach(function (value, key) {
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
    const url = "/admin/student/" + student.get("studentId")[0];
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