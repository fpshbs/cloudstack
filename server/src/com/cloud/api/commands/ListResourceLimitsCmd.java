/**
 *  Copyright (C) 2010 Cloud.com, Inc.  All rights reserved.
 * 
 * This software is licensed under the GNU General Public License v3 or later.
 * 
 * It is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.cloud.api.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloud.api.ApiDBUtils;
import com.cloud.api.BaseCmd.Manager;
import com.cloud.api.BaseListCmd;
import com.cloud.api.Implementation;
import com.cloud.api.Parameter;
import com.cloud.api.response.ListResponse;
import com.cloud.api.response.ResourceLimitResponse;
import com.cloud.configuration.ResourceLimitVO;
import com.cloud.user.Account;

@Implementation(method="searchForLimits", manager=Manager.AccountManager, description="Lists resource limits.")
public class ListResourceLimitsCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger(ListResourceLimitsCmd.class.getName());

    private static final String s_name = "listresourcelimitsresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name="account", type=CommandType.STRING, description="Lists resource limits by account. Must be used with the domainId parameter.")
    private String accountName;

    @Parameter(name="domainid", type=CommandType.LONG, description="Lists resource limits by domain ID. If used with the account parameter, lists resource limits for a specified account in a specified domain.")
    private Long domainId;

    @Parameter(name="id", type=CommandType.LONG, description="Lists resource limits by ID.")
    private Long id;

    @Parameter(name="resourcetype", type=CommandType.INTEGER, description="Type of resource to update. Values are 0, 1, 2, 3, and 4. 0 - Instance. Number of instances a user can create. " +
																						"1 - IP. Number of public IP addresses a user can own. " +
																						"2 - Volume. Number of disk volumes a user can create." +
																						"3 - Snapshot. Number of snapshots a user can create." +
																						"4 - Template. Number of templates that a user can register/create.")
	private Integer resourceType;
			
    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public String getAccountName() {
        return accountName;
    }

    public Long getDomainId() {
        return domainId;
    }

    public Long getId() {
        return id;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getName() {
        return s_name;
    }

    @Override @SuppressWarnings("unchecked")
    public ListResponse<ResourceLimitResponse> getResponse() {
        List<ResourceLimitVO> limits = (List<ResourceLimitVO>)getResponseObject();

        ListResponse<ResourceLimitResponse> response = new ListResponse<ResourceLimitResponse>();
        List<ResourceLimitResponse> limitResponses = new ArrayList<ResourceLimitResponse>();
        for (ResourceLimitVO limit : limits) {
            ResourceLimitResponse resourceLimitResponse = new ResourceLimitResponse();
            if (limit.getDomainId() != null) {
                resourceLimitResponse.setDomainId(limit.getDomainId());
                resourceLimitResponse.setDomainName(ApiDBUtils.findDomainById(limit.getDomainId()).getName());
            }
                
            if (limit.getAccountId() != null) {
                Account accountTemp = ApiDBUtils.findAccountById(limit.getAccountId());
                if (accountTemp != null) {
                    resourceLimitResponse.setAccountName(accountTemp.getAccountName());
                    resourceLimitResponse.setDomainId(accountTemp.getDomainId());
                    resourceLimitResponse.setDomainName(ApiDBUtils.findDomainById(accountTemp.getDomainId()).getName());
                }
            }

            resourceLimitResponse.setResourceType(limit.getType().ordinal());
            resourceLimitResponse.setMax(limit.getMax());

            resourceLimitResponse.setResponseName("resourcelimit");
            limitResponses.add(resourceLimitResponse);
        }

        response.setResponses(limitResponses);
        response.setResponseName(getName());
        return response;
    }
}
