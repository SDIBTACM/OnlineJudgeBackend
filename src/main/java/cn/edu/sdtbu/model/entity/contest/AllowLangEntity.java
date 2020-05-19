package cn.edu.sdtbu.model.entity.contest;

import cn.edu.sdtbu.model.entity.base.BaseEntityWithDeleteTs;
import cn.edu.sdtbu.model.enums.LangType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-27 09:52
 */
@Data
@Entity
@Table(name = "contest_allow_lang", indexes = {
    @Index(name = "idx_contest_id", columnList = "contestId")
})
@ToString
@EqualsAndHashCode(callSuper = true)
public class AllowLangEntity extends BaseEntityWithDeleteTs {
    @Column
    Long contestId;

    @Column
    LangType lang;
}
