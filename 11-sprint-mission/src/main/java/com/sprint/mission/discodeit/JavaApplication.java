package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import java.util.UUID;

public class JavaApplication {

    public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService(userService, channelService);

        System.out.println("----- 유저 서비스 테스트 -----");
        testUserService(userService);

        System.out.println("\n----- 채널 서비스 테스트 -----");
        testChannelService(channelService);

        System.out.println("\n----- 메시지 서비스 테스트 -----");
        testMessageService(userService, channelService, messageService);
    }

    private static void testUserService(UserService userService) {
        User user1 = userService.createUser("홍길동");
        User user2 = userService.createUser("김철수");
        System.out.println("등록 " + user1.getUsername() + ", " + user2.getUsername());

        UUID userId = user1.getId();
        User foundUser = userService.findUser(userId);
        System.out.println("조회-단건 " + foundUser.getId() + " / " + foundUser.getUsername());
        System.out.println("조회-다건 count=" + userService.getAllUsers().size());

        userService.updateName(userId, "홍길동(수정)");
        System.out.println("수정 id=" + userId);

        User updated = userService.findUser(userId);
        System.out.println("수정 데이터 조회 " + updated.getUsername());

        userService.delete(userId);
        System.out.println("삭제 id=" + userId);

        verifyDeletedUser(userService, userId);
    }

    private static void verifyDeletedUser(UserService userService, UUID userId) {
        try {
            userService.findUser(userId);
            System.out.println("삭제 실패: 유저가 아직 존재해요.");
        } catch (IllegalArgumentException e) {
            System.out.println("삭제 성공: " + e.getMessage());
        }
    }

    private static void testChannelService(ChannelService channelService) {
        Channel channel1 = channelService.createChannel("일반");
        Channel channel2 = channelService.createChannel("공지");
        System.out.println("등록 " + channel1.getChannelName() + ", " + channel2.getChannelName());

        UUID channelId = channel1.getId();
        Channel foundChannel = channelService.findChannel(channelId);
        System.out.println("조회-단건 " + foundChannel.getId() + " / " + foundChannel.getChannelName());
        System.out.println("조회-다건 count=" + channelService.getAllChannels().size());

        channelService.updateName(channelId, "일반(수정)");
        System.out.println("수정 id=" + channelId);

        Channel updated = channelService.findChannel(channelId);
        System.out.println("수정 데이터 조회 " + updated.getChannelName());

        channelService.delete(channelId);
        System.out.println("삭제 id=" + channelId);

        verifyDeletedChannel(channelService, channelId);
    }

    private static void verifyDeletedChannel(ChannelService channelService, UUID channelId) {
        try {
            channelService.findChannel(channelId);
            System.out.println("삭제 실패: 채널이 아직 존재해요.");
        } catch (IllegalArgumentException e) {
            System.out.println("삭제 성공: " + e.getMessage());
        }
    }

    private static void testMessageService(
            UserService userService,
            ChannelService channelService,
            MessageService messageService
    ) {
        User author = userService.createUser("작성자");
        Channel channel = channelService.createChannel("채팅");

        Message message1 = messageService.createMessage(author.getId(), channel.getId(), "안녕하세요");
        Message message2 = messageService.createMessage(author.getId(), channel.getId(), "반갑습니다");
        System.out.println("등록 " + message1.getContent() + ", " + message2.getContent());

        UUID messageId = message1.getId();
        Message found = messageService.findMessage(messageId);
        System.out.println("조회-단건 " + found.getId() + " / " + found.getContent());
        System.out.println("조회-다건 count=" + messageService.getAllMessages().size());

        messageService.updateContent(messageId, "안녕하세요(수정)");
        System.out.println("수정 id=" + messageId);

        Message updated = messageService.findMessage(messageId);
        System.out.println("수정 데이터 조회 " + updated.getContent());

        messageService.delete(messageId);
        System.out.println("삭제 id=" + messageId);

        verifyDeletedMessage(messageService, messageId);
    }

    private static void verifyDeletedMessage(MessageService messageService, UUID messageId) {
        try {
            messageService.findMessage(messageId);
            System.out.println("삭제 실패: 메시지가 아직 존재해요.");
        } catch (IllegalArgumentException e) {
            System.out.println("삭제 성공: " + e.getMessage());
        }
    }
}
