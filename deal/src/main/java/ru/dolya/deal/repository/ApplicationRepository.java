package ru.dolya.deal.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dolya.deal.model.domain.Application;


public interface ApplicationRepository extends CrudRepository<Application, Long> {
}
