import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import entity.jackson.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import util.JacksonUtil;

import java.io.IOException;
import java.util.*;

@Slf4j
public class JacksonTest{
    /**
     * 各种对象转为 字符串
     */
    @Test
    public void obj2String(){
        Student student = new Student();
        student.setAge(18);
        student.setName("小明儿");
        student.setProfileImageUrl("");
        String res1 = JacksonUtil.obj2String(student);
        // {"name":"小明儿","age":18,"profileImageUrl":""}
        System.out.println(res1);
        res1 = JacksonUtil.obj2StringPretty(student);
        /*
            {
              "name" : "小明儿",
              "age" : 18,
              "profileImageUrl" : ""
            }
         */
        System.out.println(res1);

        HashMap<String, List<Integer>> map = new HashMap();
        map.put("a" , Arrays.asList(1,2,3));
        map.put("b", Arrays.asList(4,5,6));
        String res2 = JacksonUtil.obj2String(map);
        // {"a":[1,2,3],"b":[4,5,6]}
        System.out.println(res2);
        res2 = JacksonUtil.obj2StringPretty(map);
        /*
            {
              "a" : [ 1, 2, 3 ],
              "b" : [ 4, 5, 6 ]
            }
         */
        System.out.println(res2);
    }


    @Test
    public void string2Obj() {
        String str = "{\"name\":\"小明儿\",\"age\":18,\"profileImageUrl\":\"\"}";
        Student student = JacksonUtil.string2Obj(str, Student.class);
        // entity.jackson.Student(name=name, age=10, profileImageUrl=link)
        System.out.println(student);

        List<Student> students = new ArrayList<Student>();
        students.add(student);
        students.add(student);
        String listStr = JacksonUtil.obj2String(students);
        // [{"name":"小明儿","age":18,"profileImageUrl":""},{"name":"小明儿","age":18,"profileImageUrl":""}]
        System.out.println(listStr);
        List list = JacksonUtil.string2Obj(listStr, List.class);
        // [{name=小明儿, age=18, profileImageUrl=}, {name=小明儿, age=18, profileImageUrl=}]
        System.out.println(list);

        list = JacksonUtil.string2Obj(listStr, new TypeReference<List<Student>>(){});
        // [entity.jackson.Student(name=小明儿, age=18, profileImageUrl=), entity.jackson.Student(name=小明儿, age=18, profileImageUrl=)]
        System.out.println(list);

        list = JacksonUtil.string2Obj(listStr, List.class , Student.class);
        // [entity.jackson.Student(name=小明儿, age=18, profileImageUrl=), entity.jackson.Student(name=小明儿, age=18, profileImageUrl=)]
        System.out.println(list);

        String mapStr = "{\"a\":[1,2,3],\"b\":[4,5,6]}";
        Map map = JacksonUtil.string2Obj(mapStr, Map.class);
        // {a=[1, 2, 3], b=[4, 5, 6]}
        System.out.println(map);
    }

    @Test
    public void jsonIgnore() {
        // 序列化： 被忽略的字段，不会序列化到字符串
        StudentIgnore student = new StudentIgnore();
        student.setAge(10);
        student.setName("name");
        student.setProfileImageUrl("link");
        String result = JacksonUtil.obj2String(student);
        // {"name":"name","age":10}
        System.out.println(result);

        // 反序列化： 被忽略的字段，会变为null
        String str = "{\"name\":\"name\",\"age\":10,\"profileImageUrl\":\"link\"}";
        StudentIgnore student1 = JacksonUtil.string2Obj(str, StudentIgnore.class);
        // entity.jackson.Student(name=name, age=10, profileImageUrl=null)
        System.out.println(student1);
    }

    @Test
    public void jsonPro() {
        // 序列化： 被忽略的字段，不会序列化到字符串
        StudentPro student = new StudentPro();
        student.setAge(10);
        student.setName("name");
        student.setProfileImageUrl("link");
        String result = JacksonUtil.obj2String(student);
        // {"name":"name","profileImageUrl":"link","ageee":10}
        System.out.println(result);

        // 使用 jackson反序列化时，如果 字符串 中有未知属性，则会抛出异常
        String str = "{\"name\":\"name\",\"ageee\":10,\"profileImageUrl\":\"link\"}";
        StudentPro student1 = JacksonUtil.string2Obj(str, StudentPro.class);
        // entity.jackson.StudentPro(name=name, age=10, profileImageUrl=link)
        System.out.println(student1);
    }

    @Test
    public void jsonDateFormat() {
        // 序列化：
        StudentBirth student = new StudentBirth();
        student.setAge(10);
        student.setName("name");
        student.setProfileImageUrl("link");
        student.setBirthDay( new Date() );
        String result = JacksonUtil.obj2String(student);
        // {"name":"name","age":10,"profileImageUrl":"link","birthDay":"2019-04-25 17:04:14"}
        System.out.println(result);

        // 反序列化
        StudentBirth student1 = JacksonUtil.string2Obj(result, StudentBirth.class);
        // StudentBirth(name=name, age=10, profileImageUrl=link, birthDay=Thu Apr 25 17:04:14 CST 2019)
        System.out.println(student1);
    }

    @Test
    public void jsonIgnoreUnknown() {
        String str = "{\"name\":\"name\",\"age\":10,\"profileImageUrl\":\"link\",\"birthDay\":\"2019-04-25 17:04:14\"}";
        // 反序列化: 正常
        StudentBirth student1 = JacksonUtil.string2Obj(str, StudentBirth.class);
        // StudentBirth(name=name, age=10, profileImageUrl=link, birthDay=Thu Apr 25 17:04:14 CST 2019)
        System.out.println(student1);

        // 反序列化: 有未知属性
        try {
            Student student2 = JacksonUtil.string2Obj(str, Student.class);
            // StudentBirth(name=name, age=10, profileImageUrl=link, birthDay=Thu Apr 25 17:04:14 CST 2019)
            System.out.println(student2);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        // 反序列化： 忽略未知属性
        StudentIgnoreUnkown student3 = JacksonUtil.string2Obj(str, StudentIgnoreUnkown.class);
        // StudentBirth(name=name, age=10, profileImageUrl=link, birthDay=Thu Apr 25 17:04:14 CST 2019)
        System.out.println(student3);
    }

    @Test
    public void jsonTreeMode() throws IOException {
        /**
         {
             "name": "zhansan",
             "age": 100,
             "schools": [
                 {
                     "name": "tsinghua",
                     "location": "beijing"
                 },
                 {
                     "name": "pku",
                     "location": "beijing"
                 }
             ]
         }
         */
        String str = "{\"name\":\"zhansan\",\"age\":100,\"schools\":[{\"name\":\"tsinghua\",\"location\":\"beijing\"},{\"name\":\"pku\",\"location\":\"beijing\"}]}";

        JsonNode jsonNode = JacksonUtil.objectMapper.readTree(str);
        String name = jsonNode.get("name").asText();
        Integer age = jsonNode.get("age").asInt();
        log.info("name is {} , age is {}" , name , age);

        JsonNode schools = jsonNode.get("schools");
        int size = schools.size();
        if (size > 0){
            for (int i = 0; i < size; i++) {
                JsonNode school = schools.get(i);
                String sName = school.get("name").asText();
                String sLocation = school.get("location").asText();
                log.info("sName is {} , sLocation is {}" , sName , sLocation);
            }
        }
    }
}
