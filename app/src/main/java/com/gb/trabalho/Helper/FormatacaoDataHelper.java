package com.gb.trabalho.Helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class FormatacaoDataHelper implements TextWatcher {

    private final EditText editText;
    private boolean isUpdating;

    public FormatacaoDataHelper(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        if (isUpdating) return;

        String str = s.toString().replaceAll("[^\\d]", "");
        StringBuilder formatted = new StringBuilder();

        isUpdating = true;

        int length = str.length();
        if (length > 2) {
            formatted.append(str.substring(0, 2)).append("/");
            if (length > 4) {
                formatted.append(str.substring(2, 4)).append("/");
                if (length > 8) length = 8;
                if (length > 4) formatted.append(str.substring(4, length));
            } else {
                formatted.append(str.substring(2));
            }
        } else {
            formatted.append(str);
        }

        editText.setText(formatted.toString());
        editText.setSelection(editText.getText().length());
        isUpdating = false;
    }
}