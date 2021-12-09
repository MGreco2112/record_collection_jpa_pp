package com.recordcollection.recorddatabase.repositories;

import com.recordcollection.recorddatabase.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}
