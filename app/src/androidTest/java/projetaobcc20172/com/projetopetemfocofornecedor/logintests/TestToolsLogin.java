package projetaobcc20172.com.projetopetemfocofornecedor.logintests;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;

/**
 * Created by raul on 08/11/17.
 * Classe que armazena os métodos que interagem com a tela de login para realizar os teste.
 */
public class TestToolsLogin {

    protected static void preencherEclicar(String login, String senha){
        TestTools.digitarCampo(R.id.etLoginEmail,login);
        TestTools.digitarCampo(R.id.etLoginSenha,senha);
        TestTools.clicarBotao(R.id.botao_login);
    }

}