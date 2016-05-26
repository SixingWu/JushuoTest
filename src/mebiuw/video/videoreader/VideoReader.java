package mebiuw.video.videoreader;

import org.json.JSONObject;
import mebiuw.common.system.RuntimeWorker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取Video的内容,并将其转化成图片和文字等
 * 目前仅支持中文
 * Created by MebiuW on 16/5/26.
 */
public class VideoReader {
    /**
     * 在磁盘上进行操作所需要的文件地址
     */
    private final String folderLocation;
    /**
     * 百度的APPKey和Secret Key等
     */
    private final String baiduAppKey;
    private final String baiduSecretKey;
    /**
     *截取间隔
     */
    private final int interval;

    /**
     * 默认按照截取间隔15S算
     * @param folderLocation 进行视频操作的时候,需要一个本地的视频保存位置,请给出他的地址
     * @param baiduAppKey 语音识别百度用到的APPKey
     * @param baiduSecretKey 语音识别用到的SecretKey
     */
    public VideoReader(String folderLocation, String baiduAppKey, String baiduSecretKey) {
        this.folderLocation = folderLocation;
        this.baiduAppKey = baiduAppKey;
        this.baiduSecretKey = baiduSecretKey;
        this.interval = 15;
    }

    /**
     *
     * @param folderLocation 进行视频操作的时候,需要一个本地的视频保存位置,请给出他的地址
     * @param baiduAppKey 语音识别百度用到的APPKey
     * @param baiduSecretKey 语音识别用到的SecretKey
     * @param interval 截取间隔
     */
    public VideoReader(String folderLocation, String baiduAppKey, String baiduSecretKey, int interval) {
        this.folderLocation = folderLocation;
        this.baiduAppKey = baiduAppKey;
        this.baiduSecretKey = baiduSecretKey;
        this.interval = interval;
    }

    public void process(String tid, String filePosition){
        //首先获得市场
        double duration = this.queryVideoDuration(filePosition);
        worker(tid,filePosition,duration);
    }

    /**
     * 将视频进行切片
     * @param tid 处理编号
     * @param filePosition 文件地址
     * @param duration 持续时长
     * @return
     */
    public List<Timeline> worker(String tid, String filePosition, double duration){
        List<Timeline> timelines = new ArrayList<Timeline>();
        String suffix = filePosition.substring(filePosition.lastIndexOf('.'));
        boolean flag = true;
        for(int i=0;i<duration;i+=interval){
            Timeline timeline = new Timeline(filePosition,folderLocation+"/"+tid+"#"+i+suffix,i,i+(int)interval,(int)interval);
            timeline.setTmpAudioPosition(folderLocation+"/"+tid+"#"+i+".mp3");
            timeline.setAudioPosition(folderLocation+"/"+tid+"#"+i+".amr");

            /**
             * 切成小视频,转化成mp3,然后再次转化为amr
             */
            flag &= RuntimeWorker.execute("ffmpeg -y -ss "+i+" -t "+interval+" -i "+filePosition+" -acodec copy -vcodec copy "+timeline.getMyFilePosition());
            flag &=RuntimeWorker.execute("ffmpeg -y -i "+timeline.getMyFilePosition()+" -vn "+timeline.getTmpAudioPosition());
            flag &=RuntimeWorker.execute("ffmpeg -y -i "+timeline.getTmpAudioPosition()+" -ar 8000 -ab 12.2k -ac 1 "+timeline.getAudioPosition());
            timeline.setText(TextToAudio.convert(timeline.getAudioPosition()));
            /**
             * 进行截图
             */
            timeline.setImgPrefix(folderLocation+"/"+tid+"#");
            for(int j=i;j<i+interval;j++){
                flag &= RuntimeWorker.execute("ffmpeg -y -ss "+j+"  -i "+filePosition+" -f image2 -y  "+timeline.getImgPosition(j));
            }
            System.out.println(timeline.toString());
            timelines.add(timeline);
        }
        System.out.println(timelines);
        return timelines;
    }

    private double queryVideoDuration(String filePosition){
        String videoInfor = RuntimeWorker.query("ffprobe -v quiet -print_format json -show_format -show_streams " + filePosition);
        JSONObject jsonInfor = new JSONObject(videoInfor);
        double duration = jsonInfor.getJSONArray("streams").getJSONObject(0).getDouble("duration");
        return duration;
    }


}
