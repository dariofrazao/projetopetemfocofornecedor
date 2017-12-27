package projetaobcc20172.com.projetopetemfocofornecedor.excecoes;

/**
 * Created by Felipe Oliveira on 06/12/17.
 * <flpdias14@gmail.com>
 */

public class ValidacaoException extends DefaultException {
    public ValidacaoException(){
        super("Erro ao validar os campos!");
        this.mMensagem = "Erro ao validar os campos!";
    }

    public ValidacaoException(String mensagem){
        super(mensagem);
        this.mMensagem = mensagem;
    }

}
