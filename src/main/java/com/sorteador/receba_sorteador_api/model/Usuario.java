package com.sorteador.receba_sorteador_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[0-9]{9}$", message = "Celular deve ter 9 dígitos numéricos")
    private String celular;

    @Column(nullable = false)
    private boolean ganhou;

    @Column(nullable = false)
    private String numeros;  // Ex: "1,2,3"

    @Column
    private String filiado;

    @Column(nullable = false)
    private LocalDate data;

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public boolean isGanhou() { return ganhou; }
    public void setGanhou(boolean ganhou) { this.ganhou = ganhou; }
    public String getNumeros() { return numeros; }
    public void setNumeros(String numeros) { this.numeros = numeros; }
    public String getFiliado() { return filiado; }
    public void setFiliado(String filiado) { this.filiado = filiado; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
}
