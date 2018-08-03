package model;

public class Vacunas {
    private String vacuna_id, nombre, descripcion, dosis, frecuencia;

    public String getVacuna_id() {
        return vacuna_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setVacuna_id(String vacuna_id) {
        this.vacuna_id = vacuna_id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Vacunas(String nombre){
        this.nombre = nombre;
    }
}
