package indi.f.controller;

import java.util.List;

import indi.f.bean.Department;
import indi.f.bean.Msg;
import indi.f.service.DeptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *@author:jeff
 *@date:2017年10月22日下午1:02:57
 *代码要添加注释
 */
@Controller
public class DeptController {
	@Autowired
	DeptService deptService;
	
	@RequestMapping("/getDepts")
	@ResponseBody
	public Msg getDepts(){
		List<Department> list=deptService.getDepts();
		return Msg.success().add("depts", list);
	}
	
}
