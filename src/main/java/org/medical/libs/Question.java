package org.medical.libs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String questionBody;
    private String variantA;
    private String variantB;
    private String variantC;
    private String variantD;
    private String variantE;
    private String rightAnswer;

    public Question(String questionBody, String variantA, String variantB, String variantC, String variantD, String variantE, String rightAnswer) {
        this.questionBody = questionBody;
        this.variantA = variantA;
        this.variantB = variantB;
        this.variantC = variantC;
        this.variantD = variantD;
        this.variantE = variantE;
        this.rightAnswer = rightAnswer;
    }
    public Question(Question question) {
        this.id = question.getId();
        this.questionBody = question.getQuestionBody();
        this.variantA = question.getVariantA();
        this.variantB = question.getVariantB();
        this.variantC = question.getVariantC();
        this.variantD = question.getVariantD();
        this.variantE = question.getVariantE();
        this.rightAnswer = question.getRightAnswer();
    }
    public void clear(){
        this.id = 0;
        this.questionBody = "";
        this.variantA = "";
        this.variantB = "";
        this.variantC = "";
        this.variantD = "";
        this.variantE = "";
        this.rightAnswer = "";
    }

}
