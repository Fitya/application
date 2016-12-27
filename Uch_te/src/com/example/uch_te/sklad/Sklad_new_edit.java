package com.example.uch_te.sklad;

import com.example.uch_te.R;
import com.example.uch_te.database.DBHelper;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Sklad_new_edit extends Activity {

	static String _id;
	EditText ET_kol, ET_naimen, ET_tip_obo, ET_mar_mod;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_sklad_new_edit);
		
		ET_kol = (EditText) findViewById(R.id.ET_kol);
		ET_naimen = (EditText) findViewById(R.id.ET_naimen);
		ET_tip_obo = (EditText) findViewById(R.id.ET_tip_obo);
		ET_mar_mod = (EditText) findViewById(R.id.ET_mar_mod);
	}
	
	public void OnClick_save(View view) {
		if (ET_kol.getText().toString().equals("")) {
			Toast myToast = Toast.makeText(this, "Укажите количество", 1000);
			myToast.show();
			return;}
		if (ET_naimen.getText().toString().equals("")) {
			Toast myToast = Toast.makeText(this, "Укажите наименование, характеристики", 1000);
			myToast.show();
			return;}
		if (ET_tip_obo.getText().toString().equals("")) {
			Toast myToast = Toast.makeText(this, "Укажите тип устройства", 1000);
			myToast.show();
			return;}
		if (ET_mar_mod.getText().toString().equals("")) {
			Toast myToast = Toast.makeText(this, "Укажите марку и модель", 1000);
			myToast.show();
			return;}
		Cursor cur_tmp = DBHelper.mDB.rawQuery("select * from reestr where _id='"+_id+"'", null);
		cur_tmp.moveToFirst();	
		if (cur_tmp.getCount()>0){
			//DBHelper.mDB.execSQL("UPDATE reestr set naimen='"+B_naimen.getText().toString()+"', marka='"+B_marka.getText().toString()+"', model='"+B_model.getText().toString()+"' where _id='"+_id+"';");
		}else{
			DBHelper.mDB.execSQL("INSERT INTO reestr (_id, kol, naimen, tip_obo, mar_mod) VALUES ('"+_id+"', '"+ET_kol.getText().toString()+"', '"+ET_naimen.getText().toString()+"', '"+ET_tip_obo.getText().toString()+"', '"+ET_mar_mod.getText().toString()+"');");
		}
		finish();
	}

}
