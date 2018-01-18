package projetaobcc20172.com.projetopetemfocofornecedor.cadastroenderecotest;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.CadastroEnderecoActivity;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.LoginActivity;

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
        TestToolsCadEndereco.clicarCadastrar();
        TestTools.verificarMudancaActivity(LoginActivity.class.getName());
        //TestTools.checarToast(R.string.sucesso_cadastro_Fornecedor);
        //TestTools.clicarBotao(R.id.btnSair);
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

    @Test
    public void testeEnderecoCamposEmBranco(){
        TestToolsCadEndereco.clicarCadastrar();
        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
    }

    @Test
    public void testeEnderecoCampoObgLogradouro(){
        TestToolsCadEndereco.preencher("", sNumero, sComplemento, sBairro,
                sCidade, sUf, sCep);
        TestToolsCadEndereco.clicarCadastrar();
        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
    }

    @Test
    public void testeEnderecoCampoObgBairro(){
        TestToolsCadEndereco.preencher(sLogradouro, sNumero, sComplemento,"",
                sCidade, sUf, sCep);
        TestToolsCadEndereco.clicarCadastrar();
        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
    }

    @Test
    public void testeEnderecoCampoObgCidade(){
        TestToolsCadEndereco.preencher(sLogradouro, sNumero, sComplemento, sBairro,
                "", sUf, sCep);
        TestToolsCadEndereco.clicarCadastrar();
        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
    }


    @Test
    public void testeEnderecoCampoObgCep(){
        TestToolsCadEndereco.preencher(sLogradouro, sNumero, sComplemento, sBairro,
                sCidade, sUf,"");
        TestToolsCadEndereco.clicarCadastrar();
        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
    }
}
