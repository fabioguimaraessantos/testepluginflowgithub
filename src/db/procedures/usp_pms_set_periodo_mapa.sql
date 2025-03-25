create or replace procedure usp_pms_set_periodo_mapa is

-- declaração de variavies
v_mapa_min_date date;
v_mapa_max_date date;
v_data_inicio_janela date;
v_error_code number;
v_error_text varchar(200);
v_message varchar(500);

begin

FOR mapa IN (select maal_cd_mapa_alocacao from mapa_alocacao) LOOP

begin
     -- pego a menor data do mapa de aloação
    select MIN(f.faum_dt_mes)
    into v_mapa_min_date
    from fator_ur_mes f where f.maal_cd_mapa_alocacao = mapa.maal_cd_mapa_alocacao;

    -- pego a maior data do mapa de aloação
    select MAX(f.faum_dt_mes)
    into v_mapa_max_date
    from fator_ur_mes f where f.maal_cd_mapa_alocacao = mapa.maal_cd_mapa_alocacao;

    ------------------------------------------
    -- calculo a data inicio da janela
    --
    select add_months(m.modu_dt_fechamento, -1)
    into v_data_inicio_janela
    from modulo m where m.modu_cd_modulo = 1;
    -- Verifico se o mapa possui mais de 12 meses
    -- se falso a 'data inicio da janela' é setada com a 'data inicial do mapa'
    if (add_months(v_mapa_min_date, 11) > v_mapa_max_date) then
       v_data_inicio_janela := v_mapa_min_date;
    elsif (add_months(v_data_inicio_janela,11) > v_mapa_max_date) then
       v_data_inicio_janela := add_months(v_mapa_max_date, -11);
    elsif (v_data_inicio_janela < v_mapa_min_date) then
          v_data_inicio_janela := v_mapa_min_date;
    end if;
    ------------------------------------------

    -- seta o periodo do mapa de alocação.
    update mapa_alocacao ma
    set ma.maal_dt_inicio = v_mapa_min_date,
        ma.maal_dt_fim = v_mapa_max_date,
        ma.maal_dt_inicio_janela = v_data_inicio_janela
    where ma.maal_cd_mapa_alocacao = mapa.maal_cd_mapa_alocacao;

    exception
    when OTHERS then
      rollback;
      v_error_code := SQLCODE;
      v_error_text := SUBSTR(SQLERRM, 1,200);
      v_message := 'Error('||v_error_code||']'|| ' - ' || v_error_text;

      dbms_output.put_line(v_message);
end;

END LOOP

commit;

end usp_pms_set_periodo_mapa;