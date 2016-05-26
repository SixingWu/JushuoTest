package mebiuw.video.videoreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 按照截取时间,将视频的内容展开成Timeline,而这个就是Timeline的东西
 * Created by MebiuW on 16/5/26.
 */
public class Timeline {
    /**
     * 开始时间,结束时间,持续时间
     */
    private int startTime,endTime,duration;
    /**
     * 语音识别后转换的文本
     */
    private String text;
    /**
     * Tokens
     */
    private List<String> tokens;
    /**
     * 关键词
     */
    private List<String> keywords;
    /**
     * 视频地址,原始文件的地址,当前视频段的地址
     */
    private String sourceFilePosition,myFilePosition;
    /**
     * 其他一些元信息的地址,诸如图片,对应的mp3等
     */
    private Map<String,Object> metaDatas;

    /**
     * 时间线
     * @param sourceFilePosition 原始视频的地址
     * @param myFilePosition 当前分段视频地址
     * @param startTime 在整个时间片段内的开始时间
     * @param endTime 在整个时间片段内的结束时间
     * @param duration 持续时间
     */
    public Timeline(String sourceFilePosition,String myFilePosition,int startTime, int endTime, int duration) {
        this.sourceFilePosition = sourceFilePosition;
        this.myFilePosition = myFilePosition;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.tokens = new ArrayList<>();
        this.keywords = new ArrayList<>();
        this.metaDatas = new HashMap<>();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTmpAudioPosition(String str){
        this.metaDatas.put("mp3",str);
    }

    public String getMyFilePosition() {
        return myFilePosition;
    }

    public void setAudioPosition(String str){
        this.metaDatas.put("amr",str);

    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public Map<String, Object> getMetaDatas() {
        return metaDatas;
    }

    public void setMetaDatas(Map<String, Object> metaDatas) {
        this.metaDatas = metaDatas;
    }

    public String getTmpAudioPosition(){
        return (String)this.metaDatas.get("mp3");
    }

    public String getAudioPosition(){
        return (String)this.metaDatas.get("amr");
    }

    public void setImgPrefix(String str){
        this.metaDatas.put("img",str);

    }

    public String getImgPosition(int index){
        return (String)this.metaDatas.get("img")+index+".jpg";
    }

    @Override
    public String toString() {
        return "Timeline{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", text='" + text + '\'' +
                ", tokens=" + tokens +
                ", keywords=" + keywords +
                ", sourceFilePosition='" + sourceFilePosition + '\'' +
                ", myFilePosition='" + myFilePosition + '\'' +
                ", metaDatas=" + metaDatas +
                '}';
    }



}
