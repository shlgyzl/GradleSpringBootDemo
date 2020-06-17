package com.application.feign.service;

import feign.Param;
import feign.RequestLine;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DamServiceFeign {
    /**
     * 大坝列表
     */
    @RequestLine("GET /api/dams/search/simple")
    List findSimpleDams();

    /**
     * 根据damId和类型查询关联业务
     */
    @RequestLine("GET /api/receipt/business?damIds={damIds}&businessType={businessType}")
    Map findRelatedBusiness(@Param("damIds") String damIds, @Param("businessType") Integer businessType);

    /**
     * 获取大坝中心人员
     *
     * @return
     */
    @RequestLine("GET /api/users/damCentral/all/sort")
    List<Map<String, Object>> findDamCentralPerson();

    /**
     * 微信获取主管单位通讯录列表集合
     *
     * @return
     */
    @RequestLine("GET /api/findCompanies?userType=2")
    List<Map<String, Object>> findCompaniesByUserType();

    /**
     * 微信获取派出机构通讯录列表集合
     *
     * @return
     */
    @RequestLine("GET /api/findMonitorCompanies?userType=4")
    List<Map<String, Object>> findMonitorCompaniesByUserType();

    /**
     * 根据当前用户的的收藏集或者是名下管理的大坝显示其概要信息
     *
     * @param page
     * @param size
     * @param params
     * @param sort
     * @param userId
     * @return
     */
    @RequestLine("GET /api/dams/summary?page={page}&size={size}&params={params}&sort={sort}&userId={userId}")
    Page<Map<String, Object>> findDams(@Param("page") Integer page,
                                       @Param("size") Integer size, @Param("params") String params,
                                       @Param("sort") String sort, @Param("userId") Integer userId);

    /**
     * 查询所有大坝联系人，用于微信电厂联系人接口
     *
     * @return
     */
    @RequestLine("GET /api/damPeopleList")
    List<Map<String, Object>> findAllDamPeopleList();

    /**
     * 微信获取单位对应联系人
     */
    @RequestLine("GET /api/findUsers?userType={userType}&companyId={companyId}")
    List<Map<String, Object>> findUsersByCompanyId(@Param("userType") Integer userType,
                                                   @Param("companyId") Integer companyId);

}
