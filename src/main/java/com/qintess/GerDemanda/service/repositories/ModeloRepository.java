package com.qintess.GerDemanda.service.repositories;

import com.qintess.GerDemanda.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;



@Repository
public interface ModeloRepository  extends JpaRepository<Modelo, Integer>  {

    List<Modelo> findByOrderByDescricaoAsc();
}
