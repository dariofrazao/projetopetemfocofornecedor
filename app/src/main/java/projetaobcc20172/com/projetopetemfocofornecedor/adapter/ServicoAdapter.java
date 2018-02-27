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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;

//Classe que monta uma View para exibir os serviços cadastrados do fornecedor
public class ServicoAdapter extends ArrayAdapter<Servico> {

    private ArrayList<Servico> mServicos;
    private Context mContext;

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
            view = inflater.inflate(R.layout.lista_servicos, parent, false);

            // recupera elemento para exibição
//            holder.nome = view.findViewById(R.id.tv_titulo);
//            holder.subtitulo = view.findViewById(R.id.tv_subtitulo);
//            holder.remover = view.findViewById(R.id.iv_remover);

            TextView mNomeServico = view.findViewById(R.id.tvExibirServico);
            TextView mValorServico = view.findViewById(R.id.tvExibirServicoValor);
            ImageView imgPet = view.findViewById(R.id.ivPetImagem);
            ImageView imgServico = view.findViewById(R.id.ivServicoImagem);

            Servico servico = mServicos.get(position);
            mNomeServico.setText(servico.getNome());
            String valorConvertido = servico.getValor();
            mValorServico.setText(valorConvertido);
            gerarImagemNomeServico(servico, imgServico);
            gerarImagemPet(servico, imgPet);


        }

        return view;
    }

    private void gerarImagemNomeServico(Servico serv, ImageView img){
        if(serv.getNome().equals("Banho")){
            img.setImageResource(R.drawable.servico_banho);
        }
        else if(serv.getNome().equals("Tosa")){
            img.setImageResource(R.drawable.servico_tosa);
        }
        else if(serv.getNome().equals("Hospedagem")){
            img.setImageResource(R.drawable.servico_hospedagem);
        }
        else if(serv.getNome().equals("Passeio")){
            img.setImageResource(R.drawable.servico_passeio);
        }
        else if(serv.getNome().equals("Vacinação")){
            img.setImageResource(R.drawable.servico_vacinacao);
        }
    }

    private void gerarImagemPet(Servico serv, ImageView img){
        if(serv.getTipoPet().equals("Cachorro")){
            img.setImageResource(R.drawable.tipo_pet_cachorro);
        }
        else if(serv.getTipoPet().equals("Gato")){
            img.setImageResource(R.drawable.tipo_pet_gato);
        }
    }

}

