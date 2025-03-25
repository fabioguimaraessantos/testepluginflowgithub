CREATE OR REPLACE VIEW VW_DEAL_HRS_FAT_FILTER AS
select deal_cd_deal, 
       deal_nm_deal,
       nvl(hofd_cd_horas_fat_deal,-1) hofd_cd_horas_fat_deal, 
       nvl(hofd_dt_mes,to_date('01/01/1900','dd/mm/yyyy')) hofd_dt_mes, 
       clie_cd_cliente,
       clie_nm_cliente,
       nacl_cd_natureza,
       celu_cd_centro_lucro,
       copr_nm_contrato_pratica,
       prat_cd_pratica,
       celu_nm_centro_lucro,
       prat_nm_pratica,
       nacl_nm_natureza,
       (case when hofd_dt_mes is null then 'NA' else hofd_in_versao end) hofd_in_versao
       
FROM (

     SELECT deal_cd_deal, 
             deal_nm_deal,
             hofd_cd_horas_fat_deal, 
             hofd_dt_mes, hofd_in_versao,
             clie_cd_cliente,
             clie_nm_cliente,
             nacl_cd_natureza,
             celu_cd_centro_lucro,
             copr_nm_contrato_pratica,
             prat_cd_pratica, 
             celu_nm_centro_lucro,
             prat_nm_pratica,
             nacl_nm_natureza,
             count( deal_cd_deal ) over( partition by deal_cd_deal, hofd_dt_mes, hofd_in_versao) total,
             row_number() over( partition by deal_cd_deal, hofd_dt_mes, hofd_in_versao order by  deal_cd_deal, hofd_dt_mes, hofd_in_versao) nlinha
             
     FROM 

        (select d.deal_cd_deal, 
               d.deal_nm_deal,
               horas.hofd_cd_horas_fat_deal, 
               horas.hofd_dt_mes, horas.hofd_in_versao,
               cli.clie_cd_cliente,
               cli.clie_nm_cliente,
               ncl.nacl_cd_natureza,
               ncl.nacl_nm_natureza,
               cl.celu_cd_centro_lucro,
               cl.celu_nm_centro_lucro,
               cp.copr_nm_contrato_pratica,
               p.prat_nm_pratica,
               p.prat_cd_pratica
          from cliente cli, contrato_pratica cp, contrato c,
               natureza_centro_lucro ncl, contrato_pratica_centro_lucro cpcl,
               centro_lucro cl, pratica p, 
               deal d, horas_faturadas_deal horas
          where
          
            --join do cliente 
            d.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica
            and cp.cont_cd_contrato = c.cont_cd_contrato
            and c.clie_cd_cliente = cli.clie_cd_cliente
              
            --join da pratica
            and cp.prat_cd_pratica = p.prat_cd_pratica
              
            --join natureza e centro_lucro
            and cp.copr_cd_contrato_pratica = cpcl.copr_cd_contrato_pratica 
            and cpcl.celu_cd_centro_lucro = cl.celu_cd_centro_lucro 
            and cl.nacl_cd_natureza = ncl.nacl_cd_natureza
            
            AND d.deal_cd_deal = horas.deal_cd_deal (+)
            AND d.deal_in_status = 'A' 
       )
)

where nlinha = 1