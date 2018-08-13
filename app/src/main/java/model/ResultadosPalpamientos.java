package model;

public class ResultadosPalpamientos {
    private String id, nombre, descripcion;

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ResultadosPalpamientos(String id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }
}
