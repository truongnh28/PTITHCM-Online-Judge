function clearSearch(subjectId, classId) {
    window.location = `/admin/subject/${subjectId}/class/${classId}/group`;
}

$(".table .btn-primary").on("click", function (e) {
    $("#formEditGroup input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    e.preventDefault();
    const url = $(this).attr("href");
    $.get(url, function (response) {
        if (response.status === "OK") {
            $("#groupIdEdit").val(response.data.id);
            $("#groupNameEdit").val(response.data.subjectClassGroupName);
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

function checkInfo(form) {
    $("#formEditGroup input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    const errors = new Map();
    const groupEdit = new Map([
        ["groupNameEdit", [$("#groupNameEdit").val(), 100]],
    ]);
    groupEdit.forEach(function (value, key) {
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

function checkUniqueGroup(form, subjectId, classId) {
    $("#formAddGroup input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    })
    const errors = new Map();
    const group = new Map([
        ["groupId", [$("#groupId").val(), 50]],
        ["groupName", [$("#groupName").val(), 100]]
    ]);
    group.forEach(function (value, key) {
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
    const url = `/admin/subject/${subjectId}/class/${classId}/group/${group.get('groupId')[0]}`;
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

function checkGetOne(subject, subjectClass, groupId, action) {
    const url = `/admin/subject/${subject}/class/${subjectClass}/group/${groupId}`;
    $.get(url, function (response) {
        if (response.status === "OK") {
            $("#modalConfirmTitle").text("Cảnh báo");
            if (action === 'lock') {
                $("#modalConfirmMessage").text("Bạn có thực sự muốn khóa nhóm thực hành có mã số " + groupId);
                $("#yes").attr("href", `/admin/subject/${subject}/class/${subjectClass}/group/${groupId}/lock`);
                $("#modalConfirm").modal("show");
            } else {
                $("#modalConfirmMessage").text("Bạn có thực sự muốn mở khóa nhóm thực hành có mã số " + groupId);
                $("#yes").attr("href", `/admin/subject/${subject}/class/${subjectClass}/group/${groupId}/unlock`);
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