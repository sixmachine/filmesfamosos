package br.com.arthursena.filmesfamosos.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * Formata a data para o padrão dd/MM/yyyy
     * @param data
     * @return
     */
    public static String formatarData(Date data){
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        return sf.format(data);
    }

    /**
     * Formata a data para o padrão yyyy-MM-dd
     * @param data
     * @return
     */
    public static String formatarDataSqlite(Date data){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(data);
    }

    public static Date formatarDataString(String data) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sf.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
