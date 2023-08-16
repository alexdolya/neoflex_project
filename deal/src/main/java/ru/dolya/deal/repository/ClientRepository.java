package ru.dolya.deal.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dolya.deal.model.domain.Client;


public interface ClientRepository extends CrudRepository <Client, Long> {

}
