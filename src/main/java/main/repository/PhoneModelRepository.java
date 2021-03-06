package main.repository;

import java.util.List;
import main.model.PhoneModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneModelRepository extends JpaRepository<PhoneModel, Long> {

	PhoneModel getByModelNameAndDeleted(String modelName, boolean deleted);

	List<PhoneModel> findAllByDeleted(boolean deleted);
}
