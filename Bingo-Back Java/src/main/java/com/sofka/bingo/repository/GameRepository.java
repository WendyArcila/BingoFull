package com.sofka.bingo.repository;

import com.sofka.bingo.domain.Game;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;



/**
 * Esta interfaz representa un repositorio para juegos.
 * Extiende la interfaz JPA {@link org.springframework.data.repository.CrudRepository} de Spring Data.
 *
 */
public interface GameRepository extends CrudRepository <Game, Long> {

    /**
     * Actualiza el ganador de un juego con el ID dado.
     * @param idGame el ID del juego a actualizar
     * @param winner el nombre del ganador
     */
    @Modifying
    @Query("update Game game set game.winner = :winner where game.idGame = :gam_id")
    public void updateWinnerG(
            @Param(value = "gam_id") Long idGame,
            @Param(value = "winner") String winner);


}


