package com.sofka.bingo.service;

import com.sofka.bingo.domain.Gamer;
import com.sofka.bingo.domain.Status;
import com.sofka.bingo.repository.GamerRepository;
import com.sofka.bingo.service.interfaces.IGamer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * El servicio GamerService establece los métodos para manejar los jugadores de bingo.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
@Service
public class GamerService implements IGamer {
    @Autowired
    private GamerRepository gamerRepository;

    /**
     * Retorna una lista de todos los gamers.
     * @return una lista de objetos Gamer
     */
    @Override
    @Transactional()
    public List<Gamer> getGamerList() {
        return (List<Gamer>) gamerRepository.findAll();
    }

    /**
     * Encuentra y retorna un Gamer por su ID.
     * @param id el ID del Gamer a buscar
     * @return un objeto Optional que contiene el Gamer, o un objeto vacío si no se encuentra
     */
    @Override
    @Transactional
    public Optional<Gamer> findGamer(Long id) {
        return gamerRepository.findById(id);
    }

    /**
     * Crea un nuevo Gamer con el estado especificado y lo agrega a la lista de gamers.
     * @param gamer el nuevo Gamer a crear
     * @param status el estado inicial del Gamer
     * @return el objeto Gamer recién creado
     */
    @Override
    @Transactional
    public Gamer postGamer(Gamer gamer, Status status) {
        gamer.setCreationAt(Instant.now());
        gamer.setStatusGamer(status);
        return gamerRepository.save(gamer);
    }

    /**
     * Actualiza un Gamer existente con un nuevo objeto Gamer.
     * @param id el ID del Gamer a actualizar
     * @param gamer el nuevo objeto Gamer
     * @return el objeto Gamer actualizado
     */
    @Override
    @Transactional
    public Gamer putGamer(Long id, Gamer gamer) {
        gamer.setIdGamer(id);
        gamer.setUpdateAt(Instant.now());
        return gamerRepository.save(gamer);
    }

    /**
     * Actualiza el estado del Gamer con el ID especificado en la base de datos.
     * @param id el ID del Gamer a actualizar
     * @param gamer el objeto Gamer con el estado actualizado
     */
    @Transactional
    public void updateStatusGamerS(Long id, Gamer gamer){
        gamer.setUpdateAt(Instant.now());
        gamerRepository.updateGamerStatus(gamer.getStatusGamer().getIdStatus(), id);
    }

    /**
     * Actualiza la tabla del Gamer con el ID especificado en la base de datos.
     * @param id el ID del Gamer a actualizar
     * @param gamer el objeto Gamer con la tabla actualizada
     */
    @Transactional
    public void updateBoardGamerS(Long id, Gamer gamer){
        gamer.setUpdateAt(Instant.now());
        gamerRepository.updateGamerBoard(gamer.getBoard().getIdBoard(), id);
    }

    /**
     * Actualiza el juego del Gamer con el ID especificado en la base de datos.
     * @param id el ID del Gamer a actualizar
     * @param gamer el objeto Gamer con el juego actualizado
     */
    @Transactional
    public void updateGameGamerS(Long id, Gamer gamer){
        Gamer gamerId = findGamer(id).get();
        gamerId.setUpdateAt(Instant.now());
        gamerId.setGame(gamer.getGame());
        gamerRepository.save(gamerId);
    }

    /**
     * Elimina un Gamer por su ID.
     * @param id el ID del Gamer a eliminar
     */
    @Override
    @Transactional
    public void deleteGamer(Long id) {
        gamerRepository.deleteById(id);
    }

    /**
     * Retorna verdadero si un Gamer con el ID especificado existe en la base de datos, de lo contrario falso.
     * @param id el ID del Gamer a buscar
     * @return verdadero si existe un Gamer con el ID especificado, de lo contrario falso
     */
    public boolean existsByIdGamer(Long id){
        return gamerRepository.existsById(id);
    }

    /**
     * Busca y retorna un Gamer por su nombre de usuario.
     * @param user el nombre de usuario del Gamer a buscar
     * @return el objeto Gamer correspondiente al nombre de usuario especificado, o null si no se encuentra
     */
    public Gamer existUser(String user) {
        return gamerRepository.findByUser(user);
    }

}
