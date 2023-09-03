package ru.dolya.deal.service;

public interface SesService {
    void setSes(Long applicationId, Integer ses);

    void verifySes(Long applicationId, Integer sesCode);
}
