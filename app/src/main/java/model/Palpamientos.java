package model;

public class Palpamientos {
    private String id, bovinoid, resultado, empleado, fecha, observaciones;

    public String getId() {
        return id;
    }

    public String getBovinoid() {
        return bovinoid;
    }

    public String getResultado() {
        return resultado;
    }

    public String getEmpleado() {
        return empleado;
    }

    public String getFecha() {
        return fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBovinoid(String bovinoid) {
        this.bovinoid = bovinoid;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public Palpamientos(String id, String bovinoid){
        this.id = id;
        this.bovinoid = bovinoid;
    }
}
