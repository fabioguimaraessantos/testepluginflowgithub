CREATE OR REPLACE PROCEDURE USP_PMS_CUSTFIXO (flag number) as
   log_data              date := trunc(sysdate);
--INTERVAL:
   -- FLAG
   -- 1 = COSTF_LOB
   -- 2 = COSTF_LOB_SEM_ALOC
   -- 3 = COSTF_UMKT
   -- 4 = COSTF_UMKT_SEM_ALOC
   -- 99 = TODOS
   -- Procedure de carga
        -- 1 - ORIGEM: COSTF_LOB CATEGORIA: Services CAMPO: vl_C_L_cusfixratpra
        -- 2 - ORIGEM: COSTF_LOB CATEGORIA: Services CAMPO: valor_custfix_ratprat_brl
        -- 3 - ORIGEM: COSTF_LOB CATEGORIA: Services CAMPO: VALOR_FTE_CUSTFIX_RATPRAT
        -- 4 - ORIGEM: COSTF_LOB_SEM_ALOC CATEGORIA: Services CAMPO: valor_custfix_rateio_prat
        -- 5 - ORIGEM: COSTF_LOB_SEM_ALOC CATEGORIA: Services CAMPO: valor_tce_custfix_ratprat
        -- 6 - ORIGEM: COSTF_LOB_SEM_ALOC CATEGORIA: Services CAMPO: valor_custinfra_fix_ratprat
        -- 7 - ORIGEM: COSTF_LOB_SEM_ALOC CATEGORIA: Services CAMPO: valor_custfix_ratprat_brl
        -- 8 - ORIGEM: COSTF_LOB_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_FTE_CUSTFIX_RATPRAT
        -- 9 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_CUSTFIX_RATEIO_UMKT
        -- 10 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_TCE_CUSTFIX_RATUMKT
        -- 11 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_CUSTINFRA_FIX_RATUMKT
        -- 12 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_CUSTFIX_RATEIO_UMKT_BRL
        -- 13 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_TCE_CUSTFIX_RATUMKT_BRL
        -- 14 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_CUSTINF_FIX_RATUMKT_BRL
        -- 15 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_CUSTFIX_UMKT_FERIAS
        -- 16 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_FTE_CUSTFIX_RATUMKT
        -- 17 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_CUSTFIX_RATEIO_UMKT
        -- 18 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_TCE_CUSTFIX_RATUMKT
        -- 19 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_CUSTINFRA_FIX_RATUMKT
        -- 20 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_CUSTFIX_RATEIO_UMKT_BRL
        -- 21 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_TCE_CUSTFIX_RATUMKT_BRL
        -- 22 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_CUSTINF_FIX_RATUMKT_BRL
        -- 23 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_FTE_CUSTFIX_RATUMKT
   -- Informac?es para LOG
   log_dt_carga_processo date;
   log_status                varchar2(30) := '';
   log_origem                varchar2(50) := '';
   log_categoria             varchar2(50) := '';
   log_campo                 varchar2(150):= '';
   log_diferenca             number;
   log_descricao             varchar2(200) := '';
   log_sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'USP_PMS_CUSTFIXO';
   --
   crlf      varchar2(2) := chr(13) || chr(10); -- <ENTER>
   assunto   varchar2(200) := '';
   mensagem  varchar2(5300) := '=== Inicio da Carga ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===' ;
   valor_pre integer;
   --VARIAVEIS PARA LOG
lo_C_L_cusfixratpra integer;
lo_C_L_cusfixratprablr integer;
lo_C_L_ftecusfixratpra integer;
lo_C_L_S_A_cusfixratpra integer;
lo_C_L_S_A_tcecusfixratpra integer;
lo_C_L_S_A_cusinffix integer;
lo_C_L_S_A_cusfixratprabrl integer;
lo_C_L_S_A_ftecusfixratpra integer;
lo_C_U_cusfixratumkt integer;
lo_C_U_tcecusfixratumkt integer;
lo_C_U_cusinfrafixratumkt integer;
lo_C_U_cusfixratumktbrl integer;
lo_C_U_tcecusfixratumktbrl integer;
lo_C_U_cusinffixratumktbrl integer;
lo_C_U_cusfixumktferias integer;
lo_C_U_ftecusfixratumkt integer;
lo_C_U_S_A_cusfixratumkt integer;
lo_C_U_S_A_tcecusfixratumkt integer;
lo_C_U_S_A_cusinfrafixratumkt integer;
lo_C_U_S_A_cusfixratumktbrl integer;
lo_C_U_S_A_tcecusratumktblr integer;
lo_C_U_S_A_cusinffixratumktbrl integer;
lo_C_U_S_A_ftecusfixratumkt integer;

begin
   --------------------------------------------------------
   -- VALIDACAO VW_BI_PMS_RECEITAS
   --------------------------------------------------------
   mensagem := mensagem || crlf || ':: PROCEDURE: ' || nome_proc || ' ::' || crlf || 'FLAG: '||flag || crlf || crlf;
   send_mail('lahumada@cit.com.br', '[PMS][CUSTFIXO] Start Proc', mensagem);
   send_mail('alexandrel@cit.com.br', '[PMS][CUSTFIXO] Start Proc', mensagem);
   send_mail('lnoboru@cit.com.br', '[PMS][CUSTFIXO] Start Proc', mensagem);
      --------------------------------------------
             select
                  sum(vl_C_L_cusfixratpra) vl_C_L_cusfixratpra,
                  sum(vl_C_L_cusfixratprablr) vl_C_L_cusfixratprablr,
                  sum(vl_C_L_ftecusfixratpra) vl_C_L_ftecusfixratpra,
                  sum(vl_C_L_S_A_cusfixratpra) vl_C_L_S_A_cusfixratpra,
                  sum(vl_C_L_S_A_tcecusfixratpra) vl_C_L_S_A_tcecusfixratpra,
                  sum(vl_C_L_S_A_cusinffix) vl_C_L_S_A_cusinffix,
                  sum(vl_C_L_S_A_cusfixratprabrl) vl_C_L_S_A_cusfixratprabrl,
                  sum(vl_C_L_S_A_ftecusfixratpra) vl_C_L_S_A_ftecusfixratpra,
                  sum(vl_C_U_cusfixratumkt) vl_C_U_cusfixratumkt,
                  sum(vl_C_U_tcecusfixratumkt) vl_C_U_tcecusfixratumkt,
                  sum(vl_C_U_cusinfrafixratumkt) vl_C_U_cusinfrafixratumkt,
                  sum(vl_C_U_cusfixratumktbrl) vl_C_U_cusfixratumktbrl,
                  sum(vl_C_U_tcecusfixratumktbrl) vl_C_U_tcecusfixratumktbrl,
                  sum(vl_C_U_cusinffixratumktbrl) vl_C_U_cusinffixratumktbrl,
                  sum(vl_C_U_cusfixumktferias) vl_C_U_cusfixumktferias,
                  sum(vl_C_U_ftecusfixratumkt) vl_C_U_ftecusfixratumkt,
                  sum(vl_C_U_S_A_cusfixratumkt) vl_C_U_S_A_cusfixratumkt,
                  sum(vl_C_U_S_A_tcecusfixratumkt) vl_C_U_S_A_tcecusfixratumkt,
                  sum(vl_C_U_S_A_cusinfrafixratumkt) vl_C_U_S_A_cusinfrafixratumkt,
                  sum(vl_C_U_S_A_cusfixratumktbrl) vl_C_U_S_A_cusfixratumktbrl,
                  sum(vl_C_U_S_A_tcecusratumktblr) vl_C_U_S_A_tcecusratumktblr,
                  sum(vl_C_U_S_A_cusinffixratumktbrl) vl_C_U_S_A_cusinffixratumktbrl,
                  sum(vl_C_U_S_A_ftecusfixratumkt) vl_C_U_S_A_ftecusfixratumkt
               into
                     lo_C_L_cusfixratpra ,
                     lo_C_L_cusfixratprablr ,
                     lo_C_L_ftecusfixratpra ,
                     lo_C_L_S_A_cusfixratpra ,
                     lo_C_L_S_A_tcecusfixratpra ,
                     lo_C_L_S_A_cusinffix ,
                     lo_C_L_S_A_cusfixratprabrl ,
                     lo_C_L_S_A_ftecusfixratpra ,
                     lo_C_U_cusfixratumkt ,
                     lo_C_U_tcecusfixratumkt ,
                     lo_C_U_cusinfrafixratumkt ,
                     lo_C_U_cusfixratumktbrl ,
                     lo_C_U_tcecusfixratumktbrl ,
                     lo_C_U_cusinffixratumktbrl ,
                     lo_C_U_cusfixumktferias ,
                     lo_C_U_ftecusfixratumkt ,
                     lo_C_U_S_A_cusfixratumkt ,
                     lo_C_U_S_A_tcecusfixratumkt ,
                     lo_C_U_S_A_cusinfrafixratumkt ,
                     lo_C_U_S_A_cusfixratumktbrl ,
                     lo_C_U_S_A_tcecusratumktblr ,
                     lo_C_U_S_A_cusinffixratumktbrl ,
                     lo_C_U_S_A_ftecusfixratumkt
               from (
               select /*s.origem,
                      s.categoria,*/
                      case when s.origem = 'COSTF_LOB' and s.categoria = 'Services' then
                             sum(s.valor_custfix_rateio_prat)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_L_cusfixratpra, --1
                      case when s.origem = 'COSTF_LOB' and s.categoria = 'Services' then
                             sum(s.valor_custfix_ratprat_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_L_cusfixratprablr, --2
                      case when s.origem = 'COSTF_LOB' and s.categoria = 'Services' then
                             sum(s.VALOR_FTE_CUSTFIX_RATPRAT)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_L_ftecusfixratpra, --3

                      case when s.origem = 'COSTF_LOB_SEM_ALOC' and s.categoria = 'Services' then
                             sum(s.valor_custfix_rateio_prat)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_L_S_A_cusfixratpra, --4
                      case when s.origem = 'COSTF_LOB_SEM_ALOC' and s.categoria = 'Services' then
                             sum(s.valor_tce_custfix_ratprat)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_L_S_A_tcecusfixratpra, --5
                      case when s.origem = 'COSTF_LOB_SEM_ALOC' and s.categoria = 'Services' then
                             sum(s.valor_custinfra_fix_ratprat)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_L_S_A_cusinffix, --6
                      case when s.origem = 'COSTF_LOB_SEM_ALOC' and s.categoria = 'Services' then
                             sum(s.valor_custfix_ratprat_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_L_S_A_cusfixratprabrl, --7
                      case when s.origem = 'COSTF_LOB_SEM_ALOC' and s.categoria = 'Services' then
                             sum(s.VALOR_FTE_CUSTFIX_RATPRAT)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_L_S_A_ftecusfixratpra, --8

                      case when s.origem = 'COSTF_UMKT' and s.categoria = 'Services' then
                             sum(s.VALOR_CUSTFIX_RATEIO_UMKT)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_cusfixratumkt, --9
                      case when s.origem = 'COSTF_UMKT' and s.categoria = 'Services' then
                             sum(s.VALOR_TCE_CUSTFIX_RATUMKT)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_tcecusfixratumkt, --10
                      case when s.origem = 'COSTF_UMKT' and s.categoria = 'Services' then
                             sum(s.VALOR_CUSTINFRA_FIX_RATUMKT)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_cusinfrafixratumkt, --11
                      case when s.origem = 'COSTF_UMKT' and s.categoria = 'Services' then
                             sum(s.VALOR_CUSTFIX_RATEIO_UMKT_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_cusfixratumktbrl, --12
                      case when s.origem = 'COSTF_UMKT' and s.categoria = 'Services' then
                             sum(s.VALOR_TCE_CUSTFIX_RATUMKT_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_tcecusfixratumktbrl, --13
                      case when s.origem = 'COSTF_UMKT' and s.categoria = 'Services' then
                             sum(s.VALOR_CUSTINF_FIX_RATUMKT_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_cusinffixratumktbrl, --14
                      case when s.origem = 'COSTF_UMKT' and s.categoria = 'Services' then
                             sum(s.VALOR_CUSTFIX_UMKT_FERIAS)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_cusfixumktferias, --15
                      case when s.origem = 'COSTF_UMKT' and s.categoria = 'Services' then
                             sum(s.VALOR_FTE_CUSTFIX_RATUMKT)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_ftecusfixratumkt,  --16

                      case when s.origem = 'COSTF_UMKT_SEM_ALOC' and s.categoria = 'Services' then
                             sum(s.VALOR_CUSTFIX_RATEIO_UMKT)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_S_A_cusfixratumkt,--17
                      case when s.origem = 'COSTF_UMKT_SEM_ALOC' and s.categoria = 'Services' then
                             sum(s.VALOR_TCE_CUSTFIX_RATUMKT)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_S_A_tcecusfixratumkt,--18
                      case when s.origem = 'COSTF_UMKT_SEM_ALOC' and s.categoria = 'Services' then
                             sum(s.VALOR_CUSTINFRA_FIX_RATUMKT)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_S_A_cusinfrafixratumkt,--19
                      case when s.origem = 'COSTF_UMKT_SEM_ALOC' and s.categoria = 'Services' then
                             sum(s.VALOR_CUSTFIX_RATEIO_UMKT_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_S_A_cusfixratumktbrl,--20
                      case when s.origem = 'COSTF_UMKT_SEM_ALOC' and s.categoria = 'Services' then
                             sum(s.VALOR_TCE_CUSTFIX_RATUMKT_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_S_A_tcecusratumktblr,--21
                      case when s.origem = 'COSTF_UMKT_SEM_ALOC' and s.categoria = 'Services' then
                             sum(s.VALOR_CUSTINF_FIX_RATUMKT_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_S_A_cusinffixratumktbrl,--22

                      case when s.origem = 'COSTF_UMKT_SEM_ALOC' and s.categoria = 'Services' then
                             sum(s.VALOR_FTE_CUSTFIX_RATUMKT)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_C_U_S_A_ftecusfixratumkt,--23
                     /* case when s.origem = 'COSTF_UMKT_SEM_ALOC' and s.categoria = 'Services' then
                             sum(s.VALOR_FTE_CUSTFIX_RATUMKT)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                            else 0
                       end vl_COSTF_UMKT_SEM_ALOC_ftecusfixratumkt,--24  */
                    row_number() over (partition by s.origem, s.categoria order by s.origem, s.categoria) nlinha
               from vw_bi_pms_custos_fixos_dre s
               where s.dt_mes between '01-jan-2011' and (select m.modu_dt_fechamento
                                                         from   modulo m
                                                         where  m.modu_cd_modulo = 2)
               ) where nlinha=1;

              --    lo_ITCHBK_IT_unitti := round(lo_ITCHBK_IT_unitti,2);
                  lo_C_L_cusfixratpra := round(lo_C_L_cusfixratpra,2);
                  lo_C_L_cusfixratprablr := round(lo_C_L_cusfixratprablr,2);
                  lo_C_L_ftecusfixratpra := round(lo_C_L_ftecusfixratpra,2);
                  lo_C_L_S_A_cusfixratpra := round(lo_C_L_S_A_cusfixratpra,2);
                  lo_C_L_S_A_tcecusfixratpra := round(lo_C_L_S_A_tcecusfixratpra,2);
                  lo_C_L_S_A_cusinffix := round(lo_C_L_S_A_cusinffix,2);
                  lo_C_L_S_A_cusfixratprabrl := round(lo_C_L_S_A_cusfixratprabrl,2);
                  lo_C_L_S_A_ftecusfixratpra := round(lo_C_L_S_A_ftecusfixratpra,2);
                  lo_C_U_cusfixratumkt := round(lo_C_U_cusfixratumkt,2);
                  lo_C_U_tcecusfixratumkt := round(lo_C_U_tcecusfixratumkt,2);
                  lo_C_U_cusinfrafixratumkt := round(lo_C_U_cusinfrafixratumkt,2);
                  lo_C_U_cusfixratumktbrl := round(lo_C_U_cusfixratumktbrl,2);
                  lo_C_U_tcecusfixratumktbrl := round(lo_C_U_tcecusfixratumktbrl,2);
                  lo_C_U_cusinffixratumktbrl := round(lo_C_U_cusinffixratumktbrl,2);
                  lo_C_U_cusfixumktferias := round(lo_C_U_cusfixumktferias,2);
                  lo_C_U_ftecusfixratumkt := round(lo_C_U_ftecusfixratumkt,2);
                  lo_C_U_S_A_cusfixratumkt := round(lo_C_U_S_A_cusfixratumkt,2);
                  lo_C_U_S_A_tcecusfixratumkt := round(lo_C_U_S_A_tcecusfixratumkt,2);
                  lo_C_U_S_A_cusinfrafixratumkt := round(lo_C_U_S_A_cusinfrafixratumkt,2);
                  lo_C_U_S_A_cusfixratumktbrl := round(lo_C_U_S_A_cusfixratumktbrl,2);
                  lo_C_U_S_A_tcecusratumktblr := round(lo_C_U_S_A_tcecusratumktblr,2);
                  lo_C_U_S_A_cusinffixratumktbrl := round(lo_C_U_S_A_cusinffixratumktbrl,2);
                  lo_C_U_S_A_ftecusfixratumkt := round(lo_C_U_S_A_ftecusfixratumkt,2);

       if( flag = 1 or flag = 99) then --COSTF_LOB
       begin
        ----------------------------------------------------------------------------------------------
        -- 1 - ORIGEM: COSTF_LOB CATEGORIA: Services CAMPO: vl_C_L_cusfixratpra
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_LOB';
         log_categoria := 'Services';
         log_campo := 'vl_C_L_cusfixratpra';
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
                valor_pre := lo_C_L_cusfixratpra;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_L_cusfixratpra/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_cusfixratpra) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_cusfixratpra) || crlf;
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
              lo_C_L_cusfixratpra,
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
        -- 2 - ORIGEM: COSTF_LOB CATEGORIA: Services CAMPO: valor_custfix_ratprat_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_LOB';
         log_categoria := 'Services';
         log_campo := 'valor_custfix_ratprat_brl';
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
                valor_pre := lo_C_L_cusfixratprablr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_L_cusfixratprablr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_cusfixratprablr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_cusfixratprablr) || crlf;
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
              lo_C_L_cusfixratprablr,
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
        -- 3 - ORIGEM: COSTF_LOB CATEGORIA: Services CAMPO: VALOR_FTE_CUSTFIX_RATPRAT
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_LOB';
         log_categoria := 'Services';
         log_campo := 'VALOR_FTE_CUSTFIX_RATPRAT';
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
                valor_pre := lo_C_L_ftecusfixratpra;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_L_ftecusfixratpra/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_ftecusfixratpra) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_ftecusfixratpra) || crlf;
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
              lo_C_L_ftecusfixratpra,
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
     if(flag = 2 or flag = 99) then --COSTF_LOB_SEM_ALOC
     begin
        ----------------------------------------------------------------------------------------------
        -- 4 - ORIGEM: COSTF_LOB_SEM_ALOC CATEGORIA: Services CAMPO: valor_custfix_rateio_prat
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_LOB_SEM_ALOC';
         log_categoria := 'Services';
         log_campo := 'valor_custfix_rateio_prat';
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
                valor_pre := lo_C_L_S_A_cusfixratpra;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_L_S_A_cusfixratpra/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_S_A_cusfixratpra) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_S_A_cusfixratpra) || crlf;
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
              lo_C_L_S_A_cusfixratpra,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 5 - ORIGEM: COSTF_LOB_SEM_ALOC CATEGORIA: Services CAMPO: valor_tce_custfix_ratprat
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_LOB_SEM_ALOC';
         log_categoria := 'Services';
         log_campo := 'valor_tce_custfix_ratprat';
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
                valor_pre := lo_C_L_S_A_tcecusfixratpra;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_L_S_A_tcecusfixratpra/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_S_A_tcecusfixratpra) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_S_A_tcecusfixratpra) || crlf;
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
              lo_C_L_S_A_tcecusfixratpra,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 6 - ORIGEM: COSTF_LOB_SEM_ALOC CATEGORIA: Services CAMPO: valor_custinfra_fix_ratprat
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_LOB_SEM_ALOC';
         log_categoria := 'Services';
         log_campo := 'valor_custinfra_fix_ratprat';
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
                valor_pre := lo_C_L_S_A_cusinffix;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_L_S_A_cusinffix/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_S_A_cusinffix) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_S_A_cusinffix) || crlf;
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
              lo_C_L_S_A_cusinffix,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 7 - ORIGEM: COSTF_LOB_SEM_ALOC CATEGORIA: Services CAMPO: valor_custfix_ratprat_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_LOB_SEM_ALOC';
         log_categoria := 'Services';
         log_campo := 'valor_custfix_ratprat_brl';
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
                valor_pre := lo_C_L_S_A_cusfixratprabrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_L_S_A_cusfixratprabrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_S_A_cusfixratprabrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_S_A_cusfixratprabrl) || crlf;
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
              lo_C_L_S_A_cusfixratprabrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 8 - ORIGEM: COSTF_LOB_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_FTE_CUSTFIX_RATPRAT
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_LOB_SEM_ALOC';
         log_categoria := 'Services';
         log_campo := 'VALOR_FTE_CUSTFIX_RATPRAT';
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
                valor_pre := lo_C_L_S_A_ftecusfixratpra;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_L_S_A_ftecusfixratpra/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_S_A_ftecusfixratpra) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_L_S_A_ftecusfixratpra) || crlf;
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
              lo_C_L_S_A_ftecusfixratpra,
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
     if(flag = 3 or flag = 99) then --COSTF_UMKT
     begin

        ----------------------------------------------------------------------------------------------
        -- 9 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_CUSTFIX_RATEIO_UMKT
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT';
         log_categoria := 'Services';
         log_campo := 'VALOR_CUSTFIX_RATEIO_UMKT';
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
                valor_pre := lo_C_U_cusfixratumkt;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_cusfixratumkt/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_cusfixratumkt) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_cusfixratumkt) || crlf;
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
              lo_C_U_cusfixratumkt,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;

        ----------------------------------------------------------------------------------------------
        -- 10 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_TCE_CUSTFIX_RATUMKT
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT';
         log_categoria := 'Services';
         log_campo := 'VALOR_TCE_CUSTFIX_RATUMKT';
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
                valor_pre := lo_C_U_tcecusfixratumkt;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_tcecusfixratumkt/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_tcecusfixratumkt) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_tcecusfixratumkt) || crlf;
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
              lo_C_U_tcecusfixratumkt,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 11 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_CUSTINFRA_FIX_RATUMKT
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT';
         log_categoria := 'Services';
         log_campo := 'VALOR_CUSTINFRA_FIX_RATUMKT';
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
                valor_pre := lo_C_U_cusinfrafixratumkt;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_cusinfrafixratumkt/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_cusinfrafixratumkt) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_cusinfrafixratumkt) || crlf;
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
              lo_C_U_cusinfrafixratumkt,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 12 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_CUSTFIX_RATEIO_UMKT_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT';
         log_categoria := 'Services';
         log_campo := 'VALOR_CUSTFIX_RATEIO_UMKT_BRL';
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
                valor_pre := lo_C_U_cusfixratumktbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_cusfixratumktbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_cusfixratumktbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_cusfixratumktbrl) || crlf;
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
              lo_C_U_cusfixratumktbrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 13 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_TCE_CUSTFIX_RATUMKT_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT';
         log_categoria := 'Services';
         log_campo := 'VALOR_TCE_CUSTFIX_RATUMKT_BRL';
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
                valor_pre := lo_C_U_tcecusfixratumktbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_tcecusfixratumktbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_tcecusfixratumktbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_tcecusfixratumktbrl) || crlf;
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
              lo_C_U_tcecusfixratumktbrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;

        ----------------------------------------------------------------------------------------------
        -- 14 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_CUSTINF_FIX_RATUMKT_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT';
         log_categoria := 'Services';
         log_campo := 'VALOR_CUSTINF_FIX_RATUMKT_BRL';
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
                valor_pre := lo_C_U_cusinffixratumktbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_cusinffixratumktbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_cusinffixratumktbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_cusinffixratumktbrl) || crlf;
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
              lo_C_U_cusinffixratumktbrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 15 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_CUSTFIX_UMKT_FERIAS
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT';
         log_categoria := 'Services';
         log_campo := 'VALOR_CUSTFIX_UMKT_FERIAS';
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
                valor_pre := lo_C_U_cusfixumktferias;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_cusfixumktferias/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_cusfixumktferias) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_cusfixumktferias) || crlf;
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
              lo_C_U_cusfixumktferias,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 16 - ORIGEM: COSTF_UMKT CATEGORIA: Services CAMPO: VALOR_FTE_CUSTFIX_RATUMKT
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT';
         log_categoria := 'Services';
         log_campo := 'VALOR_FTE_CUSTFIX_RATUMKT';
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
                valor_pre := lo_C_U_ftecusfixratumkt;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_ftecusfixratumkt/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_ftecusfixratumkt) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_ftecusfixratumkt) || crlf;
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
              lo_C_U_ftecusfixratumkt,
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
      if(flag = 4 or flag = 99) then --COSTF_UMKT_SEM_ALOC
      begin
        ----------------------------------------------------------------------------------------------
        -- 17 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_CUSTFIX_RATEIO_UMKT
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT_SEM_ALOC';
         log_categoria := 'Services';
         log_campo := 'VALOR_CUSTFIX_RATEIO_UMKT';
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
                valor_pre := lo_C_U_S_A_cusfixratumkt;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_S_A_cusfixratumkt/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_cusfixratumkt) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_cusfixratumkt) || crlf;
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
              lo_C_U_S_A_cusfixratumkt,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 18 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_TCE_CUSTFIX_RATUMKT
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT_SEM_ALOC';
         log_categoria := 'Services';
         log_campo := 'VALOR_TCE_CUSTFIX_RATUMKT';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '18- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := lo_C_U_S_A_tcecusfixratumkt;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_S_A_tcecusfixratumkt/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_tcecusfixratumkt) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_tcecusfixratumkt) || crlf;
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
              lo_C_U_S_A_tcecusfixratumkt,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       ----------------------------------------------------------------------------------------------
        -- 19 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_CUSTINFRA_FIX_RATUMKT
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT_SEM_ALOC';
         log_categoria := 'Services';
         log_campo := 'VALOR_CUSTINFRA_FIX_RATUMKT';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '19- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := lo_C_U_S_A_cusinfrafixratumkt;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_S_A_cusinfrafixratumkt/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_cusinfrafixratumkt) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_cusinfrafixratumkt) || crlf;
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
              lo_C_U_S_A_cusinfrafixratumkt,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       ----------------------------------------------------------------------------------------------
        -- 20 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_CUSTFIX_RATEIO_UMKT_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT_SEM_ALOC';
         log_categoria := 'Services';
         log_campo := 'VALOR_CUSTFIX_RATEIO_UMKT_BRL';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '20- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := lo_C_U_S_A_cusfixratumktbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_S_A_cusfixratumktbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_cusfixratumktbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_cusfixratumktbrl) || crlf;
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
              lo_C_U_S_A_cusfixratumktbrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       ----------------------------------------------------------------------------------------------
        -- 21 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_TCE_CUSTFIX_RATUMKT_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT_SEM_ALOC';
         log_categoria := 'Services';
         log_campo := 'VALOR_TCE_CUSTFIX_RATUMKT_BRL';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '21- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := lo_C_U_S_A_tcecusratumktblr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_S_A_tcecusratumktblr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_tcecusratumktblr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_tcecusratumktblr) || crlf;
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
              lo_C_U_S_A_tcecusratumktblr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       ----------------------------------------------------------------------------------------------
        -- 22 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_CUSTINF_FIX_RATUMKT_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT_SEM_ALOC';
         log_categoria := 'Services';
         log_campo := 'VALOR_CUSTINF_FIX_RATUMKT_BRL';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '22- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := lo_C_U_S_A_cusinffixratumktbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_S_A_cusinffixratumktbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_cusinffixratumktbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_cusinffixratumktbrl) || crlf;
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
              lo_C_U_S_A_cusinffixratumktbrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       ----------------------------------------------------------------------------------------------
        -- 23 - ORIGEM: COSTF_UMKT_SEM_ALOC CATEGORIA: Services CAMPO: VALOR_FTE_CUSTFIX_RATUMKT
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COSTF_UMKT_SEM_ALOC';
         log_categoria := 'Services';
         log_campo := 'VALOR_FTE_CUSTFIX_RATUMKT';
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
                valor_pre := lo_C_U_S_A_ftecusfixratumkt;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_C_U_S_A_ftecusfixratumkt/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_ftecusfixratumkt) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_C_U_S_A_ftecusfixratumkt) || crlf;
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
              lo_C_U_S_A_ftecusfixratumkt,
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
   assunto  := assunto || ' [PMS][CUSTFIXO] Resultado da Carga';
   send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
   send_mail('lnoboru@cit.com.br', assunto, mensagem);
   if log_status = 'ERRO' then
      mensagem := 'ATTENTION: Please, contact Ciandt IT Business Intelligence Team as soon as possible. ' || crlf || crlf ||
                  mensagem;
--      send_mail('infra_ti@cit.com.br', assunto, mensagem);
   end if;
end USP_PMS_CUSTFIXO;