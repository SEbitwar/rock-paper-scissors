package com.goudscode.servive;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.UUID;

public interface GameService {

    ObjectNode startGame();

    Object playRandom(String token, String userMove);

    Object playClever(String token, String userMove);
}
