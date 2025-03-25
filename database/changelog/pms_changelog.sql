--liquibase formatted sql

--changeset rbastos:7 dbms:oracle 
--comment: Cria��o de tabela de associa��o entre Fiscal Deal e Condi��o de Pagamento
CREATE SEQUENCE SQ_DFCP_CD_DF_COND_PAGTO minvalue 1 maxvalue 999999999999999999999999999999 start with 1 increment by 1 cache 2;
CREATE TABLE DEAL_FISCAL_COND_PAGTO
(
	DFCP_CD_DEAL_FISCAL_COND_PAGTO NUMBER(18) NOT NULL,
	DEFI_CD_DEAL_FISCAL NUMBER(18) NOT NULL,
	DFCP_ST_COND_PAGTO  VARCHAR2 (100) NOT NULL,
	DFCP_DT_INICIO_VIGENCIA DATE NULL,
	DFCP_DT_FIM_VIGENCIA DATE NULL,
	DFCP_DT_ALTERACAO  DATE NULL,
	DFCP_CD_LOGIN_AUTOR  VARCHAR2(50) NULL	
);

ALTER TABLE DEAL_FISCAL_COND_PAGTO
  ADD CONSTRAINT DFCP_PK PRIMARY KEY (DFCP_CD_DEAL_FISCAL_COND_PAGTO);
  
ALTER TABLE DEAL_FISCAL_COND_PAGTO
   ADD CONSTRAINT FK_DFCP_DEFI FOREIGN KEY (DEFI_CD_DEAL_FISCAL)
   REFERENCES DEAL_FISCAL (DEFI_CD_DEAL_FISCAL);  
  
COMMENT ON COLUMN DEAL_FISCAL_COND_PAGTO.DFCP_CD_DEAL_FISCAL_COND_PAGTO IS 'Codigo identificador da tabela';
COMMENT ON COLUMN DEAL_FISCAL_COND_PAGTO.DEFI_CD_DEAL_FISCAL IS 'Codigo do deal fiscal no PMS';
COMMENT ON COLUMN DEAL_FISCAL_COND_PAGTO.DFCP_ST_COND_PAGTO IS 'Nome da condicao de pagamento do Mega';
COMMENT ON COLUMN DEAL_FISCAL_COND_PAGTO.DFCP_DT_INICIO_VIGENCIA IS 'Data de inicio da vigencia da condicao de pagamento';
COMMENT ON COLUMN DEAL_FISCAL_COND_PAGTO.DFCP_DT_FIM_VIGENCIA IS 'Data final da vigencia da condicao de pagamento';
COMMENT ON COLUMN DEAL_FISCAL_COND_PAGTO.DFCP_DT_ALTERACAO IS 'Data de alteracao do registro';
COMMENT ON COLUMN DEAL_FISCAL_COND_PAGTO.DFCP_CD_LOGIN_AUTOR IS 'Login do usuario que incluiu/alterou';
   
--rollback
--DROP TABLE DEAL_FISCAL_COND_PAGTO; DROP sequence SQ_DFCP_CD_DF_COND_PAGTO;


--changeset josef:8 dbms:oracle 
--comment: Add column CLIE_CD_AGENTE_MEGA in Cliente table
alter table cliente add CLIE_CD_AGENTE_MEGA number(10);
COMMENT ON COLUMN cliente.CLIE_CD_AGENTE_MEGA IS 'Ccdigo do Agente do Mega';

--rollback
--ALTER TABLE cliente DROP COLUMN CLIE_CD_AGENTE_MEGA;

--changeset rbastos:9 dbms:oracle
--comment: Alter constraint AVCON_1262885501_REDF__000 in RECEITA_DEAL_FISCAL table
ALTER TABLE RECEITA_DEAL_FISCAL
DROP CONSTRAINT AVCON_1262885501_REDF__000;

ALTER TABLE RECEITA_DEAL_FISCAL
ADD CONSTRAINT AVCON_1262885501_REDF__000 CHECK 
(REDF_IN_STATUS IN ('I', 'E', 'W','F', 'P'));
--rollback
--ALTER TABLE RECEITA_DEAL_FISCAL
--DROP CONSTRAINT AVCON_1262885501_REDF__000;
--ALTER TABLE RECEITA_DEAL_FISCAL
--ADD CONSTRAINT AVCON_1262885501_REDF__000 CHECK 
--(REDF_IN_STATUS IN ('I', 'E', 'W','F'));

--changeset rbastos:10 dbms:oracle
--comment: Cria��o da view VW_PMS_INTEG_REVENUE_NATIONAL
CREATE OR REPLACE VIEW VW_PMS_INTEG_REVENUE_NATIONAL AS
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       R.RECE_DT_MES             NOT_DT_EMISSAO,
       R.RECE_DT_MES             NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RDF.REDF_VL_RECEITA       NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RDF.REDF_VL_RECEITA       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND R.RECE_DT_MES >= '01/01/2018'
AND DF.DEFI_IN_INTERCOMPANY = 'N'
AND (EMP.EMPR_CD_EMPRESA_MATRIZ = 1 AND DF.MOED_CD_MOEDA = 1)

UNION ALL

-- Receita de servi�o intercompany
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       R.RECE_DT_MES             NOT_DT_EMISSAO,
       R.RECE_DT_MES             NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                     from receita_deal_fiscal rdf1
                     inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                     inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                     inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                     inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                     inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                     inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                     inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                     inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                     where df1.defi_in_intercompany = 'Y'
                     and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                    from empresa e2
                    where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                     and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                     AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                     AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                     group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                     from receita_deal_fiscal rdf1
                     inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                     inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                     inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                     inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                     inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                     inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                     inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                     inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                     where df1.defi_in_intercompany = 'Y'
                     and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                    from empresa e2
                    where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                     and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                     AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                     AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                     group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) ITN_RE_VALORUNITARIO,
       39                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE
FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND R.RECE_DT_MES >= '01/01/2018'
AND RM.REMO_VL_TOTAL_MOEDA > 0
AND DF.DEFI_IN_INTERCOMPANY = 'Y'

UNION ALL

--Receita de licen�a
SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       RL.RELI_DT_MES            NOT_DT_EMISSAO,
       RL.RELI_DT_MES            NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RL.RELI_VL_RECEITA        NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RL.RELI_VL_RECEITA        ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RL.RELI_VL_RECEITA        MOV_RE_VALORMOE
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = RL.copr_cd_contrato_pratica and RL.RELI_DT_MES between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS = 'W'
AND RL.RELI_DT_MES >= '01/01/2018'

UNION ALL

--Receita tipo exporta��o
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       R.RECE_DT_MES             NOT_DT_EMISSAO,
       R.RECE_DT_MES             NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2) NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND EMP.EMPR_CD_EMPRESA_MATRIZ = 1
AND DF.MOED_CD_MOEDA <> 1

UNION ALL

SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'DUTY_HOURS'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       R.RECE_DT_MES             NOT_DT_EMISSAO,
       R.RECE_DT_MES             NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
      RP.REPL_VL_RECEITA_PLANTAO NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
      RP.REPL_VL_RECEITA_PLANTAO ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
      RP.REPL_VL_RECEITA_PLANTAO MOV_RE_VALORMOE
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 7
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RP.REPL_IN_INTEGRACAO = 'N' AND RP.REPL_VL_RECEITA_PLANTAO > 0;
--rollback
--drop view vw_pms_integ_revenue_national;

--changeset vnogueira:11 dbms:oracle 
--comment: Ajuste no nome da coluna CUS_IN_REDUZIDO da VW_PMS_INTEG_REVENUE_NATIONAL
CREATE OR REPLACE FORCE VIEW "VW_PMS_INTEG_REVENUE_NATIONAL" ("REVENUE_CODE", "REVENUE_SOURCE", "IS_INTERCOMPANY", "NOTA_FISCAL_OPERACAO", "FIL_IN_CODIGO", "NOT_DT_EMISSAO", "NOT_DT_SAIDA", "COND_ST_CODIGO", "NOT_RE_VALORTOTAL", "NOT_CH_SITUACAO", "TPD_IN_CODIGO", "AGN_TAU_ST_CODIGO", "CALC_AGN_ST_CODIGO", "CALC_AGN_ST_TIPOCODIGO", "ITEM_NOTA_FISCAL_OPERACAO", "ITN_IN_SEQUENCIA", "PRO_IN_CODIGO", "ITN_RE_QUANTIDADE", "ITN_RE_VALORUNITARIO", "APL_IN_CODIGO", "TPC_ST_CLASSE", "PROJ_IDE_ST_CODIGO", "PROJ_IN_REDUZIDO", "CUS_IDE_ST_CODIGO", "CUS_IN_REDUZIDO", "COS_IN_CODIGO", "PARCELAS_OPERACAO", "MOV_ST_PARCELA", "MOV_RE_VALORMOE") AS
  SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       R.RECE_DT_MES             NOT_DT_EMISSAO,
       R.RECE_DT_MES             NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RDF.REDF_VL_RECEITA       NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RDF.REDF_VL_RECEITA       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND R.RECE_DT_MES >= '01/01/2018'
AND DF.DEFI_IN_INTERCOMPANY = 'N'
AND (EMP.EMPR_CD_EMPRESA_MATRIZ = 1 AND DF.MOED_CD_MOEDA = 1)

UNION ALL

-- Receita de servi�o intercompany
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       R.RECE_DT_MES             NOT_DT_EMISSAO,
       R.RECE_DT_MES             NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                     from receita_deal_fiscal rdf1
                     inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                     inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                     inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                     inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                     inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                     inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                     inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                     inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                     where df1.defi_in_intercompany = 'Y'
                     and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                    from empresa e2
                    where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                     and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                     AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                     AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                     group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                     from receita_deal_fiscal rdf1
                     inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                     inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                     inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                     inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                     inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                     inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                     inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                     inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                     where df1.defi_in_intercompany = 'Y'
                     and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                    from empresa e2
                    where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                     and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                     AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                     AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                     group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) ITN_RE_VALORUNITARIO,
       39                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE
FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND R.RECE_DT_MES >= '01/01/2018'
AND RM.REMO_VL_TOTAL_MOEDA > 0
AND DF.DEFI_IN_INTERCOMPANY = 'Y'

UNION ALL

--Receita de licen�a
SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       RL.RELI_DT_MES            NOT_DT_EMISSAO,
       RL.RELI_DT_MES            NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RL.RELI_VL_RECEITA        NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RL.RELI_VL_RECEITA        ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RL.RELI_VL_RECEITA        MOV_RE_VALORMOE
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = RL.copr_cd_contrato_pratica and RL.RELI_DT_MES between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS = 'W'
AND RL.RELI_DT_MES >= '01/01/2018'

UNION ALL

--Receita tipo exporta��o
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       R.RECE_DT_MES             NOT_DT_EMISSAO,
       R.RECE_DT_MES             NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2) NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND EMP.EMPR_CD_EMPRESA_MATRIZ = 1
AND DF.MOED_CD_MOEDA <> 1

UNION ALL

SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'DUTY_HOURS'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       R.RECE_DT_MES             NOT_DT_EMISSAO,
       R.RECE_DT_MES             NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
      RP.REPL_VL_RECEITA_PLANTAO NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
      RP.REPL_VL_RECEITA_PLANTAO ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
      RP.REPL_VL_RECEITA_PLANTAO MOV_RE_VALORMOE
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 7
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RP.REPL_IN_INTEGRACAO = 'N' AND RP.REPL_VL_RECEITA_PLANTAO > 0;
--rollback
--drop view vw_pms_integ_revenue_national;

--changeset vnogueira:12 dbms:oracle 
--comment: Inclusao do TO_DATE nas datas presentes nas clausulas WHERE
CREATE OR REPLACE FORCE VIEW "VW_PMS_INTEG_REVENUE_NATIONAL" ("REVENUE_CODE", "REVENUE_SOURCE", "IS_INTERCOMPANY", "NOTA_FISCAL_OPERACAO", "FIL_IN_CODIGO", "NOT_DT_EMISSAO", "NOT_DT_SAIDA", "COND_ST_CODIGO", "NOT_RE_VALORTOTAL", "NOT_CH_SITUACAO", "TPD_IN_CODIGO", "AGN_TAU_ST_CODIGO", "CALC_AGN_ST_CODIGO", "CALC_AGN_ST_TIPOCODIGO", "ITEM_NOTA_FISCAL_OPERACAO", "ITN_IN_SEQUENCIA", "PRO_IN_CODIGO", "ITN_RE_QUANTIDADE", "ITN_RE_VALORUNITARIO", "APL_IN_CODIGO", "TPC_ST_CLASSE", "PROJ_IDE_ST_CODIGO", "PROJ_IN_REDUZIDO", "CUS_IDE_ST_CODIGO", "CUS_IN_REDUZIDO", "COS_IN_CODIGO", "PARCELAS_OPERACAO", "MOV_ST_PARCELA", "MOV_RE_VALORMOE") AS
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       R.RECE_DT_MES             NOT_DT_EMISSAO,
       R.RECE_DT_MES             NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RDF.REDF_VL_RECEITA       NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RDF.REDF_VL_RECEITA       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND DF.DEFI_IN_INTERCOMPANY = 'N'
AND (EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3) AND DF.MOED_CD_MOEDA = 1)

UNION ALL

-- Receita de servi�o intercompany
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       R.RECE_DT_MES             NOT_DT_EMISSAO,
       R.RECE_DT_MES             NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                     from receita_deal_fiscal rdf1
                     inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                     inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                     inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                     inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                     inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                     inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                     inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                     inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                     where df1.defi_in_intercompany = 'Y'
                     and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                    from empresa e2
                    where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                     and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                     AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                     AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                     group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                     from receita_deal_fiscal rdf1
                     inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                     inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                     inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                     inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                     inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                     inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                     inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                     inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                     where df1.defi_in_intercompany = 'Y'
                     and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                    from empresa e2
                    where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                     and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                     AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                     AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                     group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) ITN_RE_VALORUNITARIO,
       39                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE
FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND RM.REMO_VL_TOTAL_MOEDA > 0
AND DF.DEFI_IN_INTERCOMPANY = 'Y'

UNION ALL

--Receita de licen�a
SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       RL.RELI_DT_MES            NOT_DT_EMISSAO,
       RL.RELI_DT_MES            NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RL.RELI_VL_RECEITA        NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RL.RELI_VL_RECEITA        ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RL.RELI_VL_RECEITA        MOV_RE_VALORMOE
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = RL.copr_cd_contrato_pratica and RL.RELI_DT_MES between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS = 'W'
AND RL.RELI_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

UNION ALL

--Receita tipo exporta��o
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       R.RECE_DT_MES             NOT_DT_EMISSAO,
       R.RECE_DT_MES             NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2) NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)
AND DF.MOED_CD_MOEDA <> 1

UNION ALL

SELECT RP.REPL_CD_RECEITA_PLANTAO REVENUE_CODE,
       'DUTY_HOURS'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL    FIL_IN_CODIGO,
       R.RECE_DT_MES             NOT_DT_EMISSAO,
       R.RECE_DT_MES             NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
      RP.REPL_VL_RECEITA_PLANTAO NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
      RP.REPL_VL_RECEITA_PLANTAO ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       'Vd Serv'                 TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
      RP.REPL_VL_RECEITA_PLANTAO MOV_RE_VALORMOE
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 7
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RP.REPL_IN_INTEGRACAO = 'N' AND RP.REPL_VL_RECEITA_PLANTAO > 0;


--changeset josef:13 dbms:oracle endDelimiter:endProcedure rollbackSplitStatements:false 
--comment: Disable do job que integra as Faturas do MEGA para o PMS
declare
   l_job_exists number;          
begin
   select count(*) into l_job_exists
     from USER_JOBS 
    where job = '6769';

   if l_job_exists = 1 then
      dbms_job.broken(6769, true);
   end if;
end;
--endDelimiter:endProcedure rollbackSplitStatements:false

--changeset josef:14 dbms:oracle
--comment: Scripts Quickbooks
create table INTEGRACAO_QUICKBOOKS_ERROR
(
  REDF_CD_RECEITA_DFISCAL     NUMBER,
  TEXT                        VARCHAR2(4000),
  INCONSISTENCY_TYPE          NUMBER,
  IN_MAIL_SENT                VARCHAR2(1) default 'N',
  CREATE_AT                   DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
  -- Add comments to the columns
comment on column INTEGRACAO_QUICKBOOKS_ERROR.REDF_CD_RECEITA_DFISCAL
  is 'C�digo da Receita da tabela Receita Deal Fiscal';
comment on column INTEGRACAO_QUICKBOOKS_ERROR.INCONSISTENCY_TYPE
  is '1 - Inconsistencia x, 2 - Inconsistencia Y';
comment on column INTEGRACAO_QUICKBOOKS_ERROR.IN_MAIL_SENT
  is 'Flag Indicando se e-mail j� foi enviado para usuario';

-- Create table
create table LANCAMENTO_RECEITA_QUICKBOOKS
(
  REDF_CD_RECEITA_DFISCAL  NUMBER,
  VL_TOTAL       VARCHAR2(40),
  MEGA_CD_CCUSTO VARCHAR2(40),
  MEGA_CD_PROJ   VARCHAR2(40),
  DT_CREATION    DATE,
  DUE_DATE       DATE,
  MEMO           VARCHAR2(4000),
  LISTID_CUSTOMER  VARCHAR2(40),
  LISTID_ACCOUNT   VARCHAR2(40),
  LISTID_PROJECT   VARCHAR(40),
  NATUREZA         VARCHAR(1)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
   -- Add comments to the columns
comment on column LANCAMENTO_RECEITA_QUICKBOOKS.REDF_CD_RECEITA_DFISCAL
  is 'C�digo da Receita da tabela Receita Deal Fiscal';
     -- Add comments to the columns
comment on column LANCAMENTO_RECEITA_QUICKBOOKS.LISTID_CUSTOMER
  is 'C�digo da Customer do Quickbooks';
comment on column LANCAMENTO_RECEITA_QUICKBOOKS.LISTID_ACCOUNT
  is 'C�digo da Account do Quickbooks';
 comment on column LANCAMENTO_RECEITA_QUICKBOOKS.LISTID_PROJECT
  is 'C�digo da Project do Quickbooks';
 comment on column LANCAMENTO_RECEITA_QUICKBOOKS.NATUREZA
  is 'D - D�bito e C - Cr�dito';

create table INTEG_PARAMETRO_QUICKBOOKS
(
  TISE_CD_TIPO_SERVICO  NUMBER,
  FORE_CD_FONTE_RECEITA NUMBER,
  REVENUE_ACCOUNT_NUMBER       VARCHAR(20),
  INVOICE_ACCOUNT_NUMBER       VARCHAR(20)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
   comment on column INTEG_PARAMETRO_QUICKBOOKS.TISE_CD_TIPO_SERVICO
  is 'C�digo do Tipo de Servi�o do PMS';
  comment on column INTEG_PARAMETRO_QUICKBOOKS.FORE_CD_FONTE_RECEITA
  is 'C�digo do Fonte Receita do PMS';
 comment on column INTEG_PARAMETRO_QUICKBOOKS.REVENUE_ACCOUNT_NUMBER
  is 'Numero da conta cont�bil do Quickbooks para Receita';
   comment on column INTEG_PARAMETRO_QUICKBOOKS.INVOICE_ACCOUNT_NUMBER
  is 'Numero da conta cont�bil do Quickbooks para Invoice';

--Desenvolvimento
  INSERT INTO INTEG_PARAMETRO_QUICKBOOKS (TISE_CD_TIPO_SERVICO,FORE_CD_FONTE_RECEITA, REVENUE_ACCOUNT_NUMBER, INVOICE_ACCOUNT_NUMBER)
  VALUES (1, 1, '4001001', '101004');

  --Suporte
    INSERT INTO INTEG_PARAMETRO_QUICKBOOKS (TISE_CD_TIPO_SERVICO,FORE_CD_FONTE_RECEITA, REVENUE_ACCOUNT_NUMBER, INVOICE_ACCOUNT_NUMBER)
  VALUES (2, 1, '4001002', '101001');

  --License
    INSERT INTO INTEG_PARAMETRO_QUICKBOOKS (TISE_CD_TIPO_SERVICO,FORE_CD_FONTE_RECEITA, REVENUE_ACCOUNT_NUMBER, INVOICE_ACCOUNT_NUMBER)
  VALUES (3, 4, '4001003', '101005');

  --Reimb. Exp
    INSERT INTO INTEG_PARAMETRO_QUICKBOOKS (TISE_CD_TIPO_SERVICO,FORE_CD_FONTE_RECEITA, REVENUE_ACCOUNT_NUMBER, INVOICE_ACCOUNT_NUMBER)
  VALUES (1, 2, '4001004', '101006');

  --consultoria
    INSERT INTO INTEG_PARAMETRO_QUICKBOOKS (TISE_CD_TIPO_SERVICO,FORE_CD_FONTE_RECEITA, REVENUE_ACCOUNT_NUMBER, INVOICE_ACCOUNT_NUMBER)
  VALUES (7, 1, '4001005', '101002');

  --contractors
    INSERT INTO INTEG_PARAMETRO_QUICKBOOKS (TISE_CD_TIPO_SERVICO,FORE_CD_FONTE_RECEITA, REVENUE_ACCOUNT_NUMBER, INVOICE_ACCOUNT_NUMBER)
  VALUES (1, 3, '4001006', '101007');

  --Duty Hours
  INSERT INTO INTEG_PARAMETRO_QUICKBOOKS (TISE_CD_TIPO_SERVICO,FORE_CD_FONTE_RECEITA, REVENUE_ACCOUNT_NUMBER, INVOICE_ACCOUNT_NUMBER)
  VALUES (1, 7, '4004001', '101004');

-- Create table
create table LOG_INTEG_QUICKBOOKS
(
  LOIQ_CD_INTEG_QUICKBOOKS   NUMBER not null,
  LOIQ_CD_REVENUE_CODE       NUMBER,
  LOIQ_CD_REVENUE_SOURCE     NUMBER,
  LOIQ_DT_CREATED            DATE not null,
  LOIQ_MOTIVO_ERRO           VARCHAR2(2000) not null,
  LOIQ_INFO_AUX              VARCHAR2(4000)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );


  -- Create sequence
create sequence SQ_LOIQ_CD_INTEG_QB
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;

--rollback
--drop table INTEGRACAO_QUICKBOOKS_ERROR;
--drop table LANCAMENTO_RECEITA_QUICKBOOKS;
--drop table LOG_INTEG_QUICKBOOKS;
--drop table INTEG_PARAMETRO_QUICKBOOKS;
--drop sequence SQ_LOIQ_CD_INTEG_QB;


--changeset josef:15 dbms:oracle
--comment: View Receita Internacional
CREATE OR REPLACE VIEW VW_PMS_INTEG_REVENUE_INTERNAC AS

--RECEITA INTERNACIONAL
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       R.RECE_DT_MES             RECE_DT_MES,
       RDF.REDF_VL_RECEITA       VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE,
       IPQ.REVENUE_ACCOUNT_NUMBER
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1 
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 1
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS in( 'E', 'P'))
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')  
AND (EMP.EMPR_CD_EMPRESA_MATRIZ = 9 AND DF.MOED_CD_MOEDA <> 1)

union all 
--RECEITA DUTY HOURS INTERNACIONAL
SELECT RP.REPL_CD_RECEITA_PLANTAO REVENUE_CODE,
       'DUTY_HOURS'               REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       R.RECE_DT_MES             RECE_DT_MES,
       RP.REPL_VL_RECEITA_PLANTAO       VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE,
       IPQ.REVENUE_ACCOUNT_NUMBER 
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 7
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 7
WHERE RP.REPL_VL_RECEITA_PLANTAO > 0
and RP.REPL_IN_INTEGRACAO = 'N'
AND (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS in( 'E', 'P'))
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')  

union all

--Receita de licen�a
SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
        DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       RL.RELI_DT_MES            RECE_DT_MES,
       RL.RELI_VL_RECEITA        VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       RL.RELI_VL_RECEITA        MOV_RE_VALORMOE,
       IPQ.REVENUE_ACCOUNT_NUMBER  
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 4
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS in ('P')
AND RL.RELI_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND (EMP.EMPR_CD_EMPRESA_MATRIZ = 9 AND DF.MOED_CD_MOEDA <> 1);

--changeset rbastos:16 dbms:oracle
--comment: Adi��o da coluna de condi��o de pagamento na tabela fatura
ALTER TABLE FATURA ADD FATU_ST_COND_PGTO VARCHAR2(100);
COMMENT ON COLUMN FATURA.FATU_ST_COND_PGTO IS 'Descri��o da condi��o de pagamento do Mega';
--rollback
--ALTER TABLE FATURA DROP COLUMN FATU_ST_COND_PGTO;

--changeset rbastos:17 dbms:oracle
--comment: Alter constraint AVCON_1273685685_FATU__000 in FATURA table
ALTER TABLE FATURA
DROP CONSTRAINT AVCON_1273685685_FATU__000;

ALTER TABLE FATURA
ADD CONSTRAINT AVCON_1273685685_FATU__000 CHECK 
(FATU_IN_STATUS IN ('AP', 'OP', 'SB', 'IN', 'ER', 'CA', 'PD'));
--rollback
--ALTER TABLE FATURA
--DROP CONSTRAINT AVCON_1273685685_FATU__000;
--ALTER TABLE RECEITA_DEAL_FISCAL
--ADD CONSTRAINT AVCON_1273685685_FATU__000 CHECK 
--(FATU_IN_STATUS IN ('AP', 'OP', 'SB', 'IN', 'ER', 'CA'));

--changeset rbastos:18 dbms:oracle
--comment: Alter constraint AVCON_1305568240_HIFA__001 e AVCON_1305568240_HIFA__000 in HISTORICO_FATURA table
ALTER TABLE HISTORICO_FATURA
DROP CONSTRAINT AVCON_1305568240_HIFA__001;
ALTER TABLE HISTORICO_FATURA
DROP CONSTRAINT AVCON_1305568240_HIFA__000;

ALTER TABLE HISTORICO_FATURA
ADD CONSTRAINT AVCON_1305568240_HIFA__001 CHECK
(HIFA_IN_STATUS_ATUAL IN ('AP', 'CA', 'ER', 'IN', 'OP', 'SB', 'PD'));
ALTER TABLE HISTORICO_FATURA
ADD CONSTRAINT AVCON_1305568240_HIFA__000 CHECK
(HIFA_IN_STATUS_ANTERIOR IN ('AP', 'CA', 'ER', 'IN', 'OP', 'SB', 'PD'));
--rollback
--ALTER TABLE HISTORICO_FATURA
--DROP CONSTRAINT AVCON_1305568240_HIFA__001;
--ALTER TABLE HISTORICO_FATURA
--DROP CONSTRAINT AVCON_1305568240_HIFA__000;
--ALTER TABLE HISTORICO_FATURA
--ADD CONSTRAINT AVCON_1305568240_HIFA__001 CHECK
--(HIFA_IN_STATUS_ATUAL IN ('AP', 'CA', 'ER', 'IN', 'OP', 'SB'));
--ALTER TABLE HISTORICO_FATURA
--ADD CONSTRAINT AVCON_1305568240_HIFA__000 CHECK
--(HIFA_IN_STATUS_ANTERIOR IN ('AP', 'CA', 'ER', 'IN', 'OP', 'SB'));

--changeset rbastos:19 dbms:oracle
--comment: Cria��o da view vw_pms_integ_invoice_national
CREATE OR REPLACE VIEW VW_PMS_INTEG_INVOICE_NATIONAL AS
SELECT IF.ITFA_CD_ITEM_FATURA              ID_VIEW,
       F.FATU_CD_FATURA                    INVOICE_CODE,
       'I'                                 NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL              FIL_IN_CODIGO,
       TO_DATE(SYSDATE, 'DD/MM/RRRR')      NOT_DT_EMISSAO,
       TO_DATE(SYSDATE, 'DD/MM/RRRR')      NOT_DT_SAIDA,
       F.FATU_ST_COND_PGTO                 COND_ST_CODIGO,
       'N'                                 NOT_CH_SITUACAO,
       CASE EMP.EMPR_CD_ERP_FILIAL WHEN 3162  THEN 44
                                   WHEN 1976  THEN 148
                                   WHEN 2872  THEN 61
                                   WHEN 12803 THEN 62
                                   WHEN 3     THEN 146
                                   WHEN 3163  THEN 147 END TPD_IN_CODIGO,
       'C'                                 AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA               CALC_AGN_ST_CODIGO,
       'COD'                               CALC_AGN_ST_TIPOCODIGO,
       'I'                                 ITEM_NOTA_FISCAL_OPERACAO,
       ROWNUM                              ITN_IN_SEQUENCIA,
       I.INFP_CD_ERP_ITEM                  PRO_IN_CODIGO,
       1                                   ITN_RE_QUANTIDADE,
       IF.ITFA_VL_ITEM                     ITN_RE_VALORUNITARIO,
       26                                  APL_IN_CODIGO,
        DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       NVL(PP.PRO_IDE_ST_CODIGO, EMP.EMPR_CD_ERP_PROJ_IDE) PROJ_IDE_ST_CODIGO,
       NVL(PP.COD_IN_PROJETO, EMP.EMPR_CD_ERP_PROJETO)     PROJ_IN_REDUZIDO,
       NVL(CC.CUS_IDE_ST_CODIGO, EMP.EMPR_CD_ERP_CUS_IDE)  CUS_IDE_ST_CODIGO,
       NVL(CC.CUS_IN_REDUZIDO, EMP.EMPR_CD_ERP_CCUSTO)      CUS_IN_REDUZIDO,
       I.INFP_CD_ERP_SERVICO               COS_IN_CODIGO,
       'I'                                 OBSERVACAO_NF_OPERACAO,
       'N'                                 NOB_CH_TIPO_OBSERVACAO,
       F.FATU_TX_OBSERVACAO                NOB_ST_OBSERVACAO,
       'I'                                 PARCELAS_OPERACAO,
       1                                   MOV_ST_PARCELA,
       F.FATU_DT_VENCIMENTO                MOV_DT_VENCTO,
       (SELECT SUM(IF1.ITFA_VL_ITEM) FROM FATURA F1 JOIN ITEM_FATURA IF1 ON F1.FATU_CD_FATURA = IF1.FATU_CD_FATURA WHERE F1.FATU_CD_FATURA = F.FATU_CD_FATURA) MOV_RE_VALORMOE
       FROM FATURA F
       JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
       JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
       LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
       LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
       LEFT JOIN INTEG_FATURA_PARAMETRO I on I.TISE_CD_TIPO_SERVICO = IF.TISE_CD_TIPO_SERVICO AND I.EMPR_CD_EMPRESA = DF.EMPR_CD_EMPRESA AND I.FORE_CD_FONTE_RECEITA = IF.FORE_CD_FONTE_RECEITA
       LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN VW_MEGA_CCUSTO CC ON CONV.CONV_CD_CCUSTO_MEGA = CC.CUS_IN_REDUZIDO
       LEFT JOIN VW_MEGA_PROJETO PP ON CONV.CONV_CD_PROJETO_MEGA = PP.COD_IN_PROJETO
       WHERE F.FATU_IN_STATUS IN ('AP', 'ER')
       and EMP.EMPR_CD_EMPRESA_MATRIZ IN (1,3);

--changeset josef:20 dbms:oracle
--comment: Ajustes na View conforme MGPK-874 e MGPK-875
CREATE OR REPLACE VIEW VW_PMS_INTEG_REVENUE_NATIONAL AS
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RDF.REDF_VL_RECEITA       NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RDF.REDF_VL_RECEITA       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND DF.DEFI_IN_INTERCOMPANY = 'N'
AND (EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3) AND DF.MOED_CD_MOEDA = 1)
AND RDF.REDF_VL_RECEITA > 0

UNION ALL

-- Receita de servi�o intercompany
SELECT REVENUE_CODE,
       REVENUE_SOURCE,
       IS_INTERCOMPANY,
       NOTA_FISCAL_OPERACAO,
       FIL_IN_CODIGO,
       NOT_DT_EMISSAO,
       NOT_DT_SAIDA,
       COND_ST_CODIGO,
       NOT_RE_VALORTOTAL,
       NOT_CH_SITUACAO,
       TPD_IN_CODIGO,
       AGN_TAU_ST_CODIGO,
       CALC_AGN_ST_CODIGO,
       CALC_AGN_ST_TIPOCODIGO,
       ITEM_NOTA_FISCAL_OPERACAO,
       ITN_IN_SEQUENCIA,
       PRO_IN_CODIGO,
       ITN_RE_QUANTIDADE,
       ITN_RE_VALORUNITARIO,
       APL_IN_CODIGO,
       TPC_ST_CLASSE,
       PROJ_IDE_ST_CODIGO,
       PROJ_IN_REDUZIDO,
       CUS_IDE_ST_CODIGO,
       CUS_IN_REDUZIDO,
       COS_IN_CODIGO,
       PARCELAS_OPERACAO,
       MOV_ST_PARCELA,
       MOV_RE_VALORMOE
    FROM (
        SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
               'SERVICES'                REVENUE_SOURCE,
               DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
               'I'                       NOTA_FISCAL_OPERACAO,
               decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
               LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
               LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
               'PMS'                     COND_ST_CODIGO,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) NOT_RE_VALORTOTAL,
               'N'                       NOT_CH_SITUACAO,
               38                        TPD_IN_CODIGO,
               'C'                       AGN_TAU_ST_CODIGO,
               2421     CALC_AGN_ST_CODIGO,
               'COD'                     CALC_AGN_ST_TIPOCODIGO,
               'I'                       ITEM_NOTA_FISCAL_OPERACAO,
               1                         ITN_IN_SEQUENCIA,
               i.inrp_cd_erp_item        PRO_IN_CODIGO,
               1                         ITN_RE_QUANTIDADE,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) ITN_RE_VALORUNITARIO,
               39                        APL_IN_CODIGO,
               DECODE(
                EMP.EMPR_CD_EMPRESA_MATRIZ,
                        3, 'DA-Vd S',
                           'Vd Serv')    TPC_ST_CLASSE,
               EMP.EMPR_CD_EMPRESA_MATRIZ,
               MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
               CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
               MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
               GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
               i.inrp_cd_erp_servico     COS_IN_CODIGO,
               'I'                       PARCELAS_OPERACAO,
               1                         MOV_ST_PARCELA,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) MOV_RE_VALORMOE
        FROM RECEITA R
        JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
        JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
        JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
        LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
        LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
        LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
        LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
        LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
        LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
        LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
        LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
        LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
        INNER JOIN HISTORICO_PERCENTUAL_INTERCOMP hpi ON hpi.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL AND hpi.HIPI_DT_FIM is null
        WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
        AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
        AND RM.REMO_VL_TOTAL_MOEDA > 0
        AND DF.DEFI_IN_INTERCOMPANY = 'Y'
        AND hpi.EMPR_CD_EMP_INTERCOMP in (select emp_int.EMPR_CD_EMPRESA from EMPRESA emp_int where emp_int.EMPR_CD_EMPRESA_MATRIZ = 1)) inter
    WHERE inter.NOT_RE_VALORTOTAL > 0

UNION ALL

--Receita de licen�a
SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)   FIL_IN_CODIGO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_EMISSAO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RL.RELI_VL_RECEITA        NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RL.RELI_VL_RECEITA        ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RL.RELI_VL_RECEITA        MOV_RE_VALORMOE
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = RL.copr_cd_contrato_pratica and RL.RELI_DT_MES between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS = 'W'
AND RL.RELI_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND RL.RELI_VL_RECEITA > 0
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)

UNION ALL

--Receita tipo exporta��o
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2) NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)
AND DF.MOED_CD_MOEDA <> 1
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

UNION ALL

SELECT RP.REPL_CD_RECEITA_PLANTAO REVENUE_CODE,
       'DUTY_HOURS'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
      RP.REPL_VL_RECEITA_PLANTAO NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
      RP.REPL_VL_RECEITA_PLANTAO ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
      RP.REPL_VL_RECEITA_PLANTAO MOV_RE_VALORMOE
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 7
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RP.REPL_IN_INTEGRACAO = 'N' AND RP.REPL_VL_RECEITA_PLANTAO > 0
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

UNION ALL

SELECT IF.ITFA_CD_ITEM_FATURA        REVENUE_CODE,
       'REIMB. EXP'                  REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY       IS_INTERCOMPANY,
       'I'                           NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)        FIL_IN_CODIGO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_EMISSAO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_SAIDA,
       'PMS'                         COND_ST_CODIGO,
       IF.ITFA_VL_ITEM               NOT_RE_VALORTOTAL,
       'N'                           NOT_CH_SITUACAO,
       38                            TPD_IN_CODIGO,
       'C'                           AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA         CALC_AGN_ST_CODIGO,
       'COD'                         CALC_AGN_ST_TIPOCODIGO,
       'I'                           ITEM_NOTA_FISCAL_OPERACAO,
       1                             ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item            PRO_IN_CODIGO,
       1                             ITN_RE_QUANTIDADE,
       IF.ITFA_VL_ITEM               ITN_RE_VALORUNITARIO,
       26                            APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')        TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO          PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA     PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO          CUS_IDE_ST_CODIGO,
       MC.cus_in_reduzido            CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico         COS_IN_CODIGO,
       'I'                           PARCELAS_OPERACAO,
       1                             MOV_ST_PARCELA,
       IF.ITFA_VL_ITEM               MOV_RE_VALORMOE
FROM FATURA F
JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL and IF.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = IF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = if.fore_cd_fonte_receita
LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE F.FATU_IN_STATUS IN ('AP', 'ER')
and EMP.EMPR_CD_EMPRESA_MATRIZ IN (1,3)
and if.fore_cd_fonte_receita IN (2,3)
and f.fatu_dt_previsao >= TO_DATE('01/01/2018', 'dd/MM/yyyy');

--changeset josef:21 dbms:oracle
--comment: Ajustes na tabela de parametros MGPK-961
update integ_receita_parametro irp set inrp_cd_erp_servico = 1 where irp.empr_cd_empresa = 8 and irp.fore_cd_fonte_receita in (2,3);

--changeset rbastos:22 dbms:oracle
--comment: Cria��o da view que indica qual a conta de d�bito do Mega por centro de custo no PMS
CREATE OR REPLACE VIEW VW_PMS_GC_CONTA_DEBITO AS
select gc.grcu_cd_grupo_custo,
       gc.grcu_nm_grupo_custo,
       c.conv_cd_ccusto_mega COS_IN_REDUZIDO,
       c.conv_cd_projeto_mega PROJ_IN_REDUZIDO,
       c.conv_nm_projeto_mega NM_PROJ_MEGA,
       c.copr_cd_contrato_pratica,
       ta.tiar_cd_tipo_area,
       ta.tiar_nm_tipo_area,
       case ta.tiar_cd_tipo_area
         when 1 then
          '3143044' --Prod 
         when 3 then
          '3163040' --ADM 
         when 2 then
          '3153043' --COM 
         when 4 then
          '3194016' --P&D 
       end CONTA_CONTABIL_DEBITO
  from grupo_custo gc
 inner join area a on a.area_cd_area = gc.area_cd_area
 inner join tipo_area ta on ta.tiar_cd_tipo_area = a.tiar_cd_tipo_area
 inner join convergencia c on c.grcu_cd_grupo_custo =
                              gc.grcu_cd_grupo_custo
 where gc.grcu_in_ativo = 'A';

--changeset victorh:23 dbms:oracle
--comment: Cria��o da tabela de licen�a projeto
 --Create sequence
CREATE SEQUENCE SQ_LIPR_CD_LICENCA_SW_PROJETO minvalue 1 maxvalue 999999999999999999999999999999 start with 1 increment by 1 cache 2;

--Create table
create table LICENCA_SW_PROJETO
(
LIPR_CD_LICENCA_SW_PROJETO NUMBER(18)     NOT NULL, 
LIPR_DT_START_DATE         DATE           NOT NULL,
TIRE_CD_TI_RECURSO         NUMBER(18)     NOT NULL, 
LIPR_TICKET_ID             VARCHAR2(50)   NOT NULL,
LIPR_NOTA_FISCAL           NUMBER(18)     NOT NULL,
LIPR_DESCRICAO             VARCHAR2(2000) NULL,
MOED_CD_MOEDA              NUMBER(18)     NOT NULL, 
LIPR_VALOR_TOTAL           NUMBER         NOT NULL,
LIPR_QUANTIDADE_PARECELAS  NUMBER(2)      NOT NULL
);

ALTER TABLE LICENCA_SW_PROJETO
ADD CONSTRAINT PK_LIPR primary key (LIPR_CD_LICENCA_SW_PROJETO);

ALTER TABLE LICENCA_SW_PROJETO
ADD CONSTRAINT FK_LIPR_TIRE_RECURSO foreign key (TIRE_CD_TI_RECURSO)
references TI_RECURSO (TIRE_CD_TI_RECURSO);

ALTER TABLE LICENCA_SW_PROJETO
ADD CONSTRAINT FK_LIPR_MOED foreign key (MOED_CD_MOEDA)
references MOEDA (MOED_CD_MOEDA);

COMMENT ON COLUMN LICENCA_SW_PROJETO.LIPR_CD_LICENCA_SW_PROJETO IS 'ID sequencial da tabela';
COMMENT ON COLUMN LICENCA_SW_PROJETO.LIPR_DT_START_DATE IS 'Data in�cio da licen�a';
COMMENT ON COLUMN LICENCA_SW_PROJETO.TIRE_CD_TI_RECURSO IS 'C�digo da Licen�a/Recurso';
COMMENT ON COLUMN LICENCA_SW_PROJETO.LIPR_TICKET_ID IS 'N�mero ou C�digo do Chamado de solicita��o da lincen�a';
COMMENT ON COLUMN LICENCA_SW_PROJETO.LIPR_NOTA_FISCAL IS 'N�mero da Nota Fiscal de Compra da lincen�a';
COMMENT ON COLUMN LICENCA_SW_PROJETO.LIPR_DESCRICAO IS 'Descri��o da licen�a (campo livre)';
COMMENT ON COLUMN LICENCA_SW_PROJETO.MOED_CD_MOEDA IS 'C�digo da Moeda utilizada na compra da licen�a';
COMMENT ON COLUMN LICENCA_SW_PROJETO.LIPR_VALOR_TOTAL IS 'Valor total da Nota Fiscal';
COMMENT ON COLUMN LICENCA_SW_PROJETO.LIPR_QUANTIDADE_PARECELAS IS 'Quantidade de Parcelas que a licen�a ser� apropriada';

--rollback
--drop table LICENCA_SW_PROJETO;
--drop sequence SQ_LIPR_CD_LICENCA_SW_PROJETO;

--changeset victorh:24 dbms:oracle
--comment: Cria��o da tabela de licen�a projeto parcela
--Create sequence
CREATE SEQUENCE SQ_LIPP_CD_LIC_SW_PROJ_PARC minvalue 1 maxvalue 999999999999999999999999999999 start with 1 increment by 1 cache 2;

--Create table
create table LICENCA_SW_PROJETO_PARCELA
(
LIPP_CD_LICENCA_SW_PROJ_PARC  NUMBER(18)   NOT NULL, 
LIPR_CD_LICENCA_SW_PROJETO    NUMBER(18)   NOT NULL, 
LIPP_VALOR_PARCELA            NUMBER       NOT NULL,
LIPP_DATE                     DATE         NOT NULL,
LIPP_NUMERO_PARCELA           NUMBER(2)    NOT NULL,
LIPP_TIPO_PARCELA             VARCHAR2(5)  NOT NULL,
COPR_CD_CONTRATO_PRATICA      NUMBER(18)   NOT NULL, 
LIPP_IN_STATUS                VARCHAR2(20) NULL      
);

ALTER TABLE LICENCA_SW_PROJETO_PARCELA
ADD CONSTRAINT PK_LIPP primary key (LIPP_CD_LICENCA_SW_PROJ_PARC);

ALTER TABLE LICENCA_SW_PROJETO_PARCELA
ADD CONSTRAINT FK_LIPP_LIPR_LICENCA foreign key (LIPR_CD_LICENCA_SW_PROJETO)
references LICENCA_SW_PROJETO (LIPR_CD_LICENCA_SW_PROJETO);

ALTER TABLE LICENCA_SW_PROJETO_PARCELA
ADD CONSTRAINT FK_LIPP_COPR_CONTRATO_PRATICA foreign key (COPR_CD_CONTRATO_PRATICA)
references CONTRATO_PRATICA (COPR_CD_CONTRATO_PRATICA);

ALTER TABLE LICENCA_SW_PROJETO_PARCELA
ADD CONSTRAINT CK_LIPP_STATUS
check (LIPP_IN_STATUS in ('PENDENTE','INTEGRADO'));

COMMENT ON COLUMN LICENCA_SW_PROJETO_PARCELA.LIPP_CD_LICENCA_SW_PROJ_PARC IS 'ID sequencial da tabela filha';
COMMENT ON COLUMN LICENCA_SW_PROJETO_PARCELA.LIPR_CD_LICENCA_SW_PROJETO IS 'ID sequencial da tabela pai';
COMMENT ON COLUMN LICENCA_SW_PROJETO_PARCELA.LIPP_VALOR_PARCELA IS 'Valor calculado da parcela para apropria��o mensal';
COMMENT ON COLUMN LICENCA_SW_PROJETO_PARCELA.LIPP_DATE IS 'Data da parcela';
COMMENT ON COLUMN LICENCA_SW_PROJETO_PARCELA.LIPP_NUMERO_PARCELA IS 'N�mero da parcela';
COMMENT ON COLUMN LICENCA_SW_PROJETO_PARCELA.LIPP_TIPO_PARCELA IS 'Tipo da Parcela (curto ou longo prazo)';
COMMENT ON COLUMN LICENCA_SW_PROJETO_PARCELA.COPR_CD_CONTRATO_PRATICA IS 'Contrato pr�tica da parcela da licen�a';
COMMENT ON COLUMN LICENCA_SW_PROJETO_PARCELA.LIPP_IN_STATUS IS 'Status da Apropria��o da Parcela (Pendente ou Integrado)';

--ROLLBACK
--DROP TABLE LICENCA_SW_PROJETO_PARCELA;
--DROP SEQUENCE SQ_LIPP_CD_LIC_SW_PROJ_PARC;

--changeset rbastos:25 dbms:oracle
--comment: Adi��o da coluna de condi��o de pagamento na tabela fatura_apagada
ALTER TABLE FATURA_APAGADA ADD FAAP_ST_COND_PGTO VARCHAR2(100);
COMMENT ON COLUMN FATURA_APAGADA.FAAP_ST_COND_PGTO IS 'Descri��o da condi��o de pagamento do Mega';
--rollback
--ALTER TABLE FATURA_APAGADA DROP COLUMN FAAP_ST_COND_PGTO;

--changeset josef:26 dbms:oracle
--comment: Adi��o op��o tipo recurso
ALTER TABLE TI_RECURSO DROP CONSTRAINT AVCON_1311779368_TIRE__000;

alter table TI_RECURSO
  add constraint AVCON_1311779368_TIRE__000
  check (TIRE_IN_TIPO_ALOCACAO IN ('CS', 'SU','SOFTWARE_PROJECT'));

--changeset rbastos:27 dbms:oracle
--comment: Adi��o da coluna de erro, status e c�digo de licen�a na tabela rateio_licenca_sw
ALTER TABLE RATEIO_LICENCA_SW ADD TIRE_CD_TI_RECURSO NUMBER(18);
ALTER TABLE RATEIO_LICENCA_SW ADD RELI_TX_ERROR VARCHAR2(4000);
ALTER TABLE RATEIO_LICENCA_SW ADD RELI_IN_STATUS VARCHAR2(20);
COMMENT ON COLUMN RATEIO_LICENCA_SW.TIRE_CD_TI_RECURSO IS 'C�digo da lincen�a de software';
COMMENT ON COLUMN RATEIO_LICENCA_SW.RELI_TX_ERROR IS 'Mensagem de erro na integra��o da licen�a de software';
COMMENT ON COLUMN RATEIO_LICENCA_SW.RELI_IN_STATUS IS 'Status da integra��o da licen�a de software';
--rollback
--ALTER TABLE RATEIO_LICENCA_SW DROP COLUMN TIRE_CD_TI_RECURSO;
--ALTER TABLE RATEIO_LICENCA_SW DROP COLUMN RELI_TX_ERROR;
--ALTER TABLE RATEIO_LICENCA_SW DROP COLUMN RELI_IN_STATUS;

--changeset rbastos:28 dbms:oracle
--comment: Adi��o da coluna de erro na tabela licenca_sw_projeto_parcela
ALTER TABLE LICENCA_SW_PROJETO_PARCELA ADD LIPP_TX_ERROR VARCHAR2(4000);
COMMENT ON COLUMN LICENCA_SW_PROJETO_PARCELA.LIPP_TX_ERROR IS 'Mensagem de erro na integra��o da licen�a de software';
--rollback
--ALTER TABLE LICENCA_SW_PROJETO_PARCELA DROP COLUMN LIPP_TX_ERROR;

--changeset rbastos:29 dbms:oracle
--comment: Adi��o da coluna de erro na tabela licenca_sw_projeto_parcela
update rateio_licenca_sw rls set rls.tire_cd_ti_recurso = 176 where rls.tire_nm_ti_recurso = 'Adobe Photoshop CC';
update rateio_licenca_sw rls set rls.tire_cd_ti_recurso = 105 where rls.tire_nm_ti_recurso = 'Adobe Creative Cloud';
update rateio_licenca_sw rls set rls.tire_cd_ti_recurso = 121 where rls.tire_nm_ti_recurso = 'Visual Studio MSDN Standard';
update rateio_licenca_sw rls set rls.tire_cd_ti_recurso = 122 where rls.tire_nm_ti_recurso = 'MS Project';
update rateio_licenca_sw rls set rls.tire_cd_ti_recurso = 123 where rls.tire_nm_ti_recurso = 'MS Visio';
update rateio_licenca_sw rls set rls.tire_cd_ti_recurso = 124 where rls.tire_nm_ti_recurso = 'MS Office';
update rateio_licenca_sw rls set rls.tire_cd_ti_recurso = 178 where rls.tire_nm_ti_recurso = 'InVision';

--changeset josef:30 dbms:oracle
--comment: Add  Constraint status
ALTER TABLE rateio_licenca_sw
ADD CONSTRAINT CK_RELI_STATUS
check (RELI_IN_STATUS in ('PENDENTE','INTEGRADO'));

--changeset josef:31 dbms:oracle
--comment: Constraint do tipo de parcela
alter table LICENCA_SW_PROJETO_PARCELA
  add constraint CK_LIPP_TIPO
  check (LIPP_IN_STATUS in ('CURTO','LONGO'));
  
--changeset josef:32 dbms:oracle
--comment: View Grupo Custpo x Conta Contabil
create or replace view vw_pms_gc_conta_debito as
select gc.grcu_cd_grupo_custo,
       gc.grcu_nm_grupo_custo,
       c.conv_cd_ccusto_mega COS_IN_REDUZIDO,
       c.conv_cd_projeto_mega PROJ_IN_REDUZIDO,
       c.conv_nm_projeto_mega NM_PROJ_MEGA,
       c.copr_cd_contrato_pratica,
       ta.tiar_cd_tipo_area,
       ta.tiar_nm_tipo_area,
       case ta.tiar_cd_tipo_area
         when 1 then
          '3143044'
         when 3 then
          '3163040'
         when 2 then
          '3153043'
         when 4 then
          '3194016'
       end CONTA_CONTABIL_DEBITO,
       '118104' CONTA_CONTABIL_CREDITO
  from grupo_custo gc
 inner join area a on a.area_cd_area = gc.area_cd_area
 inner join tipo_area ta on ta.tiar_cd_tipo_area = a.tiar_cd_tipo_area
 inner join convergencia c on c.grcu_cd_grupo_custo =
                              gc.grcu_cd_grupo_custo
 where gc.grcu_in_ativo = 'A';

--changeset victorh:33 dbms:oracle
--comment: Adicionado coluna para conta de debito para longo prazo
create or replace view vw_pms_gc_conta_debito as
select gc.grcu_cd_grupo_custo,
       gc.grcu_nm_grupo_custo,
       c.conv_cd_ccusto_mega COS_IN_REDUZIDO,
       c.conv_cd_projeto_mega PROJ_IN_REDUZIDO,
       c.conv_nm_projeto_mega NM_PROJ_MEGA,
       c.copr_cd_contrato_pratica,
       ta.tiar_cd_tipo_area,
       ta.tiar_nm_tipo_area,
       case ta.tiar_cd_tipo_area
         when 1 then
          '3143044'
         when 3 then
          '3163040'
         when 2 then
          '3153043'
         when 4 then
          '3194016'
       end CONTA_CONTABIL_DEBITO,
       '118104' CONTA_CONTABIL_CREDITO,
       '122102' CONTA_CONTABIL_DEBITO_LP
  from grupo_custo gc
 inner join area a on a.area_cd_area = gc.area_cd_area
 inner join tipo_area ta on ta.tiar_cd_tipo_area = a.tiar_cd_tipo_area
 inner join convergencia c on c.grcu_cd_grupo_custo =
                              gc.grcu_cd_grupo_custo
 where gc.grcu_in_ativo = 'A';

 --changeset josef:34 dbms:oracle
--comment: View integracao receita internacional
CREATE OR REPLACE VIEW VW_PMS_INTEG_REVENUE_INTERNAC AS
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE         NM_CLIENTE,
       CP.COPR_CD_CONTRATO_PRATICA,
       CP.COPR_NM_CONTRATO_PRATICA,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       R.RECE_DT_MES             RECE_DT_MES,
       RDF.REDF_VL_RECEITA       VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE,
       IPQ.REVENUE_ACCOUNT_NUMBER
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 1
Left join cliente c on c.clie_cd_cliente = df.clie_cd_cliente
LEFT JOIN CONTRATO_PRATICA CP ON CP.COPR_CD_CONTRATO_PRATICA = R.COPR_CD_CONTRATO_PRATICA
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') AND RDF.REDF_IN_STATUS in('E', 'P', 'I'))
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND (EMP.EMPR_CD_EMPRESA_MATRIZ = 9 AND DF.MOED_CD_MOEDA <> 1)

union all
--RECEITA DUTY HOURS INTERNACIONAL
SELECT RP.REPL_CD_RECEITA_PLANTAO REVENUE_CODE,
       'DUTY_HOURS'               REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE         NM_CLIENTE,
       CP.COPR_CD_CONTRATO_PRATICA,
       CP.COPR_NM_CONTRATO_PRATICA,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       R.RECE_DT_MES             RECE_DT_MES,
       RP.REPL_VL_RECEITA_PLANTAO       VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE,
       IPQ.REVENUE_ACCOUNT_NUMBER
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 7
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 7
Left join cliente c on c.clie_cd_cliente = df.clie_cd_cliente
LEFT JOIN CONTRATO_PRATICA CP ON CP.COPR_CD_CONTRATO_PRATICA = R.COPR_CD_CONTRATO_PRATICA
WHERE RP.REPL_VL_RECEITA_PLANTAO > 0
and RP.REPL_IN_INTEGRACAO = 'N'
AND (R.RECE_IN_VERSAO IN ('PB', 'PD') AND RDF.REDF_IN_STATUS in( 'E', 'P', 'I'))
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND (EMP.EMPR_CD_EMPRESA_MATRIZ = 9 AND DF.MOED_CD_MOEDA <> 1)

union all

--Receita de licen�a
SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE         NM_CLIENTE,
       CP.COPR_CD_CONTRATO_PRATICA,
       CP.COPR_NM_CONTRATO_PRATICA,
        DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       RL.RELI_DT_MES            RECE_DT_MES,
       RL.RELI_VL_RECEITA        VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       RL.RELI_VL_RECEITA        MOV_RE_VALORMOE,
       IPQ.REVENUE_ACCOUNT_NUMBER
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 4
Left join cliente c on c.clie_cd_cliente = df.clie_cd_cliente
LEFT JOIN CONTRATO_PRATICA CP ON CP.COPR_CD_CONTRATO_PRATICA = RL.COPR_CD_CONTRATO_PRATICA
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS in ('P', 'E')
AND RL.RELI_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND (EMP.EMPR_CD_EMPRESA_MATRIZ = 9 AND DF.MOED_CD_MOEDA <> 1);


--changeset luizsj:200 dbms:oracle
--comment: Constraint do tipo de parcela
ALTER TABLE LICENCA_SW_PROJETO_PARCELA
DROP CONSTRAINT CK_LIPP_TIPO;

--changeset luizsj:201 dbms:oracle
--comment: Constraint do tipo de parcela
alter table LICENCA_SW_PROJETO_PARCELA
  add constraint CK_LIPP_TIPO
  check (LIPP_TIPO_PARCELA in ('CURTO','LONGO'));

--changeset josef:37 dbms:oracle endDelimiter:endProcedure
--comment: Correcao do arredondamento do rateio das licencas de usuario
create or replace procedure USP_PMS_CALCULO_SW_LICENSE is
begin

DECLARE

v_vlr_full_rateio number;
v_vlr_final_rateio number;
v_indireto number;
vl_pct_aloc_prop number;

cursor c_chargeback_monthly is
 select p.pess_cd_pessoa, p.pess_cd_login, tr.tire_cd_ti_recurso, tr.tire_nm_ti_recurso, cti.cutr_vl_custo_ti_recurso, c.qtde, round(cti.cutr_vl_custo_ti_recurso / c.qtde,2) vl_unitario
   from custo_ti_recurso cti
   inner join ti_recurso tr on cti.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   inner join chargeback_pessoa cbp on cbp.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   inner join pessoa p on cbp.pess_cd_pessoa = p.pess_cd_pessoa
   inner join (select cbp1.tire_cd_ti_recurso, count(1) qtde
                               from chargeback_pessoa cbp1
                               where (cbp1.chpe_dt_fim is null or cbp1.chpe_dt_fim between trunc(sysdate + 30,'MM') and trunc(sysdate + 60,'MM'))
                               --where (cbp1.chpe_dt_fim is null or cbp1.chpe_dt_fim between trunc(sysdate ,'MM') and trunc(sysdate + 30,'MM')) --REPROCESSAR MES ANTERIOR
                               group by cbp1.tire_cd_ti_recurso) c on c.tire_cd_ti_recurso = cbp.tire_cd_ti_recurso
   where cti.cutr_dt_fim is null
   AND (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate + 30,'MM') and trunc(sysdate + 60,'MM'));
   --AND (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate,'MM') and trunc(sysdate + 30,'MM')); --REPROCESSAR MES ANTERIOR


--carrega alocaoes > 0 (cenario 1,2,3,4)
cursor c_alocacao (p_cd_pessoa number) is
        select  pess_cd_pessoa,
               pess_cd_login,
               trunc(sysdate, 'MM') dt_competencia,
               --trunc(sysdate - 30, 'MM') dt_competencia, --REPROCESSAR MES ANTERIOR
               clie_cd_cliente, clie_nm_cliente,
               copr_cd_contrato_pratica, copr_nm_contrato_pratica,
               conv_cd_projeto_mega, conv_nm_projeto_mega,
               conv_cd_ccusto_mega, grcu_cd_grupo_custo, grcu_nm_grupo_custo,
               pr_aloc,
               count(1) OVER () qtd_reg,
                sum(pr_aloc) over() sum_aloc_total
               from ( select p.pess_cd_pessoa,
                             p.pess_cd_login,
                             ap.alpe_dt_alocacao_periodo,
                             cp.copr_cd_contrato_pratica,
                             cp.copr_nm_contrato_pratica,
                             cli.clie_cd_cliente, cli.clie_nm_cliente,
                             c.conv_cd_projeto_mega, c.conv_nm_projeto_mega,
                             c.conv_cd_ccusto_mega, gc.grcu_cd_grupo_custo, gc.grcu_nm_grupo_custo,
                             sum(ap.alpe_pr_alocacao_periodo) AS PR_ALOC
                            from pessoa p
                            inner join alocacao a on a.recu_cd_recurso = p.recu_cd_recurso
                             inner join alocacao_periodo ap on a.aloc_cd_alocacao = ap.aloc_cd_alocacao
                             inner join mapa_alocacao ma on a.maal_cd_mapa_alocacao =  ma.maal_cd_mapa_alocacao
                             inner join contrato_pratica cp on ma.copr_cd_contrato_pratica =  cp.copr_cd_contrato_pratica
                             inner join convergencia c on cp.copr_cd_contrato_pratica = c.copr_cd_contrato_pratica
                             inner join grupo_custo gc on c.conv_cd_ccusto_mega = gc.erp_cd_ccusto
                             inner join msa m on  cp.msaa_cd_msa = m.msaa_cd_msa
                             inner join cliente cli on cli.clie_cd_cliente = m.clie_cd_cliente
                             where p.pess_cd_pessoa =   p_cd_pessoa
                             and ap.alpe_dt_alocacao_periodo = trunc(sysdate, 'MM')
                             --and ap.alpe_dt_alocacao_periodo = trunc(sysdate -30, 'MM') --REPROCESSAR MES ANTERIOR
                             and ap.alpe_pr_alocacao_periodo > 0
                             group by p.pess_cd_pessoa,
                                   p.pess_cd_login,
                                   ap.alpe_dt_alocacao_periodo,
                                   cp.copr_cd_contrato_pratica,
                                   cp.copr_nm_contrato_pratica,
                                   cli.clie_cd_cliente, cli.clie_nm_cliente,
                                   c.conv_cd_projeto_mega, c.conv_nm_projeto_mega,
                                   c.conv_cd_ccusto_mega, gc.grcu_cd_grupo_custo, gc.grcu_nm_grupo_custo) t1
                group by pess_cd_pessoa,
                         pess_cd_login,
                         alpe_dt_alocacao_periodo,
                          copr_cd_contrato_pratica,
                          copr_nm_contrato_pratica,
                          clie_cd_cliente,  clie_nm_cliente,
                         conv_cd_projeto_mega, conv_nm_projeto_mega,
                         conv_cd_ccusto_mega,grcu_cd_grupo_custo,grcu_nm_grupo_custo,
                         pr_aloc;

--carrega cenario 5,6
cursor c_sem_alocacao (p_cd_pessoa2 number) is
      select distinct  trunc(sysdate, 'MM') dt_competencia,
      --select distinct  trunc(sysdate -30, 'MM') dt_competencia, --REPROCESSAR MES ANTERIOR
                 p.pess_cd_login,
                 c.conv_cd_ccusto_mega,
                 c.conv_cd_projeto_mega,
                 c.conv_nm_projeto_mega,
                  gc.grcu_cd_grupo_custo,
                 gc.grcu_nm_grupo_custo,
                 cp.copr_cd_contrato_pratica,
                 cp.copr_nm_contrato_pratica,
                 cli.clie_cd_cliente,
                 cli.clie_nm_cliente
        from custo_ti_recurso cti
         inner join ti_recurso tr on cti.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
         inner join chargeback_pessoa cbp on cbp.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
         inner join pessoa p on cbp.pess_cd_pessoa = p.pess_cd_pessoa
         inner join pessoa_grupo_custo pgc on pgc.pess_cd_pessoa = p.pess_cd_pessoa and pgc.pegc_dt_fim is null
         inner join convergencia c on pgc.grcu_cd_grupo_custo = c.grcu_cd_grupo_custo   and c.copr_cd_contrato_pratica is null
         inner join grupo_custo gc on pgc.grcu_cd_grupo_custo = gc.grcu_cd_grupo_custo
         left join contrato_pratica cp on c.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica
         left join msa m on cp.msaa_cd_msa = m.msaa_cd_msa
         left join cliente cli on m.clie_cd_cliente = cli.clie_cd_cliente
        where cti.cutr_dt_fim is null
        and (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate + 30,'MM') and trunc(sysdate + 60,'MM'))
        --and (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate ,'MM') and trunc(sysdate + 30,'MM')) --REPROCESSAR MES ANTERIOR
        and p.pess_cd_pessoa = p_cd_pessoa2
        and (p.pess_dt_rescisao is null or  p.pess_dt_rescisao between trunc(sysdate,'MM') and trunc(sysdate + 30,'MM'))
        --and (p.pess_dt_rescisao is null or  p.pess_dt_rescisao between trunc(sysdate - 30,'MM') and trunc(sysdate,'MM')) --REPROCESSAR MES ANTERIOR
        and p.pess_cd_pessoa not in (select  p.pess_cd_pessoa
                                      from pessoa p
                                      inner join alocacao a on a.recu_cd_recurso = p.recu_cd_recurso
                                       inner join alocacao_periodo ap on a.aloc_cd_alocacao = ap.aloc_cd_alocacao
                                       inner join mapa_alocacao ma on a.maal_cd_mapa_alocacao =  ma.maal_cd_mapa_alocacao
                                       inner join contrato_pratica cp on ma.copr_cd_contrato_pratica =  cp.copr_cd_contrato_pratica
                                       inner join convergencia c on cp.copr_cd_contrato_pratica = c.copr_cd_contrato_pratica
                                       inner join grupo_custo gc on c.conv_cd_ccusto_mega = gc.erp_cd_ccusto
                                       where ap.alpe_dt_alocacao_periodo = trunc(sysdate, 'MM')
                                       --where ap.alpe_dt_alocacao_periodo = trunc(sysdate -30, 'MM') --REPROCESSAR MES ANTERIOR
                                       and ap.alpe_pr_alocacao_periodo > 0
                                       group by p.pess_cd_pessoa,
                                             p.pess_cd_login,
                                             ap.alpe_dt_alocacao_periodo,
                                             c.conv_cd_projeto_mega,c.conv_nm_projeto_mega,
                                             c.conv_cd_ccusto_mega, gc.grcu_nm_grupo_custo);

--Ajuste de arredondamento
cursor lic_sw_arredondar is
 select tr.tire_cd_ti_recurso,
        tr.tire_nm_ti_recurso,
        rlw.dt_competencia,
        cti.cutr_vl_custo_ti_recurso,
        sum(rlw.rali_vl_custo_licenca),
        round(sum(rlw.rali_vl_custo_licenca) - cti.cutr_vl_custo_ti_recurso,2) diff
   from custo_ti_recurso cti
   inner join ti_recurso tr on cti.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   inner join rateio_licenca_sw rlw on rlw.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   where cti.cutr_dt_fim is null
  -- and tr.tire_cd_ti_recurso = 124
   and rlw.dt_competencia >= trunc(sysdate, 'MM')
   group by tr.tire_cd_ti_recurso, tr.tire_nm_ti_recurso, rlw.dt_competencia,cti.cutr_vl_custo_ti_recurso;

cursor rateio_lic(p_cd_ti_recurso number) is
         select rlw.rali_cd_rateio_licenca, rlw.pess_cd_login, round(rlw.rali_vl_custo_licenca,2) valor
         from rateio_licenca_sw rlw
         where rlw.tire_cd_ti_recurso = p_cd_ti_recurso
         and rlw.dt_competencia = trunc(sysdate, 'MM');

BEGIN
--APAGAR PARA REPROCESSAR
DELETE FROM RATEIO_LICENCA_SW WHERE DT_COMPETENCIA >= trunc(sysdate, 'MM');
--DELETE FROM RATEIO_LICENCA_SW WHERE DT_COMPETENCIA >= trunc(sysdate -30, 'MM'); --REPROCESSAR MES ANTERIOR

--ENCERRAR COBRAN�A DOS RESCINDIDOS
 merge into chargeback_pessoa c
 using(select cbp.chpe_cd_chargeback_pess,  trunc(p.pess_dt_rescisao, 'MM') dt_fim
          from pessoa p
          inner join chargeback_pessoa cbp on p.pess_cd_pessoa = cbp.pess_cd_pessoa
         where p.pess_dt_rescisao is not null and cbp.chpe_dt_fim is null) s
    on (c.chpe_cd_chargeback_pess = s.chpe_cd_chargeback_pess)
    when matched then update set c.chpe_dt_fim = s.dt_fim;

for item in c_chargeback_monthly loop
begin
    v_vlr_full_rateio := item.vl_unitario;

         for aloc in c_alocacao(item.pess_cd_pessoa) loop

          --Cenario 1: alocacao full em 1 contrato apenas -> 1.0
          --Cenario 2: alocacao c/ feiras/licensa menor que entre 0 e 1 (0.96 de aloc e 0.04 de ferias)
           IF((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) AND aloc.qtd_reg = 1) THEN

             INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    Tire_Cd_Ti_Recurso)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    ALOC.pr_aloc,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_full_rateio,
                    item.tire_cd_ti_recurso);

          END if;

          --Cenario 3: Alocacao 100% compartilhada (ex: 0.5 e 0.5 de alocacao)
          IF((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) and aloc.qtd_reg > 1 and aloc.sum_aloc_total = 1) then

             INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    Tire_Cd_Ti_Recurso)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    ALOC.pr_aloc,
                    item.TIRE_NM_TI_RECURSO,
                    round(v_vlr_full_rateio * aloc.pr_aloc,2),
                    item.tire_cd_ti_recurso);

          end if;

          --Cenario 4: Alocacao compartilhada com arrasto (ex: 0.4 e 0.3 + 0.3 de arrasto)
          if ((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) and aloc.qtd_reg > 1  and aloc.sum_aloc_total < 1) then
             v_indireto := aloc.pr_aloc/aloc.sum_aloc_total * (aloc.sum_aloc_total -1) * -1;

             v_vlr_final_rateio := round(v_vlr_full_rateio * (aloc.pr_aloc + v_indireto),2);

            INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    TIRE_CD_TI_RECURSO)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    ALOC.pr_aloc,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_final_rateio,
                    item.tire_cd_ti_recurso);

          end if;
          -- Cenario 5: Super alocacao
          if ((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) and aloc.qtd_reg > 1  and aloc.sum_aloc_total > 1) then

          vl_pct_aloc_prop := 1 * (aloc.pr_aloc  / aloc.sum_aloc_total);

          v_vlr_final_rateio := round(v_vlr_full_rateio * vl_pct_aloc_prop,2);

           INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    TIRE_CD_TI_RECURSO)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    vl_pct_aloc_prop,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_final_rateio,
                    item.tire_cd_ti_recurso);

          end if;

         end loop;

         for sem_aloc in c_sem_alocacao (item.pess_cd_pessoa) loop

             INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    TIRE_CD_TI_RECURSO)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    sem_aloc.PESS_CD_LOGIN,
                    sem_aloc.dt_competencia,
                    sem_aloc.CONV_CD_CCUSTO_MEGA,
                    sem_aloc.CONV_CD_PROJETO_MEGA,
                    sem_aloc.conv_nm_projeto_mega,
                    sem_aloc.grcu_cd_grupo_custo,
                    sem_aloc.grcu_nm_grupo_custo,
                    sem_aloc.copr_cd_contrato_pratica,
                    sem_aloc.copr_nm_contrato_pratica,
                    sem_aloc.clie_cd_cliente,
                    sem_aloc.clie_nm_cliente,
                    null,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_full_rateio,
                    item.tire_cd_ti_recurso);

         end loop;

exception

when others then
     dbms_output.put_line('[ERRO]: ' || to_char(sqlerrm)  );
     raise_application_error(-20001,'An error was encountered '||' -ERROR- '||SQLERRM);

   rollback;
end;
end loop;
commit;

for item_arr in lic_sw_arredondar loop

   if(item_arr.diff <> 0) then

     for item_rat in rateio_lic(item_arr.tire_cd_ti_recurso) loop

       if(item_arr.diff <> 0) then
           if(item_arr.diff < 0) then
             update rateio_licenca_sw rlw
             set rlw.rali_vl_custo_licenca = rlw.rali_vl_custo_licenca + 0.01
             where rlw.rali_cd_rateio_licenca = item_rat.rali_cd_rateio_licenca;

             item_arr.diff := item_arr.diff + 0.01;
           else
             update rateio_licenca_sw rlw
             set rlw.rali_vl_custo_licenca = rlw.rali_vl_custo_licenca - 0.01
             where rlw.rali_cd_rateio_licenca = item_rat.rali_cd_rateio_licenca;

             item_arr.diff := item_arr.diff - 0.01;
           end if;
       end if;

     end loop;
   end if;
end loop;

commit;

end;

END USP_PMS_CALCULO_SW_LICENSE;

--changeset rbastos:38 dbms:oracle
--comment: Constraint do status de parcela
ALTER TABLE LICENCA_SW_PROJETO_PARCELA
DROP CONSTRAINT CK_LIPP_STATUS;

--changeset rbastos:39 dbms:oracle
--comment: Constraint do status de parcela
alter table LICENCA_SW_PROJETO_PARCELA
  add constraint CK_LIPP_STATUS
  check (LIPP_IN_STATUS in ('PENDENTE','INTEGRADO','ERRO'));
  
--changeset rbastos:40 dbms:oracle
--comment: Constraint do status de rateio licenca
ALTER TABLE RATEIO_LICENCA_SW
DROP CONSTRAINT CK_RELI_STATUS;

--changeset rbastos:41 dbms:oracle
--comment: Constraint do status de rateio licenca
alter table RATEIO_LICENCA_SW
  add constraint CK_RELI_STATUS
  check (RELI_IN_STATUS in ('PENDENTE','INTEGRADO','ERRO'));

--changeset luizsj:202 dbms:oracle
--comment: Add column GRCU_CD_GRUPO_CUSTO in LICENCA_SW_PROJETO table
ALTER TABLE LICENCA_SW_PROJETO_PARCELA ADD GRCU_CD_GRUPO_CUSTO NUMBER(18);
COMMENT ON COLUMN LICENCA_SW_PROJETO_PARCELA.GRCU_CD_GRUPO_CUSTO IS 'Ccdigo do Centro do custo do PMS';

--changeset josef:42 dbms:oracle endDelimiter:endProcedure
--comment: Correcao para nao recalcular licencas  integradas
create or replace procedure USP_PMS_CALCULO_SW_LICENSE is
begin

DECLARE

v_vlr_full_rateio number;
v_vlr_final_rateio number;
v_indireto number;
vl_pct_aloc_prop number;

cursor c_chargeback_monthly is
 select p.pess_cd_pessoa, p.pess_cd_login, tr.tire_cd_ti_recurso, tr.tire_nm_ti_recurso, cti.cutr_vl_custo_ti_recurso, c.qtde, round(cti.cutr_vl_custo_ti_recurso / c.qtde,2) vl_unitario
   from custo_ti_recurso cti
   inner join ti_recurso tr on cti.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   inner join chargeback_pessoa cbp on cbp.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   inner join pessoa p on cbp.pess_cd_pessoa = p.pess_cd_pessoa
   inner join (select cbp1.tire_cd_ti_recurso, count(1) qtde
                               from chargeback_pessoa cbp1
                               where (cbp1.chpe_dt_fim is null or cbp1.chpe_dt_fim between trunc(sysdate + 30,'MM') and trunc(sysdate + 60,'MM'))
                               --where (cbp1.chpe_dt_fim is null or cbp1.chpe_dt_fim between trunc(sysdate ,'MM') and trunc(sysdate + 30,'MM')) --REPROCESSAR MES ANTERIOR
                               group by cbp1.tire_cd_ti_recurso) c on c.tire_cd_ti_recurso = cbp.tire_cd_ti_recurso
   where cti.cutr_dt_fim is null
   AND (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate + 30,'MM') and trunc(sysdate + 60,'MM'))
   --AND (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate,'MM') and trunc(sysdate + 30,'MM')); --REPROCESSAR MES ANTERIOR
   and cbp.tire_cd_ti_recurso not in (select distinct tire_cd_ti_recurso from rateio_licenca_sw rls where rls.dt_competencia = trunc(sysdate, 'MM') and rls.reli_in_status is not null);



--carrega alocaoes > 0 (cenario 1,2,3,4)
cursor c_alocacao (p_cd_pessoa number) is
        select  pess_cd_pessoa,
               pess_cd_login,
               trunc(sysdate, 'MM') dt_competencia,
               --trunc(sysdate - 30, 'MM') dt_competencia, --REPROCESSAR MES ANTERIOR
               clie_cd_cliente, clie_nm_cliente,
               copr_cd_contrato_pratica, copr_nm_contrato_pratica,
               conv_cd_projeto_mega, conv_nm_projeto_mega,
               conv_cd_ccusto_mega, grcu_cd_grupo_custo, grcu_nm_grupo_custo,
               pr_aloc,
               count(1) OVER () qtd_reg,
                sum(pr_aloc) over() sum_aloc_total
               from ( select p.pess_cd_pessoa,
                             p.pess_cd_login,
                             ap.alpe_dt_alocacao_periodo,
                             cp.copr_cd_contrato_pratica,
                             cp.copr_nm_contrato_pratica,
                             cli.clie_cd_cliente, cli.clie_nm_cliente,
                             c.conv_cd_projeto_mega, c.conv_nm_projeto_mega,
                             c.conv_cd_ccusto_mega, gc.grcu_cd_grupo_custo, gc.grcu_nm_grupo_custo,
                             sum(ap.alpe_pr_alocacao_periodo) AS PR_ALOC
                            from pessoa p
                            inner join alocacao a on a.recu_cd_recurso = p.recu_cd_recurso
                             inner join alocacao_periodo ap on a.aloc_cd_alocacao = ap.aloc_cd_alocacao
                             inner join mapa_alocacao ma on a.maal_cd_mapa_alocacao =  ma.maal_cd_mapa_alocacao
                             inner join contrato_pratica cp on ma.copr_cd_contrato_pratica =  cp.copr_cd_contrato_pratica
                             inner join convergencia c on cp.copr_cd_contrato_pratica = c.copr_cd_contrato_pratica
                             inner join grupo_custo gc on c.conv_cd_ccusto_mega = gc.erp_cd_ccusto
                             inner join msa m on  cp.msaa_cd_msa = m.msaa_cd_msa
                             inner join cliente cli on cli.clie_cd_cliente = m.clie_cd_cliente
                             where p.pess_cd_pessoa =   p_cd_pessoa
                             and ap.alpe_dt_alocacao_periodo = trunc(sysdate, 'MM')
                             --and ap.alpe_dt_alocacao_periodo = trunc(sysdate -30, 'MM') --REPROCESSAR MES ANTERIOR
                             and ap.alpe_pr_alocacao_periodo > 0
                             group by p.pess_cd_pessoa,
                                   p.pess_cd_login,
                                   ap.alpe_dt_alocacao_periodo,
                                   cp.copr_cd_contrato_pratica,
                                   cp.copr_nm_contrato_pratica,
                                   cli.clie_cd_cliente, cli.clie_nm_cliente,
                                   c.conv_cd_projeto_mega, c.conv_nm_projeto_mega,
                                   c.conv_cd_ccusto_mega, gc.grcu_cd_grupo_custo, gc.grcu_nm_grupo_custo) t1
                group by pess_cd_pessoa,
                         pess_cd_login,
                         alpe_dt_alocacao_periodo,
                          copr_cd_contrato_pratica,
                          copr_nm_contrato_pratica,
                          clie_cd_cliente,  clie_nm_cliente,
                         conv_cd_projeto_mega, conv_nm_projeto_mega,
                         conv_cd_ccusto_mega,grcu_cd_grupo_custo,grcu_nm_grupo_custo,
                         pr_aloc;

--carrega cenario 5,6
cursor c_sem_alocacao (p_cd_pessoa2 number) is
      select distinct  trunc(sysdate, 'MM') dt_competencia,
      --select distinct  trunc(sysdate -30, 'MM') dt_competencia, --REPROCESSAR MES ANTERIOR
                 p.pess_cd_login,
                 c.conv_cd_ccusto_mega,
                 c.conv_cd_projeto_mega,
                 c.conv_nm_projeto_mega,
                  gc.grcu_cd_grupo_custo,
                 gc.grcu_nm_grupo_custo,
                 cp.copr_cd_contrato_pratica,
                 cp.copr_nm_contrato_pratica,
                 cli.clie_cd_cliente,
                 cli.clie_nm_cliente
        from custo_ti_recurso cti
         inner join ti_recurso tr on cti.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
         inner join chargeback_pessoa cbp on cbp.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
         inner join pessoa p on cbp.pess_cd_pessoa = p.pess_cd_pessoa
         inner join pessoa_grupo_custo pgc on pgc.pess_cd_pessoa = p.pess_cd_pessoa and pgc.pegc_dt_fim is null
         inner join convergencia c on pgc.grcu_cd_grupo_custo = c.grcu_cd_grupo_custo   and c.copr_cd_contrato_pratica is null
         inner join grupo_custo gc on pgc.grcu_cd_grupo_custo = gc.grcu_cd_grupo_custo
         left join contrato_pratica cp on c.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica
         left join msa m on cp.msaa_cd_msa = m.msaa_cd_msa
         left join cliente cli on m.clie_cd_cliente = cli.clie_cd_cliente
        where cti.cutr_dt_fim is null
        and (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate + 30,'MM') and trunc(sysdate + 60,'MM'))
        --and (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate ,'MM') and trunc(sysdate + 30,'MM')) --REPROCESSAR MES ANTERIOR
        and p.pess_cd_pessoa = p_cd_pessoa2
        and (p.pess_dt_rescisao is null or  p.pess_dt_rescisao between trunc(sysdate,'MM') and trunc(sysdate + 30,'MM'))
        --and (p.pess_dt_rescisao is null or  p.pess_dt_rescisao between trunc(sysdate - 30,'MM') and trunc(sysdate,'MM')) --REPROCESSAR MES ANTERIOR
        and p.pess_cd_pessoa not in (select  p.pess_cd_pessoa
                                      from pessoa p
                                      inner join alocacao a on a.recu_cd_recurso = p.recu_cd_recurso
                                       inner join alocacao_periodo ap on a.aloc_cd_alocacao = ap.aloc_cd_alocacao
                                       inner join mapa_alocacao ma on a.maal_cd_mapa_alocacao =  ma.maal_cd_mapa_alocacao
                                       inner join contrato_pratica cp on ma.copr_cd_contrato_pratica =  cp.copr_cd_contrato_pratica
                                       inner join convergencia c on cp.copr_cd_contrato_pratica = c.copr_cd_contrato_pratica
                                       inner join grupo_custo gc on c.conv_cd_ccusto_mega = gc.erp_cd_ccusto
                                       where ap.alpe_dt_alocacao_periodo = trunc(sysdate, 'MM')
                                       --where ap.alpe_dt_alocacao_periodo = trunc(sysdate -30, 'MM') --REPROCESSAR MES ANTERIOR
                                       and ap.alpe_pr_alocacao_periodo > 0
                                       group by p.pess_cd_pessoa,
                                             p.pess_cd_login,
                                             ap.alpe_dt_alocacao_periodo,
                                             c.conv_cd_projeto_mega,c.conv_nm_projeto_mega,
                                             c.conv_cd_ccusto_mega, gc.grcu_nm_grupo_custo);

--Ajuste de arredondamento
cursor lic_sw_arredondar is
 select tr.tire_cd_ti_recurso,
        tr.tire_nm_ti_recurso,
        rlw.dt_competencia,
        cti.cutr_vl_custo_ti_recurso,
        sum(rlw.rali_vl_custo_licenca),
        round(sum(rlw.rali_vl_custo_licenca) - cti.cutr_vl_custo_ti_recurso,2) diff
   from custo_ti_recurso cti
   inner join ti_recurso tr on cti.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   inner join rateio_licenca_sw rlw on rlw.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   where cti.cutr_dt_fim is null
  -- and tr.tire_cd_ti_recurso = 124
   and rlw.dt_competencia >= trunc(sysdate, 'MM')
   group by tr.tire_cd_ti_recurso, tr.tire_nm_ti_recurso, rlw.dt_competencia,cti.cutr_vl_custo_ti_recurso;

cursor rateio_lic(p_cd_ti_recurso number) is
         select rlw.rali_cd_rateio_licenca, rlw.pess_cd_login, round(rlw.rali_vl_custo_licenca,2) valor
         from rateio_licenca_sw rlw
         where rlw.tire_cd_ti_recurso = p_cd_ti_recurso
         and rlw.dt_competencia = trunc(sysdate, 'MM');

BEGIN
--APAGAR PARA REPROCESSAR
DELETE FROM RATEIO_LICENCA_SW sw WHERE DT_COMPETENCIA >= trunc(sysdate, 'MM') and sw.reli_in_status is null;
--DELETE FROM RATEIO_LICENCA_SW WHERE DT_COMPETENCIA >= trunc(sysdate -30, 'MM'); --REPROCESSAR MES ANTERIOR

--ENCERRAR COBRAN�A DOS RESCINDIDOS
 merge into chargeback_pessoa c
 using(select cbp.chpe_cd_chargeback_pess,  trunc(p.pess_dt_rescisao, 'MM') dt_fim
          from pessoa p
          inner join chargeback_pessoa cbp on p.pess_cd_pessoa = cbp.pess_cd_pessoa
         where p.pess_dt_rescisao is not null and cbp.chpe_dt_fim is null) s
    on (c.chpe_cd_chargeback_pess = s.chpe_cd_chargeback_pess)
    when matched then update set c.chpe_dt_fim = s.dt_fim;

for item in c_chargeback_monthly loop
begin
    v_vlr_full_rateio := item.vl_unitario;

         for aloc in c_alocacao(item.pess_cd_pessoa) loop

          --Cenario 1: alocacao full em 1 contrato apenas -> 1.0
          --Cenario 2: alocacao c/ feiras/licensa menor que entre 0 e 1 (0.96 de aloc e 0.04 de ferias)
           IF((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) AND aloc.qtd_reg = 1) THEN

             INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    Tire_Cd_Ti_Recurso)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    ALOC.pr_aloc,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_full_rateio,
                    item.tire_cd_ti_recurso);

          END if;

          --Cenario 3: Alocacao 100% compartilhada (ex: 0.5 e 0.5 de alocacao)
          IF((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) and aloc.qtd_reg > 1 and aloc.sum_aloc_total = 1) then

             INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    Tire_Cd_Ti_Recurso)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    ALOC.pr_aloc,
                    item.TIRE_NM_TI_RECURSO,
                    round(v_vlr_full_rateio * aloc.pr_aloc,2),
                    item.tire_cd_ti_recurso);

          end if;

          --Cenario 4: Alocacao compartilhada com arrasto (ex: 0.4 e 0.3 + 0.3 de arrasto)
          if ((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) and aloc.qtd_reg > 1  and aloc.sum_aloc_total < 1) then
             v_indireto := aloc.pr_aloc/aloc.sum_aloc_total * (aloc.sum_aloc_total -1) * -1;

             v_vlr_final_rateio := round(v_vlr_full_rateio * (aloc.pr_aloc + v_indireto),2);

            INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    TIRE_CD_TI_RECURSO)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    ALOC.pr_aloc,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_final_rateio,
                    item.tire_cd_ti_recurso);

          end if;
          -- Cenario 5: Super alocacao
          if ((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) and aloc.qtd_reg > 1  and aloc.sum_aloc_total > 1) then

          vl_pct_aloc_prop := 1 * (aloc.pr_aloc  / aloc.sum_aloc_total);

          v_vlr_final_rateio := round(v_vlr_full_rateio * vl_pct_aloc_prop,2);

           INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    TIRE_CD_TI_RECURSO)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    vl_pct_aloc_prop,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_final_rateio,
                    item.tire_cd_ti_recurso);

          end if;

         end loop;

         for sem_aloc in c_sem_alocacao (item.pess_cd_pessoa) loop

             INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    TIRE_CD_TI_RECURSO)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    sem_aloc.PESS_CD_LOGIN,
                    sem_aloc.dt_competencia,
                    sem_aloc.CONV_CD_CCUSTO_MEGA,
                    sem_aloc.CONV_CD_PROJETO_MEGA,
                    sem_aloc.conv_nm_projeto_mega,
                    sem_aloc.grcu_cd_grupo_custo,
                    sem_aloc.grcu_nm_grupo_custo,
                    sem_aloc.copr_cd_contrato_pratica,
                    sem_aloc.copr_nm_contrato_pratica,
                    sem_aloc.clie_cd_cliente,
                    sem_aloc.clie_nm_cliente,
                    null,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_full_rateio,
                    item.tire_cd_ti_recurso);

         end loop;

exception

when others then
     dbms_output.put_line('[ERRO]: ' || to_char(sqlerrm)  );
     raise_application_error(-20001,'An error was encountered '||' -ERROR- '||SQLERRM);

   rollback;
end;
end loop;
commit;

for item_arr in lic_sw_arredondar loop

   if(item_arr.diff <> 0) then

     for item_rat in rateio_lic(item_arr.tire_cd_ti_recurso) loop

       if(item_arr.diff <> 0) then
           if(item_arr.diff < 0) then
             update rateio_licenca_sw rlw
             set rlw.rali_vl_custo_licenca = rlw.rali_vl_custo_licenca + 0.01
             where rlw.rali_cd_rateio_licenca = item_rat.rali_cd_rateio_licenca;

             item_arr.diff := item_arr.diff + 0.01;
           else
             update rateio_licenca_sw rlw
             set rlw.rali_vl_custo_licenca = rlw.rali_vl_custo_licenca - 0.01
             where rlw.rali_cd_rateio_licenca = item_rat.rali_cd_rateio_licenca;

             item_arr.diff := item_arr.diff - 0.01;
           end if;
       end if;

     end loop;
   end if;
end loop;

commit;

end;

END USP_PMS_CALCULO_SW_LICENSE;

--changeset josef:43
--comment: Alteracao tamanho da coluna tipo_alocacao
alter table TI_RECURSO modify TIRE_IN_TIPO_ALOCACAO VARCHAR2(20);

--changeset luizsj:203
--comment: Alteracao coluna c-lob para nullable
alter table LICENCA_SW_PROJETO_PARCELA modify COPR_CD_CONTRATO_PRATICA NUMBER(18);

--changeset josef:44
--comment: Alteracao tabela quickbooks error
alter table INTEGRACAO_QUICKBOOKS_ERROR add FATU_CD_FATURA number(18);

--changeset josef:45
--comment: Inserts de novas combinacoes conta contabil para invoice internacional
INSERT INTO INTEG_PARAMETRO_QUICKBOOKS (TISE_CD_TIPO_SERVICO,FORE_CD_FONTE_RECEITA, REVENUE_ACCOUNT_NUMBER, INVOICE_ACCOUNT_NUMBER)
VALUES (2, 2, '4001004', '101006');
  
INSERT INTO INTEG_PARAMETRO_QUICKBOOKS (TISE_CD_TIPO_SERVICO,FORE_CD_FONTE_RECEITA, REVENUE_ACCOUNT_NUMBER, INVOICE_ACCOUNT_NUMBER)
VALUES (7, 2, '4001004', '101006');

--changeset josef:46
--comment: View invoice internacional
CREATE OR REPLACE VIEW VW_PMS_INTEG_INVOICE_INTERNAC AS
--Receita Contractors
SELECT F.FATU_CD_FATURA REVENUE_CODE,
       'CONTRACTORS'                REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE           NM_CLIENTE,
       CP.COPR_CD_CONTRATO_PRATICA,
       CP.COPR_NM_CONTRATO_PRATICA,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       F.FATU_DT_PREVISAO        RECE_DT_MES,
       IF.ITFA_VL_ITEM           VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       IF.ITFA_VL_ITEM           MOV_RE_VALORMOE,
       IPQ.INVOICE_ACCOUNT_NUMBER
       FROM FATURA F
       JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
       JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
       LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL and IF.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO
       LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
       LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
       LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 3
       WHERE F.FATU_IN_STATUS IN ('PD', 'ER')
       and EMP.EMPR_CD_EMPRESA_MATRIZ IN (9)
       and if.fore_cd_fonte_receita = 3
       and f.fatu_dt_previsao >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

union all

--Invoice
SELECT F.FATU_CD_FATURA REVENUE_CODE,
       'INVOICE'                REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE           NM_CLIENTE, 
       CP.COPR_CD_CONTRATO_PRATICA,
       CP.COPR_NM_CONTRATO_PRATICA,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       F.FATU_DT_PREVISAO        RECE_DT_MES,
       (SELECT SUM(IF1.ITFA_VL_ITEM) FROM FATURA F1 JOIN ITEM_FATURA IF1 ON F1.FATU_CD_FATURA = IF1.FATU_CD_FATURA WHERE F1.FATU_CD_FATURA = F.FATU_CD_FATURA)           VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       (SELECT SUM(IF1.ITFA_VL_ITEM) FROM FATURA F1 JOIN ITEM_FATURA IF1 ON F1.FATU_CD_FATURA = IF1.FATU_CD_FATURA WHERE F1.FATU_CD_FATURA = F.FATU_CD_FATURA)           MOV_RE_VALORMOE,
       IPQ.INVOICE_ACCOUNT_NUMBER
        FROM FATURA F
       JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
       JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
       LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL and IF.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO
       LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
       LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
       LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA =  if.fore_cd_fonte_receita
       WHERE F.FATU_IN_STATUS IN ('PD', 'ER')
       and EMP.EMPR_CD_EMPRESA_MATRIZ IN (9)
       and f.fatu_dt_previsao >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
      
       
union all

--Transitoria
SELECT distinct F.FATU_CD_FATURA REVENUE_CODE,
       'TRANSITORIA'                REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE           NM_CLIENTE, 
       null COPR_CD_CONTRATO_PRATICA,
       '' COPR_NM_CONTRATO_PRATICA,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       F.FATU_DT_PREVISAO        RECE_DT_MES,
       (SELECT SUM(IF1.ITFA_VL_ITEM) FROM FATURA F1 JOIN ITEM_FATURA IF1 ON F1.FATU_CD_FATURA = IF1.FATU_CD_FATURA WHERE F1.FATU_CD_FATURA = F.FATU_CD_FATURA)           VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       null PROJ_IN_REDUZIDO,
       (SELECT SUM(IF1.ITFA_VL_ITEM) FROM FATURA F1 JOIN ITEM_FATURA IF1 ON F1.FATU_CD_FATURA = IF1.FATU_CD_FATURA WHERE F1.FATU_CD_FATURA = F.FATU_CD_FATURA)           MOV_RE_VALORMOE,
       '4072' INVOICE_ACCOUNT_NUMBER
        FROM FATURA F
       JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
       JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
       LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL and IF.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO
       LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
       LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
       LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA =  if.fore_cd_fonte_receita
       WHERE F.FATU_IN_STATUS IN ('PD', 'ER')
       and EMP.EMPR_CD_EMPRESA_MATRIZ IN (9)
       and f.fatu_dt_previsao >= TO_DATE('01/01/2018', 'dd/MM/yyyy');
         
--changeset josef:47
--comment: Correcao defeito Intercompany com Duty HOURS
CREATE OR REPLACE VIEW VW_PMS_INTEG_REVENUE_NATIONAL AS
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RDF.REDF_VL_RECEITA       NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RDF.REDF_VL_RECEITA       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND DF.DEFI_IN_INTERCOMPANY = 'N'
AND (EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3) AND DF.MOED_CD_MOEDA = 1)
AND RDF.REDF_VL_RECEITA > 0

UNION ALL


SELECT REVENUE_CODE,
       REVENUE_SOURCE,
       IS_INTERCOMPANY,
       NOTA_FISCAL_OPERACAO,
       FIL_IN_CODIGO,
       NOT_DT_EMISSAO,
       NOT_DT_SAIDA,
       COND_ST_CODIGO,
       NOT_RE_VALORTOTAL,
       NOT_CH_SITUACAO,
       TPD_IN_CODIGO,
       AGN_TAU_ST_CODIGO,
       CALC_AGN_ST_CODIGO,
       CALC_AGN_ST_TIPOCODIGO,
       ITEM_NOTA_FISCAL_OPERACAO,
       ITN_IN_SEQUENCIA,
       PRO_IN_CODIGO,
       ITN_RE_QUANTIDADE,
       ITN_RE_VALORUNITARIO,
       APL_IN_CODIGO,
       TPC_ST_CLASSE,
       PROJ_IDE_ST_CODIGO,
       PROJ_IN_REDUZIDO,
       CUS_IDE_ST_CODIGO,
       CUS_IN_REDUZIDO,
       COS_IN_CODIGO,
       PARCELAS_OPERACAO,
       MOV_ST_PARCELA,
       MOV_RE_VALORMOE
    FROM (
        SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
               'SERVICES'                REVENUE_SOURCE,
               DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
               'I'                       NOTA_FISCAL_OPERACAO,
               decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
               LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
               LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
               'PMS'                     COND_ST_CODIGO,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) NOT_RE_VALORTOTAL,
               'N'                       NOT_CH_SITUACAO,
               38                        TPD_IN_CODIGO,
               'C'                       AGN_TAU_ST_CODIGO,
               2421     CALC_AGN_ST_CODIGO,
               'COD'                     CALC_AGN_ST_TIPOCODIGO,
               'I'                       ITEM_NOTA_FISCAL_OPERACAO,
               1                         ITN_IN_SEQUENCIA,
               i.inrp_cd_erp_item        PRO_IN_CODIGO,
               1                         ITN_RE_QUANTIDADE,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) ITN_RE_VALORUNITARIO,
               39                        APL_IN_CODIGO,
               DECODE(
                EMP.EMPR_CD_EMPRESA_MATRIZ,
                        3, 'DA-Vd S',
                           'Vd Serv')    TPC_ST_CLASSE,
               EMP.EMPR_CD_EMPRESA_MATRIZ,
               MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
               CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
               MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
               GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
               i.inrp_cd_erp_servico     COS_IN_CODIGO,
               'I'                       PARCELAS_OPERACAO,
               1                         MOV_ST_PARCELA,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) MOV_RE_VALORMOE
        FROM RECEITA R
        JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
        JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
        JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
        LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
        LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
        LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
        LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
        LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
        LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
        LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
        LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
        LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
        INNER JOIN HISTORICO_PERCENTUAL_INTERCOMP hpi ON hpi.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL AND hpi.HIPI_DT_FIM is null
        WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
        AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
        AND RM.REMO_VL_TOTAL_MOEDA > 0
        AND DF.DEFI_IN_INTERCOMPANY = 'Y'
        AND hpi.EMPR_CD_EMP_INTERCOMP in (select emp_int.EMPR_CD_EMPRESA from EMPRESA emp_int where emp_int.EMPR_CD_EMPRESA_MATRIZ = 1)) inter
    WHERE inter.NOT_RE_VALORTOTAL > 0

UNION ALL


SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)   FIL_IN_CODIGO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_EMISSAO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RL.RELI_VL_RECEITA        NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RL.RELI_VL_RECEITA        ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RL.RELI_VL_RECEITA        MOV_RE_VALORMOE
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = RL.copr_cd_contrato_pratica and RL.RELI_DT_MES between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS = 'W'
AND RL.RELI_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND RL.RELI_VL_RECEITA > 0
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)

UNION ALL


SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2) NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)
AND DF.MOED_CD_MOEDA <> 1
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

UNION ALL

SELECT RP.REPL_CD_RECEITA_PLANTAO REVENUE_CODE,
       'DUTY_HOURS'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
      RP.REPL_VL_RECEITA_PLANTAO NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
      RP.REPL_VL_RECEITA_PLANTAO ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
      RP.REPL_VL_RECEITA_PLANTAO MOV_RE_VALORMOE
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = decode(df.defi_in_intercompany,'Y',df.empr_cd_emp_intercomp,DF.EMPR_CD_EMPRESA) AND i.fore_cd_fonte_receita = 7
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RP.REPL_IN_INTEGRACAO = 'N' AND RP.REPL_VL_RECEITA_PLANTAO > 0
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

UNION ALL

SELECT IF.ITFA_CD_ITEM_FATURA        REVENUE_CODE,
       'REIMB. EXP'                  REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY       IS_INTERCOMPANY,
       'I'                           NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)        FIL_IN_CODIGO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_EMISSAO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_SAIDA,
       'PMS'                         COND_ST_CODIGO,
       IF.ITFA_VL_ITEM               NOT_RE_VALORTOTAL,
       'N'                           NOT_CH_SITUACAO,
       38                            TPD_IN_CODIGO,
       'C'                           AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA         CALC_AGN_ST_CODIGO,
       'COD'                         CALC_AGN_ST_TIPOCODIGO,
       'I'                           ITEM_NOTA_FISCAL_OPERACAO,
       1                             ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item            PRO_IN_CODIGO,
       1                             ITN_RE_QUANTIDADE,
       IF.ITFA_VL_ITEM               ITN_RE_VALORUNITARIO,
       26                            APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')        TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO          PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA     PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO          CUS_IDE_ST_CODIGO,
       MC.cus_in_reduzido            CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico         COS_IN_CODIGO,
       'I'                           PARCELAS_OPERACAO,
       1                             MOV_ST_PARCELA,
       IF.ITFA_VL_ITEM               MOV_RE_VALORMOE
FROM FATURA F
JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL and IF.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = IF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = if.fore_cd_fonte_receita
LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE F.FATU_IN_STATUS IN ('AP', 'ER')
and EMP.EMPR_CD_EMPRESA_MATRIZ IN (1,3)
and if.fore_cd_fonte_receita IN (2,3)
and f.fatu_dt_previsao >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

--changeset josef:48
--comment: Correcao defeito Intercompany com Duty HOURS
CREATE OR REPLACE VIEW VW_PMS_INTEG_REVENUE_INTERNAC AS
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE         NM_CLIENTE,
       CP.COPR_CD_CONTRATO_PRATICA,
       CP.COPR_NM_CONTRATO_PRATICA,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       R.RECE_DT_MES             RECE_DT_MES,
       RDF.REDF_VL_RECEITA       VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE,
       IPQ.REVENUE_ACCOUNT_NUMBER
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 1
Left join cliente c on c.clie_cd_cliente = df.clie_cd_cliente
LEFT JOIN CONTRATO_PRATICA CP ON CP.COPR_CD_CONTRATO_PRATICA = R.COPR_CD_CONTRATO_PRATICA
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') AND RDF.REDF_IN_STATUS in('E', 'P', 'I'))
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND (EMP.EMPR_CD_EMPRESA_MATRIZ = 9 AND DF.MOED_CD_MOEDA <> 1)

union all

SELECT RP.REPL_CD_RECEITA_PLANTAO REVENUE_CODE,
       'DUTY_HOURS'               REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE         NM_CLIENTE,
       CP.COPR_CD_CONTRATO_PRATICA,
       CP.COPR_NM_CONTRATO_PRATICA,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       R.RECE_DT_MES             RECE_DT_MES,
       RP.REPL_VL_RECEITA_PLANTAO       VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE,
       IPQ.REVENUE_ACCOUNT_NUMBER
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 7
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 7
Left join cliente c on c.clie_cd_cliente = df.clie_cd_cliente
LEFT JOIN CONTRATO_PRATICA CP ON CP.COPR_CD_CONTRATO_PRATICA = R.COPR_CD_CONTRATO_PRATICA
WHERE RP.REPL_VL_RECEITA_PLANTAO > 0
and RP.REPL_IN_INTEGRACAO in('N','I')
AND (R.RECE_IN_VERSAO IN ('PB', 'PD') AND RDF.REDF_IN_STATUS in( 'E', 'P', 'I'))
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND (EMP.EMPR_CD_EMPRESA_MATRIZ = 9 AND DF.MOED_CD_MOEDA <> 1)

union all


SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE         NM_CLIENTE,
       CP.COPR_CD_CONTRATO_PRATICA,
       CP.COPR_NM_CONTRATO_PRATICA,
        DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       RL.RELI_DT_MES            RECE_DT_MES,
       RL.RELI_VL_RECEITA        VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       RL.RELI_VL_RECEITA        MOV_RE_VALORMOE,
       IPQ.REVENUE_ACCOUNT_NUMBER
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 4
Left join cliente c on c.clie_cd_cliente = df.clie_cd_cliente
LEFT JOIN CONTRATO_PRATICA CP ON CP.COPR_CD_CONTRATO_PRATICA = RL.COPR_CD_CONTRATO_PRATICA
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS in ('P', 'E')
AND RL.RELI_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND (EMP.EMPR_CD_EMPRESA_MATRIZ = 9 AND DF.MOED_CD_MOEDA <> 1)

--changeset luizsj:204
--comment: Alteracao coluna LIPR_NOTA_FISCAL para nullable
alter table LICENCA_SW_PROJETO modify LIPR_NOTA_FISCAL NUMBER(18);

--changeset luizsj:205
--comment: Adicionando coluna procurify na tabela LICENCA_SW_PROJETO
alter table LICENCA_SW_PROJETO add LIPR_CD_PROCURIFY VARCHAR2(100);
COMMENT ON COLUMN LICENCA_SW_PROJETO.LIPR_CD_PROCURIFY IS 'C�digo da solicita��o do procurify';

--changeset luizsj:206
--comment: Alteracao coluna LIPR_NOTA_FISCAL para nullable
alter table LICENCA_SW_PROJETO modify (LIPR_NOTA_FISCAL NULL);

--changeset josef:49
--comment: Correcao Receita Exportacao
CREATE OR REPLACE VIEW VW_PMS_INTEG_REVENUE_NATIONAL AS
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RDF.REDF_VL_RECEITA       NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RDF.REDF_VL_RECEITA       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND DF.DEFI_IN_INTERCOMPANY = 'N'
AND (EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3) AND DF.MOED_CD_MOEDA = 1)
AND RDF.REDF_VL_RECEITA > 0
AND C.CLIE_IN_TIPO = 'NAC'

UNION ALL


SELECT REVENUE_CODE,
       REVENUE_SOURCE,
       IS_INTERCOMPANY,
       NOTA_FISCAL_OPERACAO,
       FIL_IN_CODIGO,
       NOT_DT_EMISSAO,
       NOT_DT_SAIDA,
       COND_ST_CODIGO,
       NOT_RE_VALORTOTAL,
       NOT_CH_SITUACAO,
       TPD_IN_CODIGO,
       AGN_TAU_ST_CODIGO,
       CALC_AGN_ST_CODIGO,
       CALC_AGN_ST_TIPOCODIGO,
       ITEM_NOTA_FISCAL_OPERACAO,
       ITN_IN_SEQUENCIA,
       PRO_IN_CODIGO,
       ITN_RE_QUANTIDADE,
       ITN_RE_VALORUNITARIO,
       APL_IN_CODIGO,
       TPC_ST_CLASSE,
       PROJ_IDE_ST_CODIGO,
       PROJ_IN_REDUZIDO,
       CUS_IDE_ST_CODIGO,
       CUS_IN_REDUZIDO,
       COS_IN_CODIGO,
       PARCELAS_OPERACAO,
       MOV_ST_PARCELA,
       MOV_RE_VALORMOE
    FROM (
        SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
               'SERVICES'                REVENUE_SOURCE,
               DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
               'I'                       NOTA_FISCAL_OPERACAO,
               decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
               LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
               LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
               'PMS'                     COND_ST_CODIGO,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) NOT_RE_VALORTOTAL,
               'N'                       NOT_CH_SITUACAO,
               38                        TPD_IN_CODIGO,
               'C'                       AGN_TAU_ST_CODIGO,
               2421     CALC_AGN_ST_CODIGO,
               'COD'                     CALC_AGN_ST_TIPOCODIGO,
               'I'                       ITEM_NOTA_FISCAL_OPERACAO,
               1                         ITN_IN_SEQUENCIA,
               i.inrp_cd_erp_item        PRO_IN_CODIGO,
               1                         ITN_RE_QUANTIDADE,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) ITN_RE_VALORUNITARIO,
               39                        APL_IN_CODIGO,
               DECODE(
                EMP.EMPR_CD_EMPRESA_MATRIZ,
                        3, 'DA-Vd S',
                           'Vd Serv')    TPC_ST_CLASSE,
               EMP.EMPR_CD_EMPRESA_MATRIZ,
               MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
               CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
               MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
               GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
               i.inrp_cd_erp_servico     COS_IN_CODIGO,
               'I'                       PARCELAS_OPERACAO,
               1                         MOV_ST_PARCELA,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) MOV_RE_VALORMOE
        FROM RECEITA R
        JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
        JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
        JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
        LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
        LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
        LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
        LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
        LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
        LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
        LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
        LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
        LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
        INNER JOIN HISTORICO_PERCENTUAL_INTERCOMP hpi ON hpi.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL AND hpi.HIPI_DT_FIM is null
        WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
        AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
        AND RM.REMO_VL_TOTAL_MOEDA > 0
        AND DF.DEFI_IN_INTERCOMPANY = 'Y'
        AND hpi.EMPR_CD_EMP_INTERCOMP in (select emp_int.EMPR_CD_EMPRESA from EMPRESA emp_int where emp_int.EMPR_CD_EMPRESA_MATRIZ = 1)) inter
    WHERE inter.NOT_RE_VALORTOTAL > 0

UNION ALL


SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)   FIL_IN_CODIGO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_EMISSAO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RL.RELI_VL_RECEITA        NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RL.RELI_VL_RECEITA        ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RL.RELI_VL_RECEITA        MOV_RE_VALORMOE
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = RL.copr_cd_contrato_pratica and RL.RELI_DT_MES between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS = 'W'
AND RL.RELI_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND RL.RELI_VL_RECEITA > 0
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)

UNION ALL


SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RDF.REDF_VL_RECEITA 
          ELSE
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)  
       END     NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RDF.REDF_VL_RECEITA 
          ELSE
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)  
       END     ITN_RE_VALORUNITARIO,
       24                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RDF.REDF_VL_RECEITA 
          ELSE
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)  
       END    MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)
AND C.CLIE_IN_TIPO = 'INT'
--AND DF.MOED_CD_MOEDA <> 1
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

UNION ALL

SELECT RP.REPL_CD_RECEITA_PLANTAO REVENUE_CODE,
       'DUTY_HOURS'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
      RP.REPL_VL_RECEITA_PLANTAO NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
      RP.REPL_VL_RECEITA_PLANTAO ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       GC.ERP_CD_CCUSTO          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
      RP.REPL_VL_RECEITA_PLANTAO MOV_RE_VALORMOE
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = decode(df.defi_in_intercompany,'Y',df.empr_cd_emp_intercomp,DF.EMPR_CD_EMPRESA) AND i.fore_cd_fonte_receita = 7
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RP.REPL_IN_INTEGRACAO = 'N' AND RP.REPL_VL_RECEITA_PLANTAO > 0
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

UNION ALL

SELECT IF.ITFA_CD_ITEM_FATURA        REVENUE_CODE,
       'REIMB. EXP'                  REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY       IS_INTERCOMPANY,
       'I'                           NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)        FIL_IN_CODIGO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_EMISSAO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_SAIDA,
       'PMS'                         COND_ST_CODIGO,
       IF.ITFA_VL_ITEM               NOT_RE_VALORTOTAL,
       'N'                           NOT_CH_SITUACAO,
       38                            TPD_IN_CODIGO,
       'C'                           AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA         CALC_AGN_ST_CODIGO,
       'COD'                         CALC_AGN_ST_TIPOCODIGO,
       'I'                           ITEM_NOTA_FISCAL_OPERACAO,
       1                             ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item            PRO_IN_CODIGO,
       1                             ITN_RE_QUANTIDADE,
       IF.ITFA_VL_ITEM               ITN_RE_VALORUNITARIO,
       26                            APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')        TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO          PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA     PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO          CUS_IDE_ST_CODIGO,
       MC.cus_in_reduzido            CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico         COS_IN_CODIGO,
       'I'                           PARCELAS_OPERACAO,
       1                             MOV_ST_PARCELA,
       IF.ITFA_VL_ITEM               MOV_RE_VALORMOE
FROM FATURA F
JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL and IF.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = IF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = if.fore_cd_fonte_receita
LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE F.FATU_IN_STATUS IN ('AP', 'ER')
and EMP.EMPR_CD_EMPRESA_MATRIZ IN (1,3)
and if.fore_cd_fonte_receita IN (2,3)
and f.fatu_dt_previsao >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

--changeset josef:50
--comment: Nova Flag para tratar concorrencia
alter table RECEITA_DEAL_FISCAL add REDF_IN_INTEG_QUICKBOOKS varchar2(1);
alter table RECEITA_DEAL_FISCAL_AUD add REDF_IN_INTEG_QUICKBOOKS varchar2(1);

--changeset josef:51
--comment: Correcao Receita Internacional 
CREATE OR REPLACE VIEW VW_PMS_INTEG_REVENUE_INTERNAC AS
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE         NM_CLIENTE,
       CP.COPR_CD_CONTRATO_PRATICA,
       CP.COPR_NM_CONTRATO_PRATICA,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       R.RECE_DT_MES             RECE_DT_MES,
       RDF.REDF_VL_RECEITA       VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE,
       IPQ.REVENUE_ACCOUNT_NUMBER
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 1
Left join cliente c on c.clie_cd_cliente = df.clie_cd_cliente
LEFT JOIN CONTRATO_PRATICA CP ON CP.COPR_CD_CONTRATO_PRATICA = R.COPR_CD_CONTRATO_PRATICA
WHERE (RDF.REDF_IN_STATUS in('E', 'P', 'I'))
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND RDF.REDF_IN_INTEG_QUICKBOOKS = 'N'
AND (EMP.EMPR_CD_EMPRESA_MATRIZ = 9 AND DF.MOED_CD_MOEDA <> 1)

union all

SELECT RP.REPL_CD_RECEITA_PLANTAO REVENUE_CODE,
       'DUTY_HOURS'               REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE         NM_CLIENTE,
       CP.COPR_CD_CONTRATO_PRATICA,
       CP.COPR_NM_CONTRATO_PRATICA,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       R.RECE_DT_MES             RECE_DT_MES,
       RP.REPL_VL_RECEITA_PLANTAO       VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE,
       IPQ.REVENUE_ACCOUNT_NUMBER
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 7
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 7
Left join cliente c on c.clie_cd_cliente = df.clie_cd_cliente
LEFT JOIN CONTRATO_PRATICA CP ON CP.COPR_CD_CONTRATO_PRATICA = R.COPR_CD_CONTRATO_PRATICA
WHERE RP.REPL_VL_RECEITA_PLANTAO > 0
and RP.REPL_IN_INTEGRACAO in('N','I')
AND (R.RECE_IN_VERSAO IN ('PB', 'PD') AND RDF.REDF_IN_STATUS in( 'E', 'P', 'I'))
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND (EMP.EMPR_CD_EMPRESA_MATRIZ = 9 AND DF.MOED_CD_MOEDA <> 1)

union all


SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE         NM_CLIENTE,
       CP.COPR_CD_CONTRATO_PRATICA,
       CP.COPR_NM_CONTRATO_PRATICA,
        DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       RL.RELI_DT_MES            RECE_DT_MES,
       RL.RELI_VL_RECEITA        VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       RL.RELI_VL_RECEITA        MOV_RE_VALORMOE,
       IPQ.REVENUE_ACCOUNT_NUMBER
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 4
Left join cliente c on c.clie_cd_cliente = df.clie_cd_cliente
LEFT JOIN CONTRATO_PRATICA CP ON CP.COPR_CD_CONTRATO_PRATICA = RL.COPR_CD_CONTRATO_PRATICA
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS in ('P', 'E')
AND RL.RELI_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND (EMP.EMPR_CD_EMPRESA_MATRIZ = 9 AND DF.MOED_CD_MOEDA <> 1)

--changeset josef:52
--comment: View Invoice INTERNACIONAL
CREATE OR REPLACE VIEW VW_PMS_INTEG_INVOICE_INTERNAC AS
SELECT F.FATU_CD_FATURA REVENUE_CODE,
       'CONTRACTORS'                REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE           NM_CLIENTE,
       CP.COPR_CD_CONTRATO_PRATICA,
       CP.COPR_NM_CONTRATO_PRATICA,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       F.FATU_DT_PREVISAO        FATU_DT_PREVISAO,
       F.FATU_DT_VENCIMENTO      FATU_DT_VENCTO,
       IF.ITFA_VL_ITEM           VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       IF.ITFA_VL_ITEM           VL_ITEM,
       f.fatu_tx_observacao FATU_OBSERVACAO,
       if.itfa_tx_observacao ITEM_OBSSERVACAO,
       IPQ.REVENUE_ACCOUNT_NUMBER INVOICE_ACCOUNT_NUMBER
       FROM FATURA F
       JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
       JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
       LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL and IF.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO
       LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
       LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
       LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA = 3
       WHERE F.FATU_IN_STATUS IN ('PD', 'ER')
       and EMP.EMPR_CD_EMPRESA_MATRIZ IN (9)
       and if.fore_cd_fonte_receita = 3
       and f.fatu_dt_previsao >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

union all

--Invoice
SELECT F.FATU_CD_FATURA REVENUE_CODE,
       'INVOICE'                REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE           NM_CLIENTE,
       CP.COPR_CD_CONTRATO_PRATICA,
       CP.COPR_NM_CONTRATO_PRATICA,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       F.FATU_DT_PREVISAO        FATU_DT_PREVISAO,
       F.FATU_DT_VENCIMENTO      FATU_DT_VENCTO,
       (SELECT SUM(IF1.ITFA_VL_ITEM) FROM FATURA F1 JOIN ITEM_FATURA IF1 ON F1.FATU_CD_FATURA = IF1.FATU_CD_FATURA WHERE F1.FATU_CD_FATURA = F.FATU_CD_FATURA)           VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       if.itfa_vl_item           VL_ITEM,
       f.fatu_tx_observacao FATU_OBSERVACAO,
       if.itfa_tx_observacao ITEM_OBSSERVACAO,
       IPQ.INVOICE_ACCOUNT_NUMBER
        FROM FATURA F
       JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
       JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
       LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL and IF.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO
       LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
       LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
       LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA =  if.fore_cd_fonte_receita
       WHERE F.FATU_IN_STATUS IN ('PD', 'ER')
       and EMP.EMPR_CD_EMPRESA_MATRIZ IN (9)
       and f.fatu_dt_previsao >= TO_DATE('01/01/2018', 'dd/MM/yyyy')


union all

--Transitoria
SELECT distinct F.FATU_CD_FATURA REVENUE_CODE,
       'TRANSITORIA'                REVENUE_SOURCE,
       C.CLIE_NM_CLIENTE           NM_CLIENTE,
       null COPR_CD_CONTRATO_PRATICA,
       '' COPR_NM_CONTRATO_PRATICA,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       F.FATU_DT_PREVISAO        FATU_DT_PREVISAO,
       F.FATU_DT_VENCIMENTO      FATU_DT_VENCTO,
       (SELECT SUM(IF1.ITFA_VL_ITEM) FROM FATURA F1 JOIN ITEM_FATURA IF1 ON F1.FATU_CD_FATURA = IF1.FATU_CD_FATURA WHERE F1.FATU_CD_FATURA = F.FATU_CD_FATURA)           VALOR_TOTAL,
       C.CLIE_CD_AGENTE_MEGA     CLIE_CD_AGENTE_MEGA,
       null PROJ_IN_REDUZIDO,
       (SELECT SUM(IF1.ITFA_VL_ITEM) FROM FATURA F1 JOIN ITEM_FATURA IF1 ON F1.FATU_CD_FATURA = IF1.FATU_CD_FATURA WHERE F1.FATU_CD_FATURA = F.FATU_CD_FATURA)           VL_ITEM,
       f.fatu_tx_observacao FATU_OBSERVACAO,
       '' ITEM_OBSSERVACAO,
       '4072' INVOICE_ACCOUNT_NUMBER
        FROM FATURA F
       JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
       JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
       LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL and IF.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO
       LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
       LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
       LEFT JOIN INTEG_PARAMETRO_QUICKBOOKS IPQ ON IPQ.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO AND IPQ.FORE_CD_FONTE_RECEITA =  if.fore_cd_fonte_receita
       WHERE F.FATU_IN_STATUS IN ('PD', 'ER')
       and EMP.EMPR_CD_EMPRESA_MATRIZ IN (9)
       and f.fatu_dt_previsao >= TO_DATE('01/01/2018', 'dd/MM/yyyy');
	   
--changeset josef:53
--comment: Script Integracao Quickbooks

-- Create table
create table LANCAMENTO_INVOICE_QUICKBOOKS
(
  FATU_CD_FATURA  NUMBER(18),
  VL_TOTAL       VARCHAR2(40),
  MEGA_CD_CCUSTO VARCHAR2(40),
  MEGA_CD_PROJ   VARCHAR2(40),
  DT_CREATION    DATE,
  DUE_DATE       DATE,
  MEMO           VARCHAR2(4000),
  LISTID_CUSTOMER  VARCHAR2(40),
  LISTID_ACCOUNT   VARCHAR2(40),
  LISTID_PROJECT   VARCHAR(40),
  NATUREZA         VARCHAR(1),
  SOURCE           VARCHAR(40)
  
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
   -- Add comments to the columns 
comment on column LANCAMENTO_INVOICE_QUICKBOOKS.FATU_CD_FATURA
  is 'C�digo da Receita da tabela Fatura';
     -- Add comments to the columns 
comment on column LANCAMENTO_INVOICE_QUICKBOOKS.LISTID_CUSTOMER
  is 'C�digo da Customer do Quickbooks';
comment on column LANCAMENTO_INVOICE_QUICKBOOKS.LISTID_ACCOUNT
  is 'C�digo da Account do Quickbooks';
 comment on column LANCAMENTO_INVOICE_QUICKBOOKS.LISTID_PROJECT
  is 'C�digo da Project do Quickbooks';
 comment on column LANCAMENTO_INVOICE_QUICKBOOKS.NATUREZA
  is 'D - D�bito e C - Cr�dito';  	   
  
--changeset josef:54
--comment: Add column 
-- Add/modify columns 
alter table LANCAMENTO_INVOICE_QUICKBOOKS add LISTID_TERMS VARCHAR2(40);

--changeset vnogueira:55 dbms:oracle
--comment: Remover constraint de Status da Parcela
ALTER TABLE LICENCA_SW_PROJETO_PARCELA
    DROP CONSTRAINT CK_LIPP_STATUS;

--changeset vnogueira:56 dbms:oracle
--comment: Recriar constraint de Status da Parcela para incluir dois novos Status
ALTER TABLE LICENCA_SW_PROJETO_PARCELA
  ADD CONSTRAINT CK_LIPP_STATUS
  CHECK (LIPP_IN_STATUS IN ('AGUARDANDO_APROVACAO', 'APROVADO', 'PENDENTE','INTEGRADO','ERRO'));
  
--changeset vnogueira:57 dbms:oracle
--comment: Alterar tamanho coluna tx_error fatura
alter table FATURA modify FATU_TX_ERROR VARCHAR2(4000);

--changeset vnogueira:58 dbms:oracle
--comment: Constraint do status de rateio licenca
ALTER TABLE RATEIO_LICENCA_SW
DROP CONSTRAINT CK_RELI_STATUS;

--changeset vnogueira:59 dbms:oracle
--comment: Constraint do status de rateio licenca
alter table RATEIO_LICENCA_SW
  add constraint CK_RELI_STATUS
  check (RELI_IN_STATUS in ('AGUARDANDO_APROVACAO', 'APROVADO', 'PENDENTE','INTEGRADO','ERRO'));

--changeset vnogueira:60 dbms:oracle endDelimiter:endProcedure
--comment: Constraint do status de rateio licenca
create or replace procedure USP_PMS_CALCULO_SW_LICENSE is
begin

DECLARE

v_vlr_full_rateio number;
v_vlr_final_rateio number;
v_indireto number;
vl_pct_aloc_prop number;

c_status CONSTANT varchar(20) := 'AGUARDANDO_APROVACAO';

cursor c_chargeback_monthly is
 select p.pess_cd_pessoa, p.pess_cd_login, tr.tire_cd_ti_recurso, tr.tire_nm_ti_recurso, cti.cutr_vl_custo_ti_recurso, c.qtde, round(cti.cutr_vl_custo_ti_recurso / c.qtde,2) vl_unitario
   from custo_ti_recurso cti
   inner join ti_recurso tr on cti.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   inner join chargeback_pessoa cbp on cbp.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   inner join pessoa p on cbp.pess_cd_pessoa = p.pess_cd_pessoa
   inner join (select cbp1.tire_cd_ti_recurso, count(1) qtde
                               from chargeback_pessoa cbp1
                               where (cbp1.chpe_dt_fim is null or cbp1.chpe_dt_fim between trunc(sysdate + 30,'MM') and trunc(sysdate + 60,'MM'))
                               --where (cbp1.chpe_dt_fim is null or cbp1.chpe_dt_fim between trunc(sysdate ,'MM') and trunc(sysdate + 30,'MM')) --REPROCESSAR MES ANTERIOR
                               group by cbp1.tire_cd_ti_recurso) c on c.tire_cd_ti_recurso = cbp.tire_cd_ti_recurso
   where cti.cutr_dt_fim is null
   AND (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate + 30,'MM') and trunc(sysdate + 60,'MM'))
   --AND (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate,'MM') and trunc(sysdate + 30,'MM')); --REPROCESSAR MES ANTERIOR
   and cbp.tire_cd_ti_recurso not in (select distinct tire_cd_ti_recurso from rateio_licenca_sw rls where rls.dt_competencia = trunc(sysdate, 'MM') and rls.reli_in_status = c_status);



--carrega alocaoes > 0 (cenario 1,2,3,4)
cursor c_alocacao (p_cd_pessoa number) is
        select  pess_cd_pessoa,
               pess_cd_login,
               trunc(sysdate, 'MM') dt_competencia,
               --trunc(sysdate - 30, 'MM') dt_competencia, --REPROCESSAR MES ANTERIOR
               clie_cd_cliente, clie_nm_cliente,
               copr_cd_contrato_pratica, copr_nm_contrato_pratica,
               conv_cd_projeto_mega, conv_nm_projeto_mega,
               conv_cd_ccusto_mega, grcu_cd_grupo_custo, grcu_nm_grupo_custo,
               pr_aloc,
               count(1) OVER () qtd_reg,
                sum(pr_aloc) over() sum_aloc_total
               from ( select p.pess_cd_pessoa,
                             p.pess_cd_login,
                             ap.alpe_dt_alocacao_periodo,
                             cp.copr_cd_contrato_pratica,
                             cp.copr_nm_contrato_pratica,
                             cli.clie_cd_cliente, cli.clie_nm_cliente,
                             c.conv_cd_projeto_mega, c.conv_nm_projeto_mega,
                             c.conv_cd_ccusto_mega, gc.grcu_cd_grupo_custo, gc.grcu_nm_grupo_custo,
                             sum(ap.alpe_pr_alocacao_periodo) AS PR_ALOC
                            from pessoa p
                            inner join alocacao a on a.recu_cd_recurso = p.recu_cd_recurso
                             inner join alocacao_periodo ap on a.aloc_cd_alocacao = ap.aloc_cd_alocacao
                             inner join mapa_alocacao ma on a.maal_cd_mapa_alocacao =  ma.maal_cd_mapa_alocacao
                             inner join contrato_pratica cp on ma.copr_cd_contrato_pratica =  cp.copr_cd_contrato_pratica
                             inner join convergencia c on cp.copr_cd_contrato_pratica = c.copr_cd_contrato_pratica
                             inner join grupo_custo gc on c.conv_cd_ccusto_mega = gc.erp_cd_ccusto
                             inner join msa m on  cp.msaa_cd_msa = m.msaa_cd_msa
                             inner join cliente cli on cli.clie_cd_cliente = m.clie_cd_cliente
                             where p.pess_cd_pessoa =   p_cd_pessoa
                             and ap.alpe_dt_alocacao_periodo = trunc(sysdate, 'MM')
                             --and ap.alpe_dt_alocacao_periodo = trunc(sysdate -30, 'MM') --REPROCESSAR MES ANTERIOR
                             and ap.alpe_pr_alocacao_periodo > 0
                             group by p.pess_cd_pessoa,
                                   p.pess_cd_login,
                                   ap.alpe_dt_alocacao_periodo,
                                   cp.copr_cd_contrato_pratica,
                                   cp.copr_nm_contrato_pratica,
                                   cli.clie_cd_cliente, cli.clie_nm_cliente,
                                   c.conv_cd_projeto_mega, c.conv_nm_projeto_mega,
                                   c.conv_cd_ccusto_mega, gc.grcu_cd_grupo_custo, gc.grcu_nm_grupo_custo) t1
                group by pess_cd_pessoa,
                         pess_cd_login,
                         alpe_dt_alocacao_periodo,
                          copr_cd_contrato_pratica,
                          copr_nm_contrato_pratica,
                          clie_cd_cliente,  clie_nm_cliente,
                         conv_cd_projeto_mega, conv_nm_projeto_mega,
                         conv_cd_ccusto_mega,grcu_cd_grupo_custo,grcu_nm_grupo_custo,
                         pr_aloc;

--carrega cenario 5,6
cursor c_sem_alocacao (p_cd_pessoa2 number) is
      select distinct  trunc(sysdate, 'MM') dt_competencia,
      --select distinct  trunc(sysdate -30, 'MM') dt_competencia, --REPROCESSAR MES ANTERIOR
                 p.pess_cd_login,
                 c.conv_cd_ccusto_mega,
                 c.conv_cd_projeto_mega,
                 c.conv_nm_projeto_mega,
                  gc.grcu_cd_grupo_custo,
                 gc.grcu_nm_grupo_custo,
                 cp.copr_cd_contrato_pratica,
                 cp.copr_nm_contrato_pratica,
                 cli.clie_cd_cliente,
                 cli.clie_nm_cliente
        from custo_ti_recurso cti
         inner join ti_recurso tr on cti.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
         inner join chargeback_pessoa cbp on cbp.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
         inner join pessoa p on cbp.pess_cd_pessoa = p.pess_cd_pessoa
         inner join pessoa_grupo_custo pgc on pgc.pess_cd_pessoa = p.pess_cd_pessoa and pgc.pegc_dt_fim is null
         inner join convergencia c on pgc.grcu_cd_grupo_custo = c.grcu_cd_grupo_custo   and c.copr_cd_contrato_pratica is null
         inner join grupo_custo gc on pgc.grcu_cd_grupo_custo = gc.grcu_cd_grupo_custo
         left join contrato_pratica cp on c.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica
         left join msa m on cp.msaa_cd_msa = m.msaa_cd_msa
         left join cliente cli on m.clie_cd_cliente = cli.clie_cd_cliente
        where cti.cutr_dt_fim is null
        and (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate + 30,'MM') and trunc(sysdate + 60,'MM'))
        --and (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate ,'MM') and trunc(sysdate + 30,'MM')) --REPROCESSAR MES ANTERIOR
        and p.pess_cd_pessoa = p_cd_pessoa2
        and (p.pess_dt_rescisao is null or  p.pess_dt_rescisao between trunc(sysdate,'MM') and trunc(sysdate + 30,'MM'))
        --and (p.pess_dt_rescisao is null or  p.pess_dt_rescisao between trunc(sysdate - 30,'MM') and trunc(sysdate,'MM')) --REPROCESSAR MES ANTERIOR
        and p.pess_cd_pessoa not in (select  p.pess_cd_pessoa
                                      from pessoa p
                                      inner join alocacao a on a.recu_cd_recurso = p.recu_cd_recurso
                                       inner join alocacao_periodo ap on a.aloc_cd_alocacao = ap.aloc_cd_alocacao
                                       inner join mapa_alocacao ma on a.maal_cd_mapa_alocacao =  ma.maal_cd_mapa_alocacao
                                       inner join contrato_pratica cp on ma.copr_cd_contrato_pratica =  cp.copr_cd_contrato_pratica
                                       inner join convergencia c on cp.copr_cd_contrato_pratica = c.copr_cd_contrato_pratica
                                       inner join grupo_custo gc on c.conv_cd_ccusto_mega = gc.erp_cd_ccusto
                                       where ap.alpe_dt_alocacao_periodo = trunc(sysdate, 'MM')
                                       --where ap.alpe_dt_alocacao_periodo = trunc(sysdate -30, 'MM') --REPROCESSAR MES ANTERIOR
                                       and ap.alpe_pr_alocacao_periodo > 0
                                       group by p.pess_cd_pessoa,
                                             p.pess_cd_login,
                                             ap.alpe_dt_alocacao_periodo,
                                             c.conv_cd_projeto_mega,c.conv_nm_projeto_mega,
                                             c.conv_cd_ccusto_mega, gc.grcu_nm_grupo_custo);

--Ajuste de arredondamento
cursor lic_sw_arredondar is
 select tr.tire_cd_ti_recurso,
        tr.tire_nm_ti_recurso,
        rlw.dt_competencia,
        cti.cutr_vl_custo_ti_recurso,
        sum(rlw.rali_vl_custo_licenca),
        round(sum(rlw.rali_vl_custo_licenca) - cti.cutr_vl_custo_ti_recurso,2) diff
   from custo_ti_recurso cti
   inner join ti_recurso tr on cti.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   inner join rateio_licenca_sw rlw on rlw.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   where cti.cutr_dt_fim is null
  -- and tr.tire_cd_ti_recurso = 124
   and rlw.dt_competencia >= trunc(sysdate, 'MM')
   group by tr.tire_cd_ti_recurso, tr.tire_nm_ti_recurso, rlw.dt_competencia,cti.cutr_vl_custo_ti_recurso;

cursor rateio_lic(p_cd_ti_recurso number) is
         select rlw.rali_cd_rateio_licenca, rlw.pess_cd_login, round(rlw.rali_vl_custo_licenca,2) valor
         from rateio_licenca_sw rlw
         where rlw.tire_cd_ti_recurso = p_cd_ti_recurso
         and rlw.dt_competencia = trunc(sysdate, 'MM');

BEGIN
--APAGAR PARA REPROCESSAR
DELETE FROM RATEIO_LICENCA_SW sw WHERE DT_COMPETENCIA >= trunc(sysdate, 'MM') and sw.reli_in_status = c_status;
--DELETE FROM RATEIO_LICENCA_SW WHERE DT_COMPETENCIA >= trunc(sysdate -30, 'MM'); --REPROCESSAR MES ANTERIOR

--ENCERRAR COBRAN�A DOS RESCINDIDOS
 merge into chargeback_pessoa c
 using(select cbp.chpe_cd_chargeback_pess,  trunc(p.pess_dt_rescisao, 'MM') dt_fim
          from pessoa p
          inner join chargeback_pessoa cbp on p.pess_cd_pessoa = cbp.pess_cd_pessoa
         where p.pess_dt_rescisao is not null and cbp.chpe_dt_fim is null) s
    on (c.chpe_cd_chargeback_pess = s.chpe_cd_chargeback_pess)
    when matched then update set c.chpe_dt_fim = s.dt_fim;

for item in c_chargeback_monthly loop
begin
    v_vlr_full_rateio := item.vl_unitario;

         for aloc in c_alocacao(item.pess_cd_pessoa) loop

          --Cenario 1: alocacao full em 1 contrato apenas -> 1.0
          --Cenario 2: alocacao c/ feiras/licensa menor que entre 0 e 1 (0.96 de aloc e 0.04 de ferias)
           IF((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) AND aloc.qtd_reg = 1) THEN

             INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    Tire_Cd_Ti_Recurso,
                    RELI_IN_STATUS)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    ALOC.pr_aloc,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_full_rateio,
                    item.tire_cd_ti_recurso,
                    c_status);

          END if;

          --Cenario 3: Alocacao 100% compartilhada (ex: 0.5 e 0.5 de alocacao)
          IF((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) and aloc.qtd_reg > 1 and aloc.sum_aloc_total = 1) then

             INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    Tire_Cd_Ti_Recurso,
                    RELI_IN_STATUS)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    ALOC.pr_aloc,
                    item.TIRE_NM_TI_RECURSO,
                    round(v_vlr_full_rateio * aloc.pr_aloc,2),
                    item.tire_cd_ti_recurso,
                    c_status);

          end if;

          --Cenario 4: Alocacao compartilhada com arrasto (ex: 0.4 e 0.3 + 0.3 de arrasto)
          if ((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) and aloc.qtd_reg > 1  and aloc.sum_aloc_total < 1) then
             v_indireto := aloc.pr_aloc/aloc.sum_aloc_total * (aloc.sum_aloc_total -1) * -1;

             v_vlr_final_rateio := round(v_vlr_full_rateio * (aloc.pr_aloc + v_indireto),2);

            INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    TIRE_CD_TI_RECURSO,
                    RELI_IN_STATUS)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    ALOC.pr_aloc,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_final_rateio,
                    item.tire_cd_ti_recurso,
                    c_status);

          end if;
          -- Cenario 5: Super alocacao
          if ((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) and aloc.qtd_reg > 1  and aloc.sum_aloc_total > 1) then

          vl_pct_aloc_prop := 1 * (aloc.pr_aloc  / aloc.sum_aloc_total);

          v_vlr_final_rateio := round(v_vlr_full_rateio * vl_pct_aloc_prop,2);

           INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    TIRE_CD_TI_RECURSO,
                    RELI_IN_STATUS)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    vl_pct_aloc_prop,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_final_rateio,
                    item.tire_cd_ti_recurso,
                    c_status);

          end if;

         end loop;

         for sem_aloc in c_sem_alocacao (item.pess_cd_pessoa) loop

             INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    TIRE_CD_TI_RECURSO,
                    RELI_IN_STATUS)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    sem_aloc.PESS_CD_LOGIN,
                    sem_aloc.dt_competencia,
                    sem_aloc.CONV_CD_CCUSTO_MEGA,
                    sem_aloc.CONV_CD_PROJETO_MEGA,
                    sem_aloc.conv_nm_projeto_mega,
                    sem_aloc.grcu_cd_grupo_custo,
                    sem_aloc.grcu_nm_grupo_custo,
                    sem_aloc.copr_cd_contrato_pratica,
                    sem_aloc.copr_nm_contrato_pratica,
                    sem_aloc.clie_cd_cliente,
                    sem_aloc.clie_nm_cliente,
                    null,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_full_rateio,
                    item.tire_cd_ti_recurso,
                    c_status);

         end loop;

exception

when others then
     dbms_output.put_line('[ERRO]: ' || to_char(sqlerrm)  );
     raise_application_error(-20001,'An error was encountered '||' -ERROR- '||SQLERRM);

   rollback;
end;
end loop;
commit;

for item_arr in lic_sw_arredondar loop

   if(item_arr.diff <> 0) then

     for item_rat in rateio_lic(item_arr.tire_cd_ti_recurso) loop

       if(item_arr.diff <> 0) then
           if(item_arr.diff < 0) then
             update rateio_licenca_sw rlw
             set rlw.rali_vl_custo_licenca = rlw.rali_vl_custo_licenca + 0.01
             where rlw.rali_cd_rateio_licenca = item_rat.rali_cd_rateio_licenca;

             item_arr.diff := item_arr.diff + 0.01;
           else
             update rateio_licenca_sw rlw
             set rlw.rali_vl_custo_licenca = rlw.rali_vl_custo_licenca - 0.01
             where rlw.rali_cd_rateio_licenca = item_rat.rali_cd_rateio_licenca;

             item_arr.diff := item_arr.diff - 0.01;
           end if;
       end if;

     end loop;
   end if;
end loop;

commit;

end;

END USP_PMS_CALCULO_SW_LICENSE;


--changeset josef:61 dbms:oracle endDelimiter:endProcedure
--comment: Correcao de duplicacao
create or replace procedure USP_PMS_CALCULO_SW_LICENSE is
begin

DECLARE

v_vlr_full_rateio number;
v_vlr_final_rateio number;
v_indireto number;
vl_pct_aloc_prop number;

c_status CONSTANT varchar(20) := 'AGUARDANDO_APROVACAO';

cursor c_chargeback_monthly is
 select p.pess_cd_pessoa, p.pess_cd_login, tr.tire_cd_ti_recurso, tr.tire_nm_ti_recurso, cti.cutr_vl_custo_ti_recurso, c.qtde, round(cti.cutr_vl_custo_ti_recurso / c.qtde,2) vl_unitario

   from custo_ti_recurso cti
   inner join ti_recurso tr on cti.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   inner join chargeback_pessoa cbp on cbp.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   inner join pessoa p on cbp.pess_cd_pessoa = p.pess_cd_pessoa
   inner join (select cbp1.tire_cd_ti_recurso, count(1) qtde
                               from chargeback_pessoa cbp1
                               where (cbp1.chpe_dt_fim is null or cbp1.chpe_dt_fim between trunc(sysdate + 30,'MM') and trunc(sysdate + 60,'MM'))

                               group by cbp1.tire_cd_ti_recurso) c on c.tire_cd_ti_recurso = cbp.tire_cd_ti_recurso

   where cti.cutr_dt_fim is null
   AND (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate + 30,'MM') and trunc(sysdate + 60,'MM'))
   and ( cbp.tire_cd_ti_recurso in (select distinct tire_cd_ti_recurso from rateio_licenca_sw rls where rls.dt_competencia = trunc(sysdate, 'MM')
   and rls.reli_in_status = 'AGUARDANDO_APROVACAO')
   or cbp.tire_cd_ti_recurso not in (select tire_cd_ti_recurso from rateio_licenca_sw rls where rls.dt_competencia = trunc(sysdate,'MM') and rls.reli_in_status <> 'AGUARDANDO_APROVACAO'  ));


cursor c_alocacao (p_cd_pessoa number) is
        select  pess_cd_pessoa,
               pess_cd_login,
               trunc(sysdate, 'MM') dt_competencia,

               clie_cd_cliente, clie_nm_cliente,
               copr_cd_contrato_pratica, copr_nm_contrato_pratica,
               conv_cd_projeto_mega, conv_nm_projeto_mega,
               conv_cd_ccusto_mega, grcu_cd_grupo_custo, grcu_nm_grupo_custo,
               pr_aloc,
               count(1) OVER () qtd_reg,
                sum(pr_aloc) over() sum_aloc_total
               from ( select p.pess_cd_pessoa,
                             p.pess_cd_login,
                             ap.alpe_dt_alocacao_periodo,
                             cp.copr_cd_contrato_pratica,
                             cp.copr_nm_contrato_pratica,
                             cli.clie_cd_cliente, cli.clie_nm_cliente,
                             c.conv_cd_projeto_mega, c.conv_nm_projeto_mega,
                             c.conv_cd_ccusto_mega, gc.grcu_cd_grupo_custo, gc.grcu_nm_grupo_custo,
                             sum(ap.alpe_pr_alocacao_periodo) AS PR_ALOC
                            from pessoa p
                            inner join alocacao a on a.recu_cd_recurso = p.recu_cd_recurso
                             inner join alocacao_periodo ap on a.aloc_cd_alocacao = ap.aloc_cd_alocacao
                             inner join mapa_alocacao ma on a.maal_cd_mapa_alocacao =  ma.maal_cd_mapa_alocacao
                             inner join contrato_pratica cp on ma.copr_cd_contrato_pratica =  cp.copr_cd_contrato_pratica
                             inner join convergencia c on cp.copr_cd_contrato_pratica = c.copr_cd_contrato_pratica
                             inner join grupo_custo gc on c.conv_cd_ccusto_mega = gc.erp_cd_ccusto
                             inner join msa m on  cp.msaa_cd_msa = m.msaa_cd_msa
                             inner join cliente cli on cli.clie_cd_cliente = m.clie_cd_cliente
                             where p.pess_cd_pessoa =   p_cd_pessoa
                             and ap.alpe_dt_alocacao_periodo = trunc(sysdate, 'MM')

                             and ap.alpe_pr_alocacao_periodo > 0
                             group by p.pess_cd_pessoa,
                                   p.pess_cd_login,
                                   ap.alpe_dt_alocacao_periodo,
                                   cp.copr_cd_contrato_pratica,
                                   cp.copr_nm_contrato_pratica,
                                   cli.clie_cd_cliente, cli.clie_nm_cliente,
                                   c.conv_cd_projeto_mega, c.conv_nm_projeto_mega,
                                   c.conv_cd_ccusto_mega, gc.grcu_cd_grupo_custo, gc.grcu_nm_grupo_custo) t1
                group by pess_cd_pessoa,
                         pess_cd_login,
                         alpe_dt_alocacao_periodo,
                          copr_cd_contrato_pratica,
                          copr_nm_contrato_pratica,
                          clie_cd_cliente,  clie_nm_cliente,
                         conv_cd_projeto_mega, conv_nm_projeto_mega,
                         conv_cd_ccusto_mega,grcu_cd_grupo_custo,grcu_nm_grupo_custo,
                         pr_aloc;


cursor c_sem_alocacao (p_cd_pessoa2 number) is
      select distinct  trunc(sysdate, 'MM') dt_competencia,

                 p.pess_cd_login,
                 c.conv_cd_ccusto_mega,
                 c.conv_cd_projeto_mega,
                 c.conv_nm_projeto_mega,
                  gc.grcu_cd_grupo_custo,
                 gc.grcu_nm_grupo_custo,
                 cp.copr_cd_contrato_pratica,
                 cp.copr_nm_contrato_pratica,
                 cli.clie_cd_cliente,
                 cli.clie_nm_cliente
        from custo_ti_recurso cti
         inner join ti_recurso tr on cti.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
         inner join chargeback_pessoa cbp on cbp.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
         inner join pessoa p on cbp.pess_cd_pessoa = p.pess_cd_pessoa
         inner join pessoa_grupo_custo pgc on pgc.pess_cd_pessoa = p.pess_cd_pessoa and pgc.pegc_dt_fim is null
         inner join convergencia c on pgc.grcu_cd_grupo_custo = c.grcu_cd_grupo_custo   and c.copr_cd_contrato_pratica is null
         inner join grupo_custo gc on pgc.grcu_cd_grupo_custo = gc.grcu_cd_grupo_custo
         left join contrato_pratica cp on c.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica
         left join msa m on cp.msaa_cd_msa = m.msaa_cd_msa
         left join cliente cli on m.clie_cd_cliente = cli.clie_cd_cliente
        where cti.cutr_dt_fim is null
        and (cbp.chpe_dt_fim is null or cbp.chpe_dt_fim between trunc(sysdate + 30,'MM') and trunc(sysdate + 60,'MM'))

        and p.pess_cd_pessoa = p_cd_pessoa2
        and (p.pess_dt_rescisao is null or  p.pess_dt_rescisao between trunc(sysdate,'MM') and trunc(sysdate + 30,'MM'))

        and p.pess_cd_pessoa not in (select  p.pess_cd_pessoa
                                      from pessoa p
                                      inner join alocacao a on a.recu_cd_recurso = p.recu_cd_recurso
                                       inner join alocacao_periodo ap on a.aloc_cd_alocacao = ap.aloc_cd_alocacao
                                       inner join mapa_alocacao ma on a.maal_cd_mapa_alocacao =  ma.maal_cd_mapa_alocacao
                                       inner join contrato_pratica cp on ma.copr_cd_contrato_pratica =  cp.copr_cd_contrato_pratica
                                       inner join convergencia c on cp.copr_cd_contrato_pratica = c.copr_cd_contrato_pratica
                                       inner join grupo_custo gc on c.conv_cd_ccusto_mega = gc.erp_cd_ccusto
                                       where ap.alpe_dt_alocacao_periodo = trunc(sysdate, 'MM')

                                       and ap.alpe_pr_alocacao_periodo > 0
                                       group by p.pess_cd_pessoa,
                                             p.pess_cd_login,
                                             ap.alpe_dt_alocacao_periodo,
                                             c.conv_cd_projeto_mega,c.conv_nm_projeto_mega,
                                             c.conv_cd_ccusto_mega, gc.grcu_nm_grupo_custo);


cursor lic_sw_arredondar is
 select tr.tire_cd_ti_recurso,
        tr.tire_nm_ti_recurso,
        rlw.dt_competencia,
        cti.cutr_vl_custo_ti_recurso,
        sum(rlw.rali_vl_custo_licenca),
        round(sum(rlw.rali_vl_custo_licenca) - cti.cutr_vl_custo_ti_recurso,2) diff
   from custo_ti_recurso cti
   inner join ti_recurso tr on cti.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   inner join rateio_licenca_sw rlw on rlw.tire_cd_ti_recurso = tr.tire_cd_ti_recurso
   where cti.cutr_dt_fim is null

   and rlw.dt_competencia >= trunc(sysdate, 'MM')
   group by tr.tire_cd_ti_recurso, tr.tire_nm_ti_recurso, rlw.dt_competencia,cti.cutr_vl_custo_ti_recurso;

cursor rateio_lic(p_cd_ti_recurso number) is
         select rlw.rali_cd_rateio_licenca, rlw.pess_cd_login, round(rlw.rali_vl_custo_licenca,2) valor
         from rateio_licenca_sw rlw
         where rlw.tire_cd_ti_recurso = p_cd_ti_recurso
         and rlw.dt_competencia = trunc(sysdate, 'MM');

BEGIN

DELETE FROM RATEIO_LICENCA_SW sw WHERE DT_COMPETENCIA >= trunc(sysdate, 'MM') and sw.reli_in_status = c_status;



 merge into chargeback_pessoa c
 using(select cbp.chpe_cd_chargeback_pess,  trunc(p.pess_dt_rescisao, 'MM') dt_fim
          from pessoa p
          inner join chargeback_pessoa cbp on p.pess_cd_pessoa = cbp.pess_cd_pessoa
         where p.pess_dt_rescisao is not null and cbp.chpe_dt_fim is null) s
    on (c.chpe_cd_chargeback_pess = s.chpe_cd_chargeback_pess)
    when matched then update set c.chpe_dt_fim = s.dt_fim;

for item in c_chargeback_monthly loop
begin
    v_vlr_full_rateio := item.vl_unitario;

         for aloc in c_alocacao(item.pess_cd_pessoa) loop



           IF((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) AND aloc.qtd_reg = 1) THEN

             INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    Tire_Cd_Ti_Recurso,
                    RELI_IN_STATUS)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    ALOC.pr_aloc,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_full_rateio,
                    item.tire_cd_ti_recurso,
                    c_status);

          END if;


          IF((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) and aloc.qtd_reg > 1 and aloc.sum_aloc_total = 1) then

             INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    Tire_Cd_Ti_Recurso,
                    RELI_IN_STATUS)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    ALOC.pr_aloc,
                    item.TIRE_NM_TI_RECURSO,
                    round(v_vlr_full_rateio * aloc.pr_aloc,2),
                    item.tire_cd_ti_recurso,
                    c_status);

          end if;


          if ((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) and aloc.qtd_reg > 1  and aloc.sum_aloc_total < 1) then
             v_indireto := aloc.pr_aloc/aloc.sum_aloc_total * (aloc.sum_aloc_total -1) * -1;

             v_vlr_final_rateio := round(v_vlr_full_rateio * (aloc.pr_aloc + v_indireto),2);

            INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    TIRE_CD_TI_RECURSO,
                    RELI_IN_STATUS)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    ALOC.pr_aloc,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_final_rateio,
                    item.tire_cd_ti_recurso,
                    c_status);

          end if;

          if ((ALOC.PR_ALOC > 0 and ALOC.PR_ALOC <= 1) and aloc.qtd_reg > 1  and aloc.sum_aloc_total > 1) then

          vl_pct_aloc_prop := 1 * (aloc.pr_aloc  / aloc.sum_aloc_total);

          v_vlr_final_rateio := round(v_vlr_full_rateio * vl_pct_aloc_prop,2);

           INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    TIRE_CD_TI_RECURSO,
                    RELI_IN_STATUS)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    ALOC.PESS_CD_LOGIN,
                    ALOC.dt_competencia,
                    ALOC.CONV_CD_CCUSTO_MEGA,
                    ALOC.CONV_CD_PROJETO_MEGA,
                    ALOC.conv_nm_projeto_mega,
                    ALOC.grcu_cd_grupo_custo,
                    ALOC.grcu_nm_grupo_custo,
                    ALOC.COPR_CD_CONTRATO_PRATICA,
                    ALOC.COPR_NM_CONTRATO_PRATICA,
                    ALOC.CLIE_CD_CLIENTE,
                    ALOC.CLIE_NM_CLIENTE,
                    vl_pct_aloc_prop,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_final_rateio,
                    item.tire_cd_ti_recurso,
                    c_status);

          end if;

         end loop;

         for sem_aloc in c_sem_alocacao (item.pess_cd_pessoa) loop

             INSERT INTO RATEIO_LICENCA_SW(
                    RALI_CD_RATEIO_LICENCA,
                    PESS_CD_LOGIN,
                    DT_COMPETENCIA,
                    CONV_CD_CCUSTO_MEGA,
                    CONV_CD_PROJETO_MEGA,
                    conv_nm_projeto_mega,
                    grcu_cd_grupo_custo,
                    grcu_nm_grupo_custo,
                    COPR_CD_CONTRATO_PRATICA,
                    COPR_NM_CONTRATO_PRATICA,
                    CLIE_CD_CLIENTE,
                    CLIE_NM_CLIENTE,
                    Alpe_Pr_Alocacao_Periodo,
                    TIRE_NM_TI_RECURSO,
                    RALI_VL_CUSTO_LICENCA,
                    TIRE_CD_TI_RECURSO,
                    RELI_IN_STATUS)
                    VALUES(
                    SQ_RALI_CD_RATEIO_LICENCA.NEXTVAL,
                    sem_aloc.PESS_CD_LOGIN,
                    sem_aloc.dt_competencia,
                    sem_aloc.CONV_CD_CCUSTO_MEGA,
                    sem_aloc.CONV_CD_PROJETO_MEGA,
                    sem_aloc.conv_nm_projeto_mega,
                    sem_aloc.grcu_cd_grupo_custo,
                    sem_aloc.grcu_nm_grupo_custo,
                    sem_aloc.copr_cd_contrato_pratica,
                    sem_aloc.copr_nm_contrato_pratica,
                    sem_aloc.clie_cd_cliente,
                    sem_aloc.clie_nm_cliente,
                    null,
                    item.TIRE_NM_TI_RECURSO,
                    v_vlr_full_rateio,
                    item.tire_cd_ti_recurso,
                    c_status);

         end loop;

exception

when others then
     dbms_output.put_line('[ERRO]: ' || to_char(sqlerrm)  );
     raise_application_error(-20001,'An error was encountered '||' -ERROR- '||SQLERRM);

   rollback;
end;
end loop;
commit;

for item_arr in lic_sw_arredondar loop

   if(item_arr.diff <> 0) then

     for item_rat in rateio_lic(item_arr.tire_cd_ti_recurso) loop

       if(item_arr.diff <> 0) then
           if(item_arr.diff < 0) then
             update rateio_licenca_sw rlw
             set rlw.rali_vl_custo_licenca = rlw.rali_vl_custo_licenca + 0.01
             where rlw.rali_cd_rateio_licenca = item_rat.rali_cd_rateio_licenca;

             item_arr.diff := item_arr.diff + 0.01;
           else
             update rateio_licenca_sw rlw
             set rlw.rali_vl_custo_licenca = rlw.rali_vl_custo_licenca - 0.01
             where rlw.rali_cd_rateio_licenca = item_rat.rali_cd_rateio_licenca;

             item_arr.diff := item_arr.diff - 0.01;
           end if;
       end if;

     end loop;
   end if;
end loop;

commit;

end;

END USP_PMS_CALCULO_SW_LICENSE;

--changeset luizsj:207
--comment: Alteracao coluna MOED_CD_MOEDA para nullable
alter table TI_RECURSO modify (MOED_CD_MOEDA NULL);

--changeset vnogueira:208
--comment: Ajuste na coluna de valor de Licenca Projeto para apenas duas casas decimais.
ALTER TABLE LICENCA_SW_PROJETO RENAME COLUMN LIPR_VALOR_TOTAL TO LIPR_VALOR_TOTAL_BKP;
ALTER TABLE LICENCA_SW_PROJETO ADD LIPR_VALOR_TOTAL NUMBER(18,2);
UPDATE LICENCA_SW_PROJETO lp SET lp.LIPR_VALOR_TOTAL = lp.LIPR_VALOR_TOTAL_BKP;
ALTER TABLE LICENCA_SW_PROJETO MODIFY LIPR_VALOR_TOTAL NOT NULL;
ALTER TABLE LICENCA_SW_PROJETO DROP COLUMN LIPR_VALOR_TOTAL_BKP;
COMMENT ON COLUMN LICENCA_SW_PROJETO.LIPR_VALOR_TOTAL IS 'Valor total da Nota Fiscal';

--changeset josef:62
--comment: View Invoice Internacional com Intercompany
CREATE OR REPLACE VIEW VW_PMS_INTEG_INVOICE_NATIONAL AS
SELECT IF.ITFA_CD_ITEM_FATURA              ID_VIEW,
       F.FATU_CD_FATURA                    INVOICE_CODE,
       'I'                                 NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL              FIL_IN_CODIGO,
       TO_DATE(SYSDATE, 'DD/MM/RRRR')      NOT_DT_EMISSAO,
       TO_DATE(SYSDATE, 'DD/MM/RRRR')      NOT_DT_SAIDA,
       F.FATU_ST_COND_PGTO                 COND_ST_CODIGO,
       'N'                                 NOT_CH_SITUACAO,
       CASE EMP.EMPR_CD_ERP_FILIAL WHEN 3162  THEN 149
                                   WHEN 1976  THEN 148
                                   WHEN 2872  THEN 61
                                   WHEN 12803 THEN 62
                                   WHEN 3     THEN 146
                                   WHEN 3163  THEN 147 END TPD_IN_CODIGO,
       'C'                                 AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA               CALC_AGN_ST_CODIGO,
       'COD'                               CALC_AGN_ST_TIPOCODIGO,
       'I'                                 ITEM_NOTA_FISCAL_OPERACAO,
       ROWNUM                              ITN_IN_SEQUENCIA,
       I.INFP_CD_ERP_ITEM                  PRO_IN_CODIGO,
       1                                   ITN_RE_QUANTIDADE,
       IF.ITFA_VL_ITEM                     ITN_RE_VALORUNITARIO,
       26                                  APL_IN_CODIGO,
        DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       NVL(PP.PRO_IDE_ST_CODIGO, EMP.EMPR_CD_ERP_PROJ_IDE) PROJ_IDE_ST_CODIGO,
       NVL(PP.COD_IN_PROJETO, EMP.EMPR_CD_ERP_PROJETO)     PROJ_IN_REDUZIDO,
       NVL(CC.CUS_IDE_ST_CODIGO, EMP.EMPR_CD_ERP_CUS_IDE)  CUS_IDE_ST_CODIGO,
       NVL(CC.CUS_IN_REDUZIDO, EMP.EMPR_CD_ERP_CCUSTO)      CUS_IN_REDUZIDO,
       I.INFP_CD_ERP_SERVICO               COS_IN_CODIGO,
       'I'                                 OBSERVACAO_NF_OPERACAO,
       'N'                                 NOB_CH_TIPO_OBSERVACAO,
       F.FATU_TX_OBSERVACAO                NOB_ST_OBSERVACAO,
       'I'                                 PARCELAS_OPERACAO,
       1                                   MOV_ST_PARCELA,
       F.FATU_DT_VENCIMENTO                MOV_DT_VENCTO,
       (SELECT SUM(IF1.ITFA_VL_ITEM) FROM FATURA F1 JOIN ITEM_FATURA IF1 ON F1.FATU_CD_FATURA = IF1.FATU_CD_FATURA WHERE F1.FATU_CD_FATURA = F.FATU_CD_FATURA) MOV_RE_VALORMOE
       FROM FATURA F
       JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
       JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
       LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
       LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
       LEFT JOIN INTEG_FATURA_PARAMETRO I on I.TISE_CD_TIPO_SERVICO = IF.TISE_CD_TIPO_SERVICO AND I.EMPR_CD_EMPRESA = DF.EMPR_CD_EMPRESA AND I.FORE_CD_FONTE_RECEITA = IF.FORE_CD_FONTE_RECEITA
       LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN VW_MEGA_CCUSTO CC ON CONV.CONV_CD_CCUSTO_MEGA = CC.CUS_IN_REDUZIDO
       LEFT JOIN VW_MEGA_PROJETO PP ON CONV.CONV_CD_PROJETO_MEGA = PP.COD_IN_PROJETO
       WHERE F.FATU_IN_STATUS IN ('AP', 'ER')
       and EMP.EMPR_CD_EMPRESA_MATRIZ IN (1,3)
union all

      SELECT
      ID_VIEW,
       INVOICE_CODE,
       NOTA_FISCAL_OPERACAO,
       FIL_IN_CODIGO,
       NOT_DT_EMISSAO,
       NOT_DT_SAIDA,
       COND_ST_CODIGO,
       NOT_CH_SITUACAO,
      TPD_IN_CODIGO,
       AGN_TAU_ST_CODIGO,
       CALC_AGN_ST_CODIGO,
       CALC_AGN_ST_TIPOCODIGO,
       ITEM_NOTA_FISCAL_OPERACAO,
       ITN_IN_SEQUENCIA,
       PRO_IN_CODIGO,
       ITN_RE_QUANTIDADE,
      ITN_RE_VALORUNITARIO,
      APL_IN_CODIGO,
      TPC_ST_CLASSE,
      PROJ_IDE_ST_CODIGO,
      PROJ_IN_REDUZIDO,
      CUS_IDE_ST_CODIGO,
      CUS_IN_REDUZIDO,
      COS_IN_CODIGO,
      OBSERVACAO_NF_OPERACAO,
      NOB_CH_TIPO_OBSERVACAO,
      NOB_ST_OBSERVACAO,
      PARCELAS_OPERACAO,
      MOV_ST_PARCELA,
      MOV_DT_VENCTO,
      MOV_RE_VALORMOE

      FROM (
        SELECT RDF.REDF_CD_RECEITA_DFISCAL          ID_VIEW,
               RDF.REDF_CD_RECEITA_DFISCAL          INVOICE_CODE,
               'I'                                  NOTA_FISCAL_OPERACAO,
               decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
               TO_DATE(SYSDATE, 'DD/MM/RRRR')       NOT_DT_EMISSAO,
               TO_DATE(SYSDATE, 'DD/MM/RRRR')       NOT_DT_SAIDA,
               'PMS'                                COND_ST_CODIGO,
               'N'                       NOT_CH_SITUACAO,
               CASE decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL) WHEN 3162  THEN 149
                                                                                                             WHEN 1976  THEN 148
                                                                                                             WHEN 2872  THEN 61
                                                                                                             WHEN 12803 THEN 62
                                                                                                             WHEN 3     THEN 146
                                                                                                             WHEN 3163  THEN 147 END TPD_IN_CODIGO,
               'C'                                 AGN_TAU_ST_CODIGO,
               2421                                CALC_AGN_ST_CODIGO,
               'COD'                               CALC_AGN_ST_TIPOCODIGO,
               'I'                                 ITEM_NOTA_FISCAL_OPERACAO,
               1                                   ITN_IN_SEQUENCIA,
               ts.tise_cd_erp                      PRO_IN_CODIGO,
                1                                  ITN_RE_QUANTIDADE,


               ROUND((select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp)
                             * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)  ITN_RE_VALORUNITARIO,
                39                                  APL_IN_CODIGO,
                DECODE( EMP.EMPR_CD_EMPRESA_MATRIZ, 3, 'DA-Vd S', 'Vd Serv')    TPC_ST_CLASSE,
                 NVL(PP.PRO_IDE_ST_CODIGO, EMP.EMPR_CD_ERP_PROJ_IDE)            PROJ_IDE_ST_CODIGO,
                 NVL(PP.COD_IN_PROJETO, EMP.EMPR_CD_ERP_PROJETO)                PROJ_IN_REDUZIDO,
                 NVL(CC.CUS_IDE_ST_CODIGO, EMP.EMPR_CD_ERP_CUS_IDE)             CUS_IDE_ST_CODIGO,
                 NVL(CC.CUS_IN_REDUZIDO, EMP.EMPR_CD_ERP_CCUSTO)                CUS_IN_REDUZIDO,
                 w.cos_in_codigo                                          COS_IN_CODIGO,
                 'I'                                                            OBSERVACAO_NF_OPERACAO,
                 'N'                                                            NOB_CH_TIPO_OBSERVACAO,
                 'Invoice Intercompany'                                         NOB_ST_OBSERVACAO,
                 'I'                                                            PARCELAS_OPERACAO,
                 1                                                              MOV_ST_PARCELA,
                 trunc(sysdate + cpgto.days)                                                             MOV_DT_VENCTO,
                 ROUND(
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp)
                             * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
                              MOV_RE_VALORMOE

        FROM RECEITA R
        JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
        JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
        JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
        LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
        LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
        LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
        left join tipo_servico ts on ts.tise_cd_tipo_servico = tsdf.tise_cd_tipo_servico
        LEFT JOIN integ_fatura_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
        LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = ts.tise_cd_erp
        LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
        LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
        LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
        LEFT JOIN VW_MEGA_CCUSTO CC ON CONV.CONV_CD_CCUSTO_MEGA = CC.CUS_IN_REDUZIDO
        LEFT JOIN VW_MEGA_PROJETO PP ON CONV.CONV_CD_PROJETO_MEGA = PP.COD_IN_PROJETO
        LEFT JOIN MGCLI.VW_MEGA_CLIENTE_COND_PAGTO@ln_mgweb cpgto on cpgto.agent_code = 2421 and cpgto.payment_cond_name = '60 DIAS'
        INNER JOIN HISTORICO_PERCENTUAL_INTERCOMP hpi ON hpi.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL AND hpi.HIPI_DT_FIM is null
        WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
        AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
        AND RM.REMO_VL_TOTAL_MOEDA > 0
        AND DF.DEFI_IN_INTERCOMPANY = 'Y'
        AND hpi.EMPR_CD_EMP_INTERCOMP in (select emp_int.EMPR_CD_EMPRESA from EMPRESA emp_int where emp_int.EMPR_CD_EMPRESA_MATRIZ = 1)
        ) inter
    WHERE inter.ITN_RE_VALORUNITARIO > 0;

--changeset luizsj:208
--comment: Atualizacao view VW_PMS_INTEG_REVENUE_NATIONAL
CREATE OR REPLACE VIEW VW_PMS_INTEG_REVENUE_NATIONAL AS
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RDF.REDF_VL_RECEITA       NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RDF.REDF_VL_RECEITA       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND DF.DEFI_IN_INTERCOMPANY = 'N'
AND (EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3) AND DF.MOED_CD_MOEDA = 1)
AND RDF.REDF_VL_RECEITA > 0
AND C.CLIE_IN_TIPO = 'NAC'

UNION ALL


SELECT REVENUE_CODE,
       REVENUE_SOURCE,
       IS_INTERCOMPANY,
       NOTA_FISCAL_OPERACAO,
       FIL_IN_CODIGO,
       NOT_DT_EMISSAO,
       NOT_DT_SAIDA,
       COND_ST_CODIGO,
       NOT_RE_VALORTOTAL,
       NOT_CH_SITUACAO,
       TPD_IN_CODIGO,
       AGN_TAU_ST_CODIGO,
       CALC_AGN_ST_CODIGO,
       CALC_AGN_ST_TIPOCODIGO,
       ITEM_NOTA_FISCAL_OPERACAO,
       ITN_IN_SEQUENCIA,
       PRO_IN_CODIGO,
       ITN_RE_QUANTIDADE,
       ITN_RE_VALORUNITARIO,
       APL_IN_CODIGO,
       TPC_ST_CLASSE,
       PROJ_IDE_ST_CODIGO,
       PROJ_IN_REDUZIDO,
       CUS_IDE_ST_CODIGO,
       CUS_IN_REDUZIDO,
       COS_IN_CODIGO,
       PARCELAS_OPERACAO,
       MOV_ST_PARCELA,
       MOV_RE_VALORMOE
    FROM (
        SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
               'SERVICES'                REVENUE_SOURCE,
               DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
               'I'                       NOTA_FISCAL_OPERACAO,
               decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
               LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
               LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
               'PMS'                     COND_ST_CODIGO,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) NOT_RE_VALORTOTAL,
               'N'                       NOT_CH_SITUACAO,
               38                        TPD_IN_CODIGO,
               'C'                       AGN_TAU_ST_CODIGO,
               2421     CALC_AGN_ST_CODIGO,
               'COD'                     CALC_AGN_ST_TIPOCODIGO,
               'I'                       ITEM_NOTA_FISCAL_OPERACAO,
               1                         ITN_IN_SEQUENCIA,
               i.inrp_cd_erp_item        PRO_IN_CODIGO,
               1                         ITN_RE_QUANTIDADE,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) ITN_RE_VALORUNITARIO,
               39                        APL_IN_CODIGO,
               DECODE(
                EMP.EMPR_CD_EMPRESA_MATRIZ,
                        3, 'DA-Vd S',
                           'Vd Serv')    TPC_ST_CLASSE,
               EMP.EMPR_CD_EMPRESA_MATRIZ,
               MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
               CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
               MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
               conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
               i.inrp_cd_erp_servico     COS_IN_CODIGO,
               'I'                       PARCELAS_OPERACAO,
               1                         MOV_ST_PARCELA,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) MOV_RE_VALORMOE
        FROM RECEITA R
        JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
        JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
        JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
        LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
        LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
        LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
        LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
        LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
        LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
        LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
        LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
        LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
        INNER JOIN HISTORICO_PERCENTUAL_INTERCOMP hpi ON hpi.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL AND hpi.HIPI_DT_FIM is null
        WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
        AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
        AND RM.REMO_VL_TOTAL_MOEDA > 0
        AND DF.DEFI_IN_INTERCOMPANY = 'Y'
        AND hpi.EMPR_CD_EMP_INTERCOMP in (select emp_int.EMPR_CD_EMPRESA from EMPRESA emp_int where emp_int.EMPR_CD_EMPRESA_MATRIZ = 1)) inter
    WHERE inter.NOT_RE_VALORTOTAL > 0

UNION ALL


SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)   FIL_IN_CODIGO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_EMISSAO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RL.RELI_VL_RECEITA        NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RL.RELI_VL_RECEITA        ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RL.RELI_VL_RECEITA        MOV_RE_VALORMOE
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = RL.copr_cd_contrato_pratica and RL.RELI_DT_MES between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS = 'W'
AND RL.RELI_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND RL.RELI_VL_RECEITA > 0
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)

UNION ALL


SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RDF.REDF_VL_RECEITA
          ELSE
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END     NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RDF.REDF_VL_RECEITA
          ELSE
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END     ITN_RE_VALORUNITARIO,
       24                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RDF.REDF_VL_RECEITA
          ELSE
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END    MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)
AND C.CLIE_IN_TIPO = 'INT'

AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

UNION ALL

SELECT RP.REPL_CD_RECEITA_PLANTAO REVENUE_CODE,
       'DUTY_HOURS'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
      RP.REPL_VL_RECEITA_PLANTAO NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
      RP.REPL_VL_RECEITA_PLANTAO ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
      RP.REPL_VL_RECEITA_PLANTAO MOV_RE_VALORMOE
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = decode(df.defi_in_intercompany,'Y',df.empr_cd_emp_intercomp,DF.EMPR_CD_EMPRESA) AND i.fore_cd_fonte_receita = 7
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RP.REPL_IN_INTEGRACAO = 'N' AND RP.REPL_VL_RECEITA_PLANTAO > 0
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

UNION ALL

SELECT IF.ITFA_CD_ITEM_FATURA        REVENUE_CODE,
       'REIMB. EXP'                  REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY       IS_INTERCOMPANY,
       'I'                           NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)        FIL_IN_CODIGO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_EMISSAO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_SAIDA,
       'PMS'                         COND_ST_CODIGO,
       IF.ITFA_VL_ITEM               NOT_RE_VALORTOTAL,
       'N'                           NOT_CH_SITUACAO,
       38                            TPD_IN_CODIGO,
       'C'                           AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA         CALC_AGN_ST_CODIGO,
       'COD'                         CALC_AGN_ST_TIPOCODIGO,
       'I'                           ITEM_NOTA_FISCAL_OPERACAO,
       1                             ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item            PRO_IN_CODIGO,
       1                             ITN_RE_QUANTIDADE,
       IF.ITFA_VL_ITEM               ITN_RE_VALORUNITARIO,
       26                            APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')        TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO          PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA     PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO          CUS_IDE_ST_CODIGO,
       MC.cus_in_reduzido            CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico         COS_IN_CODIGO,
       'I'                           PARCELAS_OPERACAO,
       1                             MOV_ST_PARCELA,
       IF.ITFA_VL_ITEM               MOV_RE_VALORMOE
FROM FATURA F
JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL and IF.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = IF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = if.fore_cd_fonte_receita
LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE F.FATU_IN_STATUS IN ('AP', 'ER')
and EMP.EMPR_CD_EMPRESA_MATRIZ IN (1,3)
and if.fore_cd_fonte_receita IN (2,3)
and f.fatu_dt_previsao >= TO_DATE('01/01/2018', 'dd/MM/yyyy');

--changeset vnogueira:209
--comment: Criacao de INDEX e UNIQUE KEY na tabela de RECEITA
CREATE INDEX IDX_RECE_VERSAO ON RECEITA (RECE_DT_MES, COPR_CD_CONTRATO_PRATICA, RECE_IN_VERSAO);
ALTER TABLE RECEITA ADD CONSTRAINT UK_RECE_VERSAO UNIQUE(RECE_DT_MES, COPR_CD_CONTRATO_PRATICA, RECE_IN_VERSAO) ENABLE NOVALIDATE;

--changeset rbastos:210 dbms:oracle 
--comment: Cria��o da tabela de parametrizacao de moedas
CREATE SEQUENCE SQ_INMP_CD_INTEG_MOEDA_PARAM minvalue 1 maxvalue 999999999999999999999999999999 start with 1 increment by 1 cache 2;
CREATE TABLE INTEG_MOEDA_PARAMETRO
(
	INMP_CD_INTEG_MOEDA_PARAM  NUMBER(18) NOT NULL,
	MOED_CD_MOEDA  NUMBER(18) NOT NULL,
	INMP_CD_MOEDA_ERP   NUMBER(18) NOT NULL
);

ALTER TABLE INTEG_MOEDA_PARAMETRO
  ADD CONSTRAINT INMP_PK PRIMARY KEY (INMP_CD_INTEG_MOEDA_PARAM);
  
ALTER TABLE INTEG_MOEDA_PARAMETRO
   ADD CONSTRAINT FK_INMP_MOED FOREIGN KEY (MOED_CD_MOEDA)
   REFERENCES MOEDA (MOED_CD_MOEDA);  
  
COMMENT ON COLUMN INTEG_MOEDA_PARAMETRO.INMP_CD_INTEG_MOEDA_PARAM IS 'Codigo identificador da tabela';
COMMENT ON COLUMN INTEG_MOEDA_PARAMETRO.MOED_CD_MOEDA IS 'Codigo da moeda no PMS';
COMMENT ON COLUMN INTEG_MOEDA_PARAMETRO.INMP_CD_MOEDA_ERP IS 'Codigo da Moeda no Mega';

--changeset rbastos:211
--comment: Script para popular dados na tabela INTEG_MOEDA_PARAMETRO
insert into INTEG_MOEDA_PARAMETRO
  (INMP_CD_INTEG_MOEDA_PARAM,
   MOED_CD_MOEDA,
   INMP_CD_MOEDA_ERP)
values
  (SQ_INMP_CD_INTEG_MOEDA_PARAM.NEXTVAL, 2, 15);
  
insert into INTEG_MOEDA_PARAMETRO
  (INMP_CD_INTEG_MOEDA_PARAM,
   MOED_CD_MOEDA,
   INMP_CD_MOEDA_ERP)
values
  (SQ_INMP_CD_INTEG_MOEDA_PARAM.NEXTVAL, 41, 16);

  insert into INTEG_MOEDA_PARAMETRO
  (INMP_CD_INTEG_MOEDA_PARAM,
   MOED_CD_MOEDA,
   INMP_CD_MOEDA_ERP)
values
  (SQ_INMP_CD_INTEG_MOEDA_PARAM.NEXTVAL, 3, 14);

  insert into INTEG_MOEDA_PARAMETRO
  (INMP_CD_INTEG_MOEDA_PARAM,
   MOED_CD_MOEDA,
   INMP_CD_MOEDA_ERP)
values
  (SQ_INMP_CD_INTEG_MOEDA_PARAM.NEXTVAL, 63, 23);  

--changeset gnicolini:212
--comment: Aumentar tamanho do texto da observacao do item fatura
alter table ITEM_FATURA modify ITFA_TX_OBSERVACAO varchar2(1000);

--changeset rbastos:213
--comment: Alteração na view vw_pms_integ_revenue_national para considerar receitas com FD CPS em dolar
CREATE OR REPLACE VIEW VW_PMS_INTEG_REVENUE_NATIONAL AS
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RDF.REDF_VL_RECEITA       NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RDF.REDF_VL_RECEITA       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND DF.DEFI_IN_INTERCOMPANY = 'N'
AND (EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3) AND DF.MOED_CD_MOEDA = 1)
AND RDF.REDF_VL_RECEITA > 0
AND C.CLIE_IN_TIPO = 'NAC'

UNION ALL


SELECT REVENUE_CODE,
       REVENUE_SOURCE,
       IS_INTERCOMPANY,
       NOTA_FISCAL_OPERACAO,
       FIL_IN_CODIGO,
       NOT_DT_EMISSAO,
       NOT_DT_SAIDA,
       COND_ST_CODIGO,
       NOT_RE_VALORTOTAL,
       NOT_CH_SITUACAO,
       TPD_IN_CODIGO,
       AGN_TAU_ST_CODIGO,
       CALC_AGN_ST_CODIGO,
       CALC_AGN_ST_TIPOCODIGO,
       ITEM_NOTA_FISCAL_OPERACAO,
       ITN_IN_SEQUENCIA,
       PRO_IN_CODIGO,
       ITN_RE_QUANTIDADE,
       ITN_RE_VALORUNITARIO,
       APL_IN_CODIGO,
       TPC_ST_CLASSE,
       PROJ_IDE_ST_CODIGO,
       PROJ_IN_REDUZIDO,
       CUS_IDE_ST_CODIGO,
       CUS_IN_REDUZIDO,
       COS_IN_CODIGO,
       PARCELAS_OPERACAO,
       MOV_ST_PARCELA,
       MOV_RE_VALORMOE
    FROM (
        SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
               'SERVICES'                REVENUE_SOURCE,
               DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
               'I'                       NOTA_FISCAL_OPERACAO,
               decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
               LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
               LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
               'PMS'                     COND_ST_CODIGO,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) NOT_RE_VALORTOTAL,
               'N'                       NOT_CH_SITUACAO,
               38                        TPD_IN_CODIGO,
               'C'                       AGN_TAU_ST_CODIGO,
               2421     CALC_AGN_ST_CODIGO,
               'COD'                     CALC_AGN_ST_TIPOCODIGO,
               'I'                       ITEM_NOTA_FISCAL_OPERACAO,
               1                         ITN_IN_SEQUENCIA,
               i.inrp_cd_erp_item        PRO_IN_CODIGO,
               1                         ITN_RE_QUANTIDADE,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) ITN_RE_VALORUNITARIO,
               39                        APL_IN_CODIGO,
               DECODE(
                EMP.EMPR_CD_EMPRESA_MATRIZ,
                        3, 'DA-Vd S',
                           'Vd Serv')    TPC_ST_CLASSE,
               EMP.EMPR_CD_EMPRESA_MATRIZ,
               MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
               CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
               MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
               conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
               i.inrp_cd_erp_servico     COS_IN_CODIGO,
               'I'                       PARCELAS_OPERACAO,
               1                         MOV_ST_PARCELA,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) MOV_RE_VALORMOE
        FROM RECEITA R
        JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
        JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
        JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
        LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
        LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
        LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
        LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
        LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
        LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
        LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
        LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
        LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
        INNER JOIN HISTORICO_PERCENTUAL_INTERCOMP hpi ON hpi.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL AND hpi.HIPI_DT_FIM is null
        WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
        AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
        AND RM.REMO_VL_TOTAL_MOEDA > 0
        AND DF.DEFI_IN_INTERCOMPANY = 'Y'
        AND hpi.EMPR_CD_EMP_INTERCOMP in (select emp_int.EMPR_CD_EMPRESA from EMPRESA emp_int where emp_int.EMPR_CD_EMPRESA_MATRIZ = 1)) inter
    WHERE inter.NOT_RE_VALORTOTAL > 0

UNION ALL


SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)   FIL_IN_CODIGO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_EMISSAO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RL.RELI_VL_RECEITA        NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RL.RELI_VL_RECEITA        ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RL.RELI_VL_RECEITA        MOV_RE_VALORMOE
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = RL.copr_cd_contrato_pratica and RL.RELI_DT_MES between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS = 'W'
AND RL.RELI_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND RL.RELI_VL_RECEITA > 0
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)

UNION ALL


SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       'N'                       IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RDF.REDF_VL_RECEITA
          ELSE
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END     NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RDF.REDF_VL_RECEITA
          ELSE
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END     ITN_RE_VALORUNITARIO,
       24                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RDF.REDF_VL_RECEITA
          ELSE
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END    MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)
AND C.CLIE_IN_TIPO = 'INT'

AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

UNION ALL

SELECT RP.REPL_CD_RECEITA_PLANTAO REVENUE_CODE,
       'DUTY_HOURS'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
      RP.REPL_VL_RECEITA_PLANTAO NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
      RP.REPL_VL_RECEITA_PLANTAO ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
      RP.REPL_VL_RECEITA_PLANTAO MOV_RE_VALORMOE
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = decode(df.defi_in_intercompany,'Y',df.empr_cd_emp_intercomp,DF.EMPR_CD_EMPRESA) AND i.fore_cd_fonte_receita = 7
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RP.REPL_IN_INTEGRACAO = 'N' AND RP.REPL_VL_RECEITA_PLANTAO > 0
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

UNION ALL

SELECT IF.ITFA_CD_ITEM_FATURA        REVENUE_CODE,
       'REIMB. EXP'                  REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY       IS_INTERCOMPANY,
       'I'                           NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)        FIL_IN_CODIGO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_EMISSAO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_SAIDA,
       'PMS'                         COND_ST_CODIGO,
       IF.ITFA_VL_ITEM               NOT_RE_VALORTOTAL,
       'N'                           NOT_CH_SITUACAO,
       38                            TPD_IN_CODIGO,
       'C'                           AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA         CALC_AGN_ST_CODIGO,
       'COD'                         CALC_AGN_ST_TIPOCODIGO,
       'I'                           ITEM_NOTA_FISCAL_OPERACAO,
       1                             ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item            PRO_IN_CODIGO,
       1                             ITN_RE_QUANTIDADE,
       IF.ITFA_VL_ITEM               ITN_RE_VALORUNITARIO,
       26                            APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')        TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO          PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA     PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO          CUS_IDE_ST_CODIGO,
       MC.cus_in_reduzido            CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico         COS_IN_CODIGO,
       'I'                           PARCELAS_OPERACAO,
       1                             MOV_ST_PARCELA,
       IF.ITFA_VL_ITEM               MOV_RE_VALORMOE
FROM FATURA F
JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL and IF.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = IF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = if.fore_cd_fonte_receita
LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE F.FATU_IN_STATUS IN ('AP', 'ER')
and EMP.EMPR_CD_EMPRESA_MATRIZ IN (1,3)
and if.fore_cd_fonte_receita IN (2,3)
and f.fatu_dt_previsao >= TO_DATE('01/01/2018', 'dd/MM/yyyy');

--changeset rbastos:214
--comment: Alteração na view vw_pms_integ_invoice_national para considerar invoices com FD CPS em dolar
CREATE OR REPLACE VIEW VW_PMS_INTEG_INVOICE_NATIONAL AS
SELECT IF.ITFA_CD_ITEM_FATURA              ID_VIEW,
       F.FATU_CD_FATURA                    INVOICE_CODE,
       'I'                                 NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL              FIL_IN_CODIGO,
       TO_DATE(SYSDATE, 'DD/MM/RRRR')      NOT_DT_EMISSAO,
       TO_DATE(SYSDATE, 'DD/MM/RRRR')      NOT_DT_SAIDA,
       F.FATU_ST_COND_PGTO                 COND_ST_CODIGO,
       'N'                                 NOT_CH_SITUACAO,
       CASE EMP.EMPR_CD_ERP_FILIAL WHEN 3162  THEN 149
                                   WHEN 1976  THEN 148
                                   WHEN 2872  THEN 61
                                   WHEN 12803 THEN 62
                                   WHEN 3     THEN 146
                                   WHEN 3163  THEN 147 END TPD_IN_CODIGO,
       'C'                                 AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA               CALC_AGN_ST_CODIGO,
       'COD'                               CALC_AGN_ST_TIPOCODIGO,
       'I'                                 ITEM_NOTA_FISCAL_OPERACAO,
       ROWNUM                              ITN_IN_SEQUENCIA,
       I.INFP_CD_ERP_ITEM                  PRO_IN_CODIGO,
       1                                   ITN_RE_QUANTIDADE,
       IF.ITFA_VL_ITEM                     ITN_RE_VALORUNITARIO,
       26                                  APL_IN_CODIGO,
        DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       NVL(PP.PRO_IDE_ST_CODIGO, EMP.EMPR_CD_ERP_PROJ_IDE) PROJ_IDE_ST_CODIGO,
       NVL(PP.COD_IN_PROJETO, EMP.EMPR_CD_ERP_PROJETO)     PROJ_IN_REDUZIDO,
       NVL(CC.CUS_IDE_ST_CODIGO, EMP.EMPR_CD_ERP_CUS_IDE)  CUS_IDE_ST_CODIGO,
       NVL(CC.CUS_IN_REDUZIDO, EMP.EMPR_CD_ERP_CCUSTO)      CUS_IN_REDUZIDO,
       I.INFP_CD_ERP_SERVICO               COS_IN_CODIGO,
       'I'                                 OBSERVACAO_NF_OPERACAO,
       'N'                                 NOB_CH_TIPO_OBSERVACAO,
       IF.ITFA_TX_OBSERVACAO               NOB_ST_OBSERVACAO,
       'I'                                 PARCELAS_OPERACAO,
       1                                   MOV_ST_PARCELA,
       F.FATU_DT_VENCIMENTO                MOV_DT_VENCTO,
       (SELECT SUM(IF1.ITFA_VL_ITEM) FROM FATURA F1 JOIN ITEM_FATURA IF1 ON F1.FATU_CD_FATURA = IF1.FATU_CD_FATURA WHERE F1.FATU_CD_FATURA = F.FATU_CD_FATURA) MOV_RE_VALORMOE
       FROM FATURA F
       JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
       JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
       LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
       LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
       LEFT JOIN INTEG_FATURA_PARAMETRO I on I.TISE_CD_TIPO_SERVICO = IF.TISE_CD_TIPO_SERVICO AND I.EMPR_CD_EMPRESA = DF.EMPR_CD_EMPRESA AND I.FORE_CD_FONTE_RECEITA = IF.FORE_CD_FONTE_RECEITA
       LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN VW_MEGA_CCUSTO CC ON CONV.CONV_CD_CCUSTO_MEGA = CC.CUS_IN_REDUZIDO
       LEFT JOIN VW_MEGA_PROJETO PP ON CONV.CONV_CD_PROJETO_MEGA = PP.COD_IN_PROJETO
       WHERE F.FATU_IN_STATUS IN ('AP', 'ER')
       and (EMP.EMPR_CD_EMPRESA_MATRIZ IN (1,3) AND DF.MOED_CD_MOEDA = 1)
       and c.clie_in_tipo = 'NAC'
union all

SELECT IF.ITFA_CD_ITEM_FATURA              ID_VIEW,
       F.FATU_CD_FATURA                    INVOICE_CODE,
       'I'                                 NOTA_FISCAL_OPERACAO,
       EMP.EMPR_CD_ERP_FILIAL              FIL_IN_CODIGO,
       TO_DATE(SYSDATE, 'DD/MM/RRRR')      NOT_DT_EMISSAO,
       TO_DATE(SYSDATE, 'DD/MM/RRRR')      NOT_DT_SAIDA,
       F.FATU_ST_COND_PGTO                 COND_ST_CODIGO,
       'N'                                 NOT_CH_SITUACAO,
       CASE EMP.EMPR_CD_ERP_FILIAL WHEN 3162  THEN 149
                                   WHEN 1976  THEN 148
                                   WHEN 2872  THEN 61
                                   WHEN 12803 THEN 62
                                   WHEN 3     THEN 146
                                   WHEN 3163  THEN 147 END TPD_IN_CODIGO,
       'C'                                 AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA               CALC_AGN_ST_CODIGO,
       'COD'                               CALC_AGN_ST_TIPOCODIGO,
       'I'                                 ITEM_NOTA_FISCAL_OPERACAO,
       ROWNUM                              ITN_IN_SEQUENCIA,
       I.INFP_CD_ERP_ITEM                  PRO_IN_CODIGO,
       1                                   ITN_RE_QUANTIDADE,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       IF.ITFA_VL_ITEM
          ELSE
       ROUND(IF.ITFA_VL_ITEM * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END  ITN_RE_VALORUNITARIO,
       24                                  APL_IN_CODIGO,
        DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       NVL(PP.PRO_IDE_ST_CODIGO, EMP.EMPR_CD_ERP_PROJ_IDE) PROJ_IDE_ST_CODIGO,
       NVL(PP.COD_IN_PROJETO, EMP.EMPR_CD_ERP_PROJETO)     PROJ_IN_REDUZIDO,
       NVL(CC.CUS_IDE_ST_CODIGO, EMP.EMPR_CD_ERP_CUS_IDE)  CUS_IDE_ST_CODIGO,
       NVL(CC.CUS_IN_REDUZIDO, EMP.EMPR_CD_ERP_CCUSTO)      CUS_IN_REDUZIDO,
       I.INFP_CD_ERP_SERVICO               COS_IN_CODIGO,
       'I'                                 OBSERVACAO_NF_OPERACAO,
       'N'                                 NOB_CH_TIPO_OBSERVACAO,
       IF.ITFA_TX_OBSERVACAO               NOB_ST_OBSERVACAO,
       'I'                                 PARCELAS_OPERACAO,
       1                                   MOV_ST_PARCELA,
       F.FATU_DT_VENCIMENTO                MOV_DT_VENCTO,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       (SELECT SUM(IF1.ITFA_VL_ITEM) FROM FATURA F1 JOIN ITEM_FATURA IF1 ON F1.FATU_CD_FATURA = IF1.FATU_CD_FATURA WHERE F1.FATU_CD_FATURA = F.FATU_CD_FATURA)
       ELSE
       ROUND((SELECT SUM(IF1.ITFA_VL_ITEM) FROM FATURA F1 JOIN ITEM_FATURA IF1 ON F1.FATU_CD_FATURA = IF1.FATU_CD_FATURA WHERE F1.FATU_CD_FATURA = F.FATU_CD_FATURA) * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END    MOV_RE_VALORMOE
       FROM FATURA F
       JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
       JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
       LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
       LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
       LEFT JOIN INTEG_FATURA_PARAMETRO I on I.TISE_CD_TIPO_SERVICO = IF.TISE_CD_TIPO_SERVICO AND I.EMPR_CD_EMPRESA = DF.EMPR_CD_EMPRESA AND I.FORE_CD_FONTE_RECEITA = IF.FORE_CD_FONTE_RECEITA
       LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
       LEFT JOIN VW_MEGA_CCUSTO CC ON CONV.CONV_CD_CCUSTO_MEGA = CC.CUS_IN_REDUZIDO
       LEFT JOIN VW_MEGA_PROJETO PP ON CONV.CONV_CD_PROJETO_MEGA = PP.COD_IN_PROJETO
       WHERE F.FATU_IN_STATUS IN ('AP', 'ER')
       and EMP.EMPR_CD_EMPRESA_MATRIZ IN (1,3)
       and c.clie_in_tipo = 'INT'

union all

      SELECT
      ID_VIEW,
       INVOICE_CODE,
       NOTA_FISCAL_OPERACAO,
       FIL_IN_CODIGO,
       NOT_DT_EMISSAO,
       NOT_DT_SAIDA,
       COND_ST_CODIGO,
       NOT_CH_SITUACAO,
      TPD_IN_CODIGO,
       AGN_TAU_ST_CODIGO,
       CALC_AGN_ST_CODIGO,
       CALC_AGN_ST_TIPOCODIGO,
       ITEM_NOTA_FISCAL_OPERACAO,
       ITN_IN_SEQUENCIA,
       PRO_IN_CODIGO,
       ITN_RE_QUANTIDADE,
      ITN_RE_VALORUNITARIO,
      APL_IN_CODIGO,
      TPC_ST_CLASSE,
      PROJ_IDE_ST_CODIGO,
      PROJ_IN_REDUZIDO,
      CUS_IDE_ST_CODIGO,
      CUS_IN_REDUZIDO,
      COS_IN_CODIGO,
      OBSERVACAO_NF_OPERACAO,
      NOB_CH_TIPO_OBSERVACAO,
      NOB_ST_OBSERVACAO,
      PARCELAS_OPERACAO,
      MOV_ST_PARCELA,
      MOV_DT_VENCTO,
      MOV_RE_VALORMOE

      FROM (
        SELECT RDF.REDF_CD_RECEITA_DFISCAL          ID_VIEW,
               RDF.REDF_CD_RECEITA_DFISCAL          INVOICE_CODE,
               'I'                                  NOTA_FISCAL_OPERACAO,
               decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
               TO_DATE(SYSDATE, 'DD/MM/RRRR')       NOT_DT_EMISSAO,
               TO_DATE(SYSDATE, 'DD/MM/RRRR')       NOT_DT_SAIDA,
               'PMS'                                COND_ST_CODIGO,
               'N'                       NOT_CH_SITUACAO,
               CASE decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL) WHEN 3162  THEN 149
                                                                                                             WHEN 1976  THEN 148
                                                                                                             WHEN 2872  THEN 61
                                                                                                             WHEN 12803 THEN 62
                                                                                                             WHEN 3     THEN 146
                                                                                                             WHEN 3163  THEN 147 END TPD_IN_CODIGO,
               'C'                                 AGN_TAU_ST_CODIGO,
               2421                                CALC_AGN_ST_CODIGO,
               'COD'                               CALC_AGN_ST_TIPOCODIGO,
               'I'                                 ITEM_NOTA_FISCAL_OPERACAO,
               1                                   ITN_IN_SEQUENCIA,
               ts.tise_cd_erp                      PRO_IN_CODIGO,
                1                                  ITN_RE_QUANTIDADE,


               ROUND((select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp)
                             * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)  ITN_RE_VALORUNITARIO,
                39                                  APL_IN_CODIGO,
                DECODE( EMP.EMPR_CD_EMPRESA_MATRIZ, 3, 'DA-Vd S', 'Vd Serv')    TPC_ST_CLASSE,
                 NVL(PP.PRO_IDE_ST_CODIGO, EMP.EMPR_CD_ERP_PROJ_IDE)            PROJ_IDE_ST_CODIGO,
                 NVL(PP.COD_IN_PROJETO, EMP.EMPR_CD_ERP_PROJETO)                PROJ_IN_REDUZIDO,
                 NVL(CC.CUS_IDE_ST_CODIGO, EMP.EMPR_CD_ERP_CUS_IDE)             CUS_IDE_ST_CODIGO,
                 NVL(CC.CUS_IN_REDUZIDO, EMP.EMPR_CD_ERP_CCUSTO)                CUS_IN_REDUZIDO,
                 w.cos_in_codigo                                          COS_IN_CODIGO,
                 'I'                                                            OBSERVACAO_NF_OPERACAO,
                 'N'                                                            NOB_CH_TIPO_OBSERVACAO,
                 'Invoice Intercompany'                                         NOB_ST_OBSERVACAO,
                 'I'                                                            PARCELAS_OPERACAO,
                 1                                                              MOV_ST_PARCELA,
                 trunc(sysdate + cpgto.days)                                                             MOV_DT_VENCTO,
                 ROUND(
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp)
                             * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
                              MOV_RE_VALORMOE

        FROM RECEITA R
        JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
        JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
        JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
        LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
        LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
        LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
        left join tipo_servico ts on ts.tise_cd_tipo_servico = tsdf.tise_cd_tipo_servico
        LEFT JOIN integ_fatura_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
        LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = ts.tise_cd_erp
        LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
        LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
        LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
        LEFT JOIN VW_MEGA_CCUSTO CC ON CONV.CONV_CD_CCUSTO_MEGA = CC.CUS_IN_REDUZIDO
        LEFT JOIN VW_MEGA_PROJETO PP ON CONV.CONV_CD_PROJETO_MEGA = PP.COD_IN_PROJETO
        LEFT JOIN MGCLI.VW_MEGA_CLIENTE_COND_PAGTO@ln_mgweb cpgto on cpgto.agent_code = 2421 and cpgto.payment_cond_name = '60 DIAS'
        INNER JOIN HISTORICO_PERCENTUAL_INTERCOMP hpi ON hpi.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL AND hpi.HIPI_DT_FIM is null
        WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
        AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
        AND RM.REMO_VL_TOTAL_MOEDA > 0
        AND DF.DEFI_IN_INTERCOMPANY = 'Y'
        AND hpi.EMPR_CD_EMP_INTERCOMP in (select emp_int.EMPR_CD_EMPRESA from EMPRESA emp_int where emp_int.EMPR_CD_EMPRESA_MATRIZ = 1)
        ) inter
    WHERE inter.ITN_RE_VALORUNITARIO > 0;

--changeset rbastos:215
--comment: Alteração na view vw_pms_integ_revenue_national para considerar receitas geradas pelas invoices contractors e reemb exp e duty hours
CREATE OR REPLACE VIEW VW_PMS_INTEG_REVENUE_NATIONAL AS
SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RDF.REDF_VL_RECEITA       NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RDF.REDF_VL_RECEITA       ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RDF.REDF_VL_RECEITA       MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND DF.DEFI_IN_INTERCOMPANY = 'N'
AND (EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3) AND DF.MOED_CD_MOEDA = 1)
AND RDF.REDF_VL_RECEITA > 0
AND C.CLIE_IN_TIPO = 'NAC'

UNION ALL


SELECT REVENUE_CODE,
       REVENUE_SOURCE,
       IS_INTERCOMPANY,
       NOTA_FISCAL_OPERACAO,
       FIL_IN_CODIGO,
       NOT_DT_EMISSAO,
       NOT_DT_SAIDA,
       COND_ST_CODIGO,
       NOT_RE_VALORTOTAL,
       NOT_CH_SITUACAO,
       TPD_IN_CODIGO,
       AGN_TAU_ST_CODIGO,
       CALC_AGN_ST_CODIGO,
       CALC_AGN_ST_TIPOCODIGO,
       ITEM_NOTA_FISCAL_OPERACAO,
       ITN_IN_SEQUENCIA,
       PRO_IN_CODIGO,
       ITN_RE_QUANTIDADE,
       ITN_RE_VALORUNITARIO,
       APL_IN_CODIGO,
       TPC_ST_CLASSE,
       PROJ_IDE_ST_CODIGO,
       PROJ_IN_REDUZIDO,
       CUS_IDE_ST_CODIGO,
       CUS_IN_REDUZIDO,
       COS_IN_CODIGO,
       PARCELAS_OPERACAO,
       MOV_ST_PARCELA,
       MOV_RE_VALORMOE
    FROM (
        SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
               'SERVICES'                REVENUE_SOURCE,
               DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
               'I'                       NOTA_FISCAL_OPERACAO,
               decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
               LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
               LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
               'PMS'                     COND_ST_CODIGO,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) NOT_RE_VALORTOTAL,
               'N'                       NOT_CH_SITUACAO,
               38                        TPD_IN_CODIGO,
               'C'                       AGN_TAU_ST_CODIGO,
               2421     CALC_AGN_ST_CODIGO,
               'COD'                     CALC_AGN_ST_TIPOCODIGO,
               'I'                       ITEM_NOTA_FISCAL_OPERACAO,
               1                         ITN_IN_SEQUENCIA,
               i.inrp_cd_erp_item        PRO_IN_CODIGO,
               1                         ITN_RE_QUANTIDADE,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) ITN_RE_VALORUNITARIO,
               39                        APL_IN_CODIGO,
               DECODE(
                EMP.EMPR_CD_EMPRESA_MATRIZ,
                        3, 'DA-Vd S',
                           'Vd Serv')    TPC_ST_CLASSE,
               EMP.EMPR_CD_EMPRESA_MATRIZ,
               MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
               CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
               MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
               conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
               i.inrp_cd_erp_servico     COS_IN_CODIGO,
               'I'                       PARCELAS_OPERACAO,
               1                         MOV_ST_PARCELA,
               (select nvl(max((rdf1.redf_vl_receita/rm1.remo_vl_total_moeda) * sum(ir1.itre_vl_total_item) * (hi1.hipi_pr_intercompany/100)),0)
                             from receita_deal_fiscal rdf1
                             inner join receita_moeda rm1 on rm1.remo_cd_receita_moeda = rdf1.remo_cd_receita_moeda
                             inner join receita r1 on rm1.rece_cd_receita = r1.rece_cd_receita
                             inner join deal_fiscal df1 on rdf1.defi_cd_deal_fiscal = df1.defi_cd_deal_fiscal
                             inner join historico_percentual_intercomp hi1 on df1.defi_cd_deal_fiscal = hi1.defi_cd_deal_fiscal and hi1.hipi_dt_fim is null
                             inner join item_receita ir1 on rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                             inner join pessoa p1 on ir1.pess_cd_pessoa= p1.pess_cd_pessoa
                             inner join pessoa_tipo_contrato ptc1 on p1.pess_cd_pessoa = ptc1.pess_cd_pessoa and ptc1.petc_dt_fim is null
                             inner join empresa e1 on ptc1.empr_cd_empresa = e1.empr_cd_empresa_matriz and (hi1.empr_cd_emp_intercomp = e1.empr_cd_empresa or df1.empr_cd_empresa = e1.empr_cd_empresa)
                             where df1.defi_in_intercompany = 'Y'
                             and ptc1.empr_cd_empresa = (select e2.empr_cd_empresa_matriz
                            from empresa e2
                            where e2.empr_cd_empresa = DF.EMPR_CD_EMP_INTERCOMP)
                             and rdf1.remo_cd_receita_moeda = RM.REMO_CD_RECEITA_MOEDA
                             AND e1.empr_cd_empresa <> DF1.EMPR_CD_EMPRESA
                             AND rdf1.defi_cd_deal_fiscal = DF.DEFI_CD_DEAL_FISCAL
                             group by rdf1.remo_cd_receita_moeda,rdf1.redf_vl_receita,rm1.remo_vl_total_moeda,hi1.hipi_pr_intercompany,hi1.empr_cd_emp_intercomp) MOV_RE_VALORMOE
        FROM RECEITA R
        JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
        JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
        JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
        LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
        LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
        LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
        LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
        LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
        LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
        LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
        LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
        LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
        LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
        INNER JOIN HISTORICO_PERCENTUAL_INTERCOMP hpi ON hpi.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL AND hpi.HIPI_DT_FIM is null
        WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
        AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
        AND RM.REMO_VL_TOTAL_MOEDA > 0
        AND DF.DEFI_IN_INTERCOMPANY = 'Y'
        AND hpi.EMPR_CD_EMP_INTERCOMP in (select emp_int.EMPR_CD_EMPRESA from EMPRESA emp_int where emp_int.EMPR_CD_EMPRESA_MATRIZ = 1)) inter
    WHERE inter.NOT_RE_VALORTOTAL > 0

UNION ALL


SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)   FIL_IN_CODIGO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_EMISSAO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       RL.RELI_VL_RECEITA        NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       RL.RELI_VL_RECEITA        ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       RL.RELI_VL_RECEITA        MOV_RE_VALORMOE
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = RL.copr_cd_contrato_pratica and RL.RELI_DT_MES between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS = 'W'
AND RL.RELI_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND RL.RELI_VL_RECEITA > 0
and (EMP.EMPR_CD_EMPRESA_MATRIZ IN (1,3) AND DF.MOED_CD_MOEDA = 1)
and c.clie_in_tipo = 'NAC'

UNION ALL

SELECT RL.RELI_CD_RECEITA_LICENCA REVENUE_CODE,
       'LICENSES'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)   FIL_IN_CODIGO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_EMISSAO,
       LAST_DAY(RL.RELI_DT_MES)  NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RL.RELI_VL_RECEITA 
          ELSE
       ROUND(RL.RELI_VL_RECEITA  * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END                       NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RL.RELI_VL_RECEITA
          ELSE
       ROUND(RL.RELI_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END                       ITN_RE_VALORUNITARIO,
       24                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RL.RELI_VL_RECEITA
          ELSE
       ROUND(RL.RELI_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END                       MOV_RE_VALORMOE
FROM RECEITA_LICENCA RL
JOIN DEAL_FISCAL DF ON RL.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 4
LEFT JOIN CONVERGENCIA CONV ON RL.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = RL.copr_cd_contrato_pratica and RL.RELI_DT_MES between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RL.RELI_IN_STATUS IS NULL OR RL.RELI_IN_STATUS = 'W'
AND RL.RELI_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND RL.RELI_VL_RECEITA > 0
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)
AND C.CLIE_IN_TIPO = 'INT'

UNION ALL


SELECT RDF.REDF_CD_RECEITA_DFISCAL REVENUE_CODE,
       'SERVICES'                REVENUE_SOURCE,
       'N'                       IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RDF.REDF_VL_RECEITA
          ELSE
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END     NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RDF.REDF_VL_RECEITA
          ELSE
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END     ITN_RE_VALORUNITARIO,
       24                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RDF.REDF_VL_RECEITA
          ELSE
       ROUND(RDF.REDF_VL_RECEITA * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END    MOV_RE_VALORMOE
       FROM RECEITA R
JOIN RECEITA_MOEDA RM ON R.RECE_CD_RECEITA = RM.RECE_CD_RECEITA
JOIN RECEITA_DEAL_FISCAL RDF ON RM.REMO_CD_RECEITA_MOEDA = RDF.REMO_CD_RECEITA_MOEDA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = 1
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE (R.RECE_IN_VERSAO IN ('PB', 'PD') OR RDF.REDF_IN_STATUS = 'E')
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)
AND C.CLIE_IN_TIPO = 'INT'

AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

UNION ALL

SELECT RP.REPL_CD_RECEITA_PLANTAO REVENUE_CODE,
       'DUTY_HOURS'                REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY   IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
      RP.REPL_VL_RECEITA_PLANTAO NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
      RP.REPL_VL_RECEITA_PLANTAO ITN_RE_VALORUNITARIO,
       26                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
      RP.REPL_VL_RECEITA_PLANTAO MOV_RE_VALORMOE
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = decode(df.defi_in_intercompany,'Y',df.empr_cd_emp_intercomp,DF.EMPR_CD_EMPRESA) AND i.fore_cd_fonte_receita = 7
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RP.REPL_IN_INTEGRACAO = 'N' AND RP.REPL_VL_RECEITA_PLANTAO > 0
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
and (EMP.EMPR_CD_EMPRESA_MATRIZ IN (1,3) AND DF.MOED_CD_MOEDA = 1)
and c.clie_in_tipo = 'NAC'

UNION ALL

SELECT RP.REPL_CD_RECEITA_PLANTAO REVENUE_CODE,
       'DUTY_HOURS'                REVENUE_SOURCE,
       'N'                       IS_INTERCOMPANY,
       'I'                       NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)    FIL_IN_CODIGO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_EMISSAO,
       LAST_DAY(R.RECE_DT_MES)   NOT_DT_SAIDA,
       'PMS'                     COND_ST_CODIGO,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RP.REPL_VL_RECEITA_PLANTAO
          ELSE
       ROUND(RP.REPL_VL_RECEITA_PLANTAO * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END     NOT_RE_VALORTOTAL,
       'N'                       NOT_CH_SITUACAO,
       38                        TPD_IN_CODIGO,
       'C'                       AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA     CALC_AGN_ST_CODIGO,
       'COD'                     CALC_AGN_ST_TIPOCODIGO,
       'I'                       ITEM_NOTA_FISCAL_OPERACAO,
       1                         ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item        PRO_IN_CODIGO,
       1                         ITN_RE_QUANTIDADE,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RP.REPL_VL_RECEITA_PLANTAO
          ELSE
       ROUND(RP.REPL_VL_RECEITA_PLANTAO * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END                        ITN_RE_VALORUNITARIO,
       24                        APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')    TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO      PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO      CUS_IDE_ST_CODIGO,
       conv.conv_cd_ccusto_mega          CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico     COS_IN_CODIGO,
       'I'                       PARCELAS_OPERACAO,
       1                         MOV_ST_PARCELA,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       RP.REPL_VL_RECEITA_PLANTAO
          ELSE
       ROUND(RP.REPL_VL_RECEITA_PLANTAO * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
      END                        MOV_RE_VALORMOE
       FROM RECEITA_PLANTAO RP
JOIN RECEITA_DEAL_FISCAL RDF ON RP.REDF_CD_RECEITA_DFISCAL = RDF.REDF_CD_RECEITA_DFISCAL
JOIN RECEITA_MOEDA RM ON RDF.REMO_CD_RECEITA_MOEDA = RM.REMO_CD_RECEITA_MOEDA
JOIN RECEITA R ON RM.RECE_CD_RECEITA = R.RECE_CD_RECEITA
JOIN DEAL_FISCAL DF ON RDF.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = TSDF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = decode(df.defi_in_intercompany,'Y',df.empr_cd_emp_intercomp,DF.EMPR_CD_EMPRESA) AND i.fore_cd_fonte_receita = 7
LEFT join vw_item_codserv_pms@LN_MGWEB w on w.pro_in_codigo = i.inrp_cd_erp_item
LEFT JOIN CONVERGENCIA CONV ON R.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT join contrato_pratica_grupo_custo CPGC on CPGC.copr_cd_contrato_pratica = R.copr_cd_contrato_pratica and R.rece_dt_mes between CPGC.cpgc_dt_inicio_vigencia and nvl(CPGC.cpgc_dt_fim_vigencia,trunc(sysdate+30,'MM'))
LEFT join grupo_custo GC on CPGC.grcu_cd_grupo_custo = GC.grcu_cd_grupo_custo
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE RP.REPL_IN_INTEGRACAO = 'N' AND RP.REPL_VL_RECEITA_PLANTAO > 0
AND R.RECE_DT_MES >= TO_DATE('01/01/2018', 'dd/MM/yyyy')
AND EMP.EMPR_CD_EMPRESA_MATRIZ in (1,3)
AND C.CLIE_IN_TIPO = 'INT'

UNION ALL

SELECT IF.ITFA_CD_ITEM_FATURA        REVENUE_CODE,
       'REIMB. EXP'                  REVENUE_SOURCE,
       DF.DEFI_IN_INTERCOMPANY       IS_INTERCOMPANY,
       'I'                           NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)        FIL_IN_CODIGO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_EMISSAO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_SAIDA,
       'PMS'                         COND_ST_CODIGO,
       IF.ITFA_VL_ITEM               NOT_RE_VALORTOTAL,
       'N'                           NOT_CH_SITUACAO,
       38                            TPD_IN_CODIGO,
       'C'                           AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA         CALC_AGN_ST_CODIGO,
       'COD'                         CALC_AGN_ST_TIPOCODIGO,
       'I'                           ITEM_NOTA_FISCAL_OPERACAO,
       1                             ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item            PRO_IN_CODIGO,
       1                             ITN_RE_QUANTIDADE,
       IF.ITFA_VL_ITEM               ITN_RE_VALORUNITARIO,
       26                            APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')        TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO          PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA     PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO          CUS_IDE_ST_CODIGO,
       MC.cus_in_reduzido            CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico         COS_IN_CODIGO,
       'I'                           PARCELAS_OPERACAO,
       1                             MOV_ST_PARCELA,
       IF.ITFA_VL_ITEM               MOV_RE_VALORMOE
FROM FATURA F
JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL and IF.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = IF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = if.fore_cd_fonte_receita
LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE F.FATU_IN_STATUS IN ('AP', 'ER')
and (EMP.EMPR_CD_EMPRESA_MATRIZ IN (1,3) AND DF.MOED_CD_MOEDA = 1)
and c.clie_in_tipo = 'NAC'
and if.fore_cd_fonte_receita IN (2,3)
and f.fatu_dt_previsao >= TO_DATE('01/01/2018', 'dd/MM/yyyy')

union all

SELECT IF.ITFA_CD_ITEM_FATURA        REVENUE_CODE,
       'REIMB. EXP'                  REVENUE_SOURCE,
       'N'                           IS_INTERCOMPANY,
       'I'                           NOTA_FISCAL_OPERACAO,
       decode(EMP.EMPR_CD_ERP_FILIAL,2522,EMP_INTER.EMPR_CD_ERP_FILIAL,EMP.EMPR_CD_ERP_FILIAL)        FIL_IN_CODIGO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_EMISSAO,
       LAST_DAY(F.FATU_DT_PREVISAO)  NOT_DT_SAIDA,
       'PMS'                         COND_ST_CODIGO,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       IF.ITFA_VL_ITEM
          ELSE
       ROUND(IF.ITFA_VL_ITEM * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2) 
       END  NOT_RE_VALORTOTAL,
       'N'                           NOT_CH_SITUACAO,
       38                            TPD_IN_CODIGO,
       'C'                           AGN_TAU_ST_CODIGO,
       C.CLIE_CD_AGENTE_MEGA         CALC_AGN_ST_CODIGO,
       'COD'                         CALC_AGN_ST_TIPOCODIGO,
       'I'                           ITEM_NOTA_FISCAL_OPERACAO,
       1                             ITN_IN_SEQUENCIA,
       i.inrp_cd_erp_item            PRO_IN_CODIGO,
       1                             ITN_RE_QUANTIDADE,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       IF.ITFA_VL_ITEM
          ELSE
       ROUND(IF.ITFA_VL_ITEM * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END  ITN_RE_VALORUNITARIO,
       24                            APL_IN_CODIGO,
       DECODE(
        EMP.EMPR_CD_EMPRESA_MATRIZ,
                3, 'DA-Vd S',
                   'Vd Serv')        TPC_ST_CLASSE,
       MP.PRO_IDE_ST_CODIGO          PROJ_IDE_ST_CODIGO,
       CONV.CONV_CD_PROJETO_MEGA     PROJ_IN_REDUZIDO,
       MC.CUS_IDE_ST_CODIGO          CUS_IDE_ST_CODIGO,
       MC.cus_in_reduzido            CUS_IN_REDUZIDO,
       i.inrp_cd_erp_servico         COS_IN_CODIGO,
       'I'                           PARCELAS_OPERACAO,
       1                             MOV_ST_PARCELA,
       CASE WHEN DF.MOED_CD_MOEDA = 1 THEN
       IF.ITFA_VL_ITEM
          ELSE
       ROUND(IF.ITFA_VL_ITEM * (SELECT CMM.COMM_VL_COTACAO_VENDA FROM COTACAO_MOEDA_MEDIA CMM WHERE CMM.MOED_CD_MOEDA_DE = DF.MOED_CD_MOEDA AND CMM.MOED_CD_MOEDA_PARA = 1 AND CMM.COMM_DT_DIA_COTACAO_MOEDA_MEDI = TO_DATE(SYSDATE, 'DD/MM/RRRR') - 1),2)
       END    MOV_RE_VALORMOE
FROM FATURA F
JOIN ITEM_FATURA IF ON F.FATU_CD_FATURA = IF.FATU_CD_FATURA
JOIN DEAL_FISCAL DF ON F.DEFI_CD_DEAL_FISCAL = DF.DEFI_CD_DEAL_FISCAL
LEFT JOIN TIPO_SERVICO_DEAL_FISCAL TSDF ON DF.DEFI_CD_DEAL_FISCAL = TSDF.DEFI_CD_DEAL_FISCAL and IF.TISE_CD_TIPO_SERVICO = TSDF.TISE_CD_TIPO_SERVICO
LEFT JOIN EMPRESA EMP ON DF.EMPR_CD_EMPRESA = EMP.EMPR_CD_EMPRESA
LEFT JOIN EMPRESA EMP_INTER ON DF.EMPR_CD_EMP_INTERCOMP = EMP_INTER.EMPR_CD_EMPRESA
LEFT JOIN CLIENTE C ON DF.CLIE_CD_CLIENTE = C.CLIE_CD_CLIENTE
LEFT JOIN integ_receita_parametro i on i.tise_cd_tipo_servico = IF.TISE_CD_TIPO_SERVICO AND i.empr_cd_empresa = DF.EMPR_CD_EMPRESA AND i.fore_cd_fonte_receita = if.fore_cd_fonte_receita
LEFT JOIN CONTRATO_PRATICA CP ON IF.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA
LEFT JOIN CONVERGENCIA CONV ON CP.COPR_CD_CONTRATO_PRATICA = CONV.COPR_CD_CONTRATO_PRATICA
LEFT JOIN VW_MEGA_CCUSTO MC ON CONV.CONV_CD_CCUSTO_MEGA = MC.CUS_IN_REDUZIDO
LEFT JOIN VW_MEGA_PROJETO MP ON CONV.CONV_CD_PROJETO_MEGA = MP.COD_IN_PROJETO
WHERE F.FATU_IN_STATUS IN ('AP', 'ER')
and EMP.EMPR_CD_EMPRESA_MATRIZ IN (1,3)
and c.clie_in_tipo = 'INT'
and if.fore_cd_fonte_receita IN (2,3)
and f.fatu_dt_previsao >= TO_DATE('01/01/2018', 'dd/MM/yyyy');

--changeset aneor:216
--comment: Carga e criação da tabela de configuração das empresas de intercompany
CREATE TABLE INTERCOMP_CONFIG (
     INCO_CD_INTERCOMP_CONFIG NUMBER(18,0) NOT NULL
    ,EMPR_CD_EMPRESA_PRINCIPAL NUMBER(18,0) NOT NULL
    ,EMPR_CD_EMPRESA_INTERCOMPANY NUMBER(18,0) NOT NULL
    ,INCO_IN_ATIVO CHAR(1) CHECK (INCO_IN_ATIVO IN ('Y','N')) NOT NULL
    ,CONSTRAINT INTERCOMP_CONFIG_PK PRIMARY KEY (INCO_CD_INTERCOMP_CONFIG)
);

COMMENT ON COLUMN "INTERCOMP_CONFIG"."INCO_CD_INTERCOMP_CONFIG" IS 'Primary Key da tabela';
COMMENT ON COLUMN "INTERCOMP_CONFIG"."EMPR_CD_EMPRESA_PRINCIPAL" IS 'Id da empresa principal';
COMMENT ON COLUMN "INTERCOMP_CONFIG"."EMPR_CD_EMPRESA_INTERCOMPANY" IS 'Id da empresa intercompany';
COMMENT ON COLUMN "INTERCOMP_CONFIG"."INCO_IN_ATIVO" IS 'Status Ativo ou Inativo';

CREATE SEQUENCE  "SQ_INT_CD_INTERCOMP_CONFIG"  MINVALUE 1 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20;

ALTER TABLE INTERCOMP_CONFIG
ADD CONSTRAINT FK_INTERCOMP_EMPRESA_PRINCIPAL FOREIGN KEY (EMPR_CD_EMPRESA_PRINCIPAL)
REFERENCES EMPRESA (EMPR_CD_EMPRESA);


ALTER TABLE INTERCOMP_CONFIG
ADD CONSTRAINT FK_INTERCOMP_EMPRESA_INTERCOMP FOREIGN KEY (EMPR_CD_EMPRESA_INTERCOMPANY)
REFERENCES EMPRESA (EMPR_CD_EMPRESA);

ALTER TABLE INTERCOMP_CONFIG
ADD CONSTRAINT INCO_UN_COMPANIES UNIQUE (EMPR_CD_EMPRESA_PRINCIPAL, EMPR_CD_EMPRESA_INTERCOMPANY);

DECLARE
  insertIntercomp VARCHAR2(4000);
  software integer;
  inc integer;
  canada integer;
  uk integer;
  japan integer;
  china integer;
  iot integer;
  active char;
  inactive char;
BEGIN
  software := 1;
  inc := 9;
  canada := 118;
  uk := 114;
  japan := 111;
  china := 110;
  iot := 120;
  da := 3;
  active := 'Y';
  inactive := 'N';
  insertIntercomp := 'INSERT INTO INTERCOMP_CONFIG VALUES (:1, :2, :3, :4)';

    ---- Software
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, software, inc, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, software, canada, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, software, uk, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, software, japan, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, software, china, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, software, iot, active;

    ---- INC
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, inc, software, active;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, inc, canada, active;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, inc, uk, active;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, inc, japan, active;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, inc, china, active;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, inc, iot, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, inc, da, active;

     ---- Canada
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, canada, software, active;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, canada, inc, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, canada, uk, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, canada, japan, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, canada, china, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, canada, iot, inactive;

     ---- UK
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, uk, software, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, uk, inc, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, uk, canada, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, uk, japan, active;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, uk, china, active;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, uk, iot, inactive;

     ---- Japan
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, japan, software, active;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, japan, inc, active;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, japan, canada, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, japan, uk, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, japan, china, active;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, japan, iot, inactive;

     ---- China
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, china, software, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, china, inc, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, china, canada, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, china, uk, active;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, china, japan, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, china, iot, inactive;

     ---- Iot
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, iot, software, active;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, iot, inc, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, iot, canada, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, iot, uk, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, iot, japan, inactive;
    EXECUTE IMMEDIATE insertIntercomp USING SQ_INT_CD_INTERCOMP_CONFIG.NEXTVAL, iot, china, inactive;

    COMMIT;
END;

--changeset francis:217 dbms:oracle
--comment: Add column ORDE_TB_PURPOSE in ORC_DESPESA table
alter table ORC_DESPESA add ORDE_TB_PURPOSE VARCHAR2(2) DEFAULT 'GE' NOT NULL;
COMMENT ON COLUMN ORC_DESPESA.ORDE_TB_PURPOSE IS 'TB Purpose do Travel Budget';


--changeset francis:218 dbms:oracle
--comment: Add column ORDE_TB_PURPOSE
CREATE OR REPLACE VIEW VW_PMS_ESTRUTURA_VOUCHER
AS select VOUC_NR_VOUCHER,ORDE_CD_ORCA_DESPESA,ORDE_TP_ORC_DESP,CONV_CD_PROJETO_MEGA,CONV_CD_CCUSTO_MEGA,EMPR_CD_EMPRESA,ORDE_TB_PURPOSE from (
  select v.VOUC_NR_VOUCHER, od.ORDE_CD_ORCA_DESPESA, od.ORDE_TP_ORC_DESP, c.CONV_CD_PROJETO_MEGA, c.CONV_CD_CCUSTO_MEGA, e.EMPR_CD_EMPRESA, od.ORDE_TB_PURPOSE
  from voucher v
  inner join ORC_DESPESA od on od.ORDE_TP_ORC_DESP = 'GC' and od.ORDE_CD_ORCA_DESPESA = v.ORDE_CD_ORCA_DESPESA
  inner join CONVERGENCIA c on c.GRCU_CD_GRUPO_CUSTO = v.GRCU_CD_GRUPO_CUSTO and c.CONV_SG_TIPO = od.ORDE_TP_ORC_DESP
  inner join GRUPO_CUSTO gc on gc.GRCU_CD_GRUPO_CUSTO = v.GRCU_CD_GRUPO_CUSTO
  inner join empresa e on e.EMPR_CD_EMPRESA = gc.EMPR_CD_EMPRESA

  union all

  select v.VOUC_NR_VOUCHER, od.ORDE_CD_ORCA_DESPESA, od.ORDE_TP_ORC_DESP, c.CONV_CD_PROJETO_MEGA, c.CONV_CD_CCUSTO_MEGA, e.EMPR_CD_EMPRESA, od.ORDE_TB_PURPOSE
  from voucher v
  inner join ORC_DESPESA od on od.ORDE_TP_ORC_DESP = 'CL' and od.ORDE_CD_ORCA_DESPESA = v.ORDE_CD_ORCA_DESPESA
  inner join CONVERGENCIA c on c.COPR_CD_CONTRATO_PRATICA = v.COPR_CD_CONTRATO_PRATICA and c.CONV_SG_TIPO = od.ORDE_TP_ORC_DESP
  inner join GRUPO_CUSTO gc on gc.GRCU_CD_GRUPO_CUSTO = c.GRCU_CD_GRUPO_CUSTO
  inner join empresa e on e.EMPR_CD_EMPRESA = gc.EMPR_CD_EMPRESA
) t


--changeset francis:219 dbms:oracle
--comment: Adicionando nova tabela Billability
CREATE TABLE BILLABILITY
(
	BILL_CD_BILLABILITY NUMBER(18) NOT NULL,
	BILL_NM_BILLABILITY VARCHAR2(100) NOT NULL,
	BILL_SG_BILLABILITY CHAR(2) NOT NULL,
	BILL_IN_ATIVO CHAR(1) NOT NULL
);

ALTER TABLE BILLABILITY
  ADD CONSTRAINT BILL_PK PRIMARY KEY (BILL_CD_BILLABILITY);

ALTER TABLE BILLABILITY
  ADD CONSTRAINT BILL_UN_SIGLA UNIQUE (BILL_SG_BILLABILITY);

ALTER TABLE BILLABILITY
  ADD CONSTRAINT BILL_CK_STATUS CHECK (BILL_IN_ATIVO IN ('Y', 'N'));

COMMENT ON COLUMN BILLABILITY.BILL_CD_BILLABILITY IS 'Codigo identificador da Billability';
COMMENT ON COLUMN BILLABILITY.BILL_NM_BILLABILITY IS 'Nome do Billability';
COMMENT ON COLUMN BILLABILITY.BILL_SG_BILLABILITY IS 'Sigla do Billability';
COMMENT ON COLUMN BILLABILITY.BILL_IN_ATIVO IS 'Status Ativo ou Inativo';


--changeset francis:220 dbms:oracle
--comment: Adicionando nova tabela Pessoa Billability
CREATE TABLE PESSOA_BILLABILITY
(
	PESS_CD_PESSOA NUMBER(18) NOT NULL,
	BILL_CD_BILLABILITY NUMBER(18) NOT NULL,
	PEBI_DT_INICIO DATE NOT NULL,
    PEBI_DT_FIM DATE NULL,
	PEBI_DT_CRIACAO DATE NOT NULL
);

ALTER TABLE PESSOA_BILLABILITY
  ADD CONSTRAINT PEBI_PK PRIMARY KEY (PESS_CD_PESSOA, PEBI_DT_INICIO);

ALTER TABLE PESSOA_BILLABILITY
   ADD CONSTRAINT FK_PEBI_PESS FOREIGN KEY (PESS_CD_PESSOA)
   REFERENCES PESSOA (PESS_CD_PESSOA);

ALTER TABLE PESSOA_BILLABILITY
   ADD CONSTRAINT FK_PEBI_BILL FOREIGN KEY (BILL_CD_BILLABILITY)
   REFERENCES BILLABILITY (BILL_CD_BILLABILITY);

COMMENT ON COLUMN PESSOA_BILLABILITY.PESS_CD_PESSOA IS 'Codigo identificador da Pessoa';
COMMENT ON COLUMN PESSOA_BILLABILITY.BILL_CD_BILLABILITY IS 'Codigo identificador da Billability';
COMMENT ON COLUMN PESSOA_BILLABILITY.PEBI_DT_INICIO IS 'Data de inicio da vigencia';
COMMENT ON COLUMN PESSOA_BILLABILITY.PEBI_DT_FIM IS 'Data final da vigência';
COMMENT ON COLUMN PESSOA_BILLABILITY.PEBI_DT_CRIACAO IS 'Data de criação da vigencia';


--changeset francis:221
--comment: Script para popular dados na tabela BILLABILITY
INSERT INTO BILLABILITY (BILL_CD_BILLABILITY,BILL_NM_BILLABILITY, BILL_SG_BILLABILITY, BILL_IN_ATIVO)
  VALUES (1, 'Fixed', 'NB', 'Y');

INSERT INTO BILLABILITY (BILL_CD_BILLABILITY,BILL_NM_BILLABILITY, BILL_SG_BILLABILITY, BILL_IN_ATIVO)
  VALUES (2, 'Billable', 'BL', 'Y');


--changeset rbastos:222
--comment: Script para criar a procedure que migra as informacoes de billability
create or replace procedure USP_PMS_UPDATE_BILLABILITY is

  cursor cr_pessoas is
    select p.pess_cd_pessoa
      from pessoa p
  order by p.pess_cd_pessoa;

  cursor cr_billabilities (cod_pessoa number) is
      select pgc.pegc_in_status,
             pgc.pegc_dt_inicio,
             pgc.pegc_dt_fim,
             pgc.pegc_dt_criacao
        from pessoa_grupo_custo pgc
       where pgc.pess_cd_pessoa = cod_pessoa order by pgc.pegc_dt_inicio;

  v_sg_billability  char(2) := '';
  v_cd_billability number;
  v_datainicio   date;

begin

  for r_pessoa in cr_pessoas loop
       v_sg_billability := '';
       v_datainicio := null;
       for r_billability in cr_billabilities(r_pessoa.pess_cd_pessoa) loop
         begin
           select t.bill_sg_billability,
                  t.pebi_dt_inicio
             into v_sg_billability,
                  v_datainicio
             from (select b.bill_sg_billability,
                          pb.pebi_dt_inicio
                     from pessoa_billability pb
                     join billability b on pb.bill_cd_billability = b.bill_cd_billability
                    where pb.pess_cd_pessoa = r_pessoa.pess_cd_pessoa
                    order by pb.pebi_dt_fim desc) t
            where rownum = 1;
         exception
           when others then
           null;
         end;

          if (v_sg_billability = r_billability.pegc_in_status) then
            update pessoa_billability pb set pb.pebi_dt_fim = r_billability.pegc_dt_fim
            where pb.pess_cd_pessoa = r_pessoa.pess_cd_pessoa
            and pb.pebi_dt_inicio = v_datainicio;
            commit;
          else
            begin
              select b.bill_cd_billability
                into v_cd_billability
                from billability b
               where b.bill_sg_billability = r_billability.pegc_in_status;
              exception
              when others then
                null;
            end;
            insert into pessoa_billability
            (pess_cd_pessoa,
             bill_cd_billability,
             pebi_dt_inicio,
             pebi_dt_fim,
             pebi_dt_criacao)
             values
             (r_pessoa.pess_cd_pessoa,
              v_cd_billability,
              r_billability.pegc_dt_inicio,
              r_billability.pegc_dt_fim,
              r_billability.pegc_dt_criacao);
              commit;
          end if;
       end loop;
  end loop;
  commit;
end USP_PMS_UPDATE_BILLABILITY;


--changeset francis:223 dbms:oracle
--comment: PESSOA_BILLABILITY_AUD audit
CREATE TABLE PESSOA_BILLABILITY_AUD
(
    REV NUMBER(18,0) NOT NULL,
    REVTYPE NUMBER(3,0),
    PESS_CD_PESSOA NUMBER(18,0) NOT NULL,
	BILL_CD_BILLABILITY NUMBER(18,0) NOT NULL,
    PEBI_DT_INICIO DATE NOT NULL,
    PEBI_DT_FIM DATE,
    PEBI_DT_CRIACAO DATE NOT NULL,
    CONSTRAINT PK_PEBI_AUD PRIMARY KEY (PESS_CD_PESSOA, BILL_CD_BILLABILITY, PEBI_DT_INICIO, REV),
    CONSTRAINT FK_PEDI_AUD FOREIGN KEY (REV) REFERENCES REVINFO (REV) ENABLE
);

--changeset rbastos:224 dbms:oracle
--comment: Ajuste View vw_alocacao_grupo_custo_fpp
create or replace view vw_alocacao_grupo_custo_fpp as
select agc."ALGC_CD_ALOCACAO",agc."GRCU_CD_GRUPO_CUSTO",agc."RECU_CD_RECURSO",agc."GDTV_CD_TIPO_VISAO",agc."ALGC_DT_RESCISAO",agc."ALGC_TX_LOGIN_AUTOR",agc."ALGC_DT_UPDATE",agc."ALGC_IN_RESCINDIDO_FPP",agc."EMPR_CD_EMPRESA",agc."CIBA_CD_CIDADE_BASE"
from alocacao_grupo_custo agc
inner join recurso r on agc.recu_cd_recurso = r.recu_cd_recurso and r.recu_in_tipo_recurso = 'PA'

union all


select agc."ALGC_CD_ALOCACAO",agc."GRCU_CD_GRUPO_CUSTO",agc."RECU_CD_RECURSO",agc."GDTV_CD_TIPO_VISAO",agc."ALGC_DT_RESCISAO",agc."ALGC_TX_LOGIN_AUTOR",agc."ALGC_DT_UPDATE",agc."ALGC_IN_RESCINDIDO_FPP",agc."EMPR_CD_EMPRESA",agc."CIBA_CD_CIDADE_BASE"
from ALOCACAO_GRUPO_CUSTO agc
inner join pessoa p on agc.recu_cd_recurso = p.recu_cd_recurso
inner join pessoa_grupo_custo pgc on p.pess_cd_pessoa = pgc.pess_cd_pessoa and pgc.pegc_dt_fim is null
inner join grupo_custo_area_orcamentaria gcao on pgc.grcu_cd_grupo_custo = gcao.grcu_cd_grupo_custo and gcao.gcao_dt_fim is null
where gcao.aror_cd_area_orcamentaria != 34 

union all


select agc."ALGC_CD_ALOCACAO",agc."GRCU_CD_GRUPO_CUSTO",agc."RECU_CD_RECURSO",agc."GDTV_CD_TIPO_VISAO",agc."ALGC_DT_RESCISAO",agc."ALGC_TX_LOGIN_AUTOR",agc."ALGC_DT_UPDATE",agc."ALGC_IN_RESCINDIDO_FPP",agc."EMPR_CD_EMPRESA",agc."CIBA_CD_CIDADE_BASE"
from ALOCACAO_GRUPO_CUSTO agc
inner join pessoa p on agc.recu_cd_recurso = p.recu_cd_recurso
inner join pessoa_grupo_custo pgc on p.pess_cd_pessoa = pgc.pess_cd_pessoa and pgc.pegc_dt_fim is null 
inner join pessoa_billability pb on pb.pess_cd_pessoa = p.pess_cd_pessoa and ((TRUNC(sysdate, 'MM') between pb.pebi_dt_inicio and pb.pebi_dt_fim) or 
                         (pb.pebi_dt_fim is null and pb.pebi_dt_inicio <= TRUNC(sysdate, 'MM'))) and pb.bill_cd_billability = 1
inner join grupo_custo_area_orcamentaria gcao on pgc.grcu_cd_grupo_custo = gcao.grcu_cd_grupo_custo and gcao.gcao_dt_fim is null
where gcao.aror_cd_area_orcamentaria = 34;

--changeset rbastos:225 dbms:oracle
--comment: Ajuste View vw_pms_alocacao_gc_vigente
create or replace view vw_pms_alocacao_gc_vigente as
select rownum ALGC_CD_ALOCADO, AROR_CD_AREA_ORCAMENTARIA, GRCU_CD_GRUPO_CUSTO, RECU_CD_RECURSO, ALGC_DT_RESCISAO, status
from (
select gcao.AROR_CD_AREA_ORCAMENTARIA, gc.GRCU_CD_GRUPO_CUSTO GRCU_CD_GRUPO_CUSTO, r.RECU_CD_RECURSO RECU_CD_RECURSO, p.PESS_DT_RESCISAO ALGC_DT_RESCISAO, b.BILL_SG_BILLABILITY status
from pessoa p
inner join PESSOA_GRUPO_CUSTO pgc on PGC.PESS_CD_PESSOA = P.PESS_CD_PESSOA and PGC.PEGC_DT_FIM is null
inner join PESSOA_BILLABILITY pb on pb.PESS_CD_PESSOA = P.PESS_CD_PESSOA and ((TRUNC(sysdate, 'MM') between pb.pebi_dt_inicio and pb.pebi_dt_fim) or 
                         (pb.pebi_dt_fim is null and pb.pebi_dt_inicio <= TRUNC(sysdate, 'MM')))
inner join BILLABILITY b on pb.BILL_CD_BILLABILITY = b.BILL_CD_BILLABILITY
inner join grupo_custo gc on GC.GRCU_CD_GRUPO_CUSTO = PGC.GRCU_CD_GRUPO_CUSTO
        and gc.GRCU_IN_ATIVO = 'A'
        and gc.GRCU_IN_DELETE_LOGICO != 'S'
        and gc.GRCU_DT_INATIVACAO is null
inner join GRUPO_CUSTO_AREA_ORCAMENTARIA gcao on GCAO.GRCU_CD_GRUPO_CUSTO = GC.GRCU_CD_GRUPO_CUSTO and gcao.GCAO_DT_FIM is null
inner join recurso r on R.RECU_CD_RECURSO = P.RECU_CD_RECURSO
where (p.PESS_DT_RESCISAO is null or trunc(p.PESS_DT_RESCISAO) > trunc(sysdate))
group by gcao.AROR_CD_AREA_ORCAMENTARIA, gc.GRCU_CD_GRUPO_CUSTO, r.RECU_CD_RECURSO, p.PESS_DT_RESCISAO, b.BILL_SG_BILLABILITY

);

--changeset rbastos:226 dbms:oracle
--comment: Ajuste View VW_PMS_AVAILABILITY_ALLOC
CREATE OR REPLACE VIEW VW_PMS_AVAILABILITY_ALLOC AS
SELECT vw.recu_cd_recurso,
       vw.recu_cd_mnemonico,
       vw.alpe_dt_alocacao_periodo,
       'N' in_minor_param,
       vw.clie_nm_cliente,
       vw.clie_cd_cliente,
       sum(vw.pr_alocacao_periodo_mes) * 100 pr_alocacao_periodo_mes
  FROM (
       -- select do mapa-alocacao
        (SELECT (r.recu_cd_mnemonico || '-' ||
                EXTRACT(Month FROM ap.alpe_dt_alocacao_periodo) || '-' ||
                EXTRACT(Year FROM ap.alpe_dt_alocacao_periodo) || '-' ||
                cli.clie_cd_cliente) recu_cd_recurso,
                r.recu_cd_mnemonico,
                ap.alpe_dt_alocacao_periodo,
                cli.clie_nm_cliente,
                cli.clie_cd_cliente,
                SUM(ap.alpe_pr_alocacao_periodo) pr_alocacao_periodo_mes
           FROM pms20.mapa_alocacao    ma,
                pms20.alocacao         a,
                pms20.alocacao_periodo ap,
                pms20.recurso          r,
                pms20.contrato_pratica cprat,
                pms20.msa              msa,
                pms20.cliente          cli
          WHERE ma.maal_in_versao = 'PB'
            AND ma.maal_cd_mapa_alocacao = a.maal_cd_mapa_alocacao
            AND a.aloc_cd_alocacao = ap.aloc_cd_alocacao
            AND a.recu_cd_recurso = r.recu_cd_recurso
            AND r.recu_in_tipo_recurso = 'PE'

            AND cprat.copr_cd_contrato_pratica = ma.copr_cd_contrato_pratica
            AND cprat.msaa_cd_msa = msa.msaa_cd_msa
            AND msa.clie_cd_cliente = cli.clie_cd_cliente
          group by ap.alpe_dt_alocacao_periodo,
                   r.recu_cd_mnemonico,
                   r.recu_cd_recurso,
                   cli.clie_nm_cliente,
                   cli.clie_cd_cliente)
       -- end do select do mapa-alocacao
        UNION ALL
       -- select da alocacao-overhead
        (SELECT (p.pess_cd_login || '-' ||
                EXTRACT(Month FROM ap_oh.alpo_dt_aloc_periodo_oh) || '-' ||
                EXTRACT(Year FROM ap_oh.alpo_dt_aloc_periodo_oh) || '-' || 0) recu_cd_recurso,
                p.pess_cd_login,
                ap_oh.alpo_dt_aloc_periodo_oh,
                'Overhead',
                0,
                SUM(ap_oh.alpo_pr_aloc_periodo_oh)
           FROM pms20.alocacao_overhead   a_oh,
                pms20.alocacao_periodo_oh ap_oh,
                pms20.pessoa              p,
                pms20.recurso             r
          WHERE a_oh.alov_cd_alocacao_overhead =
                ap_oh.alov_cd_alocacao_overhead
            AND p.pess_cd_pessoa = a_oh.pess_cd_pessoa
            AND r.recu_cd_recurso = p.recu_cd_recurso
          GROUP BY ap_oh.alpo_dt_aloc_periodo_oh,
                   p.pess_cd_login,
                   r.recu_cd_recurso)
       -- fim do select da alocacao-overhead
       ) vw,
       pms20.pessoa_billability pb,
       pms20.pessoa p
 WHERE p.pess_cd_pessoa = pb.pess_cd_pessoa
   AND p.pess_cd_login = recu_cd_mnemonico
   -- apenas os bilables
   AND ((TRUNC(sysdate, 'MM') between pb.pebi_dt_inicio and pb.pebi_dt_fim) or 
                         (pb.pebi_dt_fim is null and pb.pebi_dt_inicio <= TRUNC(sysdate, 'MM')))
   AND pb.bill_cd_billability = 2
   -- apenas mes corrente para frente
   AND to_char(alpe_dt_alocacao_periodo, 'YYYY:MM') >=
       to_char(TRUNC(SYSDATE), 'YYYY:MM')
   -- comentado o codigo abaixo, pois fizemos um refactor no funcionamento do minor
   /*AND to_char(alpe_dt_alocacao_periodo, 'YYYY:MM') <=
       to_char(ADD_MONTHS(TRUNC(SYSDATE), 3), 'YYYY:MM')*/

 GROUP BY vw.recu_cd_recurso,
          vw.recu_cd_mnemonico,
          vw.alpe_dt_alocacao_periodo,
          vw.clie_nm_cliente,
          vw.clie_cd_cliente
 ORDER BY vw.recu_cd_mnemonico, vw.alpe_dt_alocacao_periodo;

--changeset rbastos:227 dbms:oracle
--comment: Ajuste View VW_PMS_CAPACITY_IDLE_RESOURCE
CREATE OR REPLACE VIEW VW_PMS_CAPACITY_IDLE_RESOURCE AS
select s.cd_person,
       s.login as login,
       s.login_pdm,
       s.in_billable,
       s.nm_tower,
       s.dt_month,
       s.cd_company,
       s.dt_admission,
       s.dt_rescission,
       s.pr_available as available,
       s.pr_allocated as allocated,
       s.pr_overhead as overhead,
       (s.pr_available - s.pr_allocated - s.pr_overhead) as to_be_allocated,
       s.in_active
  from (select pess.pess_cd_pessoa cd_person,
               pess.pess_cd_login login,
               hmgr.login_pdm,
               b.bill_sg_billability in_billable,
               gc.grcu_nm_grupo_custo nm_tower,
               mes.dt_mes dt_month,
               ptc.empr_cd_empresa cd_company,
               pess.pess_dt_admissao dt_admission,
               pess.pess_dt_rescisao dt_rescission,
               case
                 when (trunc(pess.pess_dt_admissao, 'mm') = mes.dt_mes and
                      trunc(pess.pess_dt_rescisao, 'mm') = mes.dt_mes) then
                  round((pess.pess_dt_rescisao - pess.pess_dt_admissao + 1) /
                        EXTRACT(DAY FROM LAST_DAY(mes.dt_mes)) *
                        nvl(ptc.petc_pr_alocavel, pess.pess_pr_alocavel),
                        2)
                 when trunc(pess.pess_dt_admissao, 'mm') = mes.dt_mes then
                  round((last_day(mes.dt_mes) - pess.pess_dt_admissao + 1) /
                        EXTRACT(DAY FROM LAST_DAY(mes.dt_mes)) *
                        nvl(ptc.petc_pr_alocavel, pess.pess_pr_alocavel),
                        2)
                 when trunc(pess.pess_dt_rescisao, 'mm') = mes.dt_mes then
                  round((pess.pess_dt_rescisao - mes.dt_mes + 1) /
                        EXTRACT(DAY FROM LAST_DAY(mes.dt_mes)) *
                        nvl(ptc.petc_pr_alocavel, pess.pess_pr_alocavel))
                 else
                  round(nvl(ptc.petc_pr_alocavel, pess.pess_pr_alocavel), 2)
               end pr_available,
               nvl(praloc.pr_alocado, 0) pr_allocated,
               nvl(proh.pr_overhead, 0) pr_overhead,
               case
                 when pess.pess_dt_rescisao < sysdate then
                  0
                 else
                  1
               end in_active
          from pms20.pessoa pess
         inner join (select /*+no_index(alpe)*/ distinct alpe.alpe_dt_alocacao_periodo dt_mes,
                                    count(*) nr_reg
                      from pms20.alocacao_periodo alpe
                     where alpe.alpe_dt_alocacao_periodo between
                           add_months(sysdate, -24) and
                           add_months(sysdate, 12)
                     group by alpe.alpe_dt_alocacao_periodo) mes on mes.dt_mes between trunc(pess.pess_dt_admissao, 'mm') and trunc(nvl(pess.pess_dt_rescisao,
                                                                              sysdate + 31),
                                                                          'mm')

          left join (select emp.empl_cd_login login,
                           hm.hima_dt_begin,
                           hm.hima_dt_end,
                           mgr.empl_cd_login login_pdm
                      from hrs.history_manager hm,
                           hrs.employee        emp,
                           hrs.employee        mgr
                     where hm.empl_cd_employee = emp.empl_cd_employee
                       and hm.empl_cd_employee_mgr = mgr.empl_cd_employee) hmgr on pess.pess_cd_login =
                                                                                   hmgr.login
                                                                               and mes.dt_mes between
                                                                                   hmgr.hima_dt_begin and
                                                                                   nvl(hmgr.hima_dt_end,
                                                                                       sysdate + 31)

          left join pms20.pessoa_tipo_contrato ptc on pess.pess_cd_pessoa =
                                                      ptc.pess_cd_pessoa
                                                  and mes.dt_mes between
                                                      ptc.petc_dt_inicio and
                                                      nvl(ptc.petc_dt_fim,
                                                          sysdate + 31)

          left join pms20.pessoa_grupo_custo pgc on pess.pess_cd_pessoa =
                                                    pgc.pess_cd_pessoa
                                                and mes.dt_mes between
                                                    pgc.pegc_dt_inicio and
                                                    nvl(pgc.pegc_dt_fim,
                                                        sysdate + 31)
                                                        
          left join pms20.pessoa_billability pb on pess.pess_cd_pessoa =
                                                    pb.pess_cd_pessoa
                                                and ((TRUNC(mes.dt_mes) between pb.pebi_dt_inicio and pb.pebi_dt_fim) or 
                         (pb.pebi_dt_fim is null and pb.pebi_dt_inicio <= TRUNC(mes.dt_mes))) 

         inner join pms20.billability b on pb.bill_cd_billability = b.bill_cd_billability                                                                                                

         inner join pms20.grupo_custo gc on pgc.grcu_cd_grupo_custo =
                                            gc.grcu_cd_grupo_custo

          left join (SELECT r.recu_cd_mnemonico login,
                           ap.alpe_dt_alocacao_periodo dt_mes,
                           SUM(ap.alpe_pr_alocacao_periodo) pr_alocado
                      FROM pms20.mapa_alocacao    ma,
                           pms20.alocacao         a,
                           pms20.alocacao_periodo ap,
                           pms20.recurso          r
                     WHERE ma.maal_in_versao = 'PB'
                       AND ma.maal_cd_mapa_alocacao =
                           a.maal_cd_mapa_alocacao
                       AND a.aloc_cd_alocacao = ap.aloc_cd_alocacao
                       AND a.recu_cd_recurso = r.recu_cd_recurso
                       AND r.recu_in_tipo_recurso = 'PE'
                     group by r.recu_cd_mnemonico,
                              ap.alpe_dt_alocacao_periodo) praloc on pess.pess_cd_login =
                                                                     praloc.login
                                                                 and mes.dt_mes =
                                                                     praloc.dt_mes

          left join (SELECT p.pess_cd_login login,
                           ap_oh.alpo_dt_aloc_periodo_oh dt_mes,
                           SUM(ap_oh.alpo_pr_aloc_periodo_oh) pr_overhead
                      FROM pms20.alocacao_overhead   a_oh,
                           pms20.alocacao_periodo_oh ap_oh,
                           pms20.pessoa              p,
                           pms20.recurso             r
                     WHERE a_oh.alov_cd_alocacao_overhead =
                           ap_oh.alov_cd_alocacao_overhead
                       AND p.pess_cd_pessoa = a_oh.pess_cd_pessoa
                       AND r.recu_cd_recurso = p.recu_cd_recurso
                     GROUP BY p.pess_cd_login,
                              ap_oh.alpo_dt_aloc_periodo_oh,
                              r.recu_cd_recurso) proh on pess.pess_cd_login = proh.login and mes.dt_mes = proh.dt_mes) s
 where s.dt_month = trunc(sysdate, 'mm')
   and s.in_billable = 'BL'
   and s.cd_company = 1
   and s.in_active = 1
   and s.login not in ('contarelli')
   and (s.pr_available - s.pr_allocated - s.pr_overhead) >= 0.7;

--changeset rbastos:228 dbms:oracle
--comment: Ajuste View VW_PMS_FORECAST_PP_ALLOCATION
CREATE OR REPLACE VIEW VW_PMS_FORECAST_PP_ALLOCATION
(code, cd_grupo_custo, nm_grupo_custo, cd_recurso, cd_mnemonico_recurso, sg_tipo_recurso, sg_tipo_visao, cd_alocacao, dt_rescisao, cd_alocacao_periodo, dt_alocacao_periodo, pr_alocacao_periodo, in_super_allocated, in_rescindido_fpp, nm_cidade_base, tx_comentario, has_other_allocation, in_billable_month, in_billable_padrao, cd_gc_padrao, nm_gc_padrao, pr_aloc_forecast, has_vacation)
AS
SELECT rownum code, a.GRCU_CD_GRUPO_CUSTO,a.GRCU_NM_GRUPO_CUSTO,a.RECU_CD_RECURSO,a.RECU_CD_MNEMONICO,a.RECU_IN_TIPO_RECURSO,a.GDTV_SG_TIPO_VISAO,a.ALGC_CD_ALOCACAO,a.ALGC_DT_RESCISAO,a.AGCP_CD_ALOCACAO_PERIODO,a.AGCP_DT_ALOCACAO_PERIODO,a.AGCP_PR_ALOCACAO_PERIODO,a.IN_SUPER_ALLOCATED,a.ALGC_IN_RESCINDIDO_FPP, a.CIBA_NM_CIDADE_BASE, a.algc_tx_comment, a.has_other_allocation, a.BILL_SG_BILLABILITY, a.BL_PADRAO, a.CD_GC_PADRAO, a.NM_GC_PADRAO, a.PR_ALOC_FORECAST, a.has_vacation from (

    select GC.GRCU_CD_GRUPO_CUSTO, GC.GRCU_NM_GRUPO_CUSTO, R.RECU_CD_RECURSO, R.RECU_CD_MNEMONICO, r.recu_in_tipo_recurso, GDTV.GDTV_SG_TIPO_VISAO,
            agc.algc_cd_alocacao, AGC.ALGC_DT_RESCISAO, AGCP.AGCP_CD_ALOCACAO_PERIODO, AGCP.AGCP_DT_ALOCACAO_PERIODO, AGCP.AGCP_PR_ALOCACAO_PERIODO,
            decode(a.recu_cd_recurso, null, 'N', 'Y') in_super_allocated, AGC.ALGC_IN_RESCINDIDO_FPP, cb.ciba_nm_cidade_base, agc.algc_tx_comment,
            case when count(other.code) > 0 then 'Y' else 'N' end has_other_allocation, b_mes.bill_sg_billability, 'NB' as BL_PADRAO,
            gc_padrao.GRCU_CD_GRUPO_CUSTO as CD_GC_PADRAO, gc_padrao.GRCU_NM_GRUPO_CUSTO as NM_GC_PADRAO, fc.PR_ALOCACAO_FORECAST as PR_ALOC_FORECAST,
            case when count(ferias.nr_hist_ferias) > 0 then 'Y' else 'N' end has_vacation
        FROM alocacao_grupo_custo AGC
            INNER JOIN GRUPO_CUSTO GC ON GC.GRCU_CD_GRUPO_CUSTO = AGC.GRCU_CD_GRUPO_CUSTO
            inner join grupo_custo_area_orcamentaria gcao on gc.grcu_cd_grupo_custo = gcao.grcu_cd_grupo_custo
                                                         and SYSDATE BETWEEN gcao.GCAO_DT_INICIO AND NVL(gcao.GCAO_DT_FIM, SYSDATE + 1)
                                                         and gcao.aror_cd_area_orcamentaria = 34
            INNER JOIN ALOCACAO_GRUPO_CUSTO_PERIODO AGCP ON AGCP.ALGC_CD_ALOCACAO = AGC.ALGC_CD_ALOCACAO
            INNER JOIN RECURSO R ON R.RECU_CD_RECURSO = AGC.RECU_CD_RECURSO
            inner join pessoa p on r.recu_cd_recurso = p.recu_cd_recurso
            inner join pessoa_grupo_custo pgc on p.pess_cd_pessoa = pgc.pess_cd_pessoa
                                             and SYSDATE BETWEEN pgc.PEGC_DT_INICIO AND NVL(pgc.pegc_dt_fim, SYSDATE + 1)
            inner join pessoa_billability pb on pgc.pess_cd_pessoa = pb.pess_cd_pessoa and pb.bill_cd_billability = 1 and ((TRUNC(sysdate, 'MM') between pb.pebi_dt_inicio and pb.pebi_dt_fim) or 
                         (pb.pebi_dt_fim is null and pb.pebi_dt_inicio <= TRUNC(sysdate, 'MM')))
            inner join GRUPO_CUSTO gc_padrao ON pgc.GRCU_CD_GRUPO_CUSTO = gc_padrao.GRCU_CD_GRUPO_CUSTO
            inner join pessoa_cidade_base pcb on p.pess_cd_pessoa = pcb.pess_cd_pessoa
                                             and SYSDATE BETWEEN pcb.PECB_DT_INICIO AND NVL(pcb.PECB_DT_FIM, SYSDATE + 1)
            inner join cidade_base cb on pcb.ciba_cd_cidade_base = cb.ciba_cd_cidade_base
            INNER JOIN GRUPO_DESPESA_TIPO_VISAO GDTV ON GDTV.GDTV_CD_TIPO_VISAO = AGC.GDTV_CD_TIPO_VISAO
            inner join PESSOA_BILLABILITY pb_mes ON p.PESS_CD_PESSOA = pb_mes.PESS_CD_PESSOA
                                                 AND ((TRUNC(AGCP.AGCP_DT_ALOCACAO_PERIODO, 'MM') between pb_mes.pebi_dt_inicio and pb_mes.pebi_dt_fim) or 
                         (pb_mes.pebi_dt_fim is null and pb_mes.pebi_dt_inicio <= TRUNC(AGCP.AGCP_DT_ALOCACAO_PERIODO, 'MM')))
            inner join BILLABILITY b_mes on pb_mes.bill_cd_billability = b_mes.bill_cd_billability             
            left join
            (
                select agc.recu_cd_recurso, agc.gdtv_cd_tipo_visao, agcp.agcp_dt_alocacao_periodo
                    from alocacao_grupo_custo agc
                        inner join alocacao_grupo_custo_periodo agcp on agc.algc_cd_alocacao = agcp.algc_cd_alocacao
                        inner join recurso rec on agc.recu_cd_recurso = rec.recu_cd_recurso
                                              and rec.recu_in_tipo_recurso <> 'PA'
                    group by agc.recu_cd_recurso, agc.gdtv_cd_tipo_visao, agcp.agcp_dt_alocacao_periodo
                    having sum(agcp.agcp_pr_alocacao_periodo) > 1
            ) a on a.recu_cd_recurso = agc.recu_cd_recurso
               and a.gdtv_cd_tipo_visao = agc.gdtv_cd_tipo_visao
               and a.agcp_dt_alocacao_periodo = agcp.agcp_dt_alocacao_periodo
            left join vw_pms_forecast_pp_other_aloc other on other.cd_recurso = agc.recu_cd_recurso
                                                         and other.cd_alocacao <> agc.algc_cd_alocacao
                                                         and other.dt_alocacao_periodo = agcp.agcp_dt_alocacao_periodo
                                                         and nvl(other.pr_alocacao_periodo,0) > 0
            left join (
                select al.RECU_CD_RECURSO as CD_RECURSO, alp.ALPE_DT_ALOCACAO_PERIODO as DT_ALOC_FORECAST, MAX(alp.ALPE_PR_ALOCACAO_PERIODO) as PR_ALOCACAO_FORECAST
                    from ALOCACAO al
                        inner join ALOCACAO_PERIODO alp ON alp.ALOC_CD_ALOCACAO = al.ALOC_CD_ALOCACAO
                    where alp.ALPE_PR_ALOCACAO_PERIODO > 0
                    group by al.RECU_CD_RECURSO, alp.ALPE_DT_ALOCACAO_PERIODO) fc ON fc.CD_RECURSO = agc.RECU_CD_RECURSO
                                                                                 AND fc.DT_ALOC_FORECAST = agcp.AGCP_DT_ALOCACAO_PERIODO

            left join vw_hrs_ferias ferias on ferias.login = p.pess_cd_login and to_date(to_char(agcp.agcp_dt_alocacao_periodo,'MM/RRRR'),'MM/RRRR') between to_date(to_char(dt_saida,'MM/RRRR'),'MM/RRRR') and to_date(to_char(dt_retorno,'MM/RRRR'),'MM/RRRR')

            group by GC.GRCU_CD_GRUPO_CUSTO, GC.GRCU_NM_GRUPO_CUSTO, R.RECU_CD_RECURSO, R.RECU_CD_MNEMONICO, r.recu_in_tipo_recurso, GDTV.GDTV_SG_TIPO_VISAO,
                agc.algc_cd_alocacao, AGC.ALGC_DT_RESCISAO, AGCP.AGCP_CD_ALOCACAO_PERIODO, AGCP.AGCP_DT_ALOCACAO_PERIODO, AGCP.AGCP_PR_ALOCACAO_PERIODO,
                decode(a.recu_cd_recurso, null, 'N', 'Y'), AGC.ALGC_IN_RESCINDIDO_FPP, cb.ciba_nm_cidade_base, agc.algc_tx_comment, b_mes.bill_sg_billability,
                gc_padrao.GRCU_CD_GRUPO_CUSTO, gc_padrao.GRCU_NM_GRUPO_CUSTO, fc.PR_ALOCACAO_FORECAST

union all

    select GC.GRCU_CD_GRUPO_CUSTO, GC.GRCU_NM_GRUPO_CUSTO, R.RECU_CD_RECURSO, R.RECU_CD_MNEMONICO, r.recu_in_tipo_recurso, GDTV.GDTV_SG_TIPO_VISAO,
            agc.algc_cd_alocacao, AGC.ALGC_DT_RESCISAO, AGCP.AGCP_CD_ALOCACAO_PERIODO, AGCP.AGCP_DT_ALOCACAO_PERIODO, AGCP.AGCP_PR_ALOCACAO_PERIODO,
            decode(a.recu_cd_recurso, null, 'N', 'Y') in_super_allocated, AGC.ALGC_IN_RESCINDIDO_FPP, cb.ciba_nm_cidade_base, agc.algc_tx_comment,
            case when count(other.code) > 0 then 'Y' else 'N' end has_other_allocation, b.bill_sg_billability, blnb.BILL_SG_BILLABILITY as BL_PADRAO,
            gc_padrao.GRCU_CD_GRUPO_CUSTO as CD_GC_PADRAO, gc_padrao.GRCU_NM_GRUPO_CUSTO as NM_GC_PADRAO, fc.PR_ALOCACAO_FORECAST as PR_ALOC_FORECAST,
            case when count(ferias.nr_hist_ferias) > 0 then 'Y' else 'N' end has_vacation
        FROM alocacao_grupo_custo AGC
            INNER JOIN GRUPO_CUSTO GC ON GC.GRCU_CD_GRUPO_CUSTO = AGC.GRCU_CD_GRUPO_CUSTO
            inner join grupo_custo_area_orcamentaria gcao on gc.grcu_cd_grupo_custo = gcao.grcu_cd_grupo_custo
                                                         and SYSDATE BETWEEN gcao.GCAO_DT_INICIO AND NVL(gcao.GCAO_DT_FIM, SYSDATE + 1)
                                                         and gcao.aror_cd_area_orcamentaria != 34
            INNER JOIN ALOCACAO_GRUPO_CUSTO_PERIODO AGCP ON AGCP.ALGC_CD_ALOCACAO = AGC.ALGC_CD_ALOCACAO
            INNER JOIN RECURSO R ON R.RECU_CD_RECURSO = AGC.RECU_CD_RECURSO
            inner join pessoa p on r.recu_cd_recurso = p.recu_cd_recurso
            inner join pessoa_cidade_base pcb on p.pess_cd_pessoa = pcb.pess_cd_pessoa
                                             and SYSDATE BETWEEN pcb.PECB_DT_INICIO AND NVL(pcb.pecb_dt_fim, SYSDATE + 1)
            inner join cidade_base cb on pcb.ciba_cd_cidade_base = cb.ciba_cd_cidade_base
            INNER JOIN GRUPO_DESPESA_TIPO_VISAO GDTV ON GDTV.GDTV_CD_TIPO_VISAO = AGC.GDTV_CD_TIPO_VISAO
            inner join PESSOA_BILLABILITY pb on p.PESS_CD_PESSOA = pb.PESS_CD_PESSOA  
                                                 AND ((TRUNC(AGCP.AGCP_DT_ALOCACAO_PERIODO, 'MM') between pb.pebi_dt_inicio and pb.pebi_dt_fim) or 
                         (pb.pebi_dt_fim is null and pb.pebi_dt_inicio <= TRUNC(AGCP.AGCP_DT_ALOCACAO_PERIODO, 'MM')))                                    
            inner join BILLABILITY b on pb.bill_cd_billability = b.bill_cd_billability             
            left join
            (
                select agc.recu_cd_recurso, agcp.agcp_dt_alocacao_periodo
                    from alocacao_grupo_custo agc
                        inner join alocacao_grupo_custo_periodo agcp on agc.algc_cd_alocacao = agcp.algc_cd_alocacao
                        inner join recurso rec on agc.recu_cd_recurso = rec.recu_cd_recurso
                                              and rec.recu_in_tipo_recurso <> 'PA'
                    group by agc.recu_cd_recurso, agcp.agcp_dt_alocacao_periodo
                    having sum(agcp.agcp_pr_alocacao_periodo) > 1
            ) a on a.recu_cd_recurso = agc.recu_cd_recurso and a.agcp_dt_alocacao_periodo = agcp.agcp_dt_alocacao_periodo
            left join vw_pms_forecast_pp_other_aloc other on other.cd_recurso = agc.recu_cd_recurso and other.cd_alocacao <> agc.algc_cd_alocacao
                                                         and other.dt_alocacao_periodo = agcp.agcp_dt_alocacao_periodo
                                                         and nvl(other.pr_alocacao_periodo,0) > 0
            left join (
                select pgc2.PESS_CD_PESSOA, b.BILL_SG_BILLABILITY, pgc2.GRCU_CD_GRUPO_CUSTO
                    from PESSOA_GRUPO_CUSTO pgc2
                    inner join PESSOA_BILLABILITY pb on pb.pess_cd_pessoa = pgc2.pess_cd_pessoa and ((TRUNC(sysdate, 'MM') between pb.pebi_dt_inicio and pb.pebi_dt_fim) or 
                         (pb.pebi_dt_fim is null and pb.pebi_dt_inicio <= TRUNC(sysdate, 'MM')))
                    inner join BILLABILITY b on pb.bill_cd_billability = b.bill_cd_billability
                    where SYSDATE BETWEEN pgc2.PEGC_DT_INICIO AND NVL(pgc2.PEGC_DT_FIM, SYSDATE + 1)
            ) blnb ON blnb.PESS_CD_PESSOA = p.PESS_CD_PESSOA
            left join GRUPO_CUSTO gc_padrao ON blnb.GRCU_CD_GRUPO_CUSTO = gc_padrao.GRCU_CD_GRUPO_CUSTO
            left join (
                select al.RECU_CD_RECURSO as CD_RECURSO, alp.ALPE_DT_ALOCACAO_PERIODO as DT_ALOC_FORECAST, MAX(alp.ALPE_PR_ALOCACAO_PERIODO) as PR_ALOCACAO_FORECAST
                    from ALOCACAO al
                        inner join ALOCACAO_PERIODO alp ON alp.ALOC_CD_ALOCACAO = al.ALOC_CD_ALOCACAO
                    where alp.ALPE_PR_ALOCACAO_PERIODO > 0
                    group by al.RECU_CD_RECURSO, alp.ALPE_DT_ALOCACAO_PERIODO) fc ON fc.CD_RECURSO = agc.RECU_CD_RECURSO
                                                                                 AND fc.DT_ALOC_FORECAST = agcp.AGCP_DT_ALOCACAO_PERIODO

            left join vw_hrs_ferias ferias on ferias.login = p.pess_cd_login and to_date(to_char(agcp.agcp_dt_alocacao_periodo,'MM/RRRR'),'MM/RRRR') between to_date(to_char(dt_saida,'MM/RRRR'),'MM/RRRR') and to_date(to_char(dt_retorno,'MM/RRRR'),'MM/RRRR')

            group by GC.GRCU_CD_GRUPO_CUSTO, GC.GRCU_NM_GRUPO_CUSTO, R.RECU_CD_RECURSO, R.RECU_CD_MNEMONICO, r.recu_in_tipo_recurso, GDTV.GDTV_SG_TIPO_VISAO,
                agc.algc_cd_alocacao, AGC.ALGC_DT_RESCISAO, AGCP.AGCP_CD_ALOCACAO_PERIODO, AGCP.AGCP_DT_ALOCACAO_PERIODO, AGCP.AGCP_PR_ALOCACAO_PERIODO,
                decode(a.recu_cd_recurso, null, 'N', 'Y'), AGC.ALGC_IN_RESCINDIDO_FPP, cb.ciba_nm_cidade_base, agc.algc_tx_comment, b.bill_sg_billability,
                blnb.BILL_SG_BILLABILITY, gc_padrao.GRCU_CD_GRUPO_CUSTO, gc_padrao.GRCU_NM_GRUPO_CUSTO, fc.PR_ALOCACAO_FORECAST

union all

    select GC.GRCU_CD_GRUPO_CUSTO, GC.GRCU_NM_GRUPO_CUSTO, R.RECU_CD_RECURSO, R.RECU_CD_MNEMONICO, r.recu_in_tipo_recurso, GDTV.GDTV_SG_TIPO_VISAO,
            agc.algc_cd_alocacao, AGC.ALGC_DT_RESCISAO, AGCP.AGCP_CD_ALOCACAO_PERIODO, AGCP.AGCP_DT_ALOCACAO_PERIODO, AGCP.AGCP_PR_ALOCACAO_PERIODO,
            'N' in_super_allocated, AGC.ALGC_IN_RESCINDIDO_FPP, CB.CIBA_NM_CIDADE_BASE, agc.algc_tx_comment, 'N' has_other_allocation,
            null, null as BL_PADRAO, null as CD_GC_PADRAO, null as NM_GC_PADRAO, null as PR_ALOC_FORECAST, 'N' has_vacation
        FROM alocacao_grupo_custo AGC
            INNER JOIN GRUPO_CUSTO GC ON GC.GRCU_CD_GRUPO_CUSTO = AGC.GRCU_CD_GRUPO_CUSTO
            INNER JOIN ALOCACAO_GRUPO_CUSTO_PERIODO AGCP ON AGCP.ALGC_CD_ALOCACAO = AGC.ALGC_CD_ALOCACAO
            INNER JOIN RECURSO R ON R.RECU_CD_RECURSO = AGC.RECU_CD_RECURSO and r.recu_in_tipo_recurso = 'PA'
            INNER JOIN GRUPO_DESPESA_TIPO_VISAO GDTV ON GDTV.GDTV_CD_TIPO_VISAO = AGC.GDTV_CD_TIPO_VISAO
            INNER JOIN CIDADE_BASE CB ON AGC.CIBA_CD_CIDADE_BASE = CB.CIBA_CD_CIDADE_BASE
        where agc.algc_cd_alocacao in ( select distinct agcpu.algc_cd_alocacao
                from alocacao_grupo_custo agcu
                    inner join alocacao_grupo_custo_periodo agcpu on agcpu.algc_cd_alocacao = agcu.algc_cd_alocacao
                    inner join recurso r1 on r1.recu_cd_recurso = agcu.recu_cd_recurso and r1.recu_in_tipo_recurso = 'PA'
                    where agcpu.agcp_pr_alocacao_periodo = 1 )
) a;

--changeset rbastos:229 dbms:oracle
--comment: Ajuste View VW_PMS_FORECAST_PP_OTHER_ALOC
CREATE OR REPLACE VIEW VW_PMS_FORECAST_PP_OTHER_ALOC
(code, cd_grupo_custo, nm_grupo_custo, cd_recurso, cd_mnemonico_recurso, sg_tipo_visao, cd_alocacao, cd_alocacao_periodo, dt_alocacao_periodo, pr_alocacao_periodo)
AS
SELECT rownum code, a."GRCU_CD_GRUPO_CUSTO",a."GRCU_NM_GRUPO_CUSTO",a."RECU_CD_RECURSO",a."RECU_CD_MNEMONICO",a."GDTV_SG_TIPO_VISAO",a."ALGC_CD_ALOCACAO",a."AGCP_CD_ALOCACAO_PERIODO",a."AGCP_DT_ALOCACAO_PERIODO", nvl(a."AGCP_PR_ALOCACAO_PERIODO",0) "AGCP_PR_ALOCACAO_PERIODO" from (

select GC.GRCU_CD_GRUPO_CUSTO, GC.GRCU_NM_GRUPO_CUSTO, R.RECU_CD_RECURSO, R.RECU_CD_MNEMONICO, GDTV.GDTV_SG_TIPO_VISAO,
  agc.algc_cd_alocacao, AGCP.AGCP_CD_ALOCACAO_PERIODO, AGCP.AGCP_DT_ALOCACAO_PERIODO, AGCP.AGCP_PR_ALOCACAO_PERIODO
FROM alocacao_grupo_custo AGC
  INNER JOIN GRUPO_CUSTO GC ON GC.GRCU_CD_GRUPO_CUSTO = AGC.GRCU_CD_GRUPO_CUSTO
  inner join grupo_custo_area_orcamentaria gcao on gc.grcu_cd_grupo_custo = gcao.grcu_cd_grupo_custo and gcao.gcao_dt_fim is null and gcao.aror_cd_area_orcamentaria = 34
  INNER JOIN ALOCACAO_GRUPO_CUSTO_PERIODO AGCP ON AGCP.ALGC_CD_ALOCACAO = AGC.ALGC_CD_ALOCACAO
  INNER JOIN RECURSO R ON R.RECU_CD_RECURSO = AGC.RECU_CD_RECURSO
  inner join pessoa p on r.recu_cd_recurso = p.recu_cd_recurso
  inner join pessoa_billability pb on p.pess_cd_pessoa = pb.pess_cd_pessoa and ((TRUNC(sysdate, 'MM') between pb.pebi_dt_inicio and pb.pebi_dt_fim) or 
                         (pb.pebi_dt_fim is null and pb.pebi_dt_inicio <= TRUNC(sysdate, 'MM'))) and pb.bill_cd_billability = 1
  INNER JOIN GRUPO_DESPESA_TIPO_VISAO GDTV ON GDTV.GDTV_CD_TIPO_VISAO = AGC.GDTV_CD_TIPO_VISAO

union all

select GC.GRCU_CD_GRUPO_CUSTO, GC.GRCU_NM_GRUPO_CUSTO, R.RECU_CD_RECURSO, R.RECU_CD_MNEMONICO, GDTV.GDTV_SG_TIPO_VISAO,
            agc.algc_cd_alocacao, AGCP.AGCP_CD_ALOCACAO_PERIODO, AGCP.AGCP_DT_ALOCACAO_PERIODO, AGCP.AGCP_PR_ALOCACAO_PERIODO
        FROM alocacao_grupo_custo AGC
            INNER JOIN GRUPO_CUSTO GC ON GC.GRCU_CD_GRUPO_CUSTO = AGC.GRCU_CD_GRUPO_CUSTO
            inner join grupo_custo_area_orcamentaria gcao on gc.grcu_cd_grupo_custo = gcao.grcu_cd_grupo_custo and gcao.gcao_dt_fim is null and gcao.aror_cd_area_orcamentaria != 34
            INNER JOIN ALOCACAO_GRUPO_CUSTO_PERIODO AGCP ON AGCP.ALGC_CD_ALOCACAO = AGC.ALGC_CD_ALOCACAO
            INNER JOIN RECURSO R ON R.RECU_CD_RECURSO = AGC.RECU_CD_RECURSO
            inner join pessoa p on r.recu_cd_recurso = p.recu_cd_recurso
            INNER JOIN GRUPO_DESPESA_TIPO_VISAO GDTV ON GDTV.GDTV_CD_TIPO_VISAO = AGC.GDTV_CD_TIPO_VISAO
) a;

--changeset rbastos:230 dbms:oracle
--comment: Ajuste View VW_PMS_PESSOA_ALOCACAO_CUSTO
CREATE OR REPLACE VIEW VW_PMS_PESSOA_ALOCACAO_CUSTO AS
SELECT rownum as cd_key, p.PESS_CD_LOGIN, pessoa_gc.GRCU_CD_GRUPO_CUSTO, pessoa_gc.GRCU_NM_GRUPO_CUSTO, mp.COD_IN_PROJETO CONV_CD_PROJETO_MEGA, mp.CIT_ST_EXTENSO,  pessoa_cp.COPR_CD_CONTRATO_PRATICA, pessoa_cp.COPR_NM_CONTRATO_PRATICA, pessoa_cp.GRCU_CD_GRUPO_CUSTO_CP, pessoa_cp.GRCU_NM_GRUPO_CUSTO_CP, pessoa_gc.EMPR_CD_ERP_CODIGO, pessoa_gc.EMPR_NM_EMPRESA, pessoa_cp.ALPE_PR_ALOCACAO_PERIODO, pessoa_gc.BILLABLE, decode(p.PESS_DT_RESCISAO, NULL, 'Y', 'N') AS pessoa_ativa

FROM PESSOA p

LEFT JOIN (
            SELECT pgc.PESS_CD_PESSOA, gc.GRCU_NM_GRUPO_CUSTO, gc.ERP_CD_CCUSTO GRCU_CD_GRUPO_CUSTO, e.EMPR_CD_ERP_CODIGO, e.EMPR_NM_EMPRESA, c.CONV_CD_PROJETO_MEGA proj_mega_gc, decode(b.BILL_SG_BILLABILITY, 'BL', 'Billable', 'NB', 'Fixed') billable
            FROM GRUPO_CUSTO gc
            INNER JOIN PESSOA_GRUPO_CUSTO pgc ON gc.GRCU_CD_GRUPO_CUSTO = pgc.GRCU_CD_GRUPO_CUSTO AND pgc.PEGC_DT_FIM IS NULL
            INNER JOIN EMPRESA e ON gc.EMPR_CD_EMPRESA = e.EMPR_CD_EMPRESA
            INNER JOIN CONVERGENCIA c ON gc.GRCU_CD_GRUPO_CUSTO = c.GRCU_CD_GRUPO_CUSTO AND c.COPR_CD_CONTRATO_PRATICA IS NULL
            INNER JOIN PESSOA_BILLABILITY pb ON pgc.PESS_CD_PESSOA = pb.PESS_CD_PESSOA AND ((TRUNC(sysdate, 'MM') between pb.pebi_dt_inicio and pb.pebi_dt_fim) or 
                         (pb.pebi_dt_fim is null and pb.pebi_dt_inicio <= TRUNC(sysdate, 'MM')))
            INNER JOIN BILLABILITY b on pb.BILL_CD_BILLABILITY = b.BILL_CD_BILLABILITY
          ) pessoa_gc ON p.PESS_CD_PESSOA = pessoa_gc.PESS_CD_PESSOA


LEFT JOIN (
            SELECT DISTINCT a.RECU_CD_RECURSO, cp.COPR_CD_CONTRATO_PRATICA, cp.COPR_NM_CONTRATO_PRATICA, gc.erp_cd_ccusto GRCU_CD_GRUPO_CUSTO_CP, gc.GRCU_NM_GRUPO_CUSTO GRCU_NM_GRUPO_CUSTO_CP, c.CONV_CD_PROJETO_MEGA proj_mega_cp, ap.ALPE_PR_ALOCACAO_PERIODO
            FROM ALOCACAO a
            INNER JOIN ALOCACAO_PERIODO ap ON a.ALOC_CD_ALOCACAO = ap.ALOC_CD_ALOCACAO AND ap.ALPE_DT_ALOCACAO_PERIODO = trunc(SYSDATE, 'MONTH')
            INNER JOIN MAPA_ALOCACAO ma ON a.MAAL_CD_MAPA_ALOCACAO = ma.MAAL_CD_MAPA_ALOCACAO
            INNER JOIN CONTRATO_PRATICA cp ON ma.COPR_CD_CONTRATO_PRATICA = cp.COPR_CD_CONTRATO_PRATICA
            INNER JOIN CONVERGENCIA c ON cp.COPR_CD_CONTRATO_PRATICA = c.COPR_CD_CONTRATO_PRATICA
            INNER JOIN GRUPO_CUSTO gc ON c.GRCU_CD_GRUPO_CUSTO = gc.GRCU_CD_GRUPO_CUSTO
          ) pessoa_cp ON p.RECU_CD_RECURSO = pessoa_cp.RECU_CD_RECURSO

INNER JOIN VW_MEGA_PROJETO mp ON nvl(pessoa_cp.proj_mega_cp, pessoa_gc.PROJ_MEGA_GC) = mp.COD_IN_PROJETO

--WHERE p.PESS_DT_RESCISAO IS NULL

--changeset rbastos:231 dbms:oracle
--comment: Ajuste View VW_PMS_PESSOA_GRUPO_CUSTO
CREATE OR REPLACE VIEW VW_PMS_PESSOA_GRUPO_CUSTO AS
SELECT pegc_cd_pessoa_grupo_custo,
       pgc.pess_cd_pessoa,
       p.pess_cd_login,
       pgc.grcu_cd_grupo_custo,
       gc.grcu_nm_grupo_custo,
       bill_sg_billability,
       pegc_dt_inicio,
       pegc_dt_fim,
       pegc_dt_criacao
FROM pessoa_grupo_custo pgc,
     pessoa p,
     grupo_custo gc,
     pessoa_billability pb,
     billability b
WHERE trunc(SYSDATE) BETWEEN pgc.pegc_dt_inicio AND CASE WHEN (pgc.pegc_dt_fim IS NULL)
                                                              THEN to_date('31/12/9999','dd/mm/yyyy')
                                                              ELSE pgc.pegc_dt_fim
                                                    END
  AND pgc.pess_cd_pessoa = p.pess_cd_pessoa
  AND pgc.grcu_cd_grupo_custo = gc.grcu_cd_grupo_custo
  AND p.pess_cd_pessoa = pb.pess_cd_pessoa
  AND ((TRUNC(sysdate, 'MM') between pb.pebi_dt_inicio and pb.pebi_dt_fim) or 
                         (pb.pebi_dt_fim is null and pb.pebi_dt_inicio <= TRUNC(sysdate, 'MM')))
  AND pb.bill_cd_billability = b.bill_cd_billability;

--changeset rbastos:232 dbms:oracle
--comment: Ajuste View VW_PMS_PESS_GRUP_CUST_EMPRESA 
CREATE OR REPLACE VIEW VW_PMS_PESS_GRUP_CUST_EMPRESA AS
SELECT pegc_cd_pessoa_grupo_custo,
       pgc.pess_cd_pessoa,
       p.pess_cd_login,
       pgc.grcu_cd_grupo_custo,
       gc.grcu_nm_grupo_custo,
       bill_sg_billability,
       pegc_dt_inicio,
       pegc_dt_fim,
       pegc_dt_criacao,
       case
         when p.pess_dt_rescisao < trunc(sysdate) then
          'I'
         else
          'A'
       end pess_in_status,
       ebi.empl_cd_login_pdm,
       emp.empr_cd_empresa,
       emp.empr_nm_empresa,
       ebi.empl_dt_admission_r pess_dt_admissao,
       p.pess_dt_rescisao,
       ptc.petc_pr_alocavel,
       p.pess_pr_alocavel
  FROM pessoa_grupo_custo                            pgc,
       pessoa                                        p,
       grupo_custo                                   gc,
       hrs.vw_hrs_employee_basic_info                ebi,
       pessoa_tipo_contrato                          ptc,
       empresa                                       emp,
       pessoa_billability                            pb,
       billability                                   b
 WHERE trunc(SYSDATE) BETWEEN pgc.pegc_dt_inicio AND
       nvl(pgc.pegc_dt_fim, sysdate + 60)
   AND pgc.pess_cd_pessoa = p.pess_cd_pessoa
   AND pgc.grcu_cd_grupo_custo = gc.grcu_cd_grupo_custo
   AND p.pess_cd_login = ebi.empl_cd_login(+)
   AND p.pess_cd_pessoa = ptc.pess_cd_pessoa(+)
   AND ptc.empr_cd_empresa = emp.empr_cd_empresa(+)
   AND trunc(SYSDATE) BETWEEN ptc.petc_dt_inicio(+) AND
       nvl(ptc.petc_dt_fim(+), SYSDATE + 60)
   AND pb.pess_cd_pessoa = p.pess_cd_pessoa
   AND ((TRUNC(sysdate, 'MM') between pb.pebi_dt_inicio and pb.pebi_dt_fim) or 
                         (pb.pebi_dt_fim is null and pb.pebi_dt_inicio <= TRUNC(sysdate, 'MM')))
   AND b.bill_cd_billability = pb.bill_cd_billability;

--changeset francis:233 dbms:oracle
--comment: Remoção do campo PEGC_IN_STATUS
ALTER TABLE PESSOA_GRUPO_CUSTO DROP COLUMN PEGC_IN_STATUS;