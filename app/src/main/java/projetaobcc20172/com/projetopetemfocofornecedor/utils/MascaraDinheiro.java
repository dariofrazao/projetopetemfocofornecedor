package projetaobcc20172.com.projetopetemfocofornecedor.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Classe resposável por mascarar um editText com um valor em dinheiro
 * Created by Felipe Oliveira on 05/12/17.
 */

public class MascaraDinheiro implements TextWatcher{
    private final WeakReference<EditText> mEditTextWeakReference;
    private final Locale mLocal;

    public MascaraDinheiro(EditText editText, Locale local) {
        this.mEditTextWeakReference = new WeakReference<>(editText);
        this.mLocal = (local != null ? local :Locale.getDefault());
    }

    public MascaraDinheiro(EditText editText) {
        this.mEditTextWeakReference = new WeakReference<>(editText);
        this.mLocal = Locale.getDefault();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // alterações antes do texto mudar
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // alterações quando o texto esta mudando
    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = mEditTextWeakReference.get();
        if (editText == null) return;
        editText.removeTextChangedListener(this);

        BigDecimal parsed = formatarParaBigDecimal(editable.toString(), mLocal);
        String formatted = NumberFormat.getCurrencyInstance(mLocal).format(parsed);

        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }

    private BigDecimal formatarParaBigDecimal(String value, Locale locale) {
        String stringFormatada = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());

        String stringLimpa = value.replaceAll(stringFormatada, "");

        return new BigDecimal(stringLimpa).setScale(
                2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR
        );
    }

    /**
     * Remove a mascara de dinheiro.
     * @param editText componente onde o texto foi digitado
     * @return {String} sem o prefixo que identifica a moeda, pronta para ser formatada para Double.
     */
    public static String removerMascara(EditText editText){
        if(editText.getText().toString().equals("")) {
            return "0";
        }
        return editText.getText().toString().replace("R$", "")
                .replace(".", "").replace(",", ".");
    }
}
