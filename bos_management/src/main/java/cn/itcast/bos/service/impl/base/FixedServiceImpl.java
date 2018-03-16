package cn.itcast.bos.service.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.FixedAreaDao;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.FixedAreaService;
@Service
@Transactional
public class FixedServiceImpl implements FixedAreaService{
	@Autowired
	private FixedAreaDao fixedAreaDao;

	@Override
	public void save(FixedArea fixedArea) {
		fixedAreaDao.save(fixedArea);
	}

	@Override
	public Page<FixedArea> findAllPage(Specification<FixedArea> specification, Pageable pageable) {
		return fixedAreaDao.findAll(specification, pageable);
	}
	
	
}
