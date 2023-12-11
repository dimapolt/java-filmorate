package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)

public class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    User user;
    UserDbStorage userStorage;

    @BeforeEach
    public void createData() {
        // Подготавливаем данные для теста
        user = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
                LocalDate.of(1990, 1, 1), new HashSet<>());
        userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.createUser(user);
    }


    /**
     * Тест метода getUserById и createUser
     */
    @Test
    public void testFindUserById() {
        User savedUser = userStorage.getUserById(1L);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()  // проверяем, что значения полей нового
                .isEqualTo(user); // и сохраненного пользователя - совпадают
    }

    /**
     * getAllUsers
     */
    @Test
    public void testGetAllUsers() {
        User secondUser = new User(2L, "user@email.ru", "ivanov88", "Ivan Ivanov",
                LocalDate.of(1988, 10, 1), new HashSet<>());
        userStorage.createUser(secondUser);

        List<User> users = userStorage.getAllUsers();
        assertEquals(2, users.size()); // Ожидаем, что вернётся 2 пользователя
        assertEquals(1, users.get(0).getId()); // id первого равен '1'
        assertEquals(2, users.get(1).getId()); // id второго равен '2'
    }

    /**
     * updateUser
     */
    @Test
    public void testUpdateUser() {
        User updateUser = userStorage.getUserById(1L); // Получаем пользователя из базы с id = 1

        updateUser.setName("Update name");
        userStorage.updateUser(updateUser); // Записываем в БД пользователя с изменённым именем

        User userFromBd = userStorage.getUserById(1L);

        assertEquals("Update name", userFromBd.getName()); // Сравниваем изменения
    }

    @Test
    public void testRemoveFriend() {
        Set<Long> friends = new HashSet<>();
        friends.add(1L);

        // Создаём пользователя с другом (id друга - 1)
        User secondUser = new User(2L, "user@email.ru", "ivanov88", "Ivan Ivanov",
                LocalDate.of(1988, 10, 1), friends);

        userStorage.createUser(secondUser);

        User userFromBd = userStorage.getUserById(2L);
        assertEquals(1L, userFromBd.getFriendsId().get(0)); // Проверяем, что id друга равен 1

        userStorage.removeFriend(2L, 1L); // Удаляем из списка друзей

        userFromBd = userStorage.getUserById(2L);
        assertEquals(0, userFromBd.getFriendsId().size()); // Проверяем, что список друзей пуст
    }

}
