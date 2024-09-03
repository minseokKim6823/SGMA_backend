package com.springcooler.sgma.submittedanswer.command.application.service;


import com.springcooler.sgma.studyscheduleparticipant.command.domain.aggregate.StudyScheduleParticipant;
import com.springcooler.sgma.submittedanswer.command.application.dto.SubmittedAnswerDTO;
import com.springcooler.sgma.submittedanswer.command.domain.aggregate.SubmittedAnswer;
import com.springcooler.sgma.submittedanswer.command.domain.aggregate.SubmittedAnswerPK;
import com.springcooler.sgma.submittedanswer.command.domain.repository.SubmittedAnswerRepository;
import com.springcooler.sgma.submittedanswer.command.infrastructure.service.InfraSubmittedAnswerService;
import com.springcooler.sgma.submittedanswer.query.service.SubmittedAnswerService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AppSubmittedAnswerServiceImpl implements AppSubmittedAnswerService {

    private final ModelMapper modelMapper;
    private final SubmittedAnswerRepository submittedAnswerRepository;
    private final InfraSubmittedAnswerService infraSubmittedAnswerService;
    SubmittedAnswerService submittedAnswerService;
    @Autowired
    public AppSubmittedAnswerServiceImpl(ModelMapper modelMapper, SubmittedAnswerRepository submittedAnswerRepository, InfraSubmittedAnswerService infraSubmittedAnswerService, SubmittedAnswerService submittedAnswerService) {
        this.modelMapper = modelMapper;
        this.submittedAnswerRepository = submittedAnswerRepository;
        this.infraSubmittedAnswerService = infraSubmittedAnswerService;
        this.submittedAnswerService = submittedAnswerService;
    }

    @Override
    @Transactional
    public SubmittedAnswer registSubmittedAnswer(SubmittedAnswerDTO newSubmittedAnswerDTO) {

        SubmittedAnswerPK newSubmittedAnswerPK = new SubmittedAnswerPK(newSubmittedAnswerDTO.getProblemId(), newSubmittedAnswerDTO.getParticipantId());

        SubmittedAnswer newSubmittedAnswer = new SubmittedAnswer(newSubmittedAnswerDTO.getProblemId(),newSubmittedAnswerDTO.getParticipantId(), newSubmittedAnswerDTO.getSubmittedAnswer(), newSubmittedAnswerDTO.getAnswerStatus());
        newSubmittedAnswer = submittedAnswerRepository.save(newSubmittedAnswer);

        return newSubmittedAnswer;
    }

    @Transactional
    @Override
    public SubmittedAnswer modifySubmittedAnswer(SubmittedAnswerDTO modifySubmittedAnswer) {
        SubmittedAnswerPK modifySubmittedAnswerPK = new SubmittedAnswerPK(modifySubmittedAnswer.getProblemId(), modifySubmittedAnswer.getParticipantId());
        SubmittedAnswer existingSubmittedAnswer = submittedAnswerRepository.findById(modifySubmittedAnswerPK).orElseThrow(EntityNotFoundException::new);

        existingSubmittedAnswer.setSubmittedAnswer(modifySubmittedAnswer.getSubmittedAnswer());

        return submittedAnswerRepository.save(existingSubmittedAnswer);

    }

    @Transactional
    @Override
    public SubmittedAnswer findSubmittedAnswerByProblemIdAndParticipantId(long problemId, long participantId) {
        SubmittedAnswerPK submittedAnswerPK = new SubmittedAnswerPK(problemId, participantId);
        SubmittedAnswer foundSubmittedAnswer = submittedAnswerRepository.findById(submittedAnswerPK).orElseThrow(EntityNotFoundException::new);
        return foundSubmittedAnswer;
    }

    @Override
    public void gradeSubmittedAnswersByParticipantId(long participantId) {
        List<SubmittedAnswer> submittedAnswers = submittedAnswerRepository.findByParticipantId(participantId);
        submittedAnswers.forEach(x->log.info("x: {}", x));
            log.info("testCOde: {}");
    }
}
