package model;

public class Cow {
    private String id;
    private String matricula;
    private String nombre;
    private String sexo;
    private String clase;
    private String siniiga;
    private String raza;
    private String empadre;
    private String fecha_nac;
    private String fecha_are;
    private String estado;

    public String getId() {
        return id;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public String getClase() {
        return clase;
    }

    public String getSiniiga() {
        return siniiga;
    }

    public String getRaza() {
        return raza;
    }

    public String getEmpadre() {
        return empadre;
    }

    public String getFecha_nac() {
        return fecha_nac;
    }

    public String getFecha_are() {
        return fecha_are;
    }

    public String getEstado() {
        return estado;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public void setSiniiga(String siniiga) {
        this.siniiga = siniiga;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public void setEmpadre(String empadre) {
        this.empadre = empadre;
    }

    public void setFecha_nac(String fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public void setFecha_are(String fecha_are) {
        this.fecha_are = fecha_are;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cow(String id, String matricula, String nombre){
        this.id = id;
        this.matricula = matricula;
        this.nombre = nombre;

    }

}
