package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.CupomDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Cupom;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

/**
 * Created by LuizAlberes on 23/02/2018.
 */

public class InfoCupomActivity extends AppCompatActivity implements Serializable {

    private Cupom mCupom;
    private String mIdUsuarioLogado;
    private Button mExcluirCupom, mEditarCupom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_cupom);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mIdUsuarioLogado = preferences.getString("id", "");

        mCupom = (Cupom) getIntent().getSerializableExtra("Cupom");


        TextView mTvNome;
        TextView mTvValor;
        TextView mTvNomeServico;
        TextView mTvValidade;
        ImageView mImagemCupom;
        mImagemCupom = findViewById(R.id.ivDetalhesCupom);
        mImagemCupom.setImageResource(R.drawable.ic_cupom);
        mTvNome = findViewById(R.id.tvNomeCupomInfo);
        mTvNome.setText("Nome: "+ mCupom.getNome());
        mTvNomeServico = findViewById(R.id.tvNomeServicoCupomInfo);
        mTvValor = findViewById(R.id.tvValorCupomInfo);
        mTvValor.setText("Valor: "+ mCupom.getValor());
        mTvValidade = findViewById(R.id.tvValidadeCupomInfo);
        mTvValidade.setText("Validade: "+ mCupom.getDataInicio() + " - " + mCupom.getDataVencimento());

        carregarServico(mCupom, mTvNomeServico);

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_main);

        // Configura toolbar
        toolbar.setTitle("Detalhes do Código");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        mEditarCupom = findViewById(R.id.btnEditarCupomInfo);
        mExcluirCupom = findViewById(R.id.btnExcluirCupomInfo);

        excluirCupomListener();

        editarCupomListener();

    }

    private void carregarServico(final Cupom mCupom, final TextView textView){
        // Recuperar serviços do Firebase
        Query query = ConfiguracaoFirebase.getFirebase().child("servicos");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int aux = 0;
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    if (dados.getKey().equals(mCupom.getIdServico())) {
                        String nomeServico = dados.child("nome").getValue(String.class);
                        String valorServico = dados.child("valor").getValue(String.class);
                        String petServico = dados.child("tipoPet").getValue(String.class);
                        String nomeCompleto = nomeServico + " - " + petServico + " - " + valorServico;
                        textView.setText("Serviço: " + nomeCompleto);
                        aux = 0;
                        break;
                    }
                    aux = 1;
                }
                if (aux == 1){
                    textView.setText("CUPOM SEM SERVIÇO");
                    perguntaEditarCupom();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                assert true;
            }
        });
    }

    public void perguntaEditarCupom(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // Botão sim foi clicado
                        Intent intent = new Intent(InfoCupomActivity.this, EditarCupomActivity.class);
                        intent.putExtra("Cupom", mCupom);
                        startActivity(intent);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // Botão não foi clicado
                        break;
                    default:
                        break;
                }

            }

        };
        Utils.mostrarPerguntaSimNao(this, getString(R.string.atencao),
                getString(R.string.pergunta_confirma_editar_cupom), dialogClickListener,
                dialogClickListener);
    }

    private void excluirCupomListener() {
        mExcluirCupom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    // Botão sim foi clicado
                                    CupomDaoImpl cupomDao = new CupomDaoImpl(InfoCupomActivity.this);
                                    cupomDao.remover(mCupom, mIdUsuarioLogado);
                                    InfoCupomActivity.super.onBackPressed();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    // Botão não foi clicado
                                    break;
                                default:
                                    break;
                            }
                        }
                    };

                    Utils.mostrarPerguntaSimNao(InfoCupomActivity.this, getString(R.string.atencao),
                            getString(R.string.pergunta_confirma_remocao_cupom), dialogClickListener,
                            dialogClickListener);
                }
            });
        }

    private void editarCupomListener() {
        mEditarCupom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enviar para a Activity de Edição do serviço seus atuais dados salvos para exibição
                Intent intent = new Intent(InfoCupomActivity.this, EditarCupomActivity.class);
                intent.putExtra("Cupom", mCupom);
                startActivity(intent);
                finish();
            }
        });
    }

    //Método do botão voltar
    @Override
    public void onBackPressed() {
        InfoCupomActivity.super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
