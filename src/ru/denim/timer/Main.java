package ru.denim.timer;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity implements View.OnClickListener {

	int minutes;
	Button minute1, minute10, minute30;
	Button start, clear;
	TextView timer;
	boolean working = false;
	MediaPlayer mediaPlayer = null;

	CountDownTimer brewCountDownTimer;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		minutes = 0;

		minute1 = (Button) findViewById(R.id.b1minute);
		minute1.setOnClickListener(this);

		minute10 = (Button) findViewById(R.id.b10minutes);
		minute10.setOnClickListener(this);

		minute30 = (Button) findViewById(R.id.b30minutes);
		minute30.setOnClickListener(this);

		start = (Button) findViewById(R.id.bStart);
		start.setOnClickListener(this);
		start.setEnabled(false);

		clear = (Button) findViewById(R.id.bClear);
		clear.setOnClickListener(this);
		clear.setEnabled(false);

		timer = (TextView) findViewById(R.id.tvDisplay);

		mediaPlayer = MediaPlayer.create(Main.this, R.raw.alarm);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.b1minute:
			minutes++;
			timer.setText(convertTime(getString(R.string.time_prefix), minutes));
			start.setEnabled(true);
			clear.setEnabled(true);
			break;
		case R.id.b10minutes:
			minutes += 10;
			timer.setText(convertTime(getString(R.string.time_prefix), minutes));
			start.setEnabled(true);
			clear.setEnabled(true);
			break;
		case R.id.b30minutes:
			minutes += 30;
			timer.setText(convertTime(getString(R.string.time_prefix), minutes));
			start.setEnabled(true);
			clear.setEnabled(true);
			break;
		case R.id.bStart:

			if (minutes == 0)
				break;

			if (!working) {

				// click START
				brewCountDownTimer = new CountDownTimer(minutes * 60 * 1000,
						1000) {
					@Override
					public void onTick(long millisUntilFinished) {
						timer.setText(convertTime(
								getString(R.string.time_to_cook_prefix),
								Integer.valueOf(Long
										.toString(millisUntilFinished / 1000 / 60))));
					}

					@Override
					public void onFinish() {
						timer.setText(R.string.ready);
						mediaPlayer.start();
						
						Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						long[] pattern = { 0, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500 };
						
						v.vibrate(pattern, -1);
						
						working = false;
						minutes = 0;
						start.setText(R.string.start_timer);
						start.setEnabled(false);
						clear.setEnabled(false);
						minute1.setEnabled(true);
						minute10.setEnabled(true);
						minute30.setEnabled(true);
					}
				};
				brewCountDownTimer.start();
				working = true;
				start.setText(R.string.stop_timer);
				minute1.setEnabled(false);
				minute10.setEnabled(false);
				minute30.setEnabled(false);
				clear.setEnabled(false);
			} else {

				// click STOP
				brewCountDownTimer.cancel();
				working = false;
				start.setText(R.string.start_timer);
				minutes = 0;
				start.setEnabled(false);
				clear.setEnabled(false);
				minute1.setEnabled(true);
				minute10.setEnabled(true);
				minute30.setEnabled(true);

				timer.setText(R.string.init_time_label);
				mediaPlayer.stop();
			}
			break;
		case R.id.bClear:
			minutes = 0;
			timer.setText(R.string.init_time_label);
			start.setEnabled(false);
			clear.setEnabled(false);
			minute1.setEnabled(true);
			minute10.setEnabled(true);
			minute30.setEnabled(true);
			break;
		}

	}

	private String convertTime(String prefix, int minutes) {
		if (working && minutes <= 1)
			return getString(R.string.almost_ready);
		else if (minutes < 59)
			return prefix + " " + Integer.toString(minutes) + " "
					+ getString(R.string.minutes);
		else if (minutes % 60 == 0)
			return prefix + " " + Integer.toString(minutes / 60) + " "
					+ getString(R.string.hours) + " ";
		else
			return prefix + " " + Integer.toString(minutes / 60) + " "
					+ getString(R.string.hours) + " "
					+ Integer.toString(minutes % 60) + " "
					+ getString(R.string.minutes);
	}
}
