create or replace function check_vocabulary_unique(i_id bigint, i_user_id bigint, i_first_word_id bigint, i_second_word_id bigint) returns boolean
    language plpgsql
as
$$
declare
    v_count integer;
  begin
    insert into logs values (i_id, i_first_word_id, i_second_word_id, v_count);
    select count(*)
    into v_count
    from vocabulary v
    where v.user_id = i_user_id
      and v.id != coalesce(i_id, 0)
      and (
            (v.first_word_id = i_first_word_id and v.second_word_id = i_second_word_id) or
            (v.first_word_id = i_second_word_id and v.second_word_id = i_first_word_id)
        );

    if v_count > 0 then
      return false;
    end if;

    return true;
  end
$$;

create or replace function check_diff_languages(i_first_word_id bigint, i_second_word_id bigint)
    returns bigint AS
$$
select count(distinct w.language)
from words w
where w.id in (i_first_word_id, i_second_word_id)
$$ language sql;
