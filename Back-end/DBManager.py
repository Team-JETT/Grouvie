import psycopg2

# Make a new Grouvie table to store all the plans
CREATE_GROUVIE = """
CREATE TABLE GROUVIE(
    PHONE_NUMBER    CHAR(11)            NOT NULL,
    LEADER          CHAR(16)            NOT NULL,
    SHOWTIME        CHAR(16)            NOT NULL,
    FILM            TEXT,
    CINEMA          TEXT,
    LATITUDE        NUMERIC(8, 6),
    LONGITUDE       NUMERIC(9, 6),
    
    PRIMARY KEY (PHONE_NUMBER, LEADER, SHOWTIME)
)
"""

# Make a new User table to store all user data
CREATE_USERS = """
CREATE TABLE USERS(
    PHONE_NUMBER    CHAR(11)            NOT NULL,
    NAME            CHAR(16)            NOT NULL,
    LATITUDE        NUMERIC(8, 6)       NOT NULL,
    LONGITUDE       NUMERIC(9, 6)       NOT NULL,
    
    PRIMARY KEY (PHONE_NUMBER)
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
(%s, %s, %s, %s)
"""


# Update an already existing entry in the Grouvie table
UPDATE_GROUVIE = """
UPDATE GROUVIE
SET SHOWTIME = %s, FILM = %s, CINEMA = %s, LATITUDE = %s, 
LONGITUDE = %s
WHERE
PHONE_NUMBER = %s AND LEADER = %s
"""

# Update an already existing entry in the USER table
UPDATE_USERS = """
UPDATE USERS
SET NAME = %s, LATITUDE = %s, LONGITUDE = %s
WHERE
PHONE_NUMBER = %s
"""

# Update a users preference of film
CHANGE_FILM = """
UPDATE GROUVIE
SET FILM = %s
WHERE
PHONE_NUMBER = %s and LEADER = %s and SHOWTIME = %s
"""

# Update a users preference of cinema
CHANGE_CINEMA = """
UPDATE GROUVIE
SET CINEMA = %s
WHERE
PHONE_NUMBER = %s and LEADER = %s and SHOWTIME = %s
"""

# Update a users preference of showtime
CHANGE_SHOWTIME = """
UPDATE GROUVIE
SET SHOWTIME = %s
WHERE
PHONE_NUMBER = %s and LEADER = %s and SHOWTIME = %s
"""

# Delete entry from a table given a phone_number, leader and showtime
DELETE_SINGLE = """
DELETE FROM GROUVIE
WHERE PHONE_NUMBER = %s and LEADER = %s and SHOWTIME = %s
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


# Select a single entry from the Grouvie table based on phone_number
SELECT_GROUVIE = """
SELECT * FROM GROUVIE
WHERE
PHONE_NUMBER = %s
"""

# Select a single entry from the Grouvie table based on phone_number
SELECT_USERS = """
SELECT * FROM USERS
WHERE
PHONE_NUMBER = %s
"""

SELECT_VALID_USERS = """
SELECT PHONE_NUMBER, NAME FROM USERS
WHERE
PHONE_NUMBER IN {}
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

    # Make a new Users table.
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
    def insert_grouvie(self, phone_number, leader, showtime, film, cinema,
                       latitude, longitude):
        cnxn, cursor = self.establish_connection()
        cursor.execute(INSERT_GROUVIE, (showtime, film, cinema, latitude,
                                        longitude, phone_number, leader))
        self.close_connection(cnxn, cursor)

    def insert_user(self, phone_number, name, latitude, longitude):
        cnxn, cursor = self.establish_connection()
        cursor.execute(INSERT_USERS, (phone_number, name, latitude, longitude))
        self.close_connection(cnxn, cursor)

    def change_film(self, phone_number, leader, showtime, film):
        cnxn, cursor = self.establish_connection()
        cursor.execute(CHANGE_FILM, (film, phone_number, leader, showtime))
        self.close_connection(cnxn, cursor)

    def change_cinema(self, phone_number, leader, showtime, cinema):
        cnxn, cursor = self.establish_connection()
        cursor.execute(CHANGE_CINEMA, (cinema, phone_number, leader, showtime))
        self.close_connection(cnxn, cursor)

    def change_showtime(self, phone_number, leader, showtime, new_showtime):
        cnxn, cursor = self.establish_connection()
        cursor.execute(CHANGE_SHOWTIME, (new_showtime, phone_number, leader,
                                         showtime))
        self.close_connection(cnxn, cursor)

    # Update an entry in the Grouvie table if it exists.
    def update_grouvie(self, phone_number, leader, showtime, film, cinema,
                       latitude,
                       longitude):
        cnxn, cursor = self.establish_connection()
        cursor.execute(UPDATE_GROUVIE, (showtime, film, cinema, latitude,
                                        longitude, phone_number, leader))
        self.close_connection(cnxn, cursor)

    # Update an entry in the USERS table if it exists.
    def update_users(self, phone_number, name, latitude, longitude):
        cnxn, cursor = self.establish_connection()
        cursor.execute(UPDATE_USERS, (name, latitude, longitude, phone_number))
        self.close_connection(cnxn, cursor)

    # Delete an entry from the table correlating with a user
    def delete_single_grouvie(self, phone_number, leader, showtime):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DELETE_SINGLE, (phone_number, leader, showtime))
        self.close_connection(cnxn, cursor)

    # Delete entries from the table correlating with a plan
    def delete_plan_grouvie(self, leader, showtime):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DELETE_PLAN, (leader, showtime))
        self.close_connection(cnxn, cursor)

    # Select an entry in a table based on phone_number.
    def select_grouvie(self, phone_number):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_GROUVIE, phone_number)
        result = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return result

    # Select an entry in a table based on phone_number.
    def select_users(self, phone_number):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_USERS, phone_number)
        result = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        # There should only be 1 result so we just return that tuple.
        return result[0] if result else []

    # Select users that actually have a Grouvie account.
    def select_valid_users(self, friends):
        # Build the placeholders which we require when it comes to searching.
        fields = "(" + ','.join(["%s"]*len(friends)) + ")"
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_VALID_USERS.format(fields), tuple(friends))
        results = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return results

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
    data = {'PHONE_NUMBER': "1",
            'LEADER': 0,
            'SHOWTIME': "s",
            'FILM': "GOTG3",
            'CINEMA': "MEMES",
            'LATITUDE': 52.111100,
            'LONGITUDE': 21.211122}
    query = {'PHONE_NUMBER': "1",
             'LEADER': 0,
             'SHOWTIME': "s"}
    db = DBManager()
    # db.drop_user_table()
    # db.make_user_table()
    # db.insert_user("07587247113", "Erkin", 0, 0)
    # db.insert_user("07964006128", "Tarun", 0, 0)
    # db.insert_user("07942948248", "Jay", 0, 0)
    # db.insert_user("4", "1", 0, 0)
    # print db.select_valid_users(("1", "2", "5", "6"))
    print db.select_all_grouvie()
    print db.select_all_users()
