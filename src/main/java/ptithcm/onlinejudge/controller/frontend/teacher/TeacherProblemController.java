package ptithcm.onlinejudge.controller.frontend.teacher;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.*;
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
import java.util.*;

@Controller
@RequestMapping("/teacher/problem")
public class TeacherProblemController {
    private final UploadFileService uploadFileService;

    public TeacherProblemController(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

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
        return "/teacher/problem-add";
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
        return Arrays.stream(file).filter(f -> !f.isEmpty()).count() == 0;
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
        fileNameInArray = Arrays.stream(testCasesIn).map(MultipartFile::getOriginalFilename).toList().toArray(fileNameInArray);
        Arrays.sort(fileNameInArray);
        fileNameOutArray = Arrays.stream(testCasesOut).map(MultipartFile::getOriginalFilename).toList().toArray(fileNameOutArray);
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
        // check the problemId is used or not
        boolean isOverlap = false;
        for (ProblemDTO problemDTO: Data.problemList) {
            if (problemDTO.getProblemId().equals(problem.getProblemId())) {
                isOverlap = true;
                break;
            }
        }
        // if used then return error
        if (isOverlap) {
            return "redirect:/error";
        }
        // check if file is not upload
        if (fileDescription.isEmpty()) {
            return "redirect:/error";
        }
        // upload file description
        String filePath = helperUploadToCloud(fileDescription);
        System.out.println(filePath);
        // upload to cloud using service uploadFileService
        ResponseObject responseObjectDescription = uploadFileService.uploadFile(filePath);
        if (!responseObjectDescription.getMessage().equals("Success")) {
            return "redirect:/error";
        }
        // mapping response when uploading file description
        Map cloudResponseDescription = (Map) responseObjectDescription.getData();
        String pathCloud = cloudResponseDescription.get("url").toString();
        // check if server cannot delete then return error
        if (!helperDeleteTempFile(filePath))
            return "redirect:/error";

        // set the problem description link
        problem.setProblemDescription(pathCloud);

        // check if every pair of test case uploaded has same name
        if (!checkTestCaseUpload(testCasesIn, testCasesOut)) {
            return "redirect:/error";
        }

        int numberTestCase = testCasesIn.length;
        // upload testcase from client to temp folder then upload them to cloud
        for (int i = 0; i < numberTestCase; ++i) {
            String testInPath = helperUploadToCloud(testCasesIn[i]);
            String testOutPath = helperUploadToCloud(testCasesOut[i]);
            ResponseObject responseObjectTestIn = uploadFileService.uploadFile(testInPath);
            ResponseObject responseObjectTestOut = uploadFileService.uploadFile(testOutPath);
            if (!responseObjectTestIn.getMessage().equals("Success") || !responseObjectTestOut.getMessage().equals("Success")) {
                return "redirect:/error";
            }
            Map cloudResponseTestIn = (Map) responseObjectTestIn.getData();
            Map cloudResponseTestOut = (Map) responseObjectTestOut.getData();
            String pathTestInCloud = cloudResponseTestIn.get("url").toString();
            String pathTestOutCloud = cloudResponseTestOut.get("url").toString();
            if (!helperDeleteTempFile(testInPath) || !helperDeleteTempFile(testOutPath))
                return "redirect:/error";
            String randomId = UUID.randomUUID().toString().replace("-", "");
            // add testcase of problem
            Data.testCaseList.add(new TestCaseDTO(randomId, pathTestInCloud, pathTestOutCloud, 0, problem.getProblemId()));
        }

        // set problem type
        for (String problemTypeId: problemTypeIdList)
            Data.problemHasTypeList.add(new ProblemHasTypeDTO(problem.getProblemId(), problemTypeId));
        // set level
        problem.setLevel(getLevelById(levelId));
        // set author
        problem.setAuthor(getAuthorByUsername(username));
        problem.setHide(false);
        return "redirect:/teacher/problem";
    }
}
