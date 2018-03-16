package cn.itcast.bos.web.action.base;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.FixedAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class FixedAreaAction extends BaseAction<FixedArea>{
	
	@Autowired
	private FixedAreaService fixedAreaService;
	
	@Action(value="fixedArea_save",results={@Result(name="success",type="redirect",
			location="./pages/base/fixed_area.html")})
	public String save(){
		fixedAreaService.save(model);
		return SUCCESS;
	}
	
	@Action(value="fixedArea_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Specification<FixedArea> specification = new Specification<FixedArea>() {
			
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		Pageable pageable = new PageRequest(page-1, rows);
		
		Page<FixedArea> pageData = fixedAreaService.findAllPage(specification,pageable);
		pushToValueStack(pageData);
		return SUCCESS;
	}
	
}
