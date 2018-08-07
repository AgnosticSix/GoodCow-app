package model;

public class Empleados {
    String id;
    String tipo;

    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Empleados(String id){
        this.id = id;
    }
}
