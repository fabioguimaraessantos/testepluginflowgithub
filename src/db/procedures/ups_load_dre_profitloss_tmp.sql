create or replace procedure ups_load_dre_profitloss_tmp(flag number) as
  -- Informacões para LOG
  LOG_DATA              date := trunc(sysdate);
  LOG_DT_CARGA_PROCESSO date;
  NOME_CARGA            varchar2(50) :='PROFITLOSS';
  BLOCO_UPD             varchar2(50) :='';
  DESCRICAO             varchar2(200) :='';
  STATUS                varchar2(30) :='';
  SQLERRO               varchar2(200) :='';
  NOME_PROC             varchar2(200):='ups_load_dre_profitloss';
   /*Procedure que faz a carga dos Workbooks do ProfitLoss
   Entrada:
     Tipo da Carga: 1 - Tabela: tb_bi_pms_dre_contratos - WB: PMS-ProfitLoss / PMS-ProfitLoss-Login / PMS-LOB-ProfitLoss / PMS-UMKT-ProfitLoss
     Tipo da Carga: 2 - Tabela: tb_bi_pms_dre_servicos - WB: PMS-ProfitLoss / PMS-ProfitLoss-Login / PMS-LOB-ProfitLoss / PMS-UMKT-ProfitLoss
     Tipo da Carga: 3 - Tabela: tb_bi_pms_dre_umkts - WB: PMS-UMKT-ProfitLoss
     Tipo da Carga: 4 - Tabela: tb_bi_pms_dre_praticas - WB: PMS-LOB-ProfitLoss
     Tipo da Carga: 5 - Tabela: tb_bi_pms_consolida_dre - WB: PMS-LOB-ProfitLoss     
   */  
   crlf      varchar2(2) := chr(13) || chr(10);
   assunto   varchar2(200) := '';
   mensagem  varchar(4000) := '=== Início da Carga '||to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' )
||'  ===';
   contador1 integer;
   contador2 integer;

begin
   send_mail('lahumada@cit.com.br', '[PMS][PROFITLOSS] Processo iniciado', mensagem);
   send_mail('alexandrel@cit.com.br', '[PMS][PROFITLOSS] Processo iniciado', mensagem);
   mensagem := mensagem || crlf || ':: PROCEDURE DA CARGA: pms.ups_load_dre_profitloss ::';
   if (flag = 1 or flag = 99)
   then
     mensagem := mensagem || crlf || crlf ||'Tipo da Carga: 1 - Tabela: tb_bi_pms_dre_contratos - WB: PMS-ProfitLoss / PMS-ProfitLoss-Login / PMS-LOB-ProfitLoss / PMS-UMKT-ProfitLoss';
     LOG_DT_CARGA_PROCESSO := sysdate;
     BLOCO_UPD := 'tb_bi_pms_dre_contratos (flag 1 de 5)';
      begin
         select count(*)
         into   contador1 --contador para Log
         from   pms.tb_bi_pms_dre_contratos;
         -- Inserir LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);         
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;        
         --
         execute immediate 'truncate table tb_bi_pms_dre_contratos';
         --
         insert into tb_bi_pms_dre_contratos
            select *
            from   vw_bi_pms_dre_contrato;
         commit;
         --
         select count(*)
         into   contador2 --contador para Log         
         from   pms.tb_bi_pms_dre_contratos;
         -- Info LOG
         DESCRICAO:= 'FIM - Linhas: '||to_char(contador2); 
         STATUS:='OK';
         --
         mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.tb_bi_pms_dre_contratos' || crlf ||
                     'Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     'Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     'Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' )
;
      exception
         when others then
            STATUS:='ERRO';
            SQLERRO:=sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.tb_bi_pms_dre_contratos  ##### - ' ||
                        sqlerrm || crlf || 'Data Execução: ' || sysdate;
            assunto  := '[ERRO]';
      end;
      --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;  
   end if;
   --grant select on tb_bi_pms_dre_contratos to pms_role;
   --grant select on tb_bi_pms_dre_contratos to pms_map_role;
   ------------------------------------------------------------------------------------------
   --limpar para proximo registro
   SQLERRO:='';
   if (flag = 2 or flag = 99)
   then
      mensagem := mensagem || crlf || crlf ||
                  'Tipo da Carga: 2 - Tabela: tb_bi_pms_dre_servicos - WB: PMS-ProfitLoss / PMS-ProfitLoss-Login / PMS-LOB-ProfitLoss / PMS-UMKT-ProfitLoss';
     LOG_DT_CARGA_PROCESSO := sysdate;
     BLOCO_UPD := 'tb_bi_pms_dre_servicos (flag 2 de 5)';
      begin
         select count(*)
         into   contador1 --contador para Log
         from   pms.tb_bi_pms_dre_servicos;
         -- Inserir LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);         
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;        
         --
         execute immediate 'truncate table tb_bi_pms_dre_servicos';

         insert into tb_bi_pms_dre_servicos
            select *
            from   vw_bi_pms_dre_servicos;

         select count(*)
         into   contador2 --contador para Log
         from   pms.tb_bi_pms_dre_servicos;
         -- Info para LOG
         DESCRICAO:= 'FIM - Linhas: '||to_char(contador2); 
         STATUS:='OK';
         --
         mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.tb_bi_pms_dre_servicos' || crlf ||
                     'Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     'Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     'Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
      exception
         when others then
            STATUS:='ERRO';
            SQLERRO:=sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.tb_bi_pms_dre_servicos  ##### - ' ||
                        sqlerrm || crlf || 'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
            assunto  := '[ERRO]';
      end;
      commit;
       --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit; 
   end if;
   --grant select on tb_bi_pms_dre_servicos to pms_role;
   --grant select on tb_bi_pms_dre_servicos to pms_map_role;
   -----------------------------------------------------------------------------------------
   --limpar para proximo registro
   SQLERRO:='';
   if (flag = 3 or flag = 99)
   then
      mensagem := mensagem || crlf || crlf ||
                  'Tipo da Carga: 3 - Tabela: tb_bi_pms_dre_umkts - WB: PMS-UMKT-ProfitLoss';
      LOG_DT_CARGA_PROCESSO := sysdate;
      BLOCO_UPD := 'tb_bi_pms_dre_umkts (flag 3 de 5)';
      begin
         select count(*)
         into   contador1 --contador para Log
         from   pms.tb_bi_pms_dre_umkts;
         -- Inserir LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);         
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;        
         --
         execute immediate 'truncate table tb_bi_pms_dre_umkts';

         insert into tb_bi_pms_dre_umkts
            select *
            from   vw_bi_pms_dre_umkts;
         commit;
         --
         select count(*)
         into   contador2 --contador para Log
         from   pms.tb_bi_pms_dre_umkts;
         -- Info para LOG
         DESCRICAO:= 'FIM - Linhas: '||to_char(contador2); 
         STATUS:='OK';
         --
         mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.tb_bi_pms_dre_umkts' || crlf ||
                     'Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     'Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     'Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' )
;
      exception
         when others then
            STATUS:='ERRO';
            SQLERRO:=sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.tb_bi_pms_dre_umkts  ##### - ' ||
                        sqlerrm || crlf || 'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' )
;
            assunto  := '[ERRO]';
      end;
      commit;
      --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit; 
   end if;
   --grant select on tb_bi_pms_dre_umkts to pms_dre_umkt_role;
   ------------------------------------------------------------------------------------------
   --limpar para proximo registro
   SQLERRO:='';
   if (flag = 4 or flag = 99)
   then
      mensagem := mensagem || crlf || crlf ||
                  'Tipo da Carga: 4 - Tabela: tb_bi_pms_dre_praticas - WB: PMS-LOB-ProfitLoss';
      LOG_DT_CARGA_PROCESSO := sysdate;
      BLOCO_UPD := 'tb_bi_pms_dre_praticas (flag 4 de 5)';
      begin
         select count(*)
         into   contador1 --contador para Log
         from   pms.tb_bi_pms_dre_praticas;
         -- Inserir LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);         
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;        
         execute immediate 'truncate table tb_bi_pms_dre_praticas';

         insert into tb_bi_pms_dre_praticas
            select *
            from   vw_bi_pms_dre_praticas;
         commit; 
         select count(*)
         into   contador2 --contador para Log
         from   pms.tb_bi_pms_dre_praticas;
         -- Info para LOG
         DESCRICAO:= 'FIM - Linhas: '||to_char(contador2); 
         STATUS:='OK';
         --
         mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.tb_bi_pms_dre_praticas' || crlf ||
                     'Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     'Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     'Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
      exception
         when others then
            STATUS:='ERRO';
            SQLERRO:=sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.tb_bi_pms_dre_praticas  ##### - ' ||
                        sqlerrm || crlf || 'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
            assunto  := '[ERRO]';
      end;
         --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
   end if;
   ------------------------------
    --limpar para proximo registro
    SQLERRO:='';
      if (flag = 5 or flag = 99)
   then
      mensagem := mensagem || crlf || crlf ||
                  'Tipo da Carga: 5 - Tabela: tb_bi_consolida_dres - WB: PMS-ProfitLoss';
      LOG_DT_CARGA_PROCESSO := sysdate;
      BLOCO_UPD := 'tb_bi_consolida_dres (flag 5 de 5)';
      begin
         select count(*)
         into   contador1 --contador para Log
         from   pms.tb_bi_pms_consolida_dres;
         -- Inserir LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);         
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;        
         ---
        -- execute immediate 'truncate table tb_bi_pms_consolida_dres';
         --
--         insert into tb_bi_pms_consolida_dres
--            select *
--            from   vw_bi_pms_consolida_dres;
--       commit;
         --
         select count(*)
         into   contador2 --contador para Log
         from   pms.tb_bi_pms_consolida_dres;
         -- Info para LOG
         DESCRICAO:= 'FIM - Linhas: '||to_char(contador2); 
         STATUS:='OK';
         --
         mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.tb_bi_pms_consolida_dres' || crlf ||
                     'Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     'Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     'Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
      exception      
         when others then
            STATUS:='ERRO';
            SQLERRO:=sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.tb_bi_pms_consolida_dres  ##### - ' ||
                        sqlerrm || crlf || 'Data Execução: ' || sysdate;
            assunto  := '[ERRO]';
      end;
         --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
   end if;
   
   mensagem := mensagem || crlf || crlf || '=== Fim da Carga '||to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' )||' ===';
   mensagem := mensagem || crlf || 'select * from pms.TB_BI_PMS_LOG_CARGAS where NOME_CARGA = ''PROFITLOSS'' order by 1 desc';
   assunto  := assunto ||' [PMS][PROFITLOSS] Resultado Carga';
   send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
end ups_load_dre_profitloss_tmp;