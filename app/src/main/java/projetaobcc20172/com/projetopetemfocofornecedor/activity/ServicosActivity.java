package projetaobcc20172.com.projetopetemfocofornecedor.activity;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ImageButton;

import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.adapter.ServicoAdapter;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.ServicoDaoImpl;

import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

public class ServicosActivity extends AppCompatActivity implements ServicoAdapter.CustomButtonListener{

    private FirebaseAuth mAutenticacao;
    private DatabaseReference mFirebase;
    private ArrayList<Servico> mServicos;
    private ServicoAdapter mAdapter;
    private ValueEventListener mValueEventListenerServico;
    private String mIdUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);

        mAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //Recuperar id do fornecedor logado

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mIdUsuarioLogado = preferences.getString("idFornecedor", "");

        ImageButton cadastroServico;

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_main);

        // Configura toolbar
        toolbar.setTitle("Serviços");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        cadastroServico =  findViewById(R.id.botao_cadastrar_serviço);
        ListView listView;
        listView = findViewById(R.id.lv_serviços);

        // Monta listview e mAdapter
        mServicos = new ArrayList<>();
        mAdapter = new ServicoAdapter(ServicosActivity.this, mServicos);
        mAdapter.setCustomButtonListener(ServicosActivity.this);
        listView.setAdapter(mAdapter);

        // Recuperar serviços do Firebase
        mFirebase = ConfiguracaoFirebase.getFirebase().child("fornecedor").child(mIdUsuarioLogado).child("servicos");

        mValueEventListenerServico = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mServicos.clear();

                //Recupera serviços
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Servico servico = dados.getValue(Servico.class);
                    servico.setmId(dados.getKey());
                    mServicos.add(servico);
                }
                //Notificar o adaptar que exibe a lista de serviços se houver alteração no banco
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ServicosActivity.this, "Erro na leitura do banco de dados", Toast.LENGTH_SHORT).show();
            }
        };

        cadastroServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ServicosActivity.this, CadastroServicoActivity.class);
                startActivity(intent);
            }
        });
        mFirebase.addValueEventListener(mValueEventListenerServico);

        //Ação do botão de cadastrar o serviço, que abre a tela para o seu cadastro
        cadastroServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServicosActivity.this, CadastroServicoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    @SuppressLint("ResourceType")
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.layout.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_editar){
            Intent intent = new Intent();
            intent.setClass(ServicosActivity.this, CadastroServicoActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_eliminar){
            Servico servico = null;
            confirmarRemocao(servico);
            
            return true;
        }

        return  super.onOptionsItemSelected(item);
    }



    private void removerServico(Servico servico){

        ServicoDaoImpl servicoDao =  new ServicoDaoImpl(this);
        servicoDao.remover(servico, mIdUsuarioLogado);
    }

    @Override
    public void onButtonRemoverClickListner(Servico servico) {
        confirmarRemocao(servico);
    }

    @Override
    public void onButtonEditarClickListner(Servico servico) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("servico",  servico);
        intent.putExtras(bundle);
        intent.setClass(ServicosActivity.this, CadastroServicoActivity.class);
        startActivity(intent);
    }

    /**
     *
     */
    public void confirmarRemocao(final Servico servico){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // Botão sim foi clicado
                        removerServico(servico);
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
                getString(R.string.info_confirmar_remocao_servico), dialogClickListener,
                dialogClickListener);
    }
}
