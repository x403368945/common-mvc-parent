package com.ccx.demo.business.example.service;

import com.alibaba.fastjson.JSON;
import com.ccx.demo.business.example.entity.TabValid;
import com.querydsl.core.QueryResults;
import com.support.aop.annotations.ServiceAspect;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.service.IService;
import com.support.mvc.service.ISimpleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author 谢长春 2018/12/20
 */
@Slf4j
@Service
@ServiceAspect
public class ValidService implements IService<TabValid>, ISimpleService<TabValid> {

    public String hasNull(@Null String arg) {
        return (Objects.toString(arg));
    }

    public String notNull(@NotNull String arg) {
        return (Objects.toString(arg));
    }

    public String notBlank(@NotBlank String arg) {
        return (Objects.toString(arg));
    }

    public String size(@Size(min = 1, max = 10) String arg) {
        return (Objects.toString(arg));
    }

    public String pattern(@Pattern(regexp = "\\w+") String arg) {
        return (Objects.toString(arg));
    }

    public String notEmpty(@NotEmpty List<@NotBlank String> arg) {
        return (Objects.toString(arg));
    }

    public String min(@Min(1) Integer arg) {
        return (Objects.toString(arg));
    }

    public String max(@Max(10) Integer arg) {
        return (Objects.toString(arg));
    }

    public String decimalMin(@DecimalMin("1.00") BigDecimal arg) {
        return (Objects.toString(arg));
    }

    public String decimalMax(@DecimalMax("10.0") BigDecimal arg) {
        return (Objects.toString(arg));
    }

    public String digits(@Digits(integer = 10, fraction = 2) BigDecimal arg) {
        return (Objects.toString(arg));
    }

    public String negative(@Negative BigDecimal arg) {
        return (Objects.toString(arg));
    }

    public String negativeOrZero(@NegativeOrZero BigDecimal arg) {
        return (Objects.toString(arg));
    }

    public String positive(@Positive BigDecimal arg) {
        return (Objects.toString(arg));
    }

    public String positiveOrZero(@PositiveOrZero BigDecimal arg) {
        return (Objects.toString(arg));
    }

    public String assertTrue(@AssertTrue Boolean arg) {
        return (Objects.toString(arg));
    }

    public String assertFalse(@AssertFalse Boolean arg) {
        return (Objects.toString(arg));
    }

    public String email(@Email String arg) {
        return (Objects.toString(arg));
    }

    public String past(@Past Timestamp arg) {
        return (Objects.toString(arg));
    }

    public String future(@Future Timestamp arg) {
        return (Objects.toString(arg));
    }
    // IService Start **************************************************************************************************

    @Override
    public TabValid save(final TabValid obj, final Long userId) {
        log.info(JSON.toJSONString(Arrays.asList(obj, userId)));
        if (Objects.equals((short) 10, obj.getValue())) return null;
        return obj;
    }

    @Override
    public List<TabValid> saveAll(final List<TabValid> list, final Long userId) {
        log.info(JSON.toJSONString(Arrays.asList(list, userId)));
        if (Objects.equals((short) 10, list.get(0).getValue())) return null;
        return list;
    }

    @Override
    public void update(final Long id, final Long userId, final TabValid obj) {
        log.info(JSON.toJSONString(Arrays.asList(id, userId, obj)));
    }

    @Override
    public TabValid deleteById(final Long id, final Long userId) {
        log.info(JSON.toJSONString(Arrays.asList(id, userId)));
        return null;
    }

    @Override
    public TabValid deleteByUid(final Long id, final String uid, final Long userId) {
        log.info(JSON.toJSONString(Arrays.asList(id, uid, userId)));
        return null;
    }

    @Override
    public void markDeleteById(final Long id, final Long userId) {
        log.info(JSON.toJSONString(Arrays.asList(id, userId)));
    }

    @Override
    public void markDeleteByUid(final Long id, final String uid, final Long userId) {
        log.info(JSON.toJSONString(Arrays.asList(id, uid, userId)));
    }

    @Override
    public void markDeleteByIds(final List<Long> ids, final Long userId) {
        log.info(JSON.toJSONString(Arrays.asList(ids, userId)));
    }

    @Override
    public void markDelete(final List<MarkDelete> list, final Long userId) {
        log.info(JSON.toJSONString(Arrays.asList(list, userId)));
    }

    // IService End && ISimpleService Start ****************************************************************************

    @Override
    public TabValid save(final TabValid obj) {
        log.info(JSON.toJSONString(obj));
        if (Objects.equals((short) 10, obj.getValue())) return null;
        return obj;
    }

    @Override
    public List<TabValid> saveAll(final List<TabValid> list) {
        log.info(JSON.toJSONString(list));
        if (Objects.equals((short) 10, list.get(0).getValue())) return null;
        return list;
    }

    @Override
    public void update(final Long id, final TabValid obj) {
        log.info(JSON.toJSONString(Arrays.asList(id, obj)));
    }

    @Override
    public TabValid deleteById(final Long id) {
        log.info(JSON.toJSONString(id));
        return null;
    }

    @Override
    public TabValid deleteByUid(final Long id, final String uid) {
        log.info(JSON.toJSONString(Arrays.asList(id, uid)));
        return null;
    }

    @Override
    public void markDeleteById(final Long id) {
        log.info(JSON.toJSONString(id));
    }

    @Override
    public void markDeleteByUid(final Long id, final String uid) {
        log.info(JSON.toJSONString(Arrays.asList(id, uid)));
    }

    @Override
    public void markDeleteByIds(final List<Long> ids) {
        log.info(JSON.toJSONString(ids));
    }

    @Override
    public void markDelete(final List<MarkDelete> list) {
        log.info(JSON.toJSONString(list));
    }
    // ISimpleService End **********************************************************************************************

    @Override
    public Optional<TabValid> findById(final Long id) {
        log.info(Objects.toString(id));
        return Optional.of(TabValid.builder().id(id).build());
    }

    @Override
    public Optional<TabValid> findByUid(final Long id, final String uid) {
        log.info(JSON.toJSONString(Arrays.asList(id, uid)));
        return Optional.of(TabValid.builder().id(id).uid(uid).build());
    }

    @Override
    public List<TabValid> findList(final TabValid condition) {
        log.info(JSON.toJSONString(condition));
        if (Objects.equals((short) 10, condition.getValue())) return null;
        return Collections.singletonList(condition);
    }

    @Override
    public QueryResults<TabValid> findPage(final TabValid condition, final Pager pager) {
        log.info(JSON.toJSONString(Arrays.asList(condition, pager)));
        if (Objects.equals((short) 10, condition.getValue())) return null;
        return new QueryResults<>(Collections.singletonList(condition), 1L, 1L, 1L);
    }
}
