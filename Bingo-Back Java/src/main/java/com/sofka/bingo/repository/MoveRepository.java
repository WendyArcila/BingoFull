package com.sofka.bingo.repository;

import com.sofka.bingo.domain.Move;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Esta interfaz representa un repositorio de movimientos.
 * Extiende la interfaz JPA de Spring Data {@link org.springframework.data.repository.CrudRepository}.
 */
public interface MoveRepository extends CrudRepository <Move, Long> {

    /**
     * Actualiza el estado de un juego con el ID dado.
     * @param idGame el ID del juego a actualizar
     * @param statusGame el nuevo estado del juego
     */
    @Modifying
    @Query("update Game game set game.statusGame = :statu_id where game.idGame = :gam_id")
    public void updateStatusG(
            @Param(value = "gam_id") Long idGame,
            @Param(value = "statu_id") int statusGame);


}
