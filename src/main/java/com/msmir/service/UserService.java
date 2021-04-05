package com.msmir.service;

import com.msmir.containers.game.Game;
import com.msmir.containers.game.GameStatus;
import com.msmir.entity.user.Role;
import com.msmir.entity.user.User;
import com.msmir.entity.user.UserRating;
import com.msmir.repository.UserRepository;
import java.security.Principal;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class UserService implements UserDetailsService {

    private static final int RATING_SIZE = 100;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setRoles(Collections.singleton(new Role(1L, "USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof User) {
                return (User) principal;
            }
        }
        return null;
    }

    public User getCurrentUser(Principal principal) {
        if (principal instanceof Authentication) {
            Authentication auth = (Authentication) principal;
            if (auth.getPrincipal() instanceof User) {
                return (User) auth.getPrincipal();
            }
        }
        return null;
    }

    public void addUserAvatar(Model model) {
        model.addAttribute("avatar", getUserAvatar(getCurrentUser()));
    }

    public String getUserAvatar(User user){
        if(user != null && user.getAvatar() != null){
            return "/uploads/" + user.getAvatar();
        }else{
            return "/img/defaultAvatar.jpg";
        }
    }

    private UserRating makeRating(User user){
        UserRating rating = new UserRating();
        rating.setUsername(user.getUsername());
        rating.setAvatar(getUserAvatar(user));
        return rating;
    }

    private UserRating makeWinnerRating(User user){
        if(user != null){
            UserRating rating = makeRating(user);
            rating.setRating(user.getStats().getGamesWin());
            return rating;
        }
        return null;
    }

    private UserRating makeGameTimeRating(User user){
        if(user != null){
            UserRating rating = makeRating(user);
            rating.setRating(user.getStats().getTotalGameTime());
            return rating;
        }
        return null;
    }

    public List<UserRating> getUserRating(String ratingType){
        if(ratingType != null){
            if(ratingType.equals("WINNERS")){
                return userRepository.findTopWinners(PageRequest.of(0, RATING_SIZE))
                    .stream().map(this::makeWinnerRating).collect(Collectors.toList());
            }else if(ratingType.equals("GAME_TIME")){
                return userRepository.findTopGameTime(PageRequest.of(0, RATING_SIZE))
                    .stream().map(this::makeGameTimeRating).collect(Collectors.toList());
            }
        }
        return null;
    }

    public void updateUser(User user){
        if(user != null){
            userRepository.save(user);
        }
    }

    public void joinGame(User user, Game game){
        if(user != null && game != null){
            user.setGameId(game.getId().toString());
            userRepository.save(user);
        }
    }

    public void leaveGame(User user, Game game){
        if(user != null && game != null) {
            String playerColor;

            if(user.equals(game.getWhitePlayer())){
                playerColor = "WHITE";
            }else if(user.equals(game.getBlackPlayer())){
                playerColor = "BLACK";
            }else{
                return;
            }

            if(game.getStatus() != null && game.getStatus().isFinished()) {
                if (user.getStats() != null) {
                    GameStatus status = game.getStatus();
                    if (status.getWinner().equals(playerColor)) {
                        user.getStats().addGamesWin();
                    } else if (status.getWinner().equals("DRAW")) {
                        user.getStats().addGamesDraw();
                    } else if (!status.getWinner().equals("UNKNOWN")) {
                        user.getStats().addGamesLose();
                    }
                    user.getStats().addGameTime(
                        (int) Duration.between(status.getStartTime(), status.getFinishTime())
                            .toSeconds());
                }
            }

            user.setGameId("");
            userRepository.save(user);
        }
    }
}
