CREATE OR REPLACE PROCEDURE USP_PMS_DESPRATTECSAL (flag number) as
   log_data              date := trunc(sysdate);
--INTERVAL:
   -- FLAG
   -- 1 = COMISS
   -- 2 = TECSAL
   -- 3 = TCE_DIRETO
   -- 4 = TCE_RATEIO
   -- 5 = DESPESA_DIRETO
   -- 6 = DESPESA_RATEIO
   -- 99 = TODOS
   -- Procedure de carga
        -- 1 - ORIGEM: COMISS CATEGORIA: Services CAMPO: VL_COMISS_ACEL_BRL
        -- 2 - ORIGEM: COMISS CATEGORIA: Services CAMPO: VL_COMISS_INV_BRL
        -- 3 - ORIGEM: TECSAL CATEGORIA: Others CAMPO: VLR_TECSAL_DESP_BRL
        -- 4 - ORIGEM: TECSAL CATEGORIA: Travel CAMPO: VLR_TECSAL_DESP_BRL
        -- 5 - ORIGEM: TECSAL CATEGORIA: Services CAMPO: VLR_TECSAL_CDIR_TCE_BRL
        -- 6 - ORIGEM: TECSAL CATEGORIA: Services CAMPO: VLR_TECSAL_CDIR_INFRA_BRL
        -- 7 - ORIGEM: TECSAL CATEGORIA: Services CAMPO: VLR_TECSAL_CIND_TCE_BRL
        -- 8 - ORIGEM: TECSAL CATEGORIA: Services CAMPO: VLR_TECSAL_CIND_INFRA_BRL
        -- 9 - ORIGEM: TECSAL CATEGORIA: Services CAMPO: VLR_TECSAL_HORA_EXTRA
        -- 10 - ORIGEM: TECSAL CATEGORIA: Services CAMPO: VLR_TECSAL_DESP_BRL
        -- 11 - ORIGEM: TCE_DIRETO CATEGORIA: Services CAMPO: VLR_CUST_COLAB_DN_BRL
        -- 12 - ORIGEM: TCE_DIRETO CATEGORIA: Services CAMPO: VLR_CUST_COLAB_AE_BRL
        -- 13 - ORIGEM: TCE_RATEIO CATEGORIA: Services CAMPO: VLR_CUSTRAT_MKT_BRL
        -- 14 - ORIGEM: TCE_RATEIO CATEGORIA: Services CAMPO: VLR_CUSTRAT_DN_BRL
        -- 15 - ORIGEM: TCE_RATEIO CATEGORIA: Services CAMPO: VLR_CUSTRAT_AE_BRL
        -- 16 - ORIGEM: DESPESA_DIRETO CATEGORIA: Others CAMPO: VLR_DESP_MKT_OTHERS_BRL
        -- 17 - ORIGEM: DESPESA_DIRETO CATEGORIA: Others CAMPO: VLR_DESP_DN_OTHERS_BRL
        -- 18 - ORIGEM: DESPESA_DIRETO CATEGORIA: Others CAMPO: VLR_DESP_AE_OTHERS_BRL
        -- 19 - ORIGEM: DESPESA_DIRETO CATEGORIA: Travel CAMPO: VLR_DESP_MKT_TRAVEL_BRL
        -- 20 - ORIGEM: DESPESA_DIRETO CATEGORIA: Travel CAMPO: VLR_DESP_DN_TRAVEL_BRL
        -- 21 - ORIGEM: DESPESA_DIRETO CATEGORIA: Travel CAMPO: VLR_DESP_AE_TRAVEL_BRL
        -- 22 - ORIGEM: DESPESA_DIRETO CATEGORIA: Contractors CAMPO: VLR_DESP_MKT_OTHERS_BRL
        -- 23 - ORIGEM: DESPESA_DIRETO CATEGORIA: Contractors CAMPO: VLR_DESP_DN_OTHERS_BRL
        -- 24 - ORIGEM: DESPESA_RATEIO CATEGORIA: Others CAMPO: VLR_DESPRAT_MKT_OTH_BRL
        -- 25 - ORIGEM: DESPESA_DIRETO CATEGORIA: Contractors CAMPO: VLR_DESP_DN_OTHERS_BRL
        -- 26 - ORIGEM: DESPESA_RATEIO CATEGORIA: Travel CAMPO: VLR_DESPRAT_MKT_TRAV_BRL
        -- 27 - ORIGEM: DESPESA_RATEIO CATEGORIA: Contractors CAMPO: VLR_DESP_DN_OTHERS_BRL
        -- 28 - ORIGEM: DESPESA_RATEIO CATEGORIA: Contractors CAMPO: VLR_DESPRAT_MKT_OTH_BRL
        -- 29 - ORIGEM: DESPESA_RATEIO CATEGORIA: Contractors CAMPO: VALOR_DESPRAT_DN_OTH_BRL
   -- Informac?es para LOG
   log_dt_carga_processo date;
   log_status                varchar2(30) := '';
   log_origem                varchar2(50) := '';
   log_categoria             varchar2(50) := '';
   log_campo                 varchar2(150):= '';
   log_diferenca             number;
   log_descricao             varchar2(200) := '';
   log_sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'USP_PMS_DESPRATTECSAL';
   --
   crlf      varchar2(2) := chr(13) || chr(10); -- <ENTER>
   assunto   varchar2(200) := '';
   mensagem  varchar2(7000) := '=== Inicio da Carga ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===' ;
   valor_pre integer;
   --VARIAVEIS PARA LOG
      lo_COMISS_Serv_comacebr integer;
      lo_COMISS_Serv_cominvbr integer;
      lo_TECSAL_Othe_despbr integer;
      lo_TECSAL_Trav_despbr integer;
      lo_TECSAL_Serv_cdirtcebr integer;
      lo_TECSAL_Serv_cdirinfbr integer;
      lo_TECSAL_Serv_cindtcebr integer;
      lo_TECSAL_Serv_cindinfrabr integer;
      lo_TECSAL_Serv_horaextra integer;
      lo_TECSAL_Contr_despbr integer;
      lo_TCE_DIRETO_Serv_cuscoldnbr integer;
      lo_TCE_DIRETO_Serv_cuscolarbr integer;
      lo_TCE_RATEIO_Serv_custratmkt integer;
      lo_TCE_RATEIO_Serv_custradnbr integer;
      lo_TCE_RATEIO_Serv_custraaebr integer;
      lo_DESPESA_DIRETO_Othe_mktbrl integer;
      lo_DESPESA_DIRETO_Othe_dnbr integer;
      lo_DESPESA_DIRETO_Othe_aebrl integer;
      lo_DESPESA_DIRETO_Trav_mktbrl integer;
      lo_DESPESA_DIRETO_Trav_dnbrl integer;
      lo_DESPESA_DIRETO_Trav_aebrl integer;
      lo_DESPESA_DIRETO_Cont_mktbrl integer;
      lo_DESPESA_DIRETO_Cont_dnbrl integer;
      lo_DESPESA_RATEIO_Othe_mktbrl integer;
      lo_DESPESA_RATEIO_Othe_dnbrl integer;
      lo_DESPESA_RATEIO_Trav_mktbrl integer;
      lo_DESPESA_RATEIO_Trav_dnbrl integer;
      lo_DESPESA_RATEIO_Cont_mktbrl integer;
      lo_DESPESA_RATEIO_Cont_dnbrl integer;

begin
   --------------------------------------------------------
   -- VALIDACAO VW_BI_PMS_RECEITAS
   --------------------------------------------------------
   mensagem := mensagem || crlf || ':: PROCEDURE: ' || nome_proc || ' ::' || crlf || 'FLAG: '||flag || crlf || crlf;
   send_mail('lahumada@cit.com.br', '[PMS][DESPRATTECSAL] Start Proc', mensagem);
   send_mail('alexandrel@cit.com.br', '[PMS][DESPRATTECSAL] Start Proc', mensagem);
   send_mail('lnoboru@cit.com.br', '[PMS][DESPRATTECSAL] Start Proc', mensagem);
      --------------------------------------------
              select
                  round(sum(vl_COMISS_Serv_comacebr),2) vl_COMISS_Serv_comacebr,
                  round(sum(vl_COMISS_Serv_cominvbr),2) vl_COMISS_Serv_cominvbr,
                  round(sum(vl_TECSAL_Othe_despbr),2) vl_TECSAL_Othe_despbr,
                  round(sum(vl_TECSAL_Trav_despbr),2) vl_TECSAL_Trav_despbr,
                  round(sum(vl_TECSAL_Serv_cdirtcebr),2) vl_TECSAL_Serv_cdirtcebr,
                  round(sum(vl_TECSAL_Serv_cdirinfbr),2) vl_TECSAL_Serv_cdirinfbr,
                  round(sum(vl_TECSAL_Serv_cindtcebr),2) vl_TECSAL_Serv_cindtcebr,
                  round(sum(vl_TECSAL_Serv_cindinfrabr),2) vl_TECSAL_Serv_cindinfrabr,
                  round(sum(vl_TECSAL_Serv_horaextra),2) vl_TECSAL_Serv_horaextra,
                  round(sum(vl_TECSAL_Contr_despbr),2) vl_TECSAL_Contr_despbr,
                  round(sum(vl_TCE_DIRETO_Serv_cuscoldnbr),2) vl_TCE_DIRETO_Serv_cuscoldnbr,
                  round(sum(vl_TCE_DIRETO_Serv_cuscolarbr),2) vl_TCE_DIRETO_Serv_cuscolarbr,
                  round(sum(vl_TCE_RATEIO_Serv_custratmkt),2) vl_TCE_RATEIO_Serv_custratmkt,
                  round(sum(vl_TCE_RATEIO_Serv_custradnbr),2) vl_TCE_RATEIO_Serv_custradnbr,
                  round(sum(vl_TCE_RATEIO_Serv_custraaebr),2) vl_TCE_RATEIO_Serv_custraaebr,
                  round(sum(vl_DESPESA_DIRETO_Othe_mktbrl),2) vl_DESPESA_DIRETO_Othe_mktbrl,
                  round(sum(vl_DESPESA_DIRETO_Othe_dnbr),2) vl_DESPESA_DIRETO_Othe_dnbr,
                  round(sum(vl_DESPESA_DIRETO_Othe_aebrl),2) vl_DESPESA_DIRETO_Othe_aebrl,
                  round(sum(vl_DESPESA_DIRETO_Trav_mktbrl),2) vl_DESPESA_DIRETO_Trav_mktbrl,
                  round(sum(vl_DESPESA_DIRETO_Trav_dnbrl),2) vl_DESPESA_DIRETO_Trav_dnbrl,
                  round(sum(vl_DESPESA_DIRETO_Trav_aebrl),2) vl_DESPESA_DIRETO_Trav_aebrl,
                  round(sum(vl_DESPESA_DIRETO_Cont_mktbrl),2) vl_DESPESA_DIRETO_Cont_mktbrl,
                  round(sum(vl_DESPESA_DIRETO_Cont_dnbrl),2) vl_DESPESA_DIRETO_Cont_dnbrl,
                  round(sum(vl_DESPESA_RATEIO_Othe_mktbrl),2) vl_DESPESA_RATEIO_Othe_mktbrl,
                  round(sum(vl_DESPESA_RATEIO_Othe_dnbrl),2) vl_DESPESA_RATEIO_Othe_dnbrl,
                  round(sum(vl_DESPESA_RATEIO_Trav_mktbrl),2) vl_DESPESA_RATEIO_Trav_mktbrl,
                  round(sum(vl_DESPESA_RATEIO_Trav_dnbrl),2) vl_DESPESA_RATEIO_Trav_dnbrl,
                  round(sum(vl_DESPESA_RATEIO_Cont_mktbrl),2) vl_DESPESA_RATEIO_Cont_mktbrl,
                  round(sum(vl_DESPESA_RATEIO_Cont_dnbrl),2) vl_DESPESA_RATEIO_Cont_dnbrl
               into
                        lo_COMISS_Serv_comacebr,
                        lo_COMISS_Serv_cominvbr,
                        lo_TECSAL_Othe_despbr,
                        lo_TECSAL_Trav_despbr,
                        lo_TECSAL_Serv_cdirtcebr,
                        lo_TECSAL_Serv_cdirinfbr,
                        lo_TECSAL_Serv_cindtcebr,
                        lo_TECSAL_Serv_cindinfrabr,
                        lo_TECSAL_Serv_horaextra,
                        lo_TECSAL_Contr_despbr,
                        lo_TCE_DIRETO_Serv_cuscoldnbr,
                        lo_TCE_DIRETO_Serv_cuscolarbr,
                        lo_TCE_RATEIO_Serv_custratmkt,
                        lo_TCE_RATEIO_Serv_custradnbr,
                        lo_TCE_RATEIO_Serv_custraaebr,
                        lo_DESPESA_DIRETO_Othe_mktbrl,
                        lo_DESPESA_DIRETO_Othe_dnbr,
                        lo_DESPESA_DIRETO_Othe_aebrl,
                        lo_DESPESA_DIRETO_Trav_mktbrl,
                        lo_DESPESA_DIRETO_Trav_dnbrl,
                        lo_DESPESA_DIRETO_Trav_aebrl,
                        lo_DESPESA_DIRETO_Cont_mktbrl,
                        lo_DESPESA_DIRETO_Cont_dnbrl,
                        lo_DESPESA_RATEIO_Othe_mktbrl,
                        lo_DESPESA_RATEIO_Othe_dnbrl,
                        lo_DESPESA_RATEIO_Trav_mktbrl,
                        lo_DESPESA_RATEIO_Trav_dnbrl,
                        lo_DESPESA_RATEIO_Cont_mktbrl,
                        lo_DESPESA_RATEIO_Cont_dnbrl
               from (
               select /*s.origem,
                      s.categoria,*/
                      case when s.origem = 'COMISS'          and s.categoria = 'Services' then sum(s.VL_COMISS_ACEL_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_COMISS_Serv_comacebr,    --1
                      case when s.origem = 'COMISS'          and s.categoria = 'Services' then sum(s.VL_COMISS_INV_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_COMISS_Serv_cominvbr,    --2                            
                      case when s.origem = 'TECSAL'          and s.categoria = 'Others' then sum(s.VLR_TECSAL_DESP_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_TECSAL_Othe_despbr,    --3
                      case when s.origem = 'TECSAL'          and s.categoria = 'Travel' then sum(s.VLR_TECSAL_DESP_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_TECSAL_Trav_despbr,  --4
                      case when s.origem = 'TECSAL'          and s.categoria = 'Services' then sum(s.VLR_TECSAL_CDIR_TCE_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_TECSAL_Serv_cdirtcebr, --5
                      case when s.origem = 'TECSAL'          and s.categoria = 'Services' then sum(s.VLR_TECSAL_CDIR_INFRA_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_TECSAL_Serv_cdirinfbr, --6
                      case when s.origem = 'TECSAL'          and s.categoria = 'Services' then sum(s.VLR_TECSAL_CIND_TCE_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_TECSAL_Serv_cindtcebr, --7
                      case when s.origem = 'TECSAL'          and s.categoria = 'Services' then sum(s.VLR_TECSAL_CIND_INFRA_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_TECSAL_Serv_cindinfrabr, --8
                      case when s.origem = 'TECSAL'          and s.categoria = 'Services' then sum(s.VLR_TECSAL_HORA_EXTRA)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_TECSAL_Serv_horaextra, --9
                      case when s.origem = 'TECSAL'          and s.categoria = 'Contractors' then sum(s.VLR_TECSAL_DESP_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_TECSAL_Contr_despbr,   --10
                      case when s.origem = 'TCE_DIRETO'      and s.categoria = 'Services' then sum(s.VLR_CUST_COLAB_DN_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_TCE_DIRETO_Serv_cuscoldnbr,  --11
                      case when s.origem = 'TCE_DIRETO'      and s.categoria = 'Services' then sum(s.VLR_CUST_COLAB_AE_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_TCE_DIRETO_Serv_cuscolarbr, --12
                      case when s.origem = 'TCE_RATEIO'      and s.categoria = 'Services' then sum(s.VLR_CUSTRAT_MKT_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_TCE_RATEIO_Serv_custratmkt,  --13
                      case when s.origem = 'TCE_RATEIO'      and s.categoria = 'Services' then sum(s.VLR_CUSTRAT_DN_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_TCE_RATEIO_Serv_custradnbr, --14
                      case when s.origem = 'TCE_RATEIO'      and s.categoria = 'Services' then sum(s.VLR_CUSTRAT_AE_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_TCE_RATEIO_Serv_custraaebr,  --15
                      case when s.origem = 'DESPESA_DIRETO'  and s.categoria = 'Others' then sum(s.VLR_DESP_MKT_OTHERS_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_DIRETO_Othe_mktbrl, --16
                      case when s.origem = 'DESPESA_DIRETO'  and s.categoria = 'Others' then sum(s.VLR_DESP_DN_OTHERS_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_DIRETO_Othe_dnbr,  --17
                      case when s.origem = 'DESPESA_DIRETO'  and s.categoria = 'Others' then sum(s.VLR_DESP_AE_OTHERS_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_DIRETO_Othe_aebrl,   --18
                      case when s.origem = 'DESPESA_DIRETO'  and s.categoria = 'Travel' then sum(s.VLR_DESP_MKT_TRAVEL_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_DIRETO_Trav_mktbrl, --19
                      case when s.origem = 'DESPESA_DIRETO'  and s.categoria = 'Travel' then sum(s.VLR_DESP_DN_TRAVEL_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_DIRETO_Trav_dnbrl,  --20
                      case when s.origem = 'DESPESA_DIRETO'  and s.categoria = 'Travel' then sum(s.VLR_DESP_AE_TRAVEL_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_DIRETO_Trav_aebrl, --21
                      case when s.origem = 'DESPESA_DIRETO'  and s.categoria = 'Contractors' then sum(s.VLR_DESP_MKT_OTHERS_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_DIRETO_Cont_mktbrl,  --22
                      case when s.origem = 'DESPESA_DIRETO'  and s.categoria = 'Contractors' then sum(s.VLR_DESP_DN_OTHERS_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_DIRETO_Cont_dnbrl,   --23
                      case when s.origem = 'DESPESA_RATEIO'  and s.categoria = 'Others' then sum(s.VLR_DESPRAT_MKT_OTH_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_RATEIO_Othe_mktbrl,  --24
                      case when s.origem = 'DESPESA_RATEIO'  and s.categoria = 'Others' then sum(s.VLR_DESPRAT_MKT_OTH_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_RATEIO_Othe_dnbrl,  --25
                      case when s.origem = 'DESPESA_RATEIO'  and s.categoria = 'Travel' then sum(s.VLR_DESPRAT_MKT_TRAV_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_RATEIO_Trav_mktbrl, --26
                      case when s.origem = 'DESPESA_RATEIO'  and s.categoria = 'Travel' then sum(s.VALOR_DESPRAT_DN_TRAV_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_RATEIO_Trav_dnbrl,  --27
                      case when s.origem = 'DESPESA_RATEIO'  and s.categoria = 'Contractors' then sum(s.VLR_DESPRAT_MKT_OTH_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_RATEIO_Cont_mktbrl,  --28
                      case when s.origem = 'DESPESA_RATEIO'  and s.categoria = 'Contractors' then sum(s.VALOR_DESPRAT_DN_OTH_BRL)over (partition by s.origem, s.categoria order by s.origem, s.categoria) else 0
                       end vl_DESPESA_RATEIO_Cont_dnbrl,  --29
                    row_number() over (partition by s.origem, s.categoria order by s.origem, s.categoria) nlinha
               from vw_bi_pms_cdesp_rat_tecsal_dre s
               where s.dt_mes between '01-jan-2011' and (select m.modu_dt_fechamento
                                                         from   modulo m
                                                         where  m.modu_cd_modulo = 2)
               ) where nlinha=1;

                  /*lo_COMISS_Serv_comacebr := round(lo_COMISS_Serv_comacebr,2);
                  lo_COMISS_Serv_cominvbr := round(lo_COMISS_Serv_cominvbr,2);
                  lo_TECSAL_Othe_despbr := round(lo_TECSAL_Othe_despbr,2);
                  lo_TECSAL_Trav_despbr := round(lo_TECSAL_Trav_despbr,2);
                  lo_TECSAL_Serv_cdirtcebr := round(lo_TECSAL_Serv_cdirtcebr,2);
                  lo_TECSAL_Serv_cdirinfbr := round(lo_TECSAL_Serv_cdirinfbr,2);
                  lo_TECSAL_Serv_cindtcebr := round(lo_TECSAL_Serv_cindtcebr,2);
                  lo_TECSAL_Serv_cindinfrabr := round(lo_TECSAL_Serv_cindinfrabr,2);
                  lo_TECSAL_Serv_horaextra := round(lo_TECSAL_Serv_horaextra,2);
                  lo_TECSAL_Contr_despbr := round(lo_TECSAL_Contr_despbr,2);
                  lo_TCE_DIRETO_Serv_cuscoldnbr := round(lo_TCE_DIRETO_Serv_cuscoldnbr,2);
                  lo_TCE_DIRETO_Serv_cuscolarbr := round(lo_TCE_DIRETO_Serv_cuscolarbr,2);
                  lo_TCE_RATEIO_Serv_custratmkt := round(lo_TCE_RATEIO_Serv_custratmkt,2);
                  lo_TCE_RATEIO_Serv_custradnbr := round(lo_TCE_RATEIO_Serv_custradnbr,2);
                  lo_TCE_RATEIO_Serv_custraaebr := round(lo_TCE_RATEIO_Serv_custraaebr,2);
                  lo_DESPESA_DIRETO_Othe_mktbrl := round(lo_DESPESA_DIRETO_Othe_mktbrl,2);
                  lo_DESPESA_DIRETO_Othe_dnbr := round(lo_DESPESA_DIRETO_Othe_dnbr,2);
                  lo_DESPESA_DIRETO_Othe_aebrl := round(lo_DESPESA_DIRETO_Othe_aebrl,2);
                  lo_DESPESA_DIRETO_Trav_mktbrl := round(lo_DESPESA_DIRETO_Trav_mktbrl,2);
                  lo_DESPESA_DIRETO_Trav_dnbrl := round(lo_DESPESA_DIRETO_Trav_dnbrl,2);
                  lo_DESPESA_DIRETO_Trav_aebrl := round(lo_DESPESA_DIRETO_Trav_aebrl,2);
                  lo_DESPESA_DIRETO_Cont_mktbrl := round(lo_DESPESA_DIRETO_Cont_mktbrl,2);
                  lo_DESPESA_DIRETO_Cont_dnbrl := round(lo_DESPESA_DIRETO_Cont_dnbrl,2);
                  lo_DESPESA_RATEIO_Othe_mktbrl := round(lo_DESPESA_RATEIO_Othe_mktbrl,2);
                  lo_DESPESA_RATEIO_Othe_dnbrl := round(lo_DESPESA_RATEIO_Othe_dnbrl,2);
                  lo_DESPESA_RATEIO_Trav_mktbrl := round(lo_DESPESA_RATEIO_Trav_mktbrl,2);
                  lo_DESPESA_RATEIO_Trav_dnbrl := round(lo_DESPESA_RATEIO_Trav_dnbrl,2);
                  lo_DESPESA_RATEIO_Cont_mktbrl := round(lo_DESPESA_RATEIO_Cont_mktbrl,2);
                  lo_DESPESA_RATEIO_Cont_dnbrl := round(lo_DESPESA_RATEIO_Cont_dnbrl,2);   */

       if( flag = 1 or flag = 99) then --COMISS
       begin
        ----------------------------------------------------------------------------------------------
        -- 1 - ORIGEM: COMISS CATEGORIA: Services CAMPO: VL_COMISS_ACEL_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COMISS';
         log_categoria := 'Services';
         log_campo := 'VL_COMISS_ACEL_BRL';
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
                valor_pre := lo_COMISS_Serv_comacebr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_COMISS_Serv_comacebr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COMISS_Serv_comacebr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COMISS_Serv_comacebr) || crlf;
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
              lo_COMISS_Serv_comacebr,
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
        -- 2 - ORIGEM: COMISS CATEGORIA: Services CAMPO: VL_COMISS_INV_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COMISS';
         log_categoria := 'Services';
         log_campo := 'VL_COMISS_INV_BRL';
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
                valor_pre := lo_COMISS_Serv_cominvbr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_COMISS_Serv_cominvbr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COMISS_Serv_cominvbr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COMISS_Serv_cominvbr) || crlf;
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
              lo_COMISS_Serv_cominvbr,
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
     if(flag = 2 or flag = 99) then --TECSAL
     begin
        ----------------------------------------------------------------------------------------------
        -- 3 - ORIGEM: TECSAL CATEGORIA: Others CAMPO: VLR_TECSAL_DESP_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TECSAL';
         log_categoria := 'Others';
         log_campo := 'VLR_TECSAL_DESP_BRL';
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
                valor_pre := lo_TECSAL_Othe_despbr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_TECSAL_Othe_despbr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Othe_despbr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Othe_despbr) || crlf;
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
              lo_TECSAL_Othe_despbr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 4 - ORIGEM: TECSAL CATEGORIA: Travel CAMPO: VLR_TECSAL_DESP_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TECSAL';
         log_categoria := 'Travel';
         log_campo := 'VLR_TECSAL_DESP_BRL';
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
                valor_pre := lo_TECSAL_Trav_despbr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_TECSAL_Trav_despbr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Trav_despbr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Trav_despbr) || crlf;
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
              lo_TECSAL_Trav_despbr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 5 - ORIGEM: TECSAL CATEGORIA: Services CAMPO: VLR_TECSAL_CDIR_TCE_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TECSAL';
         log_categoria := 'Services';
         log_campo := 'VLR_TECSAL_CDIR_TCE_BRL';
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
                valor_pre := lo_TECSAL_Serv_cdirtcebr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_TECSAL_Serv_cdirtcebr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Serv_cdirtcebr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Serv_cdirtcebr) || crlf;
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
              lo_TECSAL_Serv_cdirtcebr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 6 - ORIGEM: TECSAL CATEGORIA: Services CAMPO: VLR_TECSAL_CDIR_INFRA_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TECSAL';
         log_categoria := 'Services';
         log_campo := 'VLR_TECSAL_CDIR_INFRA_BRL';
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
                valor_pre := lo_TECSAL_Serv_cdirinfbr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_TECSAL_Serv_cdirinfbr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Serv_cdirinfbr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Serv_cdirinfbr) || crlf;
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
              lo_TECSAL_Serv_cdirinfbr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 7 - ORIGEM: TECSAL CATEGORIA: Services CAMPO: VLR_TECSAL_CIND_TCE_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TECSAL';
         log_categoria := 'Services';
         log_campo := 'VLR_TECSAL_CIND_TCE_BRL';
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
                valor_pre := lo_TECSAL_Serv_cindtcebr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_TECSAL_Serv_cindtcebr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Serv_cindtcebr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Serv_cindtcebr) || crlf;
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
              lo_TECSAL_Serv_cindtcebr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 8 - ORIGEM: TECSAL CATEGORIA: Services CAMPO: VLR_TECSAL_CIND_INFRA_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TECSAL';
         log_categoria := 'Services';
         log_campo := 'VLR_TECSAL_CIND_INFRA_BRL';
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
                valor_pre := lo_TECSAL_Serv_cindinfrabr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_TECSAL_Serv_cindinfrabr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Serv_cindinfrabr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Serv_cindinfrabr) || crlf;
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
              lo_TECSAL_Serv_cindinfrabr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 9 - ORIGEM: TECSAL CATEGORIA: Services CAMPO: VLR_TECSAL_HORA_EXTRA
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TECSAL';
         log_categoria := 'Services';
         log_campo := 'VLR_TECSAL_HORA_EXTRA';
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
                valor_pre := lo_TECSAL_Serv_horaextra;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_TECSAL_Serv_horaextra/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Serv_horaextra) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Serv_horaextra) || crlf;
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
              lo_TECSAL_Serv_horaextra,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;

        ----------------------------------------------------------------------------------------------
        -- 10 - ORIGEM: TECSAL CATEGORIA: Contractors CAMPO: VLR_TECSAL_DESP_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TECSAL';
         log_categoria := 'Contractors';
         log_campo := 'VLR_TECSAL_DESP_BRL';
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
                valor_pre := lo_TECSAL_Contr_despbr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_TECSAL_Contr_despbr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Contr_despbr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TECSAL_Contr_despbr) || crlf;
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
              lo_TECSAL_Contr_despbr,
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
     if(flag = 3 or flag = 99) then --TCE_DIRETO
     begin
        ----------------------------------------------------------------------------------------------
        -- 11 - ORIGEM: TCE_DIRETO CATEGORIA: Services CAMPO: VLR_CUST_COLAB_DN_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TCE_DIRETO';
         log_categoria := 'Services';
         log_campo := 'VLR_CUST_COLAB_DN_BRL';
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
                valor_pre := lo_TCE_DIRETO_Serv_cuscoldnbr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_TCE_DIRETO_Serv_cuscoldnbr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TCE_DIRETO_Serv_cuscoldnbr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TCE_DIRETO_Serv_cuscoldnbr) || crlf;
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
              lo_TCE_DIRETO_Serv_cuscoldnbr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 12 - ORIGEM: TCE_DIRETO CATEGORIA: Services CAMPO: VLR_CUST_COLAB_AE_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TCE_DIRETO';
         log_categoria := 'Services';
         log_campo := 'VLR_CUST_COLAB_AE_BRL';
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
                valor_pre := lo_TCE_DIRETO_Serv_cuscolarbr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_TCE_DIRETO_Serv_cuscolarbr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TCE_DIRETO_Serv_cuscolarbr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TCE_DIRETO_Serv_cuscolarbr) || crlf;
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
              lo_TCE_DIRETO_Serv_cuscolarbr,
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
     if(flag = 4 or flag = 99) then --TCE_RATEIO
     begin
        ----------------------------------------------------------------------------------------------
        -- 13 - ORIGEM: TCE_RATEIO CATEGORIA: Services CAMPO: VLR_CUSTRAT_MKT_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TCE_RATEIO';
         log_categoria := 'Services';
         log_campo := 'VLR_CUSTRAT_MKT_BRL';
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
                valor_pre := lo_TCE_RATEIO_Serv_custratmkt;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_TCE_RATEIO_Serv_custratmkt/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TCE_RATEIO_Serv_custratmkt) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TCE_RATEIO_Serv_custratmkt) || crlf;
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
              lo_TCE_RATEIO_Serv_custratmkt,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;

        ----------------------------------------------------------------------------------------------
        -- 14 - ORIGEM: TCE_RATEIO CATEGORIA: Services CAMPO: VLR_CUSTRAT_DN_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TCE_RATEIO';
         log_categoria := 'Services';
         log_campo := 'VLR_CUSTRAT_DN_BRL';
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
                valor_pre := lo_TCE_RATEIO_Serv_custradnbr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_TCE_RATEIO_Serv_custradnbr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TCE_RATEIO_Serv_custradnbr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TCE_RATEIO_Serv_custradnbr) || crlf;
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
              lo_TCE_RATEIO_Serv_custradnbr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 15 - ORIGEM: TCE_RATEIO CATEGORIA: Services CAMPO: VLR_CUSTRAT_AE_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'TCE_RATEIO';
         log_categoria := 'Services';
         log_campo := 'VLR_CUSTRAT_AE_BRL';
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
                valor_pre := lo_TCE_RATEIO_Serv_custraaebr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_TCE_RATEIO_Serv_custraaebr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TCE_RATEIO_Serv_custraaebr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_TCE_RATEIO_Serv_custraaebr) || crlf;
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
              lo_TCE_RATEIO_Serv_custraaebr,
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
     if(flag = 5 or flag = 99) then --DESPESA_DIRETO
     begin
        ----------------------------------------------------------------------------------------------
        -- 16 - ORIGEM: DESPESA_DIRETO CATEGORIA: Others CAMPO: VLR_DESP_MKT_OTHERS_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_DIRETO';
         log_categoria := 'Others';
         log_campo := 'VLR_DESP_MKT_OTHERS_BRL';
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
                valor_pre := lo_DESPESA_DIRETO_Othe_mktbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESPESA_DIRETO_Othe_mktbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Othe_mktbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Othe_mktbrl) || crlf;
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
              lo_DESPESA_DIRETO_Othe_mktbrl,
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
        -- 17 - ORIGEM: DESPESA_DIRETO CATEGORIA: Others CAMPO: VLR_DESP_DN_OTHERS_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_DIRETO';
         log_categoria := 'Others';
         log_campo := 'VLR_DESP_DN_OTHERS_BRL';
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
                valor_pre := lo_DESPESA_DIRETO_Othe_dnbr;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESPESA_DIRETO_Othe_dnbr/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Othe_dnbr) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Othe_dnbr) || crlf;
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
              lo_DESPESA_DIRETO_Othe_dnbr,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 18 - ORIGEM: DESPESA_DIRETO CATEGORIA: Others CAMPO: VLR_DESP_AE_OTHERS_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_DIRETO';
         log_categoria := 'Others';
         log_campo := 'VLR_DESP_AE_OTHERS_BRL';
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
                valor_pre := lo_DESPESA_DIRETO_Othe_aebrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESPESA_DIRETO_Othe_aebrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Othe_aebrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Othe_aebrl) || crlf;
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
              lo_DESPESA_DIRETO_Othe_aebrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       ----------------------------------------------------------------------------------------------
        -- 19 - ORIGEM: DESPESA_DIRETO CATEGORIA: Travel CAMPO: VLR_DESP_MKT_TRAVEL_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_DIRETO';
         log_categoria := 'Travel';
         log_campo := 'VLR_DESP_MKT_TRAVEL_BRL';
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
                valor_pre := lo_DESPESA_DIRETO_Trav_mktbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESPESA_DIRETO_Trav_mktbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Trav_mktbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Trav_mktbrl) || crlf;
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
              lo_DESPESA_DIRETO_Trav_mktbrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       ----------------------------------------------------------------------------------------------
        -- 20 - ORIGEM: DESPESA_DIRETO CATEGORIA: Travel CAMPO: VLR_DESP_DN_TRAVEL_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_DIRETO';
         log_categoria := 'Travel';
         log_campo := 'VLR_DESP_DN_TRAVEL_BRL';
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
                valor_pre := lo_DESPESA_DIRETO_Trav_dnbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESPESA_DIRETO_Trav_dnbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Trav_dnbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Trav_dnbrl) || crlf;
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
              lo_DESPESA_DIRETO_Trav_dnbrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       ----------------------------------------------------------------------------------------------
        -- 21 - ORIGEM: DESPESA_DIRETO CATEGORIA: Travel CAMPO: VLR_DESP_AE_TRAVEL_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_DIRETO';
         log_categoria := 'Travel';
         log_campo := 'VLR_DESP_AE_TRAVEL_BRL';
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
                valor_pre := lo_DESPESA_DIRETO_Trav_aebrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESPESA_DIRETO_Trav_aebrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Trav_aebrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Trav_aebrl) || crlf;
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
              lo_DESPESA_DIRETO_Trav_aebrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       ----------------------------------------------------------------------------------------------
        -- 22 - ORIGEM: DESPESA_DIRETO CATEGORIA: Contractors CAMPO: VLR_DESP_MKT_OTHERS_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_DIRETO';
         log_categoria := 'Contractors';
         log_campo := 'VLR_DESP_MKT_OTHERS_BRL';
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
                valor_pre := lo_DESPESA_DIRETO_Cont_mktbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESPESA_DIRETO_Cont_mktbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Cont_mktbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Cont_mktbrl) || crlf;
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
              lo_DESPESA_DIRETO_Cont_mktbrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       ----------------------------------------------------------------------------------------------
        -- 23 - ORIGEM: DESPESA_DIRETO CATEGORIA: Contractors CAMPO: VLR_DESP_DN_OTHERS_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_DIRETO';
         log_categoria := 'Contractors';
         log_campo := 'VLR_DESP_DN_OTHERS_BRL';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '23- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := lo_DESPESA_DIRETO_Cont_dnbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESPESA_DIRETO_Cont_dnbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Cont_dnbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_DIRETO_Cont_dnbrl) || crlf;
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
              lo_DESPESA_DIRETO_Cont_dnbrl,
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
      if(flag = 6 or flag = 99) then --DESPESA_RATEIO
      begin
        ----------------------------------------------------------------------------------------------
        -- 24 - ORIGEM: DESPESA_RATEIO CATEGORIA: Others CAMPO: VLR_DESPRAT_MKT_OTH_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_RATEIO';
         log_categoria := 'Others';
         log_campo := 'VLR_DESPRAT_MKT_OTH_BRL';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '24- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := lo_DESPESA_RATEIO_Othe_mktbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESPESA_RATEIO_Othe_mktbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_RATEIO_Othe_mktbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_RATEIO_Othe_mktbrl) || crlf;
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
              lo_DESPESA_RATEIO_Othe_mktbrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 25 - ORIGEM: DESPESA_RATEIO CATEGORIA: Others CAMPO: VLR_DESPRAT_MKT_OTH_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_RATEIO';
         log_categoria := 'Others';
         log_campo := 'VLR_DESPRAT_MKT_OTH_BRL'; --VLR_DESP_DN_OTHERS_BRL
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '25- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := lo_DESPESA_RATEIO_Othe_dnbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESPESA_RATEIO_Othe_dnbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_RATEIO_Othe_dnbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_RATEIO_Othe_dnbrl) || crlf;
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
              lo_DESPESA_RATEIO_Othe_dnbrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 26 - ORIGEM: DESPESA_RATEIO CATEGORIA: Travel CAMPO: VLR_DESPRAT_MKT_TRAV_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_RATEIO';
         log_categoria := 'Travel';
         log_campo := 'VLR_DESPRAT_MKT_TRAV_BRL';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '26- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := lo_DESPESA_RATEIO_Trav_mktbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESPESA_RATEIO_Trav_mktbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_RATEIO_Trav_mktbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_RATEIO_Trav_mktbrl) || crlf;
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
              lo_DESPESA_RATEIO_Trav_mktbrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 27 - ORIGEM: DESPESA_RATEIO CATEGORIA: Contractors CAMPO: VLR_DESP_DN_OTHERS_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_RATEIO';
         log_categoria := 'Travel';
         log_campo := 'VALOR_DESPRAT_DN_TRAV_BRL';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '27- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := lo_DESPESA_RATEIO_Trav_dnbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESPESA_RATEIO_Trav_dnbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_RATEIO_Trav_dnbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_RATEIO_Trav_dnbrl) || crlf;
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
              lo_DESPESA_RATEIO_Trav_dnbrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 28 - ORIGEM: DESPESA_RATEIO CATEGORIA: Contractors CAMPO: VLR_DESPRAT_MKT_OTH_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_RATEIO';
         log_categoria := 'Contractors';
         log_campo := 'VLR_DESPRAT_MKT_OTH_BRL';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '28- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := lo_DESPESA_RATEIO_Cont_mktbrl;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESPESA_RATEIO_Cont_mktbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_RATEIO_Cont_mktbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_RATEIO_Cont_mktbrl) || crlf;
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
              lo_DESPESA_RATEIO_Cont_mktbrl,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 29 - ORIGEM: DESPESA_RATEIO CATEGORIA: Contractors CAMPO: VALOR_DESPRAT_DN_OTH_BRL
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESPESA_RATEIO';
         log_categoria := 'Contractors';
         log_campo := 'VALOR_DESPRAT_DN_OTH_BRL';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '29- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

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
                valor_pre := lo_DESPESA_RATEIO_Cont_dnbrl;
         end;
         -- VALIDACAO             vl_DESPESA_RATEIO_Cont_dnbrl
         log_diferenca := TRUNC(((lo_DESPESA_RATEIO_Cont_dnbrl/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_RATEIO_Cont_dnbrl) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESPESA_RATEIO_Cont_dnbrl) || crlf;
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
              lo_DESPESA_RATEIO_Cont_dnbrl,
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
   assunto  := assunto || ' [PMS][DESPRATTECSAL] Resultado da Carga';
  send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
   send_mail('lnoboru@cit.com.br', assunto, mensagem);
   if log_status = 'ERRO' then
      mensagem := 'ATTENTION: Please, contact Ciandt IT Business Intelligence Team as soon as possible. ' || crlf || crlf ||
                  mensagem;
--      send_mail('infra_ti@cit.com.br', assunto, mensagem);
   end if;
end USP_PMS_DESPRATTECSAL;