package projetaobcc20172.com.projetopetemfocofornecedor.cupomtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.LoginActivity;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.ServicoDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.logintests.LoginActivityTest;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by raul on 09/12/17.
 */

public class CupomActivityTest {

    private static String sIdFavorito;
    private static String sJuncao = "Hospedagem - Cachorro - R$20,00";
    private static String sNome = "Banho";
    private static String sValor = "R$20,00";
    private static String sTipoPet = "Cachorro";
    private static String sDescricao = "";
    //private static String sIdFornecedor = "104782358621918306459";

    @Rule
    public ActivityTestRule <LoginActivity> loginActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {

        Context context = getInstrumentation().getTargetContext();

        try{

            TestTools.clicarItemNavigationMenu(R.id.drawer_layout, R.id.nav_busca, R.id.nav_sair);

        }catch (Exception e){
            e.getMessage();
        }

        //Recuperar id do usuário logado
        String mIdUsuarioLogado;
        mIdUsuarioLogado = getPreferences("id", context);

        Servico servico = new Servico(sNome, sValor, sTipoPet, sDescricao);
        ServicoDaoImpl servicoDao =  new ServicoDaoImpl(context);

        Thread.sleep(4000);

        LoginActivityTest log = new LoginActivityTest();
        log.testeLoginComSucesso();
        Thread.sleep(4000);
        servicoDao.inserir(servico, mIdUsuarioLogado);
        Thread.sleep(4000);
        TestTools.clicarItemNavigationMenu(R.id.drawer_layout, R.id.nav_busca, R.id.nav_cupons);
        Thread.sleep(4000);
        TestTools.clicarBotao(R.id.btnCadastroCupomList);
        Espresso.closeSoftKeyboard();
    }

//
    @Test
    public void testeCupomCamposEmBranco() throws InterruptedException {
        TestToolsCupom.clicarCadastrarCupom();
        Thread.sleep(2000);
        TestTools.checarToast(R.string.error_selecione_um_servico);
    }

    @Test
    public void testeCupomCampoObgServico() throws InterruptedException {
        TestTools.selecionarItemSpinnerComScroll(R.id.spCadastroServicoCupom, sJuncao);
        Thread.sleep(2000);
        TestToolsCupom.clicarCadastrarCupom();
        Thread.sleep(2000);
        TestTools.checarToast(R.string.preencha_campo_nome);
    }

//    @Test
//    public void testeCupomCampoObgValor(){
//        TestToolsCupom.clicarCadastrarCupom();
//        TestTools.checarToast(R.string.preencha_campo_nome);
//    }
//
//    @Test
//    public void testeEnderecoCampoObgLogradouro() throws InterruptedException {
//        TestToolsEndereco.preencherCadastro("", sNumero, sComplemento, sBairro,
//                sCidade, sUf, sCep);
//        Thread.sleep(2000);
//        TestTools.apagarCampo(R.id.etCadastroLogradouroEndereco);
//        Thread.sleep(2000);
//        TestTools.clicarBotaoComScroll(sBotaoCadEnd);
//        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
//    }
//
//    @Test
//    public void testeEnderecoCampoObgBairro() throws InterruptedException {
//        TestToolsEndereco.preencherCadastro(sLogradouro, sNumero, sComplemento,"",
//                sCidade, sUf, sCep);
//        Thread.sleep(2000);
//        TestTools.apagarCampo(R.id.etCadastroBairroEndereco);
//        Thread.sleep(2000);
//        TestTools.clicarBotaoComScroll(sBotaoCadEnd);
//        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
//    }
//
//    @Test
//    public void testeEnderecoCampoObgCidade() throws InterruptedException {
//        TestToolsEndereco.preencherCadastro(sLogradouro, sNumero, sComplemento, sBairro,
//                "", sUf, sCep);
//        Thread.sleep(2000);
//        TestTools.apagarCampo(R.id.etCadastroLocalidadeEndereco);
//        Thread.sleep(2000);
//        TestTools.clicarBotaoComScroll(sBotaoCadEnd);
//        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
//    }
//
//    @Test
//    public void testeEnderecoCampoObgCep() throws InterruptedException {
//        TestToolsEndereco.preencherCadastro(sLogradouro, sNumero, sComplemento, sBairro,
//                sCidade, sUf,"");
//        Thread.sleep(2000);
//        TestTools.clicarBotao(sBotaoCadEnd);
//        TestTools.checarToast(R.string.erro_cadastro_endereco_campos_obrigatorios_Toast);
//    }
//
//    @Test
//    public void testeCadastrarEndereco() throws InterruptedException {
//        TestToolsEndereco.preencherCadastro(sLogradouro, sNumero, sComplemento, sBairro,
//                sCidade, sUf,sCep);
//        Thread.sleep(2000);
//        TestTools.clicarBotao(sBotaoCadEnd);
//        TestTools.checarToast(R.string.sucesso_cadastro_endereco);
//    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(4000);
        Context context = getInstrumentation().getTargetContext();
        String mIdUsuarioLogado;
        mIdUsuarioLogado = getPreferences("id", context);

        Servico servico = new Servico(sNome, sValor, sTipoPet, sDescricao);
        ServicoDaoImpl servicoDao =  new ServicoDaoImpl(context);
        servicoDao.remover(servico, mIdUsuarioLogado);
    }

    //Método que recupera o id do usuário logado, para salvar o endereço no nó do usuário que o está cadastrando
    public static String getPreferences(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
