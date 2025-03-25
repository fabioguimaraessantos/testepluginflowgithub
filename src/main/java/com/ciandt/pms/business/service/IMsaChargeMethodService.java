package com.ciandt.pms.business.service;

import com.ciandt.pms.model.MsaChargeMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IMsaChargeMethodService {

    List<MsaChargeMethod> findAll();
}
