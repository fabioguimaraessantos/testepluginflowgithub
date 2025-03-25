create or replace procedure usp_pms_recurso is
--INTERVAL: trunc(sysdate +1) + 22.5/24
   cursor c_recurso_pessoa is
      select f.login,
             f.nm_func,
             f.email,
             f.dt_admissao,
             f.login_mgr login_mgr,
             cb.ciba_cd_cidade_base,
             f.jornada_hrs_dia,
             f.st_ativo,
             f.dt_saida,
             tc.tico_cd_tipo_contrato,
             f.mentor,
             f.ds_papel,
             ca.nm_cargo
      from   vw_adm_rh_funcionarios f,
             cidade_base            cb,
             tipo_contrato          tc,
             vw_adm_rh_cargos       ca
      where  f.cidade_base = cb.ciba_sg_cidade_base
      and    f.cd_cargo = ca.cd_cargo(+)
      and    f.login not in
             ('lmaruyama', 'rodrigues',
              'suzaner', 'jmoreno','v.gmenezes', 'v.pmello', 'karinaxs', 'lbenedicto', 'monicalima', 'v.mwoigt')
      and    f.tp_contrato = tc.tico_sg_tipo_contrato;
   v_cd_recurso  number := 0;
   v_pr_alocavel number := 0;
   v_mentor      varchar2(20);
   v_login_mgr   varchar2(20);
   -- Informacões para LOG
   log_data              date := trunc(sysdate);
   log_dt_carga_processo date;
   nome_carga            varchar2(50) := '(3) RECURSO';
   bloco_upd             varchar2(50) := '';
   descricao             varchar2(200) := '';
   status                varchar2(30) := '';
   sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'usp_pms_recurso';
   --
   crlf      varchar2(2) := chr(13) || chr(10);
   assunto   varchar2(200) := '';
   mensagem  varchar2(3000) := '=== Início da Carga ' ||
                               to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') ||
                               ' ===';
   contador1 integer := 0;
   contador2 integer := 0;
   sms_msg   varchar2(200) := 'Objeto: ';
   chk_pessoa integer;
begin
   -- Procedure alterada por Luiz Ahumada jul/2011
   mensagem := mensagem || crlf || ':: PROCEDURE: ' || nome_proc || ' ::';
   send_mail('alexandrel@cit.com.br','[NOC][' || nome_carga || ']Start Proc',mensagem);
   send_mail('lnoboru@ciandt.com','[NOC][' || nome_carga || '] Start Proc',mensagem);
  -- info para log
   select count(*)
   into chk_pessoa
   from pessoa p
   where p.pess_dt_admissao is null;

   if chk_pessoa = 0 then

            bloco_upd := 'recurso e pessoa (flag 1 de 1)';
            --
            select count(*)
            into   contador1
            from   pms20.recurso;
            -- Inserir LOG
            descricao             := 'INICIO - Linhas (recurso): ' ||
                                     to_char(contador1);
            log_dt_carga_processo := sysdate;
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
            for r_pess in c_recurso_pessoa loop
               -- verifica se já foi cadastrado
               select nvl(max(r.recu_cd_recurso), 0)
               into   v_cd_recurso
               from   recurso r
               where  r.recu_cd_mnemonico = r_pess.login;
               -- jornada
               if (r_pess.jornada_hrs_dia = 6) then
                  v_pr_alocavel := 0.75;
               else
                  v_pr_alocavel := 1;
               end if;
               -- recurso não cadastrado
               begin
                  -- tratamento log
                  if (v_cd_recurso = 0) then
                     -- Seleciona o código do recurso
                     select sq_recu_cd_recurso.nextval
                     into   v_cd_recurso
                     from   dual;
                     begin
                        -- insere na tabela de recurso
                        insert into recurso
                           (recu_cd_recurso,
                            recu_cd_mnemonico,
                            recu_in_tipo_recurso,
                            recu_in_ativo)
                        values
                           (v_cd_recurso, r_pess.login, 'PE', r_pess.st_ativo);
                        -- insere na tabela de pessoa
                        insert into pessoa
                           (pess_cd_pessoa,
                            recu_cd_recurso,
                            ciba_cd_cidade_base,
                            pess_cd_login,
                            pess_nm_pessoa,
                            pess_tx_email,
                            pess_dt_admissao,
                            pess_cd_login_mgr,
                            pess_dt_mes_validado,
                            pess_pr_alocavel,
                            pess_dt_rescisao,
                            tico_cd_tipo_contrato,
                            pess_cd_login_mentor,
                            pess_tx_papel,
                            pess_tx_cargo)
                        values
                           (sq_pess_cd_pessoa.nextval,
                            v_cd_recurso,
                            r_pess.ciba_cd_cidade_base,
                            r_pess.login,
                            r_pess.nm_func,
                            r_pess.email,
                            r_pess.dt_admissao,
                            r_pess.login_mgr,
                            null,
                            v_pr_alocavel,
                            r_pess.dt_saida,
                            r_pess.tico_cd_tipo_contrato,
                            r_pess.mentor,
                            r_pess.ds_papel,
                            r_pess.nm_cargo);
                     exception
                        when others then
                           status   := 'ERRO'; -- log
                           sqlerro  := sqlerrm; -- log
                           assunto  := '[ERRO]';
                           mensagem := mensagem || crlf || crlf ||
                                       '[ERRO] ##### NOC.recurso ou pms.pessoa  ##### - ' || crlf ||
                                       '       ERRO SQL: ' || sqlerrm || crlf ||
                                       '       Data Execução: ' ||
                                       to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                           rollback;
                           --envio alerta SMS INFRA
                           sms_msg := sms_msg || '; ERRO: ' || sqlerro || '; Data: ' ||
                                      to_char(sysdate, 'DD/MM/YYYY');
         --                  send_mail('noc-cit-sms@cit.com.br',
         --                            assunto || '[PMS][' || nome_carga ||
         --                            '] Resultado da Carga',
         --                            sms_msg);
                           --limpa msg
                           sms_msg := 'Objeto: ';
                     end;
                     --  recurso cadastrado
                  else
                     -- atualiza a tabela recurso
                     update recurso r
                     set    r.recu_in_ativo = r_pess.st_ativo
                     where  r.recu_cd_recurso = v_cd_recurso
                     and    r.recu_cd_mnemonico = r_pess.login;
                     if (r_pess.login = 'cgimenes' or r_pess.login = 'wrj') then
                        v_mentor := 'ostanelli';
                     else
                        v_mentor := r_pess.mentor;
                     end if;

                     v_login_mgr := r_pess.login_mgr;

                     -- atualiza a tabela pessoa
                     update pessoa p
                     set    p.pess_cd_login_mgr = v_login_mgr,
                            --   p.pess_pr_alocavel = v_pr_alocavel,    -- quando alguém fizer algum comentario, favor deixar comentado. (katsumi)
                            --  p.ciba_cd_cidade_base = r_pess.ciba_cd_cidade_base,
                            p.pess_dt_rescisao      = r_pess.dt_saida,
                            p.tico_cd_tipo_contrato = r_pess.tico_cd_tipo_contrato,
                            p.pess_cd_login_mentor  = v_mentor,
                            p.pess_tx_papel         = r_pess.ds_papel,
                            p.pess_tx_cargo         = r_pess.nm_cargo,
                            p.ciba_cd_cidade_base   = r_pess.ciba_cd_cidade_base
                     where  p.recu_cd_recurso = v_cd_recurso
                     and    p.pess_cd_login = r_pess.login;
                  end if;
               exception
                  --tratamento log
                  when others then
                     status   := 'ERRO'; -- log
                     sqlerro  := sqlerrm; -- log
                     assunto  := '[ERRO]';
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### NOC.recurso ##### - ' || crlf ||
                                 '       ERRO SQL: ' || sqlerrm || crlf ||
                                 '       Data Execução: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || '; ERRO: ' || sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         --            send_mail('noc-cit-sms@cit.com.br',
         --                      assunto || '[PMS][' || nome_carga ||
         --                      '] Resultado da Carga',
         --                      sms_msg);
                     --limpa msg
                     sms_msg := 'Objeto: ';
               end;
            end loop;
            commit;
            if status = 'ERRO' then
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
            else
               --
               select count(*)
               into   contador2
               from   pms20.recurso;
               -- Info LOG
               descricao := 'FIM    - Linhas: ' || to_char(contador2);
               --    -- As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
                if (contador2 <> 0) then
                         if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                           status   := 'ERRO';
                           assunto  := '[ERRO%]';
                           sqlerro  := 'Variacao porcentagem: ' || round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                           mensagem := mensagem || crlf || crlf ||
                                       '[ERRO] ##### NOC.tmp_despesa_realizada  ##### - ' || crlf ||
                                       '       '||sqlerro || crlf ||
                                       '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                       '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                                       '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
                                      --  'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                       '       Data Execução: ' ||
                                       to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                           --envio alerta SMS INFRA
                           sms_msg := sms_msg || 'NOC.tmp_despesa_realizada; ERRO: ' || sqlerro ||
                                      '; Data: ' || to_char(sysdate, 'DD/MM/YYYY');
                  --         send_mail('noc-cit-sms@cit.com.br',
                  --                   assunto || '[PMS][' || nome_carga ||
                  --                   '] Resultado da Carga',
                  --                   sms_msg);
                           --limpa msg
                           sms_msg := 'Objeto: ';
                        else
                           sqlerro  := '';
                           status   := 'OK';
                           mensagem := mensagem || crlf || crlf ||
                                       '[OK] NOC.tmp_despesa_realizada' || crlf ||
                                       '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                       '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                                       '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
                                       '       Porcentagem: ' ||
                                       round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                       '       Data Execução: ' ||
                                       to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
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
                   else
                           status   := 'ERRO';
                           assunto  := '[ERRO%]';
                           sqlerro  := 'Variacao porcentagem: 100%';
                           mensagem := mensagem || crlf || crlf ||
                                       '[ERRO] ##### NOC.tmp_despesa_realizada  ##### - ' || crlf ||
                                       '       '||sqlerro || crlf ||
                                       '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                                       '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                                       '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
                                      --  'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                       '       Data Execução: ' ||
                                       to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                           --envio alerta SMS INFRA
                           sms_msg := sms_msg || 'NOC.tmp_despesa_realizada; ERRO: ' || sqlerro ||
                                      '; Data: ' || to_char(sysdate, 'DD/MM/YYYY');
                  --         send_mail('noc-cit-sms@cit.com.br',
                  --                   assunto || '[PMS][' || nome_carga ||
                  --                   '] Resultado da Carga',
                  --                   sms_msg);
                           --limpa msg
                           sms_msg := 'Objeto: ';
                    end if;
        end if;
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
            send_mail('lahumada@cit.com.br', assunto|| ' [NOC][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
            send_mail('alexandrel@cit.com.br', assunto|| ' [NOC][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
            send_mail('lnoboru@cit.com.br', assunto|| ' [NOC][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
            send_mail('it_oncall@ciandt.com', assunto|| ' [NOC][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
            sms_msg := sms_msg || 'pms.pessoa - Data Admissao is Null';
--            send_mail('noc-cit-sms@cit.com.br',assunto || '[NOC][' || nome_carga ||']  Check Data Admissao - PMS.PESSOA',sms_msg);
   end if;
   mensagem := mensagem || crlf || crlf || '=== Fim da Carga ' ||to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===';
   mensagem := mensagem || crlf ||'select * from pms.TB_BI_PMS_LOG_CARGAS where NOME_CARGA = ''' ||nome_carga || ''' order by 2 desc';
   assunto  := assunto || '[NOC][' || nome_carga || '] Resultado da Carga';
   send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
   send_mail('lnoboru@ciandt.com', assunto, mensagem);
   if status = 'ERRO' then
      mensagem := 'ATTENTION: Please, contact Ciandt IT Business Intelligence Team as soon as possible. ' || crlf || crlf ||mensagem;
      send_mail('it_oncall@ciandt.com', assunto, mensagem);
   end if;
end usp_pms_recurso;
