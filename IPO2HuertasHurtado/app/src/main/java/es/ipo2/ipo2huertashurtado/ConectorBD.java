package es.ipo2.ipo2huertashurtado;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ConectorBD {
    static final String NOMBRE_BD = "CiudadesBDLocal";
    private CiudadesSQLiteHelper dbHelper;
    private SQLiteDatabase db;

    /*Constructor*/
    public ConectorBD(Context ctx) {
        dbHelper = new CiudadesSQLiteHelper(ctx, NOMBRE_BD, null, 1);
    }

    /*Abre la conexión con la base de datos*/
    public ConectorBD abrir() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    /*Cierra la conexión con la base de datos*/
    public void cerrar() {
        if (db != null) db.close();
    }

    /*inserta una ciudad en la BD*/
    public void insertarCiudad(String nombre, int tiempo, int tmax, int tmin, double lluvia, int viento) {
        String consultaSQL = "INSERT INTO Ciudades VALUES('" + nombre + "'," + tiempo + "," + tmax + "," + tmin + "," + lluvia + "," + viento + ")";
        db.execSQL(consultaSQL);
    }

    /*edita la informacion de una ciudad*/
    public void editarCiudad( String nombre, int tiempo, int tmax, int tmin, double lluvia, int viento) {

        String consultaSQL = "UPDATE Ciudades SET tiempo=" + tiempo + ", tmax=" + tmax + ", tmin=" + tmin + ", lluvia=" + lluvia + ", viento=" + viento + " WHERE nombre='" + nombre + "'";
        db.execSQL(consultaSQL);

    }
    /*devuelve la ciudad con el mismo nombre, usado para no poder crear dos veces la misma ciudad en la bd*/
    public Cursor existeCiudad(String nombre){
        return db.rawQuery("SELECT nombre FROM Ciudades WHERE nombre='"+nombre+"'", null);
    }

    /*elimina la ciudad seleccionada*/
    public void eliminarCiudad(String nombreciudad) {
        String consultaSQL = "DELETE FROM Ciudades WHERE nombre='" + nombreciudad + "'";
        db.execSQL(consultaSQL);
    }

    /*devuelve todas los ciudades*/
    public Cursor listarCiudades() {
        return db.rawQuery("SELECT * FROM Ciudades", null);
    }
}
