package projetaobcc20172.com.projetopetemfocofornecedor.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.adapter.ServicoAdapter;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;

//Classe que monta um Fragmente (pedaço de tela para exibir os serviços)
//Sua utilização é útil para dividir uma mesma tela em mais partes.
public class ServicosFragment extends Fragment {

    private ArrayAdapter<Servico> mAdapter;
    private ArrayList<Servico> mServicos;
    private DatabaseReference mFirebase;

    public ServicosFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment, container, false);

        // Monta listview e adapter
        mServicos = new ArrayList<>();
        ListView mListView;
        mListView = view.findViewById(R.id.lv_serviços);
        mAdapter = new ServicoAdapter(getActivity(), mServicos);
        mListView.setAdapter(mAdapter);

        //Listener que "ouve" o banco de dados
        ValueEventListener valueEventListenerServicos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mServicos.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Servico servico = dados.getValue(Servico.class);
                    mServicos.add(servico);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // vazio
            }
        };
        mFirebase.addValueEventListener(valueEventListenerServicos);
        return view;
    }

}