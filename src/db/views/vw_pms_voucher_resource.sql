CREATE OR REPLACE VIEW VW_PMS_VOUCHER_RESOURCE AS
SELECT cd_resource,
       nm_resource,
       cd_login,
       rtrim(xmlagg(xmlelement(e, cd_travel_budget || ',')).extract('//text()'), ',') cd_travel_budget,
       to_char(SYSDATE,'dd/mm/yyyy hh24:mi:ss') dt_creation
FROM (
        select a.cd_resource,
           a.nm_resource,
           a.cd_login,
           orca.orde_cd_orca_despesa cd_travel_budget,
           row_number() over(PARTITION BY a.cd_resource, orca.orde_cd_orca_despesa
                             ORDER BY a.cd_resource, orca.orde_cd_orca_despesa) nlinha
        from contrato_pratica_orc_desp_cl cpod, orc_despesa_cl orc_cl, orc_despesa orca,
             (
                SELECT cd_resource,
                       nm_resource,
                       cd_login,
                       cd_clob
                FROM (
                        select a.cd_resource,
                               a.nm_resource,
                               a.cd_login,
                               a.cd_clob,
                               row_number() over(PARTITION BY a.cd_resource, a.cd_clob
                                                 ORDER BY a.cd_resource, a.cd_clob) nlinha
                        from alocacao_periodo ap,
                             (
                               -- resources associados ao Mapa de Alocacao
                                select p.pess_cd_pessoa cd_resource,
                                       p.pess_nm_pessoa nm_resource,
                                       -- ajuste para os casos de logins "v." (vendors)
                                       CASE WHEN (p.pess_cd_login = ufc_pms_get_login_from_email(p.pess_tx_email))
                                                 THEN p.pess_cd_login
                                                 ELSE ufc_pms_get_login_from_email(p.pess_tx_email)
                                       END cd_login,
                                       ma.copr_cd_contrato_pratica cd_clob,
                                       a.aloc_cd_alocacao cd_alocacao
                                from pessoa p,
                                     recurso r,
                                     mapa_alocacao ma,
                                     alocacao a,
                                     vw_pms_voucher_clob cl
                                where ma.maal_in_versao = 'PB'
                                  and p.pess_dt_rescisao is null
                                  and p.recu_cd_recurso = r.recu_cd_recurso
                                  and r.recu_cd_recurso = a.recu_cd_recurso
                                  and a.maal_cd_mapa_alocacao = ma.maal_cd_mapa_alocacao
                                  and ma.copr_cd_contrato_pratica = cl.cd_clob
                             ) a
                        where to_date(to_char(sysdate,'mm/yyyy'),'mm/yyyy') = ap.alpe_dt_alocacao_periodo
                          and ap.aloc_cd_alocacao = a.cd_alocacao
                     )
                WHERE nlinha = 1
             ) a
        WHERE cpod.copr_cd_contrato_pratica = a.cd_clob
              and orc_cl.ordc_cd_orca_desp_cl = cpod.ordc_cd_orca_desp_cl
              and orca.orde_cd_orca_despesa = orc_cl.orde_cd_orca_despesa
        group by a.cd_resource,
                 a.nm_resource,
                 a.cd_login,
                 orca.orde_cd_orca_despesa
      union all

        -- resources associados a Grupo de Custo
        select p.pess_cd_pessoa cd_resource,
               p.pess_nm_pessoa nm_resource,
               -- ajuste para os casos de logins "v." (vendors)
               CASE WHEN (p.pess_cd_login = ufc_pms_get_login_from_email(p.pess_tx_email))
                         THEN p.pess_cd_login
                         ELSE ufc_pms_get_login_from_email(p.pess_tx_email)
               END cd_login,
               od.orde_cd_orca_despesa cd_travel_budget,
               1 nlinha
        from pessoa_grupo_custo pgc
             , grupo_custo gc
             , pessoa p
             , vw_pms_voucher_cgroup cgroup
             , orc_despesa_gc odgc
             , orc_despesa od
        where pgc.grcu_cd_grupo_custo = gc.grcu_cd_grupo_custo
              and p.pess_cd_pessoa = pgc.pess_cd_pessoa
              and gc.grcu_in_ativo = 'A'
              and cgroup.cd_cgroup = gc.grcu_cd_grupo_custo
              and trunc(sysdate, 'mm') between pgc.pegc_dt_inicio and nvl(pgc.pegc_dt_fim, last_Day(sysdate))
              and odgc.grcu_cd_grupo_custo = gc.grcu_cd_grupo_custo
              and odgc.ordg_in_delete_logico = 'N'
              and odgc.ordg_in_wl_only = 'N'
              and odgc.orde_cd_orca_despesa = od.orde_cd_orca_despesa
              and od.orde_in_ativo = 'S'
              and od.orde_in_delete_logico = 'N'
     )
WHERE nlinha = 1
GROUP BY cd_resource,
         nm_resource,
         cd_login

