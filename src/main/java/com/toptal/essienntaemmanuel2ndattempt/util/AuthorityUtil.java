package com.toptal.essienntaemmanuel2ndattempt.util;

import com.toptal.essienntaemmanuel2ndattempt.domain.Role;

/**
 * @author bodmas
 */
public interface AuthorityUtil {

    String HAS_ADMIN_AUTHORITY = "hasAuthority('" + Role.ADMIN + "')";
    String HAS_USER_AUTHORITY = "hasAuthority('" + Role.USER + "')";
    String HAS_MANAGER_AUTHORITY = "hasAuthority('" + Role.USER_MANAGER + "')";
    String ADMIN_OR_MANAGER_AUTHORITY = HAS_ADMIN_AUTHORITY + " or " + HAS_MANAGER_AUTHORITY;
}
