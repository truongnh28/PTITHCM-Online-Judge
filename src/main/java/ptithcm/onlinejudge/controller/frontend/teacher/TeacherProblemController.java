package ptithcm.onlinejudge.controller.frontend.teacher;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.*;
import ptithcm.onlinejudge.model.ResponseObject;
import ptithcm.onlinejudge.services.UploadFileService;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/problem")
public class TeacherProblemController {
    @Autowired
    private UploadFileService uploadFileService;
    @GetMapping("")
    public String showProblemList(Model model) {
        model.addAttribute("pageTitle", "Problem list");
        List<ProblemDTO> problemList = new ArrayList<>();
        for (ProblemDTO problem : Data.problemList)
            if (!problem.isHide())
                problemList.add(problem);
        model.addAttribute("problems", problemList);
        return "/teacher/problem";
    }

    @GetMapping("/add")
    public String showAddProblemForm(Model model) {
        model.addAttribute("pageTitle", "Add problem");
        ProblemDTO problem = new ProblemDTO();
        model.addAttribute("problem", problem);
        List<ProblemTypeDTO> problemTypeList = Data.problemTypeList;
        model.addAttribute("problemTypes", problemTypeList);
        List<LevelDTO> levelList = Data.levelList;
        model.addAttribute("levels", levelList);
        return "/teacher/add-problem";
    }

    private LevelDTO getLevelById(int id) {
        LevelDTO res = new LevelDTO();
        for (LevelDTO level : Data.levelList) {
            if (level.getLevelId() == id) {
                res = level;
                break;
            }
        }
        return res;
    }

    private ProblemTypeDTO getProblemTypeById(String problemTypeId) {
        ProblemTypeDTO res = new ProblemTypeDTO();
        for (ProblemTypeDTO problemType : Data.problemTypeList) {
            if (problemType.getProblemTypeId().equals(problemTypeId)) {
                res = problemType;
                break;
            }
        }
        return res;
    }

    private TeacherDTO getAuthorByUsername(String username) {
        TeacherDTO res = new TeacherDTO();
        for (TeacherDTO teacher : Data.teacherList) {
            if (teacher.getTeacherId().equals(username)) {
                res = teacher;
                break;
            }
        }
        return res;
    }

    private boolean checkFileIsNotUploaded(MultipartFile[] file) {
        return Arrays.asList(file).stream().filter(f -> !f.isEmpty()).count() == 0;
    }

    private boolean checkTestCaseUpload(MultipartFile[] testCasesIn, MultipartFile[] testCasesOut) {
        boolean testCaseInEmpty = checkFileIsNotUploaded(testCasesIn);
        boolean testCaseOutEmpty = checkFileIsNotUploaded(testCasesOut);
        // if both of them not upload then return true -> because test case can be empty
        if (testCaseInEmpty && testCaseOutEmpty) return true;
        // if one of them not upload then return false
        if ((!testCaseInEmpty && testCaseOutEmpty) || (testCaseInEmpty && !testCaseOutEmpty)) return false;
        // if upload but the number of file of input and output are not same
        if (testCasesOut.length != testCasesIn.length) return false;
        // if upload with the same number of file of input and output
        String[] fileNameInArray = new String[testCasesIn.length];
        String[] fileNameOutArray = new String[testCasesOut.length];
        fileNameInArray = Arrays.stream(testCasesIn).map(item -> item.getOriginalFilename()).collect(Collectors.toList()).toArray(fileNameInArray);
        Arrays.sort(fileNameInArray);
        fileNameOutArray = Arrays.stream(testCasesOut).map(item -> item.getOriginalFilename()).collect(Collectors.toList()).toArray(fileNameOutArray);
        Arrays.sort(fileNameOutArray);
        for (int i = 0; i < testCasesIn.length; ++i) {
            String fullNameIn = FilenameUtils.getName(fileNameInArray[i]);
            String fullNameOut = FilenameUtils.getName(fileNameOutArray[i]);
            // if file name not equal
            if (!fullNameIn.equals(fullNameOut) ||
                    !FilenameUtils.getExtension(fullNameIn).equals("in") ||
                    !FilenameUtils.getExtension(fullNameOut).equals("out")
            )
                return false;
        }
        return true;
    }

    private String helperUploadToCloud(MultipartFile multipartFile) {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String fileNameServer = UUID.randomUUID().toString().replace("-", "");
        String uploadDir = "./uploads/";
        Path uploadPath = Paths.get(uploadDir);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileExtension = FilenameUtils.getExtension(fileName);
            fileNameServer = fileNameServer + "." + fileExtension;
            Path fileServerPath = uploadPath.resolve(fileNameServer).toAbsolutePath();
            InputStream inputStream = multipartFile.getInputStream();
            Files.copy(inputStream, fileServerPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return uploadDir + fileNameServer;
    }

    private boolean helperDeleteTempFile(String filePath) {
        if (Files.exists(Path.of(filePath))) {
            File file = new File(filePath);
            return file.delete();
        }
        return false;
    }

    @PostMapping("/add")
    public String addProblem(@ModelAttribute("problem") ProblemDTO problem,
                             @RequestParam("levelSelect") int levelId,
                             @RequestParam("fileDescription") MultipartFile fileDescription,
                             @RequestParam("testCasesIn") MultipartFile[] testCasesIn,
                             @RequestParam("testCasesOut") MultipartFile[] testCasesOut,
                             @RequestParam("problemTypeIdList") String[] problemTypeIdList,
                             HttpSession session) {
        String username = ((UserLogin) session.getAttribute("user")).getUsername();
        System.out.println("Problem has been posted!");

        // check if file is not upload
        if (fileDescription.isEmpty()) {
            return "redirect:/error";
        }
        // upload file description
        String filePath = helperUploadToCloud(fileDescription);
        System.out.println(filePath);
        // upload to cloud successfully
        ResponseObject responseObject = uploadFileService.uploadFile(filePath);
        if (!responseObject.getMessage().equals("Success")) {
            return "redirect:/error";
        }
        Map cloudinaryResponse = (Map) responseObject.getData();
        String pathCloud = cloudinaryResponse.get("url").toString();
        // check if server cannot delete then return error
        if (!helperDeleteTempFile(filePath))
            return "redirect:/error";
        // set the problem description link
        problem.setProblemDescription(pathCloud);

        // check if every pair of test case uploaded has same name
        if (!checkTestCaseUpload(testCasesIn, testCasesOut)) {
            return "redirect:/error";
        }

        // set the value from the form
        problem.setLevel(getLevelById(levelId));
        problem.setAuthor(getAuthorByUsername(username));
        problem.setHide(false);
        System.out.println(problem.toString());
        return "redirect:/teacher/problem";
    }
}
