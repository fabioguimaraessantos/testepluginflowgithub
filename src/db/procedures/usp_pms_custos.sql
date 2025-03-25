CREATE OR REPLACE PROCEDURE USP_PMS_CUSTOS (flag number) as
   log_data              date := trunc(sysdate);
--INTERVAL:
   -- FLAG
   -- 1 = ITCHBK
   -- 2 = CDIR
   -- 3 = COST_EMP e COST_CONT
   -- 4 = CIND
   -- 5 = COSTF_LICENCA_UMKT e COSTF_LICENCA_LOB
   -- 6 = HEXTPL
   -- 99 = TODOS
   -- Procedure de carga
        -- 1 - ORIGEM: ITCHBK CATEGORIA: TI CAMPO: valor_custo_unit_ti_rec
        -- 2 - ORIGEM: ITCHBK CATEGORIA: TI CAMPO: valor_custo_item
        -- 3 - ORIGEM: CDIR CATEGORIA: Services CAMPO: valor_custo_dir_tce_brl
        -- 4 - ORIGEM: CDIR CATEGORIA: Services CAMPO: valor_custo_dir_infra_brl
        -- 5 - ORIGEM: CDIR CATEGORIA: Services CAMPO: valor_fte_cdir
        -- 6 - ORIGEM: CDIR CATEGORIA: Services CAMPO: valor_fte_ferias_cdir
        -- 7 - ORIGEM: CDIR CATEGORIA: Services CAMPO: valor_fte_licenca
        -- 8 - ORIGEM: COST_EMP CATEGORIA: Services CAMPO: valor_custo_out_sourc_brl
        -- 9 - ORIGEM: COST_CONT CATEGORIA: Services CAMPO: valor_custo_out_alloc_brl
        -- 10 - ORIGEM: CIND CATEGORIA: Services CAMPO: valor_custo_ind_tce_brl
        -- 11 - ORIGEM: CIND CATEGORIA: Services CAMPO: valor_custo_ind_infra_brl
        -- 12 - ORIGEM: CIND CATEGORIA: Services CAMPO: valor_fte_cind
        -- 13 - ORIGEM: CIND CATEGORIA: Services CAMPO: valor_fte_ferias_cind
        -- 14 - ORIGEM: COSTF_LICENCA_UMKT CATEGORIA: Services CAMPO: valor_custlic_rateio_umkt_brl
        -- 15 - ORIGEM: COSTF_LICENCA_LOB CATEGORIA: Services CAMPO: valor_custlic_rateio_umkt_brl
        -- 16 - ORIGEM: HEXTPL CATEGORIA: Services CAMPO: valor_hora_extra
        -- 17 - ORIGEM: HEXTPL CATEGORIA: Services CAMPO: valor_plantao
   -- Informac?es para LOG
   log_dt_carga_processo date;
   log_status                varchar2(30) := '';
   log_origem                varchar2(50) := '';
   log_categoria             varchar2(50) := '';
   log_campo                 varchar2(150):= '';
   log_diferenca             number;
   log_descricao             varchar2(200) := '';
   log_sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'USP_PMS_CUSTOS';
   --
   crlf      varchar2(2) := chr(13) || chr(10); -- <ENTER>
   assunto   varchar2(200) := '';
   mensagem  varchar2(4000) := '=== Inicio da Carga ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===' ;
   valor_pre integer;
   --VARIAVEIS PARA LOG
   log_ITCHBK_IT_unitti integer;
   log_ITCHBK_IT_item integer;
   log_CDIR_Serv_tce integer;
   log_CDIR_Serv_infra integer;
   log_CDIR_Serv_fte integer;
   log_CDIR_Serv_ferias integer;
   log_CDIR_Serv_licenca integer;
   log_COST_EMP_Serv integer;
   log_COST_CONT_Serv integer;
   log_CIND_Serv_tce integer;
   log_CIND_Serv_infra integer;
   log_CIND_Serv_fte integer;
   log_CIND_Serv_ferias integer;
   log_COSTF_LICENCA_UMKT_Serv integer;
   log_HEXTPL_Serv_horaextra integer;
   log_HEXTPL_Serv_plantao integer;
   log_COSTF_LICENCA_LOB integer;
   --   sms_msg   varchar2(200) := 'Objeto: ';

begin
   --------------------------------------------------------
   -- VALIDACAO VW_BI_PMS_RECEITAS
   --------------------------------------------------------
   mensagem := mensagem || crlf || ':: PROCEDURE: ' || nome_proc || ' ::' || crlf || 'FLAG: '||flag || crlf || crlf;
   send_mail('lahumada@cit.com.br', '[PMS][CUSTOS] Start Proc', mensagem);
   send_mail('alexandrel@cit.com.br', '[PMS][CUSTOS] Start Proc', mensagem);
   send_mail('lnoboru@cit.com.br', '[PMS][CUSTOS] Start Proc', mensagem);
      --------------------------------------------
              select
                  sum(vl_ITCHBK_IT_unitti) vl_ITCHBK_IT_unitti,
                  sum(vl_ITCHBK_IT_item) vl_ITCHBK_IT_item,
                  sum(vl_CDIR_Serv_tce) vl_CDIR_Serv_tce,
                  sum(vl_CDIR_Serv_infra) vl_CDIR_Serv_infra,
                  sum(vl_CDIR_Serv_fte) vl_CDIR_Serv_fte,
                  sum(vl_CDIR_Serv_ferias) vl_CDIR_Serv_ferias,
                  sum(vl_CDIR_Serv_licenca) vl_CDIR_Serv_licenca,
                  sum(vl_COST_EMP_Serv) vl_COST_EMP_Serv,
                  sum(vl_COST_CONT_Serv) vl_COST_CONT_Serv,
                  sum(vl_CIND_Serv_tce) vl_CIND_Serv_tce,
                  sum(vl_CIND_Serv_infra) vl_CIND_Serv_infra,
                  sum(vl_CIND_Serv_fte) vl_CIND_Serv_fte,
                  sum(vl_CIND_Serv_ferias) vl_CIND_Serv_ferias,
                  sum(vl_COSTF_LICENCA_UMKT_Serv) vl_COSTF_LICENCA_UMKT_Serv,
                  sum(vl_HEXTPL_Serv_horaextra) vl_HEXTPL_Serv_horaextra,
                  sum(vl_HEXTPL_Serv_plantao) vl_HEXTPL_Serv_plantao,
                  sum(vl_COSTF_LICENCA_LOB) vl_COSTF_LICENCA_LOB
             into
                  log_ITCHBK_IT_unitti,
                  log_ITCHBK_IT_item,
                  log_CDIR_Serv_tce,
                  log_CDIR_Serv_infra,
                  log_CDIR_Serv_fte,
                  log_CDIR_Serv_ferias,
                  log_CDIR_Serv_licenca,
                  log_COST_EMP_Serv,
                  log_COST_CONT_Serv,
                  log_CIND_Serv_tce,
                  log_CIND_Serv_infra,
                  log_CIND_Serv_fte,
                  log_CIND_Serv_ferias,
                  log_COSTF_LICENCA_UMKT_Serv,
                  log_HEXTPL_Serv_horaextra,
                  log_HEXTPL_Serv_plantao,
                  log_COSTF_LICENCA_LOB
               from (
               select /*s.origem,
                      s.categoria,*/
                      case when s.origem = 'ITCHBK' and s.categoria = 'IT' then
                             sum(s.valor_custo_unit_ti_rec)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_ITCHBK_IT_unitti,
                      case when s.origem = 'ITCHBK' and s.categoria = 'IT' then
                             sum(s.valor_custo_item)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_ITCHBK_IT_item,
                      case when s.origem = 'CDIR' and s.categoria = 'Services' then
                             sum(s.valor_custo_dir_tce_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_CDIR_Serv_tce,
                      case when s.origem = 'CDIR' and s.categoria = 'Services' then
                             sum(s.valor_custo_dir_infra_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_CDIR_Serv_infra,
                      case when s.origem = 'CDIR' and s.categoria = 'Services' then
                             sum(s.valor_fte_cdir)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_CDIR_Serv_fte,
                      case when s.origem = 'CDIR' and s.categoria = 'Services' then
                             sum(s.valor_fte_ferias_cdir)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_CDIR_Serv_ferias,
                      case when s.origem = 'CDIR' and s.categoria = 'Services' then
                             sum(s.valor_fte_licenca)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_CDIR_Serv_licenca,
                      case when s.origem = 'COST_EMP' and s.categoria = 'Services' then
                             sum(s.valor_custo_out_sourc_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_COST_EMP_Serv,
                      case when s.origem = 'COST_CONT' and s.categoria = 'Services' then
                             sum(s.valor_custo_out_alloc_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_COST_CONT_Serv,
                      case when s.origem = 'CIND' and s.categoria = 'Services' then
                             sum(s.valor_custo_ind_tce_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_CIND_Serv_tce,
                      case when s.origem = 'CIND' and s.categoria = 'Services' then
                             sum(s.valor_custo_ind_infra_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_CIND_Serv_infra,
                      case when s.origem = 'CIND' and s.categoria = 'Services' then
                             sum(s.valor_fte_cind)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_CIND_Serv_fte,
                      case when s.origem = 'CIND' and s.categoria = 'Services' then
                             sum(s.valor_fte_ferias_cind)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_CIND_Serv_ferias,
                      case when s.origem = 'COSTF_LICENCA_UMKT' and s.categoria = 'Services' then
                             sum(s.valor_custlic_rateio_umkt_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_COSTF_LICENCA_UMKT_Serv,
                      case when s.origem = 'HEXTPL' and s.categoria = 'Services' then
                             sum(s.valor_hora_extra)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_HEXTPL_Serv_horaextra,
                      case when s.origem = 'HEXTPL' and s.categoria = 'Services' then
                             sum(s.valor_plantao)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_HEXTPL_Serv_plantao,
                      case when s.origem = 'COSTF_LICENCA_LOB' and s.categoria = 'Services' then
                             sum(s.valor_custlic_ratprat_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_COSTF_LICENCA_LOB,
                    row_number() over (partition by s.origem, s.categoria order by s.origem, s.categoria) nlinha
               from vw_bi_pms_custos_dre s
               where s.dt_mes between '01-jan-2011' and (select m.modu_dt_fechamento
                                                         from   modulo m
                                                         where  m.modu_cd_modulo = 2)
               ) where nlinha=1;

                  log_ITCHBK_IT_unitti := round(log_ITCHBK_IT_unitti,2);
                  log_ITCHBK_IT_item := round(log_ITCHBK_IT_item,2);
                  log_CDIR_Serv_tce := round(log_CDIR_Serv_tce,2);
                  log_CDIR_Serv_infra := round(log_CDIR_Serv_infra,2);
                  log_CDIR_Serv_fte := round(log_CDIR_Serv_fte,2);
                  log_CDIR_Serv_ferias := round(log_CDIR_Serv_ferias,2);
                  log_CDIR_Serv_licenca := round(log_CDIR_Serv_licenca,2);
                  log_COST_EMP_Serv := round(log_COST_EMP_Serv,2);
                  log_COST_CONT_Serv := round(log_COST_CONT_Serv,2);
                  log_CIND_Serv_tce := round(log_CIND_Serv_tce,2);
                  log_CIND_Serv_infra := round(log_CIND_Serv_infra,2);
                  log_CIND_Serv_fte := round(log_CIND_Serv_fte,2);
                  log_CIND_Serv_ferias := round(log_CIND_Serv_ferias,2);
                  log_COSTF_LICENCA_UMKT_Serv := round(log_COSTF_LICENCA_UMKT_Serv,2);
                  log_HEXTPL_Serv_horaextra := round(log_HEXTPL_Serv_horaextra,2);
                  log_HEXTPL_Serv_plantao := round(log_HEXTPL_Serv_plantao,2);
                  log_COSTF_LICENCA_LOB := round(log_COSTF_LICENCA_LOB,2);

       if( flag = 1 or flag = 99) then
       begin
        ----------------------------------------------------------------------------------------------
        -- 1 - ORIGEM: ITCHBK CATEGORIA: TI CAMPO: valor_custo_unit_ti_rec
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'ITCHBK';
         log_categoria := 'Services';
         log_campo := 'valor_custo_unit_ti_rec';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
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
                valor_pre := log_ITCHBK_IT_unitti;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_ITCHBK_IT_unitti/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_ITCHBK_IT_unitti) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_ITCHBK_IT_unitti) || crlf;
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
              log_ITCHBK_IT_unitti,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       -- dbms_lock.sleep(1);
       ----------------------------------------------------------------------------------------------
        -- 2 - ORIGEM: ITCHBK CATEGORIA: TI CAMPO: valor_custo_item
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'ITCHBK';
         log_categoria := 'Services';
         log_campo := 'valor_custo_item';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
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
                valor_pre := log_ITCHBK_IT_item;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_ITCHBK_IT_item/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_ITCHBK_IT_item) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_ITCHBK_IT_item) || crlf;
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
              log_ITCHBK_IT_item,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       -- dbms_lock.sleep(1);

       end;
     end if;
     if(flag = 2 or flag = 99) then
     begin
        ----------------------------------------------------------------------------------------------
        -- 3 - ORIGEM: CDIR CATEGORIA: Services CAMPO: valor_custo_dir_tce_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'CDIR';
         log_categoria := 'Services';
         log_campo := 'valor_custo_dir_tce_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
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
                valor_pre := log_CDIR_Serv_tce;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_CDIR_Serv_tce/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CDIR_Serv_tce) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CDIR_Serv_tce) || crlf;
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
              log_CDIR_Serv_tce,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 4 - ORIGEM: CDIR CATEGORIA: Services CAMPO: valor_custo_dir_infra_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'CDIR';
         log_categoria := 'Services';
         log_campo := 'valor_custo_dir_infra_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
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
                valor_pre := log_CDIR_Serv_infra;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_CDIR_Serv_infra/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CDIR_Serv_infra) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CDIR_Serv_infra) || crlf;
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
              log_CDIR_Serv_infra,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 5 - ORIGEM: CDIR CATEGORIA: Services CAMPO: valor_fte_cdir
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'CDIR';
         log_categoria := 'Services';
         log_campo := 'valor_fte_cdir';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
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
                valor_pre := log_CDIR_Serv_fte;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_CDIR_Serv_fte/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CDIR_Serv_fte) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CDIR_Serv_fte) || crlf;
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
              log_CDIR_Serv_fte,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 6 - ORIGEM: CDIR CATEGORIA: Services CAMPO: valor_fte_ferias_cdir
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'CDIR';
         log_categoria := 'Services';
         log_campo := 'valor_fte_ferias_cdir';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '6- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := log_CDIR_Serv_ferias;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_CDIR_Serv_ferias/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CDIR_Serv_ferias) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CDIR_Serv_ferias) || crlf;
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
              log_CDIR_Serv_ferias,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 7 - ORIGEM: CDIR CATEGORIA: Services CAMPO: valor_fte_licenca
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'CDIR';
         log_categoria := 'Services';
         log_campo := 'valor_fte_licenca';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '7- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := log_CDIR_Serv_licenca;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_CDIR_Serv_licenca/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CDIR_Serv_licenca) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CDIR_Serv_licenca) || crlf;
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
              log_CDIR_Serv_licenca,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        end;
     end if;
     if(flag = 3 or flag = 99) then
     begin
        ----------------------------------------------------------------------------------------------
        -- 8 - ORIGEM: COST_EMP CATEGORIA: Services CAMPO: valor_custo_out_sourc_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COST_EMP';
         log_categoria := 'Services';
         log_campo := 'valor_custo_out_sourc_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '8- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := log_COST_EMP_Serv;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_COST_EMP_Serv/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_COST_EMP_Serv) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_COST_EMP_Serv) || crlf;
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
              log_COST_EMP_Serv,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 9 - ORIGEM: COST_CONT CATEGORIA: Services CAMPO: valor_custo_out_alloc_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COST_CONT';
         log_categoria := 'Services';
         log_campo := 'valor_custo_out_alloc_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '9- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := log_COST_CONT_Serv;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_COST_CONT_Serv/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_COST_CONT_Serv) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_COST_CONT_Serv) || crlf;
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
              log_COST_CONT_Serv,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       end;
      end if;
      if(flag = 4 or flag = 99) then
      begin
        ----------------------------------------------------------------------------------------------
        -- 10 - ORIGEM: CIND CATEGORIA: Services CAMPO: valor_custo_ind_tce_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'CIND';
         log_categoria := 'Services';
         log_campo := 'valor_custo_ind_tce_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '10- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := log_CIND_Serv_tce;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_CIND_Serv_tce/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CIND_Serv_tce) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CIND_Serv_tce) || crlf;
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
              log_CIND_Serv_tce,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 11 - ORIGEM: CIND CATEGORIA: Services CAMPO: valor_custo_ind_infra_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'CIND';
         log_categoria := 'Services';
         log_campo := 'valor_custo_ind_infra_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '11- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := log_CIND_Serv_infra;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_CIND_Serv_infra/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CIND_Serv_infra) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CIND_Serv_infra) || crlf;
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
              log_CIND_Serv_infra,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 12 - ORIGEM: CIND CATEGORIA: Services CAMPO: valor_fte_cind
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'CIND';
         log_categoria := 'Services';
         log_campo := 'valor_fte_cind';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '12- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := log_CIND_Serv_fte;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_CIND_Serv_fte/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CIND_Serv_fte) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CIND_Serv_fte) || crlf;
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
              log_CIND_Serv_fte,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 13 - ORIGEM: CIND CATEGORIA: Services CAMPO: valor_fte_ferias_cind
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'CIND';
         log_categoria := 'Services';
         log_campo := 'valor_fte_ferias_cind';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '13- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := log_CIND_Serv_ferias;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_CIND_Serv_ferias/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CIND_Serv_ferias) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_CIND_Serv_ferias) || crlf;
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
              log_CIND_Serv_ferias,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       end;
      end if;
      if(flag = 5 or flag = 99) then
      begin
        ----------------------------------------------------------------------------------------------
        -- 14 - ORIGEM: COSTF_LICENCA_UMKT CATEGORIA: Services CAMPO: valor_custlic_rateio_umkt_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_LICENCA_UMKT';
         log_categoria := 'Services';
         log_campo := 'valor_custlic_rateio_umkt_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '14- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := log_COSTF_LICENCA_UMKT_Serv;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_COSTF_LICENCA_UMKT_Serv/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_COSTF_LICENCA_UMKT_Serv) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_COSTF_LICENCA_UMKT_Serv) || crlf;
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
              log_COSTF_LICENCA_UMKT_Serv,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 15 - ORIGEM: COSTF_LICENCA_LOB CATEGORIA: Services CAMPO: valor_custlic_rateio_umkt_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_LICENCA_LOB';
         log_categoria := 'Services';
         log_campo := 'valor_custlic_ratprat_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '15- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := log_COSTF_LICENCA_LOB;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_COSTF_LICENCA_LOB/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_COSTF_LICENCA_LOB) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_COSTF_LICENCA_LOB) || crlf;
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
              log_COSTF_LICENCA_LOB,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       end;
      end if;
      if(flag = 6 or flag = 99) then
      begin
        ----------------------------------------------------------------------------------------------
        -- 16 - ORIGEM: HEXTPL CATEGORIA: Services CAMPO: valor_hora_extra
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'HEXTPL';
         log_categoria := 'Services';
         log_campo := 'valor_hora_extra';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '16- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := log_HEXTPL_Serv_horaextra;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_HEXTPL_Serv_horaextra/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_HEXTPL_Serv_horaextra) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_HEXTPL_Serv_horaextra) || crlf;
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
              log_HEXTPL_Serv_horaextra,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 17 - ORIGEM: HEXTPL CATEGORIA: Services CAMPO: valor_plantao
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'HEXTPL';
         log_categoria := 'Services';
         log_campo := 'valor_plantao';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '17- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := log_HEXTPL_Serv_plantao;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((log_HEXTPL_Serv_plantao/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_HEXTPL_Serv_plantao) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(log_HEXTPL_Serv_plantao) || crlf;
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
              log_HEXTPL_Serv_plantao,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       end;
     end if;
   mensagem := mensagem || crlf || crlf || '=== Fim da Carga ' ||to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===';
   mensagem := mensagem || crlf ||'select * from tb_bi_pms_log_sum_cons_dre where nome_proc '||nome_proc||' order by 3 desc';
   assunto  := assunto || ' [PMS][CUSTOS] Resultado da Carga';
   send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
   send_mail('lnoboru@cit.com.br', assunto, mensagem);
   if log_status = 'ERRO' then
      mensagem := 'ATTENTION: Please, contact Ciandt IT Business Intelligence Team as soon as possible. ' || crlf || crlf ||
                  mensagem;
--      send_mail('infra_ti@cit.com.br', assunto, mensagem);
   end if;
end USP_PMS_CUSTOS;