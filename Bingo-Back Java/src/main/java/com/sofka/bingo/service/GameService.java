package com.sofka.bingo.service;


import com.sofka.bingo.domain.Game;
import com.sofka.bingo.domain.Status;
import com.sofka.bingo.repository.GameRepository;
import com.sofka.bingo.service.interfaces.IGame;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de GameService establece los métodos para manejar los juegos de bingo.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
@Service
public class GameService implements IGame {
    @Autowired
    private GameRepository gameRepository;

    /**
     * Retorna una lista de todos los juegos disponibles en el sistema.
     * @return una lista de objetos Game
     */
    @Override
    @Transactional
    public List<Game> getGameList() {
        return (List<Game>) gameRepository.findAll();
    }

    /**
     * Busca un juego por su identificador único.
     * @param id el identificador único del juego a buscar
     * @return un objeto Optional que contiene el juego si se encuentra, o vacío si no se encuentra
     */
    @Override
    @Transactional
    public Optional<Game> findGame(Long id) {
        return gameRepository.findById(id);
    }

    /**
     * Crea un nuevo juego de bingo.
     * @param game el objeto Game que representa el nuevo juego
     * @param status el objeto Status que indica el estado inicial del juego
     * @return el objeto Game creado
     */
    @Override
    @Transactional
    public Game postGame(Game game, Status status) {
        game.setCreationAt(Instant.now());
        game.setStatusGame(status);
        return gameRepository.save(game);
    }

    /**
     * Actualiza un juego existente con nuevos datos.
     * @param id el identificador único del juego a actualizar
     * @param game el objeto Game que contiene los datos actualizados
     * @return el objeto Game actualizado
     */
    @Override
    @Transactional
    public Game putGame(Long id, Game game) {
        game.setIdGame(id);
        game.setUpdateAt(Instant.now());
        return gameRepository.save(game);
    }

    /**
     * Actualiza el estado de un juego existente.
     * @param id el identificador único del juego a actualizar
     * @param game el objeto Game que contiene los datos actualizados del estado del juego
     */
    @Transactional
    public void updateStatusG(Long id, Game game){
       Game gameId = findGame(id).get();
       gameId.setUpdateAt(Instant.now());
       gameId.setStatusGame(game.getStatusGame());
       gameRepository.save(gameId);
    }

    /**
     * Actualiza el ganador de un juego existente.
     * @param id el identificador único del juego a actualizar
     * @param game el objeto Game que contiene los datos actualizados del ganador del juego
     */
    @Transactional
    public void updateWinnerG(Long id, Game game){
        gameRepository.updateWinnerG(id, game.getWinner());
    }

    /**
     * Elimina un juego existente.
     * @param id el identificador único del juego a eliminar
     */
    @Override
    @Transactional
    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    /**
     * Busca si existe un juego creado con el ID dado
     * @param id Corresponde al ID a buscar
     * @return retorna un boolean según si se encuentra el juego o no.
     */
    public boolean existsByIdGame(Long id){
        return gameRepository.existsById(id);
    }
}
