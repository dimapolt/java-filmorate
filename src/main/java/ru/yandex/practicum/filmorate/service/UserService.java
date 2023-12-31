package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exceptions.NoDataFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utils.FilmRateValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public ResponseEntity<User> createUser(User user) {
        return userStorage.createUser(user);
    }

    public ResponseEntity<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        User user = userStorage.getUserById(id);
        FilmRateValidator.checkOnNull(user, "Пользователь с id=" + id + " не найден!");

        return user;
    }

    public String addFriend(Long userId, Long friendId) {

        User user = getUserIfExist(userId);
        getUserIfExist(friendId); // Проверка наличия "друга" в базе

        user.addFriend(friendId);
        userStorage.updateUser(user);

        String message = "Пользователь с id=" + friendId + " добавлен в друзья";
        log.info(message);
        return message;
    }

    public String removeFriend(Long userId, Long friendId) {
        User user = getUserIfExist(userId);
        userStorage.removeFriend(userId, friendId);

        String message = "Пользователь с id=" + friendId + " удалён из друзей";
        log.info(message);
        return message;
    }

    public List<User> getUserFriends(Long id) {
        List<Long> friendsId = userStorage.getUserById(id).getFriendsId();
        List<User> result = new ArrayList<>();

        friendsId.forEach(uId -> result.add(userStorage.getUserById(uId)));

        return result;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> commonFriends = new ArrayList<>();

        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);
        List<Long> userFriendsId = user.getFriendsId();
        List<Long> otherUserFriendsId = otherUser.getFriendsId();

        userFriendsId.retainAll(otherUserFriendsId);
        userFriendsId.forEach(uId -> commonFriends.add(userStorage.getUserById(uId)));

        return commonFriends;
    }

    private User getUserIfExist(Long id) {
        Optional<User> userO = Optional.ofNullable(userStorage.getUserById(id));

        if (userO.isEmpty()) {
            log.warn("Пользователь с id=" + id + " не найден!");
            throw new NoDataFoundException("Пользователь с id=" + id + " не найден!");
        } else {
            return userO.get();
        }
    }

}
