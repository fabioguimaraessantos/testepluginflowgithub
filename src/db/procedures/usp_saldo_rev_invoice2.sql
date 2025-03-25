CREATE OR REPLACE PROCEDURE USP_SALDO_REV_INVOICE2 is
--31/07/2012
valor_associado number := 0;
ant_revenue number := 0;
ant_invoice number := 0;

saldo_revenue number := 0;
saldo_invoice number := 0;

deal_fiscal_id number(18) := null;
mudou boolean := false;

cod_fatura number(18) := null;
total_fautra number := 0;

data_invoice date := null;

--gravou boolean := false;

----------------------------------------------------------------------------------------------------------------------------------------------------------------
-- DECLARANDO CURSOR DA RECEITA
cursor cursor_receita is
          select rdf.defi_cd_deal_fiscal      cd_deal_fiscal,
                 df.defi_nm_deal_fiscal       nome_deal_fiscal,
                 rec.rece_dt_mes              dt_mes,
                 rec_moe.remo_vl_total_moeda  valor_total_receita,
                 rdf.redf_cd_receita_dfiscal  cd_receita_dfiscal,
                 rec.rece_cd_receita          cd_receita
          from   receita_deal_fiscal rdf,
                 receita             rec,
                 receita_moeda       rec_moe,
                 deal_fiscal         df
          where  rdf.defi_cd_deal_fiscal = df.defi_cd_deal_fiscal
                 and rdf.remo_cd_receita_moeda = rec_moe.remo_cd_receita_moeda
                 and rec.rece_cd_receita = rec_moe.rece_cd_receita
                AND rdf.defi_cd_deal_fiscal    in (1,2) --=====> PARA TESTE
          order  by df.defi_cd_deal_fiscal,
                    rec.rece_dt_mes;


--DECLARANDO VARIAVEL PARA O CURSOSR
var_receita cursor_receita%rowtype;


begin
            open cursor_receita; --001

            loop   --- LOOP PARA PERCORRER A RECEITA.

                 --Lendo registro do cursor
                  if saldo_invoice <= 0 and not mudou then
                      fetch cursor_receita into var_receita;
                  end if;

                 -- Verifica se chegou ao fim. Se sim, cai fora
                 exit when cursor_receita%notfound;

                if saldo_invoice > 0 then
                                         mudou := True;

                                         ant_revenue := case when saldo_revenue < 0 then 0 else saldo_revenue end;
                                         ant_invoice := case when saldo_invoice < 0 then 0 else saldo_invoice end;
                                         valor_associado := saldo_invoice;

/*                                         if (ant_revenue < ant_invoice) and (ant_invoice > 0) then
                                             valor_associado := ant_revenue;
                                         else
                                             valor_associado := ant_invoice;
                                          end if;*/

                                          --valor_associado := saldo_invoice;


                                         saldo_revenue := var_receita.valor_total_receita - saldo_invoice;
                                         saldo_invoice := saldo_invoice - var_receita.valor_total_receita;

                                         deal_fiscal_id := var_receita.cd_deal_fiscal;


                                         insert into tb_bi_pms_saldo_rev_invoice
                                         (
                                                cd_sequencial,
                                                 mes_processamento,
                                                 cd_fiscal_deal,
                                                 cd_receita,
                                                 cd_fatura,
                                                 valor_saldo_revenue,
                                                 valor_saldo_invoice,
                                                 valor_revenue,
                                                 valor_invoice,
                                                 VALOR_ASSOCIADO,
                                                 VALOR_SALDO_ANT_INV,
                                                 VALOR_SALDO_ANTERIOR_REV,
                                                 DATA_REVENUE,
                                                 DATA_INVOICE
                                         )
                                         values
                                        (
                                                (select nvl(max(cd_sequencial), 0) + 1 from tb_bi_pms_saldo_rev_invoice),
                                                 to_date('01'|| to_char(sysdate,'/MM/YYYY')),
                                                 var_receita.cd_deal_fiscal,
                                                 var_receita. cd_receita,
                                                 cod_fatura,
                                                  case when saldo_revenue < 0 then 0 else saldo_revenue end,
                                                 case when saldo_invoice < 0 then 0 else saldo_invoice end,
                                                 var_receita.valor_total_receita,
                                                 total_fautra,
                                                 valor_associado,
                                                 ant_invoice,
                                                 ant_revenue,
                                                 var_receita.dt_mes,
                                                 --var_invoice.dt_mes_fatura
                                                 data_invoice
                                         );

                                         commit;
                                         --gravou := true;

                                         --fetch cursor_receita into var_receita;

                                 else  if saldo_invoice <= 0 then
                                       mudou := False;
                                       ----------------------------------------------------------------------------------------------------------------------------------------------------------------
                                       --DECLARANDO CURSOR DE INVOICE
                                       declare cursor cursor_invoice is

                                               select fat.defi_cd_deal_fiscal,
                                                      df_inv.defi_nm_deal_fiscal nome_deal_fiscal,
                                                      fat.fatu_dt_previsao dt_mes_fatura,
                                                      ifat.valor_item_fatura,
                                                      ifat.fatu_cd_fatura
                                               from   fatura fat,
                                                      (select fatu_cd_fatura,
                                                              valor_item_fatura
                                                       from   (select fatu_cd_fatura,
                                                                      sum(itfa_vl_item) over(partition by fatu_cd_fatura order by fatu_cd_fatura) valor_item_fatura,
                                                                      row_number() over(partition by fatu_cd_fatura order by fatu_cd_fatura) nlinha
                                                               from   item_fatura)
                                                       where  nlinha = 1) ifat,
                                                      deal_fiscal df_inv
                                               where  fat.fatu_cd_fatura = ifat.fatu_cd_fatura
                                                      and fat.defi_cd_deal_fiscal =  df_inv.defi_cd_deal_fiscal
                                                      and fat.defi_cd_deal_fiscal =  var_receita.cd_deal_fiscal
                                                      and ifat.fatu_cd_fatura not in (
                                                                                                         select cd_fatura
                                                                                                         from   (
                                                                                                                         select cd_fatura,   cd_fiscal_deal,  row_number() over(partition by cd_fatura, cd_fiscal_deal  order by cd_fatura, cd_fiscal_deal ) linha
                                                                                                                         from   tb_bi_pms_saldo_rev_invoice
                                                                                                                         where  valor_saldo_invoice <= 0
                                                                                                                                     --and cd_fiscal_deal = fat.defi_cd_deal_fiscal
                                                                                                                       )
                                                                                                                      where  linha = 1
                                                                                                                                  --and
                                                                                                                                   --cd_fiscal_deal =  fat.defi_cd_deal_fiscal
                                                                                                                                   --NO BLOCO QUE TRAZ OS SALDOS DA FATURAS, NÃO PODE FILTRAR POR FISCAL DEAL
                                                      )
                                                      --and ifat.fatu_cd_fatura not in ( SELECT  cd_fatura FROM tb_bi_pms_saldo_rev_invoice where valor_saldo_invoice <= 0 )
                                                      --and not exists ( SELECT  cd_fatura FROM tb_bi_pms_saldo_rev_invoice where valor_saldo_invoice <= 0 and CD_FISCAL_DEAL = fat.defi_cd_deal_fiscal)
                                               order  by 1,
                                                         3;

                                               var_invoice cursor_invoice%rowtype;
                                               begin
                                                           open cursor_invoice;

                                                           loop
                                                                --Lendo registro do cursor
                                                                fetch cursor_invoice into var_invoice;

                                                                -- Verifica se chegou ao fim. Se sim, cai fora
                                                                exit when cursor_invoice%notfound;

                                                                ant_revenue :=  case when saldo_revenue < 0 then 0 else saldo_revenue end;
                                                                ant_invoice := case when saldo_invoice < 0 then 0 else saldo_invoice end;



                                                                if (deal_fiscal_id is null ) or (deal_fiscal_id <> var_receita.cd_deal_fiscal) then
                                                                     saldo_revenue := var_receita.valor_total_receita - (var_invoice.valor_item_fatura);
                                                                     saldo_invoice := (var_invoice.valor_item_fatura)- var_receita.valor_total_receita;
                                                                     valor_associado := var_invoice.valor_item_fatura;
                                                                  else if (saldo_revenue > 0) then
                                                                             valor_associado := saldo_revenue;
                                                                             saldo_invoice := (var_invoice.valor_item_fatura) - saldo_revenue;
                                                                             saldo_revenue := saldo_revenue - (var_invoice.valor_item_fatura);
                                                                      else
                                                                             valor_associado := saldo_invoice;
                                                                             saldo_invoice := var_invoice.valor_item_fatura - saldo_revenue;
                                                                             saldo_revenue := var_receita.valor_total_receita - (var_invoice.valor_item_fatura);
                                                                     end if;
                                                                end if;

                                                                   --var_receita.valor_total_receita - (var_invoice.valor_item_fatura);

/*                                                                if  ((ant_revenue = 0) and (ant_invoice = 0)) then
                                                                    valor_associado := case when  ((var_receita.valor_total_receita <   var_invoice.valor_item_fatura) and (var_invoice.valor_item_fatura > 0)) then  var_receita.valor_total_receita else var_invoice.valor_item_fatura end;
                                                                else  if (ant_revenue < ant_invoice) and (ant_invoice > 0) then
                                                                             valor_associado := ant_revenue;
                                                                        else
                                                                             valor_associado := ant_invoice;
                                                                         end if;
                                                                end if; */

                                                                --valor_associado := var_invoice.valor_item_fatura;

                                                                --ant_invoice :=  case when saldo_invoice < 0 then 0 else saldo_invoice end;

                                                                deal_fiscal_id := var_receita.cd_deal_fiscal;

                                                                 total_fautra  :=  var_invoice.valor_item_fatura;
                                                                 cod_fatura := var_invoice.fatu_cd_fatura;

                                                                 data_invoice := var_invoice.dt_mes_fatura;

                                                                 insert into tb_bi_pms_saldo_rev_invoice
                                                                    (cd_sequencial,
                                                                     mes_processamento,
                                                                     cd_fiscal_deal,
                                                                     cd_receita,
                                                                     cd_fatura,
                                                                     valor_saldo_revenue,
                                                                     valor_saldo_invoice,
                                                                      valor_revenue,
                                                                      valor_invoice,
                                                                       VALOR_ASSOCIADO,
                                                                       VALOR_SALDO_ANT_INV,
                                                                       VALOR_SALDO_ANTERIOR_REV,
                                                                         DATA_REVENUE,
                                                                         DATA_INVOICE
                                                                     )
                                                                 values
                                                                    ((select nvl(max(cd_sequencial), 0) + 1 from tb_bi_pms_saldo_rev_invoice),
                                                                     to_date('01'|| to_char(sysdate,'/MM/YYYY')),
                                                                     var_receita.cd_deal_fiscal,
                                                                     var_receita. cd_receita,
                                                                     var_invoice.fatu_cd_fatura,
                                                                      case when saldo_revenue < 0 then 0 else saldo_revenue end,
                                                                     case when saldo_invoice < 0 then 0 else saldo_invoice end,
                                                                     var_receita.valor_total_receita,
                                                                     var_invoice.valor_item_fatura,
                                                                     valor_associado,
                                                                     ant_invoice,
                                                                     ant_revenue,
                                                                     var_receita.dt_mes,
                                                                     var_invoice.dt_mes_fatura
                                                                     );
                                                                     commit;
                                                                    -- gravou := True;


                                                                     if saldo_revenue <= 0 then
                                                                             --Lendo registro do cursor
                                                                             fetch cursor_receita into var_receita;

                                                                             -- Verifica se chegou ao fim. Se sim, cai fora
                                                                             --exit when cursor_receita%notfound;
                                                                             exit;

                                                                             deal_fiscal_id := null;
                                                                     end if;



                                                           end loop; -- FIM DO LOOP DA FATURA

                                                           close cursor_invoice;

                                                           --commit;
                                               end;
                                 end if;
                end if;

            end loop;  -- FIM DO LOOP DA RECEITA

          --  if (gravou) then commit; end if;

            close cursor_receita; -- Fecha o Cursor. --001

end USP_SALDO_REV_INVOICE2;
