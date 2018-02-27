package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.adapter.PromocaoAdapter;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.PromocaoDao;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.PromocaoDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Promocao;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

public class PromocaoActivity extends android.support.v4.app.Fragment implements PromocaoAdapter.CustomButtonListener{

    private ArrayList<Promocao> mPromocoes;

    private PromocaoAdapter mAdapter;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)//permite que essa variavel seja vista pela classe de teste
    public ListView listView;
    private String mIdUsuarioLogado;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.activity_promocao, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mIdUsuarioLogado = preferences.getString("id", "");

        ImageButton cadastroPromocao;

        getActivity().setTitle("Promoções");

        cadastroPromocao = getActivity().findViewById(R.id.btnCadastrarPromocao);
        this.listView = getActivity().findViewById(R.id.lv_promocao);

        mPromocoes = new ArrayList<>();
        mAdapter = new PromocaoAdapter(getActivity(), mPromocoes);
        mAdapter.setCustomButtonListener(PromocaoActivity.this);
        listView.setAdapter(mAdapter);

        this.carregarPromocoes();

        this.chamarInfoPromocaoListener();

        cadastroPromocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CadastroPromocaoActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    private void carregarPromocoes(){

        Query query = ConfiguracaoFirebase.getFirebase().child("promocoes").orderByChild("fornecedorId").equalTo(mIdUsuarioLogado);
        ValueEventListener mValueEventListenerPromocao;
        mValueEventListenerPromocao = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPromocoes.clear();
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Promocao promocao = dados.getValue(Promocao.class);
                    promocao.setId(dados.getKey());
                    mPromocoes.add(promocao);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                assert true;
            }
        };
        query.addValueEventListener(mValueEventListenerPromocao);
    }

    private void removerPromocao(Promocao promocao){
        PromocaoDao promocaoDao = new PromocaoDaoImpl(getActivity());
        promocaoDao.remover(promocao,mIdUsuarioLogado);
        mPromocoes.remove(promocao);
        mAdapter.notifyDataSetChanged();
    }

    private void confirmarRemocao(final Promocao promocao){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        removerPromocao(promocao);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                    default:
                        break;
                }
            }
        };

        Utils.mostrarPerguntaSimNao(getActivity(), getString(R.string.atencao),
                getString(R.string.info_confirmar_remocao_promocao), dialogClickListener,
                dialogClickListener);

    }

    //Método que passa as informações de uma promocao para a Activity que exibe seus detalhes
    public void chamarInfoPromocaoListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), InfoPromocaoActivity.class);
                Promocao promocao = mPromocoes.get(position);
                intent.putExtra("Promocao", promocao);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onButtonRemoverClickListner(Promocao promocao) {
        confirmarRemocao(promocao);
    }

    @Override
    public void onButtonEditarClickListner(Promocao promocao) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("promocao",  promocao);
        intent.putExtras(bundle);
        intent.setClass(getActivity(), CadastroPromocaoActivity.class);
        startActivity(intent);
        //finish();
    }

}
