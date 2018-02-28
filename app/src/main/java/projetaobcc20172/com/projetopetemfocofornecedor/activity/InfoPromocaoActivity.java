package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.annotation.SuppressLint;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.PromocaoDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Promocao;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

/**
 * Created by renat on 22/01/2018.
 */

public class InfoPromocaoActivity extends AppCompatActivity {

    private TextView mTitulo, mDescricao, mValor, mDatas;
    private Promocao mPromocao;
    private String mIdUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_promocao);

        mTitulo = findViewById(R.id.tvNomePromocaoInfo);
        mDescricao = findViewById(R.id.tvDescricaoPromocaoInfo);
        mValor = findViewById(R.id.tvValorPromocaoInfo);
        mDatas = findViewById(R.id.tvValidadePromocaoInfo);

        mPromocao = (Promocao) getIntent().getSerializableExtra("Promocao");

        //Recuperar id do usuário logado
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(InfoPromocaoActivity.this);
        mIdUsuarioLogado = preferences.getString("id", "");

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_main);

        // Configura toolbar
        toolbar.setTitle("Info Promoção");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        preencherCampos();
        excluirPromocaoListener();
        editarPromocaoListener();

    }

    @SuppressLint("SetTextI18n")
    public void preencherCampos() {
        ImageView mImagemCupom;
        mImagemCupom = findViewById(R.id.ivDetalhesPromocao);
        mImagemCupom.setImageResource(R.drawable.ic_action_promocao);
        mTitulo.setText("Nome: " + mPromocao.getTitulo());
        mDescricao.setText("Descrição: " + mPromocao.getDescricao());
        mValor.setText("Valor: " + mPromocao.getValor());

        List<Date> datas = mPromocao.getDatas();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        int i=0;
        StringBuilder mDatasValidas = new StringBuilder();
        while (datas.size() > i){
            mDatasValidas.append(dateFormat.format(datas.get(i))).append("  ");
            i++;
        }
        mDatas.setText("Datas válidas: \n" + mDatasValidas);
    }

    private void editarPromocaoListener() {
        Button mEditarPromocao =findViewById(R.id.btnEditarPromocaoInfo);
        mEditarPromocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enviar para a Activity de Edição do serviço seus atuais dados salvos para exibição
                Intent intent = new Intent(InfoPromocaoActivity.this, CadastroPromocaoActivity.class);
                intent.putExtra("promocao", mPromocao);
                startActivity(intent);
                finish();
            }
        });
    }

    //Método para excluir uma promocao
    public void excluirPromocaoListener() {
        Button mExcluirPromocao =findViewById(R.id.btnExcluirPromocaoInfo);
        mExcluirPromocao.setOnClickListener(new View.OnClickListener() {
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
