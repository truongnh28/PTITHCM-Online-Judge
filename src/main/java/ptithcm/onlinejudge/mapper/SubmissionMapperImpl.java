package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.SubmissionDTO;
import ptithcm.onlinejudge.dto.SubmissionDetailDTO;
import ptithcm.onlinejudge.helper.TimeHelper;
import ptithcm.onlinejudge.model.entity.Submission;
import ptithcm.onlinejudge.services.SubmitService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class SubmissionMapperImpl implements SubmissionMapper {
    @Autowired
    private SubmitService submitService;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ProblemMapper problemMapper;

    @Override
    public SubmissionDetailDTO entityToDetailDTO(Submission entity) {
        if (entity == null) return null;
        SubmissionDetailDTO detailDTO = new SubmissionDetailDTO();
        detailDTO.setSubmissionId(entity.getId());
        detailDTO.setSubmissionTime(TimeHelper.convertInstantToString(entity.getSubmissionTime()));
        detailDTO.setSubmissionScore(entity.getSubmissionScore());
        detailDTO.setSubmissionSourcePath(entity.getSubmissionSourcePath());
        detailDTO.setVerdict(entity.getVerdict());
        detailDTO.setStudent(studentMapper.entityToDTO(entity.getStudent()));
        detailDTO.setProblem(problemMapper.entityToDTO(entity.getProblem()));
        String sourceCode = (String) submitService.getSubmissionSourceAdapter(entity.getId()).getData();
        detailDTO.setSourceCode(sourceCode);
        if (sourceCode == null || sourceCode.isEmpty()) {
            Path fileName = Path.of(entity.getSubmissionSourcePath());
            try {
                sourceCode = Files.readString(fileName);
                detailDTO.setSourceCode(sourceCode);
            } catch (IOException e) {
                return null;
            }
        }
        return detailDTO;
    }

    @Override
    public SubmissionDTO entityToDTO(Submission entity) {
        if (entity == null) return null;
        SubmissionDTO dto = new SubmissionDTO();
        dto.setSubmissionId(entity.getId());
        dto.setSubmissionTime(TimeHelper.convertInstantToString(entity.getSubmissionTime()));
        dto.setSubmissionScore(entity.getSubmissionScore());
        dto.setSubmissionSourcePath(entity.getSubmissionSourcePath());
        dto.setVerdict(entity.getVerdict());
        dto.setStudent(studentMapper.entityToDTO(entity.getStudent()));
        dto.setProblem(problemMapper.entityToDTO(entity.getProblem()));
        return dto;
    }

    @Override
    public Submission dtoToEntity(SubmissionDTO dto) {
        if (dto == null) return null;
        Submission entity = new Submission();
        entity.setId(dto.getSubmissionId());
        entity.setVerdict(dto.getVerdict());
        entity.setSubmissionTime(TimeHelper.convertStringToInstance(dto.getSubmissionTime()));
        entity.setSubmissionScore(dto.getSubmissionScore());
        entity.setSubmissionSourcePath(dto.getSubmissionSourcePath());
        entity.setProblem(problemMapper.dtoToEntity(dto.getProblem()));
        entity.setStudent(studentMapper.dtoToEntity(dto.getStudent()));
        return entity;
    }
}
