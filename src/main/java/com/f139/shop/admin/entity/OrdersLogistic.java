package com.f139.shop.admin.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders_logistic")
public class OrdersLogistic {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "content")
    private String content;

    @Column(name = "datetime")
    private Date datetime;
}