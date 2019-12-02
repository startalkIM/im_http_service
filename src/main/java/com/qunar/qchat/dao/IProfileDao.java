package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.Profile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IProfileDao {

    List<Profile> selectProfileInfo(
            @Param("username") String username,
            @Param("host") String host,
            @Param("version") int version
    );

    Profile selectProfileInfoByUserAndHost(@Param("username") String username,
                                           @Param("host") String host);

    Profile updateProfileInfo(@Param("username") String username,
                              @Param("host") String host,
                              @Param("url") String url,
                              @Param("mood") String mood);

    int selectUserCountByUserIdAndHost(@Param("userId") String userId, @Param("host") String host);


    int selectVCardCount(@Param("userName") String username,
                         @Param("host") String host);

    int insertVCard(Profile profile);
}