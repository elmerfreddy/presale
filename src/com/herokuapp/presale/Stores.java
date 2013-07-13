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
import android.widget.Toast;

public class Stores {

	// Nombres de las tablas
	public static final String ID = "_id";
	public static final String REMOTE_ID = "remote_id";
	public static final String NAME = "name";
	public static final String OWNER = "_owner";
	public static final String ADDRESS = "_address";
	public static final String LATITUDE = "_latitude";
	public static final String LONGITUDE = "_longitude";
	public static final String UPDATED_AT = "updated_at";
	public static final String CREATED_AT = "created_at";

	// Nombre de la base de datos, nombre de la tabla, y la versión.
	private static final String DB_NAME = "presale.db";
	private static final String TABLE_NAME = "stores";
	private static final int DB_VERSION = 2;

	// Una clase para manejo de la base de datos: "BaseDeDatos"
	private DataBase1 dataBase;
	private final Context context;

	// puntero a la base de datos
	private SQLiteDatabase nBD;
	
	public class DataBase1 extends SQLiteOpenHelper {
		public DataBase1(Context c) {
			//Toast.makeText(c, "hola mundo", Toast.LENGTH_SHORT).show();
			super(c, DB_NAME, null, DB_VERSION);
			Log.i("CREACION Bd", "El constructor 1");
		}
		
		public void onCreate(SQLiteDatabase db) {
			Log.i("CREACION Bd", "Antes de crear");
			String query = 
					"CREATE TABLE " + TABLE_NAME + " (" +
					ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					REMOTE_ID + " INTEGER, " +
					NAME + " TEXT, " +
					OWNER + " TEXT, " +
					ADDRESS + " TEXT, " +
					LATITUDE + " TEXT, " +
					LONGITUDE + " TEXT, " +
					UPDATED_AT + " TEXT NOT NULL, " +
					CREATED_AT + " TEXT NOT NULL);";
			
			Log.i("CREATE TABLE QUERY", query);
			
			db.execSQL(query);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
			onCreate(db);
		}
	}

	public Stores(Context c) {
		context = c;
	}

	public Stores open() throws Exception {
		Log.i("CREACION Bd", "hola mundo");
		dataBase = new DataBase1(context);
		Log.i("CREACION Bd", "hola mundo 2");
		nBD = dataBase.getWritableDatabase(); // Abrir la conexión a la base de datos
		Log.i("CREACION Bd", "hola mundo 3");
		return this;
	}

	public void close() throws Exception {
		dataBase.close(); // Cerrar la conexión de la base de datos
	}

	/*
	 * Si utilizas SQL es mas propenso a errores.
	 * En los paquetes es mas seguro por el tema de Clave y Valor.
	 */

	public ArrayList<Store> all() {
		ArrayList<Store> storesList = new ArrayList<Store>();
		String[] columns = new String [] {ID, REMOTE_ID, NAME, OWNER, ADDRESS, LATITUDE, LONGITUDE, UPDATED_AT, CREATED_AT};
        Cursor cursor = nBD.query(TABLE_NAME, columns, null, null, null, null, null);
        Store item = null;
        
        if (cursor.getCount() != 0) {
        	for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    item = new Store();

                    item.id = cursor.getInt(cursor.getColumnIndex(ID));
                    item.remote_id = cursor.getInt(cursor.getColumnIndex(REMOTE_ID));
                    item.name = cursor.getString(cursor.getColumnIndex(NAME));
                    item.owner = cursor.getString(cursor.getColumnIndex(OWNER));
                    item.address = cursor.getString(cursor.getColumnIndex(ADDRESS));
                    item.latitude = cursor.getDouble(cursor.getColumnIndex(LATITUDE));
                    item.longitude = cursor.getDouble(cursor.getColumnIndex(LONGITUDE));
                    item.updated_at = cursor.getString(cursor.getColumnIndex(UPDATED_AT));
                    item.created_at = cursor.getString(cursor.getColumnIndex(CREATED_AT));
                    //Log.w("GET PRODUCT: ", item.name);
                    storesList.add(item);
            }
        }
        cursor.close();
        
		return storesList;
	}
	
	public void insertTestData() {
		ContentValues paquete = new ContentValues();
		for (int i = 0; i < (int)(Math.random() * 20); i++) {
			paquete.put(REMOTE_ID, (int)(Math.random()*100));
			paquete.put(NAME, "Store " + i);
			paquete.put(OWNER, "Dueño " + i);
			paquete.put(ADDRESS, "Dirección " + i);
			paquete.put(LATITUDE, Math.random()* 20);
			paquete.put(LONGITUDE, Math.random() * 10);
			paquete.put(UPDATED_AT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
			paquete.put(CREATED_AT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
			nBD.insert(TABLE_NAME,  null, paquete);
			Log.i("INSERT QUERY", "STORE " + i);
		}
	}
}
