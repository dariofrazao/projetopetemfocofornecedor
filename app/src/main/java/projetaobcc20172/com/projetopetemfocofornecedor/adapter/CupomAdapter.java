package projetaobcc20172.com.projetopetemfocofornecedor.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Cupom;

/**
 * Created by LuizAlberes on 15/02/2018.
 */

public class CupomAdapter extends ArrayAdapter<Cupom> {

    private ArrayList<Cupom> mCupons;
    private Context mContext;
    private String mIdUsuarioLogado;

    public CupomAdapter(@NonNull Context context, @NonNull ArrayList<Cupom> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.mCupons = objects;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (mCupons != null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert inflater != null;
            view = inflater.inflate(R.layout.lista_cupom, parent, false);

            TextView mNome = view.findViewById(R.id.tvNomeCupom);
            TextView mValor = view.findViewById(R.id.tvValorCupom);
            TextView mData = view.findViewById(R.id.tvDataCupom);

            final Cupom cupom = mCupons.get(position);
            mNome.setText(cupom.getNome());
            mValor.setText(cupom.getValor());
            mData.setText("Validade: "+cupom.getDataInicio()+ " - " + cupom.getDataVencimento());

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            mIdUsuarioLogado = preferences.getString("idFornecedor", "");

        }
        return view;
    }
}
