package model;

public class Ordenhas {
    private String id, bovinoid, empleado, fecha, observa, cantidad;

    public String getId() {
        return id;
    }

    public String getBovinoid() {
        return bovinoid;
    }

    public String getEmpleado() {
        return empleado;
    }

    public String getFecha() {
        return fecha;
    }

    public String getObserva() {
        return observa;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBovinoid(String bovinoid) {
        this.bovinoid = bovinoid;
    }

    public Ordenhas(String id, String bovinoid){
        this.id = id;
        this.bovinoid = bovinoid;
    }
}
