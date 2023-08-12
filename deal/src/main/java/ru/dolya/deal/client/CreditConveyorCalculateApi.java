package ru.dolya.deal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dolya.deal.dto.CreditDTO;
import ru.dolya.deal.dto.ScoringDataDTO;


@FeignClient(name = "conveyorCalculation", url = "http://localhost:8080/conveyor/calculation")
public interface CreditConveyorCalculateApi {
    @PostMapping
    CreditDTO calculate(@RequestBody ScoringDataDTO scoringDataDTO);
}

