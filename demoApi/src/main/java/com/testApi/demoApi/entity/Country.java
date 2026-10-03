package com.testApi.demoApi.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Country {
    VIETNAM(1, "Vietnam", "\uD83C\uDDFB\uD83C\uDDF3"),
    AMERICA(2, "America", "hhhh"),
    JAPAN(3, "Japan", "hhhh"),
    KOREA(4, "Korea", "hhhh"),
    GERMANY(5, "Germany", "hhhh"),
    FRANCE(6, "France", "hhhh"),
    ITALIAN(7, "Italian", "hhhh"),
    SPAIN(8, "Spain", "hhhh"),
    THAILAND(9, "Thailand", "hhhh"),
    MEXICO(10, "Mexico", "hhhh");

    int id;
    String name;
    String logo;

}
