package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.model.request.ProblemRequest;
import ptithcm.onlinejudge.model.ResponseObject;
import ptithcm.onlinejudge.services.ProblemManagementService;

@Controller
@RequestMapping("/api")
@CrossOrigin
public class ProblemManagementController {
    @Autowired
    ProblemManagementService problemManagementService;

    @PostMapping("/problem/addProblem")
    public ResponseEntity<ResponseObject> addProblem(@RequestBody ProblemRequest problem, @RequestParam String filePath) {
        ResponseObject responseObject = problemManagementService.addProblem(problem, filePath);
        return ResponseEntity.status(responseObject.getStatus()).body(responseObject);
    }
    @PostMapping("/problem/editProblem")
    public ResponseEntity<ResponseObject> editProblem(@RequestBody ProblemRequest problem, @RequestParam String filePath) {
        ResponseObject responseObject = problemManagementService.editProblem(problem, filePath);
        return ResponseEntity.status(responseObject.getStatus()).body(responseObject);
    }
    @DeleteMapping("/problem/deleteProblem")
    public ResponseEntity<ResponseObject> deleteProblem(@RequestParam String problemId) {
        ResponseObject responseObject = problemManagementService.deleteProblem(problemId);
        return ResponseEntity.status(responseObject.getStatus()).body(responseObject);
    }
}
