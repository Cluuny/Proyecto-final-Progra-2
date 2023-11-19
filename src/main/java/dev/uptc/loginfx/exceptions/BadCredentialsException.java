package dev.uptc.loginfx.exceptions;

public class BadCredentialsException extends  Exception{
    public BadCredentialsException(){
        super("Las credenciales son incorrectas, pruebe de nuevo");
    }
}
