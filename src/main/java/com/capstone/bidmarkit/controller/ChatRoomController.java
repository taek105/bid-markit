package com.capstone.bidmarkit.controller;

import com.capstone.bidmarkit.domain.ChatRoom;
import com.capstone.bidmarkit.dto.AddChatRoomRequest;
import com.capstone.bidmarkit.dto.ChatRoomDetailResponse;
import com.capstone.bidmarkit.dto.ChatRoomListResponse;
import com.capstone.bidmarkit.dto.UpdateCheckResponse;
import com.capstone.bidmarkit.service.ChatRoomService;
import com.capstone.bidmarkit.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final TokenService tokenService;

    @PostMapping("/chatRooms")
    public ResponseEntity<ChatRoom> createRoom(@RequestHeader(name="Authorization") String token, @RequestBody AddChatRoomRequest request) {
        return ResponseEntity.ok().body(chatRoomService.save(tokenService.getMemberId(token.substring(7)), request));
    }

    @GetMapping("/chatRooms")
    public ResponseEntity<Page<ChatRoomListResponse>> getChatRoomList(@RequestHeader(name="Authorization") String token , @RequestParam int pageNum, @RequestParam int size) {
        return ResponseEntity.ok().body(chatRoomService.findAllRoomByMemberId(tokenService.getMemberId(token.substring(7)), PageRequest.of(pageNum, size)));
    }

    @GetMapping("/chatRooms/{roomId}")
    public ResponseEntity<ChatRoomDetailResponse> loadMessage(@PathVariable int roomId) {
        ChatRoomDetailResponse chatDetails = chatRoomService.findChatDetailsByRoomId(roomId);
        return ResponseEntity.ok(chatDetails);
    }

    @PutMapping("/chatRooms/{roomId}/check/{checkType}") // 상품 상태변경
    public ResponseEntity<UpdateCheckResponse> customerCheck(@RequestHeader(name="Authorization") String token, @PathVariable int roomId, @PathVariable int checkType) {
        return ResponseEntity.ok(chatRoomService.updateCheck(tokenService.getMemberId(token.substring(7)), roomId, (byte) checkType));
    }
}
