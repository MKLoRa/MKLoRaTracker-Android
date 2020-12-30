package com.moko.loratrackerv2.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.moko.loratrackerv2.R;
import com.moko.loratrackerv2.R2;
import com.moko.loratrackerv2.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AboutActivity extends BaseActivity {
    @BindView(R2.id.app_version)
    TextView appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loratracker_activity_about);
        ButterKnife.bind(this);
        appVersion.setText(String.format("Version:V%s", Utils.getVersionInfo(this)));
    }

    @OnClick(R2.id.tv_back)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R2.id.tv_company_website)
    public void onCompanyWebsite(View view) {
        Uri uri = Uri.parse("https://" + getString(R.string.company_website));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
