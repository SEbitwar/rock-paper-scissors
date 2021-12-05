package com.goudscode.controller;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.goudscode.servive.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class MainController {

    private final GameService gameService;

    public MainController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/start")
    public ResponseEntity<Object> startGame() {

        ObjectNode response = gameService.startGame();
        return new ResponseEntity<>(response, OK);
    }

    @GetMapping("/v1/{token}/{userMove}")
    public ResponseEntity<Object> playRandom(@PathVariable("token") UUID token, @PathVariable("userMove") String userMove) {

        Object gameMove = gameService.playRandom(token.toString(), userMove);

        return new ResponseEntity<>( gameMove, OK);
    }

    @GetMapping("/v2/{token}/{userMove}")
    public ResponseEntity<Object> playClever(@PathVariable("token") UUID token, @PathVariable("userMove") String userMove) {

        Object gameMove = gameService.playClever(token.toString(), userMove);

        return new ResponseEntity<>( gameMove, OK);

    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        if (ex instanceof HttpClientErrorException)
            return new ResponseEntity<>(ex.getMessage(), ((HttpClientErrorException) ex).getStatusCode());
        return new ResponseEntity<>(ex.getMessage(), INTERNAL_SERVER_ERROR);
    }
}
