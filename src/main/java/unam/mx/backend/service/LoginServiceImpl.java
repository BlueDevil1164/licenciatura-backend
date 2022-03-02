package unam.mx.backend.service;

import unam.mx.backend.exceptions.ControllerException;
import unam.mx.backend.exceptions.CustomException;
import unam.mx.backend.model.Usuario;
import unam.mx.backend.model.CredencialesRequest;
import unam.mx.backend.model.Login;
import unam.mx.backend.utils.DigestEncoder;
import unam.mx.backend.utils.JWTUtil;
import unam.mx.backend.utils.EnumMessage;

import org.springframework.stereotype.Service;

import unam.mx.backend.mapper.UsuarioMapper;

@Service
public class LoginServiceImpl implements LoginService{

    private UsuarioMapper usuarioMapper;

    public LoginServiceImpl(UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Login login(CredencialesRequest usuario) throws ControllerException {

        int maximoNumeroIntentosConcedidos = 5; // 5 intentos
        long delta = 1000*60*5L; // 5 minutos
        long instanteActual = System.currentTimeMillis();
        //Usuario no existente
        if(usuario.getMail()==null) throw new CustomException(EnumMessage.BAD_CREDENTIALS);

        Usuario validUser = usuarioMapper.getbyMail(usuario.getMail());

        if(validUser==null) throw new CustomException(EnumMessage.BAD_CREDENTIALS);

        if(!validUser.isActivo()) throw new CustomException(EnumMessage.DISABLED_USER);

        //Usuario bloqueado
        long instanteDeBloqueo = validUser.getInstanteBloqueo();
        long diff = instanteActual - instanteDeBloqueo;
        long restante = delta - diff;
        if(instanteDeBloqueo>0 && restante>0) {
            long totalSegundos = restante/1000;
            long totalMinutos = totalSegundos/60;
            throw new CustomException(EnumMessage.WAIT_LOGIN, totalMinutos, totalSegundos%60);
        }
        
        //Clave incorrecta
        String passwordHasheado = DigestEncoder.digest(usuario.getClave(), usuario.getMail());
        if(!passwordHasheado.equals(validUser.getClave())){
            int numeroDeIntentosFallidos = validUser.getAccesoNegadoContador()+1;
            validUser.setAccesoNegadoContador(numeroDeIntentosFallidos);
            usuarioMapper.update(validUser);

            if(numeroDeIntentosFallidos >= maximoNumeroIntentosConcedidos) {
                validUser.setInstanteBloqueo(instanteActual); 
                throw new CustomException(EnumMessage.MAX_FAILED_LOGIN_EXCEPTION, maximoNumeroIntentosConcedidos);
            }

            throw new CustomException(EnumMessage.BAD_CREDENTIALS, numeroDeIntentosFallidos, maximoNumeroIntentosConcedidos);

        }

        // Resetea todoas las banderas de advertencia y bloqueo. Luego, actualiza y retorna el usuario:
        validUser.setAccesoNegadoContador(0);
        validUser.setInstanteBloqueo(0);
        validUser.setInstanteUltimoAcceso(instanteActual);
        usuarioMapper.update(validUser);

        Login loginResponse = new Login();
        String encryptKey = "secreto";
        loginResponse.setMail(validUser.getMail());
        String jwt = JWTUtil.getInstance().createToken(usuario.getMail(), 27, encryptKey);
        loginResponse.setJwt(jwt);
        return loginResponse;
    }
    
}