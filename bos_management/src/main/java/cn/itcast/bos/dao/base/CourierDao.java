package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;
/**
 * courier模块dao层接口
 * @author Administrator
 *
 */
public interface CourierDao extends JpaRepository<Courier, Integer>,
		JpaSpecificationExecutor<Courier>	{
	//自定义方法
	@Query("update Courier set deltag = '1' where id = ?")
	@Modifying
	void update(int id);

}
