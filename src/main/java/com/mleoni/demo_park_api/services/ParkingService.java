package com.mleoni.demo_park_api.services;

import com.mleoni.demo_park_api.entities.Client;
import com.mleoni.demo_park_api.entities.ClientSpot;
import com.mleoni.demo_park_api.entities.ParkingSpot;
import com.mleoni.demo_park_api.util.ParkingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ParkingService {

    private final ClientSpotService clientSpotService;
    private final ClientService clientService;
    private final ParkingSpotService parkingSpotService;

    @Transactional
    public ClientSpot checkIn(ClientSpot clientSpot) {
        Client client = clientService.findByCpf(clientSpot.getClient().getCpf());
        clientSpot.setClient(client);

        ParkingSpot spot = parkingSpotService.findBySpots();
        spot.setStatus(ParkingSpot.StatusVaga.OCCUPIED);
        clientSpot.setParkingSpot(spot);

        clientSpot.setEntryDate(LocalDateTime.now());

        clientSpot.setReceipt(ParkingUtils.generateReceipt());

        return clientSpotService.save(clientSpot);
    }

    @Transactional
    public ClientSpot checkOut(String receipt) {
        ClientSpot clientSpot = clientSpotService.findByReceipt(receipt);

        LocalDateTime exitDate = LocalDateTime.now();

        BigDecimal value = ParkingUtils.calculateCost(clientSpot.getEntryDate(), exitDate);
        clientSpot.setValue(value);

        long numberOfTimes = clientSpotService.getNumberOfTimesComplete(clientSpot.getClient().getCpf());

        BigDecimal discount = ParkingUtils.calculateDiscount(value, numberOfTimes);
        clientSpot.setDiscount(discount);

        clientSpot.setExitDate(exitDate);
        clientSpot.getParkingSpot().setStatus(ParkingSpot.StatusVaga.AVAILABLE);

        return clientSpotService.save(clientSpot);
    }
}
