package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.SubmissionDTO;
import ptithcm.onlinejudge.dto.SubmissionDetailDTO;
import ptithcm.onlinejudge.helper.TimeHelper;
import ptithcm.onlinejudge.model.entity.Submission;
import ptithcm.onlinejudge.services.SubmitService;

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
        detailDTO.setSubmissionTime(entity.getSubmissionTime().toString());
        detailDTO.setSubmissionScore(entity.getSubmissionScore());
        detailDTO.setVerdict(entity.getVerdict());
        detailDTO.setStudent(studentMapper.entityToDTO(entity.getStudent()));
        detailDTO.setProblem(problemMapper.entityToDTO(entity.getProblem()));
        detailDTO.setSourceCode((String) submitService.getSubmissionSourceAdapter(entity.getId()).getData());
        return detailDTO;
    }

    @Override
    public SubmissionDTO entityToDTO(Submission entity) {
        if (entity == null) return null;
        SubmissionDTO dto = new SubmissionDTO();
        dto.setSubmissionId(entity.getId());
        dto.setSubmissionTime(entity.getSubmissionTime().toString());
        dto.setSubmissionScore(entity.getSubmissionScore());
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
        entity.setProblem(problemMapper.dtoToEntity(dto.getProblem()));
        entity.setStudent(studentMapper.dtoToEntity(dto.getStudent()));
        return entity;
    }
}
