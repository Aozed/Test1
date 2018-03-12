package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.base.Standard;
//派件模块接口
public interface StandardDao extends JpaRepository<Standard, Integer>{
	
}
