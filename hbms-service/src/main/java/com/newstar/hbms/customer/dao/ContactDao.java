package com.newstar.hbms.customer.dao;

import com.newstar.hbms.customer.domain.Contact;
import com.newstar.hbms.support.paging.PageRange;
import com.newstar.hbms.support.paging.PagingResult;

import java.util.List;

/**
 * Created by fellowlong on 2016/10/22.
 */
public interface ContactDao {

  public int insert(Contact contact);

  public int update(Contact contact);

  public int disable(Long[] contactIds);

  public int enable(Long[] contactIds);

  public PagingResult<Contact> findByBean(Contact contact, PageRange pageRange);

  public List<Contact> findByIds(Long[] ids);

}
