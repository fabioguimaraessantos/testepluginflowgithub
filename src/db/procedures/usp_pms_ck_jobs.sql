create or replace procedure USP_PMS_ck_jobs as
--INTERVAL: trunc(sysdate + 1) + 6/24
   -------------------------------------------------------
   -- Informacões para LOG
   -------------------------------------------------------
   log_data              date := trunc(sysdate);
   log_dt_carga_processo date;
   nome_carga            varchar2(50) := '(11*) JOBS';
   bloco_upd             varchar2(50) := '';
   descricao             varchar2(200) := '';
   status                varchar2(30) := '';
   sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'USP_PMS_ck_jobs';
   --
   crlf      varchar2(2) := chr(13) || chr(10); -- <ENTER>
   assunto   varchar2(200) := '';
   mensagem  varchar2(3000) := '=== Início da Carga ' ||
                               to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') ||
                               ' ===';
   contador integer;
   sms_msg   varchar2(200) := 'Objeto: ';
begin
   mensagem := mensagem || crlf || ':: PROCEDURE: ' || nome_proc || ' ::';
   send_mail('lahumada@cit.com.br','[PMS][' || nome_carga || '] Inicio Validação',mensagem);
   send_mail('alexandrel@cit.com.br','[PMS][' || nome_carga || '] Inicio Validação',mensagem);
   send_mail('lnoboru@cit.com.br','[PMS][' || nome_carga || '] Inicio Validação',mensagem);   
   dbms_lock.sleep(1);
   --------------------------------------------
   mensagem              := mensagem || crlf || crlf || 'Check JOBS Brokens ';
   bloco_upd             := 'Check JOBS Brokens';
   descricao             := 'INICIO';
   log_dt_carga_processo := sysdate;
   select count(*) into contador from user_jobs where broken = 'Y';
   if contador > 0 then
      status   := 'ERRO';
      assunto  := '[ERRO]';
      sqlerro  := 'Job Broken!';
      mensagem := mensagem || crlf || crlf ||
                  '[ERRO] ##### PMS - Erro em um ou mais JOBS  ##### - ' || crlf ||
                  '       Data Execução: ' ||
                  to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
   else
      status   := 'OK';
      mensagem := mensagem || crlf || crlf || '[OK] PMS - JOBs OK' || crlf ||
                  '       Data Execução: ' ||
                  to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
   end if;
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
   --
   mensagem := mensagem || crlf || crlf || '=== Fim da Validação ' ||
               to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===';
   mensagem := mensagem || crlf ||
               'select * from pms.TB_BI_PMS_LOG_CARGAS where NOME_CARGA = ' ||
               nome_carga || ' order by 2 desc';
   assunto  := assunto || ' [PMS][' || nome_carga ||'] Resultado da Validação';
   send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
   send_mail('lnoboru@cit.com.br', assunto, mensagem);   
end USP_PMS_ck_jobs;