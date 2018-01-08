package projetaobcc20172.com.projetopetemfocofornecedor.servicostest;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.LoginActivity;
import projetaobcc20172.com.projetopetemfocofornecedor.logintests.LoginActivityTest;

import static junit.framework.Assert.assertEquals;

/**
 * Created by raul1 on 07/01/2018.
 */

public class RemoverServicoActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        try{
            TestTools.clicarBotao(R.id.btnSair);
        }catch (Exception e){
            e.getMessage();
        }
        LoginActivityTest log = new LoginActivityTest();
        log.testeUsuarioCadastrado();
        TestTools.clicarBotao(R.id.btnMeusServicos);
        TestTools.clicarBotao(R.id.btnCadastrarServico);
        Thread.sleep(1500);
        //Cadastra um servico novo
        CadastrarServicosActivityTest cad = new CadastrarServicosActivityTest();
        cad.testeCadServico();
    }
    //Exclui um item e depois verifica se o tamanho da lista diminui em 1
    @Test
    public void removerItem() throws InterruptedException {
        int tamanhoList = TestToolsServicos.getTamanhoList();
        TestToolsServicos.clicarExcluir(0);
        assertEquals(TestToolsServicos.getTamanhoList(),tamanhoList-1);
    }
}
