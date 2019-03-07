package com.support.mvc.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * 服务接口基础方法定义
 *
 * @param <E>
 * @author 谢长春 2017年7月14日 上午11:23:18
 */
@Validated
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public interface ISearchService<E> extends ISearch<E> {
}