package cn.itcast.bos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

/**
 * courier模块service层接口
 * @author Administrator
 *
 */
public interface CourierService {

	void save(Courier courier);

	Page<Courier> findAllPage(Specification<Courier> specification, Pageable pageable);

}
