package uk.gov.hmcts.reform.dev.repositories;

import uk.gov.hmcts.reform.dev.models.TaskModel;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<TaskModel, Integer> {
}
