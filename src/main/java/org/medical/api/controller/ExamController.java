package org.medical.api.controller;

import org.medical.api.service.ExamService;
import org.medical.libs.Question;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/api")
public class ExamController {

    private final ExamService service;

    public ExamController(ExamService service) {
        this.service = service;
    }

    @GetMapping("/exam")
    public void getExam() {
        throw new RuntimeException("Not implemented yet");
    }

    @PostMapping("/exam")
    public List<Question> postExam() {
       return service.shuffledQuestions();
    }

    @PutMapping("/exam")
    public void puExam(@RequestParam String examName) throws IOException {
        service.loadDocument(String.format("%s/%s.pdf",Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath(),examName),examName);

    }

}
