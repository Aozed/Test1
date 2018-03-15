package cn.itcast.bos.service.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierDao;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.CourierService;
/**
 * courier模块的service层实现类
 * @author Administrator
 *
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService{
	
	@Autowired
	private CourierDao courierDao;

	@Override
	public void save(Courier courier) {
		courierDao.save(courier);
	}

	@Override
	public Page<Courier> findAllPage(Specification specification,Pageable pageable) {
		return courierDao.findAll(specification, pageable);
	}

	@Override
	public void delete(String[] ids) {
		for (String idStr : ids) {
			int id = Integer.parseInt(idStr);
			courierDao.update(id);
		}
	}
}
