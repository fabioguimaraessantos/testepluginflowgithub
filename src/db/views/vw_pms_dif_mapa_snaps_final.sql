create or replace view vw_pms_dif_mapa_snaps_final as
select snap_cd_mapa_alocacao,
       snap_nm_contrato_pratica,
       snap_cd_recurso,
       snap_nm_recurso,
       snap_cd_alocacao,
       snap_dt_aloc,
       snap_nm_perfil_vendido_pv,
       snap_nm_perfil_vendido_cr,
       snap_in_estagio_pv,
       snap_in_estagio_cr,
       snap_vl_ur_pv,
       snap_vl_ur_cr,
       snap_pr_aloc_pv,
       snap_pr_aloc_cr,
       snap_in_tipo_op,
       snap_in_flag_dif,
       snap_in_flag
from (
        select v.snap_cd_mapa_alocacao,
               v.snap_nm_contrato_pratica,
               v.snap_cd_recurso,
               v.snap_nm_recurso,
               v.snap_cd_alocacao,
               v.snap_dt_aloc,
               v.snap_nm_perfil_vendido_pv,
               v.snap_nm_perfil_vendido_cr,
               v.snap_in_estagio_pv,
               v.snap_in_estagio_cr,
               v.snap_vl_ur_pv,
               v.snap_vl_ur_cr,
               v.snap_pr_aloc_pv,
               v.snap_pr_aloc_cr,
               v.snap_in_tipo_op,
               v.snap_in_flag_dif,
               'FLWALL' snap_in_flag
        from vw_pms_dif_mapa_snapshots v,
             (
                 select snap_cd_mapa_alocacao,
                        snap_cd_recurso,
                        snap_cd_alocacao
                 from (
                        select v.snap_cd_mapa_alocacao,
                               v.snap_cd_recurso,
                               v.snap_cd_alocacao,
                               row_number() over(partition by v.snap_cd_mapa_alocacao, v.snap_cd_recurso, v.snap_cd_alocacao
                                                 order by v.snap_cd_mapa_alocacao asc, v.snap_cd_recurso asc, v.snap_cd_alocacao asc) nlinha
                        from vw_pms_dif_mapa_snapshots v
                        /* o range deve ser de 6 meses: a regra é de mês atual -1 e conta 6 meses a partir do atual
                           exemplo 1: sysdate 01/05/2011
                                      range   01/04/2011 até 01/09/2011
                           exemplo 2: sysdate 01/01/2011
                                      range   01/12/2010 até 01/05/2011 */
                        where v.snap_dt_aloc between (select vfr.max_date from vw_pms_follow_range_start_date vfr) /* ex max_date: 01/04/2011 */
                                             and (select add_months(vfr.max_date, 5) max_date from vw_pms_follow_range_start_date vfr) /* ex max_date: 01/04/2011 + 5 = 01/09/2011 */
                          and v.snap_in_flag_dif = 'S'
                     )
                 where nlinha = 1
             ) vs
        where v.snap_cd_mapa_alocacao = vs.snap_cd_mapa_alocacao
          and v.snap_cd_recurso       = vs.snap_cd_recurso
          and v.snap_cd_alocacao      = vs.snap_cd_alocacao

        union all

        select vn.snap_cd_mapa_alocacao,
               vn.snap_nm_contrato_pratica,
               vn.snap_cd_recurso,
               vn.snap_nm_recurso,
               vn.snap_cd_alocacao,
               vn.snap_dt_aloc,
               vn.snap_nm_perfil_vendido_pv,
               vn.snap_nm_perfil_vendido_cr,
               vn.snap_in_estagio_pv,
               vn.snap_in_estagio_cr,
               vn.snap_vl_ur_pv,
               vn.snap_vl_ur_cr,
               vn.snap_pr_aloc_pv,
               vn.snap_pr_aloc_cr,
               vn.snap_in_tipo_op,
               vn.snap_in_flag_dif,
               'FLWPEOP' snap_in_flag
        from vw_pms_dif_mapa_snapshots vn,
             (
                 select snap_cd_mapa_alocacao,
                        snap_cd_recurso,
                        snap_cd_alocacao
                 from (
                        select v.snap_cd_mapa_alocacao,
                               v.snap_cd_recurso,
                               v.snap_cd_alocacao,
                               row_number() over(partition by v.snap_cd_mapa_alocacao, v.snap_cd_recurso, v.snap_cd_alocacao
                                                 order by v.snap_cd_mapa_alocacao asc, v.snap_cd_recurso asc, v.snap_cd_alocacao asc) nlinha
                        from vw_pms_dif_mapa_snapshots v
                        /* o range deve ser de 6 meses: a regra é de mês atual -1 e conta 6 meses a partir do atual
                           exemplo 1: sysdate 01/05/2011
                                      range   01/04/2011 até 01/09/2011
                           exemplo 2: sysdate 01/01/2011
                                      range   01/12/2010 até 01/05/2011 */
                        where v.snap_dt_aloc between (select vfr.max_date from vw_pms_follow_range_start_date vfr) /* ex max_date: 01/04/2011 */
                                             and (select add_months(vfr.max_date, 5) max_date from vw_pms_follow_range_start_date vfr) /* ex max_date: 01/04/2011 + 5 = 01/09/2011 */
                          and v.snap_in_flag_dif = 'S'
                     )
                 where nlinha = 1
             ) vs
        where vn.snap_in_flag_dif = 'N'
          and vn.snap_cd_recurso       = vs.snap_cd_recurso
          and vn.snap_cd_alocacao      <> vs.snap_cd_alocacao
     )