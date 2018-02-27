package projetaobcc20172.com.projetopetemfocofornecedor.helper;

/**
 * Created by Alexsandro on 25/02/2018.
 */

import android.app.Activity;
import android.os.AsyncTask;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.TimeOutException;

public class JsonRequestAsync extends AsyncTask<String, Void, String> {
    private String url;
    private String json = null;
    private int tempoEspera = 3;//em segundos
    public JsonRequestAsync(String url){
        this.url = url;
    }


    @Override
    protected String doInBackground(String... strings) {
        try {
            json = JsonRequest.request(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    //Método com espera ativa.Caso a busca pelo json demore mais de 3 segundos
    //Lança uma exceção de erro
    public String getJson(Activity act) throws InterruptedException, TimeOutException {
        int cont = 0;
        int incremento = 25;
        while(json==null) {
            if(cont==tempoEspera*1000){
                throw new TimeOutException(act.getString(R.string.erro_timeout_msg));
            }
            Thread.sleep(incremento);
            cont=cont+incremento;
        }
        return this.json;
    }
}


