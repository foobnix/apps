package com.foobnix.keycode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class KeyCodeActivity extends Activity {
	private EditText list;
	private static Map<Integer, String> map = new HashMap<Integer, String>();
	
	static {
		Field[] fields = KeyEvent.class.getFields();
		for (Field field : fields) {
			try {
				String name = field.getName();
				if (name.startsWith("KEYCODE_")) {
					map.put(field.getInt(name), name);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		list = (EditText) findViewById(R.id.editText);
		// table.setOnKeyListener(this);
		((Button) findViewById(R.id.clean))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						list.setText("");
					}
				});

		((Button) findViewById(R.id.send))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						onSendEmail();
					}
				});
		((Button) findViewById(R.id.copy))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
						clipboard.setText(list.getText());
					}
				});

	}

	public void onSendEmail() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_SUBJECT, "keyCodes");
		i.putExtra(Intent.EXTRA_TEXT, list.getText());
		try {
			startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "There are no email clients installed.",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		list.setText(list.getText() + String.format("%s, (code:%s)\n", map.get(keyCode), keyCode));
		return true;
	}

	public void addRow(String name, int value) {
		addRow(name, String.valueOf(value));
	}

	public void addRow(String name, String value) {
		TableRow row = new TableRow(this);

		TextView nameView = new TextView(this);
		nameView.setText(name);

		TextView valueView = new TextView(this);
		valueView.setText(value);

		row.addView(nameView);
		row.addView(valueView);

	}
}