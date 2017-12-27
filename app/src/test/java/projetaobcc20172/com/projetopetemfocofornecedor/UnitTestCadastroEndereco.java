package projetaobcc20172.com.projetopetemfocofornecedor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.CampoObrAusenteException;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Endereco;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.VerificadorDeObjetos;

/**
 * Created by Alexsandro on 07/12/2017.
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(Parameterized.class)
public class UnitTestCadastroEndereco {

    @Parameterized.Parameter
    public Endereco endTest;

    @Parameterized.Parameters
    public static Iterable<?> data() {
        String mLog = "teste";
        String mBairro = "bairro";
        String mCidade = "cidade";
        String mUf = "PE";
        //String mCep = "55299-520";
        return Arrays.asList(
                new Endereco("","","",""),
                new Endereco(mLog,"","",""),
                new Endereco(mLog,mBairro,"",""),
                new Endereco(mLog,mBairro,mCidade,""),
                new Endereco("","",mCidade,mUf),
                new Endereco("","","",mUf),
                new Endereco("","","",""));
    }

    @Test (expected=CampoObrAusenteException.class)
    public void testCampoObg() throws CampoObrAusenteException {
        VerificadorDeObjetos.vDadosObrEndereco(endTest);
    }

}