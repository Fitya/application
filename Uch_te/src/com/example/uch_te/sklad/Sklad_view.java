package com.example.uch_te.sklad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.example.uch_te.R;
import com.example.uch_te.database.DBHelper;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BarcodePDF417;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;

public class Sklad_view extends Activity {
	
	Cursor cur_spr;
	public TableLayout tableList;
	public ProgressDialog pDialog;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_sklad_view);
		context=Sklad_view.this;
		tableList = (TableLayout) findViewById(R.id.tableList);
        tableList.removeAllViews();
        
        TableRow tableRow1 = new TableRow(this);
        tableRow1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
        TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        llp.setMargins(2, 2, 2, 2);
        
        TextView txtView1 = new TextView(this);
        txtView1.setText("Наименование, характеристики");
        txtView1.setPadding(5, 5, 5, 5);
        txtView1.setTextColor(Color.BLACK);
        txtView1.setBackgroundColor(Color.WHITE);
        
        TextView txtView2 = new TextView(this);
        txtView2.setText("Тип устройства");
        txtView2.setPadding(5, 5, 5, 5);
        txtView2.setTextColor(Color.BLACK);
        txtView2.setBackgroundColor(Color.WHITE);
        
        TextView txtView3 = new TextView(this);
        txtView3.setText("Марка и модель");
        txtView3.setPadding(5, 5, 5, 5);
        txtView3.setTextColor(Color.BLACK);
        txtView3.setBackgroundColor(Color.WHITE);
        
        TextView txtView4 = new TextView(this);
        txtView4.setText("Количество");
        txtView4.setPadding(5, 5, 5, 5);
        txtView4.setTextColor(Color.BLACK);
        txtView4.setBackgroundColor(Color.WHITE);
        
        tableRow1.addView(txtView1, llp);
        tableRow1.addView(txtView2, llp);
        tableRow1.addView(txtView3, llp);
        tableRow1.addView(txtView4, llp);
        tableList.addView(tableRow1);
        
		cur_spr = DBHelper.mDB.rawQuery("select * from reestr", null);
		cur_spr.moveToFirst();
		while (!cur_spr.isAfterLast())
        {
            tableRow1 = new TableRow(this);
            tableRow1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    		
            txtView1 = new TextView(this);
            txtView1.setText(cur_spr.getString(cur_spr.getColumnIndex("naimen")));
            txtView1.setPadding(5, 5, 5, 5);
            txtView1.setTextColor(Color.BLACK);
            txtView1.setBackgroundColor(Color.WHITE);
            
            txtView2 = new TextView(this);
            txtView2.setText(cur_spr.getString(cur_spr.getColumnIndex("tip_obo")));
            txtView2.setPadding(5, 5, 5, 5);
            txtView2.setTextColor(Color.BLACK);
            txtView2.setBackgroundColor(Color.WHITE);
            
            txtView3 = new TextView(this);
            txtView3.setText(cur_spr.getString(cur_spr.getColumnIndex("mar_mod")));
            txtView3.setPadding(5, 5, 5, 5);
            txtView3.setTextColor(Color.BLACK);
            txtView3.setBackgroundColor(Color.WHITE);
            
            txtView4 = new TextView(this);
            txtView4.setText(cur_spr.getString(cur_spr.getColumnIndex("kol")));
            txtView4.setPadding(5, 5, 5, 5);
            txtView4.setTextColor(Color.BLACK);
            txtView4.setBackgroundColor(Color.WHITE);
            
            tableRow1.addView(txtView1, llp);
            tableRow1.addView(txtView2, llp);
            tableRow1.addView(txtView3, llp);
            tableRow1.addView(txtView4, llp);
            tableList.addView(tableRow1);
            cur_spr.moveToNext();
        }
        tableList.invalidate();      
	}
	
	public void OnClick_sklad(View view) {
		new postupleniya().execute(); 
	}

	public class postupleniya extends AsyncTask<String, String, String> {		
		@Override
		protected void onPreExecute() {	    
			pDialog = new ProgressDialog(context);
		    pDialog.setMessage("Загрузка данных");
		    pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... params){						
			String nameOfProcess = "com.adobe.reader";
		    ActivityManager  manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		    List<ActivityManager.RunningAppProcessInfo> listOfProcesses = manager.getRunningAppProcesses();
		    for (ActivityManager.RunningAppProcessInfo process : listOfProcesses){
		        if (process.processName.contains(nameOfProcess)){
		            android.os.Process.killProcess(process.pid);
		            android.os.Process.sendSignal(process.pid, android.os.Process.SIGNAL_KILL);
		            manager.killBackgroundProcesses(process.processName);
		            break;
		        }
		    }
		    
		    String filename = Environment.getExternalStorageDirectory().getPath() + "/uch_te/Temp/sklad.pdf";
		    Document document = new Document(PageSize.A4);
			try {
				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        document.open();
	        BaseFont bfComic = null;
			try {
				bfComic = BaseFont.createFont("assets/fonts/comic.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Font FRONT_text = new Font(bfComic, 10);
			Font FRONT_zago = new Font(bfComic, 14);
			
			PdfPTable table_glav = new PdfPTable(5);
			table_glav.setWidthPercentage (100);
			float[] columnWidths = {20, 35, 15, 15, 15};
			try {
				table_glav.setWidths (columnWidths);
			} catch (DocumentException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			PdfPCell cell1_1 = new PdfPCell(new Paragraph("QR код", FRONT_zago));
			table_glav.addCell(cell1_1);
			cell1_1 = new PdfPCell(new Paragraph("Наименование, характеристики", FRONT_zago));
			table_glav.addCell(cell1_1);
			cell1_1 = new PdfPCell(new Paragraph("Тип устройства", FRONT_zago));
			table_glav.addCell(cell1_1);
			cell1_1 = new PdfPCell(new Paragraph("Марка и модель", FRONT_zago));
			table_glav.addCell(cell1_1);
			cell1_1 = new PdfPCell(new Paragraph("Кол-во", FRONT_zago));
			table_glav.addCell(cell1_1);
			Image img = null;
			cur_spr = DBHelper.mDB.rawQuery("select * from reestr", null);
			cur_spr.moveToFirst();
			while (!cur_spr.isAfterLast())
	        {
		        BarcodePDF417 pdf417 = new BarcodePDF417();
		        
				try {
					img = pdf417.getImage();
				} catch (BadElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        BarcodeQRCode qrcode = new BarcodeQRCode(cur_spr.getString(cur_spr.getColumnIndex("_id")), 1, 1, null);
		        try {
					img = qrcode.getImage();
				} catch (BadElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cell1_1 = new PdfPCell(new Paragraph("Cell qr"));
				cell1_1.addElement(img);
				table_glav.addCell(cell1_1);
				
				cell1_1 = new PdfPCell(new Paragraph(cur_spr.getString(cur_spr.getColumnIndex("naimen")), FRONT_text));
				table_glav.addCell(cell1_1);
				cell1_1 = new PdfPCell(new Paragraph(cur_spr.getString(cur_spr.getColumnIndex("tip_obo")), FRONT_text));
				table_glav.addCell(cell1_1);
				cell1_1 = new PdfPCell(new Paragraph(cur_spr.getString(cur_spr.getColumnIndex("mar_mod")), FRONT_text));
				table_glav.addCell(cell1_1);
				cell1_1 = new PdfPCell(new Paragraph(cur_spr.getString(cur_spr.getColumnIndex("kol")), FRONT_text));
				table_glav.addCell(cell1_1);
				cur_spr.moveToNext();
	        }
			
			try {
				document.add(table_glav);
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			document.close();
		    return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
		    pDialog.dismiss();
	        File pdfFile = new File(Environment.getExternalStorageDirectory().getPath() + "/uch_te/Temp/sklad.pdf"); 
	        if(pdfFile.exists()){
	            Uri path = Uri.fromFile(pdfFile); 
	            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
	            pdfIntent.setDataAndType(path, "application/pdf");
	            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

	            try{
	            	startActivity(pdfIntent);
	            }
	            catch(ActivityNotFoundException e){
	                Toast.makeText(context, "Нет Приложений для просмотра pdf", Toast.LENGTH_LONG).show(); 
	            }
	        }
		}
	}
}
