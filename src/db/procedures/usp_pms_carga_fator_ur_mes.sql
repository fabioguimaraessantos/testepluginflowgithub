create or replace procedure usp_pms_carga_fator_ur_mes is

v_mapa_min_date date;
v_mapa_max_date date;
v_data_corrente date;

begin

FOR mapa IN (select * from mapa_alocacao) LOOP
    -- pega maior data de alocacao do mapa
    SELECT MAX(ap.alpe_dt_alocacao_periodo) into v_mapa_max_date from alocacao_periodo ap, alocacao a
    WHERE a.aloc_cd_alocacao = ap.aloc_cd_alocacao
         AND a.maal_cd_mapa_alocacao = mapa.maal_cd_mapa_alocacao
         AND ap.alpe_dt_alocacao_periodo =
         (select MAX(ap2.alpe_dt_alocacao_periodo) from alocacao_periodo ap2, alocacao a2
           WHERE a2.maal_cd_mapa_alocacao = mapa.maal_cd_mapa_alocacao and ap2.aloc_cd_alocacao = a.aloc_cd_alocacao);
    -- pega a menor data de alocacao do mapa
    SELECT MIN(ap.alpe_dt_alocacao_periodo) INTO v_mapa_min_date from alocacao_periodo ap, alocacao a
    WHERE a.aloc_cd_alocacao = ap.aloc_cd_alocacao
          AND a.maal_cd_mapa_alocacao = mapa.maal_cd_mapa_alocacao
          AND ap.alpe_dt_alocacao_periodo =
          (select MIN(ap2.alpe_dt_alocacao_periodo) from alocacao_periodo ap2, alocacao a2
            WHERE a2.maal_cd_mapa_alocacao = mapa.maal_cd_mapa_alocacao and ap2.aloc_cd_alocacao = a.aloc_cd_alocacao);

    v_data_corrente := v_mapa_min_date;
    -- cria os fatores de ur por mes para cada mapa
    WHILE (v_data_corrente <= v_mapa_max_date) LOOP
        insert into FATOR_UR_MES (FAUM_CD_FATOR_UR_MES, MAAL_CD_MAPA_ALOCACAO, FAUM_DT_MES, FAUM_PR_FATOR_UR)
           values (sq_faum_cd_fator_ur_mes.nextval,mapa.maal_cd_mapa_alocacao, v_data_corrente, 1.0);
            v_data_corrente := ADD_MONTHS(v_data_corrente,1);
    END LOOP;
END LOOP

commit;

end usp_pms_carga_fator_ur_mes;