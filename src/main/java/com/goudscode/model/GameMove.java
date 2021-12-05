package com.goudscode.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GameMove {

    @Id
    private String token;
    @Enumerated(value = EnumType.STRING)
    private Move serverMove;
    @Enumerated(value = EnumType.STRING)
    private Move userMove;
    private int serverScore;
    private int userScore;
}
