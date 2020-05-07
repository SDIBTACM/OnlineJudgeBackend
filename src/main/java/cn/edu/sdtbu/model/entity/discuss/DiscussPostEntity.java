package cn.edu.sdtbu.model.entity.discuss;

import cn.edu.sdtbu.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-05-04 14:33
 */
@Data
@Entity
@Table(name = "discuss_post")
@ToString
@EqualsAndHashCode(callSuper = true)
public class DiscussPostEntity extends BaseEntity {
    @Column
    Long ownerId;

    @Column
    Long topicId;

    @Column
    Boolean status;
}
