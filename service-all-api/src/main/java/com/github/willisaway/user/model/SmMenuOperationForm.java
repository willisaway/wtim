package com.github.willisaway.user.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class SmMenuOperationForm implements Serializable {

	private Long menuId;

	private String menuName;

	private List<SmMenuOperation> option;

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public List<SmMenuOperation> getOption() {
		return option;
	}

	public void setOption(List<SmMenuOperation> option) {
		this.option = option;
	}

}