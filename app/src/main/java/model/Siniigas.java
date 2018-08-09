package model;

public class Siniigas {
    private String siniiga_id, estado_id, codigo;

    public String getSiniiga_id() {
        return siniiga_id;
    }

    public String getEstado_id() {
        return estado_id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setSiniiga_id(String siniiga_id) {
        this.siniiga_id = siniiga_id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Siniigas(String id, String codigo){
        this.siniiga_id = id;
        this.codigo = codigo;
    }
}
