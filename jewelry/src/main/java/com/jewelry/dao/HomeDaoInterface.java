package com.jewelry.dao;

import java.util.List;

import com.jewelry.vo.Customer;
import com.jewelry.vo.Home;

public interface HomeDaoInterface {


	List<Home> selectCustomer(int storeNo);

	List<Home> selectProduct(int storeNo);

	List<Home> selectAc(int storeNo);

	List<Home> selectPrice(int storeNo);


/*	int countMM();
	int countWW();*/

}
