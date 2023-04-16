package utez.edu.mx.smartfitutez.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utez.edu.mx.smartfitutez.security.entity.MainUser;
import utez.edu.mx.smartfitutez.security.entity.UserLR;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserLrService userLrService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserLR userLR = userLrService.getByEmail(email).get();
        return MainUser.build(userLR);
    }
}
