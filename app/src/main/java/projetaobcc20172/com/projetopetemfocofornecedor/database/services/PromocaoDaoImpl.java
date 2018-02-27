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
import projetaobcc20172.com.projetopetemfocofornecedor.model.Promocao;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

/**
 * Created by Cloves on 19/01/2018.
 */

public class PromocaoDaoImpl implements PromocaoDao{

    private DatabaseReference mReferenciaFirebase;

    private Context mContexto;

    public PromocaoDaoImpl(Context contexto){
        this.mReferenciaFirebase = ConfiguracaoFirebase.getFirebase();
        this.mContexto = contexto;
    }

    @Override
    public void inserir(Promocao promocao, String idFornecedor) {
        promocao.setFornecedorId(idFornecedor);
        mReferenciaFirebase = mReferenciaFirebase.child("promocoes").push();
        promocao.setId(mReferenciaFirebase.getKey());

        mReferenciaFirebase.setValue(promocao).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utils.mostrarMensagemCurta(getContexto(), "sucesso ao cadastrar a promoção");
                }else{
                    Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.erro_ao_cadastrar));
                    try{
                        throw task.getException();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public void remover(final Promocao promocao, final String idFornecedor) {

        mReferenciaFirebase.child("promocoes").
                child(idFornecedor).orderByChild("id").
                equalTo(promocao.getId()).
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mReferenciaFirebase.child("promocoes").child(promocao.getId()).
                        removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Utils.mostrarMensagemCurta(getContexto(), "promocao removida com sucesso");
                        }else{
                            Utils.mostrarMensagemCurta(getContexto(), "erro ao remover");
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

    @Override
    public void atualizar(final Promocao promocao, final String idFornecedor) {
        mReferenciaFirebase.child(String.format("%s/%s","promocoes",promocao.getId()))
                .setValue(promocao).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utils.mostrarMensagemLonga(getContexto(), getContexto().getString(R.string.sucesso_atualizacao));
                } else{
                    Utils.mostrarMensagemLonga(getContexto(), getContexto().getString(R.string.falha_atualizacao));
                    try {
                        throw  task.getException();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public Context getContexto() {
        return mContexto;
    }
}
