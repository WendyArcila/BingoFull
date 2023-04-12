package com.sofka.bingo.repository;


import com.sofka.bingo.domain.Gamer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Esta interfaz representa un repositorio para jugadores.
 * Extiende la interfaz JPA de Spring Data {@link org.springframework.data.repository.CrudRepository}.
 */
public interface GamerRepository extends CrudRepository <Gamer, Long> {

    /**
     * Actualiza el estado de un jugador con el ID dado.
     * @param statusGamer el ID del nuevo estado
     * @param idGamer el ID del jugador a actualizar
     */
    @Modifying
    @Query("UPDATE Gamer gamer set gamer.statusGamer.idStatus = :statu_id where gamer.idGamer = :id_gamer")
    public void updateGamerStatus(
             @Param(value = "statu_id") Long statusGamer,
             @Param(value = "id_gamer") Long idGamer);

    /**
     * Actualiza el tablero de un jugador con el ID dado.
     * @param board el ID del nuevo tablero
     * @param idGamer el ID del jugador a actualizar
     */
    @Modifying
    @Query("UPDATE Gamer gamer set gamer.board.idBoard = :boa_id where gamer.idGamer = :id_gamer")
    public void updateGamerBoard(
            @Param(value = "boa_id") Long board,
            @Param(value = "id_gamer") Long idGamer);


    /**
     * Encuentra un jugador por su nombre de usuario.
     * @param user el nombre de usuario del jugador a encontrar
     * @return  el jugador con el nombre de usuario dado o {@literal null} si no se encuentra ninguno
     */
    @Query("SELECT gamer FROM Gamer gamer WHERE gamer.user = :user")
    public Gamer findByUser(@Param("user") String user);
}
