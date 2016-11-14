package com.newstar.hbms.common.dao;

import com.newstar.hbms.common.domain.Attachment;
import com.newstar.hbms.support.paging.PageRange;
import com.newstar.hbms.support.paging.PagingResult;

import java.util.List;

/**
 * Created by wangjinsi on 2016/11/2.
 */
public interface AttachmentDao {
    
    public int insert(Attachment attachment);

    public int update(Attachment attachment);

    public int disableByBusinessId(Long businessId);

    public int disable(Long[] attachmentIds);

    public int enable(Long[] attachmentIds);

    public int enableByBusinessId(Long businessId);

    public List<Attachment> findByBean(Attachment attachment);

    public List<Attachment> findByIds(Long[] ids);
}
