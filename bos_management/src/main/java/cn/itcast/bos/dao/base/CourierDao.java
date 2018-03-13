package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.Courier;
/**
 * courier模块dao层接口
 * @author Administrator
 *
 */
public interface CourierDao extends JpaRepository<Courier, Integer>,
		JpaSpecificationExecutor<Courier>	{

}
