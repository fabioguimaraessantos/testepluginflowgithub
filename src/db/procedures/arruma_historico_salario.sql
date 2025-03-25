create or replace procedure arruma_historico_salario is

cursor c_cd_pessoa is
       select t.pess_cd_pessoa
       from pessoa_salario  t
       where t.pesa_dt_fim is null
       group by t.pess_cd_pessoa
       having count(*) >= 2;


cursor c_hist_ferias (pCodigoPessoa number) is

       select t.pesa_dt_inicio, t.pesa_cd_pessoa_salario
       from pessoa_salario t
       where t.pess_cd_pessoa = pCodigoPessoa
       order by t.pesa_dt_inicio asc;

v_historico_corente c_hist_ferias%rowtype;
v_historico_proximo c_hist_ferias%rowtype;

begin

  delete from pessoa_salario ps where ps.pesa_dt_fim < ps.pesa_dt_inicio;

  commit;

  for r_cd_pessoa in c_cd_pessoa loop

      OPEN c_hist_ferias(r_cd_pessoa.pess_cd_pessoa);

      FETCH c_hist_ferias into v_historico_corente;

      FETCH c_hist_ferias into v_historico_proximo;

      while (c_hist_ferias%notfound = false) loop

            UPDATE pessoa_salario ps
            SET ps.pesa_dt_fim = add_months(v_historico_proximo.pesa_dt_inicio,-1)
            WHERE ps.pesa_cd_pessoa_salario = v_historico_corente.pesa_cd_pessoa_salario;

            v_historico_corente := v_historico_proximo;

            FETCH c_hist_ferias into v_historico_proximo;
      end loop;

      CLOSE c_hist_ferias;

  end loop;

  commit;

end arruma_historico_salario;