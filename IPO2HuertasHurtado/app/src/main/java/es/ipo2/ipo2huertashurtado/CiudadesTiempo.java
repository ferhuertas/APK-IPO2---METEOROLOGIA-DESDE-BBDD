package es.ipo2.ipo2huertashurtado;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CiudadesTiempo extends AppCompatActivity {

    private ConectorBD conectorBD;
    private ListView lstCiudades;
    private TextView lblSeleccionado;
    private ArrayList<Ciudad> ciudades;
    private int ciudadSeleccionada;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudades_tiempo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        conectorBD = new ConectorBD(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //cuando se usa el fab para agregar una nueva ciudad
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CiudadesTiempo.this, DetallesCiudad.class);
                i.putExtra("nombre", "");
                i.putExtra("tiempo", 0);
                i.putExtra("tmax", 0);
                i.putExtra("tmin", 0);
                i.putExtra("lluvia", 0);
                i.putExtra("viento", 0);
                i.putExtra("modo", 1);
                startActivityForResult(i, 1111);
            }
        });


        //Crear la lista de contactos y primera conexion con la bd para recoger los datos
        ciudades = new ArrayList<Ciudad>();


        conectorBD.abrir();
        Cursor c = conectorBD.listarCiudades();
        if (c.moveToFirst()) {
            do {
                Ciudad ciudad = new Ciudad(null, 0, 0, 0, 0, 0);
                ciudad.setNombreCiudad(c.getString(0));
                ciudad.setTiempo(c.getInt(1));
                ciudad.setTempMax(c.getInt(2));
                ciudad.setTempMin(c.getInt(3));
                ciudad.setLluvia(c.getDouble(4));
                ciudad.setViento(c.getInt(5));

                ciudades.add(ciudad);
            } while (c.moveToNext());
        }
        c.close();
        conectorBD.cerrar();

        //Obtener una referencia a la lista gráfica
        lstCiudades = findViewById(R.id.lstCiudades);

        AdaptadorLista adaptador = new AdaptadorLista(this, ciudades);
        lstCiudades.setAdapter(adaptador);

        //Obtener una referencia a la etiqueta en la que se mostrará el ítem seleccionado
        lblSeleccionado = findViewById(R.id.lblSeleccionado);
        registerForContextMenu(lstCiudades);
        lstCiudades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                lblSeleccionado.setText("Ciudad seleccionada: " +
                        ((Ciudad) lstCiudades.getItemAtPosition(posicion)).getNombreCiudad());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ciudades_tiempo, menu);
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ciudadSeleccionada = info.position;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.acercaDe:
                Log.d("LogCat", "Pulsó la opción de menú Acerca De...");

//Se muestra una ventana de diálogo
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Acerca de...");
                builder.setMessage("Aplicación creada por Fernando Huertas Olivares y Manuel Hurtado Lillo.\n\nLos datos representados en la aplicación son totalmente ficticios. \nPor favor no salga a la calle fiandose de estos datos meteorologicos.");
                builder.setPositiveButton("OK", null);
                builder.create();
                builder.show();


        }
        return true;
    }


    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editar:
                Log.d("LogCat", "Pulsó la opción del menú contextual Editar info");
                Intent i = new Intent(this, DetallesCiudad.class);
                Log.d("LogCat", "Item seleccionado " + ciudadSeleccionada);
                i.putExtra("nombre", ciudades.get(ciudadSeleccionada).getNombreCiudad());
                i.putExtra("tiempo", ciudades.get(ciudadSeleccionada).getTiempo());
                i.putExtra("tmax", ciudades.get(ciudadSeleccionada).getTempMax());
                i.putExtra("tmin", ciudades.get(ciudadSeleccionada).getTempMin());
                i.putExtra("lluvia", ciudades.get(ciudadSeleccionada).getLluvia());
                i.putExtra("viento", ciudades.get(ciudadSeleccionada).getViento());
                i.putExtra("modo", 2);
                startActivityForResult(i, 1234);
                break;
            case R.id.borrarCiudad:
                Log.d("LogCat", "Pulsó la opción de menú contextual Borrar Ciudad");
                conectorBD.abrir();
                conectorBD.eliminarCiudad(ciudades.get(ciudadSeleccionada).getNombreCiudad());
                conectorBD.cerrar();
                ciudades.remove(ciudadSeleccionada);
                ((AdaptadorLista) lstCiudades.getAdapter()).notifyDataSetChanged();
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Bundle b = data.getExtras();
    // si es una modificacion de una ciudad
            if (requestCode == 1234) {
                if (resultCode == RESULT_OK) {
                    Ciudad ciudadModificada = new
                            Ciudad(b.getString("nombre"), b.getInt("tiempo"), b.getInt("tmax"), b.getInt("tmin"),
                            b.getDouble("lluvia"), b.getInt("viento"));
                    ciudades.set(ciudadSeleccionada, ciudadModificada);
                    ((AdaptadorLista) lstCiudades.getAdapter()).notifyDataSetChanged();
                    conectorBD.abrir();
                    conectorBD.editarCiudad(ciudadModificada.getNombreCiudad(), ciudadModificada.getTiempo(), ciudadModificada.getTempMax(), ciudadModificada.getTempMin(), ciudadModificada.getLluvia(), ciudadModificada.getViento());
                    conectorBD.cerrar();
                    Toast.makeText(getBaseContext(), "Se edito: " + ciudadModificada.getNombreCiudad() + " correctamente.", Toast.LENGTH_SHORT).show();
                }
            }
            //si se ha agregado una nueva ciudad.
            if (requestCode == 1111) {
                if (resultCode == RESULT_OK) {
                    Ciudad ciudadNueva = new
                            Ciudad(b.getString("nombre"), b.getInt("tiempo"), b.getInt("tmax"), b.getInt("tmin"),
                            b.getDouble("lluvia"), b.getInt("viento"));
                    ciudades.add(ciudadNueva);
                    ((AdaptadorLista) lstCiudades.getAdapter()).notifyDataSetChanged();
                    conectorBD.abrir();
                    conectorBD.insertarCiudad(ciudadNueva.getNombreCiudad(), ciudadNueva.getTiempo(), ciudadNueva.getTempMax(), ciudadNueva.getTempMin(), ciudadNueva.getLluvia(), ciudadNueva.getViento());
                    conectorBD.cerrar();
                    Toast.makeText(getBaseContext(), "Se añadió: " + ciudadNueva.getNombreCiudad() + " a la BD local correctamente.", Toast.LENGTH_SHORT).show();
                }

            }
            //try-catch creado para controlar que no se cierre la aplicacion cuando pulsamos el boton de "Atras" del navegador cuando estamos en la activity de editar informacion
        } catch (Exception e) {

        }
    }

}
