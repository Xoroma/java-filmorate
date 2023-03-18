
CREATE TABLE IF NOT EXISTS mpaRating (
    mpa_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    mpa_name varchar(255),
    FOREIGN KEY (mpa_id) REFERENCES films (mpaRating_id)
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id INT   GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name varchar(255)
);



CREATE TABLE IF NOT EXISTS films (
   film_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   film_name varchar(255) NOT NULL,
   description varchar(200),
   release_date DATE  NOT NULL,
   duration INT NOT NULL,
   rate INT NOT NULL,
   mpaRating_id INT REFERENCES mpaRating (mpa_id),
  CONSTRAINT IF NOT EXISTS not_blank CHECK (LENGTH (film_name) > 0),
   CONSTRAINT IF NOT EXISTS LIMIT_BIRTHDAY__OF_FILM CHECK (release_date >= CAST('1895-12-28' AS DATE)),
   CONSTRAINT IF NOT EXISTS positive CHECK (duration > 0)
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id INT  REFERENCES films (film_id) ON DELETE CASCADE,
    genre_id INT REFERENCES genres (genre_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);



CREATE TABLE IF NOT EXISTS users
(
    user_id   INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL,
    login    VARCHAR(255) NOT NULL,
    birthday  DATE    NOT NULL,
    CONSTRAINT IF NOT EXISTS not_blank CHECK (LENGTH(email) > 0 AND LENGTH(login) > 0 AND LENGTH(user_name) > 0),
    CONSTRAINT IF NOT EXISTS earliest_date_prod CHECK (birthday < CURRENT_DATE)
);


CREATE TABLE IF NOT EXISTS friendship(
    from_user INT REFERENCES users (user_id) ON DELETE CASCADE,
    to_user   INT REFERENCES users (user_id) ON DELETE CASCADE,
    PRIMARY KEY (from_user, to_user)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id      INT REFERENCES films (film_id) ON DELETE CASCADE,
    from_user_id INT REFERENCES users (user_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, from_user_id)
);