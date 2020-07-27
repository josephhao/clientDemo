package bat.ke.qq.com;




/**
 * 源码学院-monkey老师
 */
public class App {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        //App map=new App();
        map.put("刘一", "刘一");
        map.put("陈二", "陈二");
        map.put("张三", "张三");
        map.put("李四", "李四");
        map.put("王五", "王五");

        for (int i=0;i<100;i++){
            map.put("Monkey"+i, "源码学院只为培养BAT程序员而生");
        }
       System.out.println(map);


    }







    public void put(String key, String value) {
        System.out.printf("key:%s::::::::::::::::::hash值:%s::::::::::::::::::存储位置:%s\r\n", key, key.hashCode(), Math.abs(key.hashCode() % 15));

    }
}
