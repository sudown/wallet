package br.com.wallet.wallet.presentation.controllers;

import br.com.wallet.wallet.application.WalletCommandService;
import br.com.wallet.wallet.application.commands.CreateWalletCommand;
import br.com.wallet.wallet.application.commands.DepositCommand;
import br.com.wallet.wallet.application.dispatcher.WalletCommandDispatcher;
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
    private final WalletCommandDispatcher dispatcher;

    public WalletController(WalletCommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Void> deposit(
            @PathVariable UUID id,
            @RequestBody DepositRequest request) {

        var command = new DepositCommand(id, request.amount());
        System.out.println("Chamando dispatcher para o comando: " + command.getClass());
        dispatcher.execute(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> create(@RequestBody CreateWalletRequest request) {
        UUID id = dispatcher.execute(new CreateWalletCommand(request.ownerId()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("id", id));
    }
}
