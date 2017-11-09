package indi.f.service;

import java.util.List;

import indi.f.bean.Department;
import indi.f.dao.DepartmentMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *@author:jeff
 *@date:2017年10月22日下午1:03:23
 *代码要添加注释
 */
@Service("deptService")
public class DeptService {
	@Autowired
	DepartmentMapper departmentMapper;

	public List<Department> getDepts() {
		
		return departmentMapper.selectByExample(null);
	}
	
}
