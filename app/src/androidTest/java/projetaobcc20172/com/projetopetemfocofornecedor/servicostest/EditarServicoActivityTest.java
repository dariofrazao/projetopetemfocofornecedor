package projetaobcc20172.com.projetopetemfocofornecedor.servicostest;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.LoginActivity;
import projetaobcc20172.com.projetopetemfocofornecedor.logintests.LoginActivityTest;

/**
 * Created by Felipe Oliveira on 05/01/18.
 * <flpdias14@gmail.com>
 */

public class EditarServicoActivityTest {

    private String [] mValores = {"60,00","50,00","30,00","40,00"};
    private String [] mDescricao = {"1","2","3","4"};

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
        Thread.sleep(1500);
        TestToolsServicos.clicarEditar(0);
    }

    @Test
    public void removerTodosInfos(){
        TestToolsServicos.preencherEditar("Selecionar","","","Selecionar");
       TestTools.checarToast(TestTools.activityAtual()
                .getString(R.string.error_selecione_um_servico));

    }

    @Test
    public void removerNome(){
        TestToolsServicos.preencherEditar("Selecionar","60","oi","Cachorro");
        TestTools.checarToast(TestTools.activityAtual()
                .getString(R.string.error_selecione_um_servico));

    }

    @Test
    public void editarServicoTest(){
        String [] mPets = TestTools.activityAtual().getResources().getStringArray(R.array.tiposAnimais);
        String [] mServicos = TestTools.activityAtual().getResources().getStringArray(R.array.servicos);
        int indPet = (int) (Math.random() * 1 + 1);
        int indServ = (int) (Math.random() * 3 + 1);
        TestToolsServicos.preencherEditar(mServicos[indServ],this.mValores[indServ],this.mDescricao[indServ],mPets[indPet]);
        TestToolsServicos.clicarEditar(0);
        TestToolsServicos.verificarServicoEhIgual(mServicos[indServ],"R$"+this.mValores[indServ],this.mDescricao[indServ],mPets[indPet]);
    }
}