
  CREATE OR REPLACE FORCE VIEW "PMS20"."VW_FISCAL_BALANCE" ("FONTE", "UMKT", "BUSINESS_MGR", "CD_CONTRATO_PRATICA", "NM_CONTRACT_LOB", "MSAA_CD_MSA", "MSAA_NM_MSA", "NM_LOB", "NM_FISCAL_DEAL", "CD_FISCAL_DEAL", "MONTH", "REVENUE_VALUE", "VL_REVENUE_ASSOCIATED", "CD_INVOICE", "VL_INVOICE", "VL_INVOICE_ASSOCIATED", "CD_REVENUE_FISCAL_DEAL", "MOED_CD_MOEDA", "CLIE_CD_CLIENTE", "CLIE_NM_CLIENTE", "CLIE_CD_CLIENTE_DF", "CLIE_NM_CLIENTE_DF", "EMPR_CD_EMPRESA_DF", "EMPR_NM_EMPRESA_DF", "EMPR_NR_CNPJ_DF", "FORE_NM_FONTE_RECEITA") AS 
  select 
nvl(FONTE, ' '), 
nvl(UMKT, ' ') UMKT, 
nvl(BUSINESS_MGR, ' ') BUSINESS_MGR, 
nvl(CD_CONTRATO_PRATICA, 0) CD_CONTRATO_PRATICA, 
nvl(NM_CONTRACT_LOB, ' ') NM_CONTRACT_LOB, 
nvl(MSAA_CD_MSA, 0) MSAA_CD_MSA, 
nvl(MSAA_NM_MSA, ' ') MSAA_NM_MSA, 
nvl(NM_LOB, ' ') NM_LOB, 
nvl(NM_FISCAL_DEAL, ' ') NM_FISCAL_DEAL, 
nvl(CD_FISCAL_DEAL, 0) CD_FISCAL_DEAL, 
MONTH, 
REVENUE_VALUE,
VL_REVENUE_ASSOCIATED,
nvl(CD_INVOICE, 0) CD_INVOICE,
VL_INVOICE, 
VL_INVOICE_ASSOCIATED, 
nvl(CD_REVENUE_FISCAL_DEAL, 0) CD_REVENUE_FISCAL_DEAL, 
nvl(MOED_CD_MOEDA, 0) MOED_CD_MOEDA, 
nvl(CLIE_CD_CLIENTE, 0) CLIE_CD_CLIENTE, 
nvl(CLIE_NM_CLIENTE, ' ') CLIE_NM_CLIENTE,
nvl(CLIE_CD_CLIENTE_DF, 0) CLIE_CD_CLIENTE_DF, 
nvl(CLIE_NM_CLIENTE_DF, ' ') CLIE_NM_CLIENTE_DF, 
nvl(EMPR_CD_EMPRESA_DF, 0) EMPR_CD_EMPRESA_DF, 
nvl(EMPR_NM_EMPRESA_DF, ' ') EMPR_NM_EMPRESA_DF,
nvl(EMPR_NR_CNPJ_DF, ' ') EMPR_NR_CNPJ_DF, 
nvl(FORE_NM_FONTE_RECEITA, ' ') FORE_NM_FONTE_RECEITA
from (
select inf.fonte,
       inf.umkt,
       inf.business_mgr,
       inf.cd_contrato_pratica,
       inf.nm_contract_lob,
       inf.msaa_cd_msa,
       msa.msaa_nm_msa,
       inf.nm_lob,
       inf.nm_fiscal_deal,
       inf.cd_fiscal_deal,
       inf.month,
       inf.revenue_value,
       inf.vl_revenue_associated,
       inf.cd_invoice,
       inf.vl_invoice,
       inf.vl_invoice_associated,
       inf.cd_revenue_fiscal_deal,
       inf.moed_cd_moeda,
       cli.clie_cd_cliente,
       cli.clie_nm_cliente,
       clie_cd_cliente_df,
       clie_nm_cliente_df,
       empr_cd_empresa_df,
       empr_nm_empresa_df,
       empr_nr_cnpj_df,
       ( select fore.fore_nm_fonte_receita
           from fonte_receita fore
          where fore.fore_cd_fonte_receita = 1
       ) fore_nm_fonte_receita
  from msa msa,
       cliente cli,
       (select inf.fonte,
               inf.umkt,
               null business_mgr,
               inf.cd_contrato_pratica,
               inf.nm_contract_lob,
               inf.msaa_cd_msa,
               inf.nm_lob,
               inf.nm_fiscal_deal,
               inf.cd_fiscal_deal,
               inf.month,
               inf.revenue_value,
               inf.vl_revenue_associated,
               inf.cd_invoice,
               inf.vl_invoice,
               inf.vl_invoice_associated,
               inf.cd_revenue_fiscal_deal,
               inf.moed_cd_moeda,
               inf.clie_cd_cliente_df,
               inf.clie_nm_cliente_df,
               inf.empr_cd_empresa_df,
               inf.empr_nm_empresa_df,
               inf.empr_nr_cnpj_df
          from 
                      (select 'REC' fonte,
                               null umkt,
                               cd_contrato_pratica,
                               nm_contract_lob,
                               msaa_cd_msa,
                               nm_lob,
                               nm_fiscal_deal,
                               cd_fiscal_deal,
                               month,
                               revenue_value,
                               vl_revenue_associated,
                               cd_invoice,
                               vl_invoice,
                               vl_invoice_associated,
                               cd_revenue_fiscal_deal,
                               moed_cd_moeda,
                               -- moed_sg_moeda,
                               clie_cd_cliente_df,
                               clie_nm_cliente_df,
                               empr_cd_empresa_df,
                               empr_nm_empresa_df,
                               empr_nr_cnpj_df
                        from ( select 
                                      cp.copr_cd_contrato_pratica cd_contrato_pratica,
                                      cp.copr_nm_contrato_pratica nm_contract_lob,
                                      df.msaa_cd_msa /*cp.msaa_cd_msa*/     msaa_cd_msa,
                                      pr.prat_sg_pratica          nm_lob,
                                      df.defi_nm_deal_fiscal      nm_fiscal_deal,
                                      df.defi_cd_deal_fiscal      cd_fiscal_deal,
                                      r.rece_dt_mes               month,
                                      rdf.redf_vl_receita + nvl(aj.ajre_vl_ajuste, 0) revenue_value,
                                      nvl( sum( fr.fare_vl_receita_associada ) over( partition by cp.copr_nm_contrato_pratica, r.rece_dt_mes, rdf.redf_cd_receita_dfiscal, r.rece_cd_receita), 0 ) vl_revenue_associated,
                                      null cd_invoice,
                                      0 vl_invoice,
                                      0 vl_invoice_associated,
                                      rdf.redf_cd_receita_dfiscal cd_revenue_fiscal_deal,
                                      m.moed_cd_moeda,
                                      cdf.clie_cd_cliente clie_cd_cliente_df,
                                      cdf.clie_nm_cliente clie_nm_cliente_df,
                                      edf.empr_cd_empresa empr_cd_empresa_df,
                                      edf.empr_nm_empresa empr_nm_empresa_df,
                                      edf.empr_nr_cnpj    empr_nr_cnpj_df,
                                      row_number() over(partition by cp.copr_nm_contrato_pratica, r.rece_dt_mes, rdf.redf_cd_receita_dfiscal, r.rece_cd_receita order by cp.copr_nm_contrato_pratica, r.rece_dt_mes) nlinha
                               from   receita             r,
                                      receita_moeda       rm,
                                      moeda               m,
                                      contrato_pratica    cp,
                                      pratica             pr,
                                      deal_fiscal         df,
                                      msa                 msa,
                                      receita_deal_fiscal rdf,
                                      cliente             cdf,   -- Jira TI-105253
                                      empresa             edf,   -- Jira TI-105253
                                      ( select aj.redf_cd_receita_dfiscal,
                                               sum(aj.ajre_vl_ajuste) ajre_vl_ajuste
                                        from   ajuste_receita aj
                                        group  by aj.redf_cd_receita_dfiscal ) aj,
                                      fatura_receita fr
                               where  r.rece_cd_receita = rm.rece_cd_receita
                                 and  df.clie_cd_cliente = cdf.clie_cd_cliente(+)   -- Jira TI-105253
                                 and  df.empr_cd_empresa = edf.empr_cd_empresa(+)   -- Jira TI-105253
                                  and r.rece_in_versao in  ('PB', 'IN', 'PD')
                                  and rm.remo_cd_receita_moeda = rdf.remo_cd_receita_moeda
                                  and rdf.defi_cd_deal_fiscal = df.defi_cd_deal_fiscal
                                  and rm.moed_cd_moeda = m.moed_cd_moeda
                                  and r.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica
                                  and cp.prat_cd_pratica = pr.prat_cd_pratica
                                  and fr.redf_cd_receita_dfiscal(+) = rdf.redf_cd_receita_dfiscal
                                  and df.msaa_cd_msa = msa.msaa_cd_msa
                                  and rdf.redf_cd_receita_dfiscal = aj.redf_cd_receita_dfiscal(+) ) inf
                        where  nlinha = 1 ) inf
             ) inf
where msa.msaa_cd_msa = inf.msaa_cd_msa and
      msa.clie_cd_cliente = cli.clie_cd_cliente

------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- Invoices
union all

------------------------------------------------------------------------------------------------------------------------------------------------------------------
---DEVIDO AS MUDANÇAS DO MSA
-- ESSE BLOCO NÃO IRÁ TRAZER INVOICE SEM RECEITA.
--DEVIDO AS UNIFICAÇÕES DE FISCAL DEALS,  A TABELA  pms20.cpratica_dfiscal  NÃO TEM UTILIDADE
-- PARA REGISTROS ANTES DE 2013, TEORICAMENTE ELA TERIA UTILIDADE DE JAN-2013 EM DIANTE
--COMO PRECISA CHEGAR NO CONTRATO-PRATICA, ELE PRECISA DE JOIN COM A RECEITA
select
       'FAT'  fonte,
       inf.celu_nm_centro_lucro,
       inf.business_mgr,
       inf.cd_contrato_pratica,
       inf.copr_nm_contrato_pratica,
       inf.msaa_cd_msa,
       inf.msaa_nm_msa,
       inf.prat_sg_pratica,
       inf.defi_nm_deal_fiscal,
       inf.defi_cd_deal_fiscal,
       inf.mes,
       inf.vl_rev_value,
       inf.vl_rev_assoc,
       inf.fatu_cd_fatura,
       inf.vl_invoice,
       inf.vl_inv_assoc,
       inf.redf_cd_receita_dfiscal,
       inf.moed_cd_moeda,
       inf.clie_cd_cliente,
       inf.clie_nm_cliente,
       inf.clie_cd_cliente_df,
       inf.clie_nm_cliente_df,
       inf.empr_cd_empresa_df,
       inf.empr_nm_empresa_df,
       inf.empr_nr_cnpj_df,
       inf.fore_nm_fonte_receita
from   (select null /*pc.nacl_cd_natureza */nacl_cd_natureza,
               null /*pc.nacl_nm_natureza*/ nacl_nm_natureza,
               null /*pc.celu_nm_centro_lucro*/ celu_nm_centro_lucro,
               null /*pc2.celu_nm_centro_lucro*/ business_mgr,
               null /*pc.copr_cd_contrato_pratica */cd_contrato_pratica,
               cp.copr_cd_contrato_pratica cod1,
               ifat.copr_cd_contrato_pratica cod2, -- sempre null!
               cp.copr_nm_contrato_pratica,
               msa.msaa_cd_msa,
               msa.msaa_nm_msa,
               pr.prat_sg_pratica,
               df.defi_nm_deal_fiscal,
               df.defi_cd_deal_fiscal,
               trunc(fa.fatu_dt_previsao, 'mm') mes,
               0 vl_rev_value,
               0 vl_rev_assoc,
               fa.fatu_cd_fatura,
               ifat.itfa_cd_item_fatura,
               ifat.itfa_vl_item,
               sum(ifat.itfa_vl_item) over (partition by
                       cp.copr_cd_contrato_pratica,
                       fr.redf_cd_receita_dfiscal,
                        trunc(fa.fatu_dt_previsao, 'mm'),
                        df.defi_cd_deal_fiscal,
                        fa.fatu_cd_fatura) vl_invoice,

               -- fr.fare_vl_receita_associada  vl_inv_assoc,
               nvl(sum(fr.fare_vl_receita_associada)
                   over(partition by  ifat.itfa_cd_item_fatura, /* NOBORU - 25 - FEV - 2013  -- DUPLICAÇÃO DO VL ASSOCIADO*/
                         cp.copr_cd_contrato_pratica,
                        trunc(fa.fatu_dt_previsao, 'mm'),
                        df.defi_cd_deal_fiscal,
                        fa.fatu_cd_fatura),
                   0) vl_inv_assoc,
               null redf_cd_receita_dfiscal,
               ftr.fore_cd_fonte_receita,
               ftr.fore_nm_fonte_receita,
               fa.moed_cd_moeda,
               -- fa.moed_sg_moeda,
               cli.clie_cd_cliente,
               cli.clie_nm_cliente,
               cdf.clie_cd_cliente clie_cd_cliente_df,
               cdf.clie_nm_cliente clie_nm_cliente_df,
               edf.empr_cd_empresa empr_cd_empresa_df,
               edf.empr_nm_empresa empr_nm_empresa_df,
               edf.empr_nr_cnpj    empr_nr_cnpj_df,
               row_number() over(partition by trunc(fa.fatu_dt_previsao, 'mm'), df.defi_cd_deal_fiscal, fa.fatu_cd_fatura order by trunc(fa.fatu_dt_previsao, 'mm')) nlinha
        from   fatura        fa,
               item_fatura              ifat,
               fonte_receita            ftr,
               deal_fiscal              df,
               cliente                  cdf,
               empresa                  edf,
               fatura_receita           fr,
               msa                      msa,
               cliente                  cli,
               receita_deal_fiscal      rdf,
               receita_moeda            rm,
               receita                  rec,
               contrato_pratica         cp,
               pratica                  pr
         where fa.fatu_cd_fatura = ifat.fatu_cd_fatura
           and ifat.fore_cd_fonte_receita = ftr.fore_cd_fonte_receita
           and fa.defi_cd_deal_fiscal = df.defi_cd_deal_fiscal
           and df.clie_cd_cliente     = cdf.clie_cd_cliente(+)
           and df.empr_cd_empresa     = edf.empr_cd_empresa(+)
           and fa.fatu_cd_fatura = fr.fatu_cd_fatura(+)
           and df.msaa_cd_msa = msa.msaa_cd_msa
           and msa.clie_cd_cliente = cli.clie_cd_cliente
           and rdf.redf_cd_receita_dfiscal(+) = fr.redf_cd_receita_dfiscal
           and rdf.remo_cd_receita_moeda = rm.remo_cd_receita_moeda(+)
           and rm.rece_cd_receita = rec.rece_cd_receita(+)
           and df.copr_cd_contrato_pratica_aux = cp.copr_cd_contrato_pratica -- RENATO
           and cp.prat_cd_pratica = pr.prat_cd_pratica
           and fa.fatu_in_status = 'SB'
           and trunc(fa.fatu_dt_previsao, 'mm') < '01-jan-2013'
          and ifat.fore_cd_fonte_receita = 1
 )inf
where  nlinha = 1

/*TEORICAMENTE A TABELA cpratica_dfiscal DEVERIA ESTAR CORRETA PARA OS REGISTROS DE 2013
EM DIANTE.
*/
UNION ALL

select inf.fonte,
       inf.celu_nm_centro_lucro,
       inf.business_mgr,
       inf.cd_contrato_pratica,
       inf.copr_nm_contrato_pratica,
       inf.msaa_cd_msa,
       msa.msaa_nm_msa,
       inf.prat_sg_pratica,
       inf.defi_nm_deal_fiscal,
       inf.defi_cd_deal_fiscal,
       inf.mes,
       inf.vl_rev_value,
       inf.vl_rev_assoc,
       inf.fatu_cd_fatura,
       inf.vl_invoice,
       inf.vl_inv_assoc,
       inf.redf_cd_receita_dfiscal,
       inf.moed_cd_moeda,
       cli.clie_cd_cliente,
       cli.clie_nm_cliente,
       clie_cd_cliente_df,
       clie_nm_cliente_df,
       empr_cd_empresa_df,
       empr_nm_empresa_df,
       empr_nr_cnpj_df,
       fore_nm_fonte_receita
from   msa msa,
       cliente cli,
       (select 'FAT' fonte,
               celu_nm_centro_lucro,
               business_mgr,
               cd_contrato_pratica,
               copr_nm_contrato_pratica,
               msaa_cd_msa,
               prat_sg_pratica,
               defi_nm_deal_fiscal,
               defi_cd_deal_fiscal,
               mes,
               vl_rev_value,
               vl_rev_assoc,
               fatu_cd_fatura,
               vl_invoice,
               vl_inv_assoc,
               redf_cd_receita_dfiscal,
               moed_cd_moeda,
               clie_cd_cliente_df,
               clie_nm_cliente_df,
               empr_cd_empresa_df,
               empr_nm_empresa_df,
               empr_nr_cnpj_df,
               fore_nm_fonte_receita
        from   (          select null /*pc.*/nacl_cd_natureza,
                        null /* pc.*/nacl_nm_natureza,
                        null  /*pc.*/celu_nm_centro_lucro,
                        null /*pc2.celu_nm_centro_lucro*/ business_mgr,
                        null  /*pc.copr_cd_contrato_pratica */cd_contrato_pratica,
                        null  /*cp.copr_cd_contrato_pratica */cod1,
                        null /*ifat.copr_cd_contrato_pratica*/ cod2,
                        null /*cp.*/copr_nm_contrato_pratica, -- renato
                        /*cp.*/msa.msaa_cd_msa, -- renato
                        null /*pr.*/prat_sg_pratica,
                        df.defi_nm_deal_fiscal,
                        df.defi_cd_deal_fiscal,
                        trunc(fa.fatu_dt_previsao, 'mm') mes,
                        0 vl_rev_value,
                        0 vl_rev_assoc,
                        fa.fatu_cd_fatura,
                        ifat.itfa_cd_item_fatura,
                        ifat.itfa_vl_item,
                        sum(ifat.itfa_vl_item)over(partition by
                        trunc(fa.fatu_dt_previsao, 'mm'),
                        fr.redf_cd_receita_dfiscal,
                        df.defi_cd_deal_fiscal,
                        fa.fatu_cd_fatura) vl_invoice,
                        nvl(sum(fr.fare_vl_receita_associada)
                        over(partition by  ifat.itfa_cd_item_fatura, /* NOBORU - 25 - FEV - 2013  -- DUPLICAÇÃO DO VL ASSOCIADO*/
                        trunc(fa.fatu_dt_previsao, 'mm'),
                        df.defi_cd_deal_fiscal,
                        fa.fatu_cd_fatura),
                   0) vl_inv_assoc,
                        null redf_cd_receita_dfiscal,
                        ftr.fore_cd_fonte_receita,
                        ftr.fore_nm_fonte_receita,
                        fa.moed_cd_moeda,
                        cdf.clie_cd_cliente clie_cd_cliente_df,
                        cdf.clie_nm_cliente clie_nm_cliente_df,
                        edf.empr_cd_empresa empr_cd_empresa_df,
                        edf.empr_nm_empresa empr_nm_empresa_df,
                        edf.empr_nr_cnpj    empr_nr_cnpj_df,
                        row_number() over(partition by /*cp.copr_cd_contrato_pratica,*/ trunc(fa.fatu_dt_previsao, 'mm'), df.defi_cd_deal_fiscal, fa.fatu_cd_fatura
                                         order by /*cp.copr_cd_contrato_pratica,*/ trunc(fa.fatu_dt_previsao, 'mm')) nlinha
                from   msa                      msa,
                        fatura        fa,
                        item_fatura              ifat,
                        fonte_receita            ftr,
                        deal_fiscal              df,
                        cliente                  cdf,
                        empresa                  edf,
                        fatura_receita           fr
                where  msa.msaa_cd_msa = df.msaa_cd_msa
                  and  ifat.fatu_cd_fatura = fa.fatu_cd_fatura
                  and  ifat.fore_cd_fonte_receita = ftr.fore_cd_fonte_receita
                  and  fr.fatu_cd_fatura(+) = fa.fatu_cd_fatura
                  and  fa.defi_cd_deal_fiscal = df.defi_cd_deal_fiscal
                  and  df.clie_cd_cliente     = cdf.clie_cd_cliente(+)
                  and  df.empr_cd_empresa     = edf.empr_cd_empresa(+)
                  and fa.fatu_in_status = 'SB'
                  and ifat.fore_cd_fonte_receita = 1 -- RENATO
                  and trunc(fa.fatu_dt_previsao, 'mm') >= '01-jan-2013'
                       )
        where  nlinha = 1) inf
where  msa.msaa_cd_msa = inf.msaa_cd_msa
       and msa.clie_cd_cliente = cli.clie_cd_cliente
);