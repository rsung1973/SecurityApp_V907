package com.dnake.security;

import com.dnake.security.activity.IpcActivity;
import com.dnake.security.activity.PasswordActivity;
import com.dnake.security.activity.SceneActivity;
import com.dnake.security.activity.ZoneActivity;
import com.dnake.security.utils.NavigationBarUtil;
import com.dnake.v700.login;
import com.dnake.v700.security;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

@SuppressLint("NewApi")
public class MainActivity extends BaseLabel {

	private MainActivity ctx;
    private TextView[] loopItem = new TextView[8];
    private TextView[] securityItem = new TextView[8];
    private Thread refresher;
    private ImageView btnBack;

	@Override
	public void onResume() {
		super.onResume();
		
        security.invokeIO(false);
		freshSceneInfo();
	}

	private void freshSceneInfo() {
		if (security.zone != null) {
			showSceneInfo();
		}
	}

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
        setContentView(R.layout.main);
        ctx = this;
        layoutMain = (FrameLayout) findViewById(R.id.layout_main);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        LinearLayout btn;
        btn = (LinearLayout) this.findViewById(R.id.main_btn_defence);
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (security.defence == 0 || login.ok()) {
                    Intent intent = new Intent(MainActivity.this, DefenceLabel.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent i = new Intent(MainActivity.this, DefenceLabel.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    LoginLabel login = new LoginLabel();
                    login.start(MainActivity.this, i);
                }
            }
        });
        btn = (LinearLayout) this.findViewById(R.id.main_btn_ipc);
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, IpcLabel.class);
                Intent intent = new Intent(MainActivity.this, IpcActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btn = (LinearLayout) this.findViewById(R.id.main_btn_zone);
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (security.defence == 0) {
                    if (login.ok()) {
//						Intent intent = new Intent(MainActivity.this, ZoneLabel.class);
                        Intent intent = new Intent(MainActivity.this, ZoneActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
//						Intent i = new Intent(MainActivity.this, ZoneLabel.class);
                        Intent i = new Intent(MainActivity.this, ZoneActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        LoginLabel login = new LoginLabel();
                        login.start(MainActivity.this, i);
                    }
                } else {
                    Builder b = new AlertDialog.Builder(ctx);
                    b.setTitle(R.string.main_prompt_title);
                    b.setMessage(R.string.main_prompt_defence);
                    b.setPositiveButton(R.string.main_prompt_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                        }
                    });
                    AlertDialog tmp = b.show();
                    NavigationBarUtil.hideNavigationBar(tmp.getWindow());
                    NavigationBarUtil.clearFocusNotAle(tmp.getWindow());
                }
            }
        });
        btn = (LinearLayout) this.findViewById(R.id.main_btn_scene);
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (security.defence == 0) {
                    if (login.ok()) {
//						Intent intent = new Intent(MainActivity.this, SceneLabel.class);
                        Intent intent = new Intent(MainActivity.this, SceneActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
//						Intent i = new Intent(MainActivity.this, SceneLabel.class);
                        Intent i = new Intent(MainActivity.this, SceneActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        LoginLabel login = new LoginLabel();
                        login.start(MainActivity.this, i);
                    }
                } else {
                    Builder b = new AlertDialog.Builder(ctx);
                    b.setTitle(R.string.main_prompt_title);
                    b.setMessage(R.string.main_prompt_defence);
                    b.setPositiveButton(R.string.main_prompt_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                        }
                    });
                    AlertDialog tmp = b.show();
                    NavigationBarUtil.hideNavigationBar(tmp.getWindow());
                    NavigationBarUtil.clearFocusNotAle(tmp.getWindow());
                }
            }
        });

        btn = (LinearLayout) this.findViewById(R.id.main_btn_setup);
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//				Intent intent = new Intent(MainActivity.this, SetupLabel.class);
                Intent intent = new Intent(MainActivity.this, PasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

		for (int i = 0; i < 8; i++)
		{
			loopItem[i] = (TextView) this.findViewById(R.id.scene_item_m0_0 + i);
            securityItem[i] = (TextView) this.findViewById(R.id.scene_item_m1_0 + i);
		}

        if (security.mContext == null) {
            Intent intent = new Intent(this, security.class);
            this.startService(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        TextView t = (TextView) this.findViewById(R.id.main_text_status);
        switch (security.defence) {
            case 0:
                t.setText(R.string.main_text_withdraw);
                break;
            case 1:
                t.setText(R.string.main_text_out);
                break;
            case 2:
                t.setText(R.string.main_text_home);
                break;
            case 3:
                t.setText(R.string.main_text_sleep);
                break;
        }

		final Handler handler = new Handler();
		refresher = new Thread(new Runnable() {
			@Override
			public void run() {
				while (refresher != null) {
					try {
						Thread.sleep(5000);
					} catch (Exception ex) {
//						Log.e("security", ex.getMessage());
					}
					handler.post(new Runnable() {
						@Override
						public void run() {
							ctx.freshSceneInfo();
						}
					});
				}
			}
		});
		refresher.start();
	}

	@Override
	public void onStop() {
		super.onStop();
		refresher = null;
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
