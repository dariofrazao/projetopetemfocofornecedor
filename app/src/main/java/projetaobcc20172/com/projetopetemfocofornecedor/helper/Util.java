package projetaobcc20172.com.projetopetemfocofornecedor.helper;

import android.app.Activity;

//Classe para travar ou destravar campos de uma activity
public class Util {
    private Activity activity;
    private int[] ids;

    public Util(Activity activity, int... ids){
        this.activity = activity;
        this.ids = ids;
    }

    public void lockFields (boolean isToLock){
        for(int id : ids){
            setLockField(id, isToLock);
        }
    }

    private void setLockField (int fieldId, boolean isToLock){
        activity.findViewById(fieldId).setEnabled(!isToLock);
    }
}
