CREATE TABLE IF NOT EXISTS users (
                 user_id SERIAL PRIMARY KEY,
                 user_name varchar(100) NOT NULL UNIQUE,
                 mail varchar(100) NOT NULL UNIQUE,
                 password varchar(100),
                 enabled boolean default false not null
                 );
ALTER TABLE users ADD COLUMN IF NOT EXISTS enabled boolean default false not null ;
CREATE TABLE IF NOT EXISTS verification_token(
                 token_id SERIAL PRIMARY KEY,
                 user_id integer NOT NULL unique,
                 token varchar NOT NULL,
                 exp_date timestamp NOT NULL,
                 FOREIGN KEY(user_id) REFERENCES users
                 );
CREATE TABLE IF NOT EXISTS frameworks (
                 framework_id SERIAL PRIMARY KEY,
                 framework_name varchar(50) NOT NULL,
                 category varchar(50) NOT NULL,
                 description varchar(500) NOT NULL,
                 introduction varchar(5000) NOT NULL,
                 logo varchar(150)
                 );
ALTER TABLE IF EXISTS  votes RENAME TO framework_votes;
CREATE TABLE IF NOT EXISTS framework_votes (
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
                 tstamp timestamp NOT NULL,
                 reference int,
                 FOREIGN KEY(framework_id) REFERENCES frameworks,
                 FOREIGN KEY(user_id) REFERENCES users,
                 FOREIGN KEY(reference) REFERENCES comments
                 );
ALTER TABLE comments DROP COLUMN IF EXISTS votes_up,DROP COLUMN IF EXISTS votes_down;
CREATE TABLE IF NOT EXISTS comment_votes (
                 vote_id SERIAL PRIMARY KEY,
                 user_id integer NOT NULL,
                 comment_id integer NOT NULL,
                 vote int,
                 FOREIGN KEY(comment_id) REFERENCES comments,
                 FOREIGN KEY(user_id) REFERENCES users
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
                 pending boolean NOT NULL DEFAULT false,
                 FOREIGN KEY(framework_id) REFERENCES frameworks,
                 FOREIGN KEY(user_id) REFERENCES users
                 );
ALTER TABLE content ADD COLUMN IF NOT EXISTS pending boolean default false not null ;