DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (datetime, description, calories, user_id) VALUES
  ('2018-10-28 09:02', 'Завтрак', 600, 100000),
  ('2018-10-28 14:10', 'Обед', 1000, 100000),
  ('2018-10-28 19:06', 'Ужин', 400, 100000),
  ('2018-10-28 13:13', 'Еда админа', 400, 100001),
  ('2018-10-28 21:01', 'Еда админа2', 1100, 100001),
  ('2018-10-29 09:05', 'Завтрак', 600, 100000),
  ('2018-10-29 14:27', 'Обед', 1000, 100000),
  ('2018-10-29 19:43', 'Ужин', 400, 100000),
  ('2018-10-29 10:11', 'Еда админа', 400, 100001),
  ('2018-10-29 18:55', 'Еда админа2', 1100, 100001);