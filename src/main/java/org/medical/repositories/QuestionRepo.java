package org.medical.repositories;

import org.medical.libs.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends CrudRepository<Question, Integer> {
    List<Question> findAllById(Iterable<Integer> collect);
}
