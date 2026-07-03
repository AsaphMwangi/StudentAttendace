package com.asaphmwangi.studentattendace.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.asaphmwangi.studentattendace.R;

public class LoadingDialog {
    private Dialog dialog;

    public LoadingDialog(Context context) {
        dialog = new Dialog(context, R.style.LoadingDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
