package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import projetaobcc20172.com.projetopetemfocofornecedor.R;

public class CadastroFornecedorTipoActivity extends AppCompatActivity {

    private Spinner mSpinnerTipoFornecedor;
    private Button mBtnCadastrarFornecedor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_fornecedor_tipo);

        Toolbar toolbar;
        toolbar = findViewById(R.id.tb_cadastro_fornecedor);
        // Configura toolbar
        toolbar.setTitle(R.string.tb_cadastro_fornecedor);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        mBtnCadastrarFornecedor = findViewById(R.id.btnCadastrarFornecedor);

        //Preparar o adaptar do Spinner para exibir os horários de atendimento do fornecedor
        mSpinnerTipoFornecedor = findViewById(R.id.servicoSpinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.tiposFornecedor));
        mSpinnerTipoFornecedor.setAdapter(adapter_state);

        cadastrarFornecedor();
    }

    public void cadastrarFornecedor(){
        mBtnCadastrarFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroFornecedorTipoActivity.this,CadastroFornecedorActivity.class);
                String tipoFornecedor = mSpinnerTipoFornecedor.getSelectedItem().toString();
                intent.putExtra("tipoFornecedor", tipoFornecedor);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
