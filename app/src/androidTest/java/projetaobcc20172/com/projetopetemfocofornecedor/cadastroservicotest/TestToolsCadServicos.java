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

    public static void preencherEClicar(String nomeServico, String valor, String descricao,String tipoAnimal){
        TestTools.selecionarItemSpinnerComScroll(R.id.tipoAnimalSpinner,tipoAnimal);
        TestToolsCadServicos.preencherEclicarCadastrar(nomeServico,valor,descricao);
    }

    protected  static void clicarEditar(){
        TestTools.clicarBotaoComScroll(R.id.btnEditarServico);
    }

    public static  void clicarMeusServicos(){
        TestTools.clicarBotao(R.id.btnMeusServicos);
    }

    public static void clicarAddServico(){
        TestTools.clicarBotao(R.id.btnCadastrarServico);
    }
}
