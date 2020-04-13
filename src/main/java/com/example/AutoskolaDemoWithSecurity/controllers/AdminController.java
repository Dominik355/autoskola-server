
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.ProfilePicture;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.QuestionPhoto;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.DrivingSchoolDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.PictureRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.AutoskolaDemoWithSecurity.repositories.QuestionPhotoRepository;
import com.example.AutoskolaDemoWithSecurity.services.DrivingSchoolService;
import com.example.AutoskolaDemoWithSecurity.tests.Question;
import com.example.AutoskolaDemoWithSecurity.tests.QuestionDTO;
import com.example.AutoskolaDemoWithSecurity.tests.QuestionRepository;
import com.example.AutoskolaDemoWithSecurity.tests.Test;
import com.example.AutoskolaDemoWithSecurity.tests.TestRepository;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(value = {"/admin"})
public class AdminController {
    
    @Autowired
    private DrivingSchoolService schoolService;
    
    @Autowired
    private PictureRepository pictureRepository;
    
    @Autowired
    private QuestionPhotoRepository photoRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private TestRepository testRepository;
    
   
    @PostMapping(value = {"/addNewSchool"})
    @ApiOperation(value = "${schoolController.createSchool.value}",
            notes = "${schoolController.createSchool.notes}",
            response = ResponseEntity.class)
    public ResponseEntity createSchool(@RequestBody @Valid DrivingSchoolDTO school) {
        return ResponseEntity.ok(schoolService.createDrivingSchool(school));
    }
    
    
    @PostMapping(value = {"/addBasicProfilePicture"})
    public ResponseEntity addBasicPhoto(@RequestBody MultipartFile file) {
        if(!file.isEmpty()) {
            ProfilePicture picture;
            try {
                    if(pictureRepository.existsByName("basicProfilePicture")) {
                        picture = pictureRepository.findByName("basicProfilePicture").get();
                        picture.setPicture(file.getBytes());
                    } else {
                        picture = new ProfilePicture(
                                "basicProfilePicture", FilenameUtils.getExtension(file.getOriginalFilename()), file.getBytes());
                    }
                    pictureRepository.save(picture);
                    return new ResponseEntity("Basic picture saved", HttpStatus.OK);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return new ResponseEntity("Exception occured", HttpStatus.BAD_REQUEST);
                }
        }
        return new ResponseEntity("File was empty", HttpStatus.BAD_REQUEST);
    }
    
    
    @PostMapping(value = {"/addQuestionImage/{id}"})
    public ResponseEntity addQuestionImage(@PathVariable("id") int id, @RequestBody MultipartFile file) {
        if(questionRepository.existsById(id)) {
            if(!file.isEmpty()) {
                try {
                    QuestionPhoto picture = photoRepository
                            .findByQuestion(id).orElse(new QuestionPhoto(id));
                    picture.setPicture(file.getBytes());
                    photoRepository.save(picture);
                    Question question = questionRepository.findById(id).get();
                    question.setHasPhoto(true);
                    questionRepository.save(question);
                    return new ResponseEntity("Picture for question: "+id+" saved", HttpStatus.OK);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return new ResponseEntity("Exception occured", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity("File was empty", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity("No such question with ID: "+id, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping(value = {"/addQuestion"})
    public ResponseEntity addQuestion(@RequestBody QuestionDTO question) {
        Question saved = questionRepository.save(new Question(question));
        if(saved == null) {
            return new ResponseEntity("Problem with adding question", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Question added", HttpStatus.OK);
    }
    
    @PostMapping(value = {"/addTest"})
    public ResponseEntity addTest(@RequestBody Test test) {
        try{
            for(String sep : test.getQuestions().split(",")) {
                String[] io = sep.split("-");
                if(!questionRepository.existsById(Integer.valueOf(io[0]))){
                    return new ResponseEntity("Question with id "+io[0]+" does not exist", HttpStatus.BAD_REQUEST);
                }
            }
        } catch(Exception ex) {
            return new ResponseEntity("Bad formatted Questions", HttpStatus.BAD_REQUEST);
        }
        testRepository.save(test);
        return new ResponseEntity("Test has been saved", HttpStatus.OK);
    }
    
}
