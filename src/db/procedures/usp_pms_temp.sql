create or replace procedure usp_pms_temp is

cursor c_foto is
select t.copr_cd_contrato_pratica
      ,t.moed_sg_moeda
      ,t.moed_cd_moeda_padrao
      ,t.repm_dt_mes
      ,t.repm_vl_total_fte
      ,t.repm_vl_total_receita
      ,t.repm_dt_criacao
from receita_planejada_mes t;

begin


for r in c_foto loop
    insert into receita_resultado (rere_cd_receita_resultado,
                                   copr_cd_contrato_pratica,
                                   rere_dt_mes,
                                   rere_tx_observacao,
                                   rere_nr_fte_planejado,
                                   rere_vl_receita_planejada,
                                   rere_vl_receita_realizada,
                                   more_cd_motivo_resultado,
                                   rere_dt_foto)
   values (sq_rere_cd_receita_resultado.nextval,
           r.copr_cd_contrato_pratica,
           r.repm_dt_mes,
           null,
           r.repm_vl_total_fte,
           r.repm_vl_total_receita,
           null,
           null,
           r.repm_dt_criacao);
end loop;

end usp_pms_temp;