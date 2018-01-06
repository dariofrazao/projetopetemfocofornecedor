package projetaobcc20172.com.projetopetemfocofornecedor.cadastroservicotest;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;



public class TestToolsCadServicos {

    protected static void preencherEclicarCadastrar(String nome, String valor,String descricao) {
        TestTools.selecionarItemSpinnerComScroll(R.id.spinner_nome_servico, nome);
        TestTools.digitarCampo(R.id.etCadastroValorServico, valor);
        TestTools.digitarCampoComScroll(R.id.etCadastroDescricaoServico, descricao);

        TestTools.clicarBotaoComScroll(R.id.btnCadastrarServico);
    }

    protected  static void clicarEditar(){
        TestTools.clicarBotaoComScroll(R.id.btnEditarServico);
    }

    protected  static void preencherEClicarSalvar(String nome, String valor, String descricao){
        TestTools.selecionarItemSpinnerComScroll(R.id.spinner_nome_servico, nome);
        TestTools.digitarCampo(R.id.etCadastroValorServico, valor);
        TestTools.digitarCampoComScroll(R.id.etCadastroDescricaoServico, descricao);
        TestTools.clicarBotaoComScroll(R.id.btnSalvarServico);

    }
}
