package projetaobcc20172.com.projetopetemfocofornecedor.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.adapter.ServicoAdapter;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;

public class ServicosActivity extends Fragment {

    @SuppressLint("WrongConstant")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    //permite que essa variavel seja vista pela classe de teste

    private ArrayList<Servico> mServicos;
    private ServicoAdapter mAdapter;
    public ListView mListView;
    private String mIdUsuarioLogado;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.activity_servicos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Recuperar id do fornecedor logado

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mIdUsuarioLogado = preferences.getString("id", "");

        ImageButton mCadastroServico;

        getActivity().setTitle("Serviços");

        mCadastroServico =  getView().findViewById(R.id.btnCadastrarServico);
        mListView = getView().findViewById(R.id.lv_serviços);

        // Monta listview e mAdapter
        mServicos = new ArrayList<>();
        mAdapter = new ServicoAdapter(getActivity(), mServicos);
        mListView.setAdapter(mAdapter);

        carregarServicos();

        //Ação do botão de cadastrar o serviço, que abre a tela para o seu cadastro
        mCadastroServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CadastroServicoActivity.class);
                startActivity(intent);
            }
        });

        this.chamarInfoServicoListener();

    }

    //Método que passa as informações de um serviço para a Activity que exibe seus detalhes
    public void chamarInfoServicoListener() {
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InfoServicoActivity.class);
                Servico servico = mServicos.get(position);
                intent.putExtra("Servico", servico);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
            }
        });
    }

    private void carregarServicos(){
        // Recuperar serviços do Firebase
        Query query = ConfiguracaoFirebase.getFirebase().child("servicos").orderByChild("idFornecedor").equalTo(mIdUsuarioLogado);
        ValueEventListener mValueEventListenerServico;
        mValueEventListenerServico = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mServicos.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Servico servico = dados.getValue(Servico.class);
                    servico.setmId(dados.getKey());
                    mServicos.add(servico);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Erro na leitura do banco de dados", Toast.LENGTH_SHORT).show();
            }
        };
        query.addValueEventListener(mValueEventListenerServico);
    }
}