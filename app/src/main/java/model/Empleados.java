package model;

public class Empleados {
    private String id, tipo_empleado;

    public String getId() {
        return id;
    }

    public String getTipo_empleado() {
        return tipo_empleado;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTipo_empleado(String tipo_empleado) {
        this.tipo_empleado = tipo_empleado;
    }

    public Empleados(String id){
        this.id = id;
    }
}
