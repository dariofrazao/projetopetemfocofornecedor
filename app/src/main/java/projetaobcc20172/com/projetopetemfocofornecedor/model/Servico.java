package projetaobcc20172.com.projetopetemfocofornecedor.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * Created by Felipe Oliveira on 05/12/17.
 *
 */

public class Servico implements Serializable{
    private String mId;
    private String mNome;
    private String mDescricao;
    private String mValor;
    private String mTipoPet;
    private String mIdFornecedor;

    public Servico() {}

    public Servico(String nome, String valor,String tipoPet, String descricao){
        this.mNome = nome;
        this.mValor = valor;
        this.mTipoPet = tipoPet;
        this.mDescricao = descricao;
    }

    @Exclude
    public String getId() {
        return mId;
    }

    public String getNome() {
        return mNome;
    }

    public void setNome(String nome) {
        this.mNome = nome;
    }

    public String getDescricao() {
        return mDescricao;
    }

    public void setDescricao(String descricao) {
        this.mDescricao = descricao;
    }

    public String getValor() {
        return mValor;
    }

    public void setValor(String Valor) {
        this.mValor = Valor;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Servico){
            Servico servico = (Servico) o;
            if( servico.getDescricao().equalsIgnoreCase(this.getDescricao())
                    && servico.getNome().equalsIgnoreCase(this.getNome())
                    && servico.getValor().equalsIgnoreCase(this.getValor())
                    && servico.getTipoPet().equalsIgnoreCase(this.getTipoPet())){
                return true;
            }
        }
        return false;
    }

    public String getTipoPet() {
        return mTipoPet;
    }

    public void setTipoPet(String mTipoPet) {
        this.mTipoPet = mTipoPet;
    }

    public void setIdFornecedor(String idFornecedor){
        this.mIdFornecedor = idFornecedor;
    }

    public String getIdFornecedor(){
        return this.mIdFornecedor;
    }
}
