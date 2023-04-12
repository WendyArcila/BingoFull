package com.sofka.bingo.service.interfaces;

import com.sofka.bingo.domain.Game;
import com.sofka.bingo.domain.Status;
import java.util.List;
import java.util.Optional;

/**
 * La interfaz IGame define los métodos para manejar los juegos de bingo.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
public interface IGame {

    /**
     * Retorna una lista de todos los juegos disponibles en el sistema.
     * @return una lista de objetos Game
     */
    public List<Game> getGameList();

    /**
     * Busca un juego por su identificador único.
     * @param id el identificador único del juego a buscar
     * @return un objeto Optional que contiene el juego si se encuentra, o vacío si no se encuentra
     */
    Optional<Game> findGame(Long id);

    /**
     * Crea un nuevo juego de bingo.
     * @param game el objeto Game que representa el nuevo juego
     * @param status el objeto Status que indica el estado inicial del juego
     * @return el objeto Game creado
     */
    Game postGame(Game game, Status status);

    /**
     * Actualiza un juego existente con nuevos datos.
     * @param id el identificador único del juego a actualizar
     * @param game el objeto Game que contiene los datos actualizados
     * @return el objeto Game actualizado
     */
    Game putGame(Long id, Game game);

    /**
     * Elimina un juego existente.
     * @param id el identificador único del juego a eliminar
     */
    void deleteGame(Long id);
}
