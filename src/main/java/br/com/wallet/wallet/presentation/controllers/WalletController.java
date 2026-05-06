package br.com.wallet.wallet.presentation.controllers;

import br.com.wallet.wallet.application.WalletCommandService;
import br.com.wallet.wallet.application.dto.CreateWalletRequest;
import br.com.wallet.wallet.application.dto.DepositRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletCommandService commandService;

    public WalletController(WalletCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Void> deposit(
            @PathVariable UUID id,
            @RequestBody DepositRequest request) {

        commandService.handleDeposit(id, request.amount());
        return ResponseEntity.accepted().build();
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> create(@RequestBody CreateWalletRequest request) {
        UUID id = commandService.createWallet(request.ownerId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("id", id));
    }}
