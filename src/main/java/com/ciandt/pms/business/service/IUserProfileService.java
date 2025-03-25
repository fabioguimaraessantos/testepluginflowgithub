package com.ciandt.pms.business.service;

import com.ciandt.pms.model.vo.UserProfile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public interface IUserProfileService {
    UserProfile findByUserAndProfile(final String login, final String applicationAcronym) throws IOException;
    Set<String> convertRolesToPMS(final UserProfile userProfile) throws IOException;
}
