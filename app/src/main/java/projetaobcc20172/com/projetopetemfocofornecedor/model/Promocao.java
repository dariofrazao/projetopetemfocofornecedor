package projetaobcc20172.com.projetopetemfocofornecedor.model;

import java.io.Serializable;

/**
 * Created by Cloves on 19/01/2018.
 */

public class Promocao implements Serializable{

    private String mId;
    private String mTitulo;
    private String mDescricao;
    private String mValor;
    private String mData;
    private String mFornecedorId;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitulo() {
        return mTitulo;
    }

    public void setTitulo(String mTitulo) {
        this.mTitulo = mTitulo;
    }

    public String getDescricao() {
        return mDescricao;
    }

    public void setDescricao(String mDescricao) {
        this.mDescricao = mDescricao;
    }

    public String getValor() {
        return mValor;
    }

    public void setValor(String mValor) {
        this.mValor = mValor;
    }

    public String getData() {
        return mData;
    }

    public void setData(String mData) {
        this.mData = mData;
    }

    public String getFornecedorId() {
        return mFornecedorId;
    }

    public void setFornecedorId(String mFornecedorId) {
        this.mFornecedorId = mFornecedorId;
    }
}
