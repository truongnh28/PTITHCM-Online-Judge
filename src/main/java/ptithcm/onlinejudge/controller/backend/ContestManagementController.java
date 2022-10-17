package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.request.ContestRequest;
import ptithcm.onlinejudge.services.ContestManagementService;

@Controller
@RequestMapping("/api")
@CrossOrigin
public class ContestManagementController {
    @Autowired
    ContestManagementService contestManagementService;

    @PostMapping("/contest/addProblem")
    public ResponseEntity<ResponseObject> addProblem(@RequestBody ContestRequest contest) {
//        ResponseObject responseObject = contestManagementService.addContest(contest);
//        return ResponseEntity.status(responseObject.getStatus()).body(responseObject);
        return null;
    }
    @PostMapping("/contest/editProblem")
    public ResponseEntity<ResponseObject> editProblem(@RequestBody ContestRequest contest) {
//        ResponseObject responseObject = contestManagementService.editContest(contest);
//        return ResponseEntity.status(responseObject.getStatus()).body(responseObject);
        return null;
    }
}

