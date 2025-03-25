create or replace view vw_pms_dif_mapa_snapshots as
select cd_mapa_alocacao snap_cd_mapa_alocacao,
       contrato_pratica snap_nm_contrato_pratica,
       cd_recurso snap_cd_recurso,
       recurso snap_nm_recurso,
       cd_alocacao snap_cd_alocacao,
       dt_aloc snap_dt_aloc,
       perfil_vendido_pv snap_nm_perfil_vendido_pv,
       perfil_vendido_cr snap_nm_perfil_vendido_cr,
       estagio_pv snap_in_estagio_pv,
       estagio_cr snap_in_estagio_cr,
       vl_ur_pv snap_vl_ur_pv,
       vl_ur_cr snap_vl_ur_cr,
       pr_aloc_pv snap_pr_aloc_pv,
       pr_aloc_cr snap_pr_aloc_cr,
       tipo_op snap_in_tipo_op,
       flag_dif snap_in_flag_dif
from (
        select af.cd_mapa_alocacao,
               af.contrato_pratica,
               af.cd_recurso,
               af.recurso,
               af.cd_alocacao,
               apf.dt_aloc,
               af.perfil_vendido_pv,
               af.perfil_vendido_cr,
               af.estagio_pv,
               af.estagio_cr,
               af.vl_ur_pv,
               af.vl_ur_cr,
               apf.pr_aloc_pv,
               apf.pr_aloc_cr,
               case when (perfil_vendido_pv <> perfil_vendido_cr)
                         then 'S'
                         else case when (estagio_pv <> estagio_cr)
                                        then 'S'
                                        else case when (vl_ur_pv <> vl_ur_cr)
                                                       then 'S'
                                                       else case when (pr_aloc_pv <> pr_aloc_cr) then 'S' else 'N' end end end
               end as flag_dif,
               af.tipo_op
        from (
                  select cd_mapa_alocacao,
                         contrato_pratica,
                         cd_recurso,
                         recurso,
                         cd_alocacao,
                         perfil_vendido_pv,
                         perfil_vendido_cr,
                         estagio_pv,
                         estagio_cr,
                         vl_ur_pv,
                         vl_ur_cr,
                         case when (status_pv = 'PV' and status_cr = 'CR')
                                   then 'U'
                                   else case when (status_pv = 'PV')
                                                  then 'D'
                                                  else case when (status_cr = 'CR') then 'A' else '' end end
                         end as tipo_op
                  from (
                          select cd_mapa_alocacao,
                                 contrato_pratica,
                                 cd_recurso,
                                 recurso,
                                 cd_alocacao,
                                 max(case when status = 'PV' then status else '' end)         over(partition by contrato_pratica, recurso, cd_alocacao) status_pv,
                                 max(case when status = 'CR' then status else '' end)         over(partition by contrato_pratica, recurso, cd_alocacao) status_cr,
                                 max(case when status = 'PV' then perfil_vendido else ' ' end) over(partition by contrato_pratica, recurso, cd_alocacao) perfil_vendido_pv,
                                 max(case when status = 'CR' then perfil_vendido else ' ' end) over(partition by contrato_pratica, recurso, cd_alocacao) perfil_vendido_cr,
                                 max(case when status = 'PV' then estagio else ' ' end)        over(partition by contrato_pratica, recurso, cd_alocacao) estagio_pv,
                                 max(case when status = 'CR' then estagio else ' ' end)        over(partition by contrato_pratica, recurso, cd_alocacao) estagio_cr,
                                 max(case when status = 'PV' then vl_ur else 0 end)           over(partition by contrato_pratica, recurso, cd_alocacao) vl_ur_pv,
                                 max(case when status = 'CR' then vl_ur else 0 end)           over(partition by contrato_pratica, recurso, cd_alocacao) vl_ur_cr,
                                 row_number() over(partition by contrato_pratica, recurso, cd_alocacao
                                                   order by contrato_pratica asc, recurso asc, cd_alocacao asc) nlinha
                          from (
                                  select cd_mapa_alocacao,
                                         contrato_pratica,
                                         cd_recurso,
                                         recurso,
                                         status,
                                         cd_alocacao,
                                         cd_perfil_vendido,
                                         perfil_vendido,
                                         estagio,
                                         vl_ur
                                  from (
                                          select maf.maaf_cd_mapa_alocacao cd_mapa_alocacao,
                                                 cp.copr_nm_contrato_pratica contrato_pratica,
                                                 af.alfo_cd_recurso cd_recurso,
                                                 r.recu_cd_mnemonico recurso,
                                                 af.alfo_in_status_foto status,
                                                 af.alfo_cd_alocacao cd_alocacao,
                                                 af.alfo_cd_perfil_vendido cd_perfil_vendido,
                                                 pv.peve_nm_perfil_vendido perfil_vendido,
                                                 af.alfo_in_estagio estagio,
                                                 af.alfo_vl_ur vl_ur,
                                                 row_number() over(partition by cp.copr_cd_contrato_pratica, r.recu_cd_mnemonico, af.alfo_cd_alocacao, af.alfo_in_status_foto
                                                                   order by cp.copr_cd_contrato_pratica asc, r.recu_cd_mnemonico asc, af.alfo_cd_alocacao asc, af.alfo_in_status_foto desc) nlinha
                                          from mapa_alocacao_foto maf,
                                               contrato_pratica cp,
                                               alocacao_foto af,
                                               recurso r,
                                               perfil_vendido pv
                                          where maf.maaf_cd_contrato_pratica = cp.copr_cd_contrato_pratica
                                            and maf.maaf_cd_mapa_aloc_foto = af.maaf_cd_mapa_aloc_foto
                                            and af.alfo_cd_recurso = r.recu_cd_recurso
                                            and af.alfo_cd_perfil_vendido = pv.peve_cd_perfil_vendido
                                       )
                                  where nlinha = 1
                               )
                       )
                  where nlinha = 1
             ) af,
             (
                  select cd_alocacao,
                         dt_aloc,
                         status_pv,
                         status_cr,
                         pr_aloc_pv,
                         pr_aloc_cr
                  from (
                          select cd_alocacao,
                                 dt_aloc,
                                 max(case when status = 'PV' then status else '' end)         over(partition by cd_alocacao, dt_aloc) status_pv,
                                 max(case when status = 'CR' then status else '' end)         over(partition by cd_alocacao, dt_aloc) status_cr,
                                 max(case when status = 'PV' then pr_aloc else 0 end)         over(partition by cd_alocacao, dt_aloc) pr_aloc_pv,
                                 max(case when status = 'CR' then pr_aloc else 0 end)         over(partition by cd_alocacao, dt_aloc) pr_aloc_cr,
                                 row_number() over(partition by cd_alocacao, dt_aloc
                                                   order by cd_alocacao asc, dt_aloc asc) nlinha
                          from (
                                  select cd_alocacao,
                                         status,
                                         dt_aloc,
                                         pr_aloc
                                  from (
                                          select apf.alpf_cd_alocacao cd_alocacao,
                                                 apf.alpf_in_status_foto status,
                                                 apf.alpf_dt_alocacao_periodo dt_aloc,
                                                 apf.alpf_pr_alocacao_periodo pr_aloc,
                                                 row_number() over(partition by apf.alpf_cd_alocacao, apf.alpf_dt_alocacao_periodo, apf.alpf_in_status_foto
                                                                   order by apf.alpf_cd_alocacao asc, apf.alpf_dt_alocacao_periodo asc, apf.alpf_in_status_foto desc) nlinha
                                          from alocacao_periodo_foto apf
                                       )
                                  where nlinha = 1
                               )
                       )
                  where nlinha = 1
             ) apf
        where af.cd_alocacao = apf.cd_alocacao
     )