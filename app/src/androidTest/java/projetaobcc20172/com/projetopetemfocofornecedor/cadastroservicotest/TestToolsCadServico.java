package projetaobcc20172.com.projetopetemfocofornecedor.cadastroservicotest;


import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;

/**
 * Created by raul on 10/12/17.
 */

public class TestToolsCadServico {

    public static void preencherEClicar(String nomeServico, String valor, String descricao ){
        TestTools.selecionarItemSpinnerComScroll(R.id.servicoSpinner,nomeServico);
        TestTools.digitarCampo(R.id.etCadastroValorServico,valor);
        TestTools.digitarCampo(R.id.etCadastroDescricaoServico,descricao);
        TestTools.clicarBotao(R.id.btnSalvarServico);
    }

    public static  void clicarMeusServicos(){
        TestTools.clicarBotao(R.id.btnMeusServicos);
    }

    public static void clicarAddServico(){
        TestTools.clicarBotao(R.id.btnCadastrarServico);
    }
}
