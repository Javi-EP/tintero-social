package cl.javiep.statsservice.mapper;

import cl.javiep.statsservice.dto.UserStatsRequestDTO;
import cl.javiep.statsservice.dto.UserStatsResponseDTO;
import cl.javiep.statsservice.model.UserStat;
import org.springframework.stereotype.Component;

@Component
public class UserStatMapper {

    public UserStat toEntity(UserStatsRequestDTO dto){
        UserStat userStat = new UserStat();
        userStat.setUserId(dto.getUserId());
        userStat.setBooksRead(dto.getBooksRead());
        userStat.setPagesRead(dto.getPagesRead());
        return userStat;
    }

    public UserStatsResponseDTO toResponseDTO(UserStat userStat){
        UserStatsResponseDTO dto = new UserStatsResponseDTO();
        dto.setUserId(userStat.getUserId());
        dto.setBooksRead(userStat.getBooksRead());
        dto.setPagesRead(userStat.getPagesRead());
        dto.setFavGenre(userStat.getFavGenre());
        return dto;
    }
}
