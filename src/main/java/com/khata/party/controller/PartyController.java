package com.khata.party.controller;

import com.khata.party.dto.PartyDTO;
import com.khata.party.service.PartyService;
import com.khata.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/party")
@AllArgsConstructor
public class PartyController {

    private final PartyService partyService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<PartyDTO>> createParty(@Valid @RequestBody PartyDTO partyDTO){
        PartyDTO party = this.partyService.createParty(partyDTO);
        ApiResponse<PartyDTO> response = new ApiResponse<>(party, HttpStatus.CREATED.value(), "Party Created Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PartyDTO>>> getPartyList(Pageable pageable){
        Page<PartyDTO> partyDTOPage = this.partyService.getParties(pageable);
        return ResponseEntity.ok(new ApiResponse<>(partyDTOPage, HttpStatus.OK.value()));
    }

    @PutMapping("/{partyId}")
    public ResponseEntity<ApiResponse<PartyDTO>> updateParty(@Valid @RequestBody PartyDTO partyDTO, @PathVariable Integer partyId){
        PartyDTO party = this.partyService.updateParty(partyDTO, partyId);
        ApiResponse<PartyDTO> response = new ApiResponse<>(party, HttpStatus.OK.value(), "Party Updated Successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{partyId}")
    public ResponseEntity<ApiResponse<PartyDTO>> getPartyDetails(@PathVariable Integer partyId){
        PartyDTO partyDTO = this.partyService.getPartyById(partyId);
        return ResponseEntity.ok(new ApiResponse<>(partyDTO, HttpStatus.OK.value()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PartyDTO>>> searchPartyByName(@RequestParam String keyword, Pageable pageable){
        Page<PartyDTO> partyDTOPage = this.partyService.searchPartyByName(keyword, pageable);
        return ResponseEntity.ok(new ApiResponse<>(partyDTOPage, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{partyId}")
    public ResponseEntity<ApiResponse<Void>> deleteParty(@PathVariable Integer partyId){
        this.partyService.deleteParty(partyId);
        return ResponseEntity.ok(new ApiResponse<>(null, HttpStatus.OK.value(),"Party Deleted Successfully"));
    }

}
