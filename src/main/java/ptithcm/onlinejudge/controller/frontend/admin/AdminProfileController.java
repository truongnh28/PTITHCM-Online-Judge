package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ptithcm.onlinejudge.dto.PasswordChangeDTO;
import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.mapper.TeacherMapper;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.TeacherManagementService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/profile")
public class AdminProfileController {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TeacherManagementService teacherManagementService;

    @GetMapping("")
    public String showUpdateProfilePage(Model model, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        model.addAttribute("pageTitle", "Thông tin cá nhân");
        String id = session.getAttribute("user").toString();
        ResponseObject getAdmin = teacherManagementService.getTeacherById(id);
        if (!getAdmin.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        TeacherDTO admin = teacherMapper.entityToDTO((Teacher) getAdmin.getData());
        model.addAttribute("admin", admin);
        model.addAttribute("changePassword", new PasswordChangeDTO());
        return "/admin/profile";
    }

    @PostMapping("/info")
    public String updateProfile(@ModelAttribute("admin") TeacherDTO teacher, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        String id = session.getAttribute("user").toString();
        ResponseObject updateProfileResponse = teacherManagementService.updateTeacher(id, teacher);
        if (!updateProfileResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/profile";
    }

    @PostMapping("/password")
    public String changePassword(@ModelAttribute("changePassword") PasswordChangeDTO passwordChangeDTO, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        String id = session.getAttribute("user").toString();
        ResponseObject changePasswordResponse = teacherManagementService.changePassword(id, passwordChangeDTO);
        if (!changePasswordResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/admin/profile";
    }

    private boolean isExpired(HttpSession session) {
        return session.getAttribute("user") == null;
    }
}
