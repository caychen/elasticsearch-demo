package com.caychen.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Caychen
 * @Date: 2021/4/8 11:49
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private Long id;

    private String name;

    private Integer gendex;

    private Integer age;

    private String address;
}
