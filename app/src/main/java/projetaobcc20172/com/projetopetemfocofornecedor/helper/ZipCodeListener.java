package projetaobcc20172.com.projetopetemfocofornecedor.helper;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.CadastroEnderecoActivity;

//Classe que "ouve" o campo do cep enquanto o mesmo é digitado
public class ZipCodeListener implements TextWatcher {

    private Context context;

    public ZipCodeListener(Context context){

        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable editable) {

        String zipCode = editable.toString();

        if(zipCode.length() == 9){
            new AddressRequest((CadastroEnderecoActivity) context).execute();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //vazio
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //vazio
    }
}
