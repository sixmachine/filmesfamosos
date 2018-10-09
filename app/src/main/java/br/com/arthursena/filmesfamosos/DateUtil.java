package br.com.arthursena.filmesfamosos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String formatarData(Date data){
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        return sf.format(data);
    }

}
