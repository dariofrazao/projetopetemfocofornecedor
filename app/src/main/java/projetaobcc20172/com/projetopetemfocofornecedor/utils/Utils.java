package projetaobcc20172.com.projetopetemfocofornecedor.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.widget.Toast;


/**
 * Classe de Rotinas úteis.
 * Criada por Felipe Oliveira em 05/12/17.
 */

public class Utils {

    /**
     * Construtor privado, todos os métodos são estáticos
     */
    private Utils(){}

    /**
     * Checa se o dispositivo tem conexão com a internet.
     * @param activity Activity atual
     * @return {true} se há conectividade
     */

    public static boolean checarConexaoInternet(Activity activity){
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return (connectivityManager != null) &&
                (connectivityManager.getActiveNetworkInfo() != null) &&
                (connectivityManager.getActiveNetworkInfo().isAvailable()) &&
                (connectivityManager.getActiveNetworkInfo().isConnected());

    }

    /**
     * Mostra uma mensagem longa.
     * @param context contexto onde a mensagem será exibida
     * @param mensagem texto a ser exibido
     */
    public static void mostrarMensagemLonga(Context context, String mensagem){
       Toast mToast = Toast.makeText(context, mensagem, Toast.LENGTH_LONG);
        mToast.show();
    }

    /**
     * Mostra uma mensagem curta.
     * @param context contexto onde a mensagem será exibida
     * @param mensagem texto a ser exibido
     */
    public static void mostrarMensagemCurta(Context context, String mensagem){
        Toast mToast = Toast.makeText(context, mensagem, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static String formatarMensagemErro(String acao, String mensagem){
        return String.format("Erro ao %s : %s", acao, mensagem);
    }

    /**
     * Faz uma pergunta para ser respondida com sim ou não e a exibe ao usuário.
     * @param context Contexto onde a mensagem será exibida
     * @param titulo Título da pergunta
     * @param mensagem Mensagem no corpo da pergunta
     * @param onClickListenerPositive ouvinte para a resposta positiva
     * @param onClickListenerNegative ouvunte para a resoista negativa
     */
    public static void mostrarPerguntaSimNao(Context context, String titulo, String mensagem,
                                                      DialogInterface.OnClickListener onClickListenerPositive,
                                                      DialogInterface.OnClickListener onClickListenerNegative) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //Coloca o título e a mensagem.
        builder.setTitle(titulo);
        builder.setMessage(mensagem);

        builder.setPositiveButton("Sim", onClickListenerPositive);
        builder.setNegativeButton("Não", onClickListenerNegative);

        builder.show();
    }

}
