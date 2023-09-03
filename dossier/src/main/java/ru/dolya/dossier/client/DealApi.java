package ru.dolya.dossier.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "deal", url = "${feign.client.server}")
public interface DealApi {
    @PutMapping("/admin/application/{applicationId}/status")
    void status(@PathVariable Long applicationId);

    @PutMapping("/admin/application/{applicationId}/ses")
    void setSes(@PathVariable Long applicationId, @RequestBody Integer ses);
}
