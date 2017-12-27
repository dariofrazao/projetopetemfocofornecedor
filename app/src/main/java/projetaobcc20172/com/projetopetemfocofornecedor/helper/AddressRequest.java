package projetaobcc20172.com.projetopetemfocofornecedor.helper;

import android.os.AsyncTask;
import com.google.gson.Gson;
import java.lang.ref.WeakReference;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.CadastroEnderecoActivity;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Endereco;

//Classe que faz a requisição do endereço a partir do cep informado na tela de Cadastro do Endereço
public class AddressRequest extends AsyncTask<Void, Void, Endereco> {

    private WeakReference<CadastroEnderecoActivity> activity;

    public AddressRequest(CadastroEnderecoActivity activity){
        this.activity = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(activity.get() != null){
            activity.get().lockFields(true);
        }

    }

    @Override
    protected Endereco doInBackground(Void... voids) {
        try{
            String jsonString = JsonRequest.request( activity.get().getUriZipCode() );
            Gson gson = new Gson();

            return gson.fromJson(jsonString, Endereco.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Endereco address) {
        super.onPostExecute(address);
        if(activity.get() != null){
            activity.get().lockFields( false );
        }

            if(address != null){
                activity.get().setDataViews(address);
            }
        }
}
