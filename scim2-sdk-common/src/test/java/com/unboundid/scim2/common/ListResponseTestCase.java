/*
 * Copyright 2015 UnboundID Corp.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPLv2 only)
 * or the terms of the GNU Lesser General Public License (LGPLv2.1 only)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 */

package com.unboundid.scim2.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unboundid.scim2.common.messages.ListResponse;
import com.unboundid.scim2.common.types.ResourceTypeResource;
import com.unboundid.scim2.common.utils.SchemaUtils;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;

import static org.testng.Assert.assertEquals;

/**
 * Test for list responses.
 */
public class ListResponseTestCase
{
  /**
   * Test list response.
   *
   * @throws Exception If an error occurs.
   */
  @Test
  public void testListResponse()
      throws Exception
  {
    ListResponse<ObjectNode> listResponse =
        SchemaUtils.createSCIMCompatibleMapper().readValue("{  \n" +
            "  \"schemas\":[  \n" +
            "    \"urn:ietf:params:scim:api:messages:2.0:ListResponse\"\n" +
            "  ],\n" +
            "  \"totalResults\":2,\n" +
            "  \"startIndex\":1,\n" +
            "  \"itemsPerPage\":3,\n" +
            "  \"Resources\":[  \n" +
            "    {  \n" +
            "      \"userName\":\"bjensen\"\n" +
            "    },\n" +
            "    {  \n" +
            "      \"userName\":\"jsmith\"\n" +
            "    }\n" +
            "  ]\n" +
            "}", new TypeReference<ListResponse<ObjectNode>>() {});

    assertEquals(listResponse.getTotalResults(), 2);
    assertEquals(listResponse.getStartIndex(), Integer.valueOf(1));
    assertEquals(listResponse.getItemsPerPage(), Integer.valueOf(3));
    assertEquals(listResponse.getResources().size(), 2);

    ArrayList<ResourceTypeResource> resourceTypeList =
        new ArrayList<ResourceTypeResource>();
    resourceTypeList.add(
        new ResourceTypeResource("urn:test", "test", "test", new URI("/test"),
            new URI("urn:test"),
            Collections.<ResourceTypeResource.SchemaExtension>emptyList()));
    resourceTypeList.add(
        new ResourceTypeResource("urn:test2", "test2", "test2",
            new URI("/test2"), new URI("urn:test2"),
            Collections.<ResourceTypeResource.SchemaExtension>emptyList()));
    ListResponse<ResourceTypeResource> response =
        new ListResponse<ResourceTypeResource>(100, resourceTypeList, 1, 10);

    String serialized = SchemaUtils.createSCIMCompatibleMapper().
        writeValueAsString(response);
    assertEquals(SchemaUtils.createSCIMCompatibleMapper().readValue(
        serialized,
            new TypeReference<ListResponse<ResourceTypeResource>>() { }),
        response);
  }
}