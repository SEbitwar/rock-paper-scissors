package com.goudscode.repo;

import com.goudscode.model.GameMove;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GameRepo extends CrudRepository<GameMove, String> {
}
