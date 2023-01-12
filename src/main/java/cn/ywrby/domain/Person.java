package cn.ywrby.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Person {
    private int id;
    private String name;
    private String profile;
    private Date birthday;
    private int followed;

    private int roleID;
    private String role;
}
