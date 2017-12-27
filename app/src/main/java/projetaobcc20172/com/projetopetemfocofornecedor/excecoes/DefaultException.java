package projetaobcc20172.com.projetopetemfocofornecedor.excecoes;

/**
 * Created by Felipe Oliveira on 06/12/17.
 * <flpdias14@gmail.com>
 */

public class DefaultException extends Exception {
    protected String mMensagem;

    public DefaultException() {
        super("Erro");
        this.mMensagem = "Erro";
    }

    public DefaultException(String mensagem) {
        super(mensagem);
        this.mMensagem = mensagem;
    }

    @Override
    public String getMessage() {
        return mMensagem;
    }
}
