package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.CourierService;

/**
 * 快递员模块action
 * 
 * @author Administrator
 *
 */
@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {

	private Courier courier = new Courier();
	@Autowired
	private CourierService courierService;

	@Override
	public Courier getModel() {
		return courier;
	}

	@Action(value = "courier_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/courier.html") })
	//添加快递员
	public String save() {
		courierService.save(courier);
		return SUCCESS;
	}

	/**
	 * 封装数据:page与rows
	 * @return
	 */
	private int page;
	private int rows;

	
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Action(value = "courier_pageQuery", results = { @Result(name = "success", type = "json") })
	/**
	 * 分页查询
	 * @return
	 */
	public String pageQuery() {
		// 创建pageable对象
		Pageable pageable = new PageRequest(page-1, rows);
		//编写条件
		Specification<Courier> specification = new Specification<Courier>() {
			
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				//创建数组保存条件
				List<Predicate> list = new ArrayList<>();
				/**
				 * 单表条件
				 */
				if(StringUtils.isNotBlank(courier.getCourierNum())){
					Predicate p1 = cb.equal(root.get("courierNum"), courier.getCourierNum());
					list.add(p1);
				}
				/**
				 * 多表条件
				 */
				//获取关联对象的表映射
				Join<Object, Object> standardRoot = root.join("standard",JoinType.INNER);
				if(courier.getStandard()!=null){
					Predicate p2 = cb.like(standardRoot.get("name").as(String.class), "%"+courier.getStandard().getName()+"%");
					list.add(p2);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		//获取page对象
		Page<Courier> pageData =  courierService.findAllPage(specification,pageable);
		
		//封装数据进入map,方便数据的转换total与rows
		Map<String, Object> map = new HashMap<>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		
		//存入值栈,使用push方法
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	private String id;

	public void setId(String id) {
		this.id = id;
	}
	
	@Action(value="courier_delete",results={@Result(name = "success", type = "redirect", location = "./pages/base/courier.html")})
	public String delete(){
		String[] ids = id.split(",");
		courierService.delete(ids);
		return SUCCESS;
	}
}
