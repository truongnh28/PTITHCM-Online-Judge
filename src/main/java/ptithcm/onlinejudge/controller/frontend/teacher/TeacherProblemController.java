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
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/teacher/problem")
public class TeacherProblemController {
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ProblemManagementService problemManagementService;

    @GetMapping("")
    public String showProblemListPage(Model model, HttpSession session) {
        model.addAttribute("pageTitle", "Problem list");
        Login login = (Login) session.getAttribute("user");
        List<ProblemDTO> problems = ((List<Problem>) problemManagementService.getAllProblemsCreateByTeacher(login.getUsername()).getData())
                .stream().map(item -> problemMapper.entityToDTO(item)).toList();
        model.addAttribute("problems", problems);
        return "/teacher/problem/problem";
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
        String teacherId = ((Login) session.getAttribute("user")).getUsername();
        ResponseObject responseAddProblem = problemManagementService.addProblemWithTestCasesAndTypes(problem, teacherId, levelId, description, inputs, outputs, typeIds);
        if (!responseAddProblem.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/problem";
    }
}
