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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * 请求操作响应：案例
 *
 * @author 谢长春 2018-10-5
 */
@RequestMapping("/1/valid")
@Controller
@Slf4j
@RequiredArgsConstructor
public class ValidController implements IAuthController<Long, TabValid> {

    private final ValidService service;
    int version = 1;

    @GetMapping("/hasNull")
    @ResponseBody
    public Result<String> hasNull() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.hasNull(null));
            if (1 == version) result.setSuccess(service.hasNull(""));
        });
    }

    @GetMapping("/notNull")
    @ResponseBody
    public Result<String> notNull() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.notNull(""));
            if (1 == version) result.setSuccess(service.notNull(null));
        });
    }

    @GetMapping("/notBlank")
    @ResponseBody
    public Result<String> notBlank() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.notBlank(null));
            if (1 == version) result.setSuccess(service.notBlank("text"));
            if (2 == version) result.setSuccess(service.notBlank(""));
        });
    }

    @GetMapping("/size")
    @ResponseBody
    public Result<String> size() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.size("text"));
            if (1 == version) result.setSuccess(service.size(null));
            if (2 == version) result.setSuccess(service.size(""));
            if (3 == version) result.setSuccess(service.size(Strings.repeat("-", 11)));
            if (4 == version) result.setSuccess(service.size(Strings.repeat("测试", 5)));
        });
    }

    @GetMapping("/pattern")
    @ResponseBody
    public Result<String> pattern() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.pattern("text_0"));
            if (1 == version) result.setSuccess(service.pattern(null));
            if (2 == version) result.setSuccess(service.pattern(""));
            if (3 == version) result.setSuccess(service.pattern("测试"));
        });
    }

    @GetMapping("/notEmpty")
    @ResponseBody
    public Result<String> notEmpty() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.notEmpty(Collections.singletonList(Util.uuid32())));
            if (1 == version) result.setSuccess(service.notEmpty(null));
            if (2 == version) result.setSuccess(service.notEmpty(Collections.emptyList()));
            if (3 == version) result.setSuccess(service.notEmpty(Collections.singletonList("")));
        });
    }

    @GetMapping("/min")
    @ResponseBody
    public Result<String> min() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.min(null));
            if (1 == version) result.setSuccess(service.min(0));
        });
    }

    @GetMapping("/max")
    @ResponseBody
    public Result<String> max() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.max(null));
            if (1 == version) result.setSuccess(service.max(11));
        });
    }

    @GetMapping("/decimalMin")
    @ResponseBody
    public Result<String> decimalMin() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.decimalMin(null));
            if (1 == version) result.setSuccess(service.decimalMin(BigDecimal.ZERO));
        });
    }

    @GetMapping("/decimalMax")
    @ResponseBody
    public Result<String> decimalMax() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.decimalMax(null));
            if (1 == version) result.setSuccess(service.decimalMax(BigDecimal.valueOf(11)));
        });
    }

    @GetMapping("/digits")
    @ResponseBody
    public Result<String> digits() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.digits(null));
            if (1 == version) result.setSuccess(service.digits(new BigDecimal("10.222")));
        });
    }

    @GetMapping("/negative")
    @ResponseBody
    public Result<String> negative() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.negative(null));
            if (1 == version) result.setSuccess(service.negative(BigDecimal.ONE));
        });
    }

    @GetMapping("/negativeOrZero")
    @ResponseBody
    public Result<String> negativeOrZero() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.negativeOrZero(null));
            if (1 == version) result.setSuccess(service.negativeOrZero(BigDecimal.TEN));
        });
    }

    @GetMapping("/positive")
    @ResponseBody
    public Result<String> positive() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.positive(null));
            if (1 == version) result.setSuccess(service.positive(BigDecimal.ZERO));
        });
    }

    @GetMapping("/positiveOrZero")
    @ResponseBody
    public Result<String> positiveOrZero() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.positiveOrZero(null));
            if (1 == version) result.setSuccess(service.positiveOrZero(BigDecimal.valueOf(-1)));
        });
    }

    @GetMapping("/assertTrue")
    @ResponseBody
    public Result<String> assertTrue() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.assertTrue(null));
            if (1 == version) result.setSuccess(service.assertTrue(false));
        });
    }

    @GetMapping("/assertFalse")
    @ResponseBody
    public Result<String> assertFalse() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.assertFalse(null));
            if (1 == version) result.setSuccess(service.assertFalse(true));
        });
    }

    @GetMapping("/email")
    @ResponseBody
    public Result<String> email() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.email(null));
            if (1 == version) result.setSuccess(service.email("x@126.com"));
            if (2 == version) result.setSuccess(service.email("126.com"));
        });
    }

    @GetMapping("/past")
    @ResponseBody
    public Result<String> past() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.past(null));
            if (1 == version) result.setSuccess(service.past(Dates.now().addDay(1).timestamp()));
        });
    }

    @GetMapping("/future")
    @ResponseBody
    public Result<String> future() {
        return new Result<String>().execute(result -> {
            if (0 == version) result.setSuccess(service.future(null));
            if (1 == version) result.setSuccess(service.future(Dates.now().addDay(-1).timestamp()));
        });
    }

    @PostMapping
    @ResponseBody
    @Override
    public Result<TabValid> save(
            @AuthenticationPrincipal final TabUser user,
            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
            @RequestBody(required = false) final String body) {
        return new Result<TabValid>().execute(result -> {
            {
                if (0 == version)
                    result.setSuccess(service.save(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 15)).build(), user.getId()));
                if (1 == version)
                    result.setSuccess(service.save(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 10).build(), user.getId()));
                if (2 == version) result.setSuccess(service.save(null, null));
                if (3 == version) result.setSuccess(service.save(TabValid.builder().build(), null));
                if (4 == version) result.setSuccess(service.save(TabValid.builder().build(), user.getId()));
                if (5 == version) result.setSuccess(service.save(TabValid.builder().id(1L).build(), user.getId()));
                if (6 == version)
                    result.setSuccess(service.save(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 16)).build(), user.getId()));
                if (7 == version)
                    result.setSuccess(service.save(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).build(), user.getId()));
                if (8 == version)
                    result.setSuccess(service.save(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).build(), 0L));
            }
            {
                if (9 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 15)).build()), user.getId()));
                if (10 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 10).build()), user.getId()));
                if (11 == version) result.setSuccess(service.saveAll(null, null));
                if (12 == version) result.setSuccess(service.saveAll(Collections.singletonList(null), null));
                if (13 == version) result.setSuccess(service.saveAll(Collections.emptyList(), null));
                if (14 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().build()), null));
                if (15 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().build()), user.getId()));
                if (16 == version)
                    result.setSuccess(service.saveAll(null, user.getId()));
                if (17 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().id(1L).build()), user.getId()));
                if (18 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 16)).build()), user.getId()));
                if (19 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).build()), user.getId()));
            }
            {
                if (20 == version)
                    result.setSuccess(service.save(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 15)).build()));
                if (21 == version)
                    result.setSuccess(service.save(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 10).build()));
                if (22 == version) result.setSuccess(service.save(null));
                if (23 == version) result.setSuccess(service.save(TabValid.builder().build()));
                if (24 == version) result.setSuccess(service.save(TabValid.builder().id(1L).build()));
                if (25 == version)
                    result.setSuccess(service.save(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 16)).build()));
                if (26 == version)
                    result.setSuccess(service.save(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).build()));
            }
            {
                if (27 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 15)).build())));
                if (28 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().id(1L).label(Strings.repeat("测试", 5)).value((short) 10).build())));
                if (29 == version) result.setSuccess(service.saveAll(null));
                if (30 == version) result.setSuccess(service.saveAll(Collections.singletonList(null)));
                if (31 == version) result.setSuccess(service.saveAll(Collections.emptyList()));
                if (32 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().build())));
                if (33 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().id(1L).build())));
                if (34 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).content(Strings.repeat("内容", 16)).build())));
                if (35 == version)
                    result.setSuccess(service.saveAll(Collections.singletonList(TabValid.builder().label(Strings.repeat("测试", 5)).value((short) 1).build())));
            }
        });
    }

    @PutMapping("/{id}")
    @ResponseBody
    @Override
    public Result<Void> update(
            @AuthenticationPrincipal final TabUser user,

            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
            @RequestBody(required = false) final String body) {
        return new Result<Void>().call(() -> {
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
    @Override
    public Result<Void> deleteById(
            @AuthenticationPrincipal final TabUser user,

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
    @Override
    public Result<Void> deleteByUid(
            @AuthenticationPrincipal final TabUser user,

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
    @Override
    public Result<Void> markDeleteById(
            @AuthenticationPrincipal final TabUser user,

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
    @Override
    public Result<Void> markDeleteByUid(
            @AuthenticationPrincipal final TabUser user,

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
    @Override
    public Result<Void> markDelete(
            @AuthenticationPrincipal final TabUser user,

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

    @GetMapping
    @ResponseBody
    @Override
    public Result<TabValid> search(
            @AuthenticationPrincipal final TabUser user,

            @RequestParam(required = false, defaultValue = "{}") final TabValid condition) {
        return new Result<TabValid>().execute(result -> {
            if (0 == version) result.setSuccess(service.findList(condition));
            if (1 == version) result.setSuccess(service.findList(TabValid.builder().value((short) 10).build()));
            if (2 == version) result.setSuccess(service.findList(null));
        });
    }

    @GetMapping("/page/{number}/{size}")
    @ResponseBody
    @Override
    public Result<TabValid> page(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "页码", example = "1") @PathVariable final int number,
            @ApiParam(required = true, value = "每页条数", example = "1") @PathVariable final int size,
            @RequestParam(required = false, defaultValue = "{}") final TabValid condition) {
        return new Result<TabValid>().execute(result -> {
            if (0 == version)
                result.setSuccess(service.findPage(condition, Pager.builder().number(number).size(size).build()));
            if (1 == version)
                result.setSuccess(service.findPage(TabValid.builder().value((short) 10).build(), Pager.builder().number(number).size(size).build()));
            if (2 == version) result.setSuccess(service.findPage(null, null));
        });
    }

}
