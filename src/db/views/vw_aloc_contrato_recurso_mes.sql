create or replace view vw_aloc_contrato_recurso_mes as
select pess_cd_login,
       recu_cd_recurso,
       alpe_cd_alocacao_periodo,
       alpe_dt_alocacao_periodo,
       petc_pr_alocavel,
       maal_cd_mapa_alocacao,
       copr_nm_contrato_pratica,
       alpe_pr_alocacao_periodo,
       total_alocacao as ACRM_TOTAL_ALOCACAO,
       pr_ferias as ACRM_PR_FERIAS,
       pr_licenca as ACRM_PR_LICENCA,
       total_oh as ACRM_VL_TOTAL_OH,
       case when petc_pr_alocavel is not null
       then (case when (total_alocacao + total_oh) > petc_pr_alocavel
             then 1
             else case when (total_alocacao + total_oh) = petc_pr_alocavel
                  then 0
                  else -1 end
             end)
       else 0
       end as ACRM_IN_SUPER_ALOCACAO
from (
      select pess_cd_login,
             recu_cd_recurso,
             alpe_cd_alocacao_periodo,
             alpe_dt_alocacao_periodo,
             petc_pr_alocavel,
             maal_cd_mapa_alocacao,
             copr_nm_contrato_pratica,
             alpe_pr_alocacao_periodo,
             total_alocacao,
             total_aloc_mapa,
             pr_ferias,
             pr_licenca,
             (pr_ferias + pr_licenca) total_oh,
             case when (pr_ferias > 0 or pr_licenca > 0)
             then (case when ((maal_cd_mapa_alocacao > 0)
                              or (maal_cd_mapa_alocacao = 0 and total_aloc_mapa = 0 and (count(maal_cd_mapa_alocacao) over(partition by pess_cd_login, alpe_dt_alocacao_periodo)) = 1 ))
                   then row_number() over(partition by pess_cd_login, alpe_dt_alocacao_periodo, copr_nm_contrato_pratica, aloc_cd_alocacao, alpe_cd_alocacao_periodo
                                          order by pess_cd_login asc, alpe_dt_alocacao_periodo asc, flag asc, copr_nm_contrato_pratica asc)
                   else 2
                   end)
             else row_number() over(partition by pess_cd_login, alpe_dt_alocacao_periodo, copr_nm_contrato_pratica, aloc_cd_alocacao, alpe_cd_alocacao_periodo
                                    order by pess_cd_login asc, alpe_dt_alocacao_periodo asc, flag asc, copr_nm_contrato_pratica asc)
             end as nlinha
      from (
            select sql_union.pess_cd_login,
                   sql_union.recu_cd_recurso,
                   sql_union.aloc_cd_alocacao,
                   sql_union.alpe_cd_alocacao_periodo,
                   sql_union.alpe_dt_alocacao_periodo,
                   ptc.petc_pr_alocavel,
                   maal_cd_mapa_alocacao,
                   max(case when flag = 1 then sql_union.copr_nm_contrato_pratica else '' end)
                            over(partition by sql_union.pess_cd_login, sql_union.alpe_dt_alocacao_periodo, sql_union.copr_nm_contrato_pratica) copr_nm_contrato_pratica,
                   sql_union.alpe_pr_alocacao_periodo,
                   max(case when flag = 1 then sql_union.total_alocacao else 0 end)
                            over(partition by sql_union.pess_cd_login, sql_union.alpe_dt_alocacao_periodo, sql_union.copr_nm_contrato_pratica) total_alocacao,
                   sum(sql_union.alpe_pr_alocacao_periodo) over(partition by sql_union.pess_cd_login, sql_union.alpe_dt_alocacao_periodo) total_aloc_mapa,
                   max(case when flag = 2 and sql_union.copr_nm_contrato_pratica = 'Vacation' then sql_union.total_alocacao else 0 end)
                            over(partition by sql_union.pess_cd_login, sql_union.alpe_dt_alocacao_periodo) pr_ferias,
                   max(case when flag = 2 and sql_union.copr_nm_contrato_pratica = 'Leave' then sql_union.total_alocacao else 0 end)
                            over(partition by sql_union.pess_cd_login, sql_union.alpe_dt_alocacao_periodo) pr_licenca,
                   sql_union.flag
            from pessoa_tipo_contrato ptc, (
                  select pess_cd_login,
                         pess_cd_pessoa,
                         recu_cd_recurso,
                         aloc_cd_alocacao,
                         alpe_cd_alocacao_periodo,
                         alpe_dt_alocacao_periodo,
                         maal_cd_mapa_alocacao,
                         copr_nm_contrato_pratica,
                         alpe_pr_alocacao_periodo,
                         total_alocacao,
                         origem,
                         case when origem = 'ALOC' then 1
                              when origem = 'OH'   then 2 end as flag
                  from (
                       (select sql_part_mapa.pess_cd_login,
                               sql_part_mapa.pess_cd_pessoa,
                               sql_part_mapa.recu_cd_recurso,
                               sql_part_mapa.aloc_cd_alocacao,
                               sql_part_mapa.alpe_cd_alocacao_periodo,
                               sql_part_mapa.alpe_dt_alocacao_periodo,
                               sql_part_mapa.maal_cd_mapa_alocacao,
                               sql_part_mapa.copr_nm_contrato_pratica,
                               sql_part_mapa.alpe_pr_alocacao_periodo,
                               sql_part_mapa.total_alocacao,
                               'ALOC' origem
                        from (
                              select pess_cd_login,
                                     pess_cd_pessoa,
                                     recu_cd_recurso,
                                     aloc_cd_alocacao,
                                     alpe_cd_alocacao_periodo,
                                     alpe_dt_alocacao_periodo,
                                     maal_cd_mapa_alocacao,
                                     copr_nm_contrato_pratica,
                                     alpe_pr_alocacao_periodo,
                                     total_alocacao
                              from (
                                    select p.pess_cd_login,
                                           p.pess_cd_pessoa,
                                           r.recu_cd_recurso,
                                           a.aloc_cd_alocacao,
                                           ap.alpe_cd_alocacao_periodo,
                                           ap.alpe_dt_alocacao_periodo,
                                           ma.maal_cd_mapa_alocacao,
                                           cp.copr_nm_contrato_pratica,
                                           ap.alpe_pr_alocacao_periodo,
                                           sum(ap.alpe_pr_alocacao_periodo) over(partition by p.pess_cd_login, ap.alpe_dt_alocacao_periodo) total_alocacao,
                                          row_number() over(partition by p.pess_cd_login, ap.alpe_dt_alocacao_periodo, cp.copr_nm_contrato_pratica, a.aloc_cd_alocacao
                                                             order by p.pess_cd_login asc, ap.alpe_dt_alocacao_periodo asc, cp.copr_nm_contrato_pratica asc) nlinha
                                    from mapa_alocacao ma,
                                         alocacao a,
                                         alocacao_periodo ap,
                                         recurso r,
                                         pessoa p,
                                         contrato_pratica cp
                                    where ma.maal_in_versao = 'PB'
                                      and ma.maal_cd_mapa_alocacao = a.maal_cd_mapa_alocacao
                                      and a.aloc_cd_alocacao = ap.aloc_cd_alocacao
                                      and a.recu_cd_recurso = r.recu_cd_recurso
                                      and r.recu_cd_recurso = p.recu_cd_recurso
                                      and ma.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica
                              )
                              where nlinha = 1
                        ) sql_part_mapa
                        )
                        union all
                        (select pess_cd_login,
                               pess_cd_pessoa,
                               recu_cd_recurso,
                               0 valor01,
                               0 valor02,
                               alpo_dt_aloc_periodo_oh,
                               0 valor03,
                               tipo_oh,
                               0 valor04,
                               total,
                               'OH' origem
                        from (
                              select sql_part_oh.pess_cd_login,
                                     sql_part_oh.pess_cd_pessoa,
                                     sql_part_oh.recu_cd_recurso,
                                     sql_part_oh.alpo_dt_aloc_periodo_oh,
                                     sql_part_oh.tipo_oh,
                                     sql_part_oh.total,
                                     row_number() over(partition by sql_part_oh.pess_cd_login, sql_part_oh.alpo_dt_aloc_periodo_oh, sql_part_oh.tipo_oh
                                                       order by sql_part_oh.pess_cd_login asc, sql_part_oh.alpo_dt_aloc_periodo_oh asc, sql_part_oh.tipo_oh desc) nlinha
                              from (
                                    select pess_cd_login,
                                           pess_cd_pessoa,
                                           recu_cd_recurso,
                                           alpo_dt_aloc_periodo_oh,
                                           case when tiov_cd_tipo_overhead = 1
                                           then 'Vacation'
                                           else 'Leave' end as tipo_oh,
                                           total
                                    from (
                                          select p.pess_cd_login,
                                                 p.pess_cd_pessoa,
                                                 r.recu_cd_recurso,
                                                 apo.alpo_dt_aloc_periodo_oh,
                                                 toh.tiov_cd_tipo_overhead,
                                                 apo.alpo_pr_aloc_periodo_oh,
                                                 sum(apo.alpo_pr_aloc_periodo_oh) over(partition by p.pess_cd_login, apo.alpo_dt_aloc_periodo_oh, toh.tiov_cd_tipo_overhead) total,
                                                 row_number() over(partition by p.pess_cd_login, apo.alpo_dt_aloc_periodo_oh, toh.tiov_nm_tipo_overhead
                                                                   order by p.pess_cd_login asc, apo.alpo_dt_aloc_periodo_oh asc, toh.tiov_nm_tipo_overhead asc) nlinha
                                          from pessoa p, recurso r, alocacao_overhead ao, alocacao_periodo_oh apo, tipo_overhead toh
                                          where p.recu_cd_recurso = r.recu_cd_recurso
                                            and p.pess_cd_pessoa = ao.pess_cd_pessoa
                                            and ao.alov_cd_alocacao_overhead = apo.alov_cd_alocacao_overhead
                                            and ao.tiov_cd_tipo_overhead = toh.tiov_cd_tipo_overhead
                                    )
                                    where nlinha = 1
                              ) sql_part_oh
                        )
                        where nlinha = 1
                        )
                  )
            ) sql_union
            where ptc.pess_cd_pessoa = sql_union.pess_cd_pessoa
              and (sql_union.alpe_dt_alocacao_periodo between ptc.petc_dt_inicio and
                  (case when ptc.petc_dt_fim is null
                   then ( select para_dt_parametro
                         from parametro
                         where para_cd_parametro = 1 )
                   else ptc.petc_dt_fim end))
      )
)
where nlinha = 1