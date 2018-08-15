package model;

public class Zoometricas {
    private String id, bovinoid, altura, fecha, peso, testiculos;


    public String getId() {
        return id;
    }

    public String getBovinoid() {
        return bovinoid;
    }

    public String getAltura() {
        return altura;
    }

    public String getFecha() {
        return fecha;
    }

    public String getPeso() {
        return peso;
    }

    public String getTesticulos() {
        return testiculos;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBovinoid(String bovinoid) {
        this.bovinoid = bovinoid;
    }

    public Zoometricas(String id, String bovinoid){
        this.id = id;
        this.bovinoid = bovinoid;
    }
}
