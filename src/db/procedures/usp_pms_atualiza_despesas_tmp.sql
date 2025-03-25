create or replace procedure usp_pms_atualiza_despesas_tmp is

cursor c_despesas is
select cc.org_in_codigo
       ,cc.fil_in_codigo
       ,e.empr_cd_empresa
       ,e.empr_nm_empresa
       ,cc.lan_dt_lancamento dere_dt_lancamento
       ,p.copr_cd_contrato_pratica
       ,p.grcu_cd_grupo_custo
       ,cc.pla_st_extenso
       ,cc.pla_st_descricao
       ,cc.orc_pla_st_extenso
       ,cc.orc_pla_st_descricao
       ,decode(cc.orc_pla_st_extenso
               ,'1201008',1 /*travel*/
               ,'1202002',2 /*contractor*/,null) cd_tipo_despesa
       ,cc.ccusto
       ,cc.projeto
       ,cc.complemento dere_tx_complemento
       ,(decode(cc.tipo_lanc,'D',-1,1)*cc.valor_moeda) valor
       ,cc.cod_moeda
       ,decode(cc.cod_moeda,'USD', 2, 'R$',1, null) moed_cd_moeda
from mega_adm.con_vw_razao_ccpro cc
     ,pms.empresa e
     ,vw_chp_projetos p
where cc.mes_lancamento >= '01-jan-2010'
and cc.projeto = p.cd_mega
and cc.org_in_codigo = p.cd_empresa
and cc.orc_pla_st_extenso IN ('1201008', '1202002')
-- não trazer a cesta de benefícios da contabilidade pois já é considerado no TCE
and (cc.tpd_st_codigo <> 'RELBENEF' or cc.tpd_st_codigo is null)
and ( cc.complemento not like '%Reclassificação - Benefícios Colaboradores%' or cc.complemento is null )
and cc.fil_in_codigo = e.empr_cd_erp_filial (+)
and ( cc.agn_in_codigo != 2421 or cc.agn_in_codigo is null )
and ( cc.complemento not like 'Agent: Ci_T Software%' or complemento is null )
union all
select cc2.org_in_codigo
       ,cc2.fil_in_codigo
       ,e2.empr_cd_empresa
       ,e2.empr_nm_empresa
       ,cc2.lan_dt_lancamento dere_dt_lancamento
       ,p2.copr_cd_contrato_pratica
       ,p2.grcu_cd_grupo_custo
       ,cc2.pla_st_extenso
       ,cc2.pla_st_descricao
       ,cc2.orc_pla_st_extenso
       ,cc2.orc_pla_st_descricao
       ,4 cd_tipo_despesa  -- Other Exp
       ,cc2.ccusto
       ,cc2.projeto
       ,cc2.complemento dere_tx_complemento
       ,(decode(cc2.tipo_lanc,'D',-1,1)*cc2.valor_moeda) valor
       ,cc2.cod_moeda
       ,decode(cc2.cod_moeda,'USD', 2, 'R$',1, null) moed_cd_moeda
from mega_adm.con_vw_razao_ccpro cc2
      ,pms.empresa e2
      ,vw_chp_projetos p2
where cc2.mes_lancamento >= '01-jan-2010'
and cc2.projeto = p2.cd_mega
and cc2.org_in_codigo = p2.cd_empresa
and cc2.fil_in_codigo = e2.empr_cd_erp_filial (+)
-- não trazer a cesta de benefícios da contabilidade pois já é considerado no TCE
and (cc2.tpd_st_codigo <> 'RELBENEF' or cc2.tpd_st_codigo is null)
and ( cc2.complemento not like '%Reclassificação - Benefícios Colaboradores%' or cc2.complemento is null )
and cc2.orc_pla_st_extenso not in
('1202002' -- Assessorias Comerciais ==>> Contractors
,'1201001' -- Folha
,'1103001' -- Impostos
,'1201002' -- Depreciação
,'1201008' -- Viagens
,'1101001' -- Receitas
,'1201009' -- Reembolso de Despesas
,'1201017' -- Bonus
)
and cc2.pla_st_extenso like '3%' -- trazer somente despesas da DRE
-- Incluído em 09/08, para que não considere mais os valores de Perdas Incobráveis
and cc2.pla_st_extenso not in ( '3153060' )
and cc2.cus_st_descricao not like '%Escrit%' -- não trazer custos de infra
and cc2.cus_st_descricao not like 'Infra%'-- não trazer custos de infra
and ( cc2.agn_in_codigo != 2421 or cc2.agn_in_codigo is null )
and ( cc2.complemento not like 'Agent: Ci_T Software%' or complemento is null )
;
  -- Informacões para LOG
  LOG_DATA              date := trunc(sysdate);
  LOG_DT_CARGA_PROCESSO date;
  NOME_CARGA            varchar2(50) :='DESPESAS';
  BLOCO_UPD             varchar2(50) :='';
  DESCRICAO             varchar2(200) :='';
  STATUS                varchar2(30) :='';
  SQLERRO               varchar2(200) :='';
  NOME_PROC             varchar2(200):='usp_pms_atualiza_despesas';
  --
  CRLF      VARCHAR2(2) := CHR(13) || CHR(10);
  ASSUNTO   VARCHAR2(200) := '';
  MENSAGEM  VARCHAR(2000) := '=== Início da Carga '||to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' )||' ===';
  CONTADOR1 INTEGER;
  CONTADOR2 INTEGER;
BEGIN
   /**
    * Procedure para atulizar a tabela de despesas vindas da contabilidade
    * Autor: Katsumi Arackawa Jr - 21/Jun/2010
    * Revisão: Ahumada - 05/05/2011
    **/
  MENSAGEM := MENSAGEM || CRLF || ':: Procedure pms.usp_pms_atualiza_despesas ::'; 
  SEND_MAIL('lnoboru@cit.com.br', '[PMS][DESPESAS]Start Proc' , MENSAGEM);
  SEND_MAIL('alexandrel@cit.com.br', '[PMS][DESPESAS] Start Proc' , MENSAGEM);
  
 SEND_MAIL('tspadari@ciandt.com', '[PMS][DESPESAS] Start Proc' , MENSAGEM);
 SEND_MAIL('llino@ciandt.com', '[PMS][DESPESAS] Start Proc' , MENSAGEM);
 SEND_MAIL('andreiap@ciandt.com', '[PMS][DESPESAS] Start Proc' , MENSAGEM);
   
  
  begin
     MENSAGEM := MENSAGEM || CRLF || CRLF ||'Insert: tmp_despesa_realizada - despesas vindas da contabilidade ';   
     BLOCO_UPD := 'tmp_despesa_realizada (flag 1 de 1 p1)';
     SELECT COUNT(*) INTO CONTADOR1 FROM PMS.tmp_despesa_realizada;
      -- Inserir LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);         
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;        
      --  
     execute immediate 'truncate table tmp_despesa_realizada';
     --
     for r_desp in c_despesas loop
        insert into tmp_despesa_realizada
         (dere_cd_despesa_realizada,
          dere_dt_lancamento,
          tide_cd_tipo_despesa,
          copr_cd_contrato_pratica,
          grcu_cd_grupo_custo,
          dere_tx_complemento,
          dere_tx_erp_pla_st_desc,
          dere_tx_erp_orc_pla_st_desc,
          dere_cd_erp_orc_pla_st_ext,
          dere_vl_valor,
          moed_cd_moeda,
          empr_cd_empresa,
          projeto,
          org_in_codigo)
      values
         (0,
          r_desp.dere_dt_lancamento,
          r_desp.cd_tipo_despesa,
          r_desp.copr_cd_contrato_pratica,
          r_desp.grcu_cd_grupo_custo,
          r_desp.dere_tx_complemento,
          r_desp.pla_st_descricao,
          r_desp.orc_pla_st_descricao,
          r_desp.orc_pla_st_extenso,
          r_desp.valor,
          r_desp.moed_cd_moeda,
          r_desp.empr_cd_empresa,
          r_desp.projeto,
          r_desp.org_in_codigo);
     end loop;
     commit;
     SELECT COUNT(*) INTO CONTADOR2 FROM PMS.tmp_despesa_realizada;
         -- Info LOG
         DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2); 
         STATUS:='OK';
         --    
         MENSAGEM := MENSAGEM || CRLF || CRLF || '[OK] PMS.tmp_despesa_realizada' || CRLF ||
                  'Linhas antes do Refresh: ' || TO_CHAR(CONTADOR1) || CRLF ||
                  'Linhas após Refresh: ' || TO_CHAR(CONTADOR2) || CRLF ||
                  'Diferença: ' || TO_CHAR(CONTADOR2 - CONTADOR1) || CRLF ||
                  'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
    EXCEPTION
      WHEN OTHERS then
        STATUS:='ERRO';
        SQLERRO:=sqlerrm;        
        MENSAGEM := MENSAGEM || CRLF || CRLF || '[ERRO] ##### PMS.tmp_despesa_realizada  ##### - ' || SQLERRM || CRLF || 'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
        ASSUNTO := '[ERRO]';
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;  
     --
   SQLERRO :='';
  begin
     mensagem := mensagem || crlf || crlf ||
               'Insert: despesa_realizada - atualiza os registros contrato_pratica validos';
     BLOCO_UPD := 'despesa_realizada (flag 1 de 1 p2)';
     SELECT COUNT(*) INTO CONTADOR1 FROM PMS.despesa_realizada;
       -- Inserir LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);         
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;        
      --  
     execute immediate 'truncate table despesa_realizada';
     -- atualiza os registros contrato_pratica validos
     insert all into despesa_realizada
      (dere_cd_despesa_realizada,
       dere_dt_lancamento,
       tide_cd_tipo_despesa,
       copr_cd_contrato_pratica,
       grcu_cd_grupo_custo,
       dere_tx_complemento,
       dere_tx_erp_pla_st_desc,
       dere_tx_erp_orc_pla_st_desc,
       dere_cd_erp_orc_pla_st_ext,
       dere_vl_valor,
       dere_in_debito_credito,
       moed_cd_moeda,
       empr_cd_empresa,
       projeto,
       org_in_codigo)
     values
      (sq_dere_cd_despesa_realizada.nextval,
       dere_dt_lancamento,
       tide_cd_tipo_despesa,
       copr_cd_contrato_pratica,
       grcu_cd_grupo_custo,
       dere_tx_complemento,
       dere_tx_erp_pla_st_desc,
       dere_tx_erp_orc_pla_st_desc,
       dere_cd_erp_orc_pla_st_ext,
       dere_vl_valor,
       dere_in_debito_credito,
       moed_cd_moeda,
       empr_cd_empresa,
       projeto, 
       org_in_codigo)
      select dere_dt_lancamento,
             tide_cd_tipo_despesa,
             tdr.copr_cd_contrato_pratica,
             grcu_cd_grupo_custo,
             dere_tx_complemento,
             dere_tx_erp_pla_st_desc,
             dere_tx_erp_orc_pla_st_desc,
             dere_cd_erp_orc_pla_st_ext,
             dere_vl_valor,
             dere_in_debito_credito,
             moed_cd_moeda,
             empr_cd_empresa,
             projeto,
             org_in_codigo
      from   tmp_despesa_realizada tdr,
             contrato_pratica      cp
      where  tdr.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica;
     commit;
     insert all
     into despesa_realizada
        (
            dere_cd_despesa_realizada, 
            dere_dt_lancamento, 
            tide_cd_tipo_despesa, 
            copr_cd_contrato_pratica, 
            grcu_cd_grupo_custo, 
            dere_tx_complemento, 
            dere_tx_erp_pla_st_desc, 
            dere_tx_erp_orc_pla_st_desc, 
            dere_cd_erp_orc_pla_st_ext, 
            dere_vl_valor, 
            dere_in_debito_credito, 
            moed_cd_moeda, 
            empr_cd_empresa,
            projeto,
            org_in_codigo 
        )
     values 
        (
            sq_dere_cd_despesa_realizada.nextval,
            dere_dt_lancamento, 
            tide_cd_tipo_despesa, 
            copr_cd_contrato_pratica, 
            grcu_cd_grupo_custo, 
            dere_tx_complemento, 
            dere_tx_erp_pla_st_desc, 
            dere_tx_erp_orc_pla_st_desc, 
            dere_cd_erp_orc_pla_st_ext, 
            dere_vl_valor, 
            dere_in_debito_credito, 
            moed_cd_moeda, 
            empr_cd_empresa,
            projeto,
            org_in_codigo
        )
     select dere_dt_lancamento, 
          tide_cd_tipo_despesa, 
          copr_cd_contrato_pratica, 
          grcu_cd_grupo_custo, 
          dere_tx_complemento, 
          dere_tx_erp_pla_st_desc, 
          dere_tx_erp_orc_pla_st_desc, 
          dere_cd_erp_orc_pla_st_ext, 
          dere_vl_valor, 
          dere_in_debito_credito, 
          moed_cd_moeda, 
          empr_cd_empresa,
          projeto,
          org_in_codigo 
     from tmp_despesa_realizada tdr
     where tdr.copr_cd_contrato_pratica is null;
    --
    commit;
    --
    delete from despesa_realizada
    where dere_tx_complemento like '%Deposito ref. CONTRATO DE CONCESSÃO FINEP REF 1704/07%';
    --
    commit;
    --
    SELECT COUNT(*) INTO CONTADOR2 FROM PMS.despesa_realizada;
         -- Info LOG
         DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2); 
         STATUS:='OK';
         --
         MENSAGEM := MENSAGEM || CRLF || CRLF || '[OK] PMS.despesa_realizada' || CRLF ||
                  'Linhas antes do Refresh: ' || TO_CHAR(CONTADOR1) || CRLF ||
                  'Linhas após Refresh: ' || TO_CHAR(CONTADOR2) || CRLF ||
                  'Diferença: ' || TO_CHAR(CONTADOR2 - CONTADOR1) || CRLF ||
                  'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
    EXCEPTION
      WHEN OTHERS then
        STATUS:='ERRO';
        SQLERRO:=sqlerrm;        
        MENSAGEM := MENSAGEM || CRLF || CRLF || '[ERRO] ##### PMS.despesa_realizada  ##### - ' || SQLERRM || CRLF || 'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
        ASSUNTO := '[ERRO]';
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;  
     --
/*
   mensagem := mensagem || crlf || crlf || '[OK] PMS.despesa_realizada' || crlf ||
               'Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
exception
   when others then
      mensagem := mensagem || crlf || crlf ||
                  '[ERRO] ##### PMS.despesa_realizada  ##### - ' || sqlerrm || crlf ||
                  'Data Execução: ' ||
                  to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
      assunto  := '[ERRO]';
end;*/
   -- atualiza os registros contrato_pratica nulos
   
/*select username into usertemp
from V$SESSION;
*/

  MENSAGEM := MENSAGEM || CRLF || CRLF || '=== Fim da Carga '||to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' )
||' ===';
     mensagem := mensagem || crlf || 'select * from pms.TB_BI_PMS_LOG_CARGAS where NOME_CARGA = ''DESPESAS'' order by 2 desc';
  ASSUNTO := ASSUNTO || ' [PMS][DESPESAS] Resultado da Carga';
  SEND_MAIL('lnoboru@cit.com.br', ASSUNTO, MENSAGEM);
  SEND_MAIL('alexandrel@cit.com.br', ASSUNTO, MENSAGEM);

SEND_MAIL('tspadari@ciandt.com', ASSUNTO, MENSAGEM);
SEND_MAIL('llino@ciandt.com', ASSUNTO, MENSAGEM);
 SEND_MAIL('andreiap@ciandt.com', ASSUNTO, MENSAGEM);
   
   
end usp_pms_atualiza_despesas_tmp;