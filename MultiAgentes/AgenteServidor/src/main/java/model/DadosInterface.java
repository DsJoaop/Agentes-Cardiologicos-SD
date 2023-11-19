package model;

public class DadosInterface {
    private double altura;
    private double peso;
    private double pressaoSistolica;
    private double pressaoDiastolica;
    private double atividade;
    private double pontuacaoTotal;
    private double avaliacaoMedica;

    public DadosInterface(double altura, double peso, double pressaoSistolica,
                           double pressaoDiastolica, double atividade,
                           double pontuacaoTotal, double avaliacaoMedica) {
        this.altura = altura;
        this.peso = peso;
        this.pressaoSistolica = pressaoSistolica;
        this.pressaoDiastolica = pressaoDiastolica;
        this.atividade = atividade;
        this.pontuacaoTotal = pontuacaoTotal;
        this.avaliacaoMedica = avaliacaoMedica;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getPressaoSistolica() {
        return pressaoSistolica;
    }

    public void setPressaoSistolica(double pressaoSistolica) {
        this.pressaoSistolica = pressaoSistolica;
    }

    public double getPressaoDiastolica() {
        return pressaoDiastolica;
    }

    public void setPressaoDiastolica(double pressaoDiastolica) {
        this.pressaoDiastolica = pressaoDiastolica;
    }

    public double getAtividade() {
        return atividade;
    }

    public void setAtividade(double atividade) {
        this.atividade = atividade;
    }

    public double getPontuacaoTotal() {
        return pontuacaoTotal;
    }

    public void setPontuacaoTotal(double pontuacaoTotal) {
        this.pontuacaoTotal = pontuacaoTotal;
    }

    public double getAvaliacaoMedica() {
        return avaliacaoMedica;
    }

    public void setAvaliacaoMedica(double avaliacaoMedica) {
        this.avaliacaoMedica = avaliacaoMedica;
    }
}
