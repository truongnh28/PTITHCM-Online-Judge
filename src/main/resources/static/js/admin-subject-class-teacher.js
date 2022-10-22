function removeTeacher(subjectId, classId, teacherId) {
    const urlSubject = `/admin/subject/${subjectId}`;
    const urlClass = `/admin/subject/${subjectId}/class/${classId}`;
    const urlTeacher = `/admin/teacher/${teacherId}`;
    let validate = [false, false, false]
    $.get(urlSubject, function (res) {
        if (res.status !== "OK") {
            $("#modalNotificationTitle").text("Lỗi server");
            $("#modalNotificationMessage").text("Lỗi không xác định! Vui lòng kiểm tra lại server");
            if (res.status === "FOUND") {
                $("#modalNotificationMessage").text(res.message);
            }
            $("#modalNotification").modal("show");
        }
        validate[0] = true;
    }).fail(function () {
        $("#modalNotificationTitle").text("Lỗi server");
        $("#modalNotificationMessage").text("Lỗi kết nối đến server! Vui lòng kiểm tra lại server");
        $("#modalNotification").modal("show");
    })

    $.get(urlClass, function (res) {
        if (res.status !== "OK") {
            $("#modalNotificationTitle").text("Lỗi server");
            $("#modalNotificationMessage").text("Lỗi không xác định! Vui lòng kiểm tra lại server");
            if (res.status === "FOUND") {
                $("#modalNotificationMessage").text(res.message);
            }
            $("#modalNotification").modal("show");
        }
        validate[1] = true
    }).fail(function () {
        $("#modalNotificationTitle").text("Lỗi server");
        $("#modalNotificationMessage").text("Lỗi kết nối đến server! Vui lòng kiểm tra lại server");
        $("#modalNotification").modal("show");
    });

    $.get(urlTeacher, function (res) {
        if (res.status !== "OK") {
            $("#modalNotificationTitle").text("Lỗi server");
            $("#modalNotificationMessage").text("Lỗi không xác định! Vui lòng kiểm tra lại server");
            if (res.status === "FOUND") {
                $("#modalNotificationMessage").text(res.message);
            }
            $("#modalNotification").modal("show");
        }
        validate[2] = true;
    }).fail(function () {
        $("#modalNotificationTitle").text("Lỗi server");
        $("#modalNotificationMessage").text("Lỗi kết nối đến server! Vui lòng kiểm tra lại server");
        $("#modalNotification").modal("show");
    });
    let ok = true;
    validate.forEach(function (value) {
        if (value === true) {
            ok = false;
        }
    })
    if (ok === true) {
        const url = `/admin/subject/${subjectId}/class/${classId}/teacher/${teacherId}/remove`;
        $("#modalConfirmTitle").text("Cảnh báo");
        $("#modalConfirmMessage").text(`Bạn có chắc muốn xóa giáo viên có mã ${teacherId} khỏi nhóm quản lý?`)
        $("#yes").attr('href', url);
        $("#modalConfirm").modal('show');
    }

}