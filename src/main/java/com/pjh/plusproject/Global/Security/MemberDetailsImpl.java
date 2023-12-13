package com.pjh.plusproject.Global.Security;

import com.pjh.plusproject.Member.Entity.Member;
import com.pjh.plusproject.Member.Entity.MemberRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemberDetailsImpl implements UserDetails {

    private Member member;

    public MemberDetailsImpl(Member member){
        this.member = member;
    }

    public Member getMember(){
        return member;
    }

    @Override
    public String getUsername(){
        return this.member.getUsername();
    }

    @Override
    public String getPassword(){
        return this.member.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){

        MemberRoleEnum role = member.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
