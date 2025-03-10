document.addEventListener("DOMContentLoaded", function () {
    loadUsers(); // Загружаем пользователей при загрузке страницы

    const logoutButton = document.getElementById("logoutButton");

    if (logoutButton) {
        logoutButton.addEventListener("click", function (event) {
            fetch("/logout", { method: "POST" })  // Отправляем POST-запрос на logout
                .then(() => window.location.href = "/login?logout") // Перенаправляем
                .catch(err => console.error("Logout error:", err));
        });
    }
});


function loadUsers() {
    fetch("/api/admin")  // Запрос на получение всех пользователей через REST API
        .then(response => response.json())
        .then(users => {
            console.log("Полученные пользователи:", users);
            let tableBody = document.getElementById("userTable").getElementsByTagName('tbody')[0];
            tableBody.innerHTML = ""; // Очищаем таблицу

            users.forEach(user => {
                let rolesString = user.roles.map(role => role.name).join(", ");
                let row = `<tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.country}</td>
                    <td>${user.car}</td>
                    <td>${rolesString}</td>
                    <td>${user.password}</td>
                    <td>
                        <button onclick="openUpdateModal(${user.id})">Update</button>
                    </td>
                    <td>
                        <button onclick="deleteUser(${user.id})">Delete</button>
                    </td>
                </tr>`;
                tableBody.innerHTML += row; // Добавляем строку в таблицу
            });
        })
        .catch(error => console.error("Ошибка загрузки пользователей:", error));
}

// Функция для открытия модального окна для обновления
function openUpdateModal(userId) {
    fetch(`/api/update/${userId}`)  // Получаем данные конкретного пользователя и доступные роли
        .then(response => response.json())
        .then(data => {
            // Заполняем данные в форме модального окна
            document.getElementById("UpdateUsername").value = data.user.username;
            document.getElementById("UpdateCountry").value = data.user.country;
            document.getElementById("UpdateCar").value = data.user.car;
            document.getElementById("UpdatePassword").value = "";

            let roleSelect = document.getElementById("roleSelect");
            roleSelect.innerHTML = '';  // Очищаем текущие опции

            // Добавляем все доступные роли
            data.roles.forEach(role => {
                let option = document.createElement("option");
                option.value = role.id;  // Привязываем id роли к значению option
                option.text = role.name.replace("ROLE_", "");  // Название роли (например, "ADMIN", "USER")
                roleSelect.appendChild(option);  // Добавляем опцию в select
            });

            // Устанавливаем выбранные роли
            data.user.roles.forEach(role => {
                let option = roleSelect.querySelector(`option[value='${role.id}']`);
                if (option) {
                    option.selected = true;  // Помечаем роль как выбранную
                }
            });

            // Сохраняем userId в data-id
            document.getElementById("UpdateUsername").setAttribute("data-id", data.user.id);

            // Открываем модальное окно с помощью Bootstrap
            const modal = new bootstrap.Modal(document.getElementById("updateModal"));
            modal.show();
        })
        .catch(error => console.error('Ошибка при загрузке данных пользователя:', error));
}




function updateUser() {
    const userId = document.getElementById("UpdateUsername").getAttribute("data-id");
    console.log("userId:", userId);  // Логируем userId

    if (!userId) {
        console.error("Ошибка: ID пользователя не найден!");
        return; // Прерываем выполнение, если ID не найден
    }

    // Извлекаем данные из формы в модальном окне
    const username = document.getElementById("UpdateUsername").value;
    const country = document.getElementById("UpdateCountry").value;
    const car = document.getElementById("UpdateCar").value;
    const password = document.getElementById("UpdatePassword").value;

    // Получаем выбранные роли (по значениям ID)
    const roleMapping = { 2: "ROLE_ADMIN", 1: "ROLE_USER" }; // Сопоставляем ID ролей с их именами
    const roleSelect = document.getElementById("roleSelect");
    const roles = Array.from(roleSelect.selectedOptions).map(option => {
        const roleId = parseInt(option.value); // Получаем ID из value
        return { id: roleId, name: roleMapping[roleId] }; // Маппируем ID в объект с name
    });

    const updatedUser = {
        username: username,
        country: country,
        car: car,
        roles: roles,  // Передаем массив выбранных ролей
        password: password
    };
    console.log("Roles:", roles);
    // Логируем обновленные данные для дебага
    console.log("Updated user data:", updatedUser);

    // Отправка запроса на обновление данных пользователя
    fetch('/api/admin/update/' + userId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedUser)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || 'Error occurred');
                });
            }
            // Получаем ответ как текст
            return response.text();
        })
        .then(data => {
            // data теперь это строка, например, "Пользователь успешно обновлён!"
            console.log('Response:', data);

            alert(data);  // Покажем сообщение о том, что пользователь успешно обновлён

            loadUsers();  // Загружаем список пользователей заново
            // Закрываем модальное окно (если используем Bootstrap)
            const modal = document.getElementById('updateModal');  // ID вашего модального окна
            const modalInstance = bootstrap.Modal.getInstance(modal); // Получаем экземпляр модального окна
            modalInstance.hide();  // Закрываем модальное окно
        })
        .catch(error => {
            console.error('Error:', error);
            alert('There was an error updating the user: ' + error.message);
        });
}
document.getElementById("createUserBtn").addEventListener("click", createUser);














// Функция добавления
function createUser() {
    // Получаем значения из формы
    const username = document.getElementById("newUsername").value;
    const country = document.getElementById("newCountry").value;
    const car = document.getElementById("newCar").value;
    const password = document.getElementById("newPassword").value;

    // Получаем выбранные роли
    const roleSelect = document.getElementById('roleNew');
    const roles = Array.from(roleSelect.selectedOptions).map(option => {
        return { id: parseInt(option.value, 10), name: option.text }; // Преобразуем в объект с id и name
    });
    // Создаем объект нового пользователя
    const newUser = {
        username: username,
        country: country,
        car: car,
        roles: roles,  // Передаем массив выбранных ролей
        password: password
    };

    // Логируем данные для дебага
    console.log("New user data:", newUser);

    // Отправка запроса на создание нового пользователя
    fetch('/api/admin/new', {  // Убедитесь, что путь на сервере верный
        method: 'POST',  // Обратите внимание на правильный регистр HTTP метода
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newUser)  // Отправляем данные нового пользователя
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || 'Error occurred');
                });
            }
            // Получаем ответ как текст (например, "Пользователь успешно создан!")
            return response.text();
        })
        .then(data => {
            // data теперь это строка, например, "Пользователь успешно создан!"
            console.log('Response:', data);

            alert(data);  // Покажем сообщение о том, что пользователь успешно создан

            loadUsers();  // Загружаем список пользователей заново (если такая функция существует)
         //   window.location.href = '/admin';
            // Закрываем модальное окно (если используем Bootstrap)
            const modal = document.getElementById('newUser');  // ID вашего модального окна
            if (modal) {
                const modalInstance = bootstrap.Modal.getInstance(modal); // Получаем экземпляр модального окна
                modalInstance.hide();  // Закрываем модальное окно
            } else {
                console.error('Modal element with ID "newUser" not found!');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('There was an error creating the user: ' + error.message);
        });
}



















// Функция для открытия модального окна с данными пользователя
function openDeleteModal(userId) {
    // Выполняем запрос на получение данных пользователя
    fetch(`/api/admin/${userId}`)
        .then(response => response.json())
        .then(user => {
            // Заполняем данные в модальном окне
            document.getElementById("DeleteUsername").value = user.username;
            document.getElementById("DeleteCountry").value = user.country;
            document.getElementById("DeleteCar").value = user.car;
            document.getElementById("DeleteRole").value = user.roles.map(role => role.name).join(", ");
            document.getElementById("DeletePassword").value = "password is hidden"; // Пароль скрыт

            // Заполняем скрытое поле id для отправки при удалении
            document.querySelector("#deleteModal-" + userId + " form input[name='id']").value = user.id;

            // Получаем экземпляр модального окна по ID
            const modalElement = document.getElementById(`deleteModal-${userId}`);
            const modalInstance = new bootstrap.Modal(modalElement); // Получаем экземпляр модального окна

            // Открываем модальное окно
            modalInstance.show();
        })
        .catch(error => {
            console.error("Error fetching user data:", error);
            alert("Error fetching user data");
        });
}


// Функция для удаления пользователя
function deleteUser(userId) {
    if (!confirm("Are you sure you want to delete this user?")) return;

    fetch(`/api/admin/delete/${userId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.text().then(text => ({ status: response.status, body: text })))
        .then(({ status, body }) => {
            if (status === 200) {
                location.reload();
            } else {
                throw new Error(`Error ${status}: ${body}`);
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("An error occurred: " + error.message);
        });
}
