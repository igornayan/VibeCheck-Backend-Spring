// Define o pacote para as classes de serviço.
package com.vibecheck.VibeCheck_Backend.services;

// Importações dos modelos, repositórios e do Spring Security.
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

/**
 * @Service: Marca a classe como um serviço do Spring.
 * Estende DefaultOAuth2UserService para customizar o processo de obtenção de dados do usuário
 * após a autenticação com o provedor OAuth2.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    // Uma lista fixa de e-mails que define quem são os professores.
    // Para aplicações maiores, isso seria gerenciado em um banco de dados.
    private static final List<String> EMAILS_PROFESSORES = List.of(
            "igornayancabj5a@gmail.com",
            "nathannmvr@gmail.com",
            "cristinavaleriagl@gmail.com"
    );

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    /**
     * Este método é sobrescrito para ser executado logo após o usuário se autenticar no Google.
     * @Transactional: Garante que todas as operações com o banco de dados dentro deste método
     * sejam executadas como uma única transação atômica (ou tudo funciona, ou nada é salvo).
     */
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        // 1. Chama a implementação padrão para buscar os dados do usuário do Google.
        OAuth2User oAuth2User = super.loadUser(request);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 2. Extrai os atributos principais do usuário.
        String email = (String) attributes.get("email");
        String nome = (String) attributes.get("name");
        String googleId = (String) attributes.get("sub"); // 'sub' é o ID único do usuário no Google.

        // 3. Prepara a lista de permissões (roles) do usuário.
        Set<GrantedAuthority> authorities = new HashSet<>();

        // 4. Lógica de Negócio: Determina o papel e salva/atualiza o usuário.
        if (email != null && EMAILS_PROFESSORES.contains(email.toLowerCase())) {
            // Se o e-mail está na lista de professores:
            authorities.add(new SimpleGrantedAuthority("ROLE_PROFESSOR"));

            // Padrão "Get or Create" / "Upsert": Busca o professor pelo googleId.
            // Se não existir, cria um novo objeto Professor.
            Professor prof = professorRepository.findByGoogleId(googleId).orElseGet(Professor::new);
            // Atualiza os dados com as informações mais recentes do Google.
            prof.setGoogleId(googleId);
            prof.setEmail(email);
            prof.setNome(nome);

            professorRepository.save(prof); // Salva (insere ou atualiza) no banco.
        } else {
            // Se não for professor, é considerado aluno:
            authorities.add(new SimpleGrantedAuthority("ROLE_ALUNO"));

            // Mesma lógica de "Get or Create" para o aluno.
            Aluno aluno = alunoRepository.findByGoogleId(googleId).orElseGet(Aluno::new);
            aluno.setGoogleId(googleId);
            aluno.setEmail(email);
            aluno.setNome(nome);

            alunoRepository.save(aluno);
        }

        // 5. Retorna um novo objeto de usuário para o Spring Security, com as permissões
        // e atributos corretos. O último parâmetro ("email") define qual atributo será
        // usado como o 'name' do Principal.
        return new DefaultOAuth2User(authorities, attributes, "email");
    }
}