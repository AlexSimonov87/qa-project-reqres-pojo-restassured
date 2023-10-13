package api;

import api.reqres.colors.Data;
import api.reqres.registration.Register;
import api.reqres.registration.SuccessUserReg;
import api.reqres.registration.UnSuccessUserReg;
import api.reqres.spec.Specifications;
import api.reqres.users.UserData;
import api.reqres.users.UserTime;
import api.reqres.users.UserTimeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresPojoTest {

    private static final String URL = "https://reqres.in/";

    /**
     1) GET - List Users
     Request : /api/users?page=2
     Response : 200
     - Получить список пользователей с page=2
     - Убедиться, что id пользователей содержаться в их avatar
     - Убедиться, что email пользователей имеет окончание reqres.in
     */
    @Test
    public void checkAvatarContainsIdTest() {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUnique(200));

        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        users.stream().forEach(x-> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));
        Assertions.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));

        }

    /**
     2) POST - Register - Successful
     Request : /api/register
     Response : 200
     - Тестирование успешной регистрации пользователя
     */
    @Test
    public void successUserRegTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUnique(200));
        Integer UserId = 4;
        String UserPassword = "QpwL5tke4Pnpja7X4";
        Register user = new Register("eve.holt@reqres.in","pistol");
        SuccessUserReg successReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessUserReg.class);
        Assertions.assertNotNull(successReg.getId());
        Assertions.assertNotNull(successReg.getToken());

        Assertions.assertEquals(UserId, successReg.getId());
        Assertions.assertEquals(UserPassword, successReg.getToken());
    }

    /**
     3) POST - Register - Unsuccessful
     Request : /api/register
     Response : 400
     - Тестирование неуспешной регистрации пользователя (не введен пароль)
     */
    @Test
    public void unSuccessUserRegTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUnique(400));
        Register peopleSecond = new Register("sydney@fife","");
        UnSuccessUserReg unSuccessReg = given()
        .body(peopleSecond)
                .when()
                .post("api/register")
                .then()
                .log().all()
                .extract().as(UnSuccessUserReg.class);
        Assertions.assertEquals("Missing password", unSuccessReg.getError());

    }

    /**
     4) GET - List RESOURCE
     Request : /api/unknown
     Response : 200
     - Убедиться, что операция LIST<RESOURCE> возвращает данные, отсортированные по годам.
     */
    @Test
    public void checkSortedYearsTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUnique(200));
        List<Data> data = given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", Data.class);

        List<Integer> dataYears = data.stream().map(Data::getYear).collect(Collectors.toList());
        List<Integer> sortedDataYears = dataYears.stream().sorted().collect(Collectors.toList());

        Assertions.assertEquals(sortedDataYears, dataYears);
    }

    /**
     5) DELETE - Delete
     Request : /api/users/2
     Response : 204
     - Удалить второго пользователя и проверить статус-код
     */
    @Test
    public void deleteUserTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUnique(204));
        given()
                .when()
                .delete("api/users/2")
                .then().log().all();
    }

    /**
     6) PUT - Update
     Request : api/users/2
     Response : 200
     - Сравнение текущего времени и времени создания пользователя из response,
     не считая последних 7 цифр.
     */
    @Test
    public void checkServerAndPcDateTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUnique(200));
        UserTime user = new UserTime("morpheus","zion resident");
        UserTimeResponse response = given()
                .body(user)
                .when()
                .put("api/users/2")
                .then().log().all()
                .extract().as(UserTimeResponse.class);

        String regex = "(.{7})$";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regex, "");

        Assertions.assertEquals(currentTime, response.getUpdatedAt().replaceAll(regex, ""));

    }
}
