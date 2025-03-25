create or replace procedure usp_pms_int_overhead_erp as

c_cd_pais_brasil number := 1;
c_cd_empresa_campinas number := 4;

v_dt_integracao date := sysdate;
v_dt_3_dia_util_mes_atual date := null;
v_m date := trunc(v_dt_integracao, 'MONTH');
v_m_2_dia_util date := null;
v_m_3_dia_util date := null;
v_dt_inicio_pms date := null;
v_dt_fim_pms    date := null;
v_cd_alocacao_overhead number := null;

-- variaveis usadas na alocacao_periodo_oh
validityDate date := null;
firstDay number := 0;
lastDay number := 0;
lastDayOfMonth number := 0;
percentAlocPeriodoOh number := 0;
startDate date := null;

-- licencas do Mega que devem ser inseridas ou atualizadas
cursor c_licencas_mega is
  select p.pess_cd_pessoa cd_pessoa, tae.tiov_cd_tipo_overhead tp_overhead, ma.dt_inicial dt_inicio, ma.dt_final dt_fim
  from vw_mega_afastamento@LN_MGWEB.CIT ma, TIPO_AFASTAMENTO_ERP tae, tipo_overhead t, pessoa p
  where tae.ERP_CD_TIPO_AFASTAMENTO = ma.CD_TIPO_AFASTAMENTO
  and t.TIOV_CD_TIPO_OVERHEAD = tae.TIOV_CD_TIPO_OVERHEAD
  and p.pess_cd_login = ma.cd_login
  and ma.dt_inicial > to_date('01/01/2015', 'dd/MM/yyyy')
  order by p.pess_cd_pessoa, ma.dt_inicial;

begin

  -- p_pais in number, p_empresa in number, p_datames in date, p_dia_util_ordinal in number
  v_dt_3_dia_util_mes_atual := UFC_PMS_DIA_UTIL_DO_MES(c_cd_pais_brasil, c_cd_empresa_campinas, v_dt_integracao, 3);
  v_m_2_dia_util := UFC_PMS_DIA_UTIL_DO_MES(c_cd_pais_brasil, c_cd_empresa_campinas, v_m, 2);
  v_m_3_dia_util := UFC_PMS_DIA_UTIL_DO_MES(c_cd_pais_brasil, c_cd_empresa_campinas, v_m, 3);

  -- licencas do Mega que devem ser alteradas ou inclusas no PMS
  for r_licenca_mega in c_licencas_mega loop
 
    -- 1. Calcule M no momento da integração
    if v_dt_integracao >= v_dt_3_dia_util_mes_atual then
  
      v_m := trunc(v_dt_integracao, 'MONTH');
    elsif add_months(v_m_3_dia_util, -1) <= v_dt_integracao and
          v_dt_integracao <= v_m_2_dia_util then
  
      v_m := add_months(v_m, -1);
    end if;
  
    -- 2. Olhando para as datas de início e fim da licença
    if r_licenca_mega.dt_fim > trunc(v_m, 'MONTH') then
  
      v_dt_inicio_pms := r_licenca_mega.dt_inicio;
      if trunc(v_m, 'MONTH') > r_licenca_mega.dt_inicio then
        v_dt_inicio_pms := trunc(v_m, 'MONTH');
      end if;
      v_dt_fim_pms := r_licenca_mega.dt_fim;

      -- checa se ja existe algum registro de licenca para o usuario e range de datas
      select max(ao.ALOV_CD_ALOCACAO_OVERHEAD)
      into v_cd_alocacao_overhead
      from ALOCACAO_OVERHEAD ao, pessoa p
      where ao.PESS_CD_PESSOA = p.PESS_CD_PESSOA
      and p.PESS_CD_PESSOA = r_licenca_mega.cd_pessoa
      and ((v_dt_inicio_pms >= ao.ALOV_DT_INICIO and v_dt_inicio_pms <= ao.ALOV_DT_INICIO) or
      ((r_licenca_mega.dt_fim >= ao.ALOV_DT_FIM and r_licenca_mega.dt_fim <= ao.ALOV_DT_FIM)));
  
      if v_cd_alocacao_overhead is null then

        select SQ_ALOV_CD_ALOCACAO_OVERHEAD.NEXTVAL 
        into v_cd_alocacao_overhead
        from dual;

        insert into ALOCACAO_OVERHEAD
          (ALOV_CD_ALOCACAO_OVERHEAD
            , PESS_CD_PESSOA
            , TIOV_CD_TIPO_OVERHEAD
            , ALOV_DT_INICIO
            , ALOV_DT_FIM
            , ALOV_IN_STATUS
            , ALOV_SG_SISTEMA_ORIGEM)
        values
          (v_cd_alocacao_overhead
            , r_licenca_mega.cd_pessoa
            , r_licenca_mega.tp_overhead
            , v_dt_inicio_pms
            , v_dt_fim_pms
            , 'T'
            , 'MEGA');
      else
        update alocacao_overhead ao 
        set ao.ALOV_DT_FIM = v_dt_fim_pms, ao.tiov_cd_tipo_overhead = r_licenca_mega.tp_overhead
        where ao.alov_cd_alocacao_overhead = v_cd_alocacao_overhead;
      end if;

      --
      -- remove todas as alocacao_periodo_oh e recria caso exista
      delete from ALOCACAO_PERIODO_OH h where h.ALOV_CD_ALOCACAO_OVERHEAD = v_cd_alocacao_overhead
      and h.ALPO_DT_ALOC_PERIODO_OH >= v_dt_inicio_pms;

      validityDate := v_dt_inicio_pms;
      startDate := v_dt_inicio_pms;
    
      while trunc(validityDate, 'MONTH') <= trunc(v_dt_fim_pms, 'MONTH') loop
        firstDay := extract(day from startDate);
    
        if trunc(v_dt_fim_pms, 'MONTH') > trunc(validityDate, 'MONTH') then
          lastDay := extract(day from LAST_DAY(validityDate));
        else
          lastDay := extract(day from v_dt_fim_pms);
        end if;

        lastDayOfMonth := extract(day from LAST_DAY(validityDate));
        percentAlocPeriodoOh := ((lastDay - firstDay) + 1) / lastDayOfMonth;
    
        insert into ALOCACAO_PERIODO_OH (ALPO_CD_ALOC_PERIODO_OH, ALOV_CD_ALOCACAO_OVERHEAD, ALPO_DT_ALOC_PERIODO_OH, ALPO_PR_ALOC_PERIODO_OH, ALPO_SG_SISTEMA_ORIGEM)
        values (SQ_ALPO_CD_ALOC_PERIODO_OH.NEXTVAL, v_cd_alocacao_overhead, trunc(validityDate, 'MONTH'), percentAlocPeriodoOh, 'MEGA');
        
        startDate := add_months(trunc(startDate, 'MONTH'), 1);
        validityDate := ADD_MONTHS(validityDate, 1);
      end loop;
  
      commit; 

    end if;
  end loop;

end usp_pms_int_overhead_erp;