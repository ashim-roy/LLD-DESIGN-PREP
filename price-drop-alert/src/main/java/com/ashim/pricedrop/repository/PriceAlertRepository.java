package com.ashim.pricedrop.repository;

import com.ashim.pricedrop.model.PriceAlert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PriceAlertRepository {

    PriceAlert save(PriceAlert alert);

    Optional<PriceAlert> findById(UUID id); // by alert id

    List<PriceAlert> findAll();

    List<PriceAlert> findByUserId(UUID userId);

    List<PriceAlert> findByProductId(UUID productId);
    

}
