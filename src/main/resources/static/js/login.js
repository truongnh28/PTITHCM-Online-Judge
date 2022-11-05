function checkInfo(form) {
    const usernameElement = document.getElementById("username");
    usernameElement.classList.remove("border-danger");
    const passwordElement = document.getElementById("password");
    passwordElement.classList.remove("border-danger");
    const username = usernameElement.value;
    const password = passwordElement.value;
    let errors = new Map();
    if (username.length === 0) {
        errors.set("username", "Không được bỏ trống");
    }
    if (password.length === 0) {
        errors.set("password", "Không được bỏ trống");
    }
    if (errors.size > 0) {
        let message = "";
        if (errors.size === 1) {
            errors.forEach(function (value, key) {
                message += value;
                if (key === "username")
                    message += " tên đăng nhập";
                else
                    message += " mật khẩu";
            })
        } else {
            message += "Không được bỏ trống tên đăng nhập và mật khẩu!";
        }
        errors.forEach(function (value, key) {
            $("#" + key).addClass("border-danger");
        })
        $("#modalNotificationTitle").text("Cảnh báo");
        $("#modalNotificationMessage").text(message);
        $("#modalNotification").modal("show");
        return false;
    }
    const urlStudent = `/check/student`; let isStudent = true;
    const urlTeacher = `/check/teacher`; let isTeacher = true;
    $.post(urlStudent, {username: username, password: password}, function (response) {
        if (response.status === "OK") {
            form.submit();
            return false;
        } else {
            if (response.data === -1)
                isStudent = false;
            else if (response.data === 0) {
                $("#password").val("");
                $("#modalNotificationTitle").text("Cảnh báo");
                $("#modalNotificationMessage").text(response.message);
                $("#modalNotification").modal("show");
                return false;
            } else if (response.data === 2) {
                $("#password").val("");
                $("#modalNotificationTitle").text("Cảnh báo");
                $("#modalNotificationMessage").text(response.message);
                $("#modalNotification").modal("show");
                return false;
            }
        }
    });

    $.post(urlTeacher, {username: username, password: password}, function (response) {
        if (response.status === "OK") {
            form.submit();
            return false;
        } else {
            if (response.data === -1)
                isTeacher = false;
            else if (response.data === 0) {
                $("#password").val("");
                $("#modalNotificationTitle").text("Cảnh báo");
                $("#modalNotificationMessage").text(response.message);
                $("#modalNotification").modal("show");
                return false;
            } else if (response.data === 2) {
                $("#password").val("");
                $("#modalNotificationTitle").text("Cảnh báo");
                $("#modalNotificationMessage").text(response.message);
                $("#modalNotification").modal("show");
                return false;
            }
        }
    });

    if (!isStudent && !isTeacher) {
        $("#modalNotificationTitle").text("Cảnh báo");
        $("#modalNotificationMessage").text("Sai thông tin đăng nhập");
        $("#modalNotification").modal("show");
    }
    return false;
}