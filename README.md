Стек:
- Java 8
- rest-assured (5.3.2)
- junit-jupiter-engine (5.10.0)
- jackson-databind (2.15.2)
- maven (3.9.3)

____

Автотесты в классе ReqresPojoTest
Проект - тестовый фреймворк с использованием Pojo классов и библиотеки RestAssured.
____

Автотесты для проверки запросов через api ресурса https://reqres.in/


Автотесты:

**1) GET - List Users**     
Request : /api/users?page=2       
Response : 200      
- Получить список пользователей с page=2 
- Убедиться, что id пользователей содержаться в их avatar
- Убедиться, что email пользователей имеет окончание reqres.in

**2) POST - Register - Successful**      
Request : /api/register        
Response : 200    
- Тестирование успешной регистрации пользователя  

**3) POST - Register - Unsuccessful**        
Request : /api/register           
Response : 400    
- Тестирование неуспешной регистрации пользователя (не введен пароль)

**4) GET - List RESOURCE**      
Request : /api/unknown        
Response : 200    
- Убедиться, что операция LIST<RESOURCE> возвращает данные, отсортированные по годам.

**5) DELETE - Delete**      
Request : /api/users/2        
Response : 204    
- Удалить второго пользователя и проверить статус-код


**6) PUT - Update**  
Request : api/users/2    
Response : 200  
- Сравнение текущего времени и времени создания пользователя из response, не считая последних 7 цифр.
   
____

