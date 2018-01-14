package projetaobcc20172.com.projetopetemfocofornecedor.adapter;

/**
 * Created by dario on 11/12/2017.
 */

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
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;

//Classe que monta uma View para exibir os serviços cadastrados do fornecedor
public class ServicoAdapter extends ArrayAdapter<Servico> {

    private ArrayList<Servico> mServicos;
    private Context mContext;

    private CustomButtonListener customButtonListener;

    public ServicoAdapter(Context c, ArrayList<Servico> objects) {
        super(c, 0, objects);
        this.mContext = c;
        this.mServicos = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        View view = null;
        // Verifica se a lista está preenchida
        if (mServicos != null) {

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            assert inflater != null;
            view = inflater.inflate(R.layout.lista_itens, parent, false);

            // recupera elemento para exibição
//            holder.nome = view.findViewById(R.id.tv_titulo);
//            holder.subtitulo = view.findViewById(R.id.tv_subtitulo);
//            holder.remover = view.findViewById(R.id.iv_remover);

            TextView nome = view.findViewById(R.id.tv_titulo);
            TextView subtitulo = view.findViewById(R.id.tv_subtitulo);
            ImageButton remover = view.findViewById(R.id.ibRemoverServico);
            ImageButton editar = view.findViewById(R.id.ibEditarServico);

            Servico servico = mServicos.get(position);
            nome.setText(servico.getNome());
            String valorConvertido = servico.getValor();
            subtitulo.setText(valorConvertido);

            editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(customButtonListener != null) {
                        customButtonListener.onButtonEditarClickListner(mServicos.get(position));
                    }
                }
            });

            remover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(customButtonListener != null) {
                        customButtonListener.onButtonRemoverClickListner(mServicos.get(position));
                    }
                }
            });
        }

        return view;
    }

    public void setCustomButtonListener(CustomButtonListener listener) {
        this.customButtonListener = listener;
    }


    public interface CustomButtonListener {
        void onButtonRemoverClickListner(Servico servico);
        void onButtonEditarClickListner(Servico servico);
    }
}

