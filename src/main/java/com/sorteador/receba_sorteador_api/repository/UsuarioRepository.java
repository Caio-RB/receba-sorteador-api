package com.sorteador.receba_sorteador_api.repository;

import com.sorteador.receba_sorteador_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByCelular(String celular);
    boolean existsByNumero(Integer numero);
} 
