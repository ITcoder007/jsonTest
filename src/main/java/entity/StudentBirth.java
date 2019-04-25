package entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class StudentBirth {

    /** 名字 */
    private String name;

    /** 年龄 */
    private Integer age;

    /** 头像 */
    private String profileImageUrl;

    /** 生日 */
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date birthDay;
}