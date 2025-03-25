package com.ciandt.pms.business.service;

import com.ciandt.pms.model.VwPmsIntegReceitaNacional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IVwPmsIntegReceitaNacionalService {

    List<VwPmsIntegReceitaNacional> findAll();

    VwPmsIntegReceitaNacional findById(Long revenueCode);
}
