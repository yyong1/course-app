package com.example.coursesystem.core.service;

import com.example.coursesystem.core.model.Lesson;
import com.example.coursesystem.core.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public Lesson getLessonById(String id) {
        return lessonRepository.findById(id).orElse(null);
    }

    public Lesson createLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public Lesson updateLesson(String id, Lesson lesson) {
        if (lessonRepository.existsById(id)) {
            lesson.setCourseId(id);
            return lessonRepository.save(lesson);
        }
        return null;
    }

    public void deleteLesson(String id) {
        lessonRepository.deleteById(id);
    }

    public List<Lesson> getLessonsByCourseId(String courseId) {
        return lessonRepository.findByCourseId(courseId);
    }
}
