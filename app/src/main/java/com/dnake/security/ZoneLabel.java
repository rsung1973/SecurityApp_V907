package com.dnake.security;

import com.dnake.v700.security;
import com.dnake.v700.slaves;
import com.dnake.widget.Button2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class ZoneLabel extends BaseLabel {
	private TextView [] mText = new TextView[8];
	private Spinner [] sp_type = new Spinner[8];
	private Spinner [] sp_delay = new Spinner[8];
	private Spinner [] sp_sensor = new Spinner[8];
	private Spinner [] sp_mode = new Spinner[8];

	private int mOffset = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zone);

		ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.zone_type_arrays, R.layout.spinner_text);
		ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<CharSequence> ad2 = ArrayAdapter.createFromResource(this, R.array.zone_delay_arrays, R.layout.spinner_text);
		ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<CharSequence> ad3 = ArrayAdapter.createFromResource(this, R.array.zone_sensor_arrays, R.layout.spinner_text);
		ad3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<CharSequence> ad4 = ArrayAdapter.createFromResource(this, R.array.zone_mode_arrays, R.layout.spinner_text);
		ad4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		for(int i=0; i<8; i++) {
			mText[i] = (TextView)this.findViewById(R.id.zone_text_0+i*5);
			sp_type[i] = (Spinner)this.findViewById(R.id.zone_type_0+i*5);
			sp_delay[i] = (Spinner)this.findViewById(R.id.zone_delay_0+i*5);
			sp_sensor[i] = (Spinner)this.findViewById(R.id.zone_sensor_0+i*5);
			sp_mode[i] = (Spinner)this.findViewById(R.id.zone_mode_0+i*5);

			sp_type[i].setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
					int z = 0;
					for(int i=0; i<8; i++) {
						if (arg0.getId() == sp_type[i].getId()) {
							z = i;
							break;
						}
					}
					if (pos > 0) {
						sp_delay[z].setSelection(0);
						sp_delay[z].setEnabled(false);
					} else {
						sp_delay[z].setEnabled(true);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
			sp_type[i].setAdapter(ad);
			sp_delay[i].setAdapter(ad2);
			sp_sensor[i].setAdapter(ad3);
			sp_mode[i].setAdapter(ad4);
		}
		this.loadZone();

		Button2 b = (Button2) this.findViewById(R.id.zone_prev);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				saveZone();
				mOffset = 0;
				loadZone();
			}
		});

		b = (Button2) this.findViewById(R.id.zone_next);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				saveZone();
				mOffset = 8;
				loadZone();
			}
		});
	}

	@Override
	public void onStop() {
		super.onStop();

		this.saveZone();
		security.save();
		slaves.setMarks(0x01);
	}

	public void loadZone() {
		for(int i=0; i<8; i++) {
			String s;
			s = String.format("%d", mOffset+i+1);
			mText[i].setText(s);
			sp_type[i].setSelection(security.zone[mOffset+i].type);
			sp_delay[i].setSelection(security.zone[mOffset+i].delay);
			sp_sensor[i].setSelection(security.zone[mOffset+i].sensor);
			sp_mode[i].setSelection(security.zone[mOffset+i].mode);
		}
	}

	public void saveZone() {
		for(int i=0; i<8; i++) {
			security.zone[mOffset+i].type = sp_type[i].getSelectedItemPosition();
			security.zone[mOffset+i].delay = sp_delay[i].getSelectedItemPosition();
			security.zone[mOffset+i].sensor = sp_sensor[i].getSelectedItemPosition();
			security.zone[mOffset+i].mode = sp_mode[i].getSelectedItemPosition();
		}
	}
}
