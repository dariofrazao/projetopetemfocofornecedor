package projetaobcc20172.com.projetopetemfocofornecedor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Promocao;

/**
 * Created by Cloves on 19/01/2018.
 */

public class PromocaoAdapter extends ArrayAdapter<Promocao> {

    private ArrayList<Promocao> mPromocoes;
    private Context mContext;

    private PromocaoAdapter.CustomButtonListener customButtonListener;

    public PromocaoAdapter(@NonNull Context context,@NonNull ArrayList<Promocao> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.mPromocoes = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (mPromocoes != null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert inflater != null;
            view = inflater.inflate(R.layout.lista_itens, parent, false);

            TextView nome = view.findViewById(R.id.tv_titulo);
            TextView subtitulo = view.findViewById(R.id.tv_subtitulo);
            ImageButton remover = view.findViewById(R.id.ibRemoverServico);
            ImageButton editar = view.findViewById(R.id.ibEditarServico);

            Promocao promocao = mPromocoes.get(position);
            nome.setText(promocao.getTitulo());
            subtitulo.setText(promocao.getData());

            editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(customButtonListener != null) {
                        customButtonListener.onButtonEditarClickListner(mPromocoes.get(position));
                    }
                }
            });

            remover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(customButtonListener != null) {
                        customButtonListener.onButtonRemoverClickListner(mPromocoes.get(position));
                    }
                }
            });
        }
        return view;
    }

    public void setCustomButtonListener(PromocaoAdapter.CustomButtonListener listener) {
        this.customButtonListener = listener;
    }

    public interface CustomButtonListener {
        void onButtonRemoverClickListner(Promocao promocao);
        void onButtonEditarClickListner(Promocao promocao);
    }
}
