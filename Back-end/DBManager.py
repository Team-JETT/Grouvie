import psycopg2

# Make a new Grouvie table to store all the plans
CREATE_GROUVIE = """
CREATE TABLE GROUVIE(
    USERNAME        CHAR(30)            NOT NULL,
    LEADER          CHAR(16)            NOT NULL,
    SHOWTIME        CHAR(16)            NOT NULL,
    FILM            TEXT,
    CINEMA          TEXT,
    LATITUDE        NUMERIC(8, 6),
    LONGITUDE       NUMERIC(9, 6),
    
    PRIMARY KEY (USERNAME, LEADER, SHOWTIME)
)
"""

# Make a new User table to store all user data
CREATE_USERS = """
CREATE TABLE USERS(
    USERNAME        CHAR(30)            NOT NULL,
    LATITUDE        NUMERIC(8, 6)       NOT NULL,
    LONGITUDE       NUMERIC(9, 6)       NOT NULL,
    
    PRIMARY KEY (USERNAME)
)
"""

# Delete a table
DROP_GROUVIE_TABLE = """
DROP TABLE GROUVIE
"""

# Delete a user table
DROP_USERS_TABLE = """
DROP TABLE USERS
"""

# Insert a new entry into the a table
INSERT_GROUVIE = """
INSERT INTO GROUVIE
VALUES
(%s, %s, %s, %s, %s, %s, %s)
"""

# Insert a new entry into the a table
INSERT_USERS = """
INSERT INTO USERS
VALUES
(%s, %s, %s)
"""


# Update an already existing entry in the Grouvie table
UPDATE_GROUVIE = """
UPDATE GROUVIE
SET SHOWTIME = %s, FILM = %s, CINEMA = %s, LATITUDE = %s, 
LONGITUDE = %s
WHERE
USERNAME = %s AND LEADER = %s
"""

# Update an already existing entry in the USER table
UPDATE_USERS = """
UPDATE USERS
SET LATITUDE = %s, LONGITUDE = %s
WHERE
USERNAME = %s
"""

# Delete entry from a table given a username, leader and showtime
DELETE_SINGLE = """
DELETE FROM GROUVIE
WHERE USERNAME = %s and LEADER = %s and SHOWTIME = %s
"""
# Delete entries from a table given a leader and showtime
DELETE_PLAN = """
DELETE FROM GROUVIE
WHERE LEADER = %s and SHOWTIME = %s
"""

# Display everything in the Grouvie table
SELECT_ALL_GROUVIE = """
SELECT * FROM GROUVIE
"""

# Display everything in the Grouvie table
SELECT_ALL_USERS = """
SELECT * FROM USERS
"""


# Select a single entry from the Grouvie table based on username
SELECT_GROUVIE = """
SELECT * FROM GROUVIE WHERE
USERNAME = %s
"""

# Select a single entry from the Grouvie table based on username
SELECT_USERS = """
SELECT * FROM USERS WHERE
USERNAME = %s
"""

GROUVIE = "GROUVIE"
USER = "USER"

class DBManager:

    # Establish a new connection with the PostgreSQL database.
    # We return the cursor so we can execute on the database, we return the
    # connection so we can close it when we're done.
    def establish_connection(self):
        conn_str = "dbname='g1627137_u' user='g1627137_u'" \
                   "host='db.doc.ic.ac.uk' password='Vk426n3Kjx'"
        try:
            cnxn = psycopg2.connect(conn_str)
            cnxn.autocommit = True
            cursor = cnxn.cursor()
            return cnxn, cursor
        except Exception as e:
            message = e.message + "\nFailed to establish connection. " \
                  "Check connection string."
            exit(message)

    # Close a connection to the database, kills the cursor and the connection.
    def close_connection(self, cnxn, cursor):
        try:
            cursor.close()
            cnxn.close()
        except Exception as e:
            message = e.message + "\nFailed to close connection."
            exit(message)

    # Make a new Grouvie table.
    def make_grouvie_table(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(CREATE_GROUVIE)
        self.close_connection(cnxn, cursor)

    # Make a new User table.
    def make_user_table(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(CREATE_USERS)
        self.close_connection(cnxn, cursor)

    # Delete a pre-existing table.
    def drop_grouvie_table(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DROP_GROUVIE_TABLE)
        self.close_connection(cnxn, cursor)

    # Delete a pre-existing table.
    def drop_user_table(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DROP_USERS_TABLE)
        self.close_connection(cnxn, cursor)

    # Insert a new entry into the Grouvie table.
    def insert_grouvie(self, username, leader, showtime, film, cinema,
                       latitude, longitude):
        cnxn, cursor = self.establish_connection()
        cursor.execute(INSERT_GROUVIE, (showtime, film, cinema, latitude,
                                        longitude, username, leader))
        self.close_connection(cnxn, cursor)

    def insert_user(self, username, latitude, longitude):
        cnxn, cursor = self.establish_connection()
        cursor.execute(INSERT_USERS, (username, latitude, longitude))
        self.close_connection(cnxn, cursor)

    # Update an entry in the Grouvie table if it exists.
    def update_grouvie(self, username, leader, showtime, film, cinema, latitude,
                       longitude):
        cnxn, cursor = self.establish_connection()
        cursor.execute(UPDATE_GROUVIE, (showtime, film, cinema, latitude,
                                        longitude, username, leader))
        self.close_connection(cnxn, cursor)

    # Update an entry in the USERS table if it exists.
    def update_users(self, username, latitude, longitude):
        cnxn, cursor = self.establish_connection()
        cursor.execute(UPDATE_USERS, (latitude, longitude, username))
        self.close_connection(cnxn, cursor)

    # Delete an entry from the table correlating with a user
    def delete_single_grouvie(self, username, leader, showtime):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DELETE_SINGLE, (username, leader, showtime))
        self.close_connection(cnxn, cursor)

    # Delete entries from the table correlating with a plan
    def delete_plan_grouvie(self, leader, showtime):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DELETE_PLAN, (leader, showtime))
        self.close_connection(cnxn, cursor)

    # Select an entry in a table based on username.
    def select_grouvie(self, username):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_GROUVIE, username)
        result = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return result

    # Select an entry in a table based on username.
    def select_users(self, username):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_USERS, username)
        result = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return result

    # Display everything in the Grouvie table.
    def select_all_grouvie(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_ALL_GROUVIE)
        result = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return result

    # Display everything in the Grouvie table.
    def select_all_users(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_ALL_USERS)
        result = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return result


if __name__ == '__main__':
    data = {'USERNAME': "1",
            'LEADER': 0,
            'SHOWTIME': "s",
            'FILM': "GOTG3",
            'CINEMA': "MEMES",
            'LATITUDE': 52.111100,
            'LONGITUDE': 21.211122}
    query = {'USERNAME': "1",
             'LEADER': 0,
             'SHOWTIME': "s"}
    db = DBManager()
    db.drop_user_table()
    db.make_user_table()
    db.insert_user("1", 2, 2)
    db.insert_user("2", 2, 2)
    db.insert_user("3", 2, 2)
    print db.select_users("1")
