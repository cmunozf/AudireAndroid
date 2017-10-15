package com.app.audireandroid;

/**
 * Created by Edercmf on 13/09/2017.
 */

public class Datos {

    public static String url = "https://fathomless-woodland-99127.herokuapp.com/music/upload";
    public static String file = "";
    public static String fileName = "";

    public static String linkAudio = "";

    private static String correo = "";
    private static String token = "";

    public static String getCorreo() {
        return correo;
    }

    public static void setCorreo(String correo) {
        Datos.correo = correo;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Datos.token = token;
    }
}
