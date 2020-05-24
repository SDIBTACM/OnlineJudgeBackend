package cn.edu.sdtbu.model.vo.user;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-23 21:07
 */
@Data
public class UserClassListNode {
    Long id;

    String name;

    Long total;

    Timestamp createAt;
}
