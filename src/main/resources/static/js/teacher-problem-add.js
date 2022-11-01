function checkUniqueProblem(form) {
    $("#formAddProblem input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    $("#formAddProblem input[type=number]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    $("#formAddProblem input[type=file]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    $("#problemTypeValidate").text("");
    let errors = new Map();
    let problemText = new Map();
    let problemNumber = new Map();
    $("#formAddProblem input[type=text]").each(function () {
        problemText.set(this.id, 100);
    });
    $("#formAddProblem input[type=number]").each(function () {
        if (this.id.endsWith("TimeLimit"))
            problemNumber.set(this.id, [1, 20]);
        if (this.id.endsWith("MemoryLimit"))
            problemNumber.set(this.id, [1, 1024]);
        if (this.id.endsWith("Score"))
            problemNumber.set(this.id, [10, 100]);
    });
    problemText.forEach((value, key) => {
        if ($("#" + key).val().trim().length === 0)
            errors.set(key, "Không được bỏ trống");
        if ($("#" + key).val().trim().length > value)
            errors.set(key, `Không được quá ${value} ký tự`);
    })
    problemNumber.forEach((value, key) => {
        if ($("#" + key).val() < value[0] || $("#" + key).val() > value[1])
            errors.set(key, `Chỉ nhận giá trị trong đoạn [${value[0]}, ${value[1]}]`);
    })
    let countChecked = 0;
    $("#formAddProblem input[type=checkbox]").each(function () {
        if (this.checked)
            countChecked += 1;
    });
    if (countChecked === 0) {
        errors.set("problemType", "Vui lòng chọn ít nhất một thể loại");
    }
    let problemDescriptionFile = document.getElementById("problemDescription").files;
    let problemTestInFiles = document.getElementById("problemTestIn").files;
    let problemTestOutFiles = document.getElementById("problemTestOut").files;
    if (problemDescriptionFile.length === 0) {
        errors.set("problemDescription", "Vui lòng upload file mô tả bài tập");
    } else {
        let fileName = problemDescriptionFile[0].name;
        let extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (extension !== "pdf") {
            errors.set("problemDescription", "Vui lòng upload file mô tả bài tập có đuôi là pdf");
        }
    }
    if (problemTestInFiles.length !== problemTestOutFiles.length) {
        errors.set("problemTestIn", "Số lượng file input và output phải giống nhau");
        errors.set("problemTestOut", "Số lượng file input và output phải giống nhau");
    } else {
        const len = problemTestOutFiles.length;
        for (let i = 0; i < len; i++) {
            let fileIn = problemTestInFiles[i].name;
            let fileOut = problemTestOutFiles[i].name;
            if (fileIn.lastIndexOf('.') === -1) {
                errors.set("problemTestIn", "Vui lòng upload toàn bộ file test có đuôi là .in");
                break;
            }
            if (fileOut.lastIndexOf('.') === -1) {
                errors.set("problemTestOut", "Vui lòng upload toàn bộ file test có đuôi là .out");
                break;
            }
            const extIn = fileIn.substring(fileIn.lastIndexOf('.') + 1);
            const extOut = fileOut.substring(fileOut.lastIndexOf('.') + 1);
            if (extIn !== "in") {
                errors.set("problemTestIn", "Vui lòng upload toàn bộ file test có đuôi là .in");
                break;
            }
            if (extOut !== "out") {
                errors.set("problemTestOut", "Vui lòng upload toàn bộ file test có đuôi là .out");
                break;
            }
            const baseIn = fileIn.substring(0, fileIn.lastIndexOf('.'));
            const baseOut = fileOut.substring(0, fileOut.lastIndexOf('.'));
            if (baseIn !== baseOut) {
                errors.set("problemTestIn", "Vui lòng upload các file input có tên giống với file output");
                errors.set("problemTestOut", "Vui lòng upload các file output có tên giống với file in");
                break;
            }
        }
    }
    if (errors.size > 0) {
        errors.forEach((value, key) => {
            $("#" + key).addClass("border-danger");
            $("#" + key + "Validate").text(value);
        })
        return false;
    }
    const url = `/teacher/problem/${$("#problemId").val()}`;
    $.post(url, null, function (response) {
        if (response.status === "OK")
            form.submit();
        else if (response.status === "FOUND") {
            $("#modalNotificationTitle").text("Cảnh báo");
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