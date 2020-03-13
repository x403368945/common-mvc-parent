package com.ccx.demo.business.example.web;

import com.ccx.demo.business.example.entity.TabValid;
import com.ccx.demo.business.example.service.ValidService;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.web.IAuthController;
import com.google.common.base.Strings;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Result;
import com.utils.util.Dates;
import com.utils.util.Util;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * 请求操作响应：案例
 *
 * @author 谢长春 2018-10-5
 */
@RequestMapping("/{version}/valid")
@Controller
@Slf4j
@RequiredArgsConstructor
public class ValidController implements IAuthController<Long, TabValid> {

    private final ValidService service;

    @GetMapping("/hasNull")
    @ResponseBody
    public Result<String> hasNull(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.hasNull(null)).addExtras("@Null", "成功");
            if (1 == version) result.setSuccess(service.hasNull("")).addExtras("@Null", "失败");
        });
    }

    @GetMapping("/notNull")
    @ResponseBody
    public Result<String> notNull(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.notNull("")).addExtras("@NotNull", "成功");
            if (1 == version) result.setSuccess(service.notNull(null)).addExtras("@NotNull", "失败");
        });
    }

    @GetMapping("/notBlank")
    @ResponseBody
    public Result<String> notBlank(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.notBlank(null)).addExtras("@NotBlank", "失败");
            if (1 == version) result.setSuccess(service.notBlank("text")).addExtras("@NotBlank", "成功");
            if (2 == version) result.setSuccess(service.notBlank("")).addExtras("@NotBlank", "失败");
        });
    }

    @GetMapping("/size")
    @ResponseBody
    public Result<String> size(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.size("text")).addExtras("@Size", "成功");
            if (1 == version) result.setSuccess(service.size(null)).addExtras("@Size", "成功");
            if (2 == version) result.setSuccess(service.size("")).addExtras("@Size", "失败");
            if (3 == version) result.setSuccess(service.size(Strings.repeat("-", 11))).addExtras("@Size", "失败");
            if (4 == version) result.setSuccess(service.size(Strings.repeat("测试", 5))).addExtras("@Size", "成功");
        });
    }

    @GetMapping("/pattern")
    @ResponseBody
    public Result<String> pattern(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.pattern("text_0")).addExtras("@Pattern", "成功");
            if (1 == version) result.setSuccess(service.pattern(null)).addExtras("@Pattern", "成功");
            if (2 == version) result.setSuccess(service.pattern("")).addExtras("@Pattern", "失败");
            if (3 == version) result.setSuccess(service.pattern("测试")).addExtras("@Pattern", "失败");
        });
    }

    @GetMapping("/notEmpty")
    @ResponseBody
    public Result<String> notEmpty(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version)
                result.setSuccess(service.notEmpty(Collections.singletonList(Util.uuid32()))).addExtras("@NotEmpty", "成功");
            if (1 == version) result.setSuccess(service.notEmpty(null)).addExtras("@NotEmpty", "成功");
            if (2 == version) result.setSuccess(service.notEmpty(Collections.emptyList())).addExtras("@NotEmpty", "失败");
            if (3 == version)
                result.setSuccess(service.notEmpty(Collections.singletonList(""))).addExtras("@NotEmpty", "失败");
        });
    }

    @GetMapping("/min")
    @ResponseBody
    public Result<String> min(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.min(null)).addExtras("@Min", "成功");
            if (1 == version) result.setSuccess(service.min(0)).addExtras("@Min", "失败");
        });
    }

    @GetMapping("/max")
    @ResponseBody
    public Result<String> max(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.max(null)).addExtras("@Max", "成功");
            if (1 == version) result.setSuccess(service.max(11)).addExtras("@Max", "成功");
        });
    }

    @GetMapping("/decimalMin")
    @ResponseBody
    public Result<String> decimalMin(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.decimalMin(null)).addExtras("@DecimalMin", "成功");
            if (1 == version) result.setSuccess(service.decimalMin(BigDecimal.ZERO)).addExtras("@DecimalMin", "失败");
        });
    }

    @GetMapping("/decimalMax")
    @ResponseBody
    public Result<String> decimalMax(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.decimalMax(null)).addExtras("@DecimalMax", "成功");
            if (1 == version)
                result.setSuccess(service.decimalMax(BigDecimal.valueOf(11))).addExtras("@DecimalMax", "失败");
        });
    }

    @GetMapping("/digits")
    @ResponseBody
    public Result<String> digits(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.digits(null)).addExtras("@Digits", "成功");
            if (1 == version) result.setSuccess(service.digits(new BigDecimal("10.222"))).addExtras("@Digits", "失败");
        });
    }

    @GetMapping("/negative")
    @ResponseBody
    public Result<String> negative(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.negative(null)).addExtras("@Negative", "成功");
            if (1 == version) result.setSuccess(service.negative(BigDecimal.ONE)).addExtras("@Negative", "失败");
        });
    }

    @GetMapping("/negativeOrZero")
    @ResponseBody
    public Result<String> negativeOrZero(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.negativeOrZero(null)).addExtras("@NegativeOrZero", "成功");
            if (1 == version)
                result.setSuccess(service.negativeOrZero(BigDecimal.TEN)).addExtras("@NegativeOrZero", "失败");
        });
    }

    @GetMapping("/positive")
    @ResponseBody
    public Result<String> positive(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.positive(null)).addExtras("@Positive", "成功");
            if (1 == version) result.setSuccess(service.positive(BigDecimal.ZERO)).addExtras("@Positive", "失败");
        });
    }

    @GetMapping("/positiveOrZero")
    @ResponseBody
    public Result<String> positiveOrZero(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.positiveOrZero(null)).addExtras("@PositiveOrZero", "成功");
            if (1 == version)
                result.setSuccess(service.positiveOrZero(BigDecimal.valueOf(-1))).addExtras("@PositiveOrZero", "失败");
        });
    }

    @GetMapping("/assertTrue")
    @ResponseBody
    public Result<String> assertTrue(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.assertTrue(null)).addExtras("@AssertTrue", "成功");
            if (1 == version) result.setSuccess(service.assertTrue(false)).addExtras("@AssertTrue", "失败");
        });
    }

    @GetMapping("/assertFalse")
    @ResponseBody
    public Result<String> assertFalse(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.assertFalse(null)).addExtras("@AssertFalse", "成功");
            if (1 == version) result.setSuccess(service.assertFalse(true)).addExtras("@AssertFalse", "失败");
        });
    }

    @GetMapping("/email")
    @ResponseBody
    public Result<String> email(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.email(null)).addExtras("@Email", "成功");
            if (1 == version) result.setSuccess(service.email("x@126.com")).addExtras("@Email", "成功");
            if (2 == version) result.setSuccess(service.email("126.com")).addExtras("@Email", "失败");
        });
    }

    @GetMapping("/past")
    @ResponseBody
    public Result<String> past(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.past(null)).addExtras("@Past", "成功");
            if (1 == version)
                result.setSuccess(service.past(Dates.now().addDay(1).timestamp())).addExtras("@Past", "失败");
        });
    }

    @GetMapping("/future")
    @ResponseBody
    public Result<String> future(@PathVariable int version) {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.future(null)).addExtras("@Future", "成功");
            if (1 == version)
                result.setSuccess(service.future(Dates.now().addDay(-1).timestamp())).addExtras("@Future", "失败");
        });
    }

    @PostMapping
    @ResponseBody
    public Result<TabValid> save(
            @PathVariable int version,
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
            @RequestBody(required = false) final String body) {
        return new Result<TabValid>().execute(result -> {
            {
                if (0 == version)
                    result.setSuccess(service.save(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 15)).build(), user.getId())).addExtras("@", "成功失败");
                if (1 == version)
                    result.setSuccess(service.save(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 10).build(), user.getId())).addExtras("@", "成功失败");
                if (2 == version) result.setSuccess(service.save(null, null)).addExtras("@", "成功失败");
                if (3 == version)
                    result.setSuccess(service.save(TabValid.builder().build(), null)).addExtras("@", "成功失败");
                if (4 == version)
                    result.setSuccess(service.save(TabValid.builder().build(), user.getId())).addExtras("@", "成功失败");
                if (5 == version)
                    result.setSuccess(service.save(TabValid.builder().id(1L).build(), user.getId())).addExtras("@", "成功失败");
                if (6 == version)
                    result.setSuccess(service.save(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 16)).build(), user.getId())).addExtras("@", "成功失败");
                if (7 == version)
                    result.setSuccess(service.save(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).build(), user.getId())).addExtras("@", "成功失败");
                if (8 == version)
                    result.setSuccess(service.save(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).build(), 0L)).addExtras("@", "成功失败");
            }
            {
                if (9 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 15)).build()), user.getId())).addExtras("@", "成功失败");
                if (10 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 10).build()), user.getId())).addExtras("@", "成功失败");
                if (11 == version) result.setSuccess(service.saveAll(null, null)).addExtras("@", "成功失败");
                if (12 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(null), null)).addExtras("@", "成功失败");
                if (13 == version)
                    result.setSuccess(service.saveAll(Collections.emptyList(), null)).addExtras("@", "成功失败");
                if (14 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().build()), null)).addExtras("@", "成功失败");
                if (15 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().build()), user.getId())).addExtras("@", "成功失败");
                if (16 == version)
                    result.setSuccess(service.saveAll(null, user.getId())).addExtras("@", "成功失败");
                if (17 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().id(1L).build()), user.getId())).addExtras("@", "成功失败");
                if (18 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 16)).build()), user.getId())).addExtras("@", "成功失败");
                if (19 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).build()), user.getId())).addExtras("@", "成功失败");
            }
            {
                if (20 == version)
                    result.setSuccess(service.save(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 15)).build())).addExtras("@", "成功失败");
                if (21 == version)
                    result.setSuccess(service.save(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 10).build())).addExtras("@", "成功失败");
                if (22 == version) result.setSuccess(service.save(null)).addExtras("@", "成功失败");
                if (23 == version) result.setSuccess(service.save(TabValid.builder().build())).addExtras("@", "成功失败");
                if (24 == version)
                    result.setSuccess(service.save(TabValid.builder().id(1L).build())).addExtras("@", "成功失败");
                if (25 == version)
                    result.setSuccess(service.save(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 16)).build())).addExtras("@", "成功失败");
                if (26 == version)
                    result.setSuccess(service.save(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).build())).addExtras("@", "成功失败");
            }
            {
                if (27 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 15)).build()))).addExtras("@", "成功失败");
                if (28 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 10).build()))).addExtras("@", "成功失败");
                if (29 == version) result.setSuccess(service.saveAll(null)).addExtras("@", "成功失败");
                if (30 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(null))).addExtras("@", "成功失败");
                if (31 == version) result.setSuccess(service.saveAll(Collections.emptyList())).addExtras("@", "成功失败");
                if (32 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().build()))).addExtras("@", "成功失败");
                if (33 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().id(1L).build()))).addExtras("@", "成功失败");
                if (34 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 16)).build()))).addExtras("@", "成功失败");
                if (35 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).build()))).addExtras("@", "成功失败");
            }
        });
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Result<Void> update(
            @PathVariable int version,
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
            @RequestBody(required = false) final String body) {
        return new Result<Void>().execute(result -> {
            {
                if (0 == version)
                    service.update(1L, user.getId(), TabValid.builder().uid(Util.uuid32()).label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 15)).build());
                if (1 == version) service.update(null, null, null);
                if (2 == version) service.update(0L, 0L, TabValid.builder().build());
                if (3 == version) service.update(1L, user.getId(), TabValid.builder().build());
                if (4 == version) service.update(1L, user.getId(), TabValid.builder().uid("123").build());
                if (5 == version)
                    service.update(1L, user.getId(), TabValid.builder().uid("123").content(Strings.repeat("内容", 16)).build());
                if (6 == version)
                    service.update(1L, user.getId(), TabValid.builder().uid("123").label(Strings.repeat("测试", 6)).content(Strings.repeat("内容", 16)).build());
            }
            {
                if (7 == version)
                    service.update(1L, TabValid.builder().uid(Util.uuid32()).label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 15)).build());
                if (8 == version) service.update(null, null);
                if (9 == version) service.update(0L, TabValid.builder().build());
                if (10 == version) service.update(1L, TabValid.builder().build());
                if (11 == version) service.update(1L, TabValid.builder().uid("123").build());
                if (12 == version)
                    service.update(1L, TabValid.builder().uid("123").content(Strings.repeat("内容", 16)).build());
                if (13 == version)
                    service.update(1L, TabValid.builder().uid("123").label(Strings.repeat("测试", 6)).content(Strings.repeat("内容", 16)).build());
            }
        });
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Result<Void> deleteById(
            @PathVariable int version,
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id) {
        return new Result<Void>().call(() -> {
            {
                if (0 == version) service.deleteById(id, user.getId());
                if (1 == version) service.deleteById(null, null);
                if (2 == version) service.deleteById(0L, 0L);
            }
            {
                if (3 == version) service.deleteById(id);
                if (4 == version) service.deleteById(null);
                if (5 == version) service.deleteById(0L);
            }
        });
    }

    @DeleteMapping("/{id}/{uid}")
    @ResponseBody
    public Result<Void> deleteByUid(
            @PathVariable int version,
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
            @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
        return new Result<Void>().call(() -> {
            {
                if (0 == version) service.deleteByUid(id, Util.uuid32(), user.getId());
                if (1 == version) service.deleteByUid(null, null, null);
                if (2 == version) service.deleteByUid(0L, "", 0L);
                if (3 == version) service.deleteByUid(1L, "1", 1L);
            }
            {
                if (4 == version) service.deleteByUid(id, Util.uuid32());
                if (5 == version) service.deleteByUid(null, null);
                if (6 == version) service.deleteByUid(0L, "");
                if (7 == version) service.deleteByUid(1L, "1");
            }
        });
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public Result<Void> markDeleteById(
            @PathVariable int version,
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id) {
        return new Result<Void>().call(() -> {
            {
                if (0 == version) service.markDeleteById(id, user.getId());
                if (1 == version) service.markDeleteById(null, null);
                if (2 == version) service.markDeleteById(0L, 0L);
            }
            {
                if (3 == version) service.markDeleteById(id);
                if (4 == version) service.markDeleteById(null);
                if (5 == version) service.markDeleteById(0L);
            }
        });
    }

    @PatchMapping("/{id}/{uid}")
    @ResponseBody
    public Result<Void> markDeleteByUid(
            @PathVariable int version,
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
            @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
        return new Result<Void>().call(() -> {
            {
                if (0 == version) service.markDeleteByUid(id, Util.uuid32(), user.getId());
                if (1 == version) service.markDeleteByUid(null, null, null);
                if (2 == version) service.markDeleteByUid(0L, "", 0L);
                if (3 == version) service.markDeleteByUid(1L, "1", 1L);
            }
            {

                if (4 == version) service.markDeleteByUid(id, Util.uuid32());
                if (5 == version) service.markDeleteByUid(null, null);
                if (6 == version) service.markDeleteByUid(0L, "");
                if (7 == version) service.markDeleteByUid(1L, "1");
            }
        });
    }

    @PatchMapping
    @ResponseBody
    public Result<Void> markDelete(
            @PathVariable int version,
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            @RequestBody(required = false) final List<MarkDelete> body) {
        return new Result<Void>().call(() -> {
            {
                if (0 == version)
                    service.markDelete(Collections.singletonList(MarkDelete.builder().id(1L).uid(Util.uuid32()).build()), user.getId());
                if (1 == version) service.markDelete(null, null);
                if (2 == version) service.markDelete(Collections.emptyList(), 0L);
                if (3 == version) service.markDelete(Collections.singletonList(MarkDelete.builder().build()), 0L);
                if (4 == version)
                    service.markDelete(Collections.singletonList(MarkDelete.builder().id(0L).uid("1").build()), 0L);
            }
            {
                if (5 == version)
                    service.markDelete(Collections.singletonList(MarkDelete.builder().id(1L).uid(Util.uuid32()).build()));
                if (6 == version) service.markDelete(null);
                if (7 == version) service.markDelete(Collections.emptyList());
                if (8 == version) service.markDelete(Collections.singletonList(MarkDelete.builder().build()));
                if (9 == version)
                    service.markDelete(Collections.singletonList(MarkDelete.builder().id(0L).uid("1").build()));
            }
        });
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Result<TabValid> findById(
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            @PathVariable final int version,
            @PathVariable final Long id) {
        return new Result<TabValid>().execute(result -> {
            if (0 == version) result.setSuccess(service.findById(1L).orElse(null)).addExtras("@", "成功失败");
            if (1 == version) result.setSuccess(service.findById(null).orElse(null)).addExtras("@", "成功失败");
            if (2 == version) result.setSuccess(service.findById(0L).orElse(null)).addExtras("@", "成功失败");
        }).addExtras("@", "成功失败");
    }

    @GetMapping("/{id}/{uid}")
    @ResponseBody
    public Result<TabValid> findByUid(
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            @PathVariable final int version,
            @PathVariable final Long id,
            @PathVariable final String uid) {
        return new Result<TabValid>().execute(result -> {
            if (0 == version)
                result.setSuccess(service.findByUid(1L, Util.uuid32()).orElse(null)).addExtras("@", "成功失败");
            if (1 == version) result.setSuccess(service.findByUid(null, null).orElse(null)).addExtras("@", "成功失败");
            if (2 == version) result.setSuccess(service.findByUid(0L, "0").orElse(null)).addExtras("@", "成功失败");
        });
    }

    @GetMapping
    @ResponseBody
    public Result<TabValid> search(
            @PathVariable int version,
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            final TabValid condition) {
        return new Result<TabValid>().execute(result -> {
            if (0 == version) result.setSuccess(service.findList(condition)).addExtras("@", "成功失败");
            if (1 == version)
                result.setSuccess(service.findList(TabValid.builder().value((short) 10).build())).addExtras("@", "成功失败");
            if (2 == version) result.setSuccess(service.findList(null)).addExtras("@", "成功失败");
        });
    }

    @GetMapping("/page/{number}/{size}")
    @ResponseBody
    public Result<TabValid> page(
            @PathVariable int version,
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "页码", example = "1") @PathVariable final int number,
            @ApiParam(required = true, value = "每页条数", example = "1") @PathVariable final int size,
            final TabValid condition) {
        return new Result<TabValid>().execute(result -> {
            if (0 == version)
                result.setSuccess(service.findPage(condition, Pager.builder().number(number).size(size).build())).addExtras("@", "成功失败");
            if (1 == version)
                result.setSuccess(service.findPage(TabValid.builder().value((short) 10).build(), Pager.builder().number(number).size(size).build())).addExtras("@", "成功失败");
            if (2 == version) result.setSuccess(service.findPage(null, null)).addExtras("@", "成功失败");
        });
    }

}
