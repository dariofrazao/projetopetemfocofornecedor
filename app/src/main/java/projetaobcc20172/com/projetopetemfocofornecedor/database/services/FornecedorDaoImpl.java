package projetaobcc20172.com.projetopetemfocofornecedor.database.services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Fornecedor;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

/**
 * Created by dario on 11/12/2017.
 */

public class FornecedorDaoImpl implements FornecedorDao{

    private DatabaseReference mReferenciaFirebase;
    private  final Context mContexto;

    public FornecedorDaoImpl(Context contexto){
        this.mReferenciaFirebase = ConfiguracaoFirebase.getFirebase();
        this.mContexto = contexto;
    }

    @Override
    public void inserir(Fornecedor fornecedor, String idFornecedor) {

        //Método para salvar usuário no banco de dados do Firebase


        //Chama a referência do Firebase
        //Cria os nós dos fornecedores no banco de dados
        mReferenciaFirebase.child("fornecedor").child(fornecedor.getId()).setValue(fornecedor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.sucesso_cadastro_Fornecedor));
                }
                else{
                    Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.erro_ao_cadastrar));
                    try {
                        throw  task.getException();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void atualizar(final Fornecedor fornecedor, final String idFornecedor) {

        //Adicionar um listener no nó do fornecedor que será editado
        //O método orderByChild ordena os fornecedores pelo seu id e o equalTo busca o id do fornecedor que será editado
        mReferenciaFirebase.child("fornecedor").child(idFornecedor).orderByChild("id").
                equalTo(idFornecedor).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mReferenciaFirebase.child("fornecedor").child(idFornecedor).setValue(fornecedor).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Se a edição foi feita com sucesso
                        if(task.isSuccessful()){
                            Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.sucesso_atualizacao));
                        }
                        //Senão
                        else{
                            Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.erro_ao_atualizar));
                            try {
                                throw  task.getException();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                assert true;
            }
        });
    }

    private Context getContexto(){
        return this.mContexto;
    }
}
