package com.ccx.demo.business.user.web;
//
//import com.alibaba.fastjson.JSON;
//import com.ccx.demo.business.user.entity.TabUser;
//import com.ccx.demo.business.user.entity.TabUserLogin;
//import com.ccx.demo.business.user.service.UserLoginService;
//import com.ccx.demo.business.user.service.UserService;
//import com.ccx.demo.business.user.vo.TabUserVO;
//import com.support.mvc.entity.base.Pager;
//import com.support.mvc.entity.base.Result;
//import io.swagger.annotations.ApiParam;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
///**
// * 请求操作响应:用户
// *
// * @author 谢长春 on 2017/11/13.
// */
//@RequestMapping("/user/{version}")
//@Controller
//@Slf4j
//@RequiredArgsConstructor
//public class UserController implements IAuthController<Long> {
//
//    private final UserService service;
//
//    private final UserLoginService userLoginService;
//
//    @GetMapping("/current")
//    @ResponseBody
//    public Result<?> current(@AuthenticationPrincipal final TabUser user) {
//        return new Result<TabUserVO>()
//                .execute(result -> result .setSuccess(user.toTabUserVO()) );
//    }
//
//    @PatchMapping("/nickname")
//    @ResponseBody
//    public Result<?> updateNickname(@AuthenticationPrincipal final TabUser user,
////                                    @RequestBody(required = false) final String body) {
//        return new Result<>()
//                .execute(result -> result
//                        .call(() -> service.updateNickname(user.getId(), JSON.parseObject(body).getString("nickname"), user.getId()))
//                );
//    }
//
//    @GetMapping("/log/{number}/{size}")
//    @ResponseBody
//    public Result<?> loginLog(
//            @AuthenticationPrincipal final TabUser user,
////            @ApiParam(required = true, value = "页码", example = "1") @PathVariable final int number,
//            @ApiParam(required = true, value = "每页条数", example = "1") @PathVariable final int size,
//            @RequestParam(required = false, defaultValue = "{}") final String json) {
//        return new Result<>()
//                .execute(result -> result
//                        .setSuccess(
//                                userLoginService.findPage(
//                                        JSON.parseObject(json, TabUserLogin.class),
//                                        Pager.builder().number(number).size(size).build()
//                                )
//                        )
//                );
//    }
//
//    @Override
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'UC_save')")
//    @PostMapping
//    @ResponseBody
//    public Result<?> save(@AuthenticationPrincipal final TabUser user,
////                          @RequestBody(required = false) final String body) {
//        return new Result<>()
//                .execute(result -> result
//                        .setSuccess(service.save(
//                                JSON.parseObject(body, TabUser.class), user.getId()
//                        ))
//                );
//    }
//
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'UC_update')")
//    @PutMapping("/{id}")
//    @ResponseBody
//    @Override
//    public Result<?> update(@AuthenticationPrincipal final TabUser user,
////
//@ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
//                            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
//                            @RequestBody(required = false) final String body) {
//        return new Result<>()
//                .execute(result -> result
//                        .call(() -> service.update(
//                                id,
//                                user.getId(),
//                                JSON.parseObject(body, TabUser.class)
//                        ))
//                );
//    }
//
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'UC_findByUid')")
//    @GetMapping("/{id}/{uid}")
//    @ResponseBody
//    @Override
//    public Result<?> findByUid(@AuthenticationPrincipal final TabUser user,
////
//@ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
//                               @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
//        return new Result<TabUser>()
//                .execute(result -> result
//                        .versionAssert(version, false) // 弱校验版本号
//                        .setSuccess(service.findByUid(id, uid).orElse(null))
//                );
//    }
//
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_User')")
//    @GetMapping("/page/{number}/{size}")
//    @ResponseBody
//    @Override
//    public Result<?> page(
//            @AuthenticationPrincipal final TabUser user,
////            @ApiParam(required = true, value = "页码", example = "1") @PathVariable final int number,
//            @ApiParam(required = true, value = "每页条数", example = "1") @PathVariable final int size,
//            @RequestParam(required = false, defaultValue = "{}") final String json
//    ) {
//        return new Result<TabUser>()
//                .execute(result -> result
//                        .versionAssert(version, false) // 弱校验版本号
//                        .setSuccess(service.findPage(
//                                JSON.parseObject(json, TabUser.class),
//                                Pager.builder().number(number).size(size).build()
//                        ))
//                );
//    }
//
//}