package com.app.audireandroid;

/**
 * Created by Edercmf on 13/09/2017.
 */

public class Datos {

    public static String url = "";
    public static String file = "";
    public static String fileName = "";

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
