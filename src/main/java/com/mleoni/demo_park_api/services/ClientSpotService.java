package com.mleoni.demo_park_api.services;

import com.mleoni.demo_park_api.entities.ClientSpot;
import com.mleoni.demo_park_api.exception.EntityNotFoundException;
import com.mleoni.demo_park_api.repositories.ClientSpotRepository;
import com.mleoni.demo_park_api.repositories.projection.ClientSpotProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientSpotService {

    private final ClientSpotRepository repository;

    @Transactional
    public ClientSpot save(ClientSpot clientSpot) {
        return repository.save(clientSpot);
    }

    @Transactional(readOnly = true)
    public ClientSpot findByReceipt(String receipt) {
        return repository.findByReceiptAndExitDateIsNull(receipt).orElseThrow(
                () -> new EntityNotFoundException(String.format("Receipt '%s' not found in the system or checkout already completed", receipt)
                )
        );
    }

    @Transactional(readOnly = true)
    public long getNumberOfTimesComplete(String cpf) {
        return repository.countByClientCpfAndExitDateIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ClientSpotProjection> findAllByClientCpf(String cpf, Pageable pageable) {
        return repository.findAllByClientCpf(cpf, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ClientSpotProjection> findAllByUserId(Long id, Pageable pageable) {
        return repository.findAllByClientUserId(id, pageable);
    }
}
