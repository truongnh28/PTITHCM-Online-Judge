function checkInfo(form, classId, groupId, contestId) {
    $("#formEditContest input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    $("#formEditContest input[type=datetime-local]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    })
    console.log("OK")
    const errors = new Map();
    const contest = new Map([
        ["contestIdEdit", [$("#contestIdEdit").val(), 100]],
        ["contestNameEdit", [$("#contestNameEdit").val(), 100]],
    ]);
    contest.forEach(function (value, key) {
        if (value[0].trim().length === 0)
            errors.set(key, "Không được bỏ trống");
        if (value[0].trim().length > value[1])
            errors.set(key, "Không được quá " + value[1] + " ký tự");
    });
    let start = $("#contestStartEdit").val();
    let end = $("#contestEndEdit").val();
    if (start.trim().length === 0) {
        errors.set("contestStartEdit", "Không được bỏ trống");
    }
    if (end.trim().length === 0) {
        errors.set("contestEndEdit", "Không được bỏ trống");
    }
    start = new Date(start);
    end = new Date(end);
    if (start >= end) {
        errors.set("contestStartEdit", "Thời gian bắt đầu phải trước thời gian kết thúc");
        errors.set("contestEndEdit", "Thời gian bắt đầu phải trước thời gian kết thúc");
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
            $("#modalNotificationMessage").text("Lỗi không xác định! Vui lòng kiểm tra lại");
            $("#modalNotification").modal("show");
        }
    }).fail(function () {
        $("#modalNotificationTitle").text("Lỗi server");
        $("#modalNotificationMessage").text("Lỗi kết nối đến server! Vui lòng kiểm tra lại");
        $("#modalNotification").modal("show");
    });
    return false;
}