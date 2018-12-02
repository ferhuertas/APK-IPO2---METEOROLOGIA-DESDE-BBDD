package es.ipo2.ipo2huertashurtado;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorLista extends ArrayAdapter {
    Activity context;
    private ArrayList<Ciudad> ciudades;
    private static DecimalFormat REAL_FORMATTER = new DecimalFormat("0.#");

    AdaptadorLista(Activity context, ArrayList<Ciudad> ciudades) {
        super(context, R.layout.layout_item, ciudades);
        this.context = context;
        this.ciudades = ciudades;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.layout_item, null);
        TextView lblCiudad = (TextView) item.findViewById(R.id.lblCiudad);
        lblCiudad.setText(ciudades.get(position).getNombreCiudad());
        TextView lblTempMin = (TextView) item.findViewById(R.id.lblTempMin);
        lblTempMin.setText(ciudades.get(position).getTempMin() + " ºC");
        TextView lblTempMax = (TextView) item.findViewById(R.id.lblTempMax);
        lblTempMax.setText(ciudades.get(position).getTempMax() + " ºC");
        TextView lblViento = (TextView) item.findViewById(R.id.lblViento);
        lblViento.setText(ciudades.get(position).getViento() + " Km/h");
        TextView lblLluvia = (TextView) item.findViewById(R.id.lblLluvia);
        lblLluvia.setText(REAL_FORMATTER.format(ciudades.get(position).getLluvia()) + " mm");
        ImageView imgTiempo = (ImageView) item.findViewById(R.id.imgTiempo);
        switch (ciudades.get(position).getTiempo()) {
            case 0: //Cargar imagen de tiempo tipo "sol"
                imgTiempo.setImageResource(R.drawable.sol);
                break;
            case 1: //Cargar imagen de tiempo tipo "sollluvia"
                imgTiempo.setImageResource(R.drawable.sollluvia);
                break;
            case 2: //Cargar imagen de tiempo tipo "nubes"
                imgTiempo.setImageResource(R.drawable.nubes);
                break;
            case 3: //Cargar imagen de tiempo tipo "lluvia"
                imgTiempo.setImageResource(R.drawable.lluvia);
                break;
            case 4: //Cargar imagen de tiempo tipo "sol y nubes"
                imgTiempo.setImageResource(R.drawable.solnubes);

        }
        return (item);
    }
}