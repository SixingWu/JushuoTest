package mebiuw.common.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by MebiuW on 16/5/24.
 */
public class RuntimeWorker {
    public static boolean execute(String commands){
        System.out.println("Execute :"+commands);
        Runtime runtime = Runtime.getRuntime();
        try{
            Process process=runtime.exec(commands);
            //检查是否执行失败
            if(process.waitFor()!=0){
                if(process.exitValue() == 1){
                    return false;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();

            return false;
        }
        return true;
    }

    /**
     * 调用系统命令,并且将结果返回
     * @param commands
     * @return
     */
    public static String query(String commands){
        System.out.println("Quering :"+commands);
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(commands);
            BufferedReader buf = null;
            String line = null;
            buf = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuffer sb = new StringBuffer();
            while ((line = buf.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
                continue;
            }
            int ret = process.waitFor();
            return sb.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
