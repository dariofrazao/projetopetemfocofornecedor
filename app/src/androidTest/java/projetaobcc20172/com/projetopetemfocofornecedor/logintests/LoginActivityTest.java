package projetaobcc20172.com.projetopetemfocofornecedor.logintests;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.LoginActivity;

/**
 * Created by LuizAlberes on 27/11/17.
 * Classe que executa os testes automatizados para login
 */


public class LoginActivityTest {

    private UiDevice mDevice;

    @Rule
    public ActivityTestRule <LoginActivity> loginActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        //Desloga caso já esteja logado.
        //Evita erros nos testes
        try{
            TestTools.clicarItemNavigationMenu(R.id.drawer_layout, R.id.nav_busca, R.id.nav_sair);
        }catch (Exception e){
            e.getMessage();
        }
    }


    //Teste que simula uma tentativa de login pelo Google confirmando autorização
    @Test
    public void testeLoginComSucesso() {
        TestTools.clicarBotao(R.id.btnLoginGoogle);
        TestTools.checarToast(R.string.sucesso_login_Toast);
    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(3000);
    }


}