package projetaobcc20172.com.projetopetemfocofornecedor.helper;

import android.util.Base64;

//Classe para codificação e decodificação de texto
public class Base64Custom {

    public static String codificarBase64(String texto){ //Método para codificar texto na Base64
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    public static String decodificarBase64(String textoCodificado){ //Método para decodificar texto da Base64
        return new String( Base64.decode(textoCodificado, Base64.DEFAULT ) );
    }

}
