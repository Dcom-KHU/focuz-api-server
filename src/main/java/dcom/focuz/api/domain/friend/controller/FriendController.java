package dcom.focuz.api.domain.friend.controller;
import dcom.focuz.api.domain.friend.service.FriendService;
import dcom.focuz.api.domain.user.dto.UserResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"Friend Controller"})
@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendservice;

    //UserController
    //id로 친구 추가할 유저 찾기(getUserById)

    //내가 친구 요청 하기- postmapping - 서비스 O
    //나의 친구 요청 리스트 - getmapping - 서비스 O
    //나에게 온 친구 요청 수락 - postmapping? putmapping(id필요)? (REQUEST->FRIEND) <- 생각 필요(박민재씨가 postmapping 하라 함)
//    -----친구 성립 -----
    //나의 친구 목록 - getmapping
    //나의 친구 삭제 - deletemapping(id필요)
    // -----차단 친구 -----
    //내가 친구 차단 - postmapping
    //내가 차단한 친구 목록 - getmapping

    //요청 취소 및 차단 취소

    //delete 빼고는 모두 userResponseDto.simple 타입?<-이건 생각해서 알아서 근데 그냥 다 simple일듯
    //delete mapping 주소는?
    /*어차피 유저를 지우는 요청에서 getCurrentUser() 하면서 요청하는 유저의 정보를 가져 오니까.
    그냥 targetUserId만 받으면 될듯?
    그래서 friendRepository.findByUserAndTargetUser(user, targetUser);
    friendRepository.findByUserAndTargetUser(targetUser, user);
    이렇게 가져와서 둘다 friendRepository.delete(data) 해버리면 돼*/
    //친구 요청 취소는? 친구 차단 취소는? <-차단취소 요청취소용 delete 따로 만들어야 할듯?

    @ApiOperation("현재가 타겟 유저에게 친구요청을 보냅니다.")
    @PostMapping(value="/request/{targetUserId}")
    @ResponseStatus(value=HttpStatus.OK)
    public ResponseEntity<UserResponseDto.Simple> friendRequest(@ApiParam(value="타겟 유저 ID", required = true) @PathVariable @Valid Integer targetUserId){
        return ResponseEntity.ok(friendservice.friendRequest(targetUserId));
    }

    @ApiOperation("현재 유저에게 친구요청을 한 유저 리스트를 반환합니다.")
    @GetMapping(value="/request/list")
    @ResponseStatus(value=HttpStatus.OK)
    public ResponseEntity<List<UserResponseDto.Simple>> getFriendRequestList(){
        return ResponseEntity.ok(friendservice.getFriendRequestList());
    }

    @ApiOperation("현재 유저에게 온 친구 요청을 수락합니다.")
    @PostMapping(value="/request/accept/{targetUserId}")
    @ResponseStatus(value=HttpStatus.OK)
    public ResponseEntity<UserResponseDto.Simple> AcceptFriendRequest(@ApiParam(value="타겟 유저 ID", required = true) @PathVariable @Valid Integer targetUserId){
        return ResponseEntity.ok(friendservice.acceptFriendRequest(targetUserId));
    }

    @ApiOperation("현재 유저의 친구 목록을 반환 합니다.")
    @GetMapping(value = "/list")
    @ResponseStatus(value=HttpStatus.OK)
    public ResponseEntity<List<UserResponseDto.Simple>> getFriendList(){
        return ResponseEntity.ok(friendservice.getFriendList());
    }

    @ApiOperation("타겟 유저를 현재 유저의 친구 목록에서 삭제합니다.")
    @DeleteMapping("/delete/{targetUserId}")//근데 타겟유저랑 유저 둘다에 있는데 (friend pk도 두 개) 무슨 id를 삭제 하나?
    @ResponseStatus(value=HttpStatus.ACCEPTED)
    public ResponseEntity<Void> deleteFriend(@ApiParam(value="타겟 유저 ID", required = true) @PathVariable @Valid Integer targetUserId){
        friendservice.deleteFriend(targetUserId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ApiOperation("타겟 유저를 차단합니다.")
    @PostMapping("/blocked/{targetUserId}")
    @ResponseStatus(value=HttpStatus.OK)
    public ResponseEntity<UserResponseDto.Simple> BlockFriend(@ApiParam(value="타겟 유저 ID", required = true) @PathVariable @Valid Integer targetUserId){
        return ResponseEntity.ok(friendservice.blockFriend(targetUserId));
    }

    @ApiOperation("현재 유저가 차단한 친구들의 목록을 반환 합니다.")
    @GetMapping(value = "/blocked/list")
    @ResponseStatus(value=HttpStatus.OK)
    public ResponseEntity<List<UserResponseDto.Simple>> getBlockedFriendList(){
        return ResponseEntity.ok(friendservice.getBlockedFriendList());
    }

    @ApiOperation("타겟유저에게 한 친구요청을 취소합니다.")
    @DeleteMapping("/request/delete/{targetUserId}")
    @ResponseStatus(value=HttpStatus.ACCEPTED)
    public ResponseEntity<Void> deleteRequest(@ApiParam(value="타겟 유저 ID", required = true) @PathVariable @Valid Integer targetUserId){
        friendservice.deleteRequest(targetUserId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ApiOperation("타겟 유저 차단을 취소합니다.")
    @DeleteMapping("/block/delete/{targetUserId}")
    @ResponseStatus(value=HttpStatus.ACCEPTED)
    public ResponseEntity<Void> deleteBlock(@ApiParam(value="타겟 유저 ID", required = true) @PathVariable @Valid Integer targetUserId){
        friendservice.deleteBlock(targetUserId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
