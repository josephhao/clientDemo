package bat.ke.qq.com.array;

public class Array {


    //数组:采用一段连续的存储单元来存储数据。
    //特点:指定下标O(1) 删除插入O（N） 数组：查询快 插入慢
    public static void main(String[] args) {
        Integer integers[]=new Integer[10];
        integers[0] = 0;
        integers[1] = 1;
        integers[4] = 2;
        integers[4] = 200;
        System.out.println(integers[4] );
    }











   /* public static void main2(String[] args) {
        Entry[] entries = new Entry[10];
        entries[4]=new Entry("刘一","刘一",671464,null);
        entries[4]=new Entry("张三","张三",671464,null);
        *//*integers[0] = 0;
        integers[1] = 1;
        integers[2] = 2;
        integers[9] = 9;
        integers[8] = 9;*//*
        System.out.println(entries[4] );
    }*/
}
