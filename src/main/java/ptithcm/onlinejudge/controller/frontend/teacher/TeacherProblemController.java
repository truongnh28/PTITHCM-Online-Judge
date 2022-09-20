package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.data.Data;
import ptithcm.onlinejudge.dto.*;
import ptithcm.onlinejudge.mapper.ProblemMapper;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.request.MultipleProblemTypeRequest;
import ptithcm.onlinejudge.model.request.MultipleTestCaseRequest;
import ptithcm.onlinejudge.model.request.ProblemRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/problem")
public class TeacherProblemController {
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ProblemManagementService problemManagementService;
    @Autowired
    private TestCaseManagementService testCaseManagementService;
    @Autowired
    private ProblemHasTypeManagementService problemHasTypeManagementService;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private StorageFileService storageFileService;

    @GetMapping("")
    public String showProblemListPage(Model model, HttpSession session) {
        model.addAttribute("pageTitle", "Problem list");
//        List<ProblemDTO> problemList = new ArrayList<>();
//        for (ProblemDTO problem : Data.problemList)
//            if (!problem.isHide())
//                problemList.add(problem);
        UserLogin userLogin = (UserLogin) session.getAttribute("user");
        List<Problem> problemEntities = (List<Problem>) problemManagementService.getAllProblemCreateByTeacher(userLogin.getUsername()).getData();
        List<ProblemDTO> problemList = problemEntities.stream().map(item -> problemMapper.entityToDTO(item)).collect(Collectors.toList());
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

    private boolean checkFileIsNotUploaded(MultipartFile file) {
        return file == null || file.isEmpty();
    }

    private boolean checkFilesAreNotUpload(MultipartFile[] files) {
        for (MultipartFile file: files)
            if (file == null || file.isEmpty())
                return true;
        return false;
    }

    @PostMapping("/add")
    public String addProblem(@ModelAttribute("problem") ProblemDTO problem,
                             @RequestParam("levelSelect") int levelId,
                             @RequestParam("fileDescription") MultipartFile fileDescription,
                             @RequestParam("testCasesIn") MultipartFile[] testCasesIn,
                             @RequestParam("testCasesOut") MultipartFile[] testCasesOut,
                             @RequestParam("problemTypeIds") String[] problemTypeIds,
                             HttpSession session) {
        String username = ((UserLogin) session.getAttribute("user")).getUsername();
        ProblemRequest problemRequest = new ProblemRequest();
        problemRequest.setProblemId(problem.getId());
        problemRequest.setProblemName(problem.getProblemName());
        problemRequest.setScore(problem.getProblemScore());
        problemRequest.setTimeLimit(problem.getProblemTimeLimit());
        problemRequest.setMemoryLimit(problem.getProblemMemoryLimit());
        problemRequest.setTeacherId(username);
        problemRequest.setLevelId(levelId);

        if (checkFileIsNotUploaded(fileDescription))
            return "redirect:/error";

        String descriptionPath = storageFileService.storeFile(fileDescription);
        ResponseObject responseAddProblem = problemManagementService.addProblem(problemRequest, descriptionPath);
        if (!responseAddProblem.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";

        MultipleProblemTypeRequest multipleProblemTypeRequest = new MultipleProblemTypeRequest();
        multipleProblemTypeRequest.setProblemId(problem.getId());
        multipleProblemTypeRequest.setProblemTypeIds(problemTypeIds);
        ResponseObject responseAddProblemTypes = problemHasTypeManagementService.addMultipleProblemType(multipleProblemTypeRequest);
        if (!responseAddProblemTypes.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";

        if ((checkFilesAreNotUpload(testCasesIn) && !checkFilesAreNotUpload(testCasesOut))
            || (!checkFilesAreNotUpload(testCasesIn) && checkFilesAreNotUpload(testCasesOut)))
            return "redirect:/error";

        if (!checkFilesAreNotUpload(testCasesIn) && !checkFilesAreNotUpload(testCasesOut)) {
            int inputFilesLength = testCasesIn.length;
            int outputFilesLength = testCasesOut.length;
            if (inputFilesLength != outputFilesLength) {
                return "redirect:/error";
            }

            String[] testInPaths = new String[inputFilesLength];
            String[] testOutPaths = new String[outputFilesLength];
            for (int i = 0; i < inputFilesLength; ++i) {
                testInPaths[i] = storageFileService.storeFile(testCasesIn[i]);
                testOutPaths[i] = storageFileService.storeFile(testCasesOut[i]);
            }

            MultipleTestCaseRequest multipleTestCaseRequest = new MultipleTestCaseRequest();
            multipleTestCaseRequest.setTestInPaths(testInPaths);
            multipleTestCaseRequest.setTestOutPaths(testOutPaths);
            multipleTestCaseRequest.setProblemId(problem.getId());
            ResponseObject responseAddTestCaseToProblem = testCaseManagementService.addMultipleTestCaseProblem(multipleTestCaseRequest);
            if (!responseAddTestCaseToProblem.getStatus().equals(HttpStatus.OK))
                return "redirect:/error";
        }
        /*
        // check the problemId is used or not
        boolean isOverlap = false;
        for (ProblemDTO problemDTO: Data.problemList) {
            if (problemDTO.getId().equals(problem.getId())) {
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
        problem.setProblemUrl(pathCloud);

        // check if every pair of test case uploaded has same name

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
            Data.testCaseList.add(new TestCaseDTO(randomId, pathTestInCloud, pathTestOutCloud, 0, problem.getId()));
        }

        // set problem type
        for (String problemTypeId: problemTypeIdList)
            Data.problemHasTypeList.add(new ProblemHasTypeDTO(problem.getId(), problemTypeId));
        // set level
        problem.setLevel(getLevelById(levelId));
        // set author
        problem.setTeacher(getAuthorByUsername(username));
        problem.setHide(false);
         */
        return "redirect:/teacher/problem";
    }
}
