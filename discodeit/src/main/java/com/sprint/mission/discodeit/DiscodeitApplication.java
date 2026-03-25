package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.service.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.service.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageResponse;
import com.sprint.mission.discodeit.service.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.service.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.service.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.service.dto.user.UserResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        System.out.println("----- 유저 서비스 테스트 -----");
        UserResponse user = testUserService(userService);

        System.out.println("\n----- 채널 서비스 테스트 -----");
        ChannelResponse channel = testChannelService(channelService, user.id());

        System.out.println("\n----- 메시지 서비스 테스트 -----");
        testMessageService(messageService, user.id(), channel.id());
    }

    private static UserResponse testUserService(UserService userService) {
        UserResponse user1 = userService.create(CreateUserRequest.builder()
                .username("hong")
                .email("hong@example.com")
                .password("1234")
                .build());
        UserResponse user2 = userService.create(CreateUserRequest.builder()
                .username("kim")
                .email("kim@example.com")
                .password("1234")
                .build());
        System.out.println("등록 " + user1.username() + ", " + user2.username());

        UUID userId = user1.id();
        UserResponse foundUser = userService.find(userId);
        System.out.println("조회-단건 " + foundUser.id() + " / " + foundUser.username());
        System.out.println("조회-다건 count=" + userService.findAll().size());

        userService.update(UpdateUserRequest.builder()
                .userId(userId)
                .username("hong-updated")
                .email("hong-updated@example.com")
                .password("5678")
                .build());
        System.out.println("수정 id=" + userId);

        UserResponse updated = userService.find(userId);
        System.out.println("수정 데이터 조회 " + updated.username());

        userService.delete(userId);
        System.out.println("삭제 id=" + userId);

        verifyDeletedUser(userService, userId);
        return user2;
    }

    private static void verifyDeletedUser(UserService userService, UUID userId) {
        try {
            userService.find(userId);
            System.out.println("삭제 실패: 유저가 아직 존재해요.");
        } catch (DiscodeitException e) {
            System.out.println("삭제 성공: " + e.getMessage());
        }
    }

    private static ChannelResponse testChannelService(ChannelService channelService, UUID viewerUserId) {
        ChannelResponse channel1 = channelService.createPublicChannel(new CreatePublicChannelRequest("일반", null));
        ChannelResponse channel2 = channelService.createPublicChannel(new CreatePublicChannelRequest("공지", null));
        System.out.println("등록 " + channel1.name() + ", " + channel2.name());

        UUID channelId = channel1.id();
        ChannelResponse foundChannel = channelService.find(channelId);
        System.out.println("조회-단건 " + foundChannel.id() + " / " + foundChannel.name());
        System.out.println("조회-다건 count=" + channelService.findAllByUserId(viewerUserId).size());

        channelService.update(new UpdateChannelRequest(channelId, "일반(수정)", "수정 설명"));
        System.out.println("수정 id=" + channelId);

        ChannelResponse updated = channelService.find(channelId);
        System.out.println("수정 데이터 조회 " + updated.name());

        channelService.delete(channelId);
        System.out.println("삭제 id=" + channelId);

        verifyDeletedChannel(channelService, channelId);
        return channel2;
    }

    private static void verifyDeletedChannel(ChannelService channelService, UUID channelId) {
        try {
            channelService.find(channelId);
            System.out.println("삭제 실패: 채널이 아직 존재해요.");
        } catch (DiscodeitException e) {
            System.out.println("삭제 성공: " + e.getMessage());
        }
    }

    private static void testMessageService(
            MessageService messageService,
            UUID authorId,
            UUID channelId
    ) {
        MessageResponse message1 = messageService.create(new CreateMessageRequest(authorId, channelId, "안녕하세요", null));
        MessageResponse message2 = messageService.create(new CreateMessageRequest(authorId, channelId, "반갑습니다", null));
        System.out.println("등록 " + message1.content() + ", " + message2.content());

        UUID messageId = message1.id();
        MessageResponse found = messageService.find(messageId);
        System.out.println("조회-단건 " + found.id() + " / " + found.content());
        List<MessageResponse> allMessages = messageService.findAllByChannelId(channelId);
        System.out.println("조회-다건 count=" + allMessages.size());

        messageService.update(new UpdateMessageRequest(messageId, "안녕하세요(수정)"));
        System.out.println("수정 id=" + messageId);

        MessageResponse updated = messageService.find(messageId);
        System.out.println("수정 데이터 조회 " + updated.content());

        messageService.delete(messageId);
        System.out.println("삭제 id=" + messageId);

        verifyDeletedMessage(messageService, messageId);
    }

    private static void verifyDeletedMessage(MessageService messageService, UUID messageId) {
        try {
            messageService.find(messageId);
            System.out.println("삭제 실패: 메시지가 아직 존재해요.");
        } catch (DiscodeitException e) {
            System.out.println("삭제 성공: " + e.getMessage());
        }
    }
}
