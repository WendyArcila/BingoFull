package com.sofka.bingo.controller;

import com.sofka.bingo.domain.Status;
import com.sofka.bingo.service.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;
@Slf4j
@RestController
public class StatusController {

    /**
     * Servicio para el manejo del juego (game)
     */
    @Autowired
    private StatusService statusService;


    /**
     * Manejo del código HTTP que se responde en las API
     */
    private HttpStatus httpStatus = HttpStatus.OK;

    /**
     * Función encargada de devolver una lista de todos los estados disponibles a través de una solicitud HTTP GET.
     * La ruta de acceso es "/status/all".
     * @return Un objeto ResponseEntity que contiene la lista de estados y el estado HTTP correspondiente.
     *         Si ocurre algún error, se devuelve un objeto ResponseEntity con un estado HTTP NOT_FOUND.
     */
    @GetMapping(path = "/status/all")
    public ResponseEntity<List<Status>> statusList(){
        try {
            List<Status> status =  statusService.getStatusList();
            return new ResponseEntity<>(status, httpStatus);
        }catch (Exception exc){
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de obtener el estado de un juego existente a través de una solicitud HTTP GET.
     * La ruta de acceso es "/status/get/{id}", donde {id} es el ID del estado a obtener.
     * @param id El ID del estado a obtener.
     * @return Un objeto ResponseEntity que contiene el estado del juego y el estado HTTP correspondiente.
     *         Si el estado no existe, se devuelve un objeto ResponseEntity con un estado HTTP NOT_FOUND.
     */
    @GetMapping(path = "/status/get/{id}")
    public ResponseEntity<Status> getOneStatus (@PathVariable("id") Long id){
        try {
            if (!statusService.existsByIdStatus (id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Status status = statusService.findStatus(id).get();
            return new ResponseEntity<>(status, httpStatus);
        }catch (Exception exc){
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de crear un nuevo estado de juego a través de una solicitud HTTP POST.
     * La ruta de acceso es "/status/save".
     * @param status El objeto Status que representa el nuevo estado de juego a guardar.
     * @return Un objeto ResponseEntity que contiene el nuevo estado de juego y el estado HTTP correspondiente.
     *         Si ocurre algún error, se devuelve un objeto ResponseEntity con un estado HTTP NOT_FOUND.
     */
    @PostMapping(path = "/status/save")
    public ResponseEntity<Status> insertStatus (@RequestBody Status status){
        try{
            log.info("Juego nuevo: {}", status);
            statusService.postStatus (status);
            return new ResponseEntity<>(status, HttpStatus.CREATED);
        }catch (Exception exc){
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * Actualiza un objeto Status existente mediante una solicitud HTTP PUT.
     * La ruta de acceso es "/status/update/{id}", donde {id} es el ID del objeto Status a actualizar.
     * @param status El objeto Status que representa el estado del juego actualizado.
     * @param id El ID del objeto Status a actualizar.
     * @return Un objeto ResponseEntity que contiene el objeto Status actualizado y el estado HTTP correspondiente.
     */
    @PutMapping(path = "/status/update/{id}")
    public ResponseEntity<Status> updateStatus (@RequestBody Status status, @PathVariable("id") Long id){
        try {
            statusService.putStatus (id, status);
            return new ResponseEntity<>(status, httpStatus);
        }catch (Exception exc) {
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Función encargada de eliminar un objeto Status existente mediante una solicitud HTTP DELETE.
     * La ruta de acceso es "/status/delete/{id}", donde {id} es el ID del objeto Status a eliminar.
     * @param status El objeto Status a eliminar.
     * @return Un objeto HttpEntity que contiene el objeto Status eliminado y el estado HTTP correspondiente.
     *         Si ocurre algún error, se devuelve un objeto HttpEntity con un estado HTTP NOT_FOUND.
     */
    @DeleteMapping(path = "/status/delete/{id}")
    public HttpEntity<Status> deleteOneStatus (Status status){
        try {
            statusService.deleteStatus (status);
            return new ResponseEntity<>(status, httpStatus);
        }catch (Exception exc) {
            System.out.println(exc);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

