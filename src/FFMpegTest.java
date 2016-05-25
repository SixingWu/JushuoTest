import org.json.JSONObject;

/**
 * Created by MebiuW on 16/5/25.
 */
public class FFMpegTest {
    public static void main(String args[]){
        String file = "/home/azureuser/video/a6300.mp4";
        String tid = "23423423";

        //将文件下载,并按照tid保存到零时目录下
       // RuntimeWorker.execute("cd /home/azureuser/server/raws");
        //调用查询视频的长度信息
        String videoInfor = RuntimeWorker.query("ffprobe -v quiet -print_format json -show_format -show_streams " + file);
        JSONObject jsonInfor = new JSONObject(videoInfor);
        String suffix = file.substring(file.lastIndexOf("."));
        double duration = jsonInfor.getJSONArray("streams").getJSONObject(0).getDouble("duration");
        //按照15S对他进行拆分
        for(int i=0;i<duration;i+=15){
            RuntimeWorker.query("ffmpeg -ss "+i+" -t"+15+" -i "+file+" -acodec copy -vcodec copy /home/azureuser/server/raws/"+tid+"#"+i+suffix);
        }
        //截取对应的图片和进行音频的转化 视频每段->mp3->amr
        for(int i=0;i<duration;i+=15){
            String subfilename=tid+"#"+i+suffix;
            String tmpname = tid+"#"+i+".mp3";
            String finalname = tid+"#"+i+".amr";
            RuntimeWorker.query("ffmpeg -i /home/azureuser/server/raws/"+subfilename+" -acodec copy -vn /home/azureuser/server/raws/"+tmpname);
            RuntimeWorker.query("ffmpeg -i /home/azureuser/server/raws/"+tmpname+" -ar 8000 -ab 12.2k -ac 1 /home/azureuser/server/raws/"+finalname);
        }
    }
}
