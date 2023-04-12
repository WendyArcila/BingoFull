package com.sofka.bingo.repository;

import com.sofka.bingo.domain.Status;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Esta interfaz representa un repositorio de estados de juego.
 * Extiende la interfaz JPA {@link org.springframework.data.repository.CrudRepository} de Spring Data.
 *
 */
public interface StatusRepository extends CrudRepository <Status, Long> {

    /**
     * Actualiza el estado de un juego con el ID dado.
     *
     * @param idGame el ID del juego a actualizar
     * @param statusGame el nuevo estado del juego
     */
    @Modifying
    @Query("update Game game set game.statusGame = :statu_id where game.idGame = :gam_id")
    public void updateStatusG(
            @Param(value = "gam_id") Long idGame,
            @Param(value = "statu_id") long statusGame);

}
