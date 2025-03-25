create or replace procedure ups_load_inform_dre_umkts_tmp( flag number ) as
  -- Informacões para LOG
  LOG_DATA              date := trunc(sysdate);
  LOG_DT_CARGA_PROCESSO date;
  NOME_CARGA            varchar2(50) :='UMKT';
  BLOCO_UPD             varchar2(50) :='';
  DESCRICAO             varchar2(200) :='';
  STATUS                varchar2(30) :='';
  SQLERRO               varchar2(200) :='';
  NOME_PROC             varchar2(200):='ups_load_inform_dre_umkts';
  --
  CRLF      VARCHAR2(2) := CHR(13) || CHR(10);
  ASSUNTO   VARCHAR2(200) := '';
  MENSAGEM  VARCHAR(2000) := '=== Início da Carga '||to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' )||' ===';
  CONTADOR1 INTEGER;
  CONTADOR2 INTEGER;
BEGIN
  --------------------------------------------------------
  -- Atualização da view de Technical Sales
  --------------------------------------------------------
  MENSAGEM := MENSAGEM || CRLF || ':: Procedure: pms.ups_load_inform_dre_umkts ::';
  SEND_MAIL('lahumada@cit.com.br', '[PMS][UMKT] Start Proc' , MENSAGEM);
  SEND_MAIL('alexandrel@cit.com.br', '[PMS][UMKT] Start Proc' , MENSAGEM);
  IF (FLAG = 1 OR FLAG = 99) THEN
    MENSAGEM := MENSAGEM || CRLF || CRLF ||'Tipo da Carga: 1 - View de Technical Sales ';
    -- info para log
    LOG_DT_CARGA_PROCESSO := sysdate;
    BLOCO_UPD := 'VW_BI_PMS_TECH_SALES_MAT (flag 1 de 6) [Refresh]';
    --
    BEGIN
      SELECT COUNT(*) INTO CONTADOR1 FROM PMS.VW_BI_PMS_TECH_SALES_MAT;
      -- Inserir LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);         
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;        
      --      
   --      DBMS_MVIEW.REFRESH('"PMS"."VW_BI_PMS_TECH_SALES_MAT"');
      --
      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.VW_BI_PMS_TECH_SALES_MAT;
         -- Info LOG
         DESCRICAO:= 'FIM - Linhas: '||to_char(contador2); 
         STATUS:='OK';
         --
         MENSAGEM := MENSAGEM || CRLF || CRLF || '[OK] PMS.VW_BI_PMS_TECH_SALES_MAT' || CRLF ||
                  'Linhas antes do Refresh: ' || TO_CHAR(CONTADOR1) || CRLF ||
                  'Linhas após Refresh: ' || TO_CHAR(CONTADOR2) || CRLF ||
                  'Diferença: ' || TO_CHAR(CONTADOR2 - CONTADOR1) || CRLF ||
                  'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
    EXCEPTION
      WHEN OTHERS then
        STATUS:='ERRO';
        SQLERRO:=sqlerrm;        
        MENSAGEM := MENSAGEM || CRLF || CRLF || '[ERRO] ##### PMS.VW_BI_PMS_TECH_SALES_MAT  ##### - ' || SQLERRM || CRLF || 'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
        ASSUNTO := '[ERRO]';
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;  
     --
  END IF;
  --
  -------------------------------------------------------------------------
  -- Atualização da view de custos fixos das consultorias
  -------------------------------------------------------------------------
  --limpar para proximo registro
   SQLERRO:='';
  IF (FLAG = 2 OR FLAG = 99) THEN
    MENSAGEM := MENSAGEM || CRLF || CRLF ||'Tipo da Carga: 2 - View de Custos Fixos das Consultorias';
     -- Infor LOG
       LOG_DT_CARGA_PROCESSO := sysdate;
       BLOCO_UPD := 'VW_BI_PMS_CUSTFIX_UMKT_DRE (flag 2 de 6)[REFRESH]';
     --
     BEGIN
      SELECT COUNT(*) INTO CONTADOR1 FROM PMS.VW_BI_PMS_CUSTFIX_UMKT_DRE;
      -- Inserir LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);         
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;        
      --
                   DBMS_MVIEW.REFRESH('"PMS"."VW_BI_PMS_CUSTFIX_UMKT_DRE"');
      --                   
      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.VW_BI_PMS_CUSTFIX_UMKT_DRE;
      -- Info para LOG
         DESCRICAO:= 'FIM - Linhas: '||to_char(contador2); 
         STATUS:='OK';
      --
         MENSAGEM := MENSAGEM || CRLF || CRLF || '[OK] PMS.VW_BI_PMS_CUSTFIX_UMKT_DRE' || CRLF ||
                  'Linhas antes do Refresh: ' || TO_CHAR(CONTADOR1) || CRLF ||
                  'Linhas após Refresh: ' || TO_CHAR(CONTADOR2) || CRLF ||
                  'Diferença: ' || TO_CHAR(CONTADOR2 - CONTADOR1) || CRLF ||
                  'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
    EXCEPTION
      WHEN OTHERS then
        STATUS:='ERRO';
        SQLERRO:=sqlerrm;      
        MENSAGEM := MENSAGEM || CRLF || CRLF ||'[ERRO] ##### PMS.VW_BI_PMS_CUSTFIX_UMKT_DRE  ##### - ' || SQLERRM || CRLF || 'Data Execução: ' || TRUNC(SYSDATE);
        ASSUNTO := '[ERRO]';
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
  END IF;
  --
  --------------------------------------------------------------------------
  -- Atualização da view de custos fixos dos recursos com intervalo entre admissão e 1a alocação
  --------------------------------------------------------------------------
   --limpar para proximo registro
   SQLERRO:='';  
  IF (FLAG = 3 OR FLAG = 99) THEN
    MENSAGEM := MENSAGEM || CRLF || CRLF ||'Tipo da Carga: 3 - View de Custos Fixos dos recursos com intervalo entre admissão e 1a alocação';
    -- Info LOG
      LOG_DT_CARGA_PROCESSO := sysdate;
      BLOCO_UPD := 'VW_BI_PMS_CUSTFIX_SAC_DRE_MAT (flag 3 de 6)[REFRESH]';
    --
    begin     
      SELECT COUNT(*) INTO CONTADOR1 FROM PMS.VW_BI_PMS_CUSTFIX_SAC_DRE_MAT;
      -- Inserir LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);         
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;        
      --      
                   DBMS_MVIEW.REFRESH('"PMS"."VW_BI_PMS_CUSTFIX_SAC_DRE_MAT"');
      --
      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.VW_BI_PMS_CUSTFIX_SAC_DRE_MAT;
      -- Info para LOG
         DESCRICAO:= 'FIM - Linhas: '||to_char(contador2); 
         STATUS:='OK';
      --      
         MENSAGEM := MENSAGEM || CRLF || CRLF || '[OK] PMS.VW_BI_PMS_CUSTFIX_SAC_DRE_MAT' || CRLF ||
                  'Linhas antes do Refresh: ' || TO_CHAR(CONTADOR1) || CRLF ||
                  'Linhas após Refresh: ' || TO_CHAR(CONTADOR2) || CRLF ||
                  'Diferença: ' || TO_CHAR(CONTADOR2 - CONTADOR1) || CRLF ||
                  'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
    EXCEPTION
      WHEN OTHERS then
        STATUS:='ERRO';
        SQLERRO:=sqlerrm;
        MENSAGEM := MENSAGEM || CRLF || CRLF || '[ERRO] ##### PMS.VW_BI_PMS_CUSTFIX_SAC_DRE_MAT  ##### - ' || SQLERRM || CRLF || 'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
        ASSUNTO := '[ERRO]';
    END;
    --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit; 
  END IF;
  --
  --------------------------------------------------------------------------
  -- Atualização de carga das views de custos variáveis
  --------------------------------------------------------------------------
  --limpar para proximo registro
   SQLERRO:=''; 
  IF (FLAG = 4 OR FLAG = 99) THEN
    MENSAGEM := MENSAGEM || CRLF || CRLF ||'Tipo da Carga: 4 - Views de Custos Variáveis';
    -- info log
    LOG_DT_CARGA_PROCESSO := sysdate;
    BLOCO_UPD := 'VW_BI_PMS_CUSTOS_VAR_MAT (flag 4 de 6)';
    --
    BEGIN
      SELECT COUNT(*) INTO CONTADOR1 FROM PMS.VW_BI_PMS_CUSTOS_VAR_MAT;
      -- Inserir LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);         
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;        
         --
                   DBMS_MVIEW.REFRESH('"PMS"."VW_BI_PMS_CUSTOS_VAR_MAT"');
         --
      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.VW_BI_PMS_CUSTOS_VAR_MAT;
      -- Info para LOG
         DESCRICAO:= 'FIM - Linhas: '||to_char(contador2); 
         STATUS:='OK';
      --   
         MENSAGEM := MENSAGEM || CRLF || CRLF || '[OK] PMS.VW_BI_PMS_CUSTOS_VAR_MAT' || CRLF ||
                  'Linhas antes do Refresh: ' || TO_CHAR(CONTADOR1) || CRLF ||
                  'Linhas após Refresh: ' || TO_CHAR(CONTADOR2) || CRLF ||
                  'Diferença: ' || TO_CHAR(CONTADOR2 - CONTADOR1) || CRLF ||
                  'Data Execução: ' ||to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
    EXCEPTION
      WHEN OTHERS then
        STATUS:='ERRO'; --log
        SQLERRO:=sqlerrm;  --log
        MENSAGEM := MENSAGEM || CRLF || CRLF ||'[ERRO] ##### PMS.VW_BI_PMS_CUSTOS_VAR_MAT  ##### - ' || SQLERRM || CRLF || 'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
        ASSUNTO := '[ERRO]';
    END;
    --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
  END IF;
  --
  --------------------------------------------------------------------------
  -- Atualização de carga das views de despesas fixas
  --------------------------------------------------------------------------
  --limpar para proximo registro
   SQLERRO:=''; 
  IF (FLAG = 5 OR FLAG = 99) THEN
    MENSAGEM := MENSAGEM || CRLF || CRLF ||'Tipo da Carga: 5 - Views de Despesas Fixas';
    -- info LOG
       LOG_DT_CARGA_PROCESSO := sysdate;
       BLOCO_UPD := 'VW_BI_PMS_DESPFIX_UMKT_DRE_MAT (flag 5 de 6)[REFRESH]';
    --
    BEGIN
      SELECT COUNT(*) INTO CONTADOR1 FROM PMS.VW_BI_PMS_DESPFIX_UMKT_DRE_MAT;
      -- Inserir LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);         
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;        
         --
                   DBMS_MVIEW.REFRESH('"PMS"."VW_BI_PMS_DESPFIX_UMKT_DRE_MAT"');
         --
      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.VW_BI_PMS_DESPFIX_UMKT_DRE_MAT;
      -- Info para LOG
         DESCRICAO:= 'FIM - Linhas: '||to_char(contador2); 
         STATUS:='OK';
      --   
         MENSAGEM := MENSAGEM || CRLF || CRLF || '[OK] PMS.VW_BI_PMS_DESPFIX_UMKT_DRE_MAT' || CRLF ||
                  'Linhas antes do Refresh: ' || TO_CHAR(CONTADOR1) || CRLF ||
                  'Linhas após Refresh: ' || TO_CHAR(CONTADOR2) || CRLF ||
                  'Diferença: ' || TO_CHAR(CONTADOR2 - CONTADOR1) || CRLF ||
                  'Data Execução: ' ||to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
    EXCEPTION
      WHEN OTHERS then
        STATUS:='ERRO';  --log
        SQLERRO:=sqlerrm;  --log
        MENSAGEM := MENSAGEM || CRLF || CRLF || '[ERRO] ##### PMS.VW_BI_PMS_DESPFIX_UMKT_DRE_MAT  ##### - ' || SQLERRM || CRLF || 'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
        ASSUNTO := '[ERRO]';
    END;
    --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
  END IF;
  --
  --------------------------------------------------------------------------
  -- Atualização de carga das views de despesas fixas
  --------------------------------------------------------------------------
  --limpar para proximo registro
   SQLERRO:=''; 
  IF (FLAG = 6 OR FLAG = 99) THEN
    MENSAGEM := MENSAGEM || CRLF || CRLF || 'Tipo da Carga: 6 - Views de Despesas Fixas';
    -- info LOG
     LOG_DT_CARGA_PROCESSO := sysdate;
     BLOCO_UPD := 'VW_BI_PMS_MAT_FIM_RATEIO (flag 6 de 6)[REFRESH]';
    --
    BEGIN
      SELECT COUNT(*) INTO CONTADOR1 FROM PMS.VW_BI_PMS_MAT_FIM_RATEIO;
      -- Inserir LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);         
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;        
         --
                   DBMS_MVIEW.REFRESH('"PMS"."VW_BI_PMS_MAT_FIM_RATEIO"');
         --
      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.VW_BI_PMS_MAT_FIM_RATEIO;
      -- Info para LOG
         DESCRICAO:= 'FIM - Linhas: '||to_char(contador2); 
         STATUS:='OK';
         --   
         MENSAGEM := MENSAGEM || CRLF || CRLF || '[OK} PMS.VW_BI_PMS_MAT_FIM_RATEIO' || CRLF ||
                  'Linhas antes do Refresh: ' || TO_CHAR(CONTADOR1) || CRLF ||
                  'Linhas após Refresh: ' || TO_CHAR(CONTADOR2) || CRLF ||
                  'Diferença: ' || TO_CHAR(CONTADOR2 - CONTADOR1) || CRLF ||
                  'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
    EXCEPTION
      WHEN OTHERS then
        STATUS:='ERRO';  -- log
        SQLERRO:=sqlerrm;  -- log
        MENSAGEM := MENSAGEM || CRLF || CRLF ||'[ERRO] ##### PMS.VW_BI_PMS_MAT_FIM_RATEIO  ##### - ' || SQLERRM || CRLF || 'Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
        ASSUNTO := '[ERRO]';
    END;
    --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
  END IF;

  MENSAGEM := MENSAGEM || CRLF || CRLF || '=== Fim da Carga '||to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' )
||' ===';
     mensagem := mensagem || crlf || 'select * from pms.TB_BI_PMS_LOG_CARGAS where NOME_CARGA = ''UMKT'' order by 1 desc';
  ASSUNTO := ASSUNTO || ' [PMS][UMKT] Resultado da Carga';
  SEND_MAIL('lahumada@cit.com.br', ASSUNTO, MENSAGEM);
  SEND_MAIL('alexandrel@cit.com.br', ASSUNTO, MENSAGEM);
end ups_load_inform_dre_umkts_tmp;