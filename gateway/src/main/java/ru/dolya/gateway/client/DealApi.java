package ru.dolya.gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dolya.gateway.dto.FinishRegistrationRequestDTO;

@FeignClient(name = "deal", url = "${feign.client.deal}")
public interface DealApi {
    @PutMapping("/calculate/{applicationId}")
    void calculateById(@PathVariable Long applicationId, @RequestBody FinishRegistrationRequestDTO requestDTO);

    @PutMapping("/document/{applicationId}/send")
    void send(@PathVariable Long applicationId);

    @PutMapping("/document/{applicationId}/sign")
    void sign(@PathVariable Long applicationId);

    @PutMapping("/document/{applicationId}/code")
    void verifySes(@PathVariable Long applicationId, @RequestBody Integer sesCode);

}
