package projetaobcc20172.com.projetopetemfocofornecedor.cadastroservicotest;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;

import projetaobcc20172.com.projetopetemfocofornecedor.activity.CadastroServicoActivity;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.ServicosActivity;

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
        TestToolsCadServicos.preencherEclicarCadastrar(this.mNome,"", this.mDescricao);
        TestTools.checarToast(cadastroServicoActivityActivityTestRule.getActivity().getString(R.string.preencha_campo_valor));
    }

    @Test
    public void preencherSemNome(){
        TestToolsCadServicos.preencherEclicarCadastrar("Selecionar", this.mValor, this.mDescricao);
        TestTools.checarToast(cadastroServicoActivityActivityTestRule.getActivity().getString(R.string.error_selecione_um_servico));
    }


    @After
    public void tearDown() throws Exception {
        Thread.sleep(5000);
    }
}
