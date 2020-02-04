package org.medical.api.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.medical.libs.Question;
import org.medical.repositories.QuestionRepo;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ExamService {
    private final QuestionRepo questionRepo;
    private static int count = 0;
    private List<String> answersList;
    private String examName;
    public ExamService(QuestionRepo questionRepo) {
        this.questionRepo = questionRepo;
    }

    private List<String> convertPdfToStringList(String path) throws IOException {
        File file = new File(path);
        PDDocument pdDocument = PDDocument.load(file);
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String text = pdfTextStripper.getText(pdDocument);
        return Arrays.asList(text.split("\\r?\\n"));
    }

    private static void increase() {
        count++;
    }

    private void loadAnswers() throws IOException {
        answersList = new LinkedList<>();
        String path = Objects.requireNonNull(ExamService.class.getClassLoader().getResource("")).getPath();
        String finalPath = String.format("%s/%s.xlsx", path,examName);
        String[] answers = new String[(int) questionRepo.count()];
        int answerIndex = 0;
        for (Row next : new XSSFWorkbook(new FileInputStream(new File(finalPath))).getSheetAt(0)) {
            for (Cell cell : next) {
                if (cell.getCellType().toString().equals("NUMERIC"))
                    answerIndex = (int) cell.getNumericCellValue();
                else
                    answers[answerIndex - 1] = cell.getStringCellValue();
            }
        }
        answersList.addAll(Arrays.asList(answers));
    }

    private List<Question> convertStringListToQuestionList(List<String> strings) throws IOException {
        loadAnswers();
        answersList =  new ArrayList<>(answersList);
        Question question = new Question();
        StringBuilder questionBody = new StringBuilder();
        List<Question> questions = new LinkedList<>();
        strings.stream()
                .filter(s -> s.length() > 5)
                .forEach(s -> {
                    if (s.startsWith(String.format("%d. ", count + 1))) {
                        questionBody.append(s.substring(String.format("%d. ", count + 1).length()).trim());
                        question.setRightAnswer(answersList.get(count));
                        increase();
                    } else if (s.startsWith("A) ")) {
                        question.setQuestionBody(questionBody.toString().trim());
                        questionBody.setLength(0);
                        question.setVariantA(s.substring(3).trim());
                    } else if (s.startsWith("B) ")) {
                        question.setVariantB(s.substring(3).trim());
                    } else if (s.startsWith("C) ")) {
                        question.setVariantC(s.substring(3).trim());
                    } else if (s.startsWith("D) ")) {
                        question.setVariantD(s.substring(3).trim());
                    } else if (s.startsWith("E) ")) {
                        question.setVariantE(s.substring(3).trim());
                        question.setId(count);
                        questions.add(new Question(question));
                        question.clear();
                    } else {
                        questionBody.append(" ").append(s);
                    }
                });
        return questions;
    }

    public int getCount() {
        return (int) questionRepo.count();
    }

    public List<Question> shuffledQuestions() {
        Random random = new Random();
        Set<Integer> randomIds = new HashSet<>();
        while (randomIds.size() < 50)
            randomIds.add(Math.abs(random.nextInt(getCount())));
        return new LinkedList<>(questionRepo.findAllById(randomIds));
    }

    public void loadDocument(String path, String examName) throws IOException {
        List<Question> questions = convertStringListToQuestionList(convertPdfToStringList(path));
        this.examName = examName;
        questionRepo.deleteAll();
        questionRepo.saveAll(questions);
    }

}
