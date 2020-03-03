
package com.example.AutoskolaDemoWithSecurity.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional
public class TestService {
    
    @Autowired
    private TestRepository testRepository;
    
    @Autowired
    private QuestionRepository questionRepository;

    
    public TestDTO getTest(int testNumber) {
        
        Test test = testRepository.findByNumber(testNumber);
        TestDTO testDTO = new TestDTO(test);
        List<String> questions = Arrays.stream(test.getQuestions()
                    .split(",")).collect(Collectors.toList());
        
        for(String str : questions) {
            int questionID = Integer.parseInt(str.split("-")[0]);
            int order = Integer.parseInt(str.split("-")[1]);
            Question question = questionRepository.findById(questionID).orElseThrow(
                   () -> new NoSuchElementException("question not found"));
            question.setQuestionOrder(order);
            testDTO.addQuestion(question);
        }
        
        return testDTO; 
                
    }
    
    public List<TestDTO> getAllTests() {
        
        List<TestDTO> tests = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        testRepository.findAll().forEach((obj) -> list.add(obj.getNumber()));
        list.forEach(i -> tests.add(getTest(i)));
        
        return tests;
        
    }
    
    public String saveImage(MultipartFile imageFile) throws IOException {
        System.out.println("1");
        String folder = "photos/";
        byte[] bytes = imageFile.getBytes();
        System.out.println("2");
        Path path = Paths.get(folder + imageFile.getOriginalFilename());
        System.out.println("3");
        Files.write(path, bytes);
        System.out.println("4");
        return "Image succesfully saved";
    }
    
}
