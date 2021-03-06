package com.newstar.hbms.support.paging;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by fellowlong on 2014-08-13.
 */
@Intercepts({@Signature(
  type = Executor.class,
  method = "query",
  args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PagingPlugin implements Interceptor {

  public static final String PAGING_FLAG = "PAGING_FLAG";

  private Pattern pattern = Pattern.compile(".*from", Pattern.CASE_INSENSITIVE);

  public Object intercept(Invocation invocation) throws Throwable {
    Object result = null;
    Object allParams = invocation.getArgs()[1];
    Object noPageParams = null;
    PageRange pageRange = null;
    if (allParams instanceof PageRange) {
      pageRange = (PageRange) allParams;
    } else if (allParams instanceof Map) {
      Map<Object, Object> paramMap = (Map)allParams;
      List<Object> pageRangeKeys = new ArrayList<Object>();
      Map tempNoPageParams = new HashMap();
      for (Map.Entry<Object, Object> entry : paramMap.entrySet()) {
        if (entry.getValue() instanceof PageRange) {
          pageRangeKeys.add(entry.getKey());
        } else {
          //去除重复的值，和mybatis默认参数映射有关
          tempNoPageParams.put(entry.getValue(),entry.getValue());
        }
      }
      for (Object key : pageRangeKeys) {
        pageRange = (PageRange) paramMap.remove(key);
      }
      if (tempNoPageParams.size() == 1) {
        noPageParams = tempNoPageParams.values().iterator().next();
      } else {
        noPageParams = paramMap;
      }
    }
    if (pageRange != null) {
      PagingResult pagingResult = new PagingResult();
      pagingResult.setPageSize(pageRange.getPageSize());
      pagingResult.setPageNum(pageRange.getPageNum());
      //在原SQL上加上分页条件后再执行
      Executor executor = (Executor) invocation.getTarget();
      MappedStatement originalMappedStatement = (MappedStatement) invocation.getArgs()[0];
      BoundSql originalBoundSql = originalMappedStatement.getBoundSql(noPageParams);
      String originalSql = originalBoundSql.getSql();

      String pagingSql = "select t.* from (" + originalSql + ") as t limit ?, ?";
      List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>(originalBoundSql.getParameterMappings());
      if (parameterMappings == null) {
        parameterMappings = new ArrayList<ParameterMapping>(2);
      }
      parameterMappings.add(
        new ParameterMapping.Builder(
        originalMappedStatement.getConfiguration(),
        PAGING_FLAG,
        new PagingTypeHandler(pageRange.getStartIndex() - 1)).build());
      parameterMappings.add(
        new ParameterMapping.Builder(
        originalMappedStatement.getConfiguration(),
        PAGING_FLAG,
        new PagingTypeHandler(pageRange.getPageSize())).build()
      );

      MappedStatement recordMappedStatement = new MappedStatement.Builder(
        originalMappedStatement.getConfiguration(),
        originalMappedStatement.getId(),
        new PagingSqlSource(originalMappedStatement.getConfiguration(), pagingSql, parameterMappings),
        SqlCommandType.SELECT).resultMaps(originalMappedStatement.getResultMaps()).build();
      //执行原SQL，查询当页数据
      Object pageRecords =executor.query(
        recordMappedStatement,
        noPageParams,
        (RowBounds) invocation.getArgs()[2],
        (ResultHandler)invocation.getArgs()[3]);
      pagingResult.setRecords((List<?>) pageRecords);
      //执行Count语句，返回记录总数
      SimpleExecutor countExecutor = new SimpleExecutor(originalMappedStatement.getConfiguration(), executor.getTransaction());
      String countSql = "select count(1) from " + originalSql.substring(originalSql.toUpperCase().indexOf("FROM") + "FROM".length());
      List<ResultMap> resultMaps = new ArrayList<ResultMap>();
      resultMaps.add(new ResultMap.Builder(null, "", Long.class, new ArrayList<ResultMapping>()).build());
      MappedStatement countMappedStatement = new MappedStatement.Builder(
        originalMappedStatement.getConfiguration(),
        originalMappedStatement.getId() + "_count",
        new StaticSqlSource(originalMappedStatement.getConfiguration(), countSql, originalBoundSql.getParameterMappings()),
        SqlCommandType.SELECT).resultMaps(resultMaps).build();
      List<Long> count = countExecutor.query(
        countMappedStatement,
        noPageParams,
        (RowBounds) invocation.getArgs()[2],
        (ResultHandler) invocation.getArgs()[3]);
      pagingResult.setRecordTotal(count.get(0).intValue());
      List<PagingResult> listResult = new ArrayList<PagingResult>(1);
      listResult.add(pagingResult);
      result = listResult;
    } else {
      result = invocation.proceed();
    }
    return result;
  }

  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  public void setProperties(Properties properties) {
  }
}