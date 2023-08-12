package ru.dolya.deal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.dolya.deal.domain.Application;


public interface ApplicationRepository extends CrudRepository<Application, Long> {
}
