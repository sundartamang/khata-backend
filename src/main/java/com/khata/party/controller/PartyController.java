package com.khata.party.controller;

import com.khata.party.dto.PartyDTO;
import com.khata.party.service.PartyService;
import com.khata.payload.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/party/")
public class PartyController {

    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @PostMapping("/")
    public ResponseEntity<PartyDTO> createParty(@Valid @RequestBody PartyDTO partyDTO){
        PartyDTO party = this.partyService.createParty(partyDTO);
        return new ResponseEntity<>(party, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<PartyDTO>> getPartyList(Pageable pageable){
        Page<PartyDTO> partyDTOPage = this.partyService.getParties(pageable);
        return ResponseEntity.ok(partyDTOPage);
    }

    @PutMapping("/{partyId}")
    public ResponseEntity<PartyDTO> updateParty(@Valid @RequestBody PartyDTO partyDTO, @PathVariable Integer partyId){
        PartyDTO party = this.partyService.updateParty(partyDTO, partyId);
        return ResponseEntity.ok(party);
    }

    @GetMapping("/{partyId}")
    public ResponseEntity<PartyDTO> getPartyDetails(@PathVariable Integer partyId){
        PartyDTO partyDTO = this.partyService.getPartyById(partyId);
        return ResponseEntity.ok(partyDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PartyDTO>> searchPartyByName(@RequestParam String keyword, Pageable pageable){
        Page<PartyDTO> partyDTOPage = this.partyService.searchPartyByName(keyword, pageable);
        return ResponseEntity.ok(partyDTOPage);
    }

    @DeleteMapping("/{partyId}")
    public ResponseEntity<ApiResponse> deleteParty(@PathVariable Integer partyId){
        this.partyService.deleteParty(partyId);
        return ResponseEntity.ok(new ApiResponse("Deleted successfully", true));
    }

}
