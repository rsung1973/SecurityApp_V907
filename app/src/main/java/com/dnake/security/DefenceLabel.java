package com.dnake.security;

import com.dnake.handler.DefenceHelper;
import com.dnake.v700.security;
import com.dnake.v700.slaves;
import com.dnake.widget.Button2;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DefenceLabel extends BaseLabel {
    private ImageView btnBack;
    private ImageView iv_out, iv_home, iv_sleep, iv_withdraw;
    private LinearLayout btn_out, btn_home, btn_sleep, btn_withdraw;
	private boolean available = false;
	private TextView[] loopItem = new TextView[8];
	private TextView[] securityItem = new TextView[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        this.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                hideBottomUIMenu();
            }
        });
        setContentView(R.layout.defence);
        layoutMain = (FrameLayout) findViewById(R.id.layout_main);
        iv_out = (ImageView) findViewById(R.id.iv_out);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_sleep = (ImageView) findViewById(R.id.iv_sleep);
        iv_withdraw = (ImageView) findViewById(R.id.iv_withdraw);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        btn_out = (LinearLayout) this.findViewById(R.id.defence_btn_out);
        btn_out.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
				if(DefenceHelper.setDefence(security.OUT)) {
					load_st();
				}
/*
                security.setDefence(security.OUT);
                slaves.setMarks(0x01);
                load_st();
*/
            }
        });
        btn_home = (LinearLayout) this.findViewById(R.id.defence_btn_home);
        btn_home.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
				if(DefenceHelper.setDefence(security.HOME)) {
					load_st();
				}
/*
                security.setDefence(security.HOME);
                slaves.setMarks(0x01);
                load_st();
*/
            }
        });
        btn_sleep = (LinearLayout) this.findViewById(R.id.defence_btn_sleep);
        btn_sleep.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
				if(DefenceHelper.setDefence(security.SLEEP)) {
					load_st();
				}
/*
                security.setDefence(security.SLEEP);
                slaves.setMarks(0x01);
                load_st();
*/
            }
        });
        btn_withdraw = (LinearLayout) this.findViewById(R.id.defence_btn_withdraw);
        btn_withdraw.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
				DefenceHelper.setWithdraw();
/*
                security.setDefence(security.WITHDRAW);
                slaves.setMarks(0x01);
*/
                load_st();
            }
        });

		for (int i = 0; i < 8; i++)
		{
			loopItem[i] = (TextView) this.findViewById(R.id.defence_item_m0_0 + i);
			securityItem[i] = (TextView) this.findViewById(R.id.defence_item_m1_0 + i);
		}

    }

    @Override
    public void onStart() {
        super.onStart();
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        this.load_st();
    }

    public void load_st() {
        iv_out.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_fq_out_off));
        iv_home.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_fq_home_off));
        iv_sleep.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_fq_sleep_off));
        iv_withdraw.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_fq_off_off));

        switch (security.defence) {
            case 0:
                iv_withdraw.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_fq_off));
                break;
            case 1:
                iv_out.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_fq_out));
                break;
            case 2:
                iv_home.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_fq_home));
                break;
            case 3:
                iv_sleep.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_fq_sleep));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

		security.invokeIO(false);
		if (security.zone != null) {
			showSceneInfo();
		}

        TextView t = (TextView) this.findViewById(R.id.loopAlarm);
		TextView a = (TextView) this.findViewById(R.id.delayAlarm);
        available = security.checkSecurity();
        if(available) {
			t.setVisibility(View.INVISIBLE);
			a.setVisibility(View.INVISIBLE);
        } else {
            t.setVisibility(View.VISIBLE);
            if(security.timeout>0) {
            	available=true;
				a.setVisibility(View.VISIBLE);
                a.setText("當保全設定完，將延時 " + security.timeout + " 秒後啟動!!");
			}
        }
    }

	private void showSceneInfo() {
		CharSequence[] mode = this.getResources().getTextArray(R.array.zone_mode_arrays);

		for (int i = 0; i < 8; i++) {
			if (loopItem[i] == null || securityItem[i] == null || security.zone[i] == null)
				continue;
			if(security.zone[i].currentStatus >= 0) {
				loopItem[i].setText(mode[security.zone[i].currentStatus]);
			} else {
				loopItem[i].setText("N/A");
			}
			securityItem[i].setText(mode[security.zone[i].mode]);
			if (security.zone[i].currentStatus != security.zone[i].mode
					&& security.zone[i].mode != security.M_BELL) {
				loopItem[i].setBackgroundColor(Color.argb(255, 255, 0, 0));
				securityItem[i].setBackgroundColor(Color.argb(255, 255, 0, 0));
			} else {
				loopItem[i].setBackgroundColor(Color.argb(0, 0, 0, 0));
				securityItem[i].setBackgroundColor(Color.argb(0, 0, 0, 0));
			}
		}
	}
}
