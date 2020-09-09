CREATE TABLE IF NOT EXISTS users (
                 user_id SERIAL PRIMARY KEY,
                 user_name varchar(100) NOT NULL UNIQUE,
                 mail varchar(100) NOT NULL UNIQUE,
                 password varchar(100)
                 );
CREATE TABLE IF NOT EXISTS votes (
                 vote_id SERIAL PRIMARY KEY,
                 user_id integer NOT NULL,
                 framework_id integer NOT NULL,
                 stars integer NOT NULL,
                 FOREIGN KEY(framework_id) REFERENCES frameworks,
                 FOREIGN KEY(user_id) REFERENCES users
                 );
CREATE TABLE IF NOT EXISTS comments (
                 comment_id SERIAL PRIMARY KEY,
                 framework_id int NOT NULL,
                 user_id int NOT NULL,
                 description varchar(500) NOT NULL,
                 votes_up int,
                 votes_down int,
                 tstamp timestamp NOT NULL,
                 reference int,
                 FOREIGN KEY(framework_id) REFERENCES frameworks,
                 FOREIGN KEY(user_id) REFERENCES users,
                 FOREIGN KEY(reference) REFERENCES comments
                 );
CREATE TABLE IF NOT EXISTS content (
                 content_id SERIAL PRIMARY KEY,
                 framework_id int NOT NULL,
                 user_id int NOT NULL,
                 title varchar(100) NOT NULL,
                 votes_up int,
                 votes_down int,
                 tstamp timestamp NOT NULL,
                 link text NOT NULL,
                 type varchar(10) NOT NULL,
                 FOREIGN KEY(framework_id) REFERENCES frameworks,
                 FOREIGN KEY(user_id) REFERENCES users
                 );