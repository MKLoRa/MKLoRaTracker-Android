package com.moko.loratrackerv2.dialog;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.moko.loratrackerv2.R;
import com.moko.loratrackerv2.R2;
import com.moko.loratrackerv2.view.ProgressDrawable;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingDialog extends MokoBaseDialog {

    public static final String TAG = LoadingDialog.class.getSimpleName();
    @BindView(R2.id.iv_loading)
    ImageView ivLoading;

    @Override
    public int getLayoutRes() {
        return R.layout.loratracker_dialog_loading;
    }

    @Override
    public void bindView(View v) {
        ButterKnife.bind(this, v);
        ProgressDrawable progressDrawable = new ProgressDrawable();
        progressDrawable.setColor(ContextCompat.getColor(getContext(), R.color.loratracker_text_black_4d4d4d));
        ivLoading.setImageDrawable(progressDrawable);
        progressDrawable.start();
    }


    @Override
    public int getDialogStyle() {
        return R.style.LoraTrackerCenterDialog;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public float getDimAmount() {
        return 0.7f;
    }

    @Override
    public boolean getCancelOutside() {
        return false;
    }

    @Override
    public boolean getCancellable() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ProgressDrawable) ivLoading.getDrawable()).stop();
    }
}
