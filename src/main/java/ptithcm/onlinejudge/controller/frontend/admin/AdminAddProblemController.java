package ptithcm.onlinejudge.controller.frontend.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.ProblemDTO;
import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.dto.UserLogin;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.UploadFileService;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@Controller
public class AdminAddProblemController {
    @Autowired
    private UploadFileService uploadFileService;
    @GetMapping("/add-problem")
    public String getPageAddProblem(Model model) {
        ProblemDTO problemDetails = new ProblemDTO();
        model.addAttribute("problem", problemDetails);
        return "/admin/add-problem";
    }
    private String helperCreateTempFile(MultipartFile multipartFile) {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = "./uploads/";
        Path uploadPath = Paths.get(uploadDir);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = uploadPath.resolve(fileName).toAbsolutePath();
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return uploadDir + fileName;
    }
    private boolean helperDeleteTempFile(String path) {
        if (Files.exists(Path.of(path))) {
            File file = new File(path);
            return file.delete();
        }
        return false;
    }
    @PostMapping("/admin/problem/add")
    public String addProblem(HttpSession httpSession, @ModelAttribute("problem") ProblemDTO problem, @RequestParam("fileDescription") MultipartFile fileDescription) {
        UserLogin user = (UserLogin) httpSession.getAttribute("user");
        // TODO: find by username
        TeacherDTO foundTeacher = new TeacherDTO();
        for (TeacherDTO teacher: Data.teacherList) {
            if (teacher.getTeacherId().equals(user.getUsername())) {
                foundTeacher = teacher;
                break;
            }
        }
        String pathClient = helperCreateTempFile(fileDescription);
        ResponseObject responseObject = uploadFileService.uploadFile(pathClient);
        if (!responseObject.getMessage().equals("Success")) {
            return "redirect:/error";
        }
        Map cloudinaryResponse = (Map) responseObject.getData();
        String pathServer = cloudinaryResponse.get("url").toString();
        System.out.println(pathServer);
        if (!helperDeleteTempFile(pathClient)) {
            return "redirect:/error";
        }
        problem.setProblemUrl(pathServer);
        problem.setTeacher(foundTeacher);
        problem.setHide(false);
        Data.problemList.add(problem);
        return "redirect:/admin/problems";
    }
}
