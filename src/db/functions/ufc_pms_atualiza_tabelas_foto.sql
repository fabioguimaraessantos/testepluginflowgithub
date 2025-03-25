create or replace function ufc_pms_atualiza_tabelas_foto return SYS_REFCURSOR is

--variables
refcursor sys_refcursor;

--constants
v_previous constant varchar2(2) := 'PV';

begin

  begin
    ------------------------------------
    --remove todos os registros Previous
    ------------------------------------
    delete
    from alocacao_periodo_foto apf
    where apf.alpf_in_status_foto = v_previous;

    delete
    from alocacao_foto af
    where af.alfo_in_status_foto = v_previous;

    delete
    from mapa_alocacao_foto maf
    where maf.maaf_in_status_foto = v_previous;

    commit;

    ------------------------------------
    --atualiza todos os registros de Current para Previous
    ------------------------------------
    update alocacao_periodo_foto apf
    set apf.alpf_in_status_foto = v_previous;

    update alocacao_foto af
    set af.alfo_in_status_foto = v_previous;

    update mapa_alocacao_foto maf
    set maf.maaf_in_status_foto = v_previous;

    commit;

    -- se tudo executou corretamente, retorna um (1)
    open refcursor for select 1 from dual;
    return refcursor;

  exception
    -- caso der algum erro dá rollback e retorna zero (0)
    when OTHERS then
      rollback;

    open refcursor for select 0 from dual;
    return refcursor;

  end;

end ufc_pms_atualiza_tabelas_foto;