package utez.edu.mx.smartfitutez.security.dto;

import java.util.Date;

public class Mensaje {

    private String mensaje;
    private Date timestamp;

    public Mensaje(String mensaje) {
        this.mensaje = mensaje;
        this.timestamp = new Date();
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
