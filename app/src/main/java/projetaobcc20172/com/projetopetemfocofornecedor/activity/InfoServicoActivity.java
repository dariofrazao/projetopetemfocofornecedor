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

import java.io.Serializable;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.ServicoDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

/**
 * Created by LuizAlberes on 23/02/2018.
 */

public class InfoServicoActivity extends AppCompatActivity implements Serializable {

    private ImageView mImagemServico;
    private Servico mServico;
    private String mIdUsuarioLogado;
    private Button mExcluirServico, mEditarServico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_servico);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mIdUsuarioLogado = preferences.getString("id", "");

        mServico = (Servico) getIntent().getSerializableExtra("Servico");

        TextView mTvNome;
        TextView mTvValor;
        TextView mTvTipoPetServico;

        mImagemServico = findViewById(R.id.ivDetalhesServico);
        mImagemServico.setImageResource(R.drawable.ic_cupom);

        gerarImagemNomeServico(mServico);

        mTvNome = findViewById(R.id.tvNomeServicoInfo);
        mTvNome.setText("Nome: "+ mServico.getNome());
        mTvTipoPetServico = findViewById(R.id.tvTipoPetServicoInfo);
        mTvTipoPetServico.setText("Tipo de Animal: "+ mServico.getTipoPet());
        mTvValor = findViewById(R.id.tvValorServicoInfo);
        mTvValor.setText("Valor: "+ mServico.getValor());

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_main);

        // Configura toolbar
        toolbar.setTitle("Detalhes do Serviço");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        mEditarServico = findViewById(R.id.btnEditarServicoInfo);
        mExcluirServico = findViewById(R.id.btnExcluirServicoInfo);

        excluirServicoListener();

        editarServicoListener();

    }

    private void gerarImagemNomeServico(Servico serv){
        if(serv.getNome().equals("Banho")){
            mImagemServico.setImageResource(R.drawable.servico_banho);
        }
        else if(serv.getNome().equals("Tosa")){
            mImagemServico.setImageResource(R.drawable.servico_tosa);
        }
        else if(serv.getNome().equals("Hospedagem")){
            mImagemServico.setImageResource(R.drawable.servico_hospedagem);
        }
        else if(serv.getNome().equals("Passeio")){
            mImagemServico.setImageResource(R.drawable.servico_passeio);
        }
        else if(serv.getNome().equals("Vacinação")){
            mImagemServico.setImageResource(R.drawable.servico_vacinacao);
        }
    }

    private void excluirServicoListener() {
        mExcluirServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                // Botão sim foi clicado
                                ServicoDaoImpl servicoDao = new ServicoDaoImpl(InfoServicoActivity.this);
                                servicoDao.remover(mServico, mIdUsuarioLogado);
                                InfoServicoActivity.super.onBackPressed();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // Botão não foi clicado
                                break;
                            default:
                                break;
                        }
                    }
                };

                Utils.mostrarPerguntaSimNao(InfoServicoActivity.this, getString(R.string.atencao),
                        getString(R.string.info_confirmar_remocao_servico), dialogClickListener,
                        dialogClickListener);
            }
        });
    }

    private void editarServicoListener() {
        mEditarServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enviar para a Activity de Edição do serviço seus atuais dados salvos para exibição
                Intent intent = new Intent(InfoServicoActivity.this, EditarServicoActivity.class);
                intent.putExtra("Servico", mServico);
                startActivity(intent);
                finish();
            }
        });
    }

    //Método do botão voltar
    @Override
    public void onBackPressed() {
        InfoServicoActivity.super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
