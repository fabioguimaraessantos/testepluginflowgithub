CREATE OR REPLACE PROCEDURE "USP_PMS_PROFITLOSS_CARGA4" (flag number) as
--   INTERVAL: trunc(sysdate+1) + 2.5/24
   -- Informac?es para LOG
   log_data              date := trunc(sysdate);
   log_dt_carga_processo date;
   nome_carga            varchar2(50) := '(7.4) PROFITLOSS';
   bloco_upd             varchar2(50) := '';
   descricao             varchar2(200) := '';
   status                varchar2(30) := '';
   sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'USP_PMS_profitloss';
   sms_msg               varchar2(200) := 'Objeto: ';
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
   mensagem  varchar2(4000) := '=== Inicio da Carga ' ||
                               to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') ||
                               '  ===';
   contador1 integer;
   contador2 integer;
   chk_pessoa integer;
      cod_modulo integer;
   chk_praticas integer;
   chk_umkt integer;
begin
   ----------------------------------------------------------------
   -- INICIO DA PROCEDURE
   ----------------------------------------------------------------
   mensagem := mensagem || crlf || ':: PROCEDURE: ' || nome_proc || ' ::';
   send_mail('lnoboru@ciandt.com','[PMS]['||nome_carga||'] Processo iniciado',mensagem);   
   send_mail('alexandrel@cit.com.br','[PMS]['||nome_carga||'] Processo iniciado',mensagem);

   send_mail('tspadari@ciandt.com','[PMS]['||nome_carga||'] Processo iniciado',mensagem);
   send_mail('llino@ciandt.com','[PMS]['||nome_carga||'] Processo iniciado',mensagem);
   send_mail('andreiap@ciandt.com','[PMS]['||nome_carga||'] Processo iniciado',mensagem);
   


   --Check se carregou OK as cargas de PRATICAS
   select count(*)
   into chk_praticas
   from tb_bi_pms_log_cargas t
   where nome_carga = '(6) PRATICAS'
   and t.status <> 'OK'
   and log_data = trunc(sysdate);

   --Check se carregou OK as cargas de UMKT
   select count(*)
   into chk_umkt
   from tb_bi_pms_log_cargas t
   where nome_carga = '(5) UMKT'
   and t.status <> 'OK'
   and log_data = trunc(sysdate);

   chk_umkt :=0;
   chk_praticas := 0;
   --Check se nao tem nenhuma pessoa ativa com o campo de admissao null
   select count(*)
   into chk_pessoa
   from pessoa p
   where p.pess_dt_admissao is null;
   
   select count(*) 
   into cod_modulo
   from   modulo
   where  modu_cd_modulo = 2 and
          ((modu_dt_fechamento = case when(to_char(sysdate, 'dd') <= 5)
                                      then(to_date('01/' || to_char(add_months(sysdate, -2), 'mm/yyyy'), 'dd/mm/yyyy'))
                                      else(to_date('01/' || to_char(add_months(sysdate, -1), 'mm/yyyy'), 'dd/mm/yyyy')) 
                                      end) or
            modu_dt_fechamento = (to_date('01/' || to_char(add_months(sysdate, -1), 'mm/yyyy'), 'dd/mm/yyyy')));
   
   if cod_modulo = 1 then  

            if (chk_umkt = 0) and (chk_praticas = 0 ) and (chk_pessoa = 0) then
                        if (flag = 5 or flag = 99) then
                           mensagem              := mensagem || crlf || crlf ||
                                                    'Tipo da Carga: 5 - Tabela: tb_bi_consolida_dres - WB: PMS-ProfitLoss';
                           log_dt_carga_processo := sysdate;
                           bloco_upd             := 'tb_bi_consolida_dres (flag 5 de 5)';
                           begin
                              select count(*)
                              into   contador1
                              from   tb_bi_pms_consolida_dres
                              where  dt_mes <= (select modu_dt_fechamento
                                                from   modulo
                                                where  modu_cd_modulo = 2)
                                                AND fonte in ('Q3','Q4', 'Q5');
                              -- Inserir LOG
                              descricao := 'INICIO - Linhas: ' || to_char(contador1);
                              insert into tb_bi_pms_log_cargas
                                 (log_data,
                                  log_dt_carga_processo,
                                  nome_carga,
                                  bloco_upd,
                                  descricao,
                                  status,
                                  nome_proc)
                              values
                                 (log_data,
                                  log_dt_carga_processo,
                                  nome_carga,
                                  bloco_upd,
                                  descricao,
                                  'OK',
                                  nome_proc);
                              begin
                                  delete from tb_bi_pms_consolida_dres t
                                  where dt_mes not between to_date('01-01-2010', 'dd-mm-yyyy') and  to_date('01-12-2011', 'dd-mm-yyyy')
                                              AND fonte in  ( 'Q3','Q4', 'Q5');
                                    commit;
                              exception
                                  when others then
                                 status   := 'ERRO';
                                 assunto  := '[ERRO]';
                                 sqlerro  := sqlerrm;
                                 mensagem := mensagem || crlf || crlf ||
                                             '[ERRO] ##### PMS.tb_bi_pms_consolida_dres (deletar registros <> 2011) ##### - ' || crlf ||
                                             '       SQL Erro: ' || sqlerrm || crlf ||
                                             '       Data Execucao: ' ||
                                             to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                 --envio alerta SMS INFRA
                                 sms_msg := sms_msg || 'PMS.tb_bi_pms_consolida_dres; ERRO: ' ||
                                            sqlerro || '; Data: ' ||
                                            to_char(sysdate, 'DD/MM/YYYY');
                                 sms_msg := 'Objeto: ';
                              end;
                              if (status is null) then
                                      begin 
                                             insert into tb_bi_pms_consolida_dres 
                                                 select *  from   vw_bi_pms_consolida_dres_q3;
                                              commit;
                                              
                                             insert into tb_bi_pms_consolida_dres 
                                                 select *  from   vw_bi_pms_consolida_dres_q4;
                                              commit;

                                             insert into tb_bi_pms_consolida_dres 
                                                 select *  from   vw_bi_pms_consolida_dres_q5;
                                              commit;
                                            
                                             select count(*)
                                             into   contador2
                                             from   tb_bi_pms_consolida_dres
                                             where  dt_mes <= (select modu_dt_fechamento
                                                               from   modulo
                                                               where  modu_cd_modulo = 2)
                                                                            AND fonte  in ('Q3','Q4', 'Q5');

                                             -- Info para LOG
                                             descricao := 'FIM    - Linhas: ' || to_char(contador2);
                                             -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                                             if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                                                status   := 'ERRO';
                                                assunto  := '[ERRO%]';
                                                sqlerro  := 'Variacao pocentagem: ' ||
                                                            round(((contador2 / contador1) * 100), 2) || '%';
                                                mensagem := mensagem || crlf || crlf ||
                                                            '[ERRO] ##### PMS.tb_bi_pms_consolida_dres  ##### - ' || crlf ||
                                                            '       Variacao Porcentagem: ' ||
                                                            round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                                            '       Linhas antes do Refresh: ' ||
                                                            to_char(contador1) || crlf ||
                                                            '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                                            '       Diferenca: ' ||
                                                            to_char(contador2 - contador1) || crlf ||
                                                           --                          'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                                            '       Data Execuc?o: ' ||
                                                            to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                                -- ja estava comentado, n?o retirar comentario
                                                --envio alerta SMS INFRA
                                                --sms_msg := sms_msg ||'PMS.tb_bi_pms_consolida_dres; ERRO: '|| sqlerro ||'; Data: '||to_char(sysdate, 'DD/MM/YYYY');
                                                --send_mail('noc-cit-sms@cit.com.br', assunto||'[PMS]['||NOME_CARGA||'] Resultado da Carga', sms_msg);
                                                --limpa msg
                                                --sms_msg := 'Objeto: ';
                                             else
                                                status   := 'OK';
                                                mensagem := mensagem || crlf || crlf ||
                                                            '[OK] PMS.tb_bi_pms_consolida_dres' || crlf ||
                                                            '     Linhas antes do Refresh: ' ||
                                                            to_char(contador1) || crlf ||
                                                            '     Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                                            '     Diferenca: ' || to_char(contador2 - contador1) || crlf ||
                                                            '     Porcentagem: ' ||
                                                            round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                                            '     Data Execuc?o: ' ||
                                                            to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                             end if;
                                          exception
                                             when others then
                                                assunto  := '[ERRO]';
                                                status   := 'ERRO';
                                                sqlerro  := sqlerrm;
                                                mensagem := mensagem || crlf || crlf ||
                                                            '[ERRO] ##### PMS.tb_bi_pms_consolida_dres  ##### - ' || crlf ||
                                                            '       SQL Erro: ' || sqlerrm || crlf ||
                                                            '       Data Execuc?o: ' ||
                                                            to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                                --envio alerta SMS INFRA
                                                sms_msg := sms_msg || 'PMS.tb_bi_pms_consolida_dres; ERRO: ' ||
                                                           sqlerro || '; Data: ' ||
                                                           to_char(sysdate, 'DD/MM/YYYY');
                                    --            send_mail('noc-cit-sms@cit.com.br',
                                    --                      assunto || '[PMS][' || nome_carga ||
                                    --                      '] Resultado da Carga',
                                    --                      sms_msg);
                                                --limpa msg
                                                sms_msg := 'Objeto: ';
                                          end;
                           --Inserir LOG
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
                           end;
                        end if;
         
             else
                  BLOCO_UPD := 'PESSOA.DT_ADMISSAO is NULL or Log: Praticas or UMKT with error';
                  DESCRICAO:= 'VALIDACAO PESSOA data NULL, LOG PRATICAS e LOG UMKT';
                  STATUS:='ERRO';
                  SQLERRO:= chk_pessoa||' pessoas ativas com data resicao null;  '||chk_praticas||' erros na carga de PRATICAS; '||chk_umkt||' erros na carga de UMKT!';
                  assunto  := '[ERRO]';
                  LOG_DT_CARGA_PROCESSO := sysdate;
                  insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
                  values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
                  commit;
                  mensagem := mensagem || crlf || crlf ||'[ERRO] ##### '||chk_pessoa||' pessoa(s) ativas com data resicao null #####'|| crlf ||
                                                                '##### '||chk_praticas||' erro(s) na carga de PRATICAS #####'|| crlf ||
                                                                '##### '||chk_umkt||' erro(s) na carga de UMKT #####' || crlf
                                       || crlf || '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  send_mail('alexandrel@cit.com.br', assunto|| ' [PMS][' || nome_carga || '] Check PMS.PESSOA; LOG PRATICA; LOG UMKT', mensagem);
                  send_mail('lnoboru@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check PMS.PESSOA; LOG PRATICA; LOG UMKT', mensagem);         
--                  send_mail('it_oncall@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check PMS.PESSOA; LOG PRATICA; LOG UMKT', mensagem);
                  
                  send_mail('andreiap@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check PMS.PESSOA; LOG PRATICA; LOG UMKT', mensagem);         
                  send_mail('llino@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check PMS.PESSOA; LOG PRATICA; LOG UMKT', mensagem);         
                  send_mail('andreiap@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check PMS.PESSOA; LOG PRATICA; LOG UMKT', mensagem);         
                  
                     sms_msg := sms_msg || 'pms.pessoa - Data Admissao is Null';
            end if;
else
         BLOCO_UPD := 'IF DT_FECHMNTO MODULO = 1';
         DESCRICAO:= 'ATENCAO - TABELA MODULO NAO CONTEM DATA FECHAMENTO DO MES ANTERIOR!';
         STATUS:='ERRO';
         SQLERRO:= 'DATA FECHAMENTO DO MODULO DO MES ANTERIOR NAO EXISTE!';
         assunto  := '[ERRO]';
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;         
         mensagem := mensagem || crlf || crlf ||'[ERRO] ##### MODULO - Check Data FECHAMENTO ##### - ' 
                              || crlf || '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
            send_mail('alexandrel@cit.com.br', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
            send_mail('lnoboru@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);          
            
            send_mail('tspadari@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);          
            send_mail('llino@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);          
            send_mail('andreiap@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);          
              
   end if;              
   mensagem := mensagem || crlf || crlf || '=== Fim da Carga ' ||
               to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===';
   mensagem := mensagem || crlf ||
               'select * from pms.TB_BI_PMS_LOG_CARGAS where NOME_CARGA = '''||nome_carga||''' order by 2 desc';
   mensagem := mensagem || crlf || 'FLAG: ' || flag;
   assunto  := assunto || '[PMS]['||nome_carga||'] Resultado Carga';
   send_mail('lnoboru@ciandt.com', assunto, mensagem);   
   send_mail('alexandrel@cit.com.br', assunto, mensagem);

   send_mail('tspadari@ciandt.com', assunto, mensagem);
   send_mail('llino@ciandt.com', assunto, mensagem);
   send_mail('andreiap@ciandt.com', assunto, mensagem);
   
   

   if status = 'ERRO' then
      mensagem := 'ATTENTION: Please, contact Ciandt IT Business Intelligence Team as soon as possible. ' || crlf || crlf ||
                  mensagem;
      send_mail('it_oncall@ciandt.com', assunto, mensagem);
   end if;
end USP_PMS_profitloss_CARGA4;