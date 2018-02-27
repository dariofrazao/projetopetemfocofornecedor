package projetaobcc20172.com.projetopetemfocofornecedor.editarfornecedortest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.LoginActivity;

/**
 * Created by renat on 19/01/2018.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class EditFornecedorActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> editFornecedorActivityRule = new ActivityTestRule<>(LoginActivity.class);

    //Essas variaveis guardam valores que são preenchidos nos campos
    private String mTelefone = "87992756888";
    private String mHorarios = "Tarde";
    private String mCampovazio = "";
    private String mEmail = "eeee@gmail.com";
    private String mSenha = "123456";

    @Before
    public void setUp() throws Exception {
        //Desloga caso já esteja logado.
        //Evita erros nos testes
        try{
            //TestTools.clicarBotao(R.id.btnSair);
        }catch (Exception e){
            e.getMessage();
        }
    }


    @Test //testa editar fornecedor com todos os campo preenchidos
    public void editarFornecedorCamposPreenchidos(){
        TestTools.login(mEmail, mSenha);
        TestTools.clicarItemMenuEditar();
        TestToolsEditFornecedor.preencherEclicar(this.mTelefone, this.mHorarios);
        TestTools.checarToast(R.string.sucesso_atualizacao);
    }

    @Test //testa editar fornecedor com algum campo vazio
    public void editarFornecedorCamposNaoPreenchidos(){
        TestTools.login(mEmail, mSenha);
        TestTools.clicarItemMenuEditar();
        TestToolsEditFornecedor.preencherEclicar(this.mCampovazio, this.mHorarios);
        TestTools.checarToast(R.string.erro_editar_fornecedor_campos_obrigatorios_Toast);
    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(5000);
    }
}