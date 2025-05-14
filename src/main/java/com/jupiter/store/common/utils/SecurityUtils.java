package com.jupiter.store.common.utils;

import com.jupiter.store.common.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Integer getCurrentUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return extractPrincipal(securityContext.getAuthentication());
    }

    public static Integer extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof CustomUserDetails springSecurityUser) {
            return springSecurityUser.getUserId();
        }
        if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return Integer.valueOf(springSecurityUser.getUsername());
        } else if (authentication.getPrincipal() instanceof String) {
            return Integer.valueOf(authentication.getPrincipal().toString());
        }
        return null;
    }


    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
//    public static boolean isAuthenticated() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication != null && getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
//    }
//
//    /**
//     * Checks if the current user has any of the authorities.
//     *
//     * @param authorities the authorities to check.
//     * @return true if the current user has any of the authorities, false otherwise.
//     */
//    public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return (
//            authentication != null && getAuthorities(authentication).anyMatch(authority -> Arrays.asList(authorities).contains(authority))
//        );
//    }
//
//    /**
//     * Checks if the current user has none of the authorities.
//     *
//     * @param authorities the authorities to check.
//     * @return true if the current user has none of the authorities, false otherwise.
//     */
//    public static boolean hasCurrentUserNoneOfAuthorities(String... authorities) {
//        return !hasCurrentUserAnyOfAuthorities(authorities);
//    }
//
//    /**
//     * Checks if the current user has a specific authority.
//     *
//     * @param authority the authority to check.
//     * @return true if the current user has the authority, false otherwise.
//     */
//    public static boolean hasCurrentUserThisAuthority(String authority) {
//        return hasCurrentUserAnyOfAuthorities(authority);
//    }

//    private static Stream<String> getAuthorities(Authentication authentication) {
//        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
//    }
}
