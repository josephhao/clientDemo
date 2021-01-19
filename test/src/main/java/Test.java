

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
        public static void main(String[] args) {
            split();
            Pattern pattern = Pattern.compile("[ ]+");
            Matcher matcher = pattern.matcher("   ");
            if (matcher.find()) {
                System.out.println("=" + matcher.group() + "=");
            }
        }

    public static void split() {
        String s= "1";
        String[] split = s.split("=", 2);
        Console.log(StringUtils.join(split, ","));

    }

    public static void devOrg() {
        FileInputStream fis = null;
        try {
            String file = "/Users/joseph-mac/Documents/chinalife/design/orgs.txt";
            fis = new FileInputStream(file);
            Scanner scanner = new Scanner(fis);
            List<String[]> newList = new ArrayList<>();
            Map<String, String[]> map = new HashMap<>();
            int n = 61;
            while(scanner.hasNext()) {
                String l = scanner.nextLine();
                String[] split = l.split("\\s+");
//                if (split.length != 2) {
//                    System.out.println(l);
//                }
                String[] split1 = split[0].split("->");
                String last = split1[split1.length -1];

                String ot = StringUtils.join(split1 ,"->" , 0, split1.length-1);
                String[] strings = new String[] {""+(n++), split[1], last, ot, split[0], null };
                newList.add(strings);
                map.put(split[0], strings);
            }

            File file1 = new File("/Users/joseph-mac/Documents/chinalife/design/orgs.sql");
            ArrayList<String> lines = Lists.newArrayList();
            newList.forEach(s -> {
                if (StringUtils.isNotEmpty(s[3])) {
                    String[] p = map.get(s[3]);
                    s[5] = p[0];
                }


                String t = "insert into devc.devc_dictionary( " +
                        "id, key_, code, value , " +
                        "parent_id, " +
                        "del_state, description) " +
                        "values(" + s[0] +", 'dev_org', '" + s[1] + "', '" + s[2] + "', " +
                        (StringUtils.isNotEmpty(s[5])? s[5] + ", " : "null, " ) +
                        "0, '" + s[4] + "');";
                System.out.println(t);
                lines.add(t);

            });
            try {
                FileUtils.writeLines(file1, lines);
            } catch (IOException e) {
                e.printStackTrace();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
