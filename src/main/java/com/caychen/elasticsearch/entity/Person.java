package com.caychen.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Caychen
 * @Date: 2021/4/24 11:01
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {

    private String name;

    private String gender;

    private String tel;

    private Integer age;
}
