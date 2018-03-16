package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class AreaAction extends ActionSupport implements ModelDriven<Area> {

	private Area area = new Area();

	@Override
	public Area getModel() {
		return area;
	}

	@Autowired
	private AreaService areaService;

	private File file;

	public void setFile(File file) {
		this.file = file;
	}

	// 一键上传
	@Action(value = "area_upload" ,
			results = {@Result(name = "success", type = "redirect", location = "./pages/base/area.html") })
	public String upload() {
		HSSFWorkbook hssf = null;
		List<Area> list = new ArrayList<Area>();
		List<Area> erroeList = new ArrayList<Area>();
		try {
			hssf = new HSSFWorkbook(new FileInputStream(file));
			HSSFSheet sheet = hssf.getSheetAt(0);
			for (Row row : sheet) {
				if (row.getRowNum() == 0) {
					continue;
				}
				if (row.getCell(0) == null || row.getCell(0).getStringCellValue() == "") {
					continue;
				}
				Area area = new Area();
				area.setId(row.getCell(0).getStringCellValue());
				area.setProvince(row.getCell(1).getStringCellValue());
				area.setCity(row.getCell(2).getStringCellValue());
				area.setDistrict(row.getCell(3).getStringCellValue());
				area.setPostcode(row.getCell(4).getStringCellValue());

				String province = area.getProvince();
				String city = area.getCity();
				String district = area.getDistrict();

				String citycode = PinYin4jUtils.hanziToPinyin(city, "");
				area.setCitycode(citycode);

				String[] headByString = PinYin4jUtils.getHeadByString(province + city + district);
				area.setShortcode(StringUtils.join(headByString, ""));

				list.add(area);
				if (list.size() >= 50) {
					try {
						areaService.save(list);
						list.clear();
					} catch (Exception e) {
						e.printStackTrace();
						erroeList.addAll(list);
					}
				}
			}
			try {
				areaService.save(list);
			} catch (Exception e) {
				e.printStackTrace();
				erroeList.addAll(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Action(value = "area_pageQuery", results = {
			@Result(name = "success", type = "json") })
	public String pageQuery() {
		Specification<Area> specification = new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				
				if(StringUtils.isNotBlank(area.getProvince())){
					Predicate p1 = cb.like(root.get("province").as(String.class), "%"+area.getProvince()+"%");
					list.add(p1);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};

		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Area> pageData = areaService.findAllPage(specification,pageable);
		
		//封装数据进入map,方便数据的转换total与rows
		Map<String, Object> map = new HashMap<>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		
		//存入值栈,使用push方法
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
}
