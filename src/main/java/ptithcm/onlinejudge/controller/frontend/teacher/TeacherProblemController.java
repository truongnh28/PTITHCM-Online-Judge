package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
import java.util.stream.Collectors;

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
    public String showProblemList(HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        return "redirect:/teacher/problem/page/1";
    }

    @GetMapping("/page/{page}")
    public String showProblemListPage(Model model, @PathVariable("page") int page, @Param("keyword") String keyword, HttpSession session) {
        if (isExpired(session))
            return "redirect:/login";
        model.addAttribute("pageTitle", "Danh sách bài tập");
        String teacherId = session.getAttribute("user").toString();
        Map<String, Object> response = (Map<String, Object>) problemManagementService.getAllProblemsCreateByTeacher(teacherId, page).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = (Map<String, Object>) problemManagementService.searchAllProblemsCreateByTeacher(teacherId, keyword.trim(), page).getData();
            model.addAttribute("keyword", keyword);
        }
        List<ProblemDTO> problems = getProblems(response).stream().map(item -> problemMapper.entityToDTO(item)).collect(Collectors.toList());
        int currentPage = (int) response.getOrDefault("currentPage", 0);
        int totalPages = (int) response.getOrDefault("totalPages", 0);
        String pageUrlPrefix = "/teacher/problem";
        model.addAttribute("problems", problems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageUrlPrefix", pageUrlPrefix);
        return "/teacher/problem/problem";
    }

    @GetMapping("/add")
    public String showAddProblemForm(Model model, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
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
        if (isExpired(session))
            return "redirect:/";
        String teacherId = session.getAttribute("user").toString();
        ResponseObject addProblemResponse = problemManagementService.addProblem(problem, teacherId, levelId, description, inputs, outputs, typeIds);
        if (!addProblemResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/problem";
    }

    @GetMapping("/{problemId}/edit")
    public String showUpdateProblemPage(@PathVariable("problemId") String problemId, Model model, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
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
        if (isExpired(session))
            return "redirect:/";
        String teacherId = session.getAttribute("user").toString();
        ResponseObject editProblemResponse = problemManagementService.editProblem(problem, teacherId, levelId, description, typeIds);
        if (!editProblemResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/problem";
    }

    @GetMapping("/{problemId}/lock")
    public String lockProblem(@PathVariable("problemId") String problemId, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        ResponseObject lockProblemResponse = problemManagementService.lockProblem(problemId);
        if (!lockProblemResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/problem";
    }

    @GetMapping("/{problemId}/unlock")
    public String unlockProblem(@PathVariable("problemId") String problemId, HttpSession session) {
        if (isExpired(session))
            return "redirect:/";
        ResponseObject lockProblemResponse = problemManagementService.unlockProblem(problemId);
        if (!lockProblemResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/problem";
    }

    private boolean isExpired(HttpSession session) {
        return session.getAttribute("user") == null && session.getAttribute("role") == null;
    }

    private List<Problem> getProblems(Map<String, Object> res) {
        return (List<Problem>) res.getOrDefault("data", null);
    }
}
