package projetaobcc20172.com.projetopetemfocofornecedor.servicostest;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.CadastroServicoActivity;

/**
 * Created by raul on 12/12/17.
 */

public class CadastrarServicosActivityTest {


    //Essas variaveis guardam valores que são preenchidos nos campos
    //Existentes na tela de cadastro
    private String mNome = "Tosa";
    private String mValor = "105";
    private String mDescricao = "Teste";

    @Rule
    public ActivityTestRule<CadastroServicoActivity> cadastroServicoActivityActivityTestRule = new ActivityTestRule<>(CadastroServicoActivity.class);

    @Test
    public void preencherSemValor(){
        TestToolsServicos.preencherEclicarCadastrar(this.mNome,"", this.mDescricao);
        TestTools.checarToast(cadastroServicoActivityActivityTestRule.getActivity().getString(R.string.preencha_campo_valor));
    }

    @Test
    public void preencherSemNome(){
        TestToolsServicos.preencherEclicarCadastrar("Selecionar", this.mValor, this.mDescricao);
        TestTools.checarToast(cadastroServicoActivityActivityTestRule.getActivity().getString(R.string.error_selecione_um_servico));
    }

    @Test
    public void preencherSemPet(){
        TestToolsServicos.preencherEClicar(this.mNome, this.mValor, this.mDescricao,"Selecionar");
        TestTools.checarToast(cadastroServicoActivityActivityTestRule.getActivity().getString(R.string.preencha_campo_tipoAnimal));
    }


    public void testeCadServico() throws InterruptedException {
        TestToolsServicos.preencherEClicar("Banho","60","ok","Gato");
    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(5000);
    }
}
