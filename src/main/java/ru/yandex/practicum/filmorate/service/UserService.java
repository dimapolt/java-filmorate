package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final EventStorage eventStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("eventDbStorage") EventStorage eventStorage) {
        this.userStorage = userStorage;
        this.eventStorage = eventStorage;
    }

    public ResponseEntity<User> createUser(User user) {
        return userStorage.createUser(user);
    }

    public ResponseEntity<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public String deleteUser(Long userId) {
        userStorage.deleteUser(userId);

        return "Пользователь с id=" + userId + " успешно удален!";
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public String addFriend(Long userId, Long friendId) {

        User user = userStorage.getUserById(userId);
        userStorage.getUserById(friendId); // Проверка наличия "друга" в базе

        user.addFriend(friendId);
        userStorage.updateUser(user);
        eventStorage.createEvent(Event.EntityType.FRIEND, friendId, Event.EventOperationType.ADD, userId);

        String message = "Пользователь с id=" + friendId + " добавлен в друзья";
        log.info(message);
        return message;
    }

    public String removeFriend(Long userId, Long friendId) {
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);

        userStorage.removeFriend(userId, friendId);
        eventStorage.createEvent(Event.EntityType.FRIEND, friendId, Event.EventOperationType.REMOVE, userId);

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

    public List<Film> returnRecommendedFilms(Long id) {
        userStorage.getUserById(id);

        return userStorage.getRecommendedFilms(id);
    }

}
