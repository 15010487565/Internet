package com.xcd.www.internet.rong;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

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
@MessageTag(value = "RCD:RedPacketMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class RedPackageMessage extends MessageContent {

    private String extra;
    //自定义的属性
    private String headUrl;//发送人头像url
    private String redPacketId;//红包id
    private String content;//红包描述
    private String amout;//红包金额

    private String coin;//红包种类
    private String total;//红包个数
    private String sendName;//红包发送者名字
    private String sendID;//红包发送者id

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
        Log.e("TAG_JSONException", "encode");
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("headUrl", this.getHeadUrl());
            jsonObj.put("redPacketId",this.getRedPacketId());
            jsonObj.put("content",this.getContent());
            jsonObj.put("amout", this.getAmout());
            jsonObj.put("coin", this.getCoin());
            jsonObj.put("sendID", this.getSendID());
            jsonObj.put("sendName",this.getSendName());
            jsonObj.put("total", this.getTotal());

            jsonObj.put("extra", jsonObj.toString());
            Log.e("TAG_JSONException===", jsonObj.toString());
        } catch (Exception e) {
            Log.e("TAG_JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG_JSONException", e.getMessage());
            return null;
        }
    }

    /*
    * 覆盖父类的 MessageContent(byte[] data) 构造方法，该方法将对收到的消息进行解析，
    * 先由 byte 转成 json 字符串，再将 json 中内容取出赋值给消息属性。
    * */
    public RedPackageMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
            Log.e("TAG_JSONException", jsonStr);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("headUrl"))
                setHeadUrl(jsonObj.optString("headUrl"));

            if (jsonObj.has("redPacketId"))
                setRedPacketId(jsonObj.optString("redPacketId"));

            if (jsonObj.has("content"))
                setContent(jsonObj.optString("content"));

            if (jsonObj.has("amout"))
                setAmout(jsonObj.optString("amout"));

            if (jsonObj.has("coin"))
                setCoin(jsonObj.optString("coin"));

            if (jsonObj.has("sendName"))
                setSendName(jsonObj.optString("sendName"));

            if (jsonObj.has("sendID"))
                setSendID(jsonObj.optString("sendID"));

            if (jsonObj.has("total"))
                setTotal(jsonObj.optString("total"));

            if (jsonObj.has("extra"))
                setExtra(jsonObj.optString("extra"));


        } catch (Exception e) {
            Log.d("JSONException", e.getMessage());
        }
    }

    //给消息赋值。
    public RedPackageMessage(Parcel in) {
        Log.e("TAG_JSONException", "RedPackageMessage");
        //自定义的属性

        setHeadUrl(ParcelUtils.readFromParcel(in));
        setRedPacketId(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性

        setContent(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setAmout(ParcelUtils.readFromParcel(in));
        setCoin(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        //这里可继续增加你消息的属性
        setSendName(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setSendID(ParcelUtils.readFromParcel(in));
        setTotal(ParcelUtils.readFromParcel(in));
        setExtra(ParcelUtils.readFromParcel(in));
        setUserInfo(ParcelUtils.readFromParcel(in, UserInfo.class));
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
        Log.e("TAG_JSONException", "writeToParcel");
        ParcelUtils.writeToParcel(dest, getHeadUrl());
        ParcelUtils.writeToParcel(dest, getRedPacketId());
        ParcelUtils.writeToParcel(dest, getContent());
        ParcelUtils.writeToParcel(dest, getAmout());
        ParcelUtils.writeToParcel(dest, getCoin());
        ParcelUtils.writeToParcel(dest, getSendName());
        ParcelUtils.writeToParcel(dest, getSendID()
        );
        ParcelUtils.writeToParcel(dest, getTotal());
        ParcelUtils.writeToParcel(dest, getExtra());


        ParcelUtils.writeToParcel(dest, getUserInfo());
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSendID() {
        return sendID;
    }

    public void setSendID(String sendID) {
        this.sendID = sendID;
    }

    public String getAmout() {
        return amout;
    }

    public void setAmout(String amout) {
        this.amout = amout;
    }

    public String getExtra() {
        return extra;
    }

    @Override
    public String toString() {
        return "RedPackageMessage{" +
                "extra='" + extra + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", redPacketId='" + redPacketId + '\'' +
                ", content='" + content + '\'' +
                ", amout='" + amout + '\'' +
                ", coin='" + coin + '\'' +
                ", total='" + total + '\'' +
                ", sendName='" + sendName + '\'' +
                ", sendID='" + sendID + '\'' +
                '}';
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
