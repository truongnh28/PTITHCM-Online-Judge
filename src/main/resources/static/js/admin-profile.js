function checkInfo(form) {
    $("#adminLastName").removeClass("border-danger");
    $("#adminLastNameValidate").text("");
    $("#adminFirstName").removeClass("border-danger");
    $("#adminFirstNameValidate").text("");
    const lastName = $("#adminLastName").val();
    const firstName = $("#adminFirstName").val();
    let errors = new Map();
    if (lastName.trim().length === 0) {
        errors.set("adminLastName", "Không được bỏ trống");
    }
    if (lastName.trim().length > 100) {
        errors.set("adminLastName", "Không được quá 100 ký tự");
    }
    if (firstName.trim().length === 0) {
        errors.set("adminFirstName", "Không được bỏ trống");
    }
    if (firstName.trim().length > 100) {
        errors.set("adminLastName", "Không được quá 100 ký tự");
    }
    if (errors.size > 0) {
        errors.forEach((value, key) => {
            $("#" + key).addClass("border-danger");
            $("#" + key + "Validate").text(value);
        });
        return false;
    }
    form.submit();
    return false;
}

function checkPassword(form) {
    $("#changePassword input[type=password]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    const oldPassword = $("#oldPassword").val();
    const newPassword = $("#newPassword").val();
    const confirmPassword = $("#confirmPassword").val();
    let errors = new Map();
    $("#changePassword input[type=password]").each(function () {
        const value = $("#" + this.id).val();
        if (value.length === 0)
            errors.set(this.id, "Không được bỏ trống");
        if (value.length < 6 || value.length > 100)
            errors.set(this.id, "Mật khẩu có độ dài ít nhất là 6 và lớn nhất là 100 ký tự");
    });
    if (errors.size > 0) {
        errors.forEach((value, key) => {
            $("#" + key).addClass("border-danger");
            $("#" + key + "Validate").text(value);
        })
        return false;
    }
    const url = "/admin/profile/change-password";
    const data = {
        id: $("#adminId").text(),
        passwordChange: {
            oldPassword: $("#oldPassword").val(),
            newPassword: $("#newPassword").val(),
            confirmPassword: $("#confirmPassword").val()
        }
    }
    $.post(url, data, function (response) {
        if (response.status === "OK") {
            form.submit();
        } else {
            $("#modalNotificationTitle").text("Cảnh báo");
            $("#modalNotificationMessage").text(response.message);
            $("#modalNotification").modal("show");
            return false;
        }
    })
    return false;
}