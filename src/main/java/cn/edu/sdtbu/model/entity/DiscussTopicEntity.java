package cn.edu.sdtbu.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-05-04 14:28
 */
@Data
@Entity
@Table(name = "discuss_topic")
@ToString
@EqualsAndHashCode(callSuper = true)
public class DiscussTopicEntity extends BaseEntity {
    @Column
    Long ownerId;

    @Column
    Long contestId;

    @Column
    Long problemId;

    @Column
    String title;

    @Column
    Boolean status;

    @Column
    Long views;

    @Column
    Long replies;

    @Column
    Timestamp latestReplyAt;
}
