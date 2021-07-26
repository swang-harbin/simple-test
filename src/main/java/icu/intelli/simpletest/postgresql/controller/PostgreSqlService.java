package icu.intelli.simpletest.postgresql.controller;

import icu.intelli.simpletest.postgresql.dao.TbUserDAO;
import icu.intelli.simpletest.postgresql.entity.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangshuo
 * @date 2021/07/26
 */
@RestController
@RequestMapping("/postgersql")
public class PostgreSqlService {

    @Autowired
    private TbUserDAO tbUserDAO;

    @GetMapping("/user/{id}")
    public TbUser findById(@PathVariable String id) {
        return tbUserDAO.findById(id)
                .orElse(new TbUser());
    }

}
