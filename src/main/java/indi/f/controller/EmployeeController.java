package indi.f.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import indi.f.bean.Employee;
import indi.f.bean.Msg;
import indi.f.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 *@author:jeff
 *@date:2017年10月18日下午11:44:25
 *代码要添加注释
 */
@Controller
public class EmployeeController {
	@Autowired
	EmployeeService employeeService;
	
	//@RequestMapping("/emps")
	public String getEmps(@RequestParam(value="pageNo",defaultValue="1")Integer pageNo,Model model){
		// 这不是一个分页查询；
				// 引入PageHelper分页插件
				// 在查询之前只需要调用，传入页码，以及每页的大小
				PageHelper.startPage(pageNo, 5);
				// startPage后面紧跟的这个查询就是一个分页查询
				List<Employee> emps = employeeService.getAll();
				// 使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了。
				// 封装了详细的分页信息,包括有我们查询出来的数据，传入连续显示的页数
				PageInfo page = new PageInfo(emps, 5);
				model.addAttribute("pageInfo", page);
		return "list";
	}
	
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(
			@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) {
		// 这不是一个分页查询
		// 引入PageHelper分页插件
		// 在查询之前只需要调用，传入页码，以及每页的大小
		PageHelper.startPage(pageNo, 5);
		// startPage后面紧跟的这个查询就是一个分页查询
		List<Employee> emps = employeeService.getAll();
		// 使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了。
		// 封装了详细的分页信息,包括有我们查询出来的数据，传入连续显示的页数
		PageInfo page = new PageInfo(emps, 5);
		return Msg.success().add("pageInfo", page);
	}
	
	@RequestMapping(value="/addEmp",method=RequestMethod.POST)
	@ResponseBody
	public Msg addEmp(@Valid Employee emp,BindingResult result){
		if(result.hasErrors()){
			//校验失败，应该返回失败，在模态框中显示校验失败的错误信息
			Map<String, Object> map = new HashMap<>();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				System.out.println("错误的字段名："+fieldError.getField());
				System.out.println("错误信息："+fieldError.getDefaultMessage());
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);
		}else{
			employeeService.addEmp(emp);
			return Msg.success();
		}
	}
	
	@RequestMapping(value="/checkUser")
	@ResponseBody
	public Msg checkUser(String empName){
		//先判断用户名是否是合法的表达式;
		String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
		if(!empName.matches(regx)){
			return Msg.fail().add("va_msg", "用户名必须是6-16位数字和字母的组合或者2-5位中文");
		}
		
		boolean flag=employeeService.checkUser(empName);
		if(flag){
			return Msg.success();
		}else{
			return Msg.fail().add("va_msg", "用户名不可用");
		}
	}
	//rest风格
	@RequestMapping(value="/emp/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id")Integer empId){
		Employee emp=employeeService.getEmp(empId);
		return Msg.success().add("emp", emp);
	}
	@RequestMapping(value="/emp/{empId}",method=RequestMethod.PUT)
	@ResponseBody
	public Msg updateEmp(@Valid Employee emp,BindingResult result){
		if(result.hasErrors()){
			//校验失败，应该返回失败，在模态框中显示校验失败的错误信息
			Map<String, Object> map = new HashMap<>();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				System.out.println("错误的字段名："+fieldError.getField());
				System.out.println("错误信息："+fieldError.getDefaultMessage());
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);
		}else{
			employeeService.upateEmp(emp);
			return Msg.success();
		}
	}
//	@RequestMapping(value="/emp/{empId}",method=RequestMethod.DELETE)
//	@ResponseBody
//	public Msg deleteEmp(@PathVariable("empId")Integer id){
//		employeeService.delEmp(id);
//		return Msg.success();
//	}
	//单个批量一起的请求
	//有-就是批量
	@RequestMapping(value="/emp/{empIds}",method=RequestMethod.DELETE)
	@ResponseBody
	public Msg deleteEmp(@PathVariable("empIds")String ids){
		if(ids.contains("-")){
			List<Integer> idlist=new ArrayList<>();
			String[] idsStr=ids.split("-");
			for(String s:idsStr){
				idlist.add(Integer.parseInt(s));
			}
			//批量删除方法
			employeeService.delEmps(idlist);
		}else{
			employeeService.delEmp(Integer.parseInt(ids));
		}
		return Msg.success();
	}
	
}
