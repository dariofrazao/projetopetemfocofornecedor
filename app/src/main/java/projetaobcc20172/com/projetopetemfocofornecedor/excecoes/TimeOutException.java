package projetaobcc20172.com.projetopetemfocofornecedor.excecoes;

/**
 * Created by Alexsandro on 25/02/2018.
 */

public class TimeOutException extends DefaultException {

    public TimeOutException(String mensagem){
        super(mensagem);
        this.mMensagem = mensagem;
    }
}
