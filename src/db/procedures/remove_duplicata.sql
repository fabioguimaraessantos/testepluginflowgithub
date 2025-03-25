create or replace procedure remove_duplicata is

cursor c_duplicatas is
select t.recu_cd_recurso recu_cd_recurso, count(*) num
from pessoa t
group by t.recu_cd_recurso
having count(*) > 1;

v_cd_pessoa number;

begin

for r_dup in c_duplicatas loop

    v_cd_pessoa := 0;

    select min(t.pess_cd_pessoa)
    into v_cd_pessoa
    from pessoa t
    where t.recu_cd_recurso = r_dup.recu_cd_recurso;

    if (v_cd_pessoa <> 0) then
       delete pessoa p
       where p.recu_cd_recurso = r_dup.recu_cd_recurso
       and p.pess_cd_pessoa <> v_cd_pessoa;
    end if;

    commit;
end loop;

end remove_duplicata;