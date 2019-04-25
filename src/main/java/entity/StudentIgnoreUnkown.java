package entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) //忽略未知属性
public class StudentIgnoreUnkown {

    /** 名字 */
    private String name;

    /** 年龄 */
    private Integer age;

    /** 头像 */
    private String profileImageUrl;
}