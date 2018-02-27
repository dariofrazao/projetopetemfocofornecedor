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
import projetaobcc20172.com.projetopetemfocofornecedor.model.Cupom;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

/**
 * Created by LuizAlberes on 15/02/2018.
 */

public class CupomDaoImpl implements CupomDao{

    private DatabaseReference mReferenciaFirebase;
    private  final Context mContexto;
    private String mConfirma = "0";

    public CupomDaoImpl(Context contexto){
        this.mReferenciaFirebase = ConfiguracaoFirebase.getFirebase();
        this.mContexto = contexto;
    }

    @Override
    public void inserir(final Cupom cupom, final String idFornecedor) {

        cupom.setIdFornecedor(idFornecedor);
        mReferenciaFirebase = mReferenciaFirebase.child("cupons").push();
        final String id = mReferenciaFirebase.getKey();
        cupom.setId(id);

        mReferenciaFirebase.setValue(cupom).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.cupom_criado));
//                    Intent intent = new Intent(mContexto, MainActivity.class);
//                    mContexto.startActivity(intent);
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

    public void compararInserir(final Cupom cupom, final String idFornecedor) {
        mReferenciaFirebase.child("cupons").orderByChild("idFornecedor").equalTo(idFornecedor).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            if (dados.child("nome").getValue().equals(cupom.getNome()) &
                                    dados.child("idServico").getValue().equals(cupom.getIdServico())){
                                Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.cupom_nome_existente));
                                mConfirma = "1";
                                break;
                            }
                        }
                        if(mConfirma.equals("0")){
                            inserir(cupom, idFornecedor);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //vazio
                    }
                });
    }

    @Override
    public void remover(final Cupom cupom, final String idFornecedor) {

        mReferenciaFirebase.child("cupons").child(cupom.getId()).orderByChild(idFornecedor).
                equalTo(cupom.getId()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mReferenciaFirebase.child("cupons").child(cupom.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Se a remoçao foi feita com sucesso
                        if(task.isSuccessful()){
                            Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.cupom_removido));
                        }
                        //Senão
                        else{
                            Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.erro_ao_remover));
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
                //vazio
            }
        });
    }

    @Override
    public void atualizar(final Cupom cupom, final String idFornecedor) {
        mReferenciaFirebase.child("cupons").orderByChild("id").
                equalTo(cupom.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mReferenciaFirebase.child("cupons").child(cupom.getId()).
                        setValue(cupom).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.sucesso_atualizar_cupom));
                        }else{
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

    public void compararAtualizar(final Cupom cupom, final String idFornecedor) {
        mReferenciaFirebase.child("cupons").orderByChild("idFornecedor").equalTo(idFornecedor).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            if (!(dados.child("id").getValue().toString().equals(cupom.getId())) &
                                (dados.child("nome").getValue().equals(cupom.getNome())) &
                                        (dados.child("idServico").getValue().equals(cupom.getIdServico()))){
                                    Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.cupom_nome_existente));
                                    mConfirma = "1";
                                    break;
                            }
                        }
                        if(("0").equals(mConfirma)){
                            atualizar(cupom, idFornecedor);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //vazio
                    }
                });
    }

    private Context getContexto(){
        return this.mContexto;
    }

}
