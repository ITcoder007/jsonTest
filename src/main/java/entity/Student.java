package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Student {

    /** 名字 */
    private String name;

    /** 年龄 */
    private Integer age;

    /** 头像 */
    private String profileImageUrl;
}