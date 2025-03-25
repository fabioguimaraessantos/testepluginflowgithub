CREATE OR REPLACE PROCEDURE usp_imp_cidadebase_from_mega AS

  /* Seleciona registros para vigentes do mega */
  cursor c_vigencia is
    SELECT lbc.cd_pessoa, lbc.dt_inicio_vigencia_mega, cb.ciba_cd_cidade_base
      FROM (SELECT p.PESS_CD_PESSOA AS cd_pessoa,
                   p.PESS_CD_LOGIN AS login_pessoa,
                   MAX(m.HOR_DT_INICIO) AS dt_inicio_vigencia_mega
              FROM VW_MEGA_CIDADEBASE m
             INNER JOIN PESSOA P ON m.CD_ST_LOGIN = P.PESS_CD_LOGIN AND p.PESS_DT_RESCISAO IS NULL
             GROUP BY p.PESS_CD_PESSOA, p.PESS_CD_LOGIN) lbc
     INNER JOIN VW_MEGA_CIDADEBASE mcb ON lbc.LOGIN_PESSOA = mcb.CD_ST_LOGIN AND lbc.dt_inicio_vigencia_mega = mcb.HOR_DT_INICIO
     INNER JOIN CIDADE_BASE cb ON mcb.NM_ST_CIDADE = cb.ciba_nm_cidade_base;

  v_dt_inicio_vigencia_pms DATE;
  v_cd_cidade_base_pms     NUMBER;
  v_pessoa_cidade_base_id  NUMBER;
BEGIN

  /**
  * Importa transferencias de cidade base do MEGA para o PMS.
  * Atualiza a data de fim quando for diferente
  * Atualiza a cidade base na tabela Pessoa quando necessário
  */

  for i in c_vigencia loop

    -- Pega a data e cidade base mais recente do PMS
    SELECT nvl(max(pcb.PECB_DT_INICIO), TO_DATE('01-01-1900', 'DD-MM-YYYY')), nvl(max(pcb.CIBA_CD_CIDADE_BASE), i.ciba_cd_cidade_base)
    into v_dt_inicio_vigencia_pms, v_cd_cidade_base_pms
    FROM PESSOA_CIDADE_BASE pcb
    WHERE pcb.PECB_DT_FIM IS NULL
    AND pcb.PESS_CD_PESSOA = i.cd_pessoa;

    -- caso não exista
    -- Se data mas recente PMS é menor que Mega (Mega tem registro novo)
    -- Eu estou tentando evitar a inserção de um novo registro na mesma cidade seguida
    IF (v_dt_inicio_vigencia_pms = TO_DATE('01-01-1900', 'DD-MM-YYYY') OR (v_dt_inicio_vigencia_pms < i.dt_inicio_vigencia_mega AND v_cd_cidade_base_pms != i.ciba_cd_cidade_base)) THEN

      -- Verifica se não é o primeiro registro, atualiza a data fim atual do registro anterior.
      if (v_dt_inicio_vigencia_pms != TO_DATE('01-01-1900', 'DD-MM-YYYY')) THEN

        select p.pecb_cd_pessoa_cidade_base
          into v_pessoa_cidade_base_id
          from Pessoa_Cidade_Base p
         where p.pess_cd_pessoa = i.cd_pessoa
           and p.pecb_dt_fim is null;

        update Pessoa_Cidade_Base c
           set c.pecb_dt_fim = trunc(ADD_MONTHS(i.dt_inicio_vigencia_mega, -1), 'MONTH')
         where c.pecb_cd_pessoa_cidade_base = v_pessoa_cidade_base_id;
      end if;

      -- insere nova vigencia
      insert into pessoa_cidade_base p
        (pecb_cd_pessoa_cidade_base,
         pess_cd_pessoa,
         ciba_cd_cidade_base,
         pecb_dt_inicio,
         pecb_dt_criacao)
      values
        (sq_pecb_cd_pessoa_cidade_base.NEXTVAL,
         i.cd_pessoa,
         i.ciba_cd_cidade_base,
         trunc(i.dt_inicio_vigencia_mega, 'MONTH'),
         sysdate);

         UPDATE PESSOA pe
            SET pe.CIBA_CD_CIDADE_BASE = i.CIBA_CD_CIDADE_BASE
          WHERE pe.PESS_CD_PESSOA = i.cd_pessoa;

    -- Mega não tem registro novo, é necessário fazer alguma coisa?
    END IF;
  end loop;

  /* Finalizar transacao */
  commit;
END;