create or replace view vw_pms_audit_follow as
select revi.rev aufl_cd_rev,
       revi.revtstmp aufl_dt_revtstmp,
       revi.revi_cd_autor aufl_cd_autor,
       case when (revi.revtype = 0)
                 then 'A'
                 else case when (revi.revtype = 1)
                                then 'E'
                                else 'D'
                      end
       end as aufl_in_revtype,
       revi.maal_cd_mapa_alocacao aufl_cd_mapa_alocacao,
       revi.recu_cd_recurso aufl_cd_recurso,
       revi.aloc_cd_alocacao aufl_cd_alocacao,
       revi.alpe_cd_alocacao_periodo aufl_cd_alocacao_periodo,
       revi.flag aufl_in_flag
from (
        -- buscas os autores das revisões de Alocacao
        select 'ALOC' flag,
               r.rev,
               r.revtstmp,
               r.revi_cd_autor,
               aa.revtype,
               aa.maal_cd_mapa_alocacao,
               aa.recu_cd_recurso,
               aa.aloc_cd_alocacao,
               null alpe_cd_alocacao_periodo
        from revinfo r,
             alocacao_aud aa
        where r.rev = aa.rev

        union all

        -- buscas os autores das revisões de AlocacaoPeriodo
        select 'ALPE' flag,
               revi.rev,
               revi.revtstmp,
               revi.revi_cd_autor,
               revi.revtype,
               case when (revi.maal_cd_mapa_alocacao is not null)
                    then revi.maal_cd_mapa_alocacao
                    else aa.maal_cd_mapa_alocacao
               end as maal_cd_mapa_alocacao,
               case when (revi.recu_cd_recurso is not null)
                    then revi.recu_cd_recurso
                    else aa.recu_cd_recurso
               end as recu_cd_recurso,
               revi.aloc_cd_alocacao,
               revi.alpe_cd_alocacao_periodo
        from alocacao_aud aa,
             (
                select revi.rev,
                       revi.revtstmp,
                       revi.revi_cd_autor,
                       revi.revtype,
                       a.maal_cd_mapa_alocacao,
                       a.recu_cd_recurso,
                       revi.aloc_cd_alocacao,
                       revi.alpe_cd_alocacao_periodo
                from alocacao a,
                     (
                        select r.rev,
                               r.revtstmp,
                               r.revi_cd_autor,
                               apa.revtype,
                               apa.aloc_cd_alocacao,
                               apa.alpe_cd_alocacao_periodo
                        from revinfo r,
                             alocacao_periodo_aud apa
                        where r.rev = apa.rev
                     ) revi
                where revi.aloc_cd_alocacao = a.aloc_cd_alocacao (+)
             ) revi
        where revi.rev = aa.rev (+)
          and revi.aloc_cd_alocacao = aa.aloc_cd_alocacao (+)
     ) revi