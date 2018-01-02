package projetaobcc20172.com.projetopetemfocofornecedor.cadastroservicotest;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.LoginActivity;
import projetaobcc20172.com.projetopetemfocofornecedor.logintests.LoginActivityTest;

/**
 * Created by raul1 on 02/01/2018.
 * Realiza os teste de cadastrar servico com todos os serviços disponíveis
 */
@RunWith(Parameterized.class)
public class CadastroServicoActivityTestParam {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Banho","60,00","Deixa seu pet cheirso"},
                {"Tosa","70,00","Deixa seu pet bonito"},
                {"Hospedagem","60,00","Hospeda seu pet"},
                {"Passeio","60,00","Deixa seu pet em forma"},
                {"Vacinação","30,00","Raiva"},
        });
    }
    @Parameterized.Parameter(0)
    public String mNomeServico;
    @Parameterized.Parameter(1)
    public String mValor;
    @Parameterized.Parameter(2)
    public String mDescricao;


    @Before
    public void setUp() throws Exception {
        try{
            TestTools.clicarBotao(R.id.btnSair);
        }catch (Exception e){
            e.getMessage();
        }

        LoginActivityTest log = new LoginActivityTest();
        log.testeUsuarioCadastrado();
        TestToolsCadServico.clicarMeusServicos();
        TestToolsCadServico.clicarAddServico();
        Thread.sleep(1500);

    }

    @Test
    public void testeCadServico(){
        TestToolsCadServico.preencherEClicar(this.mNomeServico,this.mValor,this.mDescricao);
        TestTools.checarToast(R.string.sucesso_cadastro_servico);
    }
}
