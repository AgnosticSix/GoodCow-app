package model;

public class Cow {
    private String id;
    private String matricula;
    private String nombre;

    public String getId() {
        return id;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public Cow(String id, String matricula, String nombre){
        this.id = id;
        this.matricula = matricula;
        this.nombre = nombre;

    }
}
