create or replace procedure usp_pms_copia_repm_p_rere (p_data_mes in date) is


-- Cursor que busca a receita projetada da tabela receita_planejada_mes (foto)
cursor c_receita_projetada(p_mes date) is
select r.copr_cd_contrato_pratica,
r.copr_nm_contrato_pratica,
r.moed_cd_moeda_padrao,
r.moed_sg_moeda,
r.repm_dt_mes,
r.repm_vl_total_fte,
r.repm_vl_total_receita,
r.repm_dt_criacao
from receita_planejada_mes r
where r.repm_dt_mes = p_mes
order by r.repm_cd_receita_planejada_mes;

v_rere_cd_receita_resultado number;

begin

    for r_rec in c_receita_projetada( trunc(p_data_mes, 'mm') ) loop
       
        select max(rr.rere_cd_receita_resultado)
        into v_rere_cd_receita_resultado 
        from receita_resultado rr
        where rr.rere_dt_mes = trunc(p_data_mes, 'mm')
        and rr.copr_cd_contrato_pratica = r_rec.copr_cd_contrato_pratica ;
        
        -- se encontrar alguma justificativa
        if v_rere_cd_receita_resultado > 0 then 
           -- atualiza o valor da receita e do fte
           update receita_resultado re
           set re.rere_vl_receita_planejada = r_rec.repm_vl_total_receita
               ,re.rere_nr_fte_planejado = r_rec.repm_vl_total_fte
           where  re.rere_cd_receita_resultado = v_rere_cd_receita_resultado;
           
       -- se não encontrar, apenas insere
        else
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
                   r_rec.copr_cd_contrato_pratica,
                   r_rec.repm_dt_mes,
                   null,
                   r_rec.repm_vl_total_fte,
                   r_rec.repm_vl_total_receita,
                   null,
                   null,
                   r_rec.repm_dt_criacao);
       end if;        
    end loop;
    commit;
end usp_pms_copia_repm_p_rere;