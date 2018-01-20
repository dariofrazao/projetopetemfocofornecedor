package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.FornecedorDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.MaskUtil;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

/**
 * Created by renat on 02/12/2017.
 */

/**
 * Activity de cadastro de fornecedor
 */
public class EditarFornecedorActivity extends AppCompatActivity {

    private EditText mTelefone;
    private Spinner mSpinnerHorarios;
    private Fornecedor mFornecedorAnterior;
    private boolean mIsViewsHabilitadas = true;

    //permite que essa variavel seja vista pela classe de teste
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_fornecedor);

        Intent intent = getIntent();

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_editar_fornecedor);

        // Configura toolbar
        toolbar.setTitle(R.string.tb_editar_fornecedor);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        mFornecedorAnterior = (Fornecedor) intent.getExtras().getSerializable("fornecedor");

        //exibir o telefone
        mTelefone = findViewById(R.id.etEditarTelefoneFornecedor);
        mTelefone.addTextChangedListener(MaskUtil.mask(mTelefone, MaskUtil.FORMAT_FONE));
        mTelefone.setText(mFornecedorAnterior.getTelefone());

        //Preparar o adaptar do Spinner para exibir os horários de atendimento do fornecedor
        mSpinnerHorarios = findViewById(R.id.horariosEditarSpinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.horariosFornecedor));
        mSpinnerHorarios.setAdapter(adapter_state);
        String horarios = mFornecedorAnterior.getHorarios();
        int posicaoHorarios = adapter_state.getPosition(horarios);
        mSpinnerHorarios.setSelection(posicaoHorarios);


        Button botaoEditar;
        botaoEditar = findViewById(R.id.botao_editar_fornecedor);

        botaoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarFornecedor();
            }
        });
    }

    //Método para cadastrar o fornecedor no FirebaseAuthentication
    private void editarFornecedor() {
        FirebaseAuth mAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //Recuperar id do usuário logado
        String mIdFornecedorLogado;
        mIdFornecedorLogado = getPreferences("idFornecedor", EditarFornecedorActivity.this);
        mFornecedorAnterior.setHorarios(mSpinnerHorarios.getSelectedItem().toString());
        mFornecedorAnterior.setTelefone(mTelefone.getText().toString());

        //se algum campo estiver vazio, mostra mensagem e nao permite editar ate que o campo seja preenchido
        if (mFornecedorAnterior.getTelefone().toString().isEmpty() || mFornecedorAnterior.getHorarios().toString().isEmpty()){
            mToast = Toast.makeText(EditarFornecedorActivity.this, R.string.erro_editar_fornecedor_campos_obrigatorios_Toast, Toast.LENGTH_SHORT);
            mToast.show();
        }else{
            FornecedorDaoImpl fornecedorDao = new FornecedorDaoImpl(EditarFornecedorActivity.this);
            fornecedorDao.atualizar(mFornecedorAnterior, mIdFornecedorLogado);
            abrirTelaPrincipal();
        }
    }


    private void abrirTelaPrincipal() {
        Intent intent = new Intent(EditarFornecedorActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private boolean verificarCamposPreenchidos(){
        return (!mTelefone.getText().toString().isEmpty()||
                !mSpinnerHorarios.getSelectedItem().toString().isEmpty());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Método do botão voltar
    @Override
    public void onBackPressed(){
        if (verificarCamposPreenchidos() && mIsViewsHabilitadas) confirmarSaida();
        else EditarFornecedorActivity.super.onBackPressed();
    }

    //Método que exibe pergunta de confirmação ao usuário caso ele clique no botão de voltar com as
    //informações do pet inseridas nos campos
    public void confirmarSaida() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Botão sim foi clicado
                        EditarFornecedorActivity.super.onBackPressed();
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
                getString(R.string.pergunta_confirma_dados_serao_perdidos), dialogClickListener,
                dialogClickListener);

    }

    //Método que recupera o id do usuário logado, para editar o pet no nó do usuário que o está cadastrando
    public static String getPreferences(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    //metodo gera Toast's
    public void gerarToast() {
         Toast toast = Toast.makeText(EditarFornecedorActivity.this, R.string.erro_editar_fornecedor_campos_obrigatorios_Toast, Toast.LENGTH_SHORT);
    }

}
