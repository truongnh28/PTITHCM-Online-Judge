function checkTestCase(form) {
    let errors = new Map();
    let problemTestInFiles = document.getElementById("problemTestIn").files;
    let problemTestOutFiles = document.getElementById("problemTestOut").files;
    if (problemTestInFiles.length === 0 && problemTestOutFiles.length === 0) {
        errors.set("problemTestIn", "File input và output phải được upload");
        errors.set("problemTestOut", "File input và output phải được upload");
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
    form.submit();
    return false;
}