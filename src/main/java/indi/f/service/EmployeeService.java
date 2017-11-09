package indi.f.service;

import java.util.List;

import indi.f.bean.Employee;
import indi.f.bean.EmployeeExample;
import indi.f.bean.EmployeeExample.Criteria;
import indi.f.dao.EmployeeMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *@author:jeff
 *@date:2017年10月18日下午11:45:46
 *代码要添加注释
 */
@Service("employeeService")
public class EmployeeService {
	@Autowired
	EmployeeMapper employeeMapper;

	public List<Employee> getAll() {
		
		return employeeMapper.selectByExampleWithDept(null);
	}
	public void addEmp(Employee emp){
		//insert是id也需要插入，由于id是自增的所以选择条件插入
		employeeMapper.insertSelective(emp);
	}
	public boolean checkUser(String name) {
		EmployeeExample example=new EmployeeExample();
		Criteria criteria=example.createCriteria();
		criteria.andEmpNameEqualTo(name);
		long count=employeeMapper.countByExample(example);
		return count==0;
	}
	public Employee getEmp(Integer empId) {
		
		return employeeMapper.selectByPrimaryKeyWithDept(empId);	
	}
	public void upateEmp(Employee emp) {
		employeeMapper.updateByPrimaryKeySelective(emp);
	}
	public void delEmp(Integer id) {
		employeeMapper.deleteByPrimaryKey(id);
	}
	public void delEmps(List<Integer> ids) {
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
			//delete from xxx where emp_id in(1,2,3)
		criteria.andEmpIdIn(ids);
		employeeMapper.deleteByExample(example);

	}
}
