package com.example.uch_te;

import java.io.File;

import com.example.uch_te.database.DBHelper;
import com.example.uch_te.sklad.Sklad_glav;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		File folder2 = new File(Environment.getExternalStorageDirectory().getPath() + "/uch_te/Temp");
		boolean success2 = true;
		if (!folder2.exists()) {
			success2 = folder2.mkdir();
		}
		
		DBHelper myDbHelper = new DBHelper(this);
		try {
			myDbHelper.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}
	}
	
	public void OnClick_sklad(View view) {
		startActivity(new Intent(this, Sklad_glav.class));
	}
	
	public void OnClick_act(View view) {
		startActivity(new Intent(this, Act.class));
	}

}
