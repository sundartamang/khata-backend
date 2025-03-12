package com.khata.party.service.impl;

import com.khata.exceptions.ResourceNotFoundException;
import com.khata.party.dto.PartyDTO;
import com.khata.party.entity.Party;
import com.khata.party.repositories.PartyRepo;
import com.khata.party.service.PartyService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PartyServiceImpl implements PartyService {

    private final PartyRepo partyRepo;
    private final ModelMapper modelMapper;

    public PartyServiceImpl(PartyRepo partyRepo, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.partyRepo = partyRepo;
    }

    @Override
    @Transactional
    public PartyDTO createParty(PartyDTO partyDTO) {
        Party party = modelMapper.map(partyDTO, Party.class);
        Party savedParty = partyRepo.save(party);
        log.info("Party created with name: {}", partyDTO.getName());
        return modelMapper.map(savedParty, PartyDTO.class);
    }

    @Override
    @Transactional
    public PartyDTO updateParty(PartyDTO partyDTO, Integer partyId) {
        Party party = getPartyEntityById(partyId);
        modelMapper.map(partyDTO, party);
        Party updatedParty = partyRepo.save(party);
        log.info("Party updated with ID: {}", partyId);
        return modelMapper.map(updatedParty, PartyDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PartyDTO getPartyById(Integer partyId) {
        Party party = getPartyEntityById(partyId);
        return modelMapper.map(party, PartyDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PartyDTO> getParties(Pageable pageable) {
        Page<Party> parties = partyRepo.findAll(pageable);
        return parties.map(party -> modelMapper.map(party, PartyDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PartyDTO> searchPartyByName(String name, Pageable pageable) {
        Page<Party> parties = partyRepo.findByNameContainingIgnoreCase(name, pageable);
        return parties.map(party -> modelMapper.map(party, PartyDTO.class));
    }

    @Override
    public void deleteParty(Integer partyId) {
        Party party = getPartyEntityById(partyId);
        partyRepo.delete(party);
        log.info("Party deleted with ID: {}", partyId);
    }

    private Party getPartyEntityById(Integer partyId) {
        return partyRepo.findById(partyId).orElseThrow(
                () -> new ResourceNotFoundException("Party", "id", partyId)
        );
    }
}