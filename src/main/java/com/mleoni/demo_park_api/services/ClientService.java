package com.mleoni.demo_park_api.services;

import com.mleoni.demo_park_api.entities.Client;
import com.mleoni.demo_park_api.exception.CpfUniqueViolationException;
import com.mleoni.demo_park_api.exception.EntityNotFoundException;
import com.mleoni.demo_park_api.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;


    @Transactional
    public Client save(Client client) {
        try {
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(String.format("CPF {%s} cannot be registered, it already exist in the system."
                    , client.getCpf()));
        }
    }

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Clint id=%s not found in the system", id))
        );
    }
}
