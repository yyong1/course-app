package com.example.coursesystem.core.repository;

import com.example.coursesystem.core.model.Lesson;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends MongoRepository<Lesson, String> {

    List<Lesson> findByCourseId(String courseId);
}
