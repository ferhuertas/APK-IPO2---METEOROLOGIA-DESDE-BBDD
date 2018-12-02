package es.ipo2.ipo2huertashurtado;

public class Ciudad {
    private String nombreCiudad;
    private int tiempo;
    private int tempMax;
    private int tempMin;
    private double lluvia;
    private int viento;

    public Ciudad(String nombreCiudad, int tiempo, int tempMax, int tempMin, double lluvia, int viento) {
        this.nombreCiudad = nombreCiudad;
        this.tiempo = tiempo;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.lluvia = lluvia;
        this.viento = viento;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public int getTiempo() {
        return tiempo;
    }

    public int getTempMax() {
        return tempMax;
    }

    public int getTempMin() {
        return tempMin;
    }

    public double getLluvia() {
        return lluvia;
    }

    public int getViento() {
        return viento;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public void setLluvia(double lluvia) {
        this.lluvia = lluvia;
    }

    public void setViento(int viento) {
        this.viento = viento;
    }
}
