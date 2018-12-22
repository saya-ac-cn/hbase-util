package ac.cn.saya.hbase.weibo;

/**
 * @Title: Message
 * @ProjectName hbase
 * @Description: TODO
 * @Author Saya
 * @Date: 2018/12/22 18:00
 * @Description:
 */

public class Message {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 发布时间
     */
    private String timeStamp;

    /**
     * 微博内容
     */
    private String content;

    public Message() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "Message[uid="+ uid + ",timeStamp="+ timeStamp +",content="+ content +"]";
    }

}
