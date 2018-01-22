package projetaobcc20172.com.projetopetemfocofornecedor;

import android.content.Context;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;

import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.ValidacaoException;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.VerificadorDeObjetos;

/**
 * Created by raul on 12/12/17.
 */
@RunWith(Parameterized.class)
public class UnitTestCadastroFornecedor {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private Context mMockContext;

    @Parameterized.Parameter
    public Fornecedor fornecedorTest;

    @Parameterized.Parameters
    public static Iterable<? extends Object> data() {
        String nome = "Teste";
        String email="teste@gmail.com";
        String cpfCnpj = "123456";
        String telefone="996085321";
        String senha="123456";
        String horarios = "17:30";

        return Arrays.asList( nome,  nomeBusca,  email,  cpfCnpj,  telefone,  senha,  senha2,  horarios) {

            new Fornecedor(nome,nome,email,cpfCnpj,telefone,senha,senha,""),
                new Fornecedor(nome,nome,email,cpfCnpj,telefone,senha,"",""),
                new Fornecedor(nome,nome,email,cpfCnpj,telefone,"","",""),
                new Fornecedor(nome,nome,email,cpfCnpj,"","","",""),
                new Fornecedor(nome,nome,email,"","","","",""),
                new Fornecedor(nome,nome,"","","","","",""),
                new Fornecedor("",nome,email,cpfCnpj,telefone,senha,senha,horarios),
                new Fornecedor(nome,nome,"",cpfCnpj,telefone,senha,senha,horarios),
                new Fornecedor(nome,nome,email,"",telefone,senha,senha,horarios),
                new Fornecedor(nome,nome,email,cpfCnpj,"",senha,senha,horarios));
    }

    @Test(expected=ValidacaoException.class)
    public void testCampoObgUsuario() throws ValidacaoException {
        VerificadorDeObjetos.vDadosFornecedor(fornecedorTest,mMockContext);
    }
}
