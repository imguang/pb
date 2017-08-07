package com.example.demo;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.UUID;

/**
 * Created by 书生 on 2017/8/7.
 */
public class PbTest {

    /**
     * 创建群聊序列化
     */
    public static byte[] buildGroupCreatePacket() {
        //创建body -- 群组创建
        IMPacket.GroupCreate groupCreate = IMPacket.GroupCreate.newBuilder()
                .setCanReadHistory(true).setDesc("公告公告")
                .setName("群组名称").setIcon(UUID.randomUUID().toString())
                .setIsPublic(true).setPersistent(IMPacket.GroupCreate.GroupType.GROUP)
                .build();


        //创建packet
        IMPacket.Packet packet = IMPacket.Packet.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setFrom("from@yealink.com")
                .setTo("to@yealink.com")
                .setType(IMPacket.Packet.PacketType.REQ)
                .setCmd(IMPacket.CmdID.GROUP_CREATE)
                .setBody(Any.pack(groupCreate)).build();

        System.out.println("=====最终对象=====");
        System.out.println(packet.toString());
        System.out.println("===========ComplexObject Byte==========");
        for (byte b : packet.toByteArray()) {
            System.out.print(b);
        }
        System.out.println();
        System.out.println(packet.toByteString());
        System.out.println("================================");

        return packet.toByteArray();
    }

    /**
     * 创建点对点聊天序列化
     */
    public static byte[] buildSingleChat() {
        //创建body -- 群聊
        IMPacket.SingleChat singleChat = IMPacket.SingleChat.newBuilder()
                .setMessage("你好！")
                .setType(IMPacket.SingleChat.ChatType.NORMAL)
                .build();

        //创建packet
        IMPacket.Packet packet = IMPacket.Packet.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setFrom("from@yealink.com")
                .setTo("to@yealink.com")
                .setType(IMPacket.Packet.PacketType.NOTIFY)
                .setCmd(IMPacket.CmdID.SINGLE_CHAT)
                .setBody(Any.pack(singleChat)).build();

        System.out.println("=====最终对象=====");
        System.out.println(packet.toString());
        System.out.println("===========ComplexObject Byte==========");
        for (byte b : packet.toByteArray()) {
            System.out.print(b);
        }
        System.out.println();
        System.out.println(packet.toByteString());
        System.out.println("================================");

        return packet.toByteArray();
    }

    /**
     * 反序列化
     */
    public static void  deserialize(byte[] bytes) throws InvalidProtocolBufferException {

        //packet 反序列化
        IMPacket.Packet packet = IMPacket.Packet.parseFrom(bytes);

        //根据cmd得到相应的对象，在特定handler中只需要实例化特定对象
        switch (packet.getCmd()) {
            //群组创建
            case GROUP_CREATE:
                IMPacket.GroupCreate groupCreate = packet.getBody().unpack(IMPacket.GroupCreate.class);
                System.out.println("====群创建====");
                System.out.println(groupCreate.toString());
                System.out.println("========");
                break;
            //单聊
            case SINGLE_CHAT:
                IMPacket.SingleChat singleChat = packet.getBody().unpack(IMPacket.SingleChat.class);
                System.out.println("====点对点聊天====");
                System.out.println(singleChat.toString());
                System.out.println("========");
                break;
            default:
                //log
                break;
        }

    }


    public static void main(String[] args) throws InvalidProtocolBufferException {
        deserialize(buildGroupCreatePacket());

        System.out.println("==========以下为单点聊天===========");

        deserialize(buildSingleChat());
    }

}
