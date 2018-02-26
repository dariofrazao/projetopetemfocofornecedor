package projetaobcc20172.com.projetopetemfocofornecedor.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Cloves on 19/01/2018.
 */

public class Promocao implements Serializable{

    private String mId;
    private String mTitulo;
    private String mDescricao;
    private String mValor;
    private List<Date> mDatas;
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

    public List<Date> getDatas() {
        return mDatas;
    }

    public void setDatas(List<Date> mData) {
        this.mDatas = mData;
    }

    public String getFornecedorId() {
        return mFornecedorId;
    }

    public void setFornecedorId(String mFornecedorId) {
        this.mFornecedorId = mFornecedorId;
    }
}
