CREATE OR REPLACE PROCEDURE "USP_PMS_PROFITLOSS" (flag number) as
--   INTERVAL: trunc(sysdate+1) + 2.5/24
   -- Informac?es para LOG
   log_data              date := trunc(sysdate);
   log_dt_carga_processo date;
   nome_carga            varchar2(50) := '(7) PROFITLOSS';
   bloco_upd             varchar2(50) := '';
   descricao             varchar2(200) := '';
   status                varchar2(30) := '';
   sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'USP_PMS_profitloss';
   sms_msg               varchar2(200) := 'Objeto: ';
   /*Procedure que faz a carga dos Workbooks do ProfitLoss
   Entrada:
     SÓ SE RODA A CARGA 1 E 2 -
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
   --send_mail('lahumada@cit.com.br','[PMS]['||nome_carga||'] Processo iniciado',mensagem);
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
         
                        if (flag = 1 or flag = 99) then
                           mensagem              := mensagem || crlf || crlf ||
                                                    'Tipo da Carga: 1 - Tabela: tb_bi_pms_dre_contratos - WB: PMS-ProfitLoss / PMS-ProfitLoss-Login / PMS-LOB-ProfitLoss / PMS-UMKT-ProfitLoss';
                           log_dt_carga_processo := sysdate;
                           bloco_upd             := 'tb_bi_pms_dre_contratos (flag 1 de 5)';
                           begin
                              select count(*)
                              into   contador1
                              from   tb_bi_pms_dre_contratos
                              where  dt_mes <= (select modu_dt_fechamento
                                                from   modulo
                                                where  modu_cd_modulo = 2);
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
                                  commit;
                      /*        delete from bi_pms.tb_bi_pms_dre_contratos_log;
                              commit;
                              --
                              insert into bi_pms.tb_bi_pms_dre_contratos_log
                                 select c.*,
                                        sysdate data_carga
                                 from   tb_bi_pms_dre_contratos c;
                              commit;*/
                              --
                              --
                              --execute immediate 'truncate table tb_bi_pms_dre_contratos';
                              begin
                                    delete from tb_bi_pms_dre_contratos t
                                    where dt_mes not between to_date('01-01-2010', 'dd-mm-yyyy') and  to_date('01-12-2011', 'dd-mm-yyyy');
                                    commit;
                              exception
                                  when others then
                                 status   := 'ERRO';
                                 assunto  := '[ERRO]';
                                 sqlerro  := sqlerrm;
                                 mensagem := mensagem || crlf || crlf ||
                                             '[ERRO] ##### PMS.tb_bi_pms_dre_contratos (deletar registros <> 2011) ##### - ' || crlf ||
                                             '       SQL Erro: ' || sqlerrm || crlf ||
                                             '       Data Execucao: ' ||
                                             to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                 --envio alerta SMS INFRA
                                 sms_msg := sms_msg || 'PMS.tb_bi_pms_dre_contratos; ERRO: ' ||
                                            sqlerro || '; Data: ' ||
                                            to_char(sysdate, 'DD/MM/YYYY');
                     --            send_mail('noc-cit-sms@cit.com.br',
                     --                      assunto || '[PMS][' || nome_carga ||
                     --                      '] Resultado da Carga',
                     --                      sms_msg);
                                 --limpa msg
                                 sms_msg := 'Objeto: ';
                              end;
                              if (status is null) then
                                        begin
                                          insert into tb_bi_pms_dre_contratos
                                             select *
                                             from   vw_bi_pms_dre_contrato;
                                          commit;
                                          --
                                          select count(*)
                                          into   contador2
                                          from   tb_bi_pms_dre_contratos
                                          where  dt_mes <= (select modu_dt_fechamento
                                                            from   modulo
                                                            where  modu_cd_modulo = 2);
                                          -- Info LOG
                                          descricao := 'FIM    - Linhas: ' || to_char(contador2);
                                          -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                                          if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                                             status   := 'ERRO';
                                             assunto  := '[ERRO%]';
                                             sqlerro  := 'Variacao pocentagem: ' ||
                                                         round(((contador2 / contador1) * 100), 2) || '%';
                                             mensagem := mensagem || crlf || crlf ||
                                                         '[ERRO] ##### PMS.tb_bi_pms_dre_contratos  ##### - ' || crlf ||
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
                                             -- Ja estava comentado, n?o retirar
                                             --envio alerta SMS INFRA
                                             --    sms_msg := sms_msg ||'PMS.tb_bi_pms_dre_contratos; ERRO: '|| sqlerro ||'; Data: '||to_char(sysdate, 'DD/MM/YYYY');
                                             --    send_mail('noc-cit-sms@cit.com.br', assunto||'[PMS]['||NOME_CARGA||'] Resultado da Carga', sms_msg);
                                             --limpa msg
                                             --    sms_msg := 'Objeto: ';
                                          else
                                             status   := 'OK';
                                             mensagem := mensagem || crlf || crlf ||
                                                         '[OK] PMS.tb_bi_pms_dre_contratos' || crlf ||
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
                                             status   := 'ERRO';
                                             assunto  := '[ERRO]';
                                             sqlerro  := sqlerrm;
                                             mensagem := mensagem || crlf || crlf ||
                                                         '[ERRO] ##### PMS.tb_bi_pms_dre_contratos  ##### - ' || crlf ||
                                                         '       SQL Erro: ' || sqlerrm || crlf ||
                                                         '       Data Execucao: ' ||
                                                         to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                             --envio alerta SMS INFRA
                                             sms_msg := sms_msg || 'PMS.tb_bi_pms_dre_contratos; ERRO: ' ||
                                                        sqlerro || '; Data: ' ||
                                                        to_char(sysdate, 'DD/MM/YYYY');
                                 --            send_mail('noc-cit-sms@cit.com.br',
                                 --                      assunto || '[PMS][' || nome_carga ||
                                 --                      '] Resultado da Carga',
                                 --                      sms_msg);
                                             --limpa msg
                                             sms_msg := 'Objeto: ';
                                       end;
                            end if;
                           exception
                              when others then
                                 status   := 'ERRO';
                                 assunto  := '[ERRO]';
                                 sqlerro  := sqlerrm;
                                 mensagem := mensagem || crlf || crlf ||
                                             '[ERRO] ##### PMS.tb_bi_pms_dre_contratos  ##### - ' || crlf ||
                                             '       SQL Erro: ' || sqlerrm || crlf ||
                                             '       Data Execucao: ' ||
                                             to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                 --envio alerta SMS INFRA
                                 sms_msg := sms_msg || 'PMS.tb_bi_pms_dre_contratos; ERRO: ' ||
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
                        ------------------------------------------------------------------------------------------
                        --Tipo da Carga: 2 - Tabela: tb_bi_pms_dre_servicos - WB: PMS-ProfitLoss / PMS-ProfitLoss-Login / PMS-LOB-ProfitLoss / PMS-UMKT-ProfitLoss
                        ------------------------------------------------------------------------------------------
                        --limpar para proximo registro
                        sqlerro := '';
                         status := '';
                        dbms_lock.sleep(1);
                        if (flag = 2 or flag = 99) then
                           mensagem              := mensagem || crlf || crlf ||
                                                    'Tipo da Carga: 2 - Tabela: tb_bi_pms_dre_servicos - WB: PMS-ProfitLoss / PMS-ProfitLoss-Login / PMS-LOB-ProfitLoss / PMS-UMKT-ProfitLoss';
                           log_dt_carga_processo := sysdate;
                           bloco_upd             := 'tb_bi_pms_dre_servicos (flag 2 de 5)';
                           begin
                              select count(*)
                              into   contador1
                              from   tb_bi_pms_dre_servicos
                              where  dt_mes <= (select modu_dt_fechamento
                                                from   modulo
                                                where  modu_cd_modulo = 2);
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
                     --         delete from pms_bi.tb_bi_pms_dre_servicos_log;
                              commit;/*
                              --
                              insert into tb_bi_pms_dre_servicos_log
                                 select c.*,
                                        sysdate data_carga
                                 from   tb_bi_pms_dre_servicos c;
                              commit;*/
                              --
                              --execute immediate 'truncate table tb_bi_pms_dre_servicos';
                              begin
                                    delete from TB_BI_PMS_DRE_SERVICOS t
                                    where dt_mes not between to_date('01-01-2010', 'dd-mm-yyyy') and  to_date('01-12-2011', 'dd-mm-yyyy');
                                    commit;
                              exception
                                  when others then
                                 status   := 'ERRO';
                                 assunto  := '[ERRO]';
                                 sqlerro  := sqlerrm;
                                 mensagem := mensagem || crlf || crlf ||
                                             '[ERRO] ##### PMS.tb_bi_pms_dre_servicos (deletar registros <> 2011) ##### - ' || crlf ||
                                             '       SQL Erro: ' || sqlerrm || crlf ||
                                             '       Data Execucao: ' ||
                                             to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                 --envio alerta SMS INFRA
                                 sms_msg := sms_msg || 'PMS.tb_bi_pms_dre_servicos; ERRO: ' ||
                                            sqlerro || '; Data: ' ||
                                            to_char(sysdate, 'DD/MM/YYYY');
                     --            send_mail('noc-cit-sms@cit.com.br',
                     --                      assunto || '[PMS][' || nome_carga ||
                     --                      '] Resultado da Carga',
                     --                      sms_msg);
                                 --limpa msg
                                 sms_msg := 'Objeto: ';
                              end;
                              if (status is null) then
                                          begin
                                             insert into tb_bi_pms_dre_servicos
                                                select *
                                                from   vw_bi_pms_dre_servicos s
                                                where s.flag = 1;
                                             commit;
                                              
                                             insert into tb_bi_pms_dre_servicos
                                                select *
                                                from   vw_bi_pms_dre_servicos s
                                                where s.flag = 2;
                                             commit;

                                             insert into tb_bi_pms_dre_servicos
                                                select *
                                                from   vw_bi_pms_dre_servicos s
                                                where s.flag = 3;
                                             commit;

                                             insert into tb_bi_pms_dre_servicos
                                                select *
                                                from   vw_bi_pms_dre_servicos s
                                                where s.flag = 4;
                                             commit;

                                             insert into tb_bi_pms_dre_servicos
                                                select *
                                                from   vw_bi_pms_dre_servicos s
                                                where s.flag = 5;
                                             commit;
                                                
                                             select count(*)
                                             into   contador2
                                             from   tb_bi_pms_dre_servicos
                                             where  dt_mes <= (select modu_dt_fechamento
                                                               from   modulo
                                                               where  modu_cd_modulo = 2);
                                             -- Info para LOG
                                             descricao := 'FIM    - Linhas: ' || to_char(contador2);
                                             -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                                             if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                                                status   := 'ERRO';
                                                assunto  := '[ERRO%]';
                                                sqlerro  := 'Variacao pocentagem: ' ||
                                                            round(((contador2 / contador1) * 100), 2) || '%';
                                                mensagem := mensagem || crlf || crlf ||
                                                            '[ERRO] ##### PMS.tb_bi_pms_dre_servicos  ##### - ' || crlf ||
                                                            '       Variacao Porcentagem: ' ||
                                                            round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                                            '       Linhas antes do Refresh: ' ||
                                                            to_char(contador1) || crlf ||
                                                            '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                                            '       Diferenca: ' ||
                                                            to_char(contador2 - contador1) || crlf ||
                                                           --         'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                                            '       Data Execuc?o: ' ||
                                                            to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                                -- ja estava assim n?o retirar comentario
                                                --envio alerta SMS INFRA
                                                --sms_msg := sms_msg ||'PMS.tb_bi_pms_dre_servicos; ERRO: '|| sqlerro ||'; Data: '||to_char(sysdate, 'DD/MM/YYYY');
                                                --send_mail('noc-cit-sms@cit.com.br', assunto||'[PMS]['||NOME_CARGA||'] Resultado da Carga', sms_msg);
                                                --limpa msg
                                                --sms_msg := 'Objeto: ';
                                             else
                                                status   := 'OK';
                                                mensagem := mensagem || crlf || crlf ||
                                                            '[OK] PMS.tb_bi_pms_dre_servicos' || crlf ||
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
                                                status   := 'ERRO';
                                                assunto  := '[ERRO]';
                                                sqlerro  := sqlerrm;
                                                mensagem := mensagem || crlf || crlf ||
                                                            '[ERRO] ##### PMS.tb_bi_pms_dre_servicos  ##### - ' || crlf ||
                                                            '       SQL Erro: ' || sqlerrm || crlf ||
                                                            '       Data Execuc?o: ' ||
                                                            to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                                --envio alerta SMS INFRA
                                                sms_msg := sms_msg || 'PMS.tb_bi_pms_dre_servicos; ERRO: ' ||
                                                           sqlerro || '; Data: ' ||
                                                           to_char(sysdate, 'DD/MM/YYYY');
                                    --            send_mail('noc-cit-sms@cit.com.br',
                                    --                      assunto || '[PMS][' || nome_carga ||
                                    --                      '] Resultado da Carga',
                                    --                      sms_msg);
                                                --limpa msg
                                                sms_msg := 'Objeto: ';
                                          end;
                                          --commit;
                            end if;
                              --Inserir LOG
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
                        -----------------------------------------------------------------------------------------
                        --  'Tipo da Carga: 3 - Tabela: tb_bi_pms_dre_umkts - WB: PMS-UMKT-ProfitLoss';
                        -----------------------------------------------------------------------------------------
                        --limpar para proximo registro
                        sqlerro := '';
                        status := '';
                        dbms_lock.sleep(1);
/*                        if (flag = 3 or flag = 99) then
                           mensagem              := mensagem || crlf || crlf ||
                                                    'Tipo da Carga: 3 - Tabela: tb_bi_pms_dre_umkts - WB: PMS-UMKT-ProfitLoss';
                           log_dt_carga_processo := sysdate;
                           bloco_upd             := 'tb_bi_pms_dre_umkts (flag 3 de 5)';
                           begin
                              select count(*)
                              into   contador1
                              from   tb_bi_pms_dre_umkts
                              where  dt_mes <= (select modu_dt_fechamento
                                                from   modulo
                                                where  modu_cd_modulo = 2);
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
                     \*         delete from tb_bi_pms_dre_umkts_log;
                              commit;
                              insert into tb_bi_pms_dre_umkts_log
                                 select c.*,
                                        sysdate data_carga
                                 from   tb_bi_pms_dre_umkts c;
                              commit;*\
                              --
                              --execute immediate 'truncate table tb_bi_pms_dre_umkts';
                              begin
                                    delete from tb_bi_pms_dre_umkts t
                                    where dt_mes not between to_date('01-01-2010', 'dd-mm-yyyy') and  to_date('01-12-2010', 'dd-mm-yyyy');
                                    commit;
                              exception
                                  when others then
                                 status   := 'ERRO';
                                 assunto  := '[ERRO]';
                                 sqlerro  := sqlerrm;
                                 mensagem := mensagem || crlf || crlf ||
                                             '[ERRO] ##### PMS.tb_bi_pms_dre_umkts (deletar registros <> 2010) ##### - ' || crlf ||
                                             '       SQL Erro: ' || sqlerrm || crlf ||
                                             '       Data Execucao: ' ||
                                             to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                 --envio alerta SMS INFRA
                                 sms_msg := sms_msg || 'PMS.tb_bi_pms_dre_umkts; ERRO: ' ||
                                            sqlerro || '; Data: ' ||
                                            to_char(sysdate, 'DD/MM/YYYY');
                     --            send_mail('noc-cit-sms@cit.com.br',
                     --                      assunto || '[PMS][' || nome_carga ||
                     --                      '] Resultado da Carga',
                     --                      sms_msg);
                                 --limpa msg
                                 sms_msg := 'Objeto: ';
                              end;
                              if (status is null) then
                                      begin
                                          insert into tb_bi_pms_dre_umkts
                                             select *
                                             from   vw_bi_pms_dre_umkts;
                                          commit;
                                          --
                                          select count(*)
                                          into   contador2
                                          from   tb_bi_pms_dre_umkts
                                          where  dt_mes <= (select modu_dt_fechamento
                                                            from   modulo
                                                            where  modu_cd_modulo = 2);
                                          -- Info para LOG
                                          descricao := 'FIM    - Linhas: ' || to_char(contador2);
                                          -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                                          if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                                             status   := 'ERRO';
                                             assunto  := '[ERRO%]';
                                             sqlerro  := 'Variacao pocentagem: ' ||
                                                         round(((contador2 / contador1) * 100), 2) || '%';
                                             mensagem := mensagem || crlf || crlf ||
                                                         '[ERRO] ##### PMS.tb_bi_pms_dre_umkts  ##### - ' || crlf ||
                                                         '       Variacao Porcentagem: ' ||
                                                         round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                                         '       Linhas antes do Refresh: ' ||
                                                         to_char(contador1) || crlf ||
                                                         '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                                         '       Diferenca: ' ||
                                                         to_char(contador2 - contador1) || crlf ||
                                                         '       Data Execuc?o: ' ||
                                                         to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                             -- ja estava comentado - n?o retirar comentario
                                             --envio alerta SMS INFRA
                                             --sms_msg := sms_msg ||'PMS.tb_bi_pms_dre_umkts; ERRO: '|| sqlerro ||'; Data: '||to_char(sysdate, 'DD/MM/YYYY');
                                             --send_mail('noc-cit-sms@cit.com.br', assunto||'[PMS]['||NOME_CARGA||'] Resultado da Carga', sms_msg);
                                             --limpa msg
                                             --sms_msg := 'Objeto: ';
                                          else
                                             status   := 'OK';
                                             mensagem := mensagem || crlf || crlf ||
                                                         '[OK] PMS.tb_bi_pms_dre_umkts' || crlf ||
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
                                             status   := 'ERRO';
                                             sqlerro  := sqlerrm;
                                             assunto  := '[ERRO]';
                                             mensagem := mensagem || crlf || crlf ||
                                                         '[ERRO] ##### PMS.tb_bi_pms_dre_umkts  ##### - ' || crlf ||
                                                         '       SQL Erro: ' || sqlerrm || crlf ||
                                                         '       Data Execuc?o: ' ||
                                                         to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                             --envio alerta SMS INFRA
                                             sms_msg := sms_msg || 'PMS.tb_bi_pms_dre_umkts; ERRO: ' ||
                                                        sqlerro || '; Data: ' ||
                                                        to_char(sysdate, 'DD/MM/YYYY');
                                 --            send_mail('noc-cit-sms@cit.com.br',
                                 --                      assunto || '[PMS][' || nome_carga ||
                                 --                      '] Resultado da Carga',
                                 --                      sms_msg);
                                             --limpa msg
                                             sms_msg := 'Objeto: ';
                                       end;
                                       commit;
                                end if;
                                 --Inserir LOG
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
                        end if;*/
                        ------------------------------------------------------------------------------------------
                        --  'Tipo da Carga: 4 - Tabela: tb_bi_pms_dre_praticas - WB: PMS-LOB-ProfitLoss';
                        ------------------------------------------------------------------------------------------
                        sqlerro := '';
                        status := '';
                        dbms_lock.sleep(1);
/*                        if (flag = 4 or flag = 99) then
                           mensagem              := mensagem || crlf || crlf ||
                                                    'Tipo da Carga: 4 - Tabela: tb_bi_pms_dre_praticas - WB: PMS-LOB-ProfitLoss';
                           log_dt_carga_processo := sysdate;
                           bloco_upd             := 'tb_bi_pms_dre_praticas (flag 4 de 5)';
                           begin
                              --         select count(*)         into   contador1          from   ;
                              select count(*)
                              into   contador1
                              from   tb_bi_pms_dre_praticas
                              where  dt_mes <= (select modu_dt_fechamento
                                                from   modulo
                                                where  modu_cd_modulo = 2);
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
                     \*         delete from tb_bi_pms_dre_praticas_log;
                              commit;
                              insert into tb_bi_pms_dre_praticas_log
                                 select c.*,
                                        sysdate data_carga
                                 from   tb_bi_pms_dre_praticas c;
                              commit;*\
                              --
                            --  execute immediate 'truncate table tb_bi_pms_dre_praticas';
                              begin
                                  delete from tb_bi_pms_dre_praticas t
                                  where dt_mes not between to_date('01-01-2010', 'dd-mm-yyyy') and  to_date('01-12-2010', 'dd-mm-yyyy');
                                    commit;
                              exception
                                  when others then
                                 status   := 'ERRO';
                                 assunto  := '[ERRO]';
                                 sqlerro  := sqlerrm;
                                 mensagem := mensagem || crlf || crlf ||
                                             '[ERRO] ##### PMS.tb_bi_pms_dre_praticas (deletar registros <> 2010) ##### - ' || crlf ||
                                             '       SQL Erro: ' || sqlerrm || crlf ||
                                             '       Data Execucao: ' ||
                                             to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                 --envio alerta SMS INFRA
                                 sms_msg := sms_msg || 'PMS.tb_bi_pms_dre_praticas; ERRO: ' ||
                                            sqlerro || '; Data: ' ||
                                            to_char(sysdate, 'DD/MM/YYYY');
                     --            send_mail('noc-cit-sms@cit.com.br',
                     --                      assunto || '[PMS][' || nome_carga ||
                     --                      '] Resultado da Carga',
                     --                      sms_msg);
                                 --limpa msg
                                 sms_msg := 'Objeto: ';
                              end;
                              if (status is null) then
                                      begin
                                          insert into tb_bi_pms_dre_praticas
                                             select *
                                             from   vw_bi_pms_dre_praticas;
                                          commit;
                                          --
                                          select count(*)
                                          into   contador2
                                          from   tb_bi_pms_dre_praticas
                                          where  dt_mes <= (select modu_dt_fechamento
                                                            from   modulo
                                                            where  modu_cd_modulo = 2);
                                          -- Info para LOG
                                          descricao := 'FIM    - Linhas: ' || to_char(contador2);
                                          -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                                          if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                                             status   := 'ERRO';
                                             assunto  := '[ERRO%]';
                                             sqlerro  := 'Variacao pocentagem: ' ||
                                                         round(((contador2 / contador1) * 100), 2) || '%';
                                             mensagem := mensagem || crlf || crlf ||
                                                         '[ERRO] ##### PMS.tb_bi_pms_dre_praticas  ##### - ' || crlf ||
                                                         '       Variacao Porcentagem: ' ||
                                                         round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                                         '       Linhas antes do Refresh: ' ||
                                                         to_char(contador1) || crlf ||
                                                         '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                                         '       Diferenca: ' ||
                                                         to_char(contador2 - contador1) || crlf ||
                                                         '       Data Execuc?o: ' ||
                                                         to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                             -- ja estava comentado, n?o retirar comentario
                                             --envio alerta SMS INFRA
                                             --sms_msg := sms_msg ||'PMS.tb_bi_pms_dre_praticas; ERRO: '|| sqlerro ||'; Data: '||to_char(sysdate, 'DD/MM/YYYY');
                                             --send_mail('noc-cit-sms@cit.com.br', assunto||'[PMS]['||NOME_CARGA||'] Resultado da Carga', sms_msg);
                                             --limpa msg
                                             --sms_msg := 'Objeto: ';
                                          else
                                             status   := 'OK';
                                             mensagem := mensagem || crlf || crlf ||
                                                         '[OK] PMS.tb_bi_pms_dre_praticas' || crlf ||
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
                                             status   := 'ERRO';
                                             sqlerro  := sqlerrm;
                                             mensagem := mensagem || crlf || crlf ||
                                                         '[ERRO] ##### PMS.tb_bi_pms_dre_praticas  ##### - ' || crlf ||
                                                         '       SQL Erro: ' || sqlerrm || crlf ||
                                                         '       Data Execuc?o: ' ||
                                                         to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                             assunto  := '[ERRO]';
                                             --envio alerta SMS INFRA
                                             sms_msg := sms_msg || 'PMS.tb_bi_pms_dre_praticas; ERRO: ' ||
                                                        sqlerro || '; Data: ' ||
                                                        to_char(sysdate, 'DD/MM/YYYY');
                                 --            send_mail('noc-cit-sms@cit.com.br',
                                 --                      assunto || '[PMS][' || nome_carga ||
                                 --                      '] Resultado da Carga',
                                 --                      sms_msg);
                                             --limpa msg
                                             sms_msg := 'Objeto: ';
                                       end;
                            end if;
                           --Inserir LOG
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
                        end if;*/
                        --------------------------------------------------------------------------------
                        --    'Tipo da Carga: 5 - Tabela: tb_bi_consolida_dres - WB: PMS-ProfitLoss';
                        --------------------------------------------------------------------------------
                        sqlerro := '';
                        status := '';
                        dbms_lock.sleep(1);
                        if (flag = 55555 or flag = 999) then
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
                                                where  modu_cd_modulo = 2);
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
                              /* delete from bi_pms.tb_bi_pms_consolida_dres_log;
                              commit;
                              insert into bi_pms.tb_bi_pms_consolida_dres_log
                                 select   ORIGEM                         ,
                                          CD_CONTRATO_PRATICA            ,
                                          NOME_CONTRATO_PRATICA          ,
                                          DT_MES                         ,
                                          UMKT                           ,
                                          SSO                            ,
                                          LOB_CONTRATO_PRATICA           ,
                                          BM                             ,
                                          CATEGORIA                      ,
                                          MOEDA                          ,
                                          PRATICA                        ,
                                          LOGIN                          ,
                                          LOGIN_MGR                      ,
                                          CD_GRUPO_CUSTO                 ,
                                          NOME_GRUPO_CUSTO               ,
                                          NOME_AREA                      ,
                                          CONT_CD_CONTRATO               ,
                                          CLIE_CD_CLIENTE                ,
                                          CD_MOEDA_REVENUE               ,
                                          VALOR_RECEITA_CURRENCY         ,
                                          VALOR_RECEITA_BRL              ,
                                          VALOR_FTE_REVENUE              ,
                                          VALOR_COTACAO_REVENUE          ,
                                          VALOR_UT_RATE                  ,
                                          VALOR_FTE_UT_RATE              ,
                                          VALOR_IMPOSTO_ORIGINAL         ,
                                          VALOR_IMPOSTO_CONVERTIDO       ,
                                          CD_MOEDA_REV_OUT_SOURC         ,
                                          VALOR_REV_OUT_SOURC            ,
                                          VALOR_REV_OUT_SOURC_BRL        ,
                                          VALOR_COTACAO_OUT_SOURCE       ,
                                          CD_MOEDA_REV_OUT_ALLOC         ,
                                          VALOR_REV_OUT_ALLOC            ,
                                          VALOR_REV_OUT_ALLOC_BRL        ,
                                          VALOR_COTACAO_OUT_ALLOC        ,
                                          VALOR_DESP_REL_CURR            ,
                                          VALOR_DESP_REL_BRL             ,
                                          VALOR_DESPFIX_RATUMKT_CURR     ,
                                          VALOR_DESPFIX_RATUMKT_BRL      ,
                                          VLR_DESPFIX_LOB_CURR           ,
                                          VLR_DESPFIX_LOB_BRL            ,
                                          VLR_DESP_CONTAB_INTER          ,
                                          VLR_DESP_CONTAB_BRL            ,
                                          VLR_QUALITY_COSTS_BRL          ,
                                          VALOR_DESP_PLAN_CURR           ,
                                          VALOR_DESP_PLAN_BRL            ,
                                          TIPO_LICENCA                   ,
                                          CD_MOEDA_CDIR                  ,
                                          VALOR_CUSTO_DIR_TCE_CURR       ,
                                          VALOR_CUSTO_DIR_TCE_BRL        ,
                                          VALOR_CUSTO_DIR_INFRA_CURR     ,
                                          VALOR_CUSTO_DIR_INFRA_BRL      ,
                                          VALOR_FTE_CDIR                 ,
                                          VALOR_FTE_FERIAS_CDIR          ,
                                          VALOR_FTE_LICENCA              ,
                                          VALOR_COTACAO_CDIR             ,
                                          CD_MOEDA_CIND                  ,
                                          VALOR_CUSTO_IND_TCE_CURR       ,
                                          VALOR_CUSTO_IND_TCE_BRL        ,
                                          VALOR_CUSTO_IND_INFRA_CURR     ,
                                          VALOR_CUSTO_IND_INFRA_BRL      ,
                                          VALOR_FTE_CIND                 ,
                                          VALOR_FTE_FERIAS_CIND          ,
                                          VALOR_COTACAO_CIND             ,
                                          VLR_VACATION_FTE               ,
                                          CD_TI_RECURSO                  ,
                                          NOME_TI_RECURSO                ,
                                          VALOR_CUSTO_UNIT_TI_REC        ,
                                          QUANT_TI_RECURSOS              ,
                                          VALOR_CUSTO_ITEM               ,
                                          CD_MOEDA_CUSTLIC_RATUMKT       ,
                                          VALOR_CUSTLIC_RATEIO_UMKT      ,
                                          VALOR_CUSTLIC_RATEIO_UMKT_BRL  ,
                                          VALOR_COTA_CUSTLIC_RATUMKT     ,
                                          CD_MOEDA_CUSTLIC_RATPRAT       ,
                                          VALOR_CUSTLIC_RATEIO_PRAT      ,
                                          VALOR_CUSTLIC_RATPRAT_BRL      ,
                                          VALOR_COTA_CUSTLIC_RATPRAT     ,
                                          VALOR_HORA_EXTRA               ,
                                          VALOR_PLANTAO                  ,
                                          CD_MOEDA_CUSTO_OUT_SOURC       ,
                                          VALOR_CUSTO_OUT_SOURC          ,
                                          VALOR_CUSTO_OUT_SOURC_BRL      ,
                                          VALOR_COTA_CUSTO_OUT_SOURC     ,
                                          CD_MOEDA_CUSTO_OUT_ALLOC       ,
                                          VALOR_CUSTO_OUT_ALOC           ,
                                          VALOR_CUSTO_OUT_ALLOC_BRL      ,
                                          VALOR_COTA_CUSTO_OUT_ALLOC     ,
                                          CD_MOEDA_LOB_FIXCOST           ,
                                          VLR_CUST_RAT_LOB_FIXCOST_BRL   ,
                                          PERC_RECEITA_LOB_FIXCOST       ,
                                          VLR_REC_LOB_FIXCOST_CURR       ,
                                          VLR_REC_LOB_FIXCOST_BRL        ,
                                          VLR_REC_CLOB_LOBFIXCOST_CURR   ,
                                          VLR_REC_CLOB_LOBFIXCOST_BRL    ,
                                          VLR_TOT_FTE_LOB_FIXCOST        ,
                                          CD_MOEDA_CUSTFIX_RATPRAT       ,
                                          VALOR_CUSTFIX_RATEIO_PRAT      ,
                                          VALOR_TCE_CUSTFIX_RATPRAT      ,
                                          VALOR_CUSTINFRA_FIX_RATPRAT    ,
                                          VALOR_CUSTFIX_RATPRAT_BRL      ,
                                          VALOR_TCE_CUSTFIX_RATPRAT_BRL  ,
                                          VALOR_CUSINFRA_FIX_RATPRAT_BRL ,
                                          VALOR_CUSTFIX_LOB_FERIAS       ,
                                          VALOR_FTE_CUSTFIX_RATPRAT      ,
                                          VLR_FTE_CUSFIX_FERIAS_RATPRAT  ,
                                          VALOR_COTA_CUSTFIX_RATPRAT     ,
                                          CD_MOEDA_CUSTFIX_RATUMKT       ,
                                          VALOR_CUSTFIX_RATEIO_UMKT      ,
                                          VALOR_TCE_CUSTFIX_RATUMKT      ,
                                          VALOR_CUSTINFRA_FIX_RATUMKT    ,
                                          VALOR_CUSTFIX_RATEIO_UMKT_BRL  ,
                                          VALOR_TCE_CUSTFIX_RATUMKT_BRL  ,
                                          VALOR_CUSTINF_FIX_RATUMKT_BRL  ,
                                          VALOR_CUSTFIX_UMKT_FERIAS      ,
                                          VALOR_FTE_CUSTFIX_RATUMKT      ,
                                          VLR_FTE_CUSFIX_FERIAS_RATUMKT  ,
                                          VALOR_COTA_CUSTFIX_RATUMKT     ,
                                          VLR_TECSAL_CDIR_TCE_CURR       ,
                                          VLR_TECSAL_CDIR_TCE_BRL        ,
                                          VLR_TECSAL_CDIR_INFRA_CURR     ,
                                          VLR_TECSAL_CDIR_INFRA_BRL      ,
                                          VLR_TECSAL_CIND_TCE_CURR       ,
                                          VLR_TECSAL_CIND_TCE_BRL        ,
                                          VLR_TECSAL_CIND_INFRA_CURR     ,
                                          VLR_TECSAL_CIND_INFRA_BRL      ,
                                          TECSAL_PR_FERIAS               ,
                                          VLR_TECSAL_CUSTLIC_CURR        ,
                                          VLR_TECSAL_CUSTLIC_BRL         ,
                                          TECSAL_PR_ALOC_LICENCA         ,
                                          VLR_TECSAL_HORA_EXTRA          ,
                                          VLR_TECSAL_PLANTAO             ,
                                          VLR_TECSAL_DESP_CURR           ,
                                          VLR_TECSAL_DESP_BRL            ,
                                          VL_COMISS_ACEL_CURR            ,
                                          VL_COMISS_ACEL_BRL             ,
                                          VL_COMISS_INV_CURR             ,
                                          VL_COMISS_INV_BRL              ,
                                          VLR_CUSTRAT_MKT_CURR           ,
                                          VLR_CUSTRAT_MKT_BRL            ,
                                          VLR_CUSTRAT_DN_CURR            ,
                                          VLR_CUSTRAT_DN_BRL             ,
                                          VLR_CUSTRAT_AE_CURR            ,
                                          VLR_CUSTRAT_AE_BRL             ,
                                          VLR_CUST_COLAB_MKT_CURR        ,
                                          VLR_CUST_COLAB_MKT_BRL         ,
                                          VLR_CUST_COLAB_DN_CURR         ,
                                          VLR_CUST_COLAB_DN_BRL          ,
                                          VLR_CUST_COLAB_AE_CURR         ,
                                          VLR_CUST_COLAB_AE_BRL          ,
                                          VLR_DESPRAT_MKT_TRAV_CURR      ,
                                          VALOR_DESPRAT_DN_TRAV_CURR     ,
                                          VALOR_DESPRAT_AE_TRAV_CURR     ,
                                          VLR_DESPRAT_MKT_OTH_CURR       ,
                                          VALOR_DESPRAT_DN_OTH_CURR      ,
                                          VALOR_DESPRAT_AE_OTH_CURR      ,
                                          VLR_DESPRAT_MKT_TRAV_BRL       ,
                                          VALOR_DESPRAT_DN_TRAV_BRL      ,
                                          VALOR_DESPRAT_AE_TRAV_BRL      ,
                                          VLR_DESPRAT_MKT_OTH_BRL        ,
                                          VALOR_DESPRAT_DN_OTH_BRL       ,
                                          VALOR_DESPRAT_AE_OTH_BRL       ,
                                          VLR_DESP_MKT_TRAVEL_CURR       ,
                                          VLR_DESP_MKT_TRAVEL_BRL        ,
                                          VLR_DESP_MKT_OTHERS_CURR       ,
                                          VLR_DESP_MKT_OTHERS_BRL        ,
                                          VLR_DESP_DN_TRAVEL_CURR        ,
                                          VLR_DESP_DN_TRAVEL_BRL         ,
                                          VLR_DESP_DN_OTHERS_CURR        ,
                                          VLR_DESP_DN_OTHERS_BRL         ,
                                          VLR_DESP_AE_TRAVEL_CURR        ,
                                          VLR_DESP_AE_TRAVEL_BRL         ,
                                          VLR_DESP_AE_OTHERS_CURR        ,
                                          VLR_DESP_AE_OTHERS_BRL         ,
                                          VALOR_TCE_ADMIN                ,
                                          VALOR_CINFRA_BASE_ADMIN        ,
                                          VALOR_CUSTO_ADMIN_TCE_CUR      ,
                                          VALOR_CUSTO_ADMIN_TCE_BRL      ,
                                          VALOR_CUSTO_ADMIN_INFRA_CUR    ,
                                          VALOR_CUSTO_ADMIN_INFRA_BRL    ,
                                          PCT_CUSTO_ADMIN                ,
                                          VALOR_CUSTO_DIR_TCE_INOV_CURR  ,
                                          VALOR_CUSTO_DIR_TCE_INOV_BRL   ,
                                          VALOR_CDIR_INFRA_INOV_CURR     ,
                                          VALOR_CDIR_INFRA_INOV_BRL      ,
                                          VALOR_CUSTO_IND_TCE_INOV_CURR  ,
                                          VALOR_CUSTO_IND_TCE_INOV_BRL   ,
                                          VALOR_CIND_INFRA_INOV_CURR,
                                          VALOR_CIND_INFRA_INOV_BRL,
                                          VALOR_HORA_EXTRA_ADMIN,
                                          VALOR_PLANTAO_ADMIN,
                                          VALOR_CUSTLIC_ADMIN_CURR,
                                          VALOR_CUSTLIC_ADMIN_BRL,
                                          VALOR_PCT_FERIAS_ADMIN,
                                          VALOR_PCT_LICENCA_ADMIN,
                                          VALOR_PCT_ALOC_INOV_ADMIN,
                                          VALOR_PCT_ALOC_NINOV_ADMIN,
                                          VALOR_FTE_CIND_ADMIN,
                                          VALOR_DESPESA_ADMIN_CURR,
                                          VALOR_DESPESA_ADMIN_BRL,
                                          trunc(sysdate),
                                          VALOR_DESPESA_CLOB_CURR,
                                          VALOR_DESPESA_CG_CURR,
                                          VALOR_DESPESA_CLOB_BRL,
                                          VALOR_DESPESA_CG_BRL
                                 from   tb_bi_pms_consolida_dres c;
                              commit;*/
                              --
                              --
                             -- execute immediate 'truncate table tb_bi_pms_consolida_dres';
                              --
                              --
                              begin
                                  delete from tb_bi_pms_consolida_dres t
                                  where dt_mes not between to_date('01-01-2010', 'dd-mm-yyyy') and  to_date('01-12-2011', 'dd-mm-yyyy');
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
                     --            send_mail('noc-cit-sms@cit.com.br',
                     --                      assunto || '[PMS][' || nome_carga ||
                     --                      '] Resultado da Carga',
                     --                      sms_msg);
                                 --limpa msg
                                 sms_msg := 'Objeto: ';
                              end;
                              if (status is null) then
                                      begin 
                                             insert into tb_bi_pms_consolida_dres 
                                                 select *  from   vw_bi_pms_consolida_dres_q1;
                                              --commit;
                                              
                                             insert into tb_bi_pms_consolida_dres 
                                                 select *  from   vw_bi_pms_consolida_dres_q2;
                                              --commit;

                                             insert into tb_bi_pms_consolida_dres 
                                                 select *  from   vw_bi_pms_consolida_dres_q3;
                                              --commit;

                                             insert into tb_bi_pms_consolida_dres 
                                                 select *  from   vw_bi_pms_consolida_dres_q4;
                                              --commit;

                                             insert into tb_bi_pms_consolida_dres 
                                                 select *  from   vw_bi_pms_consolida_dres_q5;
                                              --commit;

                                             insert into tb_bi_pms_consolida_dres 
                                                 select *  from   vw_bi_pms_consolida_dres_q6;
                                              --commit;
                                              
                                             insert into tb_bi_pms_consolida_dres 
                                                 select *  from   vw_bi_pms_consolida_dres_q7;
                                              --commit;

                                             insert into tb_bi_pms_consolida_dres 
                                                 select *  from   vw_bi_pms_consolida_dres_q8;
                                              --commit;

                                             insert into tb_bi_pms_consolida_dres 
                                                 select *  from   vw_bi_pms_consolida_dres_q9;
                                              
                                              
                                              commit;
                                                    
                                                --select *  from   vw_bi_pms_consolida_dres; 
                                                
                                            
                                             --
                                             select count(*)
                                             into   contador2
                                             from   tb_bi_pms_consolida_dres
                                             where  dt_mes <= (select modu_dt_fechamento
                                                               from   modulo
                                                               where  modu_cd_modulo = 2);
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
         
                      /*if ( flag = 6 or flag = 99 ) then
                        insert into bi_pms.tb_bi_pms_foto_ferias_licenca
                        select cd_pessoa,
                               login,
                               tipo_licenca,
                               dt_inicio_licenca,
                               dt_fim_licenca,
                               dt_mes,
                               pct_licenca,
                               trunc(sysdate) data_atualizacao
                        from ( select ao.pess_cd_pessoa              cd_pessoa,
                                      p.pess_cd_login                login,
                                      ao.tiov_cd_tipo_overhead       tipo_licenca,
                                      ao.alov_dt_inicio              dt_inicio_licenca,
                                      ao.alov_dt_fim                 dt_fim_licenca,
                                      apo.alpo_dt_aloc_periodo_oh    dt_mes,
                                      ----------------------------------------------------------------------------------------------
                                      -- Soma os % de licença, por login e mês, para não duplicar informações no resultado final.
                                      ----------------------------------------------------------------------------------------------
                                      sum( apo.alpo_pr_aloc_periodo_oh ) over( partition by ao.pess_cd_pessoa, apo.alpo_dt_aloc_periodo_oh ) pct_licenca,
                                      row_number() over( partition by ao.pess_cd_pessoa, apo.alpo_dt_aloc_periodo_oh
                                                         order by     ao.pess_cd_pessoa, apo.alpo_dt_aloc_periodo_oh ) nlinha
                               from pms.alocacao_overhead ao,
                                    pms.alocacao_periodo_oh apo,
                                    pms.pessoa p
                               where ao.alov_cd_alocacao_overhead = apo.alov_cd_alocacao_overhead and
                                     ao.pess_cd_pessoa            = p.pess_cd_pessoa  ) lic
                        where nlinha = 1;
                        --
                        commit;
                    end if;*/
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
                  --send_mail('lahumada@cit.com.br', assunto|| ' [PMS][' || nome_carga || '] Check PMS.PESSOA; LOG PRATICA; LOG UMKT', mensagem);
                  send_mail('alexandrel@cit.com.br', assunto|| ' [PMS][' || nome_carga || '] Check PMS.PESSOA; LOG PRATICA; LOG UMKT', mensagem);
                  send_mail('lnoboru@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check PMS.PESSOA; LOG PRATICA; LOG UMKT', mensagem);         
--                  send_mail('it_oncall@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check PMS.PESSOA; LOG PRATICA; LOG UMKT', mensagem);
                  
                  send_mail('andreiap@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check PMS.PESSOA; LOG PRATICA; LOG UMKT', mensagem);         
                  send_mail('llino@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check PMS.PESSOA; LOG PRATICA; LOG UMKT', mensagem);         
                  send_mail('andreiap@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check PMS.PESSOA; LOG PRATICA; LOG UMKT', mensagem);         
                  
                     sms_msg := sms_msg || 'pms.pessoa - Data Admissao is Null';
         --            send_mail('noc-cit-sms@cit.com.br',assunto || '[PMS][' || nome_carga ||']  Check Data Admissao - PMS.PESSOA',sms_msg);
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
            --send_mail('lahumada@cit.com.br', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
            send_mail('alexandrel@cit.com.br', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
            send_mail('lnoboru@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);          
            
            send_mail('tspadari@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);          
            send_mail('llino@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);          
            send_mail('andreiap@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);          
              
          --  send_mail('it_oncall@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
          --  sms_msg := sms_msg || 'pms.pessoa - Data Admissao is Null';
          --  send_mail('noc-cit-sms@cit.com.br',assunto || '[PMS][' || nome_carga ||']  Check Data Admissao - PMS.PESSOA',sms_msg);
   end if;              
   mensagem := mensagem || crlf || crlf || '=== Fim da Carga ' ||
               to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===';
   mensagem := mensagem || crlf ||
               'select * from pms.TB_BI_PMS_LOG_CARGAS where NOME_CARGA = '''||nome_carga||''' order by 2 desc';
   mensagem := mensagem || crlf || 'FLAG: ' || flag;
   assunto  := assunto || '[PMS]['||nome_carga||'] Resultado Carga';
   send_mail('lnoboru@ciandt.com', assunto, mensagem);   
   --send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@ciandt.com.br', assunto, mensagem);

   send_mail('tspadari@ciandt.com', assunto, mensagem);
   send_mail('llino@ciandt.com', assunto, mensagem);
   send_mail('andreiap@ciandt.com', assunto, mensagem);
   
   

   if status = 'ERRO' then
      mensagem := 'ATTENTION: Please, contact Ciandt IT Business Intelligence Team as soon as possible. ' || crlf || crlf ||
                  mensagem;
--      send_mail('it_oncall@ciandt.com', assunto, mensagem);
   end if;
end USP_PMS_profitloss;