package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.onlinejudge.dto.PasswordChangeDTO;
import ptithcm.onlinejudge.helper.SHA256Helper;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.TeacherRepository;

@RestController
public class TeacherChangePasswordBackendController {
    @Autowired
    private TeacherRepository teacherRepository;

    @PostMapping("/admin/profile/change-password")
    public ResponseObject checkInfo(String id, PasswordChangeDTO passwordChange) {
        System.out.println(passwordChange);
        Teacher teacher = teacherRepository.findById(id).get();
        String oldPass = teacher.getPassword();
        String inputOldPass = passwordChange.getOldPassword();
        String hashInput = SHA256Helper.hash(inputOldPass);
        if (!hashInput.equals(oldPass))
            return new ResponseObject(HttpStatus.FOUND, "Mật khẩu cũ không đúng! Vui lòng nhập lại", null);
        String inputNewPass = passwordChange.getNewPassword();
        String inputConfirmPass = passwordChange.getConfirmPassword();
        if (!inputNewPass.equals(inputConfirmPass))
            return new ResponseObject(HttpStatus.FOUND, "Không mật khẩu mới và mật khẩu xác nhận phải giống nhau", null);
        return new ResponseObject(HttpStatus.OK, "Success", null);
    }
}
