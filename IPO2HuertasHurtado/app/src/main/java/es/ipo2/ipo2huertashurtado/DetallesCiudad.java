package es.ipo2.ipo2huertashurtado;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DecimalFormat;

public class DetallesCiudad extends AppCompatActivity {

    private EditText txtNCiudad;
    private Spinner spinnerTiempo;
    private EditText txtTempMax;
    private EditText txtTempMin;
    private EditText txtLluvia;
    private EditText txtViento;
    private Button btnGuardarPrediccion;
    private boolean nuevaciudad;
    private boolean ciudadrepetida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_ciudad);
        ciudadrepetida= false;
        nuevaciudad= false;
        //Obtenemos las referencias a los elementos gráficos de la GUI
        txtNCiudad = findViewById(R.id.txtNCiudad);
        spinnerTiempo = findViewById(R.id.spinnerTiempo);
//Llenar de contenido el Spinner
        String[] opciones = {"Sol", "Sol y chubascos", "Nublado", "Lluvia", "Sol con intervalos nubosos"};
        ArrayAdapter<String> adapter = new
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinnerTiempo.setAdapter(adapter);
        txtTempMax = findViewById(R.id.txtTempMax);
        txtTempMin = findViewById(R.id.txtTempMin);
        txtLluvia = findViewById(R.id.txtLluvia);
        txtViento = findViewById(R.id.txtViento);
        btnGuardarPrediccion = (Button) findViewById(R.id.btnGuardarPrediccion);

//Recoger los datos enviados por la primera actividad y mostrarlos en la GUI
        Bundle bundle = getIntent().getExtras();
        //con estos if podemos modificar la gui dependiendo de si estamos editando una prevision o añadiendo una nueva
        //esto es posible gracias al valor modo que hemos pasado
        if (bundle.getInt("modo") == 2) {
            txtNCiudad.setEnabled(false);
        }
        if (bundle.getInt("modo") == 1) {
            btnGuardarPrediccion.setText("Añadir nueva predicción");
            nuevaciudad=true;
        }
        txtNCiudad.setText(bundle.getString("nombre"));
        spinnerTiempo.setSelection(bundle.getInt("tiempo"));
        txtTempMax.setText(String.valueOf(bundle.getInt("tmax")));
        txtTempMin.setText(String.valueOf(bundle.getInt("tmin")));
        txtLluvia.setText(String.valueOf(bundle.getDouble("lluvia")));
        txtViento.setText(String.valueOf(bundle.getInt("viento")));


        btnGuardarPrediccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //si estamos añadiendo una nueva ciudad controlamos que no exista ya en la bd
                if(nuevaciudad==true){
                    ConectorBD conectorBD = new ConectorBD(DetallesCiudad.this);
                    conectorBD.abrir();
                    Cursor c=conectorBD.existeCiudad(txtNCiudad.getText().toString());
                    if(c.getCount()!=0) {
                        ciudadrepetida = true;
                    }else{
                        ciudadrepetida=false;
                    }
                }
                if(ciudadrepetida==true) {
                    txtNCiudad.setError("Ya existe esta ciudad en la BD. ");

                    //primero vamos a controlar que ningun campo este vacio y que la temperatura maxima sea mayor o igual que la minima
                }else if (TextUtils.isEmpty(txtNCiudad.getText().toString())) {
                    txtNCiudad.setError("Este campo no puede estar vacio.");

                } else if (TextUtils.isEmpty(txtTempMax.getText().toString())) {
                    txtTempMax.setError("Este campo no puede estar vacio.");

                } else if (TextUtils.isEmpty(txtTempMin.getText().toString())) {
                    txtTempMin.setError("Este campo no puede estar vacio.");

                } else if (TextUtils.isEmpty(txtLluvia.getText().toString())) {
                    txtLluvia.setError("Este campo no puede estar vacio.");

                } else if (Integer.parseInt(txtTempMax.getText().toString()) < Integer.parseInt(txtTempMin.getText().toString())) {
                    txtTempMin.setError("La temperatura minima debe ser menor o igual que la maxima.");
                } else if (TextUtils.isEmpty(txtViento.getText().toString())) {
                    txtViento.setError("Este campo no puede estar vacio.");

                } else {
                    Intent nuevoPredicción = new Intent();
                    nuevoPredicción.putExtra("nombre", txtNCiudad.getText().toString());
                    nuevoPredicción.putExtra("tiempo", spinnerTiempo.getSelectedItemPosition());
                    nuevoPredicción.putExtra("tmax", Integer.parseInt(txtTempMax.getText().toString()));
                    nuevoPredicción.putExtra("tmin", Integer.parseInt(txtTempMin.getText().toString()));
                    nuevoPredicción.putExtra("lluvia", Double.parseDouble(txtLluvia.getText().toString()));
                    nuevoPredicción.putExtra("viento", Integer.parseInt(txtViento.getText().toString()));
                    setResult(RESULT_OK, nuevoPredicción);
                    finish();

                }
            }
        });
    }
}
