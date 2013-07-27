package com.herokuapp.presale;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Products {

	// Nombres de las tablas
	public static final String ID = "_id";
	public static final String REMOTE_ID = "remote_id";
	public static final String NAME = "name";
	public static final String PRICE = "price";
	public static final String UPDATED_AT = "updated_at";
	public static final String CREATED_AT = "created_at";

	// Nombre de la base de datos, nombre de la tabla, y la versión.
	private static final String DB_NAME = "presale.db";
	private static final String TABLE_NAME = "products";
	private static final int DB_VERSION = 2;

	// Una clase para manejo de la base de datos: "BaseDeDatos"
	private DataBase dataBase;
	private final Context context;

	// puntero a la base de datos
	private SQLiteDatabase nBD;
	
	private ArrayList<Product> productsList = new ArrayList<Product>();

	public class DataBase extends SQLiteOpenHelper {
		public DataBase(Context c) {
			super(c, DB_NAME, null, DB_VERSION);
		}
		
		public void onCreate(SQLiteDatabase db) {
			String query = 
					"CREATE TABLE " + TABLE_NAME + " (" +
					ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					REMOTE_ID + " INTEGER, " +
					NAME + " TEXT NOT NULL, " +
					PRICE + " REAL, " +
					UPDATED_AT + " TEXT NOT NULL, " +
					CREATED_AT + " TEXT NOT NULL);"; 
			db.execSQL(query);
			
			Log.i("CREATE TABLE QUERY", query);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
			onCreate(db);
		}
	}

	public Products(Context c) {
		context = c;
	}

	public Products open() throws Exception {
		dataBase = new DataBase(context);
		nBD = dataBase.getWritableDatabase(); // Abrir la conexión a la base de datos
		return this;
	}

	public void close() throws Exception {
		dataBase.close(); // Cerrar la conexión de la base de datos
	}

	/*
	 * Si utilizas SQL es mas propenso a errores.
	 * En los paquetes es mas seguro por el tema de Clave y Valor.
	 */

	public ArrayList<Product> all() {
		String[] columns = new String [] {ID, REMOTE_ID, NAME, PRICE, UPDATED_AT, CREATED_AT};
        Cursor cursor = nBD.query(TABLE_NAME, columns, null, null, null, null, null);
        
        if (cursor.getCount() != 0) {
        	for(cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext()) {
                    Product item = new Product();

                    item.id = cursor.getInt(cursor.getColumnIndex(ID));
                    item.remote_id = cursor.getInt(cursor.getColumnIndex(REMOTE_ID));
                    item.name = cursor.getString(cursor.getColumnIndex(NAME));
                    item.price = cursor.getFloat(cursor.getColumnIndex(PRICE));
                    item.updated_at = cursor.getString(cursor.getColumnIndex(UPDATED_AT));
                    item.created_at = cursor.getString(cursor.getColumnIndex(CREATED_AT));
                    //Log.w("GET PRODUCT: ", item.name);
                    productsList.add(item);
            }
        }
        cursor.close();
        
		return productsList;
	}
	
	public void insertTestData() {
		ContentValues paquete = new ContentValues();
		for (int i = 0; i < (int)(Math.random() * 20); i++) {
			paquete.put(REMOTE_ID, (int)(Math.random()*100));
			paquete.put(NAME, "Producto " + i);
			paquete.put(PRICE, Math.random() * 150 + 50);
			paquete.put(UPDATED_AT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
			paquete.put(CREATED_AT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
			nBD.insert(TABLE_NAME,  null, paquete);
			Log.i("INSERT QUERY", "Product " + i);
		}
	}
}
