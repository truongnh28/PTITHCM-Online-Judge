package ptithcm.onlinejudge.controller.frontend.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.dto.TestCaseDTO;
import ptithcm.onlinejudge.mapper.TestCaseMapper;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.TestCase;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.services.ProblemManagementService;
import ptithcm.onlinejudge.services.TestCaseManagementService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teacher/problem/{problemId}/testcase")
public class TeacherTestCaseController {
    @Autowired
    private ProblemManagementService problemManagementService;
    @Autowired
    private TestCaseMapper testCaseMapper;
    @Autowired
    private TestCaseManagementService testCaseManagementService;

    @GetMapping("")
    public String showTestCasePage(@PathVariable("problemId") String problemId, Model model) {
        model.addAttribute("pageTitle", "Test case");
        List<TestCaseDTO> testCases = ((List<TestCase>) testCaseManagementService.getTestCasesByProblem(problemId).getData())
                .stream().map(item -> testCaseMapper.entityToDTO(item)).toList();
        Problem problem = (Problem) problemManagementService.getProblemById(problemId).getData();
        model.addAttribute("problemName", problem.getProblemName());
        model.addAttribute("testcases", testCases);
        return "/teacher/problem/problem-test-case";
    }

    @GetMapping("/{testId}/delete")
    public String deleteTestCase(@PathVariable("problemId") String problemId, @PathVariable("testId") String testId) {
        ResponseObject deleteTestCaseResponse = testCaseManagementService.deleteTestCase(problemId, testId);
        if (!deleteTestCaseResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/problem/{problemId}/testcase";
    }

    @PostMapping("/add")
    public String addTestCase(@PathVariable("problemId") String problemId, @RequestParam("inputs") MultipartFile[] inputs, @RequestParam("outputs") MultipartFile[] outputs) {
        ResponseObject addTestCaseResponse = testCaseManagementService.addTestCase(problemId, inputs, outputs);
        if (!addTestCaseResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/problem/{problemId}/testcase";
    }

    @GetMapping("/{testId}/edit")
    public String showTestCaseEditPage(@PathVariable("problemId") String problemId, @PathVariable("testId") String testcaseId, Model model) {
        model.addAttribute("pageTitle", "Chỉnh sửa bộ test");
        ResponseObject getContentResponse = testCaseManagementService.getContentTestCase(problemId, testcaseId);
        if (!getContentResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        Map<String, String> contents = (Map<String, String>) getContentResponse.getData();
        String input = contents.getOrDefault("input", "");
        String output = contents.getOrDefault("output", "");
        if (input.isEmpty() || output.isEmpty())
            return "redirect:/error";
        model.addAttribute("input", input);
        model.addAttribute("output", output);
        return "/teacher/problem/problem-test-case-edit";
    }

    @PostMapping("/{testId}/edit")
    public String editTestCase(@PathVariable("problemId") String problemId, @PathVariable("testId") String testCaseId, @RequestParam("input") String input, @RequestParam("output") String output) {
        ResponseObject editTestCaseResponse = testCaseManagementService.editTestCase(input, output, problemId, testCaseId);
        if (!editTestCaseResponse.getStatus().equals(HttpStatus.OK))
            return "redirect:/error";
        return "redirect:/teacher/problem/{problemId}/testcase";
    }
}
