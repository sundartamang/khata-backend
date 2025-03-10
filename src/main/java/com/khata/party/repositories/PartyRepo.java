package com.khata.party.repositories;

import com.khata.party.entity.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepo extends JpaRepository<Party, Integer> {
    Page<Party> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
