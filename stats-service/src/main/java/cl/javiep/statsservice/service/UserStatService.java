package cl.javiep.statsservice.service;

import cl.javiep.statsservice.dto.UserStatsResponseDTO;
import cl.javiep.statsservice.mapper.UserStatMapper;
import cl.javiep.statsservice.repository.UserStatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserStatService {

    private final UserStatMapper userStatMapper;
    private final UserStatRepository userStatRepository;

    public UserStatService(UserStatRepository userStatRepository, UserStatMapper userStatMapper){
        this.userStatRepository = userStatRepository;
        this.userStatMapper = userStatMapper;
    }

    //Obtener todos los stats del usuario
    public List<UserStatsResponseDTO> getStatsByUser(Long userId){
        return userStatRepository.findByUserId(userId)
                .stream()
                .map(userStatMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
