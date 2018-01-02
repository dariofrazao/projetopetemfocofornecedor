package projetaobcc20172.com.projetopetemfocofornecedor.cadastroservicotest;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.LoginActivity;
import projetaobcc20172.com.projetopetemfocofornecedor.logintests.LoginActivityTest;


/**
 * Created by raul on 10/12/17.
 * Esse teste eh um pouco diferente dos outro,pois, neste ele primeiro realiza um login para depois
 * realizar o teste de cadastrar servico. Foi feito assim para simular uma interação mais próximo a ação
 * do usuário além de evitar erros causados por executar activity sem um usuário associado
 */

public class CadastrarServicoActivityTest {
    private String mNomeServico = "Banho";
    private String mDescricao = "Deixa seu pet cheiroso";
    private String mValor = "60,00";

    @Rule
    public ActivityTestRule <LoginActivity> loginActivityRule = new ActivityTestRule<>(LoginActivity.class);

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
        Thread.sleep(3000);

    }

    @Test
    public void testeCamposEmBranco(){
        Espresso.closeSoftKeyboard();//Fecha o teclado.Por alguma razão nesta tela ele sempre começa aberto
        TestTools.clicarBotao(R.id.btnSalvarServico);
        TestTools.checarToast(R.string.preencha_campo_valor);
    }
    /*
    @Test
    public void testeCampoObgAusenteNome(){
        TestToolsCadServico.clicarMeusServicos();
        TestToolsCadServico.clicarAddServico();
        TestToolsCadServico.preencherEClicar("",this.mValor,this.mDescricao);
        TestTools.checarToast(R.string.preencha_campo_nome);
    }*/

    @Test
    public void testeCamposObgAusenteValor(){
        TestToolsCadServico.preencherEClicar(this.mNomeServico,"",this.mDescricao);
        TestTools.checarToast(R.string.preencha_campo_valor);
    }


    @Test
    public void testeCAusenteDescricao(){
        TestToolsCadServico.preencherEClicar(this.mNomeServico,this.mValor,"");
        TestTools.checarToast(R.string.sucesso_cadastro_servico);
    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(4000);
    }

}
