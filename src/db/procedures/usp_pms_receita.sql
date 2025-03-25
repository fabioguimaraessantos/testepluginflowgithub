CREATE OR REPLACE PROCEDURE USP_PMS_RECEITA (flag number) as
   log_data              date := trunc(sysdate);
--INTERVAL:
   -- FLAG
   -- 1 = REV
   -- 2 = TAX
   -- 3 = REV_CON e REV_REC
   -- 99 = TODOS
   -- Procedure de carga
        -- 1 - ORIGEM: REV CATEGORIA: Contractors
        -- 2 - ORIGEM: REV CATEGORIA: Others
        -- 3 - ORIGEM: REV CATEGORIA: Services
        -- 4 - ORIGEM: REV CATEGORIA: Travel
        -- 5 - ORIGEM: TAX CATEGORIA: Contractors
        -- 6 - ORIGEM: TAX CATEGORIA: Others
        -- 7 - ORIGEM: TAX CATEGORIA: Services
        -- 8 - ORIGEM: TAX CATEGORIA: Travel
        -- 9 - ORIGEM: REV_CONT CATEGORIA: Services
        -- 10 - ORIGEM: REV_REC CATEGORIA: Services
   -- Informac?es para LOG
   log_dt_carga_processo date;
   log_status                varchar2(30) := '';
   log_origem                varchar2(50) := '';
   log_categoria             varchar2(50) := '';
   log_diferenca             number;
   log_descricao             varchar2(200) := '';

   log_sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'USP_PMS_RECEITA';
   --
   crlf      varchar2(2) := chr(13) || chr(10); -- <ENTER>
   assunto   varchar2(200) := '';
   mensagem  varchar2(3000) := '=== Inicio da Carga ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===' ;
   valor_pre integer;
   log_vl_rev_serv  number;
   log_vl_rev_contr number;
   log_vl_rev_othr  number;
   log_vl_rev_trav  number;
   log_vl_tax_serv  number;
   log_vl_tax_contr number;
   log_vl_tax_othr  number;
   log_vl_tax_trav  number;
   log_vl_rrec_serv number;
   log_vl_rcon_serv number;
   log_campo varchar2(50);
   --   sms_msg   varchar2(200) := 'Objeto: ';

begin
   --------------------------------------------------------
   -- VALIDACAO VW_BI_PMS_RECEITAS
   --------------------------------------------------------
   mensagem := mensagem || crlf || ':: PROCEDURE: ' || nome_proc || ' ::' || crlf || 'FLAG: '||flag || crlf || crlf;
   send_mail('lahumada@cit.com.br', '[PMS][RECEITA] Start Proc', mensagem);
   send_mail('alexandrel@cit.com.br', '[PMS][RECEITA] Start Proc', mensagem);
   send_mail('lnoboru@cit.com.br', '[PMS][RECEITA] Start Proc', mensagem);
      --------------------------------------------
            select
                  sum(vl_rev_serv) vl_rev_serv,
                  sum(vl_rev_contr) vl_rev_contr,
                  sum(vl_rev_othr) vl_rev_othr,
                  sum(vl_rev_trav) vl_rev_trav,
                  sum(vl_tax_serv) vl_tax_serv,
                  sum(vl_tax_contr) vl_tax_contr,
                  sum(vl_tax_othr) vl_tax_othr,
                  sum(vl_tax_trav) vl_tax_trav,
                  sum(vl_rrec_serv) vl_rrec_serv,
                  sum(vl_rcon_serv) vl_rcon_serv
               into
                  log_vl_rev_serv,
                  log_vl_rev_contr,
                  log_vl_rev_othr,
                  log_vl_rev_trav,
                  log_vl_tax_serv,
                  log_vl_tax_contr,
                  log_vl_tax_othr,
                  log_vl_tax_trav,
                  log_vl_rrec_serv,
                  log_vl_rcon_serv
               from (
               select /*s.origem,
                      s.categoria,*/
                      case when s.origem = 'REV' and s.categoria = 'Services' then
                             sum(s.valor_receita_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_rev_serv,
                      case when s.origem = 'REV' and s.categoria = 'Contractors' then
                             sum(s.valor_receita_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_rev_contr,
                      case when s.origem = 'REV' and s.categoria = 'Others' then
                             sum(s.valor_receita_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_rev_othr,
                      case when s.origem = 'REV' and s.categoria = 'Travel' then
                             sum(s.valor_receita_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_rev_trav,
                      case when s.origem = 'TAX' and s.categoria = 'Services' then
                             sum(s.valor_imposto_convertido)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_tax_serv,
                      case when s.origem = 'TAX' and s.categoria = 'Contractors' then
                             sum(s.valor_imposto_convertido)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_tax_contr,
                      case when s.origem = 'TAX' and s.categoria = 'Others' then
                             sum(s.valor_imposto_convertido)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_tax_othr,
                      case when s.origem = 'TAX' and s.categoria = 'Travel' then
                             sum(s.valor_imposto_convertido)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_tax_trav,
                      case when s.origem = 'REV_CONT' and s.categoria = 'Services' then
                             sum(s.valor_rev_out_sourc_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_rcon_serv,
                      case when s.origem = 'REV_REC' and s.categoria = 'Services' then
                             sum(s.valor_rev_out_alloc_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_rrec_serv,
                    row_number() over (partition by s.origem, s.categoria order by s.origem, s.categoria) nlinha
               from vw_bi_pms_receitas_dre s
               where s.dt_mes between '01-jan-2011' and (select m.modu_dt_fechamento
                                                         from   modulo m
                                                         where  m.modu_cd_modulo = 2)
               ) where nlinha=1;

                  log_vl_rev_serv := round(log_vl_rev_serv,2);
                  log_vl_rev_contr := round(log_vl_rev_contr,2);
                  log_vl_rev_othr := round(log_vl_rev_othr,2);
                  log_vl_rev_trav := round(log_vl_rev_trav,2);
                  log_vl_tax_serv := round(log_vl_tax_serv,2);
                  log_vl_tax_contr := round(log_vl_tax_contr,2);
                  log_vl_tax_othr := round(log_vl_tax_othr,2);
                  log_vl_tax_trav := round(log_vl_tax_trav,2);
                  log_vl_rrec_serv := round(log_vl_rrec_serv,2);
                  log_vl_rcon_serv := round(log_vl_rcon_serv,2);

       if( flag = 1 or flag = 99) then
       begin
        ----------------------------------------------------------------------------------------------
        -- 1 - ORIGEM: REV CATEGORIA: Contractors
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'REV';
         log_categoria := 'Contractors';
         log_campo := 'valor_receita_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria;
         mensagem := mensagem || crlf || '1- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := log_vl_rev_contr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_vl_rev_contr/valor_pre)*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_rev_contr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_rev_contr) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              log_vl_rev_contr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        --valor_pos := 0;
       -- dbms_lock.sleep(1);
        ----------------------------------------------------------------------------------------------
        -- 2 - ORIGEM: REV CATEGORIA: Others
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'REV';
         log_categoria := 'Others';
         log_campo := 'valor_receita_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria;
         mensagem := mensagem || crlf || '2- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := log_vl_rev_othr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_vl_rev_othr/valor_pre)*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_rev_othr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_rev_othr) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              log_vl_rev_othr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        --valor_pos := 0;
        --dbms_lock.sleep(1);
        ----------------------------------------------------------------------------------------------
        -- 3 - ORIGEM: REV CATEGORIA: Services
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'REV';
         log_categoria := 'Services';
         log_campo := 'valor_receita_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria;
         mensagem := mensagem || crlf || '3- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := log_vl_rev_serv;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_vl_rev_serv/valor_pre)*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_rev_serv) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_rev_serv) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              log_vl_rev_serv,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        --valor_pos := 0;
        --dbms_lock.sleep(1);
        ----------------------------------------------------------------------------------------------
        -- 4 - ORIGEM: REV CATEGORIA: Travel
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'REV';
         log_categoria := 'Travel';
         log_campo := 'valor_receita_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria;
         mensagem := mensagem || crlf || '4- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := log_vl_rev_trav;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_vl_rev_trav/valor_pre)*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_rev_trav) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_rev_trav) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              log_vl_rev_trav,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        --valor_pos := 0;
        --dbms_lock.sleep(1);
        end;
     end if;
     if(flag = 2 or flag = 99) then
     begin
        ----------------------------------------------------------------------------------------------
        -- 5 - ORIGEM: TAX CATEGORIA: Contractors
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TAX';
         log_categoria := 'Contractors';
         log_campo := 'valor_imposto_convertido';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria;
         mensagem := mensagem || crlf || '5- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := log_vl_tax_contr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_vl_tax_contr/valor_pre)*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_tax_contr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_tax_contr) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              log_vl_tax_contr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        --valor_pos := 0;
        --dbms_lock.sleep(1);
        ----------------------------------------------------------------------------------------------
        -- 6 - ORIGEM: TAX CATEGORIA: Others
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TAX';
         log_categoria := 'Others';
         log_campo := 'valor_imposto_convertido';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria;
         mensagem := mensagem || crlf || '6- '  || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := log_vl_tax_othr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_vl_tax_othr/valor_pre)*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_tax_othr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_tax_othr) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              log_vl_tax_othr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        --valor_pos := 0;
        --dbms_lock.sleep(1);
        ----------------------------------------------------------------------------------------------
        -- 7 - ORIGEM: TAX CATEGORIA: Services
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TAX';
         log_categoria := 'Services';
         log_campo := 'valor_imposto_convertido';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria;
         mensagem := mensagem || crlf || '7- '  || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := log_vl_tax_serv;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_vl_tax_serv/valor_pre)*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_tax_serv) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_tax_serv) || crlf;
         end if;
          insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              log_vl_tax_serv,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        --valor_pos := 0;
        --dbms_lock.sleep(1);
        ----------------------------------------------------------------------------------------------
        -- 8 - ORIGEM: TAX CATEGORIA: Travel
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TAX';
         log_categoria := 'Travel';
         log_campo := 'valor_imposto_convertido';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria;
         mensagem := mensagem || crlf || '8- '  || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := log_vl_tax_trav;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_vl_tax_trav/valor_pre)*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_tax_trav) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_tax_trav) || crlf;
         end if;
          insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              log_vl_tax_trav,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        --valor_pos := 0;
        --dbms_lock.sleep(1);
       end;
      end if;
      if(flag = 3 or flag = 99) then
      begin
        ----------------------------------------------------------------------------------------------
        -- 9 - ORIGEM: REV_CONT CATEGORIA: Services
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'REV_CONT';
         log_categoria := 'Services';
         log_campo := 'valor_rev_out_sourc_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria;
         mensagem := mensagem || crlf || '9- '  || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := log_vl_rcon_serv;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_vl_rcon_serv/valor_pre)*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_rcon_serv) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_rcon_serv) || crlf;
         end if;
          insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              log_vl_rcon_serv,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        --valor_pos := 0;
        --dbms_lock.sleep(1);
        ----------------------------------------------------------------------------------------------
        -- 10 - ORIGEM: REV_REC CATEGORIA: Services
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'REV_REC';
         log_categoria := 'Services';
         log_campo := 'valor_rev_out_alloc_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria;
         mensagem := mensagem || crlf || '10- '  || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := log_vl_rrec_serv;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_vl_rrec_serv/valor_pre)*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_rrec_serv) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_vl_rrec_serv) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              log_vl_rrec_serv,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        --valor_pos := 0;
        --dbms_lock.sleep(1);
       end;
     end if;
   mensagem := mensagem || crlf || crlf || '=== Fim da Carga ' ||to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===';
   mensagem := mensagem || crlf ||'select * from tb_bi_pms_log_sum_cons_dre order by 2 desc';
   assunto  := assunto || ' [PMS][RECEITA] Resultado da Carga';
   send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
   send_mail('lnoboru@cit.com.br', assunto, mensagem);
   if log_status = 'ERRO' then
      mensagem := 'ATTENTION: Please, contact Ciandt IT Business Intelligence Team as soon as possible. ' || crlf || crlf ||
                  mensagem;
--      send_mail('infra_ti@cit.com.br', assunto, mensagem);
   end if;
end USP_PMS_RECEITA;