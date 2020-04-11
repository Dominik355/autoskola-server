
package com.example.AutoskolaDemoWithSecurity.tests;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.QuestionPhoto;
import com.example.AutoskolaDemoWithSecurity.repositories.QuestionPhotoRepository;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class TestService {
    
    @Autowired
    private TestRepository testRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuestionPhotoRepository photoRepository;

    
    public TestDTO getTest(int testNumber) {
        
        Test test = testRepository.findByNumber(testNumber).orElseThrow(
                () -> new NullPointerException("There is no test with number: "+testNumber));
        TestDTO testDTO = new TestDTO(test);
        List<String> questions = Arrays.stream(test.getQuestions()
                    .split(",")).collect(Collectors.toList());
        
        for(String str : questions) {
            int questionID = Integer.parseInt(str.split("-")[0]);
            int order = Integer.parseInt(str.split("-")[1]);
            QuestionDTO question = new QuestionDTO(questionRepository.findById(questionID).orElseThrow(
                   () -> new NoSuchElementException("question not found")));
            question.setQuestionOrder(order);
            testDTO.addQuestion(question);
        }
        
        return testDTO; 
                
    }
    
    public List<Integer> getAllTests() {
        return testRepository.findAll().stream().map(obj -> obj.getNumber()).collect(Collectors.toList());        
    }
    
    public QuestionPhoto getQuestionPhoto(int questionID) {
        return photoRepository.findByQuestion(questionID)
                .orElseThrow(() -> new NoSuchElementException("This question does not have a photo"));
    }

}
