CREATE OR REPLACE PROCEDURE USP_PMS_DESPESAS (flag number) as
   log_data              date := trunc(sysdate);
--INTERVAL:
   -- FLAG
   -- 1 = DESPF_LOB
   -- 2 = DESPF_UMKT
   -- 3 = DESP_REL
   -- 4 = DESPCONTQUAL
   -- 99 = TODOS
   -- Procedure de carga
        -- 1 - ORIGEM: DESPCONTQUAL CATEGORIA: Services
        -- 2 - ORIGEM: DESPF_LOB CATEGORIA: Contractors
        -- 3 - ORIGEM: DESPF_LOB CATEGORIA: Others
        -- 4 - ORIGEM: DESPF_LOB CATEGORIA: Travel
        -- 5 - ORIGEM: DESPF_UMKT CATEGORIA: Contractors
        -- 6 - ORIGEM: DESPF_UMKT CATEGORIA: Others
        -- 7 - ORIGEM: DESPF_UMKT CATEGORIA: Travel
        -- 8 - ORIGEM: DESP_REL CATEGORIA: Contractors
        -- 9 - ORIGEM: DESP_REL CATEGORIA: Others
        -- 10 - ORIGEM: DESP_REL CATEGORIA: Travel
   -- Informac?es para LOG
   log_dt_carga_processo date;
   log_status                varchar2(30) := '';
   log_origem                varchar2(50) := '';
   log_categoria             varchar2(50) := '';
   log_diferenca             number;
   log_descricao             varchar2(200) := '';
   log_sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'USP_PMS_DESPESAS';
   --
   crlf      varchar2(2) := chr(13) || chr(10); -- <ENTER>
   assunto   varchar2(200) := '';
   mensagem  varchar2(3000) := '=== Inicio da Carga ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===' ;
   valor_pre integer;
   log_DESPCONTQUAL_Serv  number;
   log_DESPF_LOB_Cont  number;
   log_DESPF_LOB_Othe  number;
   log_DESPF_LOB_Trav  number;
   log_DESPF_UMKT_Cont  number;
   log_DESPF_UMKT_Othe  number;
   log_DESPF_UMKT_Trav  number;
   log_DESP_REL_Cont  number;
   log_DESP_REL_Othe  number;
   log_DESP_REL_Trav  number;
   log_campo varchar2(100);
   --   sms_msg   varchar2(200) := 'Objeto: ';

begin
   --------------------------------------------------------
   -- VALIDACAO VW_BI_PMS_RECEITAS
   --------------------------------------------------------
   mensagem := mensagem || crlf || ':: PROCEDURE: ' || nome_proc || ' ::' || crlf || 'FLAG: '||flag || crlf || crlf;
   send_mail('lahumada@cit.com.br', '[PMS][DESPESAS] Start Proc', mensagem);
   send_mail('alexandrel@cit.com.br', '[PMS][DESPESAS] Start Proc', mensagem);
   send_mail('lnoboru@cit.com.br', '[PMS][DESPESAS] Start Proc', mensagem);
      --------------------------------------------
             select
                  sum(vl_DESPCONTQUAL_serv) vl_DESPCONTQUAL_serv,
                  sum(vl_DESPF_LOB_contr) vl_DESPF_LOB_contr,
                  sum(vl_DESPF_LOB_othr) vl_DESPF_LOB_othr,
                  sum(vl_DESPF_LOB_trav) vl_DESPF_LOB_trav,
                  sum(vl_DESPF_UMKT_contr) vl_DESPF_UMKT_contr,
                  sum(vl_DESPF_UMKT_othr) vl_DESPF_UMKT_othr,
                  sum(vl_DESPF_UMKT_trav) vl_DESPF_UMKT_trav,
                  sum(vl_DESP_REL_contr) vl_DESP_REL_contr,
                  sum(vl_DESP_REL_othr) vl_DESP_REL_othr,
                  sum(vl_DESP_REL_trav) vl_DESP_REL_trav
              into
                  log_DESPCONTQUAL_Serv,
                  log_DESPF_LOB_Cont,
                  log_DESPF_LOB_Othe,
                  log_DESPF_LOB_Trav,
                  log_DESPF_UMKT_Cont,
                  log_DESPF_UMKT_Othe,
                  log_DESPF_UMKT_Trav,
                  log_DESP_REL_Cont,
                  log_DESP_REL_Othe,
                  log_DESP_REL_Trav
               from (
               select /*s.origem,
                      s.categoria,*/
                      case when s.origem = 'DESPCONTQUAL' and s.categoria = 'Services' then
                             sum(s.vlr_quality_costs_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_DESPCONTQUAL_serv,
                      case when s.origem = 'DESPF_LOB' and s.categoria = 'Contractors' then
                             sum(s.vlr_despfix_lob_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_DESPF_LOB_contr,
                      case when s.origem = 'DESPF_LOB' and s.categoria = 'Others' then
                             sum(s.vlr_despfix_lob_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_DESPF_LOB_othr,
                      case when s.origem = 'DESPF_LOB' and s.categoria = 'Travel' then
                             sum(s.vlr_despfix_lob_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_DESPF_LOB_trav,

                      case when s.origem = 'DESPF_UMKT' and s.categoria = 'Contractors' then
                             sum(s.valor_despfix_ratumkt_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_DESPF_UMKT_contr,
                      case when s.origem = 'DESPF_UMKT' and s.categoria = 'Others' then
                             sum(s.valor_despfix_ratumkt_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_DESPF_UMKT_othr,
                      case when s.origem = 'DESPF_UMKT' and s.categoria = 'Travel' then
                             sum(s.valor_despfix_ratumkt_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_DESPF_UMKT_trav,

                      case when s.origem = 'DESP_REL' and s.categoria = 'Contractors' then
                             sum(s.valor_desp_rel_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_DESP_REL_contr,
                      case when s.origem = 'DESP_REL' and s.categoria = 'Others' then
                             sum(s.valor_desp_rel_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_DESP_REL_othr,
                      case when s.origem = 'DESP_REL' and s.categoria = 'Travel' then
                             sum(s.valor_desp_rel_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_DESP_REL_trav,
                    row_number() over (partition by s.origem, s.categoria order by s.origem, s.categoria) nlinha
               from vw_bi_pms_despesas_dre s
               where s.dt_mes between '01-jan-2011' and (select m.modu_dt_fechamento
                                                         from   modulo m
                                                         where  m.modu_cd_modulo = 2)
               ) where nlinha=1;

                  log_DESPCONTQUAL_Serv := round(log_DESPCONTQUAL_Serv,2);
                  log_DESPF_LOB_Cont := round(log_DESPF_LOB_Cont,2);
                  log_DESPF_LOB_Othe := round(log_DESPF_LOB_Othe,2);
                  log_DESPF_LOB_Trav := round(log_DESPF_LOB_Trav,2);
                  log_DESPF_UMKT_Cont := round(log_DESPF_UMKT_Cont,2);
                  log_DESPF_UMKT_Othe := round(log_DESPF_UMKT_Othe,2);
                  log_DESPF_UMKT_Trav := round(log_DESPF_UMKT_Trav,2);
                  log_DESP_REL_Cont := round(log_DESP_REL_Cont,2);
                  log_DESP_REL_Othe := round(log_DESP_REL_Othe,2);
                  log_DESP_REL_Trav := round(log_DESP_REL_Trav,2);

       if( flag = 4 or flag = 99) then
       begin
        ----------------------------------------------------------------------------------------------
        -- 1 - ORIGEM: DESPCONTQUAL CATEGORIA: Services
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPCONTQUAL';
         log_categoria := 'Services';
         log_campo := 'vlr_quality_costs_brl';
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
                valor_pre := log_DESPCONTQUAL_Serv;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_DESPCONTQUAL_Serv/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPCONTQUAL_Serv) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPCONTQUAL_Serv) || crlf;
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
              log_DESPCONTQUAL_Serv,
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
       end;
     end if;
     if(flag = 1 or flag = 99) then
     begin
        ----------------------------------------------------------------------------------------------
        -- 2 - ORIGEM: DESPF_LOB CATEGORIA: Contractors
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPF_LOB';
         log_categoria := 'Contractors';
         log_campo := 'vlr_despfix_lob_brl';
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
                valor_pre := log_DESPF_LOB_Cont;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_DESPF_LOB_Cont/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPF_LOB_Cont) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPF_LOB_Cont) || crlf;
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
              log_DESPF_LOB_Cont,
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
        -- 3 - ORIGEM: DESPF_LOB CATEGORIA: Others
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPF_LOB';
         log_categoria := 'Others';
         log_campo := 'vlr_despfix_lob_brl';
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
                valor_pre := log_DESPF_LOB_Othe;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_DESPF_LOB_Othe/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPF_LOB_Othe) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPF_LOB_Othe) || crlf;
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
              log_DESPF_LOB_Othe,
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
        -- 4 - ORIGEM: DESPF_LOB CATEGORIA: Travel
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPF_LOB';
         log_categoria := 'Travel';
         log_campo := 'vlr_despfix_lob_brl';
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
                valor_pre := log_DESPF_LOB_Trav;
         end;

         -- VALIDACAO
         log_diferenca := TRUNC(((log_DESPF_LOB_Trav/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPF_LOB_Trav) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPF_LOB_Trav) || crlf;
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
              log_DESPF_LOB_Trav,
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
        -- 5 - ORIGEM: DESPF_UMKT CATEGORIA: Contractors
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPF_UMKT';
         log_categoria := 'Contractors';
         log_campo := 'valor_despfix_ratumkt_brl';
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
                valor_pre := log_DESPF_UMKT_Cont;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_DESPF_UMKT_Cont/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPF_UMKT_Cont) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPF_UMKT_Cont) || crlf;
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
              log_DESPF_UMKT_Cont,
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
        -- 6 - ORIGEM: DESPF_UMKT CATEGORIA: Others
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPF_UMKT';
         log_categoria := 'Others';
         log_campo := 'valor_despfix_ratumkt_brl';
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
                valor_pre := log_DESPF_UMKT_Othe;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_DESPF_UMKT_Othe/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPF_UMKT_Othe) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPF_UMKT_Othe) || crlf;
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
              log_DESPF_UMKT_Othe,
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
        -- 7 - ORIGEM: DESPF_UMKT CATEGORIA: Travel
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPF_UMKT';
         log_categoria := 'Travel';
         log_campo := 'valor_despfix_ratumkt_brl';
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
                valor_pre := log_DESPF_UMKT_Trav;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_DESPF_UMKT_Trav/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPF_UMKT_Trav) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESPF_UMKT_Trav) || crlf;
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
              log_DESPF_UMKT_Trav,
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
        -- 8 - ORIGEM: DESP_REL CATEGORIA: Contractors
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESP_REL';
         log_categoria := 'Contractors';
         log_campo := 'valor_desp_rel_brl';
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
                valor_pre := log_DESP_REL_Cont;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_DESP_REL_Cont/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESP_REL_Cont) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESP_REL_Cont) || crlf;
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
              log_DESP_REL_Cont,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        --dbms_lock.sleep(1);

        ----------------------------------------------------------------------------------------------
        -- 9 - ORIGEM: DESP_REL CATEGORIA: Others
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESP_REL';
         log_categoria := 'Others';
         log_campo := 'valor_desp_rel_brl';
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
                valor_pre := log_DESP_REL_Othe;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_DESP_REL_Othe/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESP_REL_Othe) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESP_REL_Othe) || crlf;
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
              log_DESP_REL_Othe,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        --dbms_lock.sleep(1);
        ----------------------------------------------------------------------------------------------
        -- 10 - ORIGEM: DESP_REL CATEGORIA: Travel
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESP_REL';
         log_categoria := 'Travel';
         log_campo := 'valor_desp_rel_brl';
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
                valor_pre := log_DESP_REL_Trav;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_DESP_REL_Trav/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESP_REL_Trav) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_DESP_REL_Trav) || crlf;
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
              log_DESP_REL_Trav,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        --dbms_lock.sleep(1);
       end;
     end if;
   mensagem := mensagem || crlf || crlf || '=== Fim da Carga ' ||to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===';
   mensagem := mensagem || crlf ||'select * from pms.tb_bi_pms_log_receitas where nome_proc ''USP_PMS_DESPESAS'' order by 3 desc';
   assunto  := assunto || ' [PMS][DESPESAS] Resultado da Carga';
   send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
   send_mail('lnoboru@cit.com.br', assunto, mensagem);
   if log_status = 'ERRO' then
      mensagem := 'ATTENTION: Please, contact Ciandt IT Business Intelligence Team as soon as possible. ' || crlf || crlf ||
                  mensagem;
--      send_mail('infra_ti@cit.com.br', assunto, mensagem);
   end if;
end USP_PMS_DESPESAS;