package icu.intelli.simpletest.postgresql.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wangshuo
 * @date 2021/07/26
 */
@Data
@Entity
@Table(schema = "public", name = "tb_user")
public class TbUser {

    @Id
    private String id;
    @Column
    private String name;
    @Column
    private Integer age;

}
