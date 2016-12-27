package com.example.uch_te.sklad;

import java.util.UUID;

import com.example.uch_te.R;
import com.example.uch_te.Spisok;
import com.example.uch_te.database.DBHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Sklad_spisat extends Activity {

	public static String vubor_id, tip_osn;
	EditText ET_kol;
	public static Button B_vubor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_sklad_spisat);
		
		ET_kol = (EditText) findViewById(R.id.ET_kol);
		B_vubor = (Button) findViewById(R.id.B_vubor);
		
		String[] items = new String[] { "Вышел амортизационный срок", "Прием-передача выполненных работ"};
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				tip_osn = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});
	}
	
	public void OnClick_vubor(View view) {
		final String[] option = new String[] { "Выбрать из списка", "Найти при помощи QR код" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, option);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Выберите опцию");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					callspisok();
				}
				if (which == 1) {
					callskaner();
				}
			}
		});
		final AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public void callspisok() {
		startActivity(new Intent(this, Spisok.class));
	}

	public void callskaner() {
		try {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
			startActivityForResult(intent, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "ERROR:" + e, 1).show();
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			    Cursor cursor = DBHelper.mDB.rawQuery("select * from reestr where _id='"+intent.getStringExtra("SCAN_RESULT")+"'", null);
			    cursor.moveToFirst();
			    if (cursor.getCount()>0){
            	B_vubor.setText(cursor.getString(cursor.getColumnIndex("naimen")));
            	vubor_id=cursor.getString(cursor.getColumnIndex("_id"));
			    }else{
			    	Toast myToast = Toast.makeText(this, "Ничего не найдено", 1000);
			    }

			} else if (resultCode == RESULT_CANCELED) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

			}
		}
	}

	
	public void OnClick_save(View view) {
		if (ET_kol.getText().toString().equals("")) {
			Toast myToast = Toast.makeText(this, "Укажите наименование", 1000);
			myToast.show();
			return;}
		if (B_vubor.getText().toString().equals("")) {
			Toast myToast = Toast.makeText(this, "Укажите количество", 1000);
			myToast.show();
			return;}
	    Cursor cursor = DBHelper.mDB.rawQuery("select * from reestr where _id='"+vubor_id+"'", null);
	    cursor.moveToFirst();
		if (cursor.getInt(cursor.getColumnIndex("kol"))<Integer.valueOf(ET_kol.getText().toString())) {
			Toast myToast = Toast.makeText(this, "На складе нет такого количества", 1000);
			myToast.show();
			return;}
		DBHelper.mDB.execSQL("UPDATE reestr set kol='"+String.valueOf(cursor.getInt(cursor.getColumnIndex("kol"))-Integer.valueOf(ET_kol.getText().toString()))+"' where _id='"+vubor_id+"';");
		DBHelper.mDB.execSQL("INSERT INTO act (_id, osnovanie, naimen, kol) VALUES ('"+UUID.randomUUID().toString()+"', '"+tip_osn+"', '"+B_vubor.getText().toString()+"', '"+ET_kol.getText().toString()+"');");
		finish();
	}

}
