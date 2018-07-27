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


    public Cow(String id, String matricula, String nombre){
        this.id = id;
        this.matricula = matricula;
        this.nombre = nombre;

    }

    public Cow(String id, String matricula, String nombre, String sexo, String clase, String siniiga, String raza, String empadre, String fecha_nac, String estado){

    }
}
