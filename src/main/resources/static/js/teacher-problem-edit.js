function checkInfo(form) {
    $("#formEditProblem input[type=text]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    $("#formEditProblem input[type=number]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    $("#formEditProblem input[type=file]").each(function () {
        $("#" + this.id).removeClass("border-danger");
        $("#" + this.id + "Validate").text("");
    });
    $("#problemTypeValidate").text("");
    let errors = new Map();
    let problemText = new Map();
    let problemNumber = new Map();
    $("#formEditProblem input[type=text]").each(function () {
        problemText.set(this.id, 100);
    });
    $("#formEditProblem input[type=number]").each(function () {
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
    });
    console.log(problemText);
    problemNumber.forEach((value, key) => {
        if ($("#" + key).val() < value[0] || $("#" + key).val() > value[1])
            errors.set(key, `Chỉ nhận giá trị trong đoạn [${value[0]}, ${value[1]}]`);
    });
    let countChecked = 0;
    $("#formEditProblem input[type=checkbox]").each(function () {
        if (this.checked)
            countChecked += 1;
    });
    if (countChecked === 0) {
        errors.set("problemType", "Vui lòng chọn ít nhất một thể loại");
    }
    let problemDescriptionFile = document.getElementById("problemDescription").files;
    if (problemDescriptionFile.length > 0) {
        let fileName = problemDescriptionFile[0].name;
        let extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (extension !== "pdf") {
            errors.set("problemDescription", "Vui lòng upload file mô tả bài tập có đuôi là pdf");
        }
    }
    if (errors.size > 0) {
        errors.forEach((value, key) => {
            $("#" + key).addClass("border-danger");
            $("#" + key + "Validate").text(value);
        })
        return false;
    }
    form.submit();
    return false;
}