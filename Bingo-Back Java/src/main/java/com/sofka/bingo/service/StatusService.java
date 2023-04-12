package com.sofka.bingo.service;

import com.sofka.bingo.domain.Status;
import com.sofka.bingo.repository.StatusRepository;
import com.sofka.bingo.service.interfaces.IStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio StatusService establece las operaciones para interactuar con los estados de los jugadores en un juego.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
@Service
public class StatusService implements IStatus {
    @Autowired
    private StatusRepository statusRepository;

    /**
     * Retorna una lista de todos los estados de los jugadores en la base de datos.
     * @return una lista de objetos Status que representan todos los estados de los jugadores en la base de datos
     */
    @Override
    @Transactional()
    public List<Status> getStatusList() {
        return (List<Status>) statusRepository.findAll();
    }

    /**
     * Busca y retorna un estado de jugador por su ID.
     * @param id el ID del estado de jugador a buscar
     * @return el objeto Status correspondiente al ID especificado, o null si no se encuentra
     */
    @Override
    @Transactional
    public Optional<Status> findStatus(Long id) {
        return statusRepository.findById(id);
    }

    /**
     * Agrega un nuevo estado de jugador a la base de datos.
     * @param status el objeto Status a agregar a la base de datos
     * @return el objeto Status que se ha agregado a la base de datos
     */
    @Override
    @Transactional
    public Status postStatus(Status status) {
        return statusRepository.save(status);
    }

    /**
     * Actualiza un estado de jugador existente en la base de datos.
     * @param id el ID del estado de jugador a actualizar
     * @param status el objeto Status actualizado
     * @return el objeto Status actualizado que se ha guardado en la base de datos
     */
    @Override
    @Transactional
    public Status putStatus(Long id, Status status) {
        status.setIdStatus(id);
        return statusRepository.save(status);
    }

    /**
     * Elimina un estado de jugador existente de la base de datos.
     * @param status el objeto Status a eliminar de la base de datos
     */
    @Override
    @Transactional
    public void deleteStatus(Status status) {
        statusRepository.delete(status);
    }

    public boolean existsByIdStatus(Long id){
        return statusRepository.existsById(id);
    }
}
