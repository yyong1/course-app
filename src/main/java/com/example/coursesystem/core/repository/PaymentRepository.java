package com.example.coursesystem.core.repository;

import com.example.coursesystem.core.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    List<Payment> findByUserId(String userId);

    List<Payment> findByCourseId(String courseId);
}
