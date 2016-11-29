package com.newstar.hbms.customer.web.controller;

import com.newstar.hbms.customer.domain.Position;
import com.newstar.hbms.customer.domain.Company;
import com.newstar.hbms.customer.service.PositionService;
import com.newstar.hbms.customer.service.CustomerService;
import com.newstar.hbms.mvc.JsonResult;
import com.newstar.hbms.utils.DateEditor;
import com.newstar.hbms.utils.ExceptionUtils;
import com.newstar.hbms.utils.JsonUtils;
import com.newstar.hbms.utils.WebUtils;
import com.newstar.hbms.support.paging.PageRange;
import com.newstar.hbms.support.paging.PagingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by wangjinsi on 2016/10/22.
 */
public class PositionController extends MultiActionController {

  private PositionService positionService;

  private CustomerService customerService;

  private String datePattern;

  public void setPositionService(PositionService positionService) {
    this.positionService = positionService;
  }

  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }

  public ModelAndView workspace(HttpServletRequest request, HttpServletResponse response) throws Exception {
    PagingResult<Company> customerPagingResult = customerService.findByBean(new Company(), new PageRange(1, 100));
    return new ModelAndView("/customer/positionManager", "customers", customerPagingResult.getRecords());
  }

  public void insertOrUpdate(HttpServletRequest request, HttpServletResponse response, Position position) throws Exception  {
    JsonResult jsonResult = new JsonResult();
    try {
      int resultCount = positionService.insertOrUpdate(position);
      jsonResult.setSuccess(true);
      jsonResult.setData(resultCount);
    } catch (Throwable t) {
      logger.error("新增Position失败", t);
      jsonResult.setErrorMessage(ExceptionUtils.getExceptionStack(t));
    }
    WebUtils.writeWithJson(response, jsonResult);
  }

  public void disableByIds(HttpServletRequest request, HttpServletResponse response)
          throws Exception {
    JsonResult jsonResult = new JsonResult();
    try {
      List<Long> ids = new ArrayList<Long>();
      String[] idsStrArray = request.getParameterValues("ids[]");
      if (idsStrArray != null && idsStrArray.length > 0) {
        for (String idsStr : idsStrArray) {
          ids.add(new Long(idsStr));
        }
        int result = positionService.disable(ids.toArray(new Long[ids.size()]));
        if (result > 0) {
          jsonResult.setSuccess(true);
          jsonResult.setData(result);
        }
      }
    } catch (Throwable t) {
      logger.error("删除Position失败", t);
      jsonResult.setErrorMessage(ExceptionUtils.getExceptionStack(t));
    }
    WebUtils.writeWithJson(response, jsonResult);
  }

  public void findById(HttpServletRequest request, HttpServletResponse response, Position position)
          throws Exception {
    JsonResult jsonResult = new JsonResult();
    try {
      List<Position> positions = positionService.findByIds(new Long[]{position.getId()});
      if (positions != null && positions.size() == 1) {
        jsonResult.setSuccess(true);
        jsonResult.setData(positions.get(0));
      } else {
        jsonResult.setSuccess(false);
        jsonResult.setErrorMessage("没有找到客户");
      }
    } catch (Throwable t) {
      logger.error("查询Position失败", t);
      jsonResult.setErrorMessage(ExceptionUtils.getExceptionStack(t));
    }
    WebUtils.writeWithJson(response, JsonUtils.beanToJson(jsonResult, datePattern));
  }

  public void findByBean(HttpServletRequest request, HttpServletResponse response, Position position)
          throws Exception {
    String pageSize = request.getParameter("rows");
    String pageNum = request.getParameter("page");
    PageRange pageRange = new PageRange();
    if (pageSize != null) {
      pageRange.setPageSize(Integer.parseInt(pageSize));
    }
    if (pageNum != null) {
      pageRange.setPageNum(Integer.parseInt(pageNum));
    }
    PagingResult<Position> positionResult = positionService.findByBean(position, pageRange);
    Map<String, Object> jsonMap = new HashMap();
    jsonMap.put("page", pageNum);
    jsonMap.put("total ", positionResult.getPageTotal());
    jsonMap.put("records ", positionResult.getRecordTotal());
    if (positionResult.getRecords() != null) {
      jsonMap.put("rows", positionResult.getRecords().toArray());
    } else {
      jsonMap.put("rows", null);
    }
    WebUtils.writeWithJson(response, JsonUtils.beanToJson(jsonMap, datePattern));
  }

  @Override
  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
    super.initBinder(request, binder);
    binder.registerCustomEditor(Date.class, new DateEditor(datePattern));
  }

  public void setDatePattern(String datePattern) {
    this.datePattern = datePattern;
  }
}