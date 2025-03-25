CREATE OR REPLACE VIEW VW_ALOCACAO_RECURSO_MES AS
SELECT recu_cd_recurso,
       recu_cd_mnemonico, 
       alpe_dt_alocacao_periodo, 
       sum( pr_alocacao_periodo_mes ) pr_alocacao_periodo_mes
FROM (
      -- select do mapa-alocacao
      (SELECT
             r.recu_cd_recurso,
             r.recu_cd_mnemonico, 
             ap.alpe_dt_alocacao_periodo, 
             SUM(ap.alpe_pr_alocacao_periodo) pr_alocacao_periodo_mes
      FROM
             mapa_alocacao ma, alocacao a, alocacao_periodo ap, recurso r
      WHERE 
            ma.maal_in_versao = 'PB' AND
            ma.maal_cd_mapa_alocacao = a.maal_cd_mapa_alocacao AND
            a.aloc_cd_alocacao = ap.aloc_cd_alocacao AND
            a.recu_cd_recurso = r.recu_cd_recurso AND
            r.recu_in_tipo_recurso = 'PE'              
      group by ap.alpe_dt_alocacao_periodo, r.recu_cd_mnemonico,  r.recu_cd_recurso)
      -- end do select do mapa-alocacao
      
    UNION ALL
      
      -- select da alocacao-overhead
     (SELECT r.recu_cd_recurso, p.pess_cd_login, ap_oh.alpo_dt_aloc_periodo_oh, SUM(ap_oh.alpo_pr_aloc_periodo_oh) 
            
            FROM alocacao_overhead a_oh, alocacao_periodo_oh ap_oh, pessoa p, recurso r
            
            WHERE a_oh.alov_cd_alocacao_overhead = ap_oh.alov_cd_alocacao_overhead AND
                  p.pess_cd_pessoa = a_oh.pess_cd_pessoa AND
                  r.recu_cd_recurso = p.recu_cd_recurso
                  
            GROUP BY ap_oh.alpo_dt_aloc_periodo_oh, p.pess_cd_login, r.recu_cd_recurso)    
      -- fim do select da alocacao-overhead
         
) ALOCACAO_RECURSO_MES

GROUP BY recu_cd_recurso,
       recu_cd_mnemonico, 
       alpe_dt_alocacao_periodo