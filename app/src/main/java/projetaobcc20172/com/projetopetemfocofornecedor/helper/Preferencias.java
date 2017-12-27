package projetaobcc20172.com.projetopetemfocofornecedor.helper;

import android.content.Context;
import android.content.SharedPreferences;


//Classe para armazenar dados do usuário (preferências) ao iniciar novamente o aplicativo
public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "petemfoco.preferencias"; //Nome do arquivo onde serão salvas as prefer
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado"; //Variável para identificar usuário logado
    private final String CHAVE_NOME = "nomeUsuarioLogado"; //Nome do usuário logado

    public Preferencias( Context contextoParametro){

        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE );
        editor = preferences.edit(); //Salvar a preferência

    }

    public void salvarDados(String identificadorUsuario) {

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario); //Colocar nas preferências a chave identificadora do usuário
        //editor.putString(CHAVE_NOME, nomeUsuario); //Colocar nas preferências a chave com o nome do usuário
        editor.commit(); //Salvar as alterações nas preferências

    }

    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome(){
        return preferences.getString(CHAVE_NOME, null);
    }

}
