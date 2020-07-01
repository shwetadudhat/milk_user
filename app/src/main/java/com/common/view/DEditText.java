package com.common.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class DEditText extends AppCompatEditText {

    public DEditText(Context context) {
        super(context);
    }

    public DEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TextViewHelper.setTypeface(context, this, attrs);
    }

    public long getLong() {

        return length() > 0 ? Long.parseLong(getText().toString()) : 0;
    }

    public int getInt() {

        return length() > 0 ? Integer.parseInt(getText().toString().trim()) : 0;
    }

    public String getString() {

        return length() > 0 ? getText().toString().trim() : "";
    }

    public void setText(long data) {
        if (data != 0) {
            setText("" + data);
        } else {
            setText("0");
        }
    }

    public void setText(long data, long def) {
        if (data != 0) {
            setText("" + data);
        } else {
            setText("" + def);
        }
    }

    public void setText(String data) {
        if (data != null && !data.equalsIgnoreCase("")) {
            super.setText(data);
        } else {
            super.setText("");
        }
    }

    public void setText(String data, String def) {
        if (data != null && !data.equalsIgnoreCase("")) {
            setText(data);
        } else {
            setText(def);
        }
    }
}
