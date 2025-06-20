package com.vibecheck.VibeCheck_Backend.services;

import com.vibecheck.VibeCheck_Backend.models.Aluno;
import com.vibecheck.VibeCheck_Backend.models.Professor;
import com.vibecheck.VibeCheck_Backend.repositories.AlunoRepository;
import com.vibecheck.VibeCheck_Backend.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final List<String> EMAILS_PROFESSORES = List.of(
            "igornayancabj5a@gmail.com",
            "nathannmvr@gmail.com"
    );

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String nome = (String) attributes.get("name");
        String googleId = (String) attributes.get("sub");

        Set<GrantedAuthority> authorities = new HashSet<>();

        if (email != null && EMAILS_PROFESSORES.contains(email.toLowerCase())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_PROFESSOR"));

            Professor prof = professorRepository.findByGoogleId(googleId).orElseGet(Professor::new);
            prof.setGoogleId(googleId);
            prof.setEmail(email);
            prof.setNome(nome);

            professorRepository.save(prof);
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_ALUNO"));

            Aluno aluno = alunoRepository.findByGoogleId(googleId).orElseGet(Aluno::new);
            aluno.setGoogleId(googleId);
            aluno.setEmail(email);
            aluno.setNome(nome);

            alunoRepository.save(aluno);
        }

        return new DefaultOAuth2User(authorities, attributes, "email");
    }
}
