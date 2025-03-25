CREATE OR REPLACE VIEW VW_RECEITA_FILTER AS
select COPR_CD_CONTRATO_PRATICA,
       COPR_nm_CONTRATO_PRATICA,
       nvl(RECE_CD_RECEITA,-1) RECE_CD_RECEITA,
       nvl(RECE_DT_MES,to_date('01/01/1900','dd/mm/yyyy')) RECE_DT_MES,
       clie_cd_cliente,
       clie_nm_cliente,
       nacl_cd_natureza,
       celu_cd_centro_lucro,
       prat_cd_pratica,
       celu_nm_centro_lucro,
       prat_nm_pratica,
       nacl_nm_natureza,
       (case when RECE_DT_MES is null then 'NA' else rece_in_versao end) rece_in_versao

FROM (

     SELECT COPR_CD_CONTRATO_PRATICA,
            COPR_nm_CONTRATO_PRATICA,
             RECE_CD_RECEITA,
             RECE_DT_MES, rece_in_versao,
             clie_cd_cliente,
             clie_nm_cliente,
             nacl_cd_natureza,
             celu_cd_centro_lucro,
             prat_cd_pratica,
             celu_nm_centro_lucro,
             prat_nm_pratica,
             nacl_nm_natureza,
             count( COPR_CD_CONTRATO_PRATICA ) over( partition by COPR_CD_CONTRATO_PRATICA, rece_dt_mes, rece_in_versao) total,
             row_number() over( partition by COPR_CD_CONTRATO_PRATICA, rece_dt_mes, rece_in_versao order by  COPR_CD_CONTRATO_PRATICA, rece_dt_mes, rece_in_versao) nlinha

     FROM

        (select cp.copr_cd_contrato_pratica,
               cp.copr_nm_contrato_pratica,
               r.rece_cd_receita,
               r.rece_dt_mes, r.rece_in_versao,
               cli.clie_cd_cliente,
               cli.clie_nm_cliente,
               ncl.nacl_cd_natureza,
               ncl.nacl_nm_natureza,
               cl.celu_cd_centro_lucro,
               cl.celu_nm_centro_lucro,
               p.prat_nm_pratica,
               p.prat_cd_pratica
          from cliente cli, contrato_pratica cp, msa msa,
               natureza_centro_lucro ncl, contrato_pratica_centro_lucro cpcl,
               centro_lucro cl, pratica p,
               receita r
          where

            --join do cliente
            cp.copr_cd_contrato_pratica = r.copr_cd_contrato_pratica (+)
            and cp.msaa_cd_msa = msa.msaa_cd_msa
            and msa.clie_cd_cliente = cli.clie_cd_cliente

            --join da pratica
            and cp.prat_cd_pratica = p.prat_cd_pratica

            --join natureza e centro_lucro
            and cp.copr_cd_contrato_pratica = cpcl.copr_cd_contrato_pratica
            and cpcl.celu_cd_centro_lucro = cl.celu_cd_centro_lucro
            and cl.nacl_cd_natureza = ncl.nacl_cd_natureza

            AND cp.copr_in_status = 'C'
       )
)

where nlinha = 1