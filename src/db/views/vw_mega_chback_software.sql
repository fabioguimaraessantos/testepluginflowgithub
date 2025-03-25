create or replace view vw_mega_chback_software as
select soft.sosy_cd_pessoa,
       soft.sosy_cd_login,
       soft.sosy_cd_ti_recurso,
       soft.sosy_cd_mnemonico,
       soft.sosy_dt_mes,
       soft.sosy_nr_unidades
from chargeback_pessoa cp,
     (
        SELECT sosy_cd_pessoa,
               sosy_cd_login,
               sosy_cd_ti_recurso,
               sosy_cd_mnemonico,
               sosy_dt_mes,
               sosy_nr_unidades
        FROM (
                SELECT sosy_cd_pessoa,
                       sosy_cd_login,
                       sosy_cd_ti_recurso,
                       sosy_cd_mnemonico,
                       sosy_dt_mes,
                       sum(sosy_nr_unidades) over(PARTITION BY sosy_cd_pessoa, sosy_dt_mes, sosy_cd_ti_recurso) sosy_nr_unidades,
                       row_number() over(PARTITION BY sosy_cd_pessoa, sosy_dt_mes, sosy_cd_ti_recurso
                                         ORDER BY sosy_cd_pessoa, sosy_dt_mes, sosy_cd_ti_recurso) nlinha
                FROM (
                        select p.pess_cd_pessoa sosy_cd_pessoa,
                               chb.mcbs_cd_login sosy_cd_login,
                               tr.tire_cd_ti_recurso sosy_cd_ti_recurso,
                               chb.mcbs_tx_mnemonico sosy_cd_mnemonico,
                               chb.mcbs_dt_mes sosy_dt_mes,
                               chb.mcbs_nr_licenca sosy_nr_unidades
                        from vw_mega_charge_back_sotfware@ln_mgweb.cit chb,
                             pessoa p,
                             ti_recurso tr
                        where chb.mcbs_nr_licenca >= 1
                          and chb.mcbs_cd_login = p.pess_cd_login
                          and chb.mcbs_tx_mnemonico = tr.tire_cd_mnemonico (+)
                     )
             )
        WHERE nlinha = 1
      ) soft
where soft.sosy_cd_pessoa = cp.pess_cd_pessoa (+)
  and soft.sosy_dt_mes = cp.chpe_dt_mes (+)
  AND soft.sosy_cd_ti_recurso = cp.tire_cd_ti_recurso (+)
  and cp.chpe_cd_chargeback_pess is null