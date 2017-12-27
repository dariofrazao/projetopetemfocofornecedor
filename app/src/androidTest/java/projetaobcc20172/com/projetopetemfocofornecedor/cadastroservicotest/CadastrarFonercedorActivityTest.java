package projetaobcc20172.com.projetopetemfocofornecedor.cadastroservicotest;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.CadastroEnderecoActivity;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.CadastroFornecedorActivity;

import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;

import projetaobcc20172.com.projetopetemfocofornecedor.cadastroenderecotest.EnderecoActivityTest;

/**
 * Created by raul on 12/12/17.
 */

public class CadastrarFonercedorActivityTest {


    //Essas variaveis guardam valores que são preenchidos nos campos
    //Existentes na tela de cadastro
    private String mNome = "Teste"; //mNome do usuário
    private String mEmailJaUtilizado = "raulpedrouag@gmail.com";
    private String mEmailInvalido = "testesdakjdkas--*@ffss";
    private String mEmail = "luar13.pedro@yahoo.com.br";
    private String mSenha = "12345e";
    private String mSenha2 = "12345ew1";
    private String mTelefone = "96085746";
    private String mHorarios = "Noite";
    private String mCpfCnpj = "123456789";

    @Rule
    public ActivityTestRule<CadastroFornecedorActivity> cadFonecedorActivityRule = new ActivityTestRule<>(CadastroFornecedorActivity.class);


    @Before
    public void setUp() throws Exception {
        //Desloga caso já esteja logado.
        //Evita erros nos testes
        try{
            TestTools.clicarBotao(R.id.botao_sair);
        }catch (Exception e){
            e.getMessage();
        }
        Thread.sleep(3000);
    }

    private void preencherComInfoCorretas(){
        String email = TestTools.gerarEmailTeste(10);
        TestToolsCadFornecedor.preencherEclicar(this.mNome,email, this.mCpfCnpj,this.mTelefone,this.mSenha, this.mSenha,this.mHorarios);
        TestTools.checarToast(R.string.sucesso_cadastro_proxima_etapa_Toast);
        TestTools.verificarMudancaActivity(CadastroEnderecoActivity.class.getName());
    }

    @Test
    public void testeEmailJaCadastrado(){
        TestToolsCadFornecedor.preencherEclicar(this.mNome,this.mEmailJaUtilizado, this.mCpfCnpj,this.mTelefone,this.mSenha, this.mSenha,this.mHorarios);
        TestTools.checarToast(cadFonecedorActivityRule.getActivity().getString(R.string.erro_cadastro_fornecedor_email_usado_Toast));
    }

    @Test
    public void testeCadastroCamposAusentes(){
        TestTools.clicarBotaoComScroll(R.id.botao_cadastrar_fornecedor);
        TestTools.checarToast(cadFonecedorActivityRule.getActivity().getString(R.string.erro_cadastro_fornecedor_campos_obrigatorios_Toast));
    }

    @Test
    public void testeCadastroCampoAusenteNome(){
        TestToolsCadFornecedor.preencherEclicar("",this.mEmailJaUtilizado, this.mCpfCnpj,this.mTelefone,this.mSenha, this.mSenha,this.mHorarios);
        TestTools.checarToast(cadFonecedorActivityRule.getActivity().getString(R.string.erro_cadastro_fornecedor_campos_obrigatorios_Toast));
    }

    @Test
    public void testeCadastroCampoAusenteEmail(){
        TestToolsCadFornecedor.preencherEclicar(this.mNome,"", this.mCpfCnpj,this.mTelefone,this.mSenha, this.mSenha,this.mHorarios);
        TestTools.checarToast(cadFonecedorActivityRule.getActivity().getString(R.string.erro_cadastro_fornecedor_campos_obrigatorios_Toast));
    }

    @Test
    public void testeCadastroCampoAusenteSenha1(){
        TestToolsCadFornecedor.preencherEclicar(this.mNome,this.mEmailJaUtilizado, this.mCpfCnpj,this.mTelefone,"", this.mSenha,this.mHorarios);
        TestTools.checarToast(cadFonecedorActivityRule.getActivity().getString(R.string.erro_cadastro_fornecedor_campos_obrigatorios_Toast));
    }

    @Test
    public void testeCadastroCampoAusenteSenha2(){
        TestToolsCadFornecedor.preencherEclicar(this.mNome,this.mEmailJaUtilizado, this.mCpfCnpj,this.mTelefone,this.mSenha, "",this.mHorarios);
        TestTools.checarToast(cadFonecedorActivityRule.getActivity().getString(R.string.erro_cadastro_fornecedor_senhas_diferentes_Toast));
    }

    //Teste que simula um cadastro com e-mail Inválido
    @Test
    public void testeCadastroEmailInvalido(){
        TestToolsCadFornecedor.preencherEclicar(this.mNome,this.mEmailInvalido, this.mCpfCnpj,this.mTelefone,this.mSenha, this.mSenha,this.mHorarios);
        TestTools.checarToast(cadFonecedorActivityRule.getActivity().getString(R.string.erro_cadastro_fornecedor_email_invalido_Toast));
    }

    //Teste que simula um cadastro com mSenha e mSenha de confirmação diferentes
    @Test
    public void testeCadastrarSenhasDif(){
        TestToolsCadFornecedor.preencherEclicar(this.mNome,this.mEmail, this.mCpfCnpj,this.mTelefone,this.mSenha, this.mSenha2,this.mHorarios);
        TestTools.checarToast(cadFonecedorActivityRule.getActivity().getString(R.string.erro_cadastro_fornecedor_senhas_diferentes_Toast));

    }

    //Teste que simula um cadastro com mSenha fraca (menos que 6 caracteres)
    @Test
    public void testeCadastrarSenhaFraca(){
        String senha3 = "12345";
        TestToolsCadFornecedor.preencherEclicar(this.mNome,this.mEmail, this.mCpfCnpj,this.mTelefone,senha3, senha3,this.mHorarios);
        TestTools.checarToast(R.string.erro_cadastro_fornecedor_senha_invalida_Toast);
    }

    @Test
    public void testeCadastrarFonecedor(){
        Intents.init();
        this.preencherComInfoCorretas();
        new EnderecoActivityTest().testeEnderecoCorreto();
    }

    @Test
    //Teste que avalia o cadastro de um consumidor sem
    //a informção de num no endereço
    public void testeCadastrarFonecedorEndNum(){
        Intents.init();
        this.preencherComInfoCorretas();
        new EnderecoActivityTest().testeEnderecoCorretoSemNumero();
    }

    @Test
    //Teste que avalia o cadastro de um consumidor sem
    //a informção de complemento no endereço
    public void testeCadastrarFonecedorComplemento(){
        Intents.init();
        this.preencherComInfoCorretas();
        new EnderecoActivityTest().testeEnderecoCorretoSemCompl();
    }

    @Test
    //Teste que avalia o cadastro de um consumidor sem
    //a informação de complemento e de número no endereço
    public void testeCadastrarFonecedorComplENumero(){
        Intents.init();
        this.preencherComInfoCorretas();
        new EnderecoActivityTest().testeEnderecoCorretoSemComplENumero();
    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(5000);
    }
}
