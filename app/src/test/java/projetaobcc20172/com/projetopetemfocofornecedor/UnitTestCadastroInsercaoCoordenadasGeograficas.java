package projetaobcc20172.com.projetopetemfocofornecedor;
import com.google.android.gms.maps.model.LatLng;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.ValidacaoException;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.VerificadorDeObjetos;

/**
 * Created by Alexsandro on 20/01/2018.
 */

@RunWith(Parameterized.class)
public class UnitTestCadastroInsercaoCoordenadasGeograficas {

    @Parameterized.Parameter
    public LatLng mLatLngTest;

    @Parameterized.Parameters
    public static Iterable<?> data() {
        double latitude = -8.55555888;
        double longitude = 9.45454588;
        return Arrays.asList(
                new LatLng(latitude,longitude),
                new LatLng(latitude,0),
                new LatLng(0,longitude),
                new LatLng(0,0));
    }

    @Test(expected=ValidacaoException.class)
    public void testCampoObg() throws ValidacaoException {
        VerificadorDeObjetos.vDadosObrCoordenadasGeograficas(mLatLngTest);
    }

}
