package icu.intelli.simpletest.postgresql.dao;

import icu.intelli.simpletest.postgresql.entity.TbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author wangshuo
 * @date 2021/07/26
 */
@Repository
public interface TbUserDAO extends JpaRepository<TbUser, String> {

}
