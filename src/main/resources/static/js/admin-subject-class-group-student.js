function clearSearch(subjectId, classId, groupId) {
    window.location = `/admin/subject/${subjectId}/class/${classId}/group/${groupId}/student/page/1`;
}

function checkGetOne(subjectId, classId, groupId, studentId) {
    const url = `/admin/subject/${subjectId}/class/${classId}/group/${groupId}/student/${studentId}`;
    $.get(url, function (res) {
        if (res.status === "OK") {
            $("#modalConfirmTitle").text("Cảnh báo");
            $("#modalConfirmMessage").text(`Bạn có thật sự muốn xóa khỏi nhóm sinh viên có mã số ${studentId}?`);
            $("#yes").attr('href', url + "/remove");
            $("#modalConfirm").modal("show");
        } else if (res.status === "FOUND") {
            $("#modalNotificationTitle").text("Lỗi server");
            $("#modalNotificationMessage").text(res.message);
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
    })
}