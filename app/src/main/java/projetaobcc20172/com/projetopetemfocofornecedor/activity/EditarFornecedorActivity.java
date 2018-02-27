package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.database.services.FornecedorDaoImpl;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Cupom;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.MaskUtil;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

/**
 * Created by renat on 02/12/2017.
 */

/**
 * Activity de edicao de estabelecimento
 */
public class EditarFornecedorActivity extends android.support.v4.app.Fragment {

    @SuppressLint("WrongConstant")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    //permite que essa variavel seja vista pela classe de teste

    private EditText mTelefone;
    private Spinner mSpinnerHorarios;
    private Fornecedor mFornecedor;
    private boolean mIsViewsHabilitadas = true;
    public Toast mToast;
    private String mIdUsuarioLogado;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.activity_editar_fornecedor, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mIdUsuarioLogado = preferences.getString("idFornecedor", "");

        // Configura toolbar
        getActivity().setTitle(R.string.tb_editar_fornecedor);

        Fornecedor mFornecedor = new Fornecedor();

        //exibir o telefone
        mTelefone = getActivity().findViewById(R.id.etEditarTelefoneFornecedor);
        mTelefone.addTextChangedListener(MaskUtil.mask(mTelefone, MaskUtil.FORMAT_FONE));

        //Preparar o adaptar do Spinner para exibir os horários de atendimento do fornecedor
        mSpinnerHorarios = getActivity().findViewById(R.id.horariosEditarSpinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.horariosFornecedor));
        mSpinnerHorarios.setAdapter(adapter_state);
        String horarios = mFornecedor.getHorarios();
        int posicaoHorarios = adapter_state.getPosition(horarios);
        mSpinnerHorarios.setSelection(posicaoHorarios);

        Button botaoEditar;
        botaoEditar = getActivity().findViewById(R.id.botao_editar_fornecedor);
        botaoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarFornecedor();
            }
        });

        carregarFornecedor(mTelefone, mSpinnerHorarios);
    }

    // Recuperar fornecedor do Firebase e trocar de acticity
    private void carregarFornecedor(final EditText telefone, final Spinner horarios){
        final Query query = ConfiguracaoFirebase.getFirebase().child("fornecedor");
        final ValueEventListener mValueEventListenerFornecedor;
        mValueEventListenerFornecedor = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    if(dados.getKey().equals(mIdUsuarioLogado)) {
                        Fornecedor fornecedor = dados.getValue(Fornecedor.class);
                        mFornecedor = fornecedor;
                        telefone.setText(fornecedor.getTelefone());
                        String horarioNome = fornecedor.getHorarios();
                        horarios.setSelection(getIndex(horarios, horarioNome));
                        horarios.setVisibility(View.VISIBLE);
                        telefone.setVisibility(View.VISIBLE);
                        break;
                    }


                    //private method of your class
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //vazio
            }
        };
        query.addValueEventListener(mValueEventListenerFornecedor);
    }

    private int getIndex(Spinner spinner, String string)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)){
                index = i;
                break;
            }
        }
        return index;
    }

    //Método para editar o fornecedor no FirebaseAuthentication
    private void editarFornecedor() {

        //Recuperar id do usuário logado
        mIdUsuarioLogado = getPreferences("idFornecedor", getActivity());

        mFornecedor.setHorarios(mSpinnerHorarios.getSelectedItem().toString());
        mFornecedor.setTelefone(mTelefone.getText().toString());

        //se algum campo estiver vazio, mostra mensagem e nao permite editar ate que o campo seja preenchido
        if (mFornecedor.getTelefone().toString().isEmpty() || mFornecedor.getHorarios().toString().isEmpty()){
            mToast = Toast.makeText(getActivity(), R.string.erro_editar_fornecedor_campos_obrigatorios_Toast, Toast.LENGTH_SHORT);
            mToast.show();
        }else{
            FornecedorDaoImpl fornecedorDao = new FornecedorDaoImpl(getActivity());
            fornecedorDao.atualizar(mFornecedor, mIdUsuarioLogado);
            //abrirTelaPrincipal();
        }
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private boolean verificarCamposPreenchidos(){
        return (!mTelefone.getText().toString().isEmpty()||
                !mSpinnerHorarios.getSelectedItem().toString().isEmpty());
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
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // Botão não foi clicado
                        break;
                    default:
                        break;
                }
            }
        };

        Utils.mostrarPerguntaSimNao(getActivity(), getString(R.string.atencao),
                getString(R.string.pergunta_confirma_dados_serao_perdidos), dialogClickListener,
                dialogClickListener);

    }

    //Método que recupera o id do usuário logado
    public static String getPreferences(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}
