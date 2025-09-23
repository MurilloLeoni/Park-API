package com.mleoni.demo_park_api.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_clients_have_spots")
@EntityListeners(AuditingEntityListener.class)
public class ClientSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_receipt", nullable = false, unique = true, length = 15)
    private String receipt;

    @Column(nullable = false, length = 8)
    private String plate;

    @Column(nullable = false, length = 45)
    private String brand;

    @Column(nullable = false, length = 45)
    private String model;

    @Column(nullable = false, length = 45)
    private String color;

    @Column(nullable = false)
    private LocalDateTime entryDate;

    private LocalDateTime exitDate;

    @Column(name = "spot_value", columnDefinition = "decimal(7,2)")
    private BigDecimal value;

    @Column(name = "discount", columnDefinition = "decimal(7,2)")
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_spot", nullable = false)
    private ParkingSpot parkingSpot;

    @CreatedDate
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "modification_date")
    private LocalDateTime modificationDate;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientSpot that = (ClientSpot) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
