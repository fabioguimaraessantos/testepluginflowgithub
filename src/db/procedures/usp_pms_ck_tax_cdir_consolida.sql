create or replace procedure usp_pms_ck_tax_cdir_consolida(flag number) is
   --INTERVAL: trunc(sysdate + 1) + 3/24
   v_var  number := 0;
   v_var2 number := 0;
   -- Informacões para LOG
   log_data              date := trunc(sysdate);
   log_dt_carga_processo date;
   nome_carga            varchar2(50) := '(8) TAX_CDIR';
   bloco_upd             varchar2(50) := '';
   descricao             varchar2(200) := '-';
   status                varchar2(30) := '';
   sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'USP_PMS_ck_tax_cdir_consolida';
   --
   crlf     varchar2(2) := chr(13) || chr(10);
   assunto  varchar2(200) := '';
   mensagem varchar2(3000) := '=== Início da Validacao ' ||
                              to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') ||
                              ' ===';
     var_aux integer;
begin
   -- Procedure criada por Luiz Ahumada ago/2011
   mensagem := mensagem || crlf || ':: PROCEDURE: ' || nome_proc || ' ::';
   send_mail('alexandrel@cit.com.br','[PMS][' || nome_carga || '] Inicio Validacao', mensagem);
   send_mail('lnoboru@cit.com.br','[PMS][' || nome_carga || '] Inicio Validacao', mensagem);   
   dbms_lock.sleep(1);
   ------------------------------------------------
   -- CDIR
   ------------------------------------------------
   if (flag = 1 or flag = 99) then
      mensagem              := mensagem || crlf || crlf ||'Flag 1 - Check CDIR';
      log_dt_carga_processo := sysdate;
      bloco_upd             := 'Flag 1 - Check CDIR';
      --
      select count(*)  --apos refresh
      into   v_var
      from   tb_bi_pms_consolida_dres c
      where  c.origem in ('CDIR');
      --
      select qtde  --antes do refresh
      into   v_var2
      from   (select qtde,
                     row_number() over(partition by tipo order by dt_carga desc) nlinha
              from   tb_bi_pms_log_cons_dres
              where  tipo = 'CDIR'
              order  by dt_carga desc)
      where  nlinha = 1;
      if ( v_var <> 0) then 
               if (v_var2 < v_var) and ((v_var2 / v_var) < 0.99) then
                  status   := 'ERRO';
                  assunto  := '[ERRO%]';
                  sqlerro  := 'Variacao pocentagem: ' ||round(((v_var2 / v_var) * 100), 2) || '%';
                  mensagem := mensagem || crlf || crlf ||
                              '[ERRO] ##### PMS - Check CDIR Consolida_dre  ##### - ' || crlf ||
                              '       '||sqlerro|| crlf ||
                              '       Linhas antes do Refresh: ' || to_char(v_var2) || crlf ||
                              '       Linhas após Refresh: '     || to_char(v_var) || crlf ||
                              '       Diferença: '               || to_char(v_var - v_var2) || crlf ||
                              '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
               else
                  status   := 'OK';
                  mensagem := mensagem || crlf || crlf ||
                              '[OK] PMS - Check CDIR Consolida_dre' || crlf ||
                              '       Linhas antes do Refresh: ' || to_char(v_var2) || crlf ||
                              '       Linhas após Refresh: ' || to_char(v_var) || crlf ||
                              '       Diferença: ' || to_char(v_var - v_var2) || crlf ||
                              '       Porcentagem: ' ||
                              round(((-1 * (1 - (v_var2 / v_var))) * 100), 2) || '%' || crlf ||
                              '       Data Execução: ' ||
                              to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
               end if;
       else
                  status   := 'ERRO';
                  assunto  := '[ERRO%]';
                  sqlerro  := 'Variacao pocentagem: 100%';
                  mensagem := mensagem || crlf || crlf ||
                              '[ERRO] ##### PMS - Check CDIR Consolida_dre  ##### - ' || crlf ||
                              '       '||sqlerro|| crlf ||
                              '       Linhas antes do Refresh: ' || to_char(v_var2) || crlf ||
                              '       Linhas após Refresh: '     || to_char(v_var) || crlf ||
                              '       Diferença: '               || to_char(v_var - v_var2) || crlf ||
                              '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');       
       end if;
      ---
      insert into tb_bi_pms_log_cons_dres values (sysdate, 'CDIR', v_var);
      commit;
      ---
      log_dt_carga_processo := sysdate;
      insert into tb_bi_pms_log_cargas
         (log_data,
          log_dt_carga_processo,
          nome_carga,
          bloco_upd,
          descricao,
          status,
          sqlerro,
          nome_proc)
      values
         (log_data,
          log_dt_carga_processo,
          nome_carga,
          bloco_upd,
          descricao,
          status,
          sqlerro,
          nome_proc);
      commit;
   end if;
   sqlerro := '';
   dbms_lock.sleep(1);
   v_var  := 0;
   v_var2 := 0;
   ------------------------------------------------
   -- TAX
   ------------------------------------------------
   if (flag = 2 or flag = 99) then
      bloco_upd             := 'Flag 2 - Check TAX';
      mensagem              := mensagem || crlf || crlf ||bloco_upd;
      log_dt_carga_processo := sysdate;      
      --
      select count(*)
      into   v_var  -- APOS
      from   tb_bi_pms_consolida_dres c
      where  c.origem in ('TAX');
      --
      select qtde
      into   v_var2 -- ANTES
            from (select   dt_carga dt_car,
                           tipo, 
                           qtde,
                           row_number() over(partition by tipo order by dt_carga desc) nlinha 
                    from tb_bi_pms_log_cons_dres 
                    where tipo = 'TAX' ) 
      where nlinha = 1; 
      
      var_aux := TRUNC(((v_var/nvl(case when v_var2 = 0 
                                             then 1
                                             else v_var2 end, 1))*100),2);
            
      if (var_aux < 99) then  --DIFERENCA MENOR QUE 99%
                 status   := 'ERRO';  
                 assunto  := '[ERRO%]';
                 sqlerro  := 'Variacao %: '||to_char(100 - var_aux);
                 mensagem := mensagem || crlf ||
                        '[ERRO] ##### PMS - Check TAX Consolida_dre  ##### - ' || crlf ||
                        '       '||sqlerro || crlf ||
                        '       Linhas antes do Refresh: ' || to_char(v_var2) || crlf ||
                        '       Linhas após Refresh: '     || to_char(v_var) || crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-var_aux)|| '%' || crlf ||
                        '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || crlf;
      else
                 status   := 'OK';
                 mensagem := mensagem || crlf || 
                              '[OK] PMS - Check TAX Consolida_dre' || crlf ||
                              '       Linhas antes do Refresh: ' || to_char(v_var2) || crlf ||
                              '       Linhas após Refresh: ' || to_char(v_var) || crlf ||
                              '       '||sqlerro || crlf ||
                              '       Data Execução: ' ||  to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || crlf;
     
      end if;
      var_aux:=0;
      ---
      insert into tb_bi_pms_log_cons_dres values (sysdate, 'TAX', v_var);
      commit;
      ---
      log_dt_carga_processo := sysdate;
      insert into tb_bi_pms_log_cargas
         (log_data,
          log_dt_carga_processo,
          nome_carga,
          bloco_upd,
          descricao,
          status,
          sqlerro,
          nome_proc)
      values
         (log_data,
          log_dt_carga_processo,
          nome_carga,
          bloco_upd,
          descricao,
          status,
          sqlerro,
          nome_proc);
      commit;
   end if;
   sqlerro := '';
   dbms_lock.sleep(1);
   v_var  := 0;
   v_var2 := 0;
   ------------------------------------------------
   -- COST_RES
   ------------------------------------------------
   if (flag = 3 or flag = 99) then
      mensagem              := mensagem || crlf || crlf ||
                               'Flag 3 - Check COST_RES';
      log_dt_carga_processo := sysdate;
      bloco_upd             := 'Flag 3 - Check COST_RES';
      --
      select count(*)
      into   v_var
      from   tb_bi_pms_consolida_dres c
      where  c.origem in ('COST_RES');
      --
      select qtde
      into   v_var2
      from   (select qtde,
                     row_number() over(partition by tipo order by dt_carga desc) nlinha
              from   tb_bi_pms_log_cons_dres
              where  tipo = 'COST_RES'
              order  by dt_carga desc)
      where  nlinha = 1;
      if v_var2 is null then
         v_var := 0;
      end if;
      
  
      if (v_var <> 0 ) then
               if (v_var2 < v_var) and ((v_var2 / v_var) < 0.99) then
                  status   := 'ERRO';
                  assunto  := '[ERRO%]';
                  if (v_var2 = 0) then
                     sqlerro  := 'Variacao pocentagem: 100% (qtde linhas Antes ZERADA)';
                  else                  
                     sqlerro  := 'Variacao pocentagem: ' ||round(( ( 1 - (v_var / v_var2)) * 100), 2) || '%';
                  end if;
                  mensagem := mensagem || crlf || crlf ||
                              '[ERRO] ##### PMS - Check COST_RES Consolida_dre  ##### - ' || crlf ||
                              '       '||sqlerro|| crlf ||
                              '       Linhas antes do Refresh: ' || to_char(v_var2) || crlf ||
                              '       Linhas após Refresh: '     || to_char(v_var) || crlf ||
                              '       Diferença: '               || to_char(v_var - v_var2) || crlf ||
                              '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
               else
                  status   := 'OK';
                  mensagem := mensagem || crlf || crlf ||
                              '[OK] PMS - Check COST_RES Consolida_dre' || crlf ||
                              '       Linhas antes do Refresh: ' || to_char(v_var2) || crlf ||
                              '       Linhas após Refresh: ' || to_char(v_var) || crlf ||
                              '       Diferença: ' || to_char(v_var - v_var2) || crlf ||
                              '       Porcentagem: ' ||
                              round(((-1 * (1 - (v_var2 / v_var))) * 100), 2) || '%' || crlf ||
                              '       Data Execução: ' ||
                              to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
               end if;
      else
                  status   := 'ERRO';
                  assunto  := '[ERRO%]';
                  sqlerro  := 'Variacao pocentagem: 100%';
                  mensagem := mensagem || crlf || crlf ||
                              '[ERRO] ##### PMS - Check COST_RES Consolida_dre  ##### - ' || crlf ||
                              '       '||sqlerro|| crlf ||
                              '       Linhas antes do Refresh: ' || to_char(v_var2) || crlf ||
                              '       Linhas após Refresh: '     || to_char(v_var) || crlf ||
                              '       Diferença: '               || to_char(v_var - v_var2) || crlf ||
                              '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');      
      end if;
      ---
      insert into tb_bi_pms_log_cons_dres
      values
         (sysdate, 'COST_RES', v_var);
      commit;
      ---
      log_dt_carga_processo := sysdate;
      insert into tb_bi_pms_log_cargas
         (log_data,
          log_dt_carga_processo,
          nome_carga,
          bloco_upd,
          descricao,
          status,
          sqlerro,
          nome_proc)
      values
         (log_data,
          log_dt_carga_processo,
          nome_carga,
          bloco_upd,
          descricao,
          status,
          sqlerro,
          nome_proc);
      commit;
   end if;
   sqlerro := '';
   dbms_lock.sleep(1);
   v_var  := 0;
   v_var2 := 0;
   ------------------------------------------------
   -- DESP_RES
   ------------------------------------------------
   if (flag = 4 or flag = 99) then
      mensagem              := mensagem || crlf || crlf ||
                               'Flag 4 - Check DESP_RES';
      log_dt_carga_processo := sysdate;
      bloco_upd             := 'Flag 4 - Check DESP_RES';
      --
      select count(*)
      into   v_var
      from   tb_bi_pms_consolida_dres c
      where  c.origem in ('DESP_RES');
      --
      select qtde
      into   v_var2
      from   (select qtde,
                     row_number() over(partition by tipo order by dt_carga desc) nlinha
              from   tb_bi_pms_log_cons_dres
              where  tipo = 'DESP_RES'
              order  by dt_carga desc)
      where  nlinha = 1;
      if v_var2 is null then
         v_var := 0;
      end if;
      if (v_var <> 0 ) then
               if (v_var2 < v_var) and ((v_var2 / v_var) < 0.99) then
                  status   := 'ERRO';
                  assunto  := '[ERRO%]';
                  sqlerro  := 'Variacao pocentagem: ' || round(((v_var2 / v_var) * 100), 2) || '%';
                  mensagem := mensagem || crlf || crlf ||
                              '[ERRO] ##### PMS - Check DESP_RES Consolida_dre  ##### - ' || crlf ||
                              '       '||sqlerro || crlf ||
                              '       Linhas antes do Refresh: ' || to_char(v_var2) || crlf ||
                              '       Linhas após Refresh: '     || to_char(v_var) || crlf ||
                              '       Diferença: '               || to_char(v_var - v_var2) || crlf ||
                              '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
               else
                  status   := 'OK';
                  mensagem := mensagem || crlf || crlf ||
                              '[OK] PMS - Check DESP_RES Consolida_dre' || crlf ||
                              '       Linhas antes do Refresh: ' || to_char(v_var2) || crlf ||
                              '       Linhas após Refresh: ' || to_char(v_var) || crlf ||
                              '       Diferença: ' || to_char(v_var - v_var2) || crlf ||
                              '       Porcentagem: ' ||
                              round(((-1 * (1 - (v_var2 / v_var))) * 100), 2) || '%' || crlf ||
                              '       Data Execução: ' ||
                              to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
               end if;
      else
                  status   := 'ERRO';
                  assunto  := '[ERRO%]';
                  sqlerro  := 'Variacao pocentagem: 100%';
                  mensagem := mensagem || crlf || crlf ||
                              '[ERRO] ##### PMS - Check DESP_RES Consolida_dre  ##### - ' || crlf ||
                              '       '||sqlerro || crlf ||
                              '       Linhas antes do Refresh: ' || to_char(v_var2) || crlf ||
                              '       Linhas após Refresh: '     || to_char(v_var) || crlf ||
                              '       Diferença: '               || to_char(v_var - v_var2) || crlf ||
                              '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');      
      end if;
      ---
      insert into tb_bi_pms_log_cons_dres
      values
         (sysdate, 'DESP_RES', v_var);
      commit;
      ---
      log_dt_carga_processo := sysdate;
      insert into tb_bi_pms_log_cargas
         (log_data,
          log_dt_carga_processo,
          nome_carga,
          bloco_upd,
          descricao,
          status,
          sqlerro,
          nome_proc)
      values
         (log_data,
          log_dt_carga_processo,
          nome_carga,
          bloco_upd,
          descricao,
          status,
          sqlerro,
          nome_proc);
      commit;
   end if;
   sqlerro := '';
   dbms_lock.sleep(1);
   v_var  := 0;
   v_var2 := 0;
   mensagem := mensagem || crlf || crlf || '=== Fim da Validacao ' ||to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===';
   mensagem := mensagem || crlf ||'select * from pms.TB_BI_PMS_LOG_CARGAS where NOME_CARGA = ''' ||nome_carga || ''' order by 2 desc';
   assunto  := assunto || '[PMS][' || nome_carga ||'] Resultado da Validacao';
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
   send_mail('lnoboru@cit.com.br', assunto, mensagem);   
end usp_pms_ck_tax_cdir_consolida;