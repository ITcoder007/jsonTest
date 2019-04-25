package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class StudentIgnore {

    /** 名字 */
    private String name;

    /** 年龄 */
    private Integer age;

    /** 头像 */
    @JsonIgnore
    private String profileImageUrl;
}