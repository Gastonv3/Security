package com.v3.security.Util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Skylake on 25/3/2018.
 */

public class Preferencias {
    private static final String KEY_GUARDIA = "idGuardia";
    private static final String KEY_USER = "user";
    private static final String KEY_PASS = "pass";
    private static final String KEY_RECUERDAME = "recuerdame";

    public static String getKeyGuardiaNombre() {
        return KEY_GUARDIA_NOMBRE;
    }

    private static final String KEY_GUARDIA_NOMBRE = "nombre";

    public static String getKeyGuardiaApellido() {
        return KEY_GUARDIA_APELLIDO;
    }

    private static final String KEY_GUARDIA_APELLIDO = "apellido";

    public static String getKeyRecuerdame() {
        return KEY_RECUERDAME;
    }

    public static String getKeyUser() {
        return KEY_USER;
    }

    public static String getKeyPass() {
        return KEY_PASS;
    }

    public static String getKeyGuardia() {
        return KEY_GUARDIA;
    }

    public static int getInteger(Context context, final String key) {
        android.content.SharedPreferences shaPref = PreferenceManager.getDefaultSharedPreferences(context);
        return shaPref.getInt(key, 0);
    }

    public static void setInteger(Context context, final String key, final int i) {
        android.content.SharedPreferences shaPref = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = shaPref.edit();
        editor.putInt(key, i);
        editor.commit();
    }
    public static String getString(Context context, final String key) {
        android.content.SharedPreferences shaPref = PreferenceManager.getDefaultSharedPreferences(context);
        return shaPref.getString(key, "");
    }

    public static void setString(Context context, final String key, final String value) {
        android.content.SharedPreferences shaPref = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = shaPref.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static boolean getBoolean(Context context, final String key, final boolean defaultValue) {

        android.content.SharedPreferences shaPref = PreferenceManager.getDefaultSharedPreferences(context);
        return shaPref.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context context, final String key, final boolean value) {

        android.content.SharedPreferences shaPref = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = shaPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
