package projetaobcc20172.com.projetopetemfocofornecedor.cadastroenderecotest;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.CadastroEnderecoActivity;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.LoginActivity;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;

/**
 * Created by raul on 09/12/17.
 */

public class EnderecoActivityTest {

    private static String sLogradouro = "setor norte";
    private static String sNumero = "42";
    private static String sComplemento = "prox. palacio de naboo";
    private static String sBairro = "gunga";
    private static String sCidade = "Naboo";
    private static String sUf = "PE";
    private static String sCep = "55299-525";
    private static int sBotaoCadEnd = R.id.botao_finalizar_cadastro_endereco;

    @Rule
    public ActivityTestRule<CadastroEnderecoActivity> cadEndActivityRule = new ActivityTestRule<>(CadastroEnderecoActivity.class);

    @Before
    public void setUp() throws Exception {
        Thread.sleep(3000);
    }

    private void clicarEVerificarCorreto(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TestTools.clicarBotaoComScroll(sBotaoCadEnd);
        TestTools.checarToast(R.string.sucesso_cadastro_Fornecedor);
        TestTools.verificarMudancaActivity(LoginActivity.class.getName());
        //TestTools.clicarBotao(R.id.botao_sair);
    }

    public void testeEnderecoCorreto(){
        Intents.init();
        TestToolsCadEndereco.preencher(sLogradouro, sNumero, sComplemento, sBairro,
                sCidade, sUf, sCep);
        this.clicarEVerificarCorreto();
    }

    public void testeEnderecoCorretoSemNumero(){
        Intents.init();
        TestToolsCadEndereco.preencher(sLogradouro,"", sComplemento, sBairro,
                sCidade, sUf, sCep);
        this.clicarEVerificarCorreto();
    }

    public void testeEnderecoCorretoSemCompl(){
        Intents.init();
        TestToolsCadEndereco.preencher(sLogradouro, sNumero,"", sBairro,
                sCidade, sUf, sCep);
        this.clicarEVerificarCorreto();
    }

    public void testeEnderecoCorretoSemComplENumero(){
        Intents.init();
        TestToolsCadEndereco.preencher(sLogradouro, "","", sBairro,
                sCidade, sUf, sCep);
        this.clicarEVerificarCorreto();
    }

   // @Test
    public void testeEnderecoCamposEmBranco(){
        TestTools.clicarBotaoComScroll(sBotaoCadEnd);
        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
    }

    //@Test
    public void testeEnderecoCampoObgLogradouro(){
        TestToolsCadEndereco.preencher("", sNumero, sComplemento, sBairro,
                sCidade, sUf, sCep);
        TestTools.clicarBotaoComScroll(sBotaoCadEnd);
        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
    }

    //@Test
    public void testeEnderecoCampoObgBairro(){
        TestToolsCadEndereco.preencher(sLogradouro, sNumero, sComplemento,"",
                sCidade, sUf, sCep);
        TestTools.clicarBotaoComScroll(sBotaoCadEnd);
        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
    }

    //@Test
    public void testeEnderecoCampoObgCidade(){
        TestToolsCadEndereco.preencher(sLogradouro, sNumero, sComplemento, sBairro,
                "", sUf, sCep);
        TestTools.clicarBotaoComScroll(sBotaoCadEnd);
        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
    }


    //@Test
    public void testeEnderecoCampoObgCep(){
        TestToolsCadEndereco.preencher(sLogradouro, sNumero, sComplemento, sBairro,
                sCidade, sUf,"");
        TestTools.clicarBotao(sBotaoCadEnd);
        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
    }
}
