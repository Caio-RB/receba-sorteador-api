package com.sorteador.receba_sorteador_api.repository;

import com.sorteador.receba_sorteador_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByCelular(String celular);

    @Query(value = "SELECT COUNT(*) FROM usuario u WHERE FIND_IN_SET(:numero, u.numeros) > 0", nativeQuery = true)
    int countByNumero(Integer numero);

    @Query(value = "SELECT COUNT(*) FROM usuario u WHERE FIND_IN_SET(:numero, u.numeros) > 0 AND u.id != :id", nativeQuery = true)
    int countByNumeroAndIdNot(Integer numero, Integer id);

    Optional<Usuario> findFirstByGanhouTrue();

    List<Usuario> findAllByGanhouTrue();
}
