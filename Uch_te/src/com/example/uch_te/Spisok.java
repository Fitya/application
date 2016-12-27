package com.example.uch_te;

import com.example.uch_te.database.DBHelper;
import com.example.uch_te.sklad.Sklad_spisat;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Spisok extends Activity {
	
	Cursor cursor;
	SimpleCursorAdapter scAdapter;
	ListView lvData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_spisok);

	    cursor = DBHelper.mDB.rawQuery("select * from reestr", null);
	    cursor.moveToFirst();
	    String[] from = new String[] {"naimen", "kol"};
	    int[] to = new int[] { R.id.textView1, R.id.textView3};
	    scAdapter = new MyAdapter(this, R.layout.a_spisok_list, cursor, from, to);
	    lvData = (ListView) findViewById(R.id.listView1);
	    lvData.setAdapter(scAdapter);
		lvData.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
            	cursor.moveToPosition(pos);         		
            	Sklad_spisat.B_vubor.setText(cursor.getString(cursor.getColumnIndex("naimen")));
            	Sklad_spisat.vubor_id=cursor.getString(cursor.getColumnIndex("_id"));
            	finish();
            }
	    });
	}
	
	class MyAdapter extends SimpleCursorAdapter {
		  Context ctx;
		  private Cursor cursorNew;
	      LayoutInflater lInflater;
	      
		  public MyAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
			super(context, layout, c, from, to);
			// TODO Auto-generated constructor stub
		      ctx = context;
		      cursorNew=cursor;
		      lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		  
		  public View getView(final int position, View  view, ViewGroup  parent) {
		        
	            if (view == null) {
	                view = lInflater.inflate(R.layout.a_spisok_list, parent, false);
	            }
	            TextView textView2 =(TextView) view.findViewById(R.id.textView2);
	            cursorNew.moveToPosition(position);
	            textView2.setText("Остаток: "+cursorNew.getString(cursorNew.getColumnIndex("kol")));
          	
		        return super.getView(position, view, parent);
		    }
	}

}
