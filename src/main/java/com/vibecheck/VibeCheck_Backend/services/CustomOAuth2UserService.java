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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final List<String> EMAILS_PROFESSORES = List.of(
            "igornayancabj5a@gmail.com",
            "nathannmvr@gmail.com",
            "cristinavaleriagl@gmail.com"
    );

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        // Chama a implementação padrão para buscar os dados do usuário do Google
        OAuth2User oAuth2User = super.loadUser(request);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Extrai os atributos principais do usuário
        String email = (String) attributes.get("email");
        String nome = (String) attributes.get("name");
        String googleId = (String) attributes.get("sub"); // 'sub' é o ID único do usuário no Google

        // Verificação de nulo
        if (googleId == null) {
            throw new IllegalArgumentException("Google ID não encontrado nos atributos.");
        }

        // Prepara a lista de permissões (roles) do usuário
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Lógica de Negócio: Determina o papel do usuário e salva ou atualiza no banco
        if (email != null && EMAILS_PROFESSORES.contains(email.toLowerCase())) {
            // Se o e-mail está na lista de professores:
            authorities.add(new SimpleGrantedAuthority("ROLE_PROFESSOR"));

            // Padrão "Get or Create": Busca o professor pelo googleId
            Professor prof = professorRepository.findByGoogleId(googleId).orElseGet(Professor::new);
            prof.setGoogleId(googleId);
            prof.setEmail(email);
            prof.setNome(nome);
            professorRepository.save(prof); // Salva (insere ou atualiza) no banco
        } else {
            // Se não for professor, é considerado aluno:
            authorities.add(new SimpleGrantedAuthority("ROLE_ALUNO"));

            // Mesma lógica de "Get or Create" para o aluno
            Aluno aluno = alunoRepository.findByGoogleId(googleId).orElseGet(Aluno::new);
            aluno.setGoogleId(googleId);
            aluno.setEmail(email);
            aluno.setNome(nome);
            alunoRepository.save(aluno);
        }

        // Retorna um novo objeto de usuário para o Spring Security, com as permissões
        return new DefaultOAuth2User(authorities, attributes, "email");
    }
}
