package com.xcd.www.internet.rong;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by gs on 2018/10/21.
 */

/**
 *value:消息类型
 枚举值	说明
 MessageTag.NONE	为空值，不表示任何意义，发送的自定义消息不会在会话页面和会话列表中展示。
 MessageTag.ISCOUNTED	表示客户端收到消息后，要进行未读消息计数（未读消息数增加 1），所有内容型消息都应该设置此值。非内容类消息暂不支持消息计数。
 MessageTag.ISPERSISTED	表示客户端收到消息后，要进行存储，并在之后可以通过接口查询，存储后会在会话界面中显示。
 MessageTag.STATUS	在本地不存储，不计入未读数，并且如果对方不在线，服务器会直接丢弃该消息，对方如果之后再上线也不会再收到此消息(聊天室类型除外，此类消息聊天室会视为普通消息)。
 */
@MessageTag(value = "app:RedPkgMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class RedPackageMessage extends MessageContent {

    //自定义的属性
    private String sendRedType;
    private String sendName;
    private String id;
    private String remark;

    public RedPackageMessage() {
        super();
    }

    /*
        *
        * 实现 encode() 方法，该方法的功能是将消息属性封装成 json 串，
        * 再将 json 串转成 byte 数组，该方法会在发消息时调用，如下面示例代码：
        * */
    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("sendRedType", this.getSendRedType());
            jsonObj.put("sendName",this.getSendName());
            jsonObj.put("id",this.getId());
            jsonObj.put("remark",this.getRemark());

        } catch (Exception e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    * 覆盖父类的 MessageContent(byte[] data) 构造方法，该方法将对收到的消息进行解析，
    * 先由 byte 转成 json 字符串，再将 json 中内容取出赋值给消息属性。
    * */
    public RedPackageMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("sendRedType"))
                setSendRedType(jsonObj.optString("sendRedType"));

            if (jsonObj.has("sendName"))
                setSendName(jsonObj.optString("sendName"));

            if (jsonObj.has("id"))
                setId(jsonObj.optString("id"));

            if (jsonObj.has("remark"))
                setRemark(jsonObj.optString("remark"));

        } catch (Exception e) {
            Log.d("JSONException", e.getMessage());
        }
    }

    //给消息赋值。
    public RedPackageMessage(Parcel in) {

        setSendRedType(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        //这里可继续增加你消息的属性
        setSendName(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setId(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setRemark(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<RedPackageMessage> CREATOR = new Creator<RedPackageMessage>() {

        @Override
        public RedPackageMessage createFromParcel(Parcel source) {
            return new RedPackageMessage(source);
        }

        @Override
        public RedPackageMessage[] newArray(int size) {
            return new RedPackageMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 将类的数据写入外部提供的 Parcel 中。
     * @param dest  对象被写入的 Parcel。
     * @param flags 对象如何被写入的附加标志。
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, getSendRedType());
        ParcelUtils.writeToParcel(dest, getSendName());
        ParcelUtils.writeToParcel(dest, getId());
        ParcelUtils.writeToParcel(dest, getRemark());
    }

    public String getSendRedType() {
        return sendRedType;
    }

    public void setSendRedType(String sendRedType) {
        this.sendRedType = sendRedType;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
