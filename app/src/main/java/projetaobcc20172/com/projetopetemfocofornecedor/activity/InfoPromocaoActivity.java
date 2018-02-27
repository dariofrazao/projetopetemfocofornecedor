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
import android.widget.TextView;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.adapter.PromocaoAdapter;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.PromocaoDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Promocao;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

/**
 * Created by renat on 22/01/2018.
 */

public class InfoPromocaoActivity extends AppCompatActivity {

    private TextView mTitulo, mDescricao, mValor, mData;
    private Promocao mPromocao;
    private String mIdUsuarioLogado;
    private Button mEditar, mExcluir, mVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_promocao);

        mTitulo = findViewById(R.id.tvTitulo);
        mDescricao = findViewById(R.id.tvDescricao);
        mValor = findViewById(R.id.tvValor);
        mData = findViewById(R.id.tvData);
        mVoltar = findViewById(R.id.btnVoltar);
        mEditar = findViewById(R.id.btnEditar);
        mExcluir = findViewById(R.id.btnExcluir);

        mPromocao = (Promocao) getIntent().getSerializableExtra("Promocao");

        //Recuperar id do usuário logado
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(InfoPromocaoActivity.this);
        mIdUsuarioLogado = preferences.getString("idFornecedor", "");

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_main);

        // Configura toolbar
        toolbar.setTitle("Info Promoção");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        preencherCampos();
        voltar();

        //excluirListener();

        //esconde botoes pois no momento nao estao sendo utilizados
        mEditar.setVisibility(View.INVISIBLE);
        mExcluir.setVisibility(View.INVISIBLE);



    }

    public void preencherCampos() {
        mTitulo.setText("Titulo: " + mPromocao.getTitulo());
        mDescricao.setText("Descrição: " + mPromocao.getDescricao());
        mValor.setText("Valor: " + mPromocao.getValor());
        mData.setText("Vencimento em: " + mPromocao.getData());
    }

    public void voltar() {
        mVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoPromocaoActivity.super.onBackPressed();
                finish();
            }
        });
    }

    //Método para excluir uma promocao
    public void excluirListener() {
        mExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                // Botão sim foi clicado
                                PromocaoDaoImpl promoDao = new PromocaoDaoImpl(InfoPromocaoActivity.this);
                                promoDao.remover(mPromocao, mIdUsuarioLogado);
                                InfoPromocaoActivity.super.onBackPressed();
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

                Utils.mostrarPerguntaSimNao(InfoPromocaoActivity.this, getString(R.string.atencao),
                        getString(R.string.info_confirmar_remocao_promocao), dialogClickListener,
                        dialogClickListener);
            }
        });
    }


    //Método do botão voltar
    @Override
    public void onBackPressed() {
        InfoPromocaoActivity.super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
