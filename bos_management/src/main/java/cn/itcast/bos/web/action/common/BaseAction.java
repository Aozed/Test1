package cn.itcast.bos.web.action.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;

public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>{
	
	protected T model;
	
	@Override
	public T getModel() {
		return model;
	}

	public BaseAction() {
		Type type = this.getClass().getGenericSuperclass();
		ParameterizedType parameterizedType = (ParameterizedType) type;
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public void pushToValueStack(Page<T> pageData){
		//封装数据进入map,方便数据的转换total与rows
		Map<String, Object> map = new HashMap<>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		
		//存入值栈,使用push方法
		ActionContext.getContext().getValueStack().push(map);
	}
}
