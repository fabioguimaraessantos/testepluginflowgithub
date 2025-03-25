create or replace procedure USP_PMS_umkts(flag number) as
--INTERVAL:trunc(sysdate + 1) + 1.5/24
   -- Procedure de carga
   -- 1 - View de Technical Sales
   --          VW_BI_PMS_TECH_SALES_MAT
   -- 2 - View de Custos Fixos das Consultorias
   --           VW_BI_PMS_CUSTFIX_UMKT_DRE
   -- 3 - View de Custos Fixos dos recursos com intervalo entre admissão e 1a alocação
   --          VW_BI_PMS_CUSTFIX_SAC_DRE_MAT
   -- 4 - Views de Custos Variáveis
   --          VW_BI_PMS_CUSTOS_VAR_MAT
   -- 5 - Views de Despesas Fixas
   --          VW_BI_PMS_DESPFIX_UMKT_DRE_MAT
   -- 6 - Views de Despesas Fixas
   --          VW_BI_PMS_MAT_FIM_RATEIO
   -- 7 - Informações RES
   --          VW_BI_PMS_CUSTOS_RES_OFIC e VW_BI_PMS_INFORMACOES_RES
   -- Informacões para LOG
   log_data              date := trunc(sysdate);
   log_dt_carga_processo date;
   nome_carga            varchar2(50) := '(5) UMKT';
   bloco_upd             varchar2(250) := '';
   descricao             varchar2(200) := '';
   status                varchar2(30) := '';
   sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'USP_PMS_umkts';
   --
   crlf      varchar2(2) := chr(13) || chr(10); -- <ENTER>
   assunto   varchar2(200) := '';
   mensagem  varchar2(3000) := '=== Início da Carga ' ||
                               to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') ||
                               ' ===' || crlf || 'FLAG: ' || flag;
   contador1 integer;
   contador2 integer;
   cod_modulo integer;
   sms_msg   varchar2(200) := 'Objeto: ';
   chk_pessoa integer;
begin
   --------------------------------------------------------
   -- Atualização da view de Technical Sales
   --------------------------------------------------------
   mensagem := mensagem || crlf || ':: PROCEDURE: ' || nome_proc || ' ::';
   send_mail('lnoboru@ciandt.com',  '[PMS]['||nome_carga||'] Start Proc', mensagem);   
   --send_mail('lahumada@cit.com.br', '[PMS]['||nome_carga||'] Start Proc', mensagem);
   send_mail('alexandrel@cit.com.br',  '[PMS]['||nome_carga||'] Start Proc', mensagem);
   
   send_mail('tspadari@ciandt.com',  '[PMS]['||nome_carga||'] Start Proc', mensagem);
   send_mail('llino@ciandt.com',  '[PMS]['||nome_carga||'] Start Proc', mensagem);
   send_mail('andreiap@ciandt.com',  '[PMS]['||nome_carga||'] Start Proc', mensagem);

      
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

  
            if chk_pessoa = 0 then  
               
                     if (flag = 1 or flag = 99) then
                        mensagem := mensagem || crlf || crlf ||
                                    'Tipo da Carga: 1 - View de Technical Sales ';
                        --------------------------------------------
                        --VW_BI_PMS_TECH_SALES_MAT
                        --------------------------------------------
                        begin
                           -- info para log
                           log_dt_carga_processo := sysdate;
                           bloco_upd             := '1 - VW_BI_PMS_TECH_SALES_MAT';
                           --
                           select count(*)
                           into   contador1
                           from   pms.vw_bi_pms_tech_sales_mat
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
                           --
                           --
                           dbms_mview.refresh('"PMS"."VW_BI_PMS_TECH_SALES_MAT"');
                           --
                           --
                           select count(*)
                           into   contador2
                           from   pms.vw_bi_pms_tech_sales_mat
                           where  dt_mes <= (select modu_dt_fechamento
                                             from   modulo
                                             where  modu_cd_modulo = 2);
                           -- Info LOG
                           descricao := 'FIM    - Linhas: ' || to_char(contador2);
                           -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
                           if (contador2 <> 0) then
                                    if (contador2 < contador1) and (round((contador2 / contador1), 2) < 0.99) then
                                       status   := 'ERRO';
                                       assunto  := '[ERRO%]';
                                       sqlerro  := 'Variacao porcentagem: ' ||
                                                   round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[ERRO] ##### PMS.VW_BI_PMS_TECH_SALES_MAT  ##### - ' || crlf ||
                                                   '       '||sqlerro || crlf ||
                                                   '       Linhas antes do Refresh: ' ||
                                                   to_char(contador1) || crlf ||
                                                   '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                                                   '       Diferença: ' ||
                                                   to_char(contador2 - contador1) || crlf ||
                                                  --  'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                                   '       Data Execução: ' ||
                                                   to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                       --envio alerta SMS INFRA
                                       sms_msg := sms_msg || 'PMS.VW_BI_PMS_TECH_SALES_MAT; ERRO: ' ||
                                                  sqlerro || '; Data: ' ||
                                                  to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                       --limpa msg
                                       sms_msg := 'Objeto: ';
                                    else
                                       status   := 'OK';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[OK] PMS.VW_BI_PMS_TECH_SALES_MAT' || crlf ||
                                                   '     Linhas antes do Refresh: ' ||
                                                   to_char(contador1) || crlf ||
                                                   '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                                                   '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                                                   '     Porcentagem: ' ||
                                                   round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                                   '     Data Execução: ' ||
                                                   to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                    end if;
                           else
                                       status   := 'ERRO';
                                       assunto  := '[ERRO%]';
                                       sqlerro  := 'Variacao porcentagem: 100%';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[ERRO] ##### PMS.VW_BI_PMS_TECH_SALES_MAT  ##### - ' || crlf ||
                                                   '       '||sqlerro || crlf ||
                                                   '       Linhas antes do Refresh: ' ||
                                                   to_char(contador1) || crlf ||
                                                   '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                                                   '       Diferença: ' ||
                                                   to_char(contador2 - contador1) || crlf ||
                                                  --  'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                                   '       Data Execução: ' ||
                                                   to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                       --envio alerta SMS INFRA
                                       sms_msg := sms_msg || 'PMS.VW_BI_PMS_TECH_SALES_MAT; ERRO: ' ||
                                                  sqlerro || '; Data: ' ||
                                                  to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                       --limpa msg
                                       sms_msg := 'Objeto: ';
                           end if;
                        exception
                           when others then
                              status   := 'ERRO';
                              assunto  := '[ERRO]';
                              sqlerro  := sqlerrm;
                              mensagem := mensagem || crlf || crlf ||
                                          '[ERRO] ##### PMS.VW_BI_PMS_TECH_SALES_MAT  ##### - ' || crlf ||
                                          '       SQL Erro: ' || sqlerrm || crlf ||
                                          '       Data Execução: ' ||
                                          to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                              --envio alerta SMS INFRA
                              sms_msg := sms_msg || 'PMS.VW_BI_PMS_TECH_SALES_MAT; ERRO: ' ||
                                         sqlerro || '; Data: ' ||
                                         to_char(sysdate, 'DD/MM/YYYY');
         --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
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
                     --  LOG - zerar info
                     sqlerro   := '';
                     contador1 := 0;
                     contador2 := 0;
                     dbms_lock.sleep(1);
                     --
                     -------------------------------------------------------------------------
                     -- Atualização da view de custos fixos das consultorias
                     -------------------------------------------------------------------------
                     if (flag = 2 or flag = 99) then
                        mensagem := mensagem || crlf || crlf ||
                                    'Tipo da Carga: 2 - View de Custos Fixos das Consultorias';
                        --------------------------------------------
                        -- VW_BI_PMS_CUSTFIX_UMKT_DRE
                        --------------------------------------------
                        begin
                           -- Infor LOG
                           log_dt_carga_processo := sysdate;
                           bloco_upd             := '2 - VW_BI_PMS_CUSTFIX_UMKT_DRE';
                           --
                           --
                           select count(*)
                           into   contador1
                           from   pms.vw_bi_pms_custfix_umkt_dre
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
                           --
                           --
                           dbms_mview.refresh('"PMS"."VW_BI_PMS_CUSTFIX_UMKT_DRE"');
                           --
                           --
                           select count(*)
                           into   contador2
                           from   pms.vw_bi_pms_custfix_umkt_dre
                           where  dt_mes <= (select modu_dt_fechamento
                                             from   modulo
                                             where  modu_cd_modulo = 2);
                           -- Info para LOG
                           descricao := 'FIM    - Linhas: ' || to_char(contador2);
                           -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
                           if (contador2 <> 0 ) then
                                    if (contador2 < contador1) and (round((contador2 / contador1), 2) < 0.99) then
                                       status   := 'ERRO';
                                       assunto  := '[ERRO%]';
                                       sqlerro  := 'Variacao porcentagem: ' ||
                                                   round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[ERRO] ##### PMS.VW_BI_PMS_CUSTFIX_UMKT_DRE  ##### - ' || crlf ||
                                                   '       '||sqlerro || crlf ||
                                                   '       Linhas antes do Refresh: ' ||
                                                   to_char(contador1) || crlf ||
                                                   '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                                                   '       Diferença: ' ||
                                                   to_char(contador2 - contador1) || crlf ||
                                                  -- 'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                                   '       Data Execução: ' ||
                                                   to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                       --envio alerta SMS INFRA
                                       sms_msg := sms_msg || 'PMS.VW_BI_PMS_CUSTFIX_UMKT_DRE; ERRO: ' ||
                                                  sqlerro || '; Data: ' ||
                                                  to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                       --limpa msg
                                       sms_msg := 'Objeto: ';
                                    else
                                       status   := 'OK';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[OK] PMS.VW_BI_PMS_CUSTFIX_UMKT_DRE' || crlf ||
                                                   '     Linhas antes do Refresh: ' ||
                                                   to_char(contador1) || crlf ||
                                                   '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                                                   '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                                                   '     Porcentagem: ' ||
                                                   round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                                   '     Data Execução: ' ||
                                                   to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                    end if;
                           else
                                       status   := 'ERRO';
                                       assunto  := '[ERRO%]';
                                       sqlerro  := 'Variacao porcentagem: 100%';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[ERRO] ##### PMS.VW_BI_PMS_CUSTFIX_UMKT_DRE  ##### - ' || crlf ||
                                                   '       '||sqlerro || crlf ||
                                                   '       Linhas antes do Refresh: ' ||
                                                   to_char(contador1) || crlf ||
                                                   '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                                                   '       Diferença: ' ||
                                                   to_char(contador2 - contador1) || crlf ||
                                                  -- 'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                                   '       Data Execução: ' ||
                                                   to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                       --envio alerta SMS INFRA
                                       sms_msg := sms_msg || 'PMS.VW_BI_PMS_CUSTFIX_UMKT_DRE; ERRO: ' ||
                                                  sqlerro || '; Data: ' ||
                                                  to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                       --limpa msg
                                       sms_msg := 'Objeto: ';
                           end if;
                        exception
                           when others then
                              status   := 'ERRO';
                              assunto  := '[ERRO]';
                              sqlerro  := sqlerrm;
                              mensagem := mensagem || crlf || crlf ||
                                          '[ERRO] ##### PMS.VW_BI_PMS_CUSTFIX_UMKT_DRE  ##### - ' || crlf ||
                                          '       SQL Erro: ' || sqlerrm || crlf ||
                                          '       Data Execução: ' ||
                                          to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                              --envio alerta SMS INFRA
                              sms_msg := sms_msg || 'PMS.VW_BI_PMS_CUSTFIX_UMKT_DRE; ERRO: ' ||
                                         sqlerro || '; Data: ' ||
                                         to_char(sysdate, 'DD/MM/YYYY');
         --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
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
                        status := 'OK';
                        --
                     end if;
                     --  LOG - zerar info
                     sqlerro   := '';
                     contador1 := 0;
                     contador2 := 0;
                     dbms_lock.sleep(1);
                     --
                     --------------------------------------------------------------------------
                     -- Atualização da view de custos fixos dos recursos com intervalo entre admissão e 1a alocação
                     --------------------------------------------------------------------------
                     if (flag = 3 or flag = 99) then
                        mensagem := mensagem || crlf || crlf ||
                                    'Tipo da Carga: 3 - View de Custos Fixos dos recursos com intervalo entre admissão e 1a alocação';
                        begin
                           -- Infor LOG
                           log_dt_carga_processo := sysdate;
                           bloco_upd             := '3 - VW_BI_PMS_CUSTFIX_SAC_DRE_MAT';
                           --
                           --
                           select count(*)
                           into   contador1
                           from   pms.vw_bi_pms_custfix_sac_dre_mat
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
                           --
                           dbms_mview.refresh('"PMS"."VW_BI_PMS_CUSTFIX_SAC_DRE_MAT"');
                           --
                           --
                           select count(*)
                           into   contador2
                           from   pms.vw_bi_pms_custfix_sac_dre_mat
                           where  dt_mes <= (select modu_dt_fechamento
                                             from   modulo
                                             where  modu_cd_modulo = 2);
                           -- Info para LOG
                           descricao := 'FIM    - Linhas: ' || to_char(contador2);
                           -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
                           if (contador2 <> 0 ) then
                                    if (contador2 < contador1) and (round((contador2 / contador1), 2) < 0.99) then
                                       status   := 'ERRO';
                                       assunto  := '[ERRO%]';
                                       sqlerro  := 'Variacao porcentagem: ' ||
                                                   round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[ERRO] ##### PMS.VW_BI_PMS_CUSTFIX_SAC_DRE_MAT  ##### - ' || crlf ||
                                                   '       '||sqlerro || crlf ||
                                                   '       Linhas antes do Refresh: ' ||to_char(contador1) || crlf ||
                                                   '       Linhas após Refresh: '     || to_char(contador2) || crlf ||
                                                   '       Diferença: '               ||to_char(contador2 - contador1) || crlf ||
                                                   '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                       --envio alerta SMS INFRA
                                       sms_msg := sms_msg || 'PMS.VW_BI_PMS_CUSTFIX_SAC_DRE_MAT; ERRO: ' ||
                                                  sqlerro || '; Data: ' ||
                                                  to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                       --limpa msg
                                       sms_msg := 'Objeto: ';
                                    else
                                       status   := 'OK';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[OK] PMS.VW_BI_PMS_CUSTFIX_SAC_DRE_MAT' || crlf ||
                                                   '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                                   '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                                                   '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                                                   '     Porcentagem: ' ||  round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                                   '     Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                    end if;
                           else
                                       status   := 'ERRO';
                                       assunto  := '[ERRO%]';
                                       sqlerro  := 'Variacao porcentagem: 100%';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[ERRO] ##### PMS.VW_BI_PMS_CUSTFIX_SAC_DRE_MAT  ##### - ' || crlf ||
                                                   '       '||sqlerro || crlf ||
                                                   '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                                   '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                                                   '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
                                                   '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                       --envio alerta SMS INFRA
                                       sms_msg := sms_msg || 'PMS.VW_BI_PMS_CUSTFIX_SAC_DRE_MAT; ERRO: ' ||
                                                  sqlerro || '; Data: ' ||
                                                  to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                       --limpa msg
                                       sms_msg := 'Objeto: ';
                           end if;
                        exception
                           when others then
                              status   := 'ERRO';
                              assunto  := '[ERRO]';
                              sqlerro  := sqlerrm;
                              mensagem := mensagem || crlf || crlf ||
                                          '[ERRO] ##### PMS.VW_BI_PMS_CUSTFIX_SAC_DRE_MAT  ##### - ' || crlf ||
                                          '       SQL Erro: ' || sqlerrm || crlf ||
                                          '       Data Execução: ' ||
                                          to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                              --envio alerta SMS INFRA
                              sms_msg := sms_msg || 'PMS.VW_BI_PMS_CUSTFIX_SAC_DRE_MAT; ERRO: ' ||
                                         sqlerro || '; Data: ' ||
                                         to_char(sysdate, 'DD/MM/YYYY');
         --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
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
                     --  LOG - zerar info
                     sqlerro   := '';
                     contador1 := 0;
                     contador2 := 0;
                     dbms_lock.sleep(1);
                     --
                     --------------------------------------------------------------------------
                     -- Atualização de carga das views de custos variáveis
                     --------------------------------------------------------------------------
                     /*  IF (FLAG = 4 OR FLAG = 99) THEN
                         MENSAGEM := MENSAGEM || CRLF || CRLF ||'Tipo da Carga: 4 - Views de Custos Variáveis';
                     -- VW_BI_PMS_CUSTOS_VAR_MAT
                         begin
                         -- info log
                         LOG_DT_CARGA_PROCESSO := sysdate;
                         BLOCO_UPD := '4 - VW_BI_PMS_CUSTOS_VAR_MAT';
                         --
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
                              DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2);
                              -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
                              if (contador2 < contador1) and ( round( ( contador2 / contador1 ), 2 ) < 0.99 ) then
                                    status   := 'ERRO';
                                    assunto  := '[ERRO%]';
                                    sqlerro := 'Variacao porcentagem: ' || round(((-1*(1 - (contador2 / contador1))) * 100), 2) ||'%';
                                    mensagem := mensagem || crlf || crlf ||
                                               '[ERRO] ##### PMS.VW_BI_PMS_CUSTOS_VAR_MAT  ##### - ' || crlf ||
                                               '       Variacao Porcentagem: ' || round(((-1*(1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                               '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                               '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                                               '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
                                               '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                   --envio alerta SMS INFRA
                                   sms_msg := sms_msg ||'PMS.VW_BI_PMS_CUSTOS_VAR_MAT; ERRO: '|| sqlerro ||'; Data: '||to_char(sysdate, 'DD/MM/YYYY');
                                   send_mail('noc-cit-sms@cit.com.br', assunto||'[PMS]['||NOME_CARGA||'] Resultado da Carga', sms_msg);
                                   --limpa msg
                                   sms_msg := 'Objeto: ';
                              else
                                  STATUS:='OK';
                                  mensagem := mensagem || crlf || crlf ||
                                          '[OK] PMS.VW_BI_PMS_CUSTOS_VAR_MAT' || crlf ||
                                          '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                          '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                                          '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                                          '     Porcentagem: ' || round(((-1*(1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                          '     Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
                              end if;
                         EXCEPTION
                           WHEN OTHERS then
                                 STATUS:='ERRO';
                                 assunto  := '[ERRO]';
                                 SQLERRO:= sqlerrm;
                                 mensagem := mensagem || crlf || crlf ||
                                             '[ERRO] ##### PMS.VW_BI_PMS_CUSTOS_VAR_MAT  ##### - ' || crlf ||
                                             '       SQL Erro: ' || sqlerrm || crlf ||
                                             '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                   --envio alerta SMS INFRA
                                   sms_msg := sms_msg ||'PMS.VW_BI_PMS_CUSTOS_VAR_MAT; ERRO: '|| sqlerro ||'; Data: '||to_char(sysdate, 'DD/MM/YYYY');
                                   send_mail('noc-cit-sms@cit.com.br', assunto||'[PMS]['||NOME_CARGA||'] Resultado da Carga', sms_msg);
                                   --limpa msg
                                   sms_msg := 'Objeto: ';
                         END;
                          --Inserir LOG
                              LOG_DT_CARGA_PROCESSO := sysdate;
                              insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
                              values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
                              commit;
                       END IF;*/
                     --  LOG - zerar info
                     sqlerro   := '';
                     contador1 := 0;
                     contador2 := 0;
                     --
                     --------------------------------------------------------------------------
                     -- Atualização de carga das views de despesas fixas
                     --------------------------------------------------------------------------
                     --limpar para proximo registro
                     sqlerro := '';
                     if (flag = 5 or flag = 99) then
                        mensagem := mensagem || crlf || crlf ||
                                    'Tipo da Carga: 5 - Views de Despesas Fixas';
                        --------------------------------------------
                        --  VW_BI_PMS_DESPFIX_UMKT_DRE_MAT
                        --------------------------------------------
                        begin
                           -- info LOG
                           log_dt_carga_processo := sysdate;
                           bloco_upd             := '5 - VW_BI_PMS_DESPFIX_UMKT_DRE_MAT';
                           --
                           --
                           select count(*)
                           into   contador1
                           from   pms.vw_bi_pms_despfix_umkt_dre_mat
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
                           --
                           --
                           dbms_mview.refresh('"PMS"."VW_BI_PMS_DESPFIX_UMKT_DRE_MAT"');
                           --
                           --
                           select count(*)
                           into   contador2
                           from   pms.vw_bi_pms_despfix_umkt_dre_mat
                           where  dt_mes <= (select modu_dt_fechamento
                                             from   modulo
                                             where  modu_cd_modulo = 2);
                           -- Info para LOG
                           descricao := 'FIM    - Linhas: ' || to_char(contador2);
                           -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
                           if(contador2 <> 0) then  
                                    if (contador2 < contador1) and (round((contador2 / contador1), 2) < 0.99) then
                                       status   := 'ERRO';
                                       assunto  := '[ERRO%]';
                                       sqlerro  := 'Variacao porcentagem: ' ||
                                                   round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[ERRO] ##### PMS.VW_BI_PMS_DESPFIX_UMKT_DRE_MAT  ##### - ' || crlf ||
                                                   '       '||sqlerro || crlf ||
                                                   '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                                   '       Linhas após Refresh: '     || to_char(contador2) || crlf ||
                                                   '       Diferença: '               || to_char(contador2 - contador1) || crlf ||
                                                   '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                       --envio alerta SMS INFRA
                                       sms_msg := sms_msg ||
                                                  'PMS.VW_BI_PMS_DESPFIX_UMKT_DRE_MAT; ERRO: ' ||
                                                  sqlerro || '; Data: ' ||
                                                  to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                       --limpa msg
                                       sms_msg := 'Objeto: ';
                                    else
                                       status   := 'OK';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[OK] PMS.VW_BI_PMS_DESPFIX_UMKT_DRE_MAT' || crlf ||
                                                   '     Linhas antes do Refresh: '          || to_char(contador1) || crlf ||
                                                   '     Linhas após Refresh: '              || to_char(contador2) || crlf ||
                                                   '     Diferença: '                        || to_char(contador2 - contador1) || crlf ||
                                                   '     Porcentagem: '                      || round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                                   '     Data Execução: '                    || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                    end if;
                           else
                                       status   := 'ERRO';
                                       assunto  := '[ERRO%]';
                                       sqlerro  := 'Variacao porcentagem: 100%';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[ERRO] ##### PMS.VW_BI_PMS_DESPFIX_UMKT_DRE_MAT  ##### - ' || crlf ||
                                                   '       '||sqlerro || crlf ||
                                                   '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                                   '       Linhas após Refresh: '     || to_char(contador2) || crlf ||
                                                   '       Diferença: '               || to_char(contador2 - contador1) || crlf ||
                                                   '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                       --envio alerta SMS INFRA
                                       sms_msg := sms_msg ||
                                                  'PMS.VW_BI_PMS_DESPFIX_UMKT_DRE_MAT; ERRO: ' ||
                                                  sqlerro || '; Data: ' ||
                                                  to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                       --limpa msg
                                       sms_msg := 'Objeto: ';
                           end if;         
                        exception
                           when others then
                              status   := 'ERRO';
                              assunto  := '[ERRO]';
                              sqlerro  := sqlerrm;
                              mensagem := mensagem || crlf || crlf ||
                                          '[ERRO] ##### PMS.VW_BI_PMS_DESPFIX_UMKT_DRE_MAT  ##### - ' || crlf ||
                                          '       SQL Erro: ' || sqlerrm || crlf ||
                                          '       Data Execução: ' ||
                                          to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                              --envio alerta SMS INFRA
                              sms_msg := sms_msg ||
                                         'PMS.VW_BI_PMS_DESPFIX_UMKT_DRE_MAT; ERRO: ' ||
                                         sqlerro || '; Data: ' ||
                                         to_char(sysdate, 'DD/MM/YYYY');
         --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
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
                     --  LOG - zerar info
                     sqlerro   := '';
                     contador1 := 0;
                     contador2 := 0;
                     dbms_lock.sleep(1);
                     --
                     --------------------------------------------------------------------------
                     -- Atualização de carga das views de despesas fixas
                     --------------------------------------------------------------------------
                     --limpar para proximo registro
                     sqlerro := '';
                     if (flag = 6 or flag = 99) then
                        mensagem := mensagem || crlf || crlf ||
                                    'Tipo da Carga: 6 - Views de Despesas Fixas';
                        --------------------------------------------
                        --   VW_BI_PMS_MAT_FIM_RATEIO
                        --------------------------------------------
                        begin
                           -- info LOG
                           log_dt_carga_processo := sysdate;
                           bloco_upd             := '6 - VW_BI_PMS_MAT_FIM_RATEIO';
                           --
                           --
                           select count(*)
                           into   contador1
                           from   pms.vw_bi_pms_mat_fim_rateio
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
                           --
                           -----
                           dbms_mview.refresh('"PMS"."VW_BI_PMS_MAT_FIM_RATEIO"');
                           -----
                           --
                           select count(*)
                           into   contador2
                           from   pms.vw_bi_pms_mat_fim_rateio
                           where  dt_mes <= (select modu_dt_fechamento
                                             from   modulo
                                             where  modu_cd_modulo = 2);
                           -- Info para LOG
                           descricao := 'FIM    - Linhas: ' || to_char(contador2);
                           -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
                           if (contador2 <> 0) then
                                    if (contador2 < contador1) and (round((contador2 / contador1), 2) < 0.99) then
                                       status   := 'ERRO';
                                       assunto  := '[ERRO%]';
                                       sqlerro  := 'Variacao porcentagem: ' ||
                                                   round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[ERRO] ##### PMS.VW_BI_PMS_MAT_FIM_RATEIO  ##### - ' || crlf ||
                                                   '       '|| sqlerro|| crlf ||
                                                   '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                                   '       Linhas após Refresh: '     || to_char(contador2) || crlf ||
                                                   '       Diferença: '               || to_char(contador2 - contador1) || crlf ||
                                                   '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                       --envio alerta SMS INFRA
                                       sms_msg := sms_msg || 'PMS.VW_BI_PMS_MAT_FIM_RATEIO; ERRO: ' ||
                                                  sqlerro || '; Data: ' ||
                                                  to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                       --limpa msg
                                       sms_msg := 'Objeto: ';
                                    else
                                       status   := 'OK';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[OK] PMS.VW_BI_PMS_MAT_FIM_RATEIO' || crlf ||
                                                   '     Linhas antes do Refresh: '    || to_char(contador1) || crlf ||
                                                   '     Linhas após Refresh: '        || to_char(contador2) || crlf ||
                                                   '     Diferença: '                  || to_char(contador2 - contador1) || crlf ||
                                                   '     Porcentagem: '                || round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                                   '     Data Execução: '              || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                    end if;
                           else
                                       status   := 'ERRO';
                                       assunto  := '[ERRO%]';
                                       sqlerro  := 'Variacao porcentagem: 100%';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[ERRO] ##### PMS.VW_BI_PMS_MAT_FIM_RATEIO  ##### - ' || crlf ||
                                                   '       '|| sqlerro|| crlf ||
                                                   '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                                   '       Linhas após Refresh: '     || to_char(contador2) || crlf ||
                                                   '       Diferença: '               || to_char(contador2 - contador1) || crlf ||
                                                   '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                       --envio alerta SMS INFRA
                                       sms_msg := sms_msg || 'PMS.VW_BI_PMS_MAT_FIM_RATEIO; ERRO: ' ||
                                                  sqlerro || '; Data: ' ||
                                                  to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                       --limpa msg
                                       sms_msg := 'Objeto: ';
                           end if;
                        exception
                           when others then
                              status   := 'ERRO';
                              assunto  := '[ERRO]';
                              sqlerro  := sqlerrm;
                              mensagem := mensagem || crlf || crlf ||
                                          '[ERRO] ##### PMS.VW_BI_PMS_MAT_FIM_RATEIO  ##### - ' || crlf ||
                                          '       SQL Erro: ' || sqlerrm || crlf ||
                                          '       Data Execução: ' ||
                                          to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                              --envio alerta SMS INFRA
                              sms_msg := sms_msg || 'PMS.VW_BI_PMS_MAT_FIM_RATEIO; ERRO: ' ||
                                         sqlerro || '; Data: ' ||
                                         to_char(sysdate, 'DD/MM/YYYY');
         --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
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
                     --------------------------------------------------------------------------
                     -- Atualização de carga das tabelas de informações do RES
                     --------------------------------------------------------------------------
                     --limpar para proximo registro
                     sqlerro := '';
     --COMENTADO DIA 14 DE MARÇO DE 2012                
                     if (flag = 7 or flag = 99) then
                        mensagem := mensagem || crlf || crlf ||
                                    'Tipo da Carga: 7 - Informações do RES';
                        --------------------------------------------
                        --   VW_BI_PMS_MAT_FIM_RATEIO
                        --------------------------------------------
                        begin
                           -- info LOG
                           log_dt_carga_processo := sysdate;
                           bloco_upd             := '7.1 - VW_BI_PMS_CUSTOS_RES_OFIC';
                           --
                           --
                           select count(*)
                           into   contador1
                           from   pms.tb_bi_pms_custos_res_ofic
                           where  dt_mes <= (select modu_dt_fechamento
                                             from   modulo
                                             where  modu_cd_modulo = 2);
                           --
                           -- Inserir LOG
                           descricao := 'INICIO - Linhas: ' || to_char(contador1);
                           --
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
                           --
                           execute immediate 'truncate table tb_bi_pms_custos_res_ofic';
                           --
                           insert into tb_bi_pms_custos_res_ofic
                           select fonte,
                                  origem,
                                  cd_pessoa,
                                  login,
                                  dt_mes,
                                  cd_contrato_pratica,
                                  nome_contrato_pratica,
                                  cd_cidade_base,
                                  dt_admissao,
                                  dt_rescisao,
                                  perc_alocavel,
                                  status,
                                  dt_inicio_pgc,
                                  dt_fim_pgc,
                                  cd_grupo_custo,
                                  nome_grupo_custo,
                                  nome_area,
                                  cd_tipo_contrato,
                                  nome_tipo_contrato,
                                  dt_inicio_ptc,
                                  dt_fim_ptc,
                                  pct_ferias,
                                  pct_licenca,
                                  vl_custo_licenca_curr,
                                  vl_custo_licenca_brl,
                                  cd_moeda,
                                  moeda_custo,
                                  valor_cotacao,
                                  valor_custo_hora_extra,
                                  valor_plantao,
                                  pct_alocacao_innov,
                                  pct_alocacao_nao_innov,
                                  total_aloc_inno,
                                  total_aloc_nao_inno,
                                  vl_custo_alocacao_innov,
                                  vl_custo_alocacao_nao_innov,
                                  pct_custo_res,
                                  total_aloc_geral,
                                  valor_tce,
                                  valor_custo_infra_base,
                                  valor_custo_res_tce_cur,
                                  valor_custo_res_tce_brl,
                                  valor_custo_res_inf_cur,
                                  valor_custo_res_inf_brl,
                                  valor_custo_dir_tce_curr,
                                  valor_custo_dir_tce_brl,
                                  valor_custo_dir_infra_curr,
                                  valor_custo_dir_infra_brl,
                                  valor_fte_cind,
                                  valor_custo_ind_tce_curr,
                                  valor_custo_ind_tce_brl,
                                  valor_custo_ind_infra_curr,
                                  valor_custo_ind_infra_brl,
                                  -- Motivo: Alterado por causa da Task PBI-831
                                     -- Desenv: Leandro Lino Ferreira
                                     --   Data: 18/09/12
                                     vlr_cust_infr_base_curr,
                                     vlr_cust_infr_base_brl,
                                     vl_beneficios_curr,
                                      vl_beneficios_brl        
                           from vw_bi_pms_custos_res_ofic;
                           --
                           commit;
                           --
                           select count(*)
                           into   contador2
                           from   pms.tb_bi_pms_custos_res_ofic
                           where  dt_mes <= (select modu_dt_fechamento
                                             from   modulo
                                             where  modu_cd_modulo = 2);
                           --
                           if (contador2 <> 0) then
                                    if (contador2 < contador1) and (round((contador2 / contador1), 2) < 0.99) then
                                       status   := 'ERRO';
                                       assunto  := '[ERRO%]';
                                       sqlerro  := 'Variacao porcentagem: ' ||
                                                   round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[ERRO] ##### CUSTOS RES ##### - ' || crlf ||
                                                   '       '||sqlerro|| crlf ||
                                                   '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                                   '       Linhas após Refresh: '     || to_char(contador2) || crlf ||
                                                   '       Diferença: '               || to_char(contador2 - contador1) || crlf ||
                                                   '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                       --envio alerta SMS INFRA
                                       sms_msg := sms_msg || 'CUSTOS RES; ERRO: ' ||
                                                  sqlerro || '; Data: ' ||
                                                  to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                       --limpa msg
                                       sms_msg := 'Objeto: ';
                                    else
                                       status   := 'OK';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[OK] CUSTOS RES' || crlf ||
                                                   '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                                   '     Linhas após Refresh: '     || to_char(contador2) || crlf ||
                                                   '     Diferença: '               || to_char(contador2 - contador1) || crlf ||
                                                   '     Porcentagem: '             || round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                                   '     Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                    end if;
                           else
                                       status   := 'ERRO';
                                       assunto  := '[ERRO%]';
                                       sqlerro  := 'Variacao porcentagem: 100%';
                                       mensagem := mensagem || crlf || crlf ||
                                                   '[ERRO] ##### CUSTOS RES ##### - ' || crlf ||
                                                   '       '||sqlerro|| crlf ||
                                                   '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                                   '       Linhas após Refresh: '     || to_char(contador2) || crlf ||
                                                   '       Diferença: '               || to_char(contador2 - contador1) || crlf ||
                                                   '       Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                       --envio alerta SMS INFRA
                                       sms_msg := sms_msg || 'CUSTOS RES; ERRO: ' ||
                                                  sqlerro || '; Data: ' ||
                                                  to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                       --limpa msg
                                       sms_msg := 'Objeto: ';
                           end if;
                           --
                           if contador2 > 0 then
                                 begin
                                      contador1 := 0;
                                      contador2 := 0;
                                      -- info LOG
                                      log_dt_carga_processo := sysdate;
                                      bloco_upd             := '7.2 - VW_BI_PMS_INFORMACOES_RES';
                                      --
                                      select count(*)
                                      into   contador1
                                      from   pms.vw_bi_pms_custos_res_ofic
                                      where  dt_mes <= ( select modu_dt_fechamento
                                                         from   modulo
                                                         where  modu_cd_modulo = 2);
                                      --
                                      -- Inserir LOG
                                      descricao := 'INICIO - Linhas: ' || to_char(contador1);
                                      --
                                      insert into tb_bi_pms_log_cargas
                                             ( log_data,
                                               log_dt_carga_processo,
                                               nome_carga,
                                               bloco_upd,
                                               descricao,
                                               status,
                                               nome_proc)
                                      values( log_data,
                                              log_dt_carga_processo,
                                              nome_carga,
                                              bloco_upd,
                                              descricao,
                                              'OK',
                                              nome_proc );
                                      --
                                      commit;
                                      --
                                      execute immediate 'truncate table tb_bi_pms_informacoes_res';
                                      --
                                      insert into tb_bi_pms_informacoes_res
                                      select fonte,
                                             sub_fonte,
                                             origem,
                                             cd_pessoa,
                                             login,
                                             dt_mes,
                                             categoria,
                                             cd_contrato_pratica,
                                             nome_contrato_pratica,
                                             cd_cidade_base,
                                             dt_admissao,
                                             dt_rescisao,
                                             perc_alocavel,
                                             status,
                                             dt_inicio_pgc,
                                             dt_fim_pgc,
                                             cd_grupo_custo,
                                             nome_grupo_custo,
                                             nome_area,
                                             cd_tipo_contrato,
                                             nome_tipo_contrato,
                                             dt_inicio_ptc,
                                             dt_fim_ptc,
                                             pct_ferias,
                                             pct_licenca,
                                             vl_custo_licenca_curr,
                                             vl_custo_licenca_brl,
                                             valor_tce,
                                             valor_custo_infra_base,
                                             cd_moeda,
                                             moeda_custo,
                                             valor_cotacao,
                                             valor_custo_hora_extra,
                                             valor_plantao,
                                             pct_alocacao_innov,
                                             pct_alocacao_nao_innov,
                                             total_aloc_inno,
                                             total_aloc_nao_inno,
                                             vl_custo_alocacao_innov,
                                             vl_custo_alocacao_nao_innov,
                                             pct_custo_res,
                                             valor_custo_res_tce_cur,
                                             valor_custo_res_tce_brl,
                                             valor_custo_res_infra_cur,
                                             valor_custo_res_infra_brl,
                                             valor_custo_dir_tce_curr,
                                             valor_custo_dir_tce_brl,
                                             valor_custo_dir_infra_curr,
                                             valor_custo_dir_infra_brl,
                                             valor_fte_cind,
                                             valor_custo_ind_tce_curr,
                                             valor_custo_ind_tce_brl,
                                             valor_custo_ind_infra_curr,
                                             valor_custo_ind_infra_brl,                           
                                             valor_despesa_clob_curr,
                                             valor_despesa_cg_curr,
                                             valor_despesa_clob_brl,
                                             valor_despesa_cg_brl,       
                                             valor_despesa_cur,
                                             valor_despesa_brl,
                                             cd_empresa,
                                             description_txt,
                                             expense_group,
                                             expense_type,
                                              -- Motivo: Alterado por causa da Task PBI-831
                                               -- Desenv: Leandro Lino Ferreira
                                               --   Data: 18/09/12
                                               vlr_cust_infr_base_curr,
                                               vlr_cust_infr_base_brl,
                                               vl_beneficios_curr,
                                                vl_beneficios_brl                                             
                                      from vw_bi_pms_informacoes_res;
                                      --
                                      commit;
                                      --
                                      select count(*)
                                      into   contador2
                                      from   pms.tb_bi_pms_informacoes_res
                                      where  dt_mes <= ( select modu_dt_fechamento
                                                         from   modulo
                                                         where  modu_cd_modulo = 2);
                                      --
                                      if (contador2 <> 0)then
                                               if (contador2 < contador1) and (round((contador2 / contador1), 2) < 0.99) then
                                                  status   := 'ERRO';
                                                  assunto  := '[ERRO%]';
                                                  sqlerro  := 'Variacao porcentagem: ' ||
                                                                round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                                                  mensagem := mensagem || crlf || crlf ||
                                                              '[ERRO] ##### INFORMACOES RES ##### - ' || crlf ||
                                                              '       '||sqlerro || crlf ||
                                                              '       Linhas antes do Refresh: '      || to_char(contador1) || crlf ||
                                                              '       Linhas após Refresh: '          || to_char(contador2) || crlf ||
                                                              '       Diferença: '                    || to_char(contador2 - contador1) || crlf ||
                                                              '       Data Execução: '                || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                                  --envio alerta SMS INFRA
                                                  sms_msg := sms_msg || 'PMS.VW_BI_PMS_MAT_FIM_RATEIO; ERRO: ' ||
                                                             sqlerro || '; Data: ' || to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                                  --limpa msg
                                                  sms_msg := 'Objeto: ';
                                               else
                                                  status   := 'OK';
                                                  mensagem := mensagem || crlf || crlf ||
                                                              '[OK] CUSTOS RES' || crlf ||
                                                              '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                                              '     Linhas após Refresh: '     || to_char(contador2) || crlf ||
                                                              '     Diferença: '               || to_char(contador2 - contador1) || crlf ||
                                                              '     Porcentagem: '             || round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                                              '     Data Execução: '           || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                               end if;
                                    else
                                                  status   := 'ERRO';
                                                  assunto  := '[ERRO%]';
                                                  sqlerro  := 'Variacao porcentagem: 100%';
                                                  mensagem := mensagem || crlf || crlf ||
                                                              '[ERRO] ##### INFORMACOES RES ##### - ' || crlf ||
                                                              '       '||sqlerro || crlf ||
                                                              '       Linhas antes do Refresh: '      || to_char(contador1) || crlf ||
                                                              '       Linhas após Refresh: '          || to_char(contador2) || crlf ||
                                                              '       Diferença: '                    || to_char(contador2 - contador1) || crlf ||
                                                              '       Data Execução: '                || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                                                  --envio alerta SMS INFRA
                                                  sms_msg := sms_msg || 'PMS.VW_BI_PMS_MAT_FIM_RATEIO; ERRO: ' ||
                                                             sqlerro || '; Data: ' || to_char(sysdate, 'DD/MM/YYYY');
                  --                                send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                                                  --limpa msg
                                                  sms_msg := 'Objeto: ';
                                    end if;
                                 end;
                           else
                              status   := 'ERRO';
                              assunto  := '[ERRO%]';
                              sqlerro  := 'Carga incompleta custos RES: ' ||
                                          round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                              mensagem := mensagem || crlf || crlf ||
                                          '[ERRO] ##### CUSTOS RES ##### - ' || crlf ||
                                          '       Erro na carga da primeira view de custos ' || crlf ||
                                          '       Data Execução: ' ||
                                          to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                              --envio alerta SMS INFRA
                              sms_msg := sms_msg || 'CUSTOS RES; ERRO: ' ||
                                         sqlerro || '; Data: ' ||
                                         to_char(sysdate, 'DD/MM/YYYY');
         --                     send_mail('noc-cit-sms@cit.com.br', assunto || '[PMS][' || nome_carga ||'] Resultado da Carga', sms_msg);
                              --limpa msg
                              sms_msg := 'Objeto: ';
                           end if;
                        end;
                     end if;
                     --  
            else
                  BLOCO_UPD := 'IF PESSOA.DT_ADMISSAO is NULL';
                  DESCRICAO:= 'ATENCAO - TABELA PESSOA CONTEM DATA ADMISSAO NULL!';
                  STATUS:='ERRO';
                  SQLERRO:= 'DATA ADMISSAO PESSOA NULL!';
                  assunto  := '[ERRO]';
                  LOG_DT_CARGA_PROCESSO := sysdate;
                  insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
                  values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
                  commit;         
                  mensagem := mensagem || crlf || crlf ||'[ERRO] ##### PESSOA - Check Data Admissao NULL  ##### - ' 
                                       || crlf || '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --send_mail('lahumada@cit.com.br', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
                     send_mail('alexandrel@cit.com.br', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
                     send_mail('lnoboru@cit.com.br', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);            
                     send_mail('it_oncall@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
                     
                     send_mail('tspadari@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
                     send_mail('llino@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
                     send_mail('andreiap@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
                     
                     
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
            send_mail('lnoboru@cit.com.br', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);     

            send_mail('tspadari@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);     
            send_mail('llino@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);     
            send_mail('andreiap@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);     

            
                   
          --  send_mail('it_oncall@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
          --  sms_msg := sms_msg || 'pms.pessoa - Data Admissao is Null';
          --  send_mail('noc-cit-sms@cit.com.br',assunto || '[PMS][' || nome_carga ||']  Check Data Admissao - PMS.PESSOA',sms_msg);
   end if;              
                        
   mensagem := mensagem || crlf || crlf || '=== Fim da Carga ' ||to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===';
   mensagem := mensagem || crlf ||'select * from pms.TB_BI_PMS_LOG_CARGAS where NOME_CARGA = '''||nome_carga||''' order by 2 desc';
   assunto  := assunto || ' [PMS]['||nome_carga||'] Resultado da Carga';
   send_mail('lnoboru@ciandt.com', assunto, mensagem);   
   send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
   
   send_mail('tspadari@ciandt.com', assunto, mensagem);
   send_mail('llino@ciandt.com', assunto, mensagem);
   send_mail('andreiap@ciandt.com', assunto, mensagem);


   if status = 'ERRO' then
      mensagem := 'ATTENTION: Please, contact Ciandt IT Business Intelligence Team as soon as possible. ' || crlf || crlf ||
                  mensagem;
      send_mail('it_oncall@ciandt.com', assunto, mensagem);
   end if;
end USP_PMS_umkts;