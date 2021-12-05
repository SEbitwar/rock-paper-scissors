package com.goudscode.servive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.goudscode.model.GameMove;
import com.goudscode.model.Move;
import com.goudscode.repo.GameRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

import static com.goudscode.model.Move.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class GameServiceImpl implements GameService{

    private final GameRepo gameRepo;
    private final ObjectMapper objectMapper;

    public GameServiceImpl(GameRepo gameRepo, ObjectMapper objectMapper) {
        this.gameRepo = gameRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    public ObjectNode startGame() {

        UUID token = UUID.randomUUID();

        GameMove gameMove = GameMove.builder()
                .token(token.toString())
                .serverScore(0)
                .userScore(0).build();

        gameRepo.save(gameMove);

        ObjectNode response = objectMapper.createObjectNode();
        response.put("response", "READY");
        response.put("token", token.toString());

        return response;
    }

    @Override
    public Object playRandom(String token, String userMove) {

        GameMove newMove = changeScoreRandom(userMove, token);

        if ( newMove.getUserScore() >= 3) {
            gameRepo.deleteById(newMove.getToken());
            return "Congrats.. You Won The Game..!!!";
        }
        if ( newMove.getServerScore() >= 3) {
            gameRepo.deleteById(newMove.getToken());
            return "Oops.. You Lost The Game..!!!";
        }
        gameRepo.save(newMove);

        return newMove;
    }

    @Override
    public Object playClever(String token, String userMove) {



        GameMove newMove = changeScoreClever(userMove, token);

        if ( newMove.getUserScore() >= 3) {
            gameRepo.deleteById(newMove.getToken());
            return "Congrats.. You Won The Game..!!!";
        }
        if ( newMove.getServerScore() >= 3) {
            gameRepo.deleteById(newMove.getToken());
            return "Oops.. You Lost The Game..!!!";
        }
        gameRepo.save(newMove);
        return newMove;
    }

    private GameMove changeScoreClever(String userMove, String token) {

        GameMove gameMove = gameRepo.findById(token).orElse(null);

        if ( gameMove == null)
            throw  new HttpClientErrorException("Token not present", BAD_REQUEST, null, null, null, null);

        int serverScore = gameMove.getServerScore();

        if (userMove.equalsIgnoreCase(ROCK.toString())) {
            gameMove.setUserMove(ROCK);
            gameMove.setServerMove(PAPER);
            gameMove.setServerScore( serverScore + 1);
        }
        else if (userMove.equalsIgnoreCase(PAPER.toString())) {
            gameMove.setUserMove(PAPER);
            gameMove.setServerMove(SCISSOR);
            gameMove.setServerScore( serverScore + 1);
        }
        else if (userMove.equalsIgnoreCase(SCISSOR.toString())) {
            gameMove.setUserMove(SCISSOR);
            gameMove.setServerMove(ROCK);
            gameMove.setServerScore( serverScore + 1);
        }
        else {
            throw new HttpClientErrorException("not a valid move", BAD_REQUEST, null, null, null, null);
        }
        return gameMove;
    }

    private GameMove changeScoreRandom(String userMove, String token) {



        GameMove gameMove = gameRepo.findById(token).orElse(null);

        if ( gameMove == null)
            throw  new HttpClientErrorException("Token not present", BAD_REQUEST, null, null, null, null);

        double random = Math.ceil(Math.random() * 3.0);
        Move move = null;

        if (random <= 1.0)
            move = ROCK;
        else if (random <= 2.0)
            move = PAPER;
        else if (random <= 3.0)
            move = SCISSOR;

        String serverMove = move.toString();

        int serverScore = gameMove.getServerScore();
        int userScore = gameMove.getUserScore();

        if (userMove.equalsIgnoreCase(ROCK.toString())) {
            if ( serverMove.equalsIgnoreCase(userMove)) {
                gameMove.setUserMove(ROCK);
                gameMove.setServerMove(ROCK);
                return gameMove;
            }
            if (serverMove.equalsIgnoreCase(PAPER.toString())) {
                gameMove.setServerScore(serverScore +1);
                gameMove.setServerMove(PAPER);
                gameMove.setUserMove(ROCK);
                return gameMove;
            }
            gameMove.setUserMove(ROCK);
            gameMove.setServerMove(SCISSOR);
            gameMove.setUserScore( userScore + 1);
        }

        else if (userMove.equalsIgnoreCase(PAPER.toString())) {
            if ( serverMove.equalsIgnoreCase(userMove)) {
                gameMove.setUserMove(PAPER);
                gameMove.setServerMove(PAPER);
                return gameMove;
            }
            if (serverMove.equalsIgnoreCase(SCISSOR.toString())) {
                gameMove.setServerScore(serverScore +1);
                gameMove.setServerMove(SCISSOR);
                gameMove.setUserMove(PAPER);
                return gameMove;
            }
            gameMove.setUserMove(PAPER);
            gameMove.setServerMove(ROCK);
            gameMove.setUserScore( userScore + 1);
        }

        else if (userMove.equalsIgnoreCase(SCISSOR.toString())) {
            if ( serverMove.equalsIgnoreCase(userMove)) {
                gameMove.setUserMove(SCISSOR);
                gameMove.setServerMove(SCISSOR);
                return gameMove;
            }
            if (serverMove.equalsIgnoreCase(ROCK.toString())) {
                gameMove.setServerScore(serverScore +1);
                gameMove.setServerMove(ROCK);
                gameMove.setUserMove(SCISSOR);
                return gameMove;
            }
            gameMove.setUserMove(SCISSOR);
            gameMove.setServerMove(PAPER);
            gameMove.setUserScore( userScore + 1);
        }
        else {
            throw new HttpClientErrorException("not a valid move", BAD_REQUEST, null, null, null, null);
        }
        return gameMove;
    }
}
