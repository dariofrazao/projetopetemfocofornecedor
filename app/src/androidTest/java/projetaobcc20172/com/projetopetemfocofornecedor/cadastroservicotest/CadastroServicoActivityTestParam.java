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
import projetaobcc20172.com.projetopetemfocofornecedor.activity.ServicosActivity;
import projetaobcc20172.com.projetopetemfocofornecedor.logintests.LoginActivityTest;

/**
 * Created by raul1 on 02/01/2018.
 * Realiza os teste de cadastrar servico com todos os serviços disponíveis
 */
@RunWith(Parameterized.class)
public class CadastroServicoActivityTestParam {

    @Parameterized.Parameter(0)
    public String mNomeServico;
    @Parameterized.Parameter(1)
    public String mValor;
    @Parameterized.Parameter(2)
    public String mDescricao;
    @Parameterized.Parameter(3)
    public String mTipoAnimal;

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Banho","60,00","Deixa seu pet cheirso","Cachorro"},
                {"Tosa","70,00","Deixa seu pet bonito","Cachorro"},
                {"Hospedagem","60,00","Hospeda seu pet","Cachorro"},
                {"Passeio","60,00","Deixa seu pet em forma","Cachorro"},
                {"Vacinação","30,00","Raiva","Cachorro"},
                {"Banho","60,00","Deixa seu pet cheirso","Gato"},
                {"Tosa","70,00","Deixa seu pet bonito","Gato"},
                {"Hospedagem","60,00","Hospeda seu pet","Gato"},
                {"Passeio","60,00","Deixa seu pet em forma","Gato"},
                {"Vacinação","30,00","Raiva","Gato"}
        });
    }

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
        TestToolsCadServico.preencherEClicar(this.mNomeServico,this.mValor,this.mDescricao,this.mTipoAnimal);
        TestTools.checarToast(R.string.sucesso_cadastro_servico);
        TestTools.qtNaoNula(((ServicosActivity)TestTools.activityAtual()).listView.getCount());
    }
}
