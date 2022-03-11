/*
 *
 * Paquete:     unam.mx.backend.model
 * Proyecto:    licenciatura-backend
 * Tipo:        Class
 * Nombre:      AccessController
 * Autor:       Luis Martinez 
 * Versión:     1.0-SNAPSHOT
 *
 * Historia:
 *              Creación: 02 Mar 2022
 */
package mx.unam.backend.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import mx.unam.backend.exceptions.ControllerException;
import mx.unam.backend.model.CredencialesRequest;
import mx.unam.backend.model.Login;
import mx.unam.backend.model.RecuperacionTokenRequest;
import mx.unam.backend.model.Usuario;
import mx.unam.backend.service.UsuarioService;

/**
 * Implementacion  del controlador REST asociado a los endpoints
 * de gestión del AccessController.
 *
 * <p>Todos los métodos de esta clase disparan {@link ControllerException}</p>
 *
 * <p>NOTA IMPORTANTE: Los  distintos métodos de este controlador no
 * llevan  javadoc  debido a que la  documentación  Swagger  API
 * cumple con ese objetivo.</p>
 *
 * @author  mentesniker
 * @see     mx.unam.backend.model.CredencialesRequest
 * @see     mx.unam.backend.service.UsuarioService
 * @version 1.0-SNAPSHOT
 * @since   1.0-SNAPSHOT
 */
@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

     /**
     * Constructor que realiza el setting de los servicios que serán
     * utilizados en este controlador.
     *
     * @param usuarioService Servicios de AccessService
     */
    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @ApiOperation(
            value = "UsuarioController::login",
            notes = "Recibe el usuario con el que queremos iniciar sesion.")
    @PostMapping(
        path = "/login",
        produces = "application/json; charset=utf-8")
    public Login login(@ApiParam(
        name = "usuario",
        value = "Representa a un usuario (mail, password)")
        @RequestBody CredencialesRequest usuario) throws ControllerException{
        return usuarioService.login(usuario); 
    }

    @ApiOperation(
            value = "UsuarioController::recuperarClave",
            notes = "Recibe el mail del usuario del que queremos recuperar su contrasena.")
    @PostMapping(
        path = "/recupera-clave",
        produces = "application/json; charset=utf-8")
    public String recuperarClave(@ApiParam(
        name = "usuario",
        value = "Representa a un usuario (mail, password)")
        @RequestBody CredencialesRequest usuario) throws ControllerException{
        return usuarioService.solicitaRegeneracionClave(usuario.getMail()); 
    }

    @ApiOperation(
            value = "UsuarioController::confirmaRegeneracionClave",
            notes = "Recibe la nueva clave y el token del usuario del que queremos recuperar su contrasena.")
    @PostMapping(
        path = "/confirma-token",
        produces = "application/json; charset=utf-8")
    public Usuario confirmaRegeneraClave(@ApiParam(
        name = "clave nueva y Token",
        value = "Representa a un usuario (mail, password)")
        @RequestBody RecuperacionTokenRequest tokenRequest) throws ControllerException{
        return usuarioService.confirmaRegeneraClave(tokenRequest); 
    }
}