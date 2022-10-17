package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.dto.*;
import ptithcm.onlinejudge.mapper.LevelMapper;
import ptithcm.onlinejudge.mapper.ProblemMapper;
import ptithcm.onlinejudge.mapper.ProblemTypeMapper;
import ptithcm.onlinejudge.model.entity.Level;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.ProblemHasTypeId;
import ptithcm.onlinejudge.model.entity.ProblemType;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.ProblemHasTypeRepository;
import ptithcm.onlinejudge.services.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/teacher/problem")
public class TeacherProblemController {
    @Autowired
    private LevelMapper levelMapper;
    @Autowired
    private LevelManagementService levelManagementService;
    @Autowired
    private ProblemTypeMapper problemTypeMapper;
    @Autowired
    private ProblemTypeManagementService problemTypeManagementService;
    @Autowired
    private ProblemHasTypeRepository problemHasTypeRepository;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ProblemManagementService problemManagementService;

    @GetMapping("")
    public String showProblemListPage(Model model, HttpSession session) {
        model.addAttribute("pageTitle", "Danh sách bài tập");
        String teacherId = session.getAttribute("user").toString();
        List<ProblemDTO> problems = ((List<Problem>) problemManagementService.getAllProblemsCreateByTeacher(teacherId).getData())
                .stream().map(item -> problemMapper.entityToDTO(item)).toList();
        model.addAttribute("problems", problems);
        return "/teacher/problem/problem";
    }

    @PostMapping("")
    public String searchProblems(Model model, HttpSession session, @RequestParam("keyword") String keyword) {
        model.addAttribute("pageTitle", "Danh sách bài tập");
        String teacherId = session.getAttribute("user").toString();
        List<ProblemDTO> problems = ((List<Problem>) problemManagementService.searchAllProblemsCreateByTeacher(teacherId, keyword).getData())
                .stream().map(item -> problemMapper.entityToDTO(item)).toList();
        model.addAttribute("problems", problems);
        return "/teacher/problem/problem";
    }

    @GetMapping("/add")
    public String showAddProblemForm(Model model) {
        model.addAttribute("pageTitle", "Add problem");
        ProblemDTO problem = new ProblemDTO();
        model.addAttribute("problem", problem);
        List<ProblemTypeDTO> problemTypes = ((List<ProblemType>) problemTypeManagementService.getAllProblemTypes().getData())
                .stream().map(item -> problemTypeMapper.entityToDTO(item)).toList();
        model.addAttribute("problemTypes", problemTypes);
        List<LevelDTO> levelList = ((List<Level>) levelManagementService.getAllLevels().getData())
                .stream().map(item -> levelMapper.entityToDTO(item)).toList();
        model.addAttribute("levels", levelList);
        return "/teacher/problem/problem-add";
    }

    @PostMapping("/add")
    public String addProblem(@ModelAttribute("problem") ProblemDTO problem,
                             @RequestParam("levelSelect") int levelId,
                             @RequestParam("fileDescription") MultipartFile description,
                             @RequestParam("testCasesIn") MultipartFile[] inputs,
                             @RequestParam("testCasesOut") MultipartFile[] outputs,
                             @RequestParam("problemTypeIds") String[] typeIds,
                             HttpSession session) {
        String teacherId = session.getAttribute("user").toString();
        ResponseObject addProblemResponse = problemManagementService.addProblem(problem, teacherId, levelId, description, inputs, outputs, typeIds);
        if (!addProblemResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/problem";
    }

    @GetMapping("/{problemId}/edit")
    public String showUpdateProblemPage(@PathVariable("problemId") String problemId, Model model) {
        model.addAttribute("pageTitle", "Chỉnh sửa bài tập");
        ResponseObject getProblemByIdResponse = problemManagementService.getProblemById(problemId);
        if (!getProblemByIdResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        ProblemDTO problem = problemMapper.entityToDTO((Problem) getProblemByIdResponse.getData());
        model.addAttribute("problem", problem);
        List<ProblemTypeShowDTO> problemTypes = ((List<ProblemType>) problemTypeManagementService.getAllProblemTypes().getData())
                .stream().map(item -> {
                    ProblemTypeShowDTO problemTypeShow = problemTypeMapper.entityToShowDTO(item);
                    problemTypeShow.setChecked(problemHasTypeRepository.existsById(new ProblemHasTypeId(problemId, item.getId())));
                    return problemTypeShow;
                }).toList();
        model.addAttribute("problemTypes", problemTypes);
        List<LevelShowDTO> levels = ((List<Level>) levelManagementService.getAllLevels().getData())
                .stream().map(item -> {
                    LevelShowDTO levelShow = levelMapper.entityToShowSTO(item);
                    levelShow.setSelected(item.getId() == problem.getLevel().getLevelId());
                    return levelShow;
                }).toList();
        model.addAttribute("levels", levels);
        return "/teacher/problem/problem-edit";
    }

    @PostMapping("/{problemId}/edit")
    public String updateProblem(@PathVariable("problemId") String problemId,
                                @ModelAttribute("problem") ProblemDTO problem,
                                @RequestParam("levelSelect") int levelId,
                                @RequestParam("fileDescription") MultipartFile description,
                                @RequestParam("problemTypeIds") String[] typeIds,
                                HttpSession session) {
        String teacherId = session.getAttribute("user").toString();
        ResponseObject editProblemResponse = problemManagementService.editProblem(problem, teacherId, levelId, description, typeIds);
        if (!editProblemResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/problem";
    }

    @GetMapping("/{problemId}/lock")
    public String lockProblem(@PathVariable("problemId") String problemId) {
        ResponseObject lockProblemResponse = problemManagementService.lockProblem(problemId);
        if (!lockProblemResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/problem";
    }

    @GetMapping("/{problemId}/unlock")
    public String unlockProblem(@PathVariable("problemId") String problemId) {
        ResponseObject lockProblemResponse = problemManagementService.unlockProblem(problemId);
        if (!lockProblemResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/problem";
    }
}
