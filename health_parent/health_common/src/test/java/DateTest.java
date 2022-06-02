import org.junit.Test;

public class DateTest {
    @Test
    public void test(){
        String date = "2019-2";
        String begin = date + "-" + "1";
        String end = "";
        int year = Integer.parseInt(date.substring(0, 4));
        String month = date.substring(5);
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            switch (month) {
                case "1":
                case "3":
                case "5":
                case "7":
                case "8":
                case "12":
                case "10":
                    end = date + "-31";
                    break;
                case "2":
                    end = date + "-29";
                    break;
                default:
                    end = date + "-30";
            }
        } else {
            switch (month) {
                case "1":
                case "3":
                case "5":
                case "7":
                case "8":
                case "12":
                case "10":
                    end = date + "-31";
                    break;
                case "2":
                    end = date + "-28";
                    break;
                default:
                    end = date + "-30";
            }

        }
        System.out.println(month);
        System.out.println(begin);
        System.out.println(end);
    }
}
