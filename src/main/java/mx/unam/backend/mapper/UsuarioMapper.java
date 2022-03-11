/*
 *
 * Paquete:     unam.mx.backend.mapper
 * Proyecto:    licenciatura-backend
 * Tipo:        Interface
 * Nombre:      UsuarioMapper
 * Autor:       Luis Martinez 
 * Versión:     1.0-SNAPSHOT
 *
 * Historia:
 *              Creación: 02 Mar 2022
 */
package mx.unam.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.exceptions.PersistenceException;

import mx.unam.backend.model.Rol;
import mx.unam.backend.model.Usuario;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import java.util.List;

/**
 * <p>Descripción:</p>
 * Interface 'Mapper' MyBatis asociado a la entidad 'Usuario'.
 *
 * @author  mentesniker, garellano
 * @see     unam.mx.model.Usuario
 * @see     unam.mx.model.Rol
 * @version 1.0-SNAPSHOT
 * @since   1.0-SNAPSHOT
 */

@Mapper
public interface UsuarioMapper {

    /**
     * Busca un objeto de tipo '{@link mx.unam.backend.model.Usuario} ' contenido en la base de datos usando su mail.
     *
     * @param mail el mail del usuario a buscar.
     * @return un objeto de tipo '{@link mx.unam.backend.model.Usuario} '.
     * @throws java.sql.PersistenceException Se dispara en caso de que se dispare un error en esta operación desde la base de datos.
     */
    @Results(id="UsuarioMap", value = {
        @Result(property = "id",   column = "id"),
        @Result(property = "mail",   column = "mail"),
        @Result(property = "clave",   column = "clave"),    
        @Result(property = "creado",   column = "creado"),
        @Result(property = "activo",   column = "activo"),
        @Result(property = "accesoNegadoContador",   column = "accesoNegadoContador"),
        @Result(property = "instanteBloqueo",   column = "instanteBloqueo"),
        @Result(property = "instanteUltimoAcceso",   column = "instanteUltimoAcceso"),
        @Result(property = "instanteUltimoCambioClave",   column = "instanteUltimoCambioClave"),
        @Result(property = "regeneraClaveToken",   column = "regeneraClaveToken"),
        @Result(property = "regeneraClaveInstante",   column = "regeneraClaveInstante"),
    })
    @Select("SELECT * FROM usuario WHERE mail = #{mail};")
    Usuario getByMail(String mail) throws PersistenceException;


    /**
     * Busca un objeto de tipo '{@link mx.unam.backend.model.Usuario} ' contenido en la base de datos usando su token.
     *
     * @param token el token del usuario a buscar.
     * @return un objeto de tipo '{@link mx.unam.backend.model.Usuario} '.
     * @throws java.sql.PersistenceException Se dispara en caso de que se dispare un error en esta operación desde la base de datos.
     */
    @ResultMap("UsuarioMap")
    @Select("SELECT * FROM usuario WHERE regeneraClaveToken = #{token};")
    Usuario getByToken(String token) throws PersistenceException;

    /**
     * Actualiza un objeto de tipo '{@link mx.unam.backend.model.Usuario} ' con base en la infrmación dada por el objeto de tipo 'usuario'.
     *
     * @param usr objeto de tipo '{@link mx.unam.backend.model.Usuario} ' a ser actualizado.
     * @return el numero de registros actualizados.
     * @throws java.sql.PersistenceException Se dispara en caso de que se dispare un error en esta operación desde la base de datos.
     */
    @Update("UPDATE usuario SET mail = #{mail}, clave = #{clave}, creado = #{creado}, activo = #{activo}, "
            + "accesoNegadoContador = #{accesoNegadoContador}, instanteBloqueo = #{instanteBloqueo}, "
            + "instanteUltimoAcceso = #{instanteUltimoAcceso} , instanteUltimoCambioClave = #{instanteUltimoCambioClave}, "
            + "regeneraClaveToken = #{regeneraClaveToken}, regeneraClaveInstante = #{regeneraClaveInstante} "
            + "WHERE id = #{id}; ")
    int update(Usuario usr) throws PersistenceException;

    /**
     * Regresa una lista con todos los roles de un usuario.
     *
     * @param mail el mail del usuario a buscar.
     * @return una lista de objetos de tipo '{@link mx.unam.backend.model.Rol} '.
     * @throws java.sql.PersistenceException Se dispara en caso de que se dispare un error en esta operación desde la base de datos.
     */
    @Results(id="UsuarioRolesMap", value = {
        @Result(property = "id",   column = "id"),
        @Result(property = "nombre",   column = "nombre"),
        @Result(property = "activo",   column = "activo")
    })
    @Select("SELECT id,nombre,activo FROM mailRoles WHERE mail = #{mail};")
    List<Rol> getRoles(String mail) throws PersistenceException;

}