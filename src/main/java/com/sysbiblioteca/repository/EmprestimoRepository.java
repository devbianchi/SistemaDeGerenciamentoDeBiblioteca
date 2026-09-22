package com.sysbiblioteca.repository;

import com.sysbiblioteca.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmprestimoRepository extends JpaRepository <Emprestimo, Long> { }
