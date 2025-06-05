package com.vibecheck.VibeCheck_Backend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomOAuth2User(OAuth2User oauth2User, Collection<? extends GrantedAuthority> authorities) {
        this.oauth2User = oauth2User;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return oauth2User.getName();
    }

    public String getEmail() {
        return (String) oauth2User.getAttributes().get("email");
    }

    public String getGoogleId() {
        return (String) oauth2User.getAttributes().get("sub");
    }
}
