package com.example.uch_te.sklad;

import java.util.UUID;

import com.example.uch_te.R;
import com.example.uch_te.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class Sklad_glav extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_sklad_glav);
	}
	
	public void OnClick_add(View view) {
		Sklad_new_edit._id=UUID.randomUUID().toString(); 
		startActivity(new Intent(this, Sklad_new_edit.class));
	}
	
	public void OnClick_view(View view) {
		startActivity(new Intent(this, Sklad_view.class));
	}
	
	public void OnClick_spisat(View view) {
		startActivity(new Intent(this, Sklad_spisat.class));
	}

}
