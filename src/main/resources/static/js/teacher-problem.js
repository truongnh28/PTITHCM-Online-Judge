function clearSearch() {
    window.location = "/teacher/problem/page/1";
}

function checkGetOne(problemId, action) {
    const url = "/teacher/problem/" + problemId;
    $.get(url, function (response){
        if (response.status === "OK") {
            $("#modalConfirmTitle").text("Cảnh báo");
            if (action === 'lock') {
                $("#modalConfirmMessage").text(`Bạn có thực sự muốn khóa bài tập có mã số ${problemId}?`);
                $("#yes").attr("href", "/teacher/problem/" + problemId + "/lock");
                $("#modalConfirm").modal("show");
            } else {
                $("#modalConfirmMessage").text(`Bạn có thực sự muốn mở khóa bài tập có mã số ${problemId}?`);
                $("#yes").attr("href", "/teacher/problem/" + problemId + "/unlock");
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