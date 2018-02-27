package projetaobcc20172.com.projetopetemfocofornecedor.database.services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.config.ConfiguracaoFirebase;
import projetaobcc20172.com.projetopetemfocofornecedor.helper.Preferencias;
import projetaobcc20172.com.projetopetemfocofornecedor.model.Servico;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Utils;

/**
 * Created by Felipe Oliveira on 07/12/17.
 * <flpdias14@gmail.com>
 */

public class ServicoDaoImpl implements ServicoDao{


    private DatabaseReference mReferenciaFirebase;
    private final Context mContexto;
    private final Preferencias mPreferencias;
    private String mConfirma =  "0";
    private String mId;

    public ServicoDaoImpl(Context contexto){
        this.mReferenciaFirebase = ConfiguracaoFirebase.getFirebase();
        this.mContexto = contexto;
        this.mPreferencias =  new Preferencias(this.mContexto);
    }

    @Override
    public void inserir(final Servico servico, final String idFornecedor) {
        servico.setIdFornecedor(idFornecedor);
        DatabaseReference ref = mReferenciaFirebase.child("servicos").push();
        final String id = ref.getKey();
        ref.setValue(servico).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    servico.setmId(id);
                    salvarFornecedorServico(servico,servico.getId(), idFornecedor);
                    Utils.mostrarMensagemLonga(getContexto(), getContexto().getString(R.string.sucesso_cadastro_servico));
                }
                else{
                    Utils.mostrarMensagemLonga(getContexto(), getContexto().getString(R.string.erro_cadastro_servico));
                    try {
                        throw  task.getException();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void compararInserir(final Servico servico, final String idFornecedor) {
        mReferenciaFirebase.child("servicos").orderByChild("idFornecedor").equalTo(idFornecedor).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            if (dados.child("nome").getValue().equals(servico.getNome()) &
                                    dados.child("tipoPet").getValue().equals(servico.getTipoPet()) &
                                    dados.child("valor").getValue().equals(servico.getValor()) &
                                    dados.child("descricao").getValue().equals(servico.getDescricao())){
                                    Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.servico_existente));
                                    mConfirma = "1";
                                    break;
                            }
                        }
                        if(("0").equals(mConfirma)){
                            inserir(servico, idFornecedor);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //vazio
                    }
                });
    }

    @Override
    public void remover(final Servico servico, final String idFornecedor) {

        mReferenciaFirebase.child("servicos").child(servico.getId()).orderByChild(idFornecedor).
                equalTo(servico.getId()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mReferenciaFirebase.child("servicos").child(servico.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Se a remoçao foi feita com sucesso
                        if(task.isSuccessful()){
                            Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.sucesso_remocao));
                            removerServicoFornecedor(servico.getId());
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
    public void atualizar(final Servico servico, final String idFornecedor) {
        mReferenciaFirebase.child("servicos").orderByChild("idFornecedor").
                equalTo(idFornecedor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mReferenciaFirebase.child("servicos").child(servico.getId()).
                        setValue(servico).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.sucesso_atualizar_servico));
                        }else{
                            Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.erro_ao_atualizar));
                            try {
                                throw  task.getException();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(task.isComplete()){
                            //cria um "fornecedor_servico" para todos os novos servicos add
                            atualizarFornecedorServico(servico);
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


    public void compararAtualizar(final Servico servico, final String idFornecedor) {
        mReferenciaFirebase.child("servicos").orderByChild("idFornecedor").equalTo(idFornecedor).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            if (dados.child("nome").getValue().equals(servico.getNome()) &
                                    dados.child("tipoPet").getValue().equals(servico.getTipoPet()) &
                                    dados.child("valor").getValue().equals(servico.getValor()) &
                                    dados.child("descricao").getValue().equals(servico.getDescricao())){
                                    Utils.mostrarMensagemCurta(getContexto(), getContexto().getString(R.string.servico_existente));
                                    mConfirma = "1";
                                    break;
                            }
                        }
                        if (mConfirma.equals("0")){
                            atualizar(servico, idFornecedor);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //vazio
                    }
                });
    }

    /*
    */
    //Cria um nó chamadao "servico_fornecedor" que relaciona o servico ao fornecedor
    //Permitindo a busca de fornecedores pelo servico
    private void salvarFornecedorServico(Servico servico,String idServico,String idFornecedor){
        DatabaseReference ref = ConfiguracaoFirebase.getFirebase().child("servico_fornecedor").child(idServico);
        ref.push();
        Map<String, String> mapaNomes = new HashMap<>();
        String nomeServTipoPet = servico.getNome()+"_"+ servico.getTipoPet();
        mapaNomes.put("nome_tipoPet",nomeServTipoPet);
        mapaNomes.put("idFornecedor",idFornecedor);
        mapaNomes.put("valor",servico.getValor());
        mapaNomes.put("nomeFornecedor",mPreferencias.getNome());
        mapaNomes.put("servico",servico.getNome());
        mapaNomes.put("pet",servico.getTipoPet());
        mapaNomes.put("latitude",mPreferencias.getLatitude()+"");
        mapaNomes.put("longitude",mPreferencias.getLongitude()+"");
        mapaNomes.put("nota",mPreferencias.getNota()+"");
        ref.setValue(mapaNomes);
    }

    private void atualizarFornecedorServico(final Servico servico){
        ConfiguracaoFirebase.getFirebase().child("servico_fornecedor").child(servico.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                salvarFornecedorServico(servico,servico.getId(),servico.getIdFornecedor());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                assert true;
            }
        });
    }
    private void removerServicoFornecedor(String idServico){
        ConfiguracaoFirebase.getFirebase().child("servico_fornecedor").child(idServico).removeValue();

    }
    private Context getContexto(){
        return this.mContexto;
    }

}
