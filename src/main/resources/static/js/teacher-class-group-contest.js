function clearSearch(classId, groupId) {
    window.location = `/teacher/class/${classId}/group/${groupId}/contest/page/1`
}

function checkGetOne(classId, groupId, contestId, action) {
    const url = `/teacher/class/${classId}/group/${groupId}/contest/${contestId}`;
    $.get(url, function (response) {
        if (response.status === "OK") {
            $("#modalConfirmTitle").text("Cảnh báo");
            if (action === 'lock') {
                console.log("OK");
                $("#modalConfirmMessage").text("Bạn có thực sự muốn khóa bài thực hành có mã số " + contestId);
                $("#yes").attr("href", `/teacher/class/${classId}/group/${groupId}/contest/${contestId}/lock`);
                $("#modalConfirm").modal("show");
            } else {
                $("#modalConfirmMessage").text("Bạn có thực sự muốn mở khóa bài thực hành có mã số " + contestId);
                $("#yes").attr("href", `/teacher/class/${classId}/group/${groupId}/contest/${contestId}/unlock`);
                $("#modalConfirm").modal("show");
            }
        } else if (response.status === "FOUND") {
            $("#modalNotificationTitle").text("Lỗi server");
            $("#modalNotificationMessage").text(response.message);
            $("#modalNotification").modal("show");
        } else {
            $("#modalNotificationTitle").text("Lỗi server");
            $("#modalNotificationMessage").text("Lỗi không xác định! Vui lòng kiểm tra lại");
            $("#modalNotification").modal("show");
        }
    }).fail(function () {
        $("#modalNotificationMessage").text("Lỗi kết nối đến server! Vui lòng kiểm tra lại");
        $("#modalNotificationTitle").text("Lỗi server");
        $("#modalNotification").modal("show");
    });
}

function checkUniqueContest(form, classId, groupId) {
    $("#formAddContest input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    $("#formAddContest input[type=datetime-local]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    })
    const errors = new Map();
    const contest = new Map([
        ["contestId", [$("#contestId").val(), 100]],
        ["contestName", [$("#contestName").val(), 100]],
    ]);
    contest.forEach(function (value, key) {
        if (value[0].trim().length === 0)
            errors.set(key, "Không được bỏ trống");
        if (value[0].trim().length > value[1])
            errors.set(key, "Không được quá " + value[1] + " ký tự");
    });
    let start = $("#contestStart").val();
    let end = $("#contestEnd").val();
    if (start.trim().length === 0) {
        errors.set("contestStart", "Không được bỏ trống");
    }
    if (end.trim().length === 0) {
        errors.set("contestEnd", "Không được bỏ trống");
    }
    start = new Date(start);
    end = new Date(end);
    if (start >= end) {
        errors.set("contestStart", "Thời gian bắt đầu phải trước thời gian kết thúc");
        errors.set("contestEnd", "Thời gian bắt đầu phải trước thời gian kết thúc");
    }
    if (errors.size > 0) {
        errors.forEach(function (value, key) {
            $("#" + key).addClass("border-danger");
            $("#" + key + "Validate").text(value);
        });
        return false;
    }

    const url = `/teacher/class/${classId}/group/${groupId}/contest/${contest.get("contestId")[0]}`
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