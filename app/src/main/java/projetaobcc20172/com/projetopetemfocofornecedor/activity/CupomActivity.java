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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.adapter.CupomAdapter;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Cupom;

/**
 * Created by LuizAlberes on 15/02/2018.
 */

public class CupomActivity extends Fragment {

    @SuppressLint("WrongConstant")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    //permite que essa variavel seja vista pela classe de teste

    private ArrayList<Cupom> mCupons;
    private ArrayAdapter<Cupom> mAdapter;
    private ListView mListView;
    private String mIdUsuarioLogado;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.activity_cupom, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mIdUsuarioLogado = preferences.getString("id", "");

        ImageButton mCadastroCupom;

        getActivity().setTitle("Códigos de Desconto");

        mCadastroCupom = getView().findViewById(R.id.btnCadastroCupomList);

        mListView = getView().findViewById(R.id.lv_cupom);

        // Monta listview e mAdapter
        mCupons = new ArrayList<>();
        mAdapter = new CupomAdapter(getActivity(), mCupons);
        mListView.setAdapter(mAdapter);

        carregarCupons();

        mCadastroCupom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CadastroCupomActivity.class);
                startActivity(intent);
                //getActivity().finish();
            }
        });
        this.chamarInfoCupomListener();
    }

    public void chamarInfoCupomListener() {
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //carregarCupom(mCupons.get(position));
                Intent intent = new Intent(getActivity(), InfoCupomActivity.class);
                Cupom cupom = mCupons.get(position);
                intent.putExtra("Cupom", cupom);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
            }
        });
    }

    private void carregarCupons(){
        // Recuperar cupons do Firebase
        Query query = ConfiguracaoFirebase.getFirebase().child("cupons").orderByChild("idFornecedor").equalTo(mIdUsuarioLogado);
        ValueEventListener mValueEventListenerCupom;
        mValueEventListenerCupom = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCupons.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Cupom cupom = dados.getValue(Cupom.class);
                    cupom.setId(dados.getKey());
                    mCupons.add(cupom);

                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                assert true;
            }
        };
        query.addValueEventListener(mValueEventListenerCupom);
    }

}
