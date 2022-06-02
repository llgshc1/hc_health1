import com.hcwawe.utils.DateUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {
    public static void main(String[] args) throws Exception {
        String dateString = "2022-5-28";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateString);
        String format = sdf.format(date);
        System.out.println(dateString);
        System.out.println(date);
        System.out.println(format);
    }


    @Test
    public void transformatDate() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String transformDate=simpleDateFormat.format(new Date());
        System.out.println("日期转换前："+new Date());
        System.out.println("日期转换后："+transformDate);
    }

}
