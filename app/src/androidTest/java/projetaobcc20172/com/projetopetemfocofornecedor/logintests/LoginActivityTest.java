package projetaobcc20172.com.projetopetemfocofornecedor.logintests;


import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.LoginActivity;


/**
 * Created by raul on 10/11/17.
 * Classe que executa os testes automatizados para login
 */


public class LoginActivityTest {

    private static String sLoginInvalido = "joaoNaoCadastrado@gmail.com";
    private static String sSenhaInvalida = "senhaInvalida";
    private static String sLoginValido = "raulpedrouag@gmail.com";
    private static String sSenhaValida = "123456";

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<>(LoginActivity.class);


    @Before
    public void setUp() throws Exception {
        //Desloga caso já esteja logado.
        //Evita erros nos testes
        try{
            TestTools.clicarBotao(R.id.botao_sair);
        }catch (Exception e){
            e.getMessage();
        }
    }

    //Teste que simula uma tentatica de login com um cadastro não existente
    @Test
    public void testeLoginNaoCadastrado() {
        TestToolsLogin.preencherEclicar(sLoginInvalido,sSenhaInvalida);
        TestTools.checarToast(R.string.erro_login_invalido_Toast);
    }

    //Teste que simula um login com todos os campos de texto em branco
    @Test
    public void testeCamposEmBranco(){
        TestToolsLogin.preencherEclicar("","");
        TestTools.checarToast(R.string.erro_login_invalido_Toast);
    }
    //Teste que simula um login com o campo de senha em branco
    @Test
    public void testeSenhaEmBranco(){
        TestToolsLogin.preencherEclicar(sLoginValido,"");
        TestTools.checarToast(R.string.erro_login_invalido_Toast);
    }

    @Test
    public void testeSenhaInvalida(){
        TestToolsLogin.preencherEclicar(sLoginValido,sSenhaInvalida);
        TestTools.checarToast(R.string.erro_login_invalido_Toast);
    }

    @Test
    public void testeUsuarioCadastrado() throws InterruptedException {
      //  Intents.init();//Para realizar teste de mudança dela deve-se sempre add esse método no começo do método
        TestToolsLogin.preencherEclicar(sLoginValido, sSenhaValida);
        TestTools.checarToast(R.string.sucesso_login_Toast);
       // Thread.sleep(3000);
      //  TestTools.verificarMudancaActivity(MainActivity.class.getName());
    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(5000);
    }
}