package uk.gov.hmcts.reform.dev.repositories;

import uk.gov.hmcts.reform.dev.models.TaskModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Sort;


public interface TaskRepository extends JpaRepository<TaskModel, Integer> {
    Iterable<TaskModel> findByStatus(String status);

    Iterable<TaskModel> findByStatus(String status, Sort sort);

    @Query("SELECT DISTINCT status FROM TaskModel")
    Iterable<String> findDistinctStatus();
}
