package com.f139.shop.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "good")
public class Good {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")//添加对象后返回数据的id
    private Integer id;

    @Column(name = "name")
    @NotNull(groups = {Good.Save.class, Good.Update.class})
    private String name;

    @Column(name = "category_id")
    @NotNull(groups = {Good.Save.class, Good.Update.class})
    private Integer categoryId;

    @Column(name = "datetime")
    private LocalDateTime datetime;

    @Column(name = "price")
    @NotNull(groups = {Good.Save.class, Good.Update.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    @Column(name = "images")
    private String images;

    @Column(name = "amount")
    @NotNull(groups = {Good.Save.class, Good.Update.class})
    private Integer amount;

    @Column(name = "content")
    @NotNull(groups = {Good.Save.class, Good.Update.class})
    private String content;

    @Transient //不存在的字段
    @NotNull(groups = {Good.Save.class, Good.Update.class})
    private Integer[] attributeIds;

    public interface Save{};

    public interface Update{};
}