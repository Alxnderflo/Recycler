package sv.edu.itca.recyclerprueba;

public class Pregunta {
    private String enunciado;
    private String correcta;
    private String incorrecta;

    public Pregunta() {}

    public Pregunta(String enunciado, String correcta, String incorrecta) {
        this.enunciado = enunciado;
        this.correcta = correcta;
        this.incorrecta = incorrecta;
    }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getCorrecta() { return correcta; }
    public void setCorrecta(String correcta) { this.correcta = correcta; }
    public String getIncorrecta() { return incorrecta; }
    public void setIncorrecta(String incorrecta) { this.incorrecta = incorrecta; }
}