package com.dnake.security;

import com.dnake.v700.security;
import com.dnake.v700.slaves;
import com.dnake.widget.Button2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class SceneLabel extends BaseLabel {
    private TextView mText[] = new TextView[8];
    private CheckBox cb_out[] = new CheckBox[8];
    private CheckBox cb_home[] = new CheckBox[8];
    private CheckBox cb_sleep[] = new CheckBox[8];
    private int mOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scene);

        for (int i = 0; i < 8; i++) {
            mText[i] = (TextView) this.findViewById(R.id.scene_text_0 + i);
            cb_out[i] = (CheckBox) this.findViewById(R.id.scene_m0_0 + i);
            cb_home[i] = (CheckBox) this.findViewById(R.id.scene_m1_0 + i);
            cb_sleep[i] = (CheckBox) this.findViewById(R.id.scene_m2_0 + i);
        }

        Button2 b = (Button2) this.findViewById(R.id.scene_prev);
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                save_st();
                mOffset = 0;
                load_st();
            }
        });

        b = (Button2) this.findViewById(R.id.scene_next);
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                save_st();
                mOffset = 8;
                load_st();
            }
        });

        Spinner sp = (Spinner) this.findViewById(R.id.scene_timeout);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.scene_timeout_arrays, R.layout.spinner_text2);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(ad);
        if (security.timeout == 30)
            sp.setSelection(1);
        else if (security.timeout == 40)
            sp.setSelection(2);
        else if (security.timeout == 60)
            sp.setSelection(3);
        else if (security.timeout == 100)
            sp.setSelection(4);
        else if (security.timeout == 300)
            sp.setSelection(5);
        else
            sp.setSelection(0);
    }

    public void load_st() {
        for (int i = 0; i < 8; i++) {
            String s;
            s = String.format("%d", mOffset + i + 1);
            mText[i].setText(s);
            cb_out[i].setChecked(security.zone[mOffset + i].scene[0] != 0 ? true : false);
            cb_home[i].setChecked(security.zone[mOffset + i].scene[1] != 0 ? true : false);
            cb_sleep[i].setChecked(security.zone[mOffset + i].scene[2] != 0 ? true : false);
        }
    }

    public void save_st() {
        for (int i = 0; i < 8; i++) {
            security.zone[mOffset + i].scene[0] = cb_out[i].isChecked() ? 1 : 0;
            security.zone[mOffset + i].scene[1] = cb_home[i].isChecked() ? 1 : 0;
            security.zone[mOffset + i].scene[2] = cb_sleep[i].isChecked() ? 1 : 0;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.load_st();
    }

    @Override
    public void onStop() {
        super.onStop();

        Spinner sp = (Spinner) this.findViewById(R.id.scene_timeout);
        int s = sp.getSelectedItemPosition();
        switch (s) {
            case 0:
                security.timeout = 0;
                break;
            case 1:
                security.timeout = 30;
                break;
            case 2:
                security.timeout = 40;
                break;
            case 3:
                security.timeout = 60;
                break;
            case 4:
                security.timeout = 100;
                break;
            case 5:
                security.timeout = 300;
                break;
            default:
                security.timeout = 100;
                break;
        }
        this.save_st();
        security.save();
        slaves.setMarks(0x01);
    }
}
