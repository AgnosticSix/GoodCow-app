package model;

public class Cruzamientos {
    private String id, vacaid, sementalid, empleado, fecha, estado, descripcion;

    public String getId() {
        return id;
    }

    public String getVacaid() {
        return vacaid;
    }

    public String getSementalid() {
        return sementalid;
    }

    public String getEmpleado() {
        return empleado;
    }

    public String getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVacaid(String vacaid) {
        this.vacaid = vacaid;
    }

    public void setSementalid(String sementalid) {
        this.sementalid = sementalid;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Cruzamientos(String id, String vacaid, String sementalid){
        this.id = id;
        this.vacaid = vacaid;
        this.sementalid = sementalid;
    }
}
