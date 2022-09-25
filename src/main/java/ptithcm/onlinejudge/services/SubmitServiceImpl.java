package ptithcm.onlinejudge.services;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ptithcm.onlinejudge.model.adapter.*;
import ptithcm.onlinejudge.model.request.SubmitRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;


@Service
public class SubmitServiceImpl implements SubmitService {
    @Override
    public ResponseObject getProblemListAdapter() {
        final String url = "http://localhost:80/api/get_problem_list";
        try {
            RestTemplate restTemplate = new RestTemplate();
            GetProblemListResponse result = restTemplate.getForObject(url, GetProblemListResponse.class);
            return new ResponseObject(HttpStatus.OK, "Success", result);
        } catch (Exception e) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
    }

    @Override
    public ResponseObject getProblemInfoAdapter(String problemId) {
        final String url = "http://localhost:80/api/get_problem_info/" + problemId;
        try {
            RestTemplate restTemplate = new RestTemplate();
            GetProblemInfoResponse result = restTemplate.getForObject(url, GetProblemInfoResponse.class);
            return new ResponseObject(HttpStatus.OK, "Success", result);
        } catch (Exception e) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
    }

    @Override
    public ResponseObject getStatusAdapter(String jobId) {
        final String url = "http://localhost:80/api/get_status/" + jobId;
        try {
            RestTemplate restTemplate = new RestTemplate();
            GetStatusResponse result = restTemplate.getForObject(url, GetStatusResponse.class);
            return new ResponseObject(HttpStatus.OK, "Success", result);
        } catch (Exception e) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
    }

    @Override
    public ResponseObject getSubmissionSourceAdapter(String jobId) {
        final String url = "http://localhost:80/api/get_submission_source/" + jobId;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);
            return new ResponseObject(HttpStatus.OK, "Success", result);
        } catch (Exception e) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
    }

    @Override
    public ResponseObject getSubmissionsAdapter(int pageNumber, String key) {
        final String url = "http://localhost:80/api/get_submissions/" + pageNumber + "?secret_key=" + key;
        try {
            RestTemplate restTemplate = new RestTemplate();
            GetSubmissionsResponse result = restTemplate.getForObject(url, GetSubmissionsResponse.class);
            return new ResponseObject(HttpStatus.OK, "Success", result);
        } catch (Exception e) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
    }

    @Override
    public ResponseObject submitProblemAdapter(SubmitRequest submitRequest) {
        final String url = "http://localhost:80/api/submit";
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
            multipartBodyBuilder.part("problem_id", submitRequest.getProblemId());
            multipartBodyBuilder.part("username", submitRequest.getUserId());
            multipartBodyBuilder.part("type", submitRequest.getType());
            multipartBodyBuilder.part("secret_key", submitRequest.getType());
            Resource code = new FileSystemResource(submitRequest.getPath());
            multipartBodyBuilder.part("code", code, MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();
            HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody, headers);
            SubmitResponse result = restTemplate.postForEntity(url, httpEntity, SubmitResponse.class).getBody();
            return new ResponseObject(HttpStatus.OK, "Success", result);
        } catch (Exception e) {
            return new ResponseObject(HttpStatus.FOUND, "Found", "");
        }
    }
}
