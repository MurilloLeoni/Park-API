package com.mleoni.demo_park_api.services;

import com.mleoni.demo_park_api.entities.ParkingSpot;
import com.mleoni.demo_park_api.exception.CodeUniqueViolationException;
import com.mleoni.demo_park_api.exception.EntityNotFoundException;
import com.mleoni.demo_park_api.repositories.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ParkingSpotService {

    private final ParkingSpotRepository spotRepository;

    @Transactional
    public ParkingSpot save(ParkingSpot spot) {
        try {
            return spotRepository.save(spot);
        } catch (DataIntegrityViolationException ex) {
            throw new CodeUniqueViolationException(
                    String.format("Spot with code '%s' already registered", spot.getCode()));
        }
    }

    @Transactional(readOnly = true)
    public ParkingSpot findByCode(String code) {
        return spotRepository.findByCode(code).orElseThrow(
                () -> new EntityNotFoundException(String.format("Spot with code '%s' not found", code))
        );
    }
}
